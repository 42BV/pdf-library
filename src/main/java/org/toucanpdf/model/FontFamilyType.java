package org.toucanpdf.model;

/**
 * Enum containing the default PDF font families.
 * @author Dylan de Wolff
 *
 */
public enum FontFamilyType {
    HELVETICA("Helvetica"),
    TIMES_ROMAN("Times-Roman"),
    COURIER("Courier"),
    SYMBOL("Symbol"),
    ZAPFDINGBATS("ZapfDingBats");

    private FontFamilyType(final String text) {
        this.text = text;
    }

    private final String text;

    @Override
    public String toString() {
        return text;
    }
}
