package nl.mad.pdflibrary.pdf.object;

import java.awt.Font;

/**
 * PdfDictionary representing a font.
 * @author Dylan de Wolff
 */
public class PdfFont extends PdfDictionary {

    /**
     * Creates a new PdfFont instance from the given font.
     * @param font Font to use.
     */
    public PdfFont(Font font) {
        super(PdfObjectType.FONT);
        put(new PdfName(PdfNameValue.TYPE), new PdfName(PdfNameValue.FONT));
        put(new PdfName(PdfNameValue.BASE_FONT), new PdfName(font.getFontName()));
        put(new PdfName(PdfNameValue.SUB_TYPE), new PdfName(PdfNameValue.TYPE1));
    }
}
