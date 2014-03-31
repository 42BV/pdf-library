package nl.mad.pdflibrary.utility;

import nl.mad.pdflibrary.api.BaseFont;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontFamilyType;
import nl.mad.pdflibrary.model.FontStyle;

/**
 * Contains constants that are used by several PDF object classes.
 * @author Dylan de Wolff
 *
 */
public final class Constants {

    /**
     * Specifies the default line separator value.
     */
    public static final byte[] LINE_SEPARATOR = ByteEncoder.getBytes(System.getProperty("line.separator"));
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
