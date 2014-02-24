package nl.mad.pdflibrary.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.model.PdfNameValue;
import nl.mad.pdflibrary.utility.ByteEncoder;

/**
 * Represents the page tree of a PDF api. Page tree's contain pages or other page tree's.
 * Pages are divided in page tree's to quicken the lookup process.
 * 
 * @author Dylan de Wolff
 */
public class PdfPageTree extends PdfIndirectObject {
    private List<PdfIndirectObject> kids;

    /**
     * Creates a new instance of PdfPageTree.
     * 
     * @param number Number of object.
     * @param generation Generation of object.
     * @param pages Object this refers to.
     * @param inUse Whether the object is used in the api.
     */
    public PdfPageTree(int number, int generation, PdfDictionary pages, boolean inUse) {
        super(number, generation, pages, inUse);
        kids = new ArrayList<PdfIndirectObject>();
        initPageTree();
    }

    /**
     * Initialize the page tree by setting the type value.
     */
    private void initPageTree() {
        PdfDictionary pages = (PdfDictionary) this.getObject();
        pages.put(new PdfName(PdfNameValue.TYPE), new PdfName("Pages"));
    }

    /**
     * Returns the total size of the page tree.
     * @return size of page tree.
     */
    public int getSize() {
        int size = 1;
        for (PdfIndirectObject kid : kids) {
            PdfObjectType type = kid.getObject().getType();
            if (type.equals(PdfObjectType.PAGE)) {
                ++size;
            } else if (kid instanceof PdfPageTree) {
                size += ((PdfPageTree) kid).getSize();
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
        objects.add(this);
        for (PdfIndirectObject kid : kids) {
            PdfObjectType type = kid.getObject().getType();
            if (type.equals(PdfObjectType.PAGE)) {
                objects.add(kid);
            } else if (kid instanceof PdfPageTree) {
                objects.addAll(((PdfPageTree) kid).getPageTreeObjects());
            }
        }
        return objects;
    }

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
        PdfDictionary dictionary = (PdfDictionary) this.getObject();
        dictionary.put(new PdfName("Kids"), kidsReferenceArray);
        dictionary.put(new PdfName("Count"), new PdfNumber(kidsReferenceArray.getSize()));
    }

    /**
     * Writes the object to the given OutputStream.
     * 
     * @param os OutputStream to write to.
     * @throws IOException
     * @see nl.mad.pdflibrary.syntax.PdfIndirectObject#writeToFile(java.io.OutputStream)
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        setKidReferences();
        String objectLine = getNumber() + " " + getGeneration() + " " + START;
        os.write(ByteEncoder.getBytes(objectLine));
        this.getObject().writeToFile(os);
        os.write(ByteEncoder.getBytes(END));
    }
}
