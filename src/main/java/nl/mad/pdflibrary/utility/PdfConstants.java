package nl.mad.pdflibrary.utility;

/**
 * Contains constants that are used by several PDF object classes.
 * @author Dylan de Wolff
 *
 */
public final class PdfConstants {

    /**
     * Specifies the default line separator value.
     */
    public static final byte[] LINE_SEPARATOR = ByteEncoder.getBytes(System.getProperty("line.separator"));
    public static final String RESOURCES = "/resources/";

    private PdfConstants() {

    }

}
