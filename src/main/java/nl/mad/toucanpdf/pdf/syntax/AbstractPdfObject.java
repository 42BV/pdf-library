package nl.mad.toucanpdf.pdf.syntax;

import java.io.IOException;
import java.io.OutputStream;

import nl.mad.toucanpdf.utility.ByteEncoder;

/**
 * Abstract class for PdfObjects. Represents the base of all the object types found in PDF.
 * @author Dylan de Wolff
 */
public abstract class AbstractPdfObject {
    /**
     * contains the byte representation of this object.
     */
    private byte[] byteRepresentation;
    private PdfObjectType type;

    /**
     * Creates a new instance of PdfObject.
     * @param type Type of the object.
     */
    public AbstractPdfObject(PdfObjectType type) {
        this.type = type;
        byteRepresentation = new byte[0];
    }

    /**
     * Creates a new instance of PdfObject and sets the given byte representation.
     * @param byteRepresentation Representation of object in bytes.
     * @param type Type of the object.
     */
    public AbstractPdfObject(byte[] byteRepresentation, PdfObjectType type) {
        this.setByteRepresentation(byteRepresentation);
        this.type = type;
    }

    public byte[] getByteRepresentation() {
        return byteRepresentation.clone();
    }

    public final void setByteRepresentation(byte[] byteRepresentation) {
        this.byteRepresentation = byteRepresentation.clone();
    }

    /**
     * Converts the given string and sets the byte representation to the resulting bytes.
     * @param s Strng to use.
     */
    public final void setByteRepresentation(String s) {
        this.setByteRepresentation(ByteEncoder.getBytes(s));
    }

    public PdfObjectType getType() {
        return this.type;
    }

    /**
     * Writes the byte representation to the given OutputStream.
     * @param os OutputStream which will be written to.
     * @throws IOException throws IOException if an error occured during the writing.
     */
    public void writeToFile(OutputStream os) throws IOException {
        os.write(byteRepresentation);
    }

    /**
     * Adds an array of bytes to the current byte representation.
     * @param bytes Array of bytes to be added.
     */
    public void addToByteRepresentation(byte[] bytes) {
        byte[] newRepresentation = new byte[byteRepresentation.length + bytes.length];
        System.arraycopy(byteRepresentation, 0, newRepresentation, 0, byteRepresentation.length);
        System.arraycopy(bytes, 0, newRepresentation, byteRepresentation.length, bytes.length);
        setByteRepresentation(newRepresentation);
    }

    /**
     * Converts the given string and adds it to the current byte representation.
     * @param s String to be added.
     */
    public void addToByteRepresentation(String s) {
        this.addToByteRepresentation(ByteEncoder.getBytes(s));
    }
}
