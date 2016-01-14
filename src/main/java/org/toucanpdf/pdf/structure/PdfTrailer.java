package org.toucanpdf.pdf.structure;

import java.io.IOException;
import java.io.OutputStream;

import org.toucanpdf.model.PdfNameValue;
import org.toucanpdf.pdf.syntax.PdfDictionary;
import org.toucanpdf.pdf.syntax.PdfIndirectObject;
import org.toucanpdf.pdf.syntax.PdfIndirectObjectReference;
import org.toucanpdf.pdf.syntax.PdfNumber;
import org.toucanpdf.pdf.syntax.PdfObjectType;
import org.toucanpdf.utility.ByteEncoder;
import org.toucanpdf.utility.Constants;

/**
 * PdfTrailer represents the trailer section of a PDF. The trailer specifies the amount of objects in the document,
 * where the cross reference table starts and the final line of the file.
 * @author Dylan de Wolff
 */
public class PdfTrailer extends PdfDictionary {
    private int objectAmount;
    private byte[] crossReferenceStartByte;
    private PdfIndirectObject info;
    /**
     *  Specifies the syntax used to indicate the start of the trailer.
     */
    private static final byte[] TRAILER_INDICATOR = ByteEncoder.getBytes("trailer");
    /**
     * Specifies the syntax used to indicate the start of the cross reference table.
     */
    private static final byte[] START_XREF_INDICATOR = ByteEncoder.getBytes("startxref");
    /**
     * Specifies the syntax used to indicate the end of the file.
     */
    private static final byte[] END_OF_FILE_INDICATOR = ByteEncoder.getBytes("%%EOF");

    /**
     * Used to create a new instance of PdfTrailer.
     * @param objectAmount Amount of objects in the body.
     * @param crossReferenceStartByte The start position of the cross reference table.
     * @param catalogReference The catalog object reference.
     * @param info The info object reference.
     */
    public PdfTrailer(int objectAmount, byte[] crossReferenceStartByte, PdfIndirectObjectReference catalogReference, PdfIndirectObject info) {
        super(PdfObjectType.TRAILER);
        this.objectAmount = objectAmount + 1;
        this.crossReferenceStartByte = crossReferenceStartByte.clone();
        this.info = info;
        this.fillObjectSpecification(catalogReference);
    }

    /**
     * Used to create a new instance of PdfTrailer.
     */
    public PdfTrailer() {
        super(PdfObjectType.TRAILER);
    }

    public void setObjectAmount(int objectAmount) {
        this.objectAmount = objectAmount;
    }

    public void setCrossReferenceStartByte(byte[] crossReferenceStartByte) {
        this.crossReferenceStartByte = crossReferenceStartByte.clone();
    }

    /**
     * Sets the data necessary to point out the root (catalog) of the document and the amount of objects in the body.
     * @param root Catalog reference.
     */
    public final void fillObjectSpecification(PdfIndirectObjectReference root) {
        this.put(PdfNameValue.ROOT, root);
        this.put(PdfNameValue.INFO, info.getReference());
        this.put(PdfNameValue.SIZE, new PdfNumber(objectAmount));
    }

    /** 
     * Writes the trailer to the given OutputStream.
     * @see PdfDictionary#writeToFile(java.io.OutputStream)
     * @param os OutputStream which will be written to.
     * @throws IOException throws an IOException if an error occurred during the writing.
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        os.write(TRAILER_INDICATOR);
        os.write(Constants.LINE_SEPARATOR);
        super.writeToFile(os);
        os.write(Constants.LINE_SEPARATOR);
        os.write(START_XREF_INDICATOR);
        os.write(Constants.LINE_SEPARATOR);
        os.write(crossReferenceStartByte);
        os.write(Constants.LINE_SEPARATOR);
        os.write(END_OF_FILE_INDICATOR);
    }

    public void setInfo(PdfIndirectObject info) {
        this.info = info;
    }
}
