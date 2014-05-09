package nl.mad.toucanpdf.model;

/**
 * This enum is used to specify which method of compression should be applied.
 * @author Dylan de Wolff
 */
public enum Compression {
    ASCII_HEX(PdfNameValue.ASCII_HEX_DECODE),
    ASCII_85(PdfNameValue.ASCII_85_DECODE),
    FLATE(PdfNameValue.FLATE_DECODE),
    LZW(PdfNameValue.LZW_DECODE),
    RUN_LENGTH(PdfNameValue.RUN_LENGTH_DECODE),
    DCT(PdfNameValue.DCT_DECODE),
    CCITT(PdfNameValue.CCITT_FAX_DECODE),
    JBIG2(PdfNameValue.JBIG2_DECODE),
    JPX(PdfNameValue.JPX_DECODE);

    private PdfNameValue pdfName;

    Compression(PdfNameValue name) {
        this.pdfName = name;
    }

    public PdfNameValue getPdfName() {
        return this.pdfName;
    }
}
