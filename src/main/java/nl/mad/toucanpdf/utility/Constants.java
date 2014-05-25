package nl.mad.toucanpdf.utility;

import nl.mad.toucanpdf.api.BaseFont;
import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.FontFamilyType;
import nl.mad.toucanpdf.model.FontStyle;

/**
 * Contains constants that are used by several PDF object classes.
 * @author Dylan de Wolff
 *
 */
public final class Constants {

    /**
     * Specifies the default line separator string value.
     */
	public static final String LINE_SEPARATOR_STRING = "\n";
    /**
     * Specifies the default line separator byte value.
     */
    public static final byte[] LINE_SEPARATOR = ByteEncoder.getBytes(LINE_SEPARATOR_STRING);//System.getProperty("line.separator"));
    /**
     * Specifies the location of the resources folder.
     */
    public static final String RESOURCES = "/resources/";
    /**
     * Default font. This is used when no font is specified.
     */
    public static final Font DEFAULT_FONT = new BaseFont(FontFamilyType.TIMES_ROMAN, FontStyle.NORMAL);

    private Constants() {
    }

}
