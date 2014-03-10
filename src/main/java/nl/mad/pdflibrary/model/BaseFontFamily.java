package nl.mad.pdflibrary.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nl.mad.pdflibrary.font.Type1FontMetrics;

/**
 * BaseFontFamily contains the subtype of the font and the different names of the font for each style the font has.
 * BaseFontFamily also has a map containing the default fonts that PDF viewers should support. See FontFamily and FontStyle to see which are the default fonts.
 * @author Dylan de Wolff
 * @see nl.mad.pdflibrary.model.FontFamily
 * @see nl.mad.pdflibrary.model.FontStyle
 * @see nl.mad.pdflibrary.api.BaseFont
 *
 */
public class BaseFontFamily {
    /**
     * Contains a BaseFontFamily for the five default font families.
     */
    private static Map<FontFamily, BaseFontFamily> defaultBaseFonts;
    static {
        defaultBaseFonts = new HashMap<FontFamily, BaseFontFamily>();
        defaultBaseFonts.put(FontFamily.TIMES_ROMAN, new BaseFontFamily(FontType.TYPE1, "Times-Roman", "Times-Bold", "Times-Italic", "Times-BoldItalic"));
        defaultBaseFonts.put(FontFamily.HELVETICA, new BaseFontFamily(FontType.TYPE1, "Helvetica", "Helvetica-Bold", "Helvetica-Oblique",
                "Helvetica-BoldOblique"));
        defaultBaseFonts.put(FontFamily.COURIER, new BaseFontFamily(FontType.TYPE1, "Courier", "Courier-Bold", "Courier-Oblique", "Courier-BoldOblique"));
        defaultBaseFonts.put(FontFamily.SYMBOL, new BaseFontFamily(FontType.TYPE1, "Symbol"));
        defaultBaseFonts.put(FontFamily.ZAPFDINGBATS, new BaseFontFamily(FontType.TYPE1, "ZapfDingbats"));
    }
    private FontType subType;
    private String name;
    private String boldName;
    private String italicName;
    private String boldItalicName;
    private Map<String, FontMetrics> metrics;

    /**
     * Creates a new instance of BaseFontFamily. If you use this constructor it is assumed that this BaseFontFamily 
     * has no options for bold, italic or bold-italic.
     * @param type Font SubType.
     * @param name Name of the font.
     */
    public BaseFontFamily(FontType type, String name) {
        this(type, name, name, name, name);
    }

    /**
     * Creates a new instance of BaseFontFamily.
     * @param type Font SubType.
     * @param name Name of the font.
     * @param boldName Bold name of the font. Insert the normal name if this font has no bold option.
     * @param italicName Italic name of the font. Insert the normal name if this font has no italic option.
     * @param boldItalicName Bold-italic name of the font. Insert the normal name if this font has no bold-italic option.
     */
    public BaseFontFamily(FontType type, String name, String boldName, String italicName, String boldItalicName) {
        this.subType = type;
        this.name = name;
        this.boldName = boldName;
        this.italicName = italicName;
        this.boldItalicName = boldItalicName;
        metrics = new HashMap<String, FontMetrics>();
        this.fillAllMetrics();
    }

    /**
     * Calls fillMetrics for all the different styles.
     */
    private void fillAllMetrics() {
        fillMetrics(name);
        if (!boldName.equals(name)) fillMetrics(boldName);
        if (!italicName.equals(name)) fillMetrics(italicName);
        if (!boldItalicName.equals(name)) fillMetrics(boldItalicName);
    }

    /**
     * Creates FontMetrics for the given filename and adds it to the metrics map.
     * @param filename Name of the file.
     * @see FontMetrics
     */
    private void fillMetrics(String filename) {
        if (subType == FontType.TYPE1) {
            try {
                metrics.put(filename, new Type1FontMetrics(filename));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the metrics of this font for the given style.
     * @param style Style of font.
     * @return Metrics of this font for the given style.
     */
    public FontMetrics getMetricsForStyle(FontStyle style) {
        return this.metrics.get(this.getNameOfStyle(style));
    }

    /**
     * Returns the BaseFontFamily instance for the given FontFamily.
     * @param family Family type of the font.
     * @return BaseFontFamily corresponding to the given FontFamily.
     */
    public static BaseFontFamily getDefaultBaseFontFamily(FontFamily family) {
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
