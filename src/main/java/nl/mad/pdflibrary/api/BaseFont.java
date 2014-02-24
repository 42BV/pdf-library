package nl.mad.pdflibrary.api;

import java.util.HashMap;
import java.util.Map;

/**
 * BaseFont contains the subtype of the font and the different names of the font for each style the font has.
 * BaseFont also has a map containing the default basefonts that PDF viewers should support. See FontFamily and FontStyle to see which are the default fonts.
 * @author Dylan de Wolff
 * @see FontFamily
 * @see FontStyle
 * @see Font
 *
 */
public class BaseFont {
    /**
     * Contains a basefont for the five default font families.
     */
    private static Map<FontFamily, BaseFont> defaultBaseFonts;
    static {
        defaultBaseFonts = new HashMap<FontFamily, BaseFont>();
        defaultBaseFonts.put(FontFamily.TIMES_ROMAN, new BaseFont(FontType.TYPE1, "Times-Roman", "Times-Bold", "Times-Italic", "Times-BoldItalic"));
        defaultBaseFonts.put(FontFamily.HELVETICA, new BaseFont(FontType.TYPE1, "Helvetica", "Helvetica-Bold", "Helvetica-Oblique", "Helvetica-BoldOblique"));
        defaultBaseFonts.put(FontFamily.COURIER, new BaseFont(FontType.TYPE1, "Courier", "Courier-Bold", "Courier-Oblique", "Courier-BoldOblique"));
        defaultBaseFonts.put(FontFamily.SYMBOL, new BaseFont(FontType.TYPE1, "Symbol"));
        defaultBaseFonts.put(FontFamily.ZAPFDINGBATS, new BaseFont(FontType.TYPE1, "ZapfDingbats"));
    }

    private FontType subType;
    private String name;
    private String boldName;
    private String italicName;
    private String boldItalicName;

    /**
     * Creates a new instance of BaseFont. If you use this constructor it is assumed that this BaseFont has no options for bold, italic or bold-italic.
     * @param type Font subtype.
     * @param name Name of the font.
     */
    public BaseFont(FontType type, String name) {
        this(type, name, name, name, name);
    }

    /**
     * Creates a new instance of BaseFont.
     * @param type Font subtype.
     * @param name Name of the font.
     * @param boldName Bold name of the font.
     * @param italicName Italic name of the font.
     * @param boldItalicName Bold-italic name of the font.
     */
    public BaseFont(FontType type, String name, String boldName, String italicName, String boldItalicName) {
        this.subType = type;
        this.name = name;
        this.boldName = boldName;
        this.italicName = italicName;
        this.boldItalicName = boldItalicName;
    }

    public static BaseFont getDefaultBaseFont(FontFamily family) {
        return defaultBaseFonts.get(family);
    }

    public FontType getSubType() {
        return subType;
    }

    public void setSubType(FontType subType) {
        this.subType = subType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoldName() {
        return boldName;
    }

    public void setBoldName(String boldName) {
        this.boldName = boldName;
    }

    public String getItalicName() {
        return italicName;
    }

    public void setItalicName(String italicName) {
        this.italicName = italicName;
    }

    public String getBoldItalicName() {
        return boldItalicName;
    }

    public void setBoldItalicName(String boldItalicName) {
        this.boldItalicName = boldItalicName;
    }

    /**
     * Returns the font name corresponding to the given FontStyle.
     * @param style Style you wish to get the font name of.
     * @return String containing the requested name.
     */
    public String getNameOfStyle(FontStyle style) {
        switch (style) {
        case BOLD:
            return this.boldName;
        case ITALIC:
            return this.italicName;
        case BOLDITALIC:
            return this.boldItalicName;
        default:
            return this.name;
        }
    }
}
