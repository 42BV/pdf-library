package nl.mad.pdflibrary.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import nl.mad.pdflibrary.utility.ByteEncoder;

/**
 * Represents the number object from the PDF. This is used for all kinds of specification, such as the number of pages in the api.
 * 
 * @author Dylan de Wolff
 */
public class PdfNumber extends AbstractPdfObject {
    private double number;
    /**
     * Specifies buffer size, used to convert the number value to bytes.
     */
    private final static int BYTE_BUFFER_SIZE = 8;

    /**
     * Creates a new instance of PdfNumber.
     * @param number The number this object should represent.
     */
    public PdfNumber(double number) {
        super(PdfObjectType.NUMBER);
        this.number = number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    private void updateByteRepresentation() {
        byte[] bytes = new byte[BYTE_BUFFER_SIZE];
        ByteBuffer.wrap(bytes).putDouble(number);
        this.setByteRepresentation(bytes);
    }

    /** 
     * Writes the number to the given OutputStream.
     * @param os OutputStream to write to.
     * @throws IOException 
     * @see nl.mad.pdflibrary.syntax.AbstractPdfObject#writeToFile(java.io.OutputStream)
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        this.updateByteRepresentation();
        //write an int instead of a double if possible.
        if (number == Math.floor(number)) {
            os.write(ByteEncoder.getBytes(String.valueOf((int) number)));
        } else {
            os.write(ByteEncoder.getBytes(String.valueOf(number)));
        }
    }
}
