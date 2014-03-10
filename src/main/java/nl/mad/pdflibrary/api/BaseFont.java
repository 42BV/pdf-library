package nl.mad.pdflibrary.api;

import nl.mad.pdflibrary.model.BaseFontFamily;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontStyle;

/**
 * Font class containing the font family, style and the actual font family this font is based on. 
 * @author Dylan de Wolff
 *
 */
public class BaseFont extends AbstractDocumentPart implements Font {
    private FontFamily family;
    private FontStyle style;
    private BaseFontFamily baseFont;

    /**
     * Create a new instance of Font. Will try to use a default base font since none was supplied.
     * @param family Font family.
     * @param style Style of font (bold, italic).
     */
    public BaseFont(FontFamily family, FontStyle style) {
        this(family, style, BaseFontFamily.getDefaultBaseFontFamily(family));
    }

    /**
     * Creates a new instance of Font.
     * @param family Font family.
     * @param style Style of font (bold, italic).
     * @param baseFont BaseFont corresponding to this font.
     */
    public BaseFont(FontFamily family, FontStyle style, BaseFontFamily baseFont) {
        super(DocumentPartType.FONT);
        this.setFamily(family);
        this.setStyle(style);
        this.baseFont = baseFont;
    }

    public FontFamily getFamily() {
        return family;
    }

    public void setFamily(FontFamily family) {
        this.family = family;
        if (BaseFontFamily.getDefaultBaseFontFamily(family) != null) {
            this.baseFont = BaseFontFamily.getDefaultBaseFontFamily(family);
        }
    }

    public FontStyle getStyle() {
        return style;
    }

    public void setStyle(FontStyle style) {
        this.style = style;
    }

    public BaseFontFamily getBaseFontFamily() {
        return baseFont;
    }
}
