package nl.mad.toucanpdf.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UnicodeConverter allows you to get the postscript name of a unicode character. This is needed for accessing the metric data of certain font types.
 * @author Dylan de Wolff
 *
 */
public final class UnicodeConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnicodeConverter.class);
    private static final Map<Integer, String> UNICODE_TO_POSTSCRIPT;
    private static final String FILENAME = "glyphlist.txt";
    private static final int KEY_RADIX = 16;

    static {
        Map<Integer, String> unicodeToPostscriptTemp = new HashMap<Integer, String>();
        InputStream in = null;
        try {
            in = UnicodeConverter.class.getResourceAsStream(Constants.RESOURCES + FILENAME);
            //TODO: Remove this as soon as it's no longer necessary to run the library on its own
            if (in == null) {
                in = UnicodeConverter.class.getClassLoader().getResourceAsStream(FILENAME);
            }
            if (in != null) {
                unicodeToPostscriptTemp = UnicodeConverter.processGlyphlist(unicodeToPostscriptTemp, in);
                in.close();
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not find glyphlist.txt in the resources folder! UnicodeConverter will not function without this file.");
        } catch (IOException e) {
            LOGGER.error("IOException ocurred while reading glyphlist.txt! UnicodeConverter will not be able to function normally.");
        }
        UNICODE_TO_POSTSCRIPT = Collections.unmodifiableMap(unicodeToPostscriptTemp);
    }

    private UnicodeConverter() {
    }

    /**
     * Returns the postscript name of the given unicode character code.
     * @param code Code of the character.
     * @return String containing the postscript name. Will be null if the character could not be found.
     */
    public static String getPostscriptForUnicode(int code) {
        return UNICODE_TO_POSTSCRIPT.get(code);
    }

    /**
     * Processes the file containing the list of unicode character codes and the corresponding postscript names.
     * @param Map<Integer, String> Map to store the read values in.
     * @param in InputStream for the glyph conversion list.
     * @throws IOException
     */
    private static Map<Integer, String> processGlyphlist(Map<Integer, String> unicodeToPostscriptTemp, InputStream in) throws IOException {
        String currentLine = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while ((currentLine = reader.readLine()) != null) {
            if (!currentLine.startsWith("#")) {
                StringTokenizer st = new StringTokenizer(currentLine, " ;\r\n\t\f");
                String value = st.nextToken();
                int key = Integer.parseInt(st.nextToken(), KEY_RADIX);
                if ("endash".equals(value)) {
                    System.out.println("The key for endash = " + key);
                }
                if (unicodeToPostscriptTemp.get(key) == null) {
                    unicodeToPostscriptTemp.put(key, value);
                }
            }
        }
        return unicodeToPostscriptTemp;
    }
}
