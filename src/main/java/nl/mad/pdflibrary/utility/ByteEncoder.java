package nl.mad.pdflibrary.utility;

import java.io.UnsupportedEncodingException;

/**
 * The byte encoder is responsible for returning the byte value of a given string.
 * This should be used instead of the default getBytes() method.
 * @author Dylan de Wolff
 *
 */
public final class ByteEncoder {
    private static final String DEFAULT_BYTE_ENCODING = "UTF-8";

    private ByteEncoder() {
    }

    public static byte[] getBytes(String s) {
        try {
            return s.getBytes(DEFAULT_BYTE_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
