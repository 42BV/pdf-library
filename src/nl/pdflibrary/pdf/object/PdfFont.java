package nl.pdflibrary.pdf.object;

import java.awt.Font;

/**
 * PdfDictionary representing a font
 * @author Dylan de Wolff
 */
public class PdfFont extends PdfDictionary {

    public PdfFont(Font font) {
        super(PdfDictionaryType.FONT);
        put(new PdfName(PdfNameValue.TYPE), new PdfName(PdfNameValue.FONT));
        put(new PdfName(PdfNameValue.BASE_FONT), new PdfName(font.getFontName()));
        put(new PdfName(PdfNameValue.SUB_TYPE), new PdfName(PdfNameValue.TYPE1));
    }
}
