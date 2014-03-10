package nl.mad.pdflibrary.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.model.PdfNameValue;
import nl.mad.pdflibrary.utility.ByteEncoder;
import nl.mad.pdflibrary.utility.PdfConstants;

/**
 * Represents a PDF stream object. Stream objects are dictionaries that contain a sequence of bytes. 
 * This sequence can represent all kinds of data including text and images.
 * 
 * @author Dylan de Wolff
 */
public class PdfStream extends PdfDictionary {
    /**
     * Contains the syntax used to indicate the start of a stream.
     */
    private static final String START_STREAM = "stream\n";
    /**
     * Contains the syntax used to indicate the end of a stream.
     */
    private static final String END_STREAM = "endstream";
    /**
     * Specifies the command used to specify the start of a text stream.
     */
    private static final String BEGIN_TEXT_STREAM = "BT\n";
    /**
     * Specifies the command used to specify the end of a text stream.
     */
    private static final String END_TEXT_STREAM = "ET\n";
    private static final PdfName LENGTH = new PdfName(PdfNameValue.LENGTH);
    private static final PdfName FILTER = new PdfName(PdfNameValue.FILTER);
    //somehow limit this to objects that are actually supposed to be part of the contents?
    private List<AbstractPdfObject> contents;

    /**
     * Creates a new instance of PdfStream.
     */
    public PdfStream() {
        super(PdfObjectType.STREAM);
        contents = new ArrayList<AbstractPdfObject>();
        this.put(LENGTH, new PdfNumber(0));
    }

    /**
     * Creates a new instance of PdfStream with the given filters.
     * @param filters Array containing the filters on the stream.
     */
    public PdfStream(PdfArray filters) {
        super(PdfObjectType.STREAM);
        contents = new ArrayList<AbstractPdfObject>();
        this.put(LENGTH, new PdfNumber(0));
        this.put(FILTER, filters);
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        updateLength();
        super.writeToFile(os);
        os.write(PdfConstants.LINE_SEPARATOR);
        os.write(ByteEncoder.getBytes(START_STREAM));

        for (int i = 0; i < contents.size(); ++i) {
            if (checkWriteBefore(i)) {
                os.write(getWriteBeforeStreamContent(contents.get(i)));
            }
            contents.get(i).writeToFile(os);
            if (checkWriteAfter(i)) {
                os.write(getWriteAfterStreamContent(contents.get(i)));
            }
        }
        os.write(ByteEncoder.getBytes(END_STREAM));
    }

    /**
     * Determines if a content indicator should be written before the upcoming object.
     * 
     * @param currentObjectNumber The object index currently being processed.
     * @return True if a content indicator should be written, false otherwise.
     */
    private boolean checkWriteBefore(int currentObjectNumber) {
        if (currentObjectNumber != 0 && contents.get(currentObjectNumber - 1).getType().equals(contents.get(currentObjectNumber).getType())) {
            return false;
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
        boolean lastEntry = currentObjectNumber == (this.getContentSize() - 1);
        if (!lastEntry && contents.get(currentObjectNumber + 1).getType().equals(contents.get(currentObjectNumber).getType())) {
            return false;
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
                int afterStreamContentLength = getWriteAfterStreamContent(contents.get(i)).length;
                if (afterStreamContentLength > 0) {
                    //we take the length of the end of stream indicator minus one since we do not want to take the end of line into account.
                    length += afterStreamContentLength - 1;
                }
            }
        }
        number.setNumber(length);
    }

    /**
     * Returns what needs to be written before the actual content is written.
     * @return Array of bytes containing what needs to be written.
     */
    private byte[] getWriteBeforeStreamContent(AbstractPdfObject object) {
        if (object instanceof PdfText) {
            return ByteEncoder.getBytes(BEGIN_TEXT_STREAM);
        }
        return new byte[0];
    }

    /**
     * Returns what needs to be written after the actual content is written.
     * @return Array of bytes containing what needs to be written.
     */
    private byte[] getWriteAfterStreamContent(AbstractPdfObject object) {
        if (object instanceof PdfText) {
            return ByteEncoder.getBytes(END_TEXT_STREAM);
        }
        return new byte[0];
    }

    /**
     * Adds the given object to the content of the stream.
     * @param object Object to be added to the content.
     */
    public void add(AbstractPdfObject object) {
        this.contents.add(object);
    }

    /**
     * Adds a filter to the filter array.
     * @param filter Object containing the filter.
     */
    public void addFilter(AbstractPdfObject filter) {
        PdfArray array = (PdfArray) this.get(FILTER);
        if (array == null) {
            array = new PdfArray();
            this.put(FILTER, array);
        }
        array.addValue(filter);
    }

    public int getContentSize() {
        return this.contents.size();
    }
}
