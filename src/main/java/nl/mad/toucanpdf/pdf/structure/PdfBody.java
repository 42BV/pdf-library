package nl.mad.toucanpdf.pdf.structure;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.mad.toucanpdf.model.PdfNameValue;
import nl.mad.toucanpdf.pdf.syntax.AbstractPdfObject;
import nl.mad.toucanpdf.pdf.syntax.PdfDictionary;
import nl.mad.toucanpdf.pdf.syntax.PdfIndirectObject;
import nl.mad.toucanpdf.pdf.syntax.PdfIndirectObjectReference;
import nl.mad.toucanpdf.pdf.syntax.PdfObjectType;
import nl.mad.toucanpdf.pdf.syntax.PdfPage;
import nl.mad.toucanpdf.pdf.syntax.PdfPageTree;

/** 
 * Represents the body section of a PDF file. Responsible for creating indirect objects and storing all 
 * the indirect PDF objects, including the catalog and page tree. 
 * 
 * @author Dylan de Wolff
 * @see PdfIndirectObject
 * @see PdfDocument
 */
public class PdfBody {
    private List<PdfIndirectObject> indirectObjects;
    /**
     * Represents the catalog object of the body, stored outside of the general object list to allow for easier updating.
     */
    private PdfIndirectObject catalog;
    /**
     * Represents the page tree of PDF documents, stored outside of the general object list to allow for easier updating.
     */
    private PdfIndirectObject pageTree;
    /**
     * Represents the offset caused by storing the catalog and pagetree separately.
     */
    private static final int OBJECT_NUMBER_OFFSET = 1;

    /**
     * Creates a new instance of the PdfBody. This will also result in the creation of the page tree and catalog.
     */
    public PdfBody() {
        indirectObjects = new ArrayList<PdfIndirectObject>();
        pageTree = createPageTree();
        catalog = createCatalog(pageTree.getReference());
    }

    /**
     * Used to add a PdfObject to the body, automatically creates an indirect object representation for the given PdfObject.
     *
     * @param object The PdfObject that will be added to the body.
     * @return The indirect object created with the PdfObject.
     */
    public PdfIndirectObject addObject(AbstractPdfObject object) {
        PdfIndirectObject indirectObject = this.createIndirectObject(object);
        this.indirectObjects.add(indirectObject);
        return indirectObject;
    }

    private PdfIndirectObject createIndirectObject(int number, AbstractPdfObject object) {
        PdfIndirectObject indirectObject = new PdfIndirectObject(number, 0, object, true);
        return indirectObject;
    }

    private PdfIndirectObject createIndirectObject(AbstractPdfObject object) {
        return createIndirectObject(getTotalIndirectObjectsAmount() + 1, object);
    }

    /**
     * Adds a page object to the body. Also adds the page to the page tree.
     * 
     * @param page The page object that will be added.
     * @return The indirect object created with the Page.
     */
    public PdfIndirectObject addPage(PdfPage page) {
        PdfIndirectObject indirectPage = this.createIndirectObject(page);
        getPageTreeObject().add(indirectPage);
        page.put(PdfNameValue.PARENT, pageTree.getReference());
        return indirectPage;
    }

    /**
     * Writes all the indirect objects stored in the body to the given OutputStream. Also sets the starting byte of
     * the indirect objects. This is needed for the creation of the cross reference table.
     * 
     * @param os The data output stream that will be written to.
     * @throws IOException throws IOException in case the writing went wrong.
     */
    public void writeToFile(DataOutputStream os) throws IOException {
        for (PdfIndirectObject object : getAllIndirectObjects()) {
            object.setStartByte(os.size());
            object.writeToFile(os);
        }
    }

    public List<PdfIndirectObject> getIndirectObjects() {
        return this.indirectObjects;
    }

    /**
     * Returns the number of indirect objects contained in the body, including the separately stored catalog, pageTree and page objects.
     * @return the number of objects
     */
    public final int getTotalIndirectObjectsAmount() {
        if (indirectObjects != null && pageTree != null) {
            return this.indirectObjects.size() + getPageTreeObject().getSize() + PdfBody.OBJECT_NUMBER_OFFSET;
        } else {
            return PdfBody.OBJECT_NUMBER_OFFSET;
        }
    }

    /**
     * Creates the catalog object. The catalog forms the root of the PDF file and refers to the first page tree of the api.
     * 
     * @param pages The first page node that the catalog should refer to.
     * @return The indirect object for the catalog.
     */
    private PdfIndirectObject createCatalog(PdfIndirectObjectReference pageTreeReference) {
        PdfDictionary catalogDictionary = new PdfDictionary(PdfObjectType.CATALOG);
        PdfIndirectObject indirectCatalog = createIndirectObject(1, catalogDictionary);
        catalogDictionary.put(PdfNameValue.TYPE, PdfNameValue.CATALOG);
        catalogDictionary.put(PdfNameValue.PAGES, pageTreeReference);
        return indirectCatalog;
    }

    private PdfPageTree getPageTreeObject() {
        return ((PdfPageTree) pageTree.getObject());
    }

    /**
     * Creates a new page tree.
     * 
     * @return The newly made page tree object inside of an IndirectObject.
     */
    private PdfIndirectObject createPageTree() {
        PdfPageTree pageTreeObj = new PdfPageTree();
        return this.createIndirectObject(pageTreeObj);
    }

    public PdfIndirectObject getCatalog() {
        return this.catalog;
    }

    public PdfIndirectObjectReference getCatalogReference() {
        return this.catalog.getReference();
    }

    /**
     * Returns an list containing all indirect objects contained in the body. This includes the separately stored 
     * catalog and page tree objects.
     * @return an list containing all indirect objects.
     */
    public final List<PdfIndirectObject> getAllIndirectObjects() {
        List<PdfIndirectObject> allIndirectObjects = new ArrayList<PdfIndirectObject>();
        allIndirectObjects.add(catalog);
        allIndirectObjects.add(pageTree);
        allIndirectObjects.addAll(getPageTreeObject().getPageTreeObjects());
        allIndirectObjects.addAll(this.indirectObjects);
        return allIndirectObjects;
    }
}
