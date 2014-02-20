package nl.mad.pdflibrary.pdf.object;

import java.io.IOException;
import java.io.OutputStream;

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
     * Creates a new instance of PdfObject
     */
    public AbstractPdfObject(PdfObjectType type) {
        this.type = type;
    }

    /**
     * Creates a new instance of PdfObject and sets the given byte representation
     * @param byteRepresentation
     */
    public AbstractPdfObject(byte[] byteRepresentation, PdfObjectType type) {
        this.byteRepresentation = byteRepresentation;
        this.type = type;
    }

    public byte[] getByteRepresentation() {
        return byteRepresentation;
    }

    public void setByteRepresentation(byte[] byteRepresentation) {
        this.byteRepresentation = byteRepresentation;
    }

    public PdfObjectType getType() {
        return this.type;
    }

    /**
     * Writes the byte representation to the given OutputStream
     * @param os
     * @throws IOException
     */
    public void writeToFile(OutputStream os) throws IOException {
        if (byteRepresentation != null) {
            os.write(byteRepresentation);
        }
    }

    /**
     * Adds an array of bytes to the current byte representation
     * @param bytes Array of bytes to be added
     */
    public void addToByteRepresentation(byte[] bytes) {
        if (byteRepresentation != null) {
            byte[] newRepresentation = new byte[byteRepresentation.length + bytes.length];
            System.arraycopy(byteRepresentation, 0, newRepresentation, 0, byteRepresentation.length);
            System.arraycopy(bytes, 0, newRepresentation, byteRepresentation.length, bytes.length);
            setByteRepresentation(newRepresentation);
        } else {
            setByteRepresentation(bytes);
        }
    }
}
