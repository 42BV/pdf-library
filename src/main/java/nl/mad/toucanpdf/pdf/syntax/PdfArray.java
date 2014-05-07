package nl.mad.toucanpdf.pdf.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Representation of a PDF Array object. Maintains a list of the PDF Objects. 
 * 
 * @author Dylan de Wolff
 */
public class PdfArray extends AbstractPdfObject {
    private List<AbstractPdfObject> values = new LinkedList<AbstractPdfObject>();

    /**
     * Creates a new instance of PdfArray.
     */
    public PdfArray() {
        super(PdfObjectType.ARRAY);
    }

    /**
     * Creates a new instance of PdfArray and adds the given values.
     * @param values Values to be added.
     */
    public PdfArray(List<AbstractPdfObject> values) {
        this();
        System.out.println(values.size());
        this.values = new LinkedList<AbstractPdfObject>(values);
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
     * @param valueList List of objects to be added.
     */
    public void addValues(List<AbstractPdfObject> valueList) {
        this.values.addAll(valueList);
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

    /**
     * Returns the size of the values array.
     * @return int containing the size.
     */
    public int getSize() {
        return values.size();
    }

    /**
     * Returns the values list.
     * @return List with values.
     */
    public List<AbstractPdfObject> getValues() {
        return values;
    }

    /**
     * Adds a value on the given position.
     * @param index Position to add the value on.
     * @param value Value to add.
     */
    public void addValue(int index, AbstractPdfObject value) {
        this.values.add(index, value);
    }
}
