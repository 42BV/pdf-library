package nl.mad.toucanpdf.model;

import nl.mad.toucanpdf.image.ImageParser;

/**
 * This enum is used to specify the color space that an image uses.
 * @author Dylan de Wolff
 * @see Image
 * @see ImageParser
 */
public enum ColorSpace {
    DEVICE_GRAY(PdfNameValue.DEVICE_GRAY),
    DEVICE_RGB(PdfNameValue.DEVICE_RGB),
    DEVICE_CMYK(PdfNameValue.DEVICE_CMYK),
    CAL_GRAY(PdfNameValue.CAL_GRAY),
    CAL_RGB(PdfNameValue.CAL_RGB),
    LAB(PdfNameValue.LAB),
    ICC_BASED(PdfNameValue.ICC_BASED),
    INDEXED(PdfNameValue.INDEXED),
    PATTERN(PdfNameValue.PATTERN),
    SEPARATION(PdfNameValue.SEPARATION),
    DEVICE_N(PdfNameValue.DEVICE_N);

    private PdfNameValue pdfName;

    /**
     * Creates a new instance of PdfNameValue.
     * @param string How the value should be written to the PDF file.
     */
    private ColorSpace(PdfNameValue name) {
        this.pdfName = name;
    }

    public PdfNameValue getPdfName() {
        return pdfName;
    }
}
