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

    public void addValue(AbstractPdfObject value) {
        this.values.add(value);
    }

    public void addValues(ArrayList<AbstractPdfObject> values) {
        this.values.addAll(values);
    }

    /**
     * Writes this PDF Array to the given OutputStream.
     * 
     * @see nl.mad.pdflibrary.syntax.AbstractPdfObject#writeToFile(java.io.OutputStream)
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

    public List<AbstractPdfObject> getValues() {
        return values;
    }
}
