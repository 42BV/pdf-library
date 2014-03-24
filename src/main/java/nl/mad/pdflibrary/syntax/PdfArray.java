package nl.mad.pdflibrary.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a PDF Array object. Maintains a list of the PDF Objects. 
 * 
 * @author Dylan de Wolff
 */
public class PdfArray extends AbstractPdfObject {
    private List<AbstractPdfObject> values;

    /**
     * Creates a new instance of PdfArray.
     */
    public PdfArray() {
        super(PdfObjectType.ARRAY);
        this.values = new ArrayList<AbstractPdfObject>();
    }

    /**
     * Creates a new instance of PdfArray and adds the given values.
     * @param values Values to be added.
     */
    public PdfArray(List<AbstractPdfObject> values) {
        this();
        this.values = new ArrayList<AbstractPdfObject>(values);
    }

    /**
     * Adds the given value to the array.
     * @param value Object to be added.
     */
    public void addValue(AbstractPdfObject value) {
        this.values.add(value);
    }

    /**
     * Adds a list of values to the array.
     * @param values List of objects to be added.
     */
    public void addValues(List<AbstractPdfObject> values) {
        this.values.addAll(values);
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        os.write('[');
        os.write(' ');
        for (int i = 0; i < values.size(); ++i) {
            values.get(i).writeToFile(os);
            os.write(' ');
        }
        os.write(']');
    }

    public int getSize() {
        return values.size();
    }

    public List<AbstractPdfObject> getValues() {
        return values;
    }
}
