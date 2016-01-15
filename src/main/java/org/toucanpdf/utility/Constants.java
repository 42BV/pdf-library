package org.toucanpdf.utility;

import org.toucanpdf.api.BaseFont;
import org.toucanpdf.model.Font;
import org.toucanpdf.model.FontFamilyType;
import org.toucanpdf.model.FontStyle;

/**
 * Contains constants that are used by several classes.
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
    /**
     * Default text size. This is used when no text size is specified.
     */
    public static final Integer DEFAULT_TEXT_SIZE = 12;
    public static final Integer MIN_BORDER_SIZE = 0;
    public static final Integer MAX_BORDER_SIZE = 6;
    public static final Integer MIN_PADDING_SIZE = 0;
    public static final Integer MAX_PADDING_SIZE = 20;

    private Constants() {
    }

}
