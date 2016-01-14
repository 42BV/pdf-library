package org.toucanpdf.pdf.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.toucanpdf.utility.ByteEncoder;
import org.toucanpdf.utility.FloatEqualityTester;

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
    private static final int BYTE_BUFFER_SIZE = 8;

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

    public double getNumber() {
        return this.number;
    }

    /**
     * Converts the given list of integers to a list of PdfObjects.
     * @param values Values to be converted.
     * @return List of PdfObjects.
     */
    public static List<AbstractPdfObject> convertListOfValues(List<Integer> values) {
        return values.stream().map(PdfNumber::new).collect(Collectors.toList());
    }

    /**
     * Converts the given list of doubles to a list of PdfObjects.
     * @param values Values to be converted.
     * @return List of PdfObjects.
     */
    public static List<AbstractPdfObject> convertListOfValues(double[] values) {
        List<AbstractPdfObject> list = new ArrayList<>();
        for (double value : values) {
            list.add(new PdfNumber(value));
        }
        return list;
    }

    private void updateByteRepresentation() {
        byte[] bytes = new byte[BYTE_BUFFER_SIZE];
        ByteBuffer.wrap(bytes).putDouble(number);
        this.setByteRepresentation(bytes);
    }

    /** 
     * Writes the number to the given OutputStream.
     * @param os OutputStream to write to.
     * @throws IOException if writing to the outputstream caused an exception
     * @see AbstractPdfObject#writeToFile(java.io.OutputStream)
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        this.updateByteRepresentation();
        //write an int instead of a double if possible.
        if (FloatEqualityTester.equals(number, Math.floor(number))) {
            os.write(ByteEncoder.getBytes(String.valueOf((int) number)));
        } else {
            os.write(ByteEncoder.getBytes(String.valueOf(number)));
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(number);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PdfNumber other = (PdfNumber) obj;
        return Double.doubleToLongBits(number) == Double.doubleToLongBits(other.number);
    }
}
