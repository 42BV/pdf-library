package nl.mad.toucanpdf.model;

public enum Filter {
    ASCII_HEX_DECODE(PdfNameValue.ASCII_HEX_DECODE), ASCII_85_DECODE(PdfNameValue.ASCII_85_DECODE), FLATE_DECODE(PdfNameValue.FLATE_DECODE), LZW_DECODE(
            PdfNameValue.LZW_DECODE), RUN_LENGTH_DECODE(PdfNameValue.RUN_LENGTH_DECODE), DCT_DECODE(PdfNameValue.DCT_DECODE), CCITT_FAX_DECODE(
            PdfNameValue.CCITT_FAX_DECODE), JBIG2_DECODE(PdfNameValue.JBIG2_DECODE), JPX_DECODE(PdfNameValue.JPX_DECODE);

    private PdfNameValue pdfName;

    Filter(PdfNameValue name) {
        this.pdfName = name;
    }

    public PdfNameValue getPdfName() {
        return this.pdfName;
    }
}
