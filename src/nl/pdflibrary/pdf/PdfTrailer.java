package nl.pdflibrary.pdf;

import java.io.IOException;
import java.io.OutputStream;

import nl.pdflibrary.pdf.object.PdfDictionary;
import nl.pdflibrary.pdf.object.PdfDictionaryType;
import nl.pdflibrary.pdf.object.PdfIndirectObjectReference;
import nl.pdflibrary.pdf.object.PdfName;
import nl.pdflibrary.pdf.object.PdfNameValue;
import nl.pdflibrary.pdf.object.PdfNumber;


/**
 * PdfTrailer represents the trailer section of a PDF. The trailer specifies the amount of objects in the document,
 * where the cross reference table starts and the final line of the file.
 * @author Dylan de Wolff
 */
public class PdfTrailer extends PdfDictionary {
    private int objectAmount;
    private byte[] crossReferenceStartByte;
    /**
     *  Specifies the syntax used to indicate the start of the trailer
     */
    private static final byte[] TRAILER_INDICATOR = "trailer".getBytes();
    /**
     * Specifies the syntax used to indicate the start of the cross reference table
     */
    private static final byte[] START_XREF_INDICATOR = "startxref".getBytes();
    /**
     * Specifies the syntax used to indicate the end of the file
     */
    private static final byte[] END_OF_FILE_INDICATOR = "%%EOF".getBytes();

    /**
     * Used to create a new instance of PdfTrailer
     * @param objectAmount Amount of objects in the body
     * @param crossReferenceStartByte The start position of the cross reference table
     * @param catalogReference The catalog object reference
     */
    public PdfTrailer(int objectAmount, byte[] crossReferenceStartByte, PdfIndirectObjectReference catalogReference) {
        super(PdfDictionaryType.TRAILER);
        this.objectAmount = objectAmount + 1;
        this.crossReferenceStartByte = crossReferenceStartByte;
        this.fillObjectSpecification(catalogReference);
    }

    /**
     * Used to create a new instance of PdfTrailer
     */
    public PdfTrailer() {
        super(PdfDictionaryType.TRAILER);
    }

    public void setObjectAmount(int objectAmount) {
        this.objectAmount = objectAmount;
    }

    public void setCrossReferenceStartByte(byte[] crossReferenceStartByte) {
        this.crossReferenceStartByte = crossReferenceStartByte;
    }

    /**
     * Sets the data necessary to point out the root (catalog) of the document and the amount of objects in the body
     * @param root
     */
    public void fillObjectSpecification(PdfIndirectObjectReference root) {
        this.put(new PdfName(PdfNameValue.SIZE), new PdfNumber(objectAmount));
        this.put(new PdfName(PdfNameValue.ROOT), root);
    }

    /** 
     * Writes the trailer to the given OutputStream
     * @see nl.pdflibrary.pdf.object.PdfDictionary#writeToFile(java.io.OutputStream)
     * @param os
     * @throws IOException
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        os.write(TRAILER_INDICATOR);
        os.write(PdfDocument.LINE_SEPARATOR);
        super.writeToFile(os);
        os.write(PdfDocument.LINE_SEPARATOR);
        os.write(START_XREF_INDICATOR);
        os.write(PdfDocument.LINE_SEPARATOR);
        os.write(crossReferenceStartByte);
        os.write(PdfDocument.LINE_SEPARATOR);
        os.write(END_OF_FILE_INDICATOR);
    }
}
