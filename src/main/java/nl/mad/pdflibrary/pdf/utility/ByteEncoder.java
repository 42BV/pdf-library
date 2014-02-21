package nl.mad.pdflibrary.pdf.utility;

import java.io.UnsupportedEncodingException;

public class ByteEncoder {
    private static final String DEFAULT_BYTE_ENCODING = "UTF-8";

    public static byte[] getBytes(String s) {
        try {
            return s.getBytes(DEFAULT_BYTE_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
