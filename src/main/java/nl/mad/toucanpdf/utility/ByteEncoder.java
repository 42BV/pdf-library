package nl.mad.toucanpdf.utility;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import nl.mad.toucanpdf.state.BaseStateTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The byte encoder is responsible for returning the byte value of a given string.
 * This should be used instead of the default getBytes() method.
 * @author Dylan de Wolff
 *
 */
public final class ByteEncoder {
    private static final Charset DEFAULT_BYTE_ENCODING = StandardCharsets.UTF_8;
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteEncoder.class);

    private ByteEncoder() {
    }

    /**
     * Returns the byte representation of the given string.
     * @param s String to be processed to bytes.
     * @return Byte array containing the byte value of the given string.
     */
    public static byte[] getBytes(String s) {
        return s.getBytes(DEFAULT_BYTE_ENCODING);
    }

    /**
     * Converts the given ByteArrayOutputStream into a string.
     * @param baos ByteArrayOutputStream to convert.
     * @return String containing the result.
     */
    public static String getString(ByteArrayOutputStream baos) {
        try {
            return baos.toString(DEFAULT_BYTE_ENCODING.name());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UTF-8 is unsupported on this platform, cannot execute conversion");
        }
        return "";
    }

    public static String getString(byte[] data) {
        return new String(data, DEFAULT_BYTE_ENCODING);
    }
}
