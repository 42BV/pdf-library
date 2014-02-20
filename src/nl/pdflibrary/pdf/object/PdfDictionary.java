package nl.pdflibrary.pdf.object;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * PdfDictionary represents the dictionary object in PDF's. It functions the same as a Java dictionary
 * and is used to store data for all kinds of different elements such as fonts and pages.
 * 
 * @author Dylan de Wolff
 */
public class PdfDictionary extends AbstractPdfObject {
    private HashMap<PdfName, AbstractPdfObject> content;
    private static final byte[] OPEN_DICTIONARY = "<<\n".getBytes();
    private static final byte[] CLOSE_DICTIONARY = ">>".getBytes();

    /**
     * Creates a new instance of PdfDictionary
     * @param type Specifies the type of dictionary
     */
    public PdfDictionary(PdfObjectType type) {
        super(type);
        content = new HashMap<PdfName, AbstractPdfObject>();
    }

    /**
     * Writes the dictionary to the given OutputStream
     * 
     * @param os OutputStream
     * @see nl.pdflibrary.pdf.object.AbstractPdfObject#writeToFile(java.io.OutputStream) 
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        os.write(OPEN_DICTIONARY);
        for (Entry<PdfName, AbstractPdfObject> entry : content.entrySet()) {
            entry.getKey().writeToFile(os);
            os.write((byte) ' ');
            entry.getValue().writeToFile(os);
            os.write(System.lineSeparator().getBytes());
        }
        os.write(CLOSE_DICTIONARY);
    }

    public void put(PdfName key, AbstractPdfObject value) {
        this.content.put(key, value);
    }

    public AbstractPdfObject get(PdfName key) {
        return this.content.get(key);
    }

}
