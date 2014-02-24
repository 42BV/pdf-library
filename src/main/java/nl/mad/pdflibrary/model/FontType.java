package nl.mad.pdflibrary.model;

/**
 * Enum containing the different subtypes of fonts. Each subtype also stores the corresponding PdfNameValue.
 * @author Dylan de Wolff
 * @see PdfNameValue
 */
public enum FontType {
    TYPE0(PdfNameValue.TYPE0), TYPE1(PdfNameValue.TYPE1), MMTYPE1(PdfNameValue.MMTYPE1), TYPE3(PdfNameValue.TYPE3), TRUETYPE(PdfNameValue.TRUE_TYPE);

    private PdfNameValue value;

    /**
     * Creates a new instance of FontType.
     * @param value PdfNameValue corresponding to this font type.
     */
    private FontType(PdfNameValue value) {
        this.value = value;
    }

    public PdfNameValue getPdfNameValue() {
        return this.value;
    }
}
