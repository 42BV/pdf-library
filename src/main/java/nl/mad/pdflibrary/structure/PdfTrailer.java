package nl.mad.pdflibrary.structure;

import java.io.IOException;
import java.io.OutputStream;

import nl.mad.pdflibrary.model.PdfNameValue;
import nl.mad.pdflibrary.syntax.PdfDictionary;
import nl.mad.pdflibrary.syntax.PdfIndirectObject;
import nl.mad.pdflibrary.syntax.PdfIndirectObjectReference;
import nl.mad.pdflibrary.syntax.PdfName;
import nl.mad.pdflibrary.syntax.PdfNumber;
import nl.mad.pdflibrary.syntax.PdfObjectType;
import nl.mad.pdflibrary.utility.ByteEncoder;
import nl.mad.pdflibrary.utility.PdfConstants;

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
     */
    public PdfTrailer(int objectAmount, byte[] crossReferenceStartByte, PdfIndirectObjectReference catalogReference) {
        super(PdfObjectType.TRAILER);
        this.objectAmount = objectAmount + 1;
        this.crossReferenceStartByte = crossReferenceStartByte;
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
        this.crossReferenceStartByte = crossReferenceStartByte;
    }

    /**
     * Sets the data necessary to point out the root (catalog) of the document and the amount of objects in the body.
     * @param root Catalog reference.
     */
    public final void fillObjectSpecification(PdfIndirectObjectReference root) {
        this.put(new PdfName(PdfNameValue.SIZE), new PdfNumber(objectAmount));
        this.put(new PdfName(PdfNameValue.ROOT), root);
        this.put(new PdfName(PdfNameValue.INFO), info.getReference());
    }

    /** 
     * Writes the trailer to the given OutputStream.
     * @see nl.mad.pdflibrary.syntax.PdfDictionary#writeToFile(java.io.OutputStream)
     * @param os OutputStream which will be written to.
     * @throws IOException
     */
    @Override
    public void writeToFile(OutputStream os) throws IOException {
        os.write(TRAILER_INDICATOR);
        os.write(PdfConstants.LINE_SEPARATOR);
        super.writeToFile(os);
        os.write(PdfConstants.LINE_SEPARATOR);
        os.write(START_XREF_INDICATOR);
        os.write(PdfConstants.LINE_SEPARATOR);
        os.write(crossReferenceStartByte);
        os.write(PdfConstants.LINE_SEPARATOR);
        os.write(END_OF_FILE_INDICATOR);
    }

    public void setInfo(PdfIndirectObject info) {
        this.info = info;
    }
}
