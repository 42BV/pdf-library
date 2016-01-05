package org.toucanpdf.api;

import org.toucanpdf.model.DocumentPartType;
import org.toucanpdf.model.Font;
import org.toucanpdf.model.FontFamily;
import org.toucanpdf.model.FontFamilyType;
import org.toucanpdf.model.FontMetrics;
import org.toucanpdf.model.FontStyle;
import org.toucanpdf.utility.Constants;

/**
 * Font class containing the font family, style and the actual font family this font is based on. 
 * @author Dylan de Wolff
 *
 */
public class BaseFont extends AbstractDocumentPart implements Font {
    private FontFamilyType family;
    private FontStyle style;
    private FontFamily fontFamily;

    /**
     * Creates a new instance of BaseFont. Will copy the default font specified in Document.
     */
    public BaseFont() {
        this(Constants.DEFAULT_FONT);
    }

    /**
     * Creates a new instance of BaseFont and copies the given font.
     * @param font Font to copy.
     */
    public BaseFont(Font font) {
        super(DocumentPartType.FONT);
        this.family = font.getFamily();
        this.style = font.getStyle();
        this.fontFamily = font.getFontFamily();
    }

    /**
     * Create a new instance of Font. Will try to use a default base font since none was supplied.
     * @param family Font family.
     * @param style Style of font (bold, italic).
     */
    public BaseFont(FontFamilyType family, FontStyle style) {
        this(family, style, FontFamily.getDefaultFontFamily(family));
    }

    /**
     * Creates a new instance of Font.
     * @param family Font family.
     * @param style Style of font (bold, italic).
     * @param baseFont BaseFont corresponding to this font.
     */
    public BaseFont(FontFamilyType family, FontStyle style, FontFamily baseFont) {
        super(DocumentPartType.FONT);
        this.family = family;
        this.style = style;
        this.fontFamily = baseFont;
    }

    @Override
    public FontFamilyType getFamily() {
        return family;
    }

    @Override
    public Font family(FontFamilyType fontFamilyType) {
        this.family = fontFamilyType;
        if (FontFamily.getDefaultFontFamily(family) != null) {
            this.fontFamily = FontFamily.getDefaultFontFamily(family);
        }
        return this;
    }

    @Override
    public FontStyle getStyle() {
        return style;
    }

    @Override
    public Font style(FontStyle fontStyle) {
        this.style = fontStyle;
        return this;
    }

    @Override
    public Font bold() {
        this.style = FontStyle.BOLD;
        return this;
    }

    @Override
    public Font italic() {
        this.style = FontStyle.ITALIC;
        return this;
    }

    @Override
    public Font boldItalic() {
        this.style = FontStyle.BOLDITALIC;
        return this;
    }

    @Override
    public FontFamily getFontFamily() {
        return fontFamily;
    }

    @Override
    public double getLineHeight(int textSize) {
        return fontFamily.getMetricsForStyle(style).getLineHeightForSize(textSize);
    }

    @Override
    public FontMetrics getMetrics() {
        return fontFamily.getMetricsForStyle(style);
    }
}
