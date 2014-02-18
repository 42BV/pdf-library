package nl.pdflibrary.pdf.object;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Representation of a PDF Array object. Maintains a list of the PDF Objects. 
 * 
 * @author Dylan de Wolff
 */
public class PdfArray extends PdfObject {
    private ArrayList<PdfObject> values;

    /**
     * Creates a new instance of PdfArray
     */
    public PdfArray() {
        super();
        this.values = new ArrayList<PdfObject>();
    }

    public void addValue(PdfObject value) {
        this.values.add(value);
    }

    public void addValues(ArrayList<PdfObject> values) {
        this.values.addAll(values);
    }

    /**
     * Writes this PDF Array to the given OutputStream.
     * 
     * @see nl.pdflibrary.pdf.object.PdfObject#writeToFile(java.io.OutputStream)
     * @param os OutputStream that will be written to
     * @throws IOException
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        os.write('[');
        for (int i = 0; i < values.size(); ++i) {
            values.get(i).writeToFile(os);
            os.write(' ');
        }
        os.write(']');
    }

    public int getSize() {
        return values.size();
    }

    public ArrayList<PdfObject> getValues() {
        return values;
    }
}
