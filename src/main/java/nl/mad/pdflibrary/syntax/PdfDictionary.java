package nl.mad.pdflibrary.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.mad.pdflibrary.utility.ByteEncoder;
import nl.mad.pdflibrary.utility.PdfConstants;

/**
 * PdfDictionary represents the dictionary object in PDF's. It functions the same as a Java dictionary
 * and is used to store data for all kinds of different elements such as fonts and pages.
 * 
 * @author Dylan de Wolff
 */
public class PdfDictionary extends AbstractPdfObject {
    private Map<PdfName, AbstractPdfObject> content;
    private static final String OPEN_DICTIONARY = "<<\n";
    private static final String CLOSE_DICTIONARY = ">>";

    /**
     * Creates a new instance of PdfDictionary.
     * @param type Specifies the type of dictionary.
     */
    public PdfDictionary(PdfObjectType type) {
        super(type);
        content = new HashMap<PdfName, AbstractPdfObject>();
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        os.write(ByteEncoder.getBytes(OPEN_DICTIONARY));
        for (Entry<PdfName, AbstractPdfObject> entry : content.entrySet()) {
            os.write(' ');
            entry.getKey().writeToFile(os);
            os.write(' ');
            entry.getValue().writeToFile(os);
            os.write(PdfConstants.LINE_SEPARATOR);
        }
        os.write(ByteEncoder.getBytes(CLOSE_DICTIONARY));
    }

    /**
     * Puts a new value in the dictionary.
     * @param key The PdfName that should function as key.
     * @param value The object to be added as value.
     */
    public void put(PdfName key, AbstractPdfObject value) {
        this.content.put(key, value);
    }

    /**
     * Returns a value from the dictionary corresponding to the given key.
     * @param key PdfName that represents the key.
     * @return AbstractPdfObject corresponding to the key.
     */
    public AbstractPdfObject get(PdfName key) {
        return this.content.get(key);
    }

    /**
     * Check if the dictionary contains the given key.
     * @param key PdfName that represents the key.
     * @return true if the dictionary contains the given key, false otherwise.
     */
    public boolean containsKey(PdfName key) {
        return this.content.containsKey(key);
    }

    /**
     * Check if the dictionary contains the given value.
     * @param value AbstractPdfObject representing the value.
     * @return true if the dictionary contains the given value, false otherwise. 
     */
    public boolean containsValue(AbstractPdfObject value) {
        return this.content.containsValue(value);
    }

}
