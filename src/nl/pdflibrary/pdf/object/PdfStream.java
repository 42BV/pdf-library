package nl.pdflibrary.pdf.object;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import nl.pdflibrary.pdf.PdfDocument;

/**
 * Represents a PDF stream object. Stream objects are dictionaries that contain a sequence of bytes. 
 * This sequence can represent all kinds of data including text and images.
 * 
 * @author Dylan de Wolff
 */
public class PdfStream extends PdfDictionary {
    /**
     * Contains the syntax used to indicate the start of a stream
     */
    private static final byte[] START_STREAM = "stream\n".getBytes();
    /**
     * Contains the syntax used to indicate the end of a stream
     */
    private static final byte[] END_STREAM = "endstream".getBytes();
    /**
     * Specifies the command used to specify the start of a text stream
     */
    private static final byte[] BEGIN_TEXT_STREAM = "BT\n".getBytes();
    /**
     * Specifies the command used to specify the end of a text stream
     */
    private static final byte[] END_TEXT_STREAM = "ET\n".getBytes();
    private static final PdfName LENGTH = new PdfName(PdfNameValue.LENGTH);
    private static final PdfName FILTER = new PdfName(PdfNameValue.FILTER);
    //somehow limit this to objects that are actually supposed to be part of the contents?
    private ArrayList<AbstractPdfObject> contents;

    /**
     * Creates a new instance of PdfStream
     */
    public PdfStream() {
        this(new PdfArray());
    }

    public PdfStream(PdfArray filters) {
        super(PdfObjectType.STREAM);
        contents = new ArrayList<AbstractPdfObject>();
        this.put(LENGTH, new PdfNumber(0));
        this.put(FILTER, filters);
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

        for (int i = 0; i < contents.size(); ++i) {
            if (checkWriteBefore(i)) {
                os.write(getWriteBeforeStreamContent(contents.get(i)));
            }
            contents.get(i).writeToFile(os);
            if (checkWriteAfter(i)) {
                os.write(getWriteAfterStreamContent(contents.get(i)));
            }
        }
        os.write(END_STREAM);
    }

    /**
     * Determines if a content indicator should be written before the upcoming object.
     * 
     * @param currentObjectNumber The object index currently being processed.
     * @return True if a content indicator should be written, false otherwise.
     */
    private boolean checkWriteBefore(int currentObjectNumber) {
        if (currentObjectNumber != 0) {
            if (contents.get(currentObjectNumber - 1).getType().equals(contents.get(currentObjectNumber).getType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if a content indicator should be written after the upcoming object.
     * 
     * @param currentObjectNumber The object index currently being processed.
     * @return True if a content indicator should be written, false otherwise.
     */
    private boolean checkWriteAfter(int currentObjectNumber) {
        if (currentObjectNumber != (this.getContentSize() - 1)) {
            if (contents.get(currentObjectNumber + 1).getType().equals(contents.get(currentObjectNumber).getType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the length of the stream.
     * @throws IOException 
     */
    private void updateLength() throws IOException {
        PdfNumber number = (PdfNumber) this.get(LENGTH);
        int length = 0;
        for (int i = 0; i < contents.size(); ++i) {
            if (checkWriteBefore(i)) {
                length += getWriteBeforeStreamContent(contents.get(i)).length;
            }
            length += contents.get(i).getByteRepresentation().length;
            if (checkWriteAfter(i)) {
                length += getWriteAfterStreamContent(contents.get(i)).length;
            }
        }
        number.setNumber(length);
    }

    /**
     * Returns what needs to be written before the actual content is written.
     * @return Array of bytes containing what needs to be written
     */
    private byte[] getWriteBeforeStreamContent(AbstractPdfObject object) {
        if (object instanceof PdfText) {
            return BEGIN_TEXT_STREAM;
        }
        return null;
    }

    /**
     * Returns what needs to be written after the actual content is written.
     * @return Array of bytes containing what needs to be written
     */
    private byte[] getWriteAfterStreamContent(AbstractPdfObject object) {
        if (object instanceof PdfText) {
            return END_TEXT_STREAM;
        }
        return null;
    }

    public void add(AbstractPdfObject object) {
        this.contents.add(object);
    }

    public int getContentSize() {
        return this.contents.size();
    }
}
