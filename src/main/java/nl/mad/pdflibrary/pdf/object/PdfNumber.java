package nl.mad.pdflibrary.pdf.object;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Represents the number object from the PDF. This is used for all kinds of specification, such as the number of pages in the mad.
 * 
 * @author Dylan de Wolff
 */
public class PdfNumber extends AbstractPdfObject {
    private double number;
    /**
     * Specifies buffer size, used to convert the number value to bytes
     */
    private final static int BYTE_BUFFER_SIZE = 8;

    /**
     * Creates a new instance of PdfNumber
     * @param number 
     */
    public PdfNumber(double number) {
        super(PdfObjectType.NUMBER);
        setNumber(number);
    }

    public void setNumber(double number) {
        this.number = number;
        byte[] bytes = new byte[BYTE_BUFFER_SIZE];
        ByteBuffer.wrap(bytes).putDouble(number);
        this.setByteRepresentation(bytes);
    }

    /** 
     * Writes the number to the given OutputStream
     * @param os
     * @throws IOException 
     * @see nl.mad.pdflibrary.pdf.object.AbstractPdfObject#writeToFile(java.io.OutputStream)
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        if (number == (int) number) {
            os.write(String.valueOf((int) number).getBytes());
        } else {
            os.write(String.valueOf(number).getBytes());
        }
    }
}
