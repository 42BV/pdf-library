package nl.mad.toucanpdf.pdf.syntax;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.model.Compression;
import nl.mad.toucanpdf.model.PdfNameValue;
import nl.mad.toucanpdf.utility.ByteEncoder;
import nl.mad.toucanpdf.utility.Compressor;
import nl.mad.toucanpdf.utility.Constants;

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
    private static final String START_STREAM = "stream" + Constants.LINE_SEPARATOR_STRING;
    /**
     * Contains the syntax used to indicate the end of a stream.
     */
    private static final String END_STREAM = "endstream";
    /**
     * Specifies the command used to specify the start of a text stream.
     */
    private static final String BEGIN_TEXT_STREAM = "BT" + Constants.LINE_SEPARATOR_STRING;
    /**
     * Specifies the command used to specify the end of a text stream.
     */
    private static final String END_TEXT_STREAM = "ET" + Constants.LINE_SEPARATOR_STRING;
    private static final PdfName LENGTH = new PdfName(PdfNameValue.LENGTH);
    private static final PdfName FILTER = new PdfName(PdfNameValue.FILTER);
    private List<Compression> filterList = new LinkedList<Compression>();
    private List<AbstractPdfObject> contents;

    /**
     * Creates a new instance of PdfStream.
     */
    public PdfStream() {
        this(PdfObjectType.STREAM);
    }

    /**
     * Creates a new instance of PdfStream.
     * @param type The object type of this stream.
     */
    public PdfStream(PdfObjectType type) {
        super(type);
        contents = new ArrayList<>();
        this.put(LENGTH, new PdfNumber(0));
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeContentToStream(baos);
        baos.flush();
        baos.close();
        byte[] data = processCompression(baos.toByteArray());
        updateLength(data);
        super.writeToFile(os);
        os.write(Constants.LINE_SEPARATOR);
        os.write(ByteEncoder.getBytes(START_STREAM));
        os.write(data);
        if (this.filterList.size() > 0) {
            os.write(Constants.LINE_SEPARATOR);
        }
        os.write(ByteEncoder.getBytes(END_STREAM));
    }

    private void writeContentToStream(ByteArrayOutputStream bigBaos) throws IOException {
        for (int i = 0; i < contents.size(); ++i) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (checkWriteBefore(i)) {
                baos.write(getWriteBeforeStreamContent(contents.get(i)));
            }
            contents.get(i).writeToFile(baos);
            if (checkWriteAfter(i)) {
                baos.write(getWriteAfterStreamContent(contents.get(i)));
            }
            String s = ByteEncoder.getString(baos);
            if (!s.endsWith(Constants.LINE_SEPARATOR_STRING)) {
                baos.write(Constants.LINE_SEPARATOR);
            }
            baos.flush();
            bigBaos.write(baos.toByteArray());
            baos.close();
        }
    }

    private byte[] processCompression(byte[] data) {
        byte[] compressedData = data;
        for (Compression com : filterList) {
            compressedData = Compressor.compress(compressedData, com);
        }
        return compressedData;
    }

    /**
     * Determines if a content indicator should be written before the upcoming object.
     * 
     * @param currentObjectNumber The object index currently being processed.
     * @return True if a content indicator should be written, false otherwise.
     */
    private boolean checkWriteBefore(int currentObjectNumber) {
        return !(currentObjectNumber != 0 && contents.get(currentObjectNumber - 1).getType().equals(contents.get(currentObjectNumber).getType()));
    }

    /**
     * Determines if a content indicator should be written after the upcoming object.
     * 
     * @param currentObjectNumber The object index currently being processed.
     * @return True if a content indicator should be written, false otherwise.
     */
    private boolean checkWriteAfter(int currentObjectNumber) {
        boolean lastEntry = currentObjectNumber == (this.getContentSize() - 1);
        return !(!lastEntry && contents.get(currentObjectNumber + 1).getType().equals(contents.get(currentObjectNumber).getType()));
    }

    /**
     * Updates the length of the stream.
     * @param streamContent Content of the stream
     * @throws IOException 
     */
    private void updateLength(byte[] streamContent) throws IOException {
        PdfNumber number = (PdfNumber) this.get(LENGTH);
        number.setNumber(streamContent.length);
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

    private PdfArray getFilterArray() {
        PdfArray array = (PdfArray) this.get(FILTER);
        if (array == null) {
            array = new PdfArray();
            this.put(FILTER, array);
        }
        return array;
    }

    /**
     * Adds a filter to the filter array. The filter is added to the front of the array.
     * The filters should be in order of how they were applied. The filter that was applied first should be last in the array, while the filter 
     * that was applied last should be the first in the array.
     * @param method Compression method to use.
     */
    public void addFilter(Compression method) {
        PdfName name = new PdfName(method.getPdfName());
        PdfArray array = getFilterArray();
        boolean nameExists = false;
        for (AbstractPdfObject object : array.getValues()) {
            if (object instanceof PdfName && object.equals(name)) {
                nameExists = true;
            }
        }
        if (!nameExists) {
            array.addValue(0, name);
            filterList.add(method);
        }
    }

    public int getContentSize() {
        return this.contents.size();
    }
}
