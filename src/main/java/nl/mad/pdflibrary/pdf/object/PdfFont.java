package nl.mad.pdflibrary.pdf.object;

import nl.mad.pdflibrary.document.Font;

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
        put(new PdfName(PdfNameValue.BASE_FONT), new PdfName(font.getBaseFont().getNameOfStyle(font.getStyle())));
        put(new PdfName(PdfNameValue.SUB_TYPE), new PdfName(font.getBaseFont().getSubType().getPdfNameValue()));
    }
}
