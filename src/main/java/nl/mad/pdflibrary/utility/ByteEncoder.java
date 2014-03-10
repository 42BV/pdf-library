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

    /**
     * Returns the byte representation of the given string.
     * @param s String to be processed to bytes.
     * @return Byte array containing the byte value of the given string.
     */
    public static byte[] getBytes(String s) {
        try {
            return s.getBytes(DEFAULT_BYTE_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
