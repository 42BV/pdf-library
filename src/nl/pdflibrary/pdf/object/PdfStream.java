package nl.pdflibrary.pdf.object;

import java.io.IOException;
import java.io.OutputStream;

import nl.pdflibrary.document.AbstractDocumentPart;
import nl.pdflibrary.pdf.PdfDocument;


/**
 * Represents a PDF stream object. Stream objects are dictionaries that contain a sequence of bytes. 
 * This sequence can represent all kinds of data including text and images.
 * 
 * @author Dylan de Wolff
 */
public abstract class PdfStream extends PdfDictionary {
    /**
     * Contains the syntax used to indicate the start of a stream
     */
    private static final byte[] START_STREAM = "stream\n".getBytes();
    /**
     * Contains the syntax used to indicate the end of a stream
     */
    private static final byte[] END_STREAM = "endstream".getBytes();
    private static final PdfName LENGTH = new PdfName(PdfNameValue.LENGTH);

    /**
     * Creates a new instance of PdfStream
     */
    public PdfStream() {
        super(PdfDictionaryType.STREAM);
        this.put(LENGTH, new PdfNumber(0));
    }

    /** 
     * Writes the stream to the given OutputStream
     * @throws IOException
     * @param os
     * @see nl.pdflibrary.pdf.object.PdfDictionary#writeToFile(java.io.OutputStream)
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        updateLength();
        super.writeToFile(os);
        os.write(PdfDocument.LINE_SEPARATOR);
        os.write(START_STREAM);
        os.write(getWriteBeforeStreamContent());
        os.write(this.getByteRepresentation());
        os.write(getWriteAfterStreamContent());
        os.write(END_STREAM);
    }

    /**
     * Updates the length of the stream
     */
    private void updateLength() {
        PdfNumber number = (PdfNumber) this.get(LENGTH);
        number.setNumber(this.getByteRepresentation().length + getWriteBeforeStreamContent().length + getWriteAfterStreamContent().length);

    }

    /**
     * Returns what needs to be written before the actual content is written
     * @return Array of bytes containing what needs to be written
     */
    protected abstract byte[] getWriteBeforeStreamContent();

    /**
     * Returns what needs to be written after the actual content is written
     * @return Array of bytes containing what needs to be written
     */
    protected abstract byte[] getWriteAfterStreamContent();

    /**
     * Used to add content to the stream
     * @param part Document part that will be added to the stream
     */
    public abstract void addCommands(AbstractDocumentPart part);
}
