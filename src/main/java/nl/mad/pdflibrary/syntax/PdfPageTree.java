package nl.mad.pdflibrary.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.model.PdfNameValue;

/**
 * Represents the page tree of a PDF document. Page tree's contain pages or other page tree's.
 * Pages are divided in page tree's to quicken the lookup process.
 * 
 * @author Dylan de Wolff
 */
public class PdfPageTree extends PdfDictionary {
    private List<PdfIndirectObject> kids;

    /**
     * Creates a new instance of PdfPageTree.
     */
    public PdfPageTree() {
        super(PdfObjectType.PAGETREE);
        kids = new ArrayList<PdfIndirectObject>();
        initPageTree();
    }

    /**
     * Initialize the page tree by setting the type value.
     */
    private void initPageTree() {
        this.put(PdfNameValue.TYPE, PdfNameValue.PAGES);
    }

    /**
     * Returns the total size of the page tree.
     * @return size of page tree.
     */
    public int getSize() {
        int size = 1;
        for (PdfIndirectObject kid : kids) {
            PdfObjectType type = kid.getObject().getType();
            if (type.equals(PdfObjectType.PAGETREE)) {
                size += ((PdfPageTree) kid.getObject()).getSize();
            } else {
                ++size;
            }
        }
        return size;
    }

    /**
     * Returns all the objects in the page tree.
     * @return all objects in the page tree.
     */
    public List<PdfIndirectObject> getPageTreeObjects() {
        List<PdfIndirectObject> objects = new ArrayList<PdfIndirectObject>();
        for (PdfIndirectObject kid : kids) {
            PdfObjectType type = kid.getObject().getType();
            objects.add(kid);
            if (type.equals(PdfObjectType.PAGETREE)) {
                objects.addAll(((PdfPageTree) kid.getObject()).getPageTreeObjects());
            }
        }
        return objects;
    }

    /**
     * Adds the given indirect object as a child.
     * @param indirectPage Child to be added.
     */
    public void add(PdfIndirectObject indirectPage) {
        kids.add(indirectPage);
    }

    /**
     * Adds the references to all the pages/page tree's inside this object to the dictionary.
     */
    private void setKidReferences() {
        PdfArray kidsReferenceArray = new PdfArray();
        for (PdfIndirectObject kid : kids) {
            kidsReferenceArray.addValue(kid.getReference());
        }
        this.put(PdfNameValue.KIDS, kidsReferenceArray);
        this.put(PdfNameValue.COUNT, new PdfNumber(kidsReferenceArray.getSize()));
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        setKidReferences();
        super.writeToFile(os);
    }
}
