package nl.mad.pdflibrary.font.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import nl.mad.pdflibrary.font.Type1FontMetrics;
import nl.mad.pdflibrary.model.FontMetricsFlagValues;

/**
 * This class is reponsible for parsing Afm files (contains Type 1 font metrics) and storing the data found. This is required to embed a Type 1 font.
 * 
 * @author Dylan de Wolff
 */
public class AfmParser {
    private String fontName;
    private String fullName;
    private String familyName;
    private String weight;
    private double italicAngle;
    private boolean isFixedPitch;
    private String characterSet;
    private double[] fontBBox;
    private int underlinePosition;
    private int underlineThickness;
    private String encodingScheme;
    private int capHeight;
    private int xHeight;
    private int ascender;
    private int descender;
    private int stdHW;
    private int stdVW;
    private Map<String, CharacterMetric> characterMetrics;
    private Map<KerningKey, Integer> kerningPairs;
    private int flags;
    private int averageWidth;
    private int maxWidth;
    private int firstChar;
    private int lastChar;

    /**
     * Creates a new instance of AfmParser and parses the given file.
     * @param file InputStream for the afm file.
     */
    public AfmParser(InputStream file) {
        characterMetrics = new LinkedHashMap<String, CharacterMetric>();
        kerningPairs = new LinkedHashMap<KerningKey, Integer>();
        parse(file);
    }

    /**
     * Processes the given inputstream and stores the data found.
     * @param is
     */
    private void parse(InputStream is) {
        if (is != null) {
            BufferedReader file = new BufferedReader(new InputStreamReader(is));
            boolean metricsFound = false;
            if (file != null) {
                try {
                    String currentLine = file.readLine();
                    while (currentLine != null && !metricsFound) {
                        StringTokenizer st = new StringTokenizer(currentLine, " ;\t\n\r\f");
                        String token = st.nextToken();
                        if ("FontName".equalsIgnoreCase(token)) {
                            setFontName(st.nextToken());
                        } else if ("FullName".equalsIgnoreCase(token)) {
                            setFontName(st.nextToken());
                        } else if ("FamilyName".equalsIgnoreCase(token)) {
                            setFamilyName(st.nextToken());
                        } else if ("Weight".equalsIgnoreCase(token)) {
                            setWeight(st.nextToken());
                        } else if ("ItalicAngle".equalsIgnoreCase(token)) {
                            setItalicAngle(Double.valueOf(st.nextToken()));
                        } else if ("IsFixedPitch".equalsIgnoreCase(token)) {
                            setFixedPitch("true".equals(st.nextToken()));
                        } else if ("CharacterSet".equalsIgnoreCase(token)) {
                            setCharacterSet(st.nextToken());
                        } else if ("FontBBox".equalsIgnoreCase(token)) {
                            double[] bBox = { Double.valueOf(st.nextToken()), Double.valueOf(st.nextToken()), Double.valueOf(st.nextToken()),
                                    Double.valueOf(st.nextToken()) };
                            setFontBBox(bBox);
                        } else if ("UnderLinePosition".equalsIgnoreCase(token)) {
                            setUnderlinePosition(Integer.valueOf(st.nextToken()));
                        } else if ("UnderLineThickness".equalsIgnoreCase(token)) {
                            setUnderlineThickness(Integer.valueOf(st.nextToken()));
                        } else if ("EncodingScheme".equalsIgnoreCase(token)) {
                            setEncodingScheme(st.nextToken());
                        } else if ("CapHeight".equalsIgnoreCase(token)) {
                            setCapHeight(Integer.valueOf(st.nextToken()));
                        } else if ("XHeight".equalsIgnoreCase(token)) {
                            setXHeight(Integer.valueOf(st.nextToken()));
                        } else if ("Ascender".equalsIgnoreCase(token)) {
                            setAscender(Integer.valueOf(st.nextToken()));
                        } else if ("Descender".equalsIgnoreCase(token)) {
                            setDescender(Integer.valueOf(st.nextToken()));
                        } else if ("StdHW".equalsIgnoreCase(token)) {
                            setStdHW(Integer.valueOf(st.nextToken()));
                        } else if ("StdVW".equalsIgnoreCase(token)) {
                            setStdVW(Integer.valueOf(st.nextToken()));
                        } else if ("StartCharMetrics".equalsIgnoreCase(token)) {
                            /*we've found the start of the character metrics, so we make sure the loop will stop and
                            call the method that will handle the character metrics. */
                            metricsFound = true;
                            parseCharacterMetrics(file);
                        }
                        currentLine = file.readLine();
                    }
                    file.close();
                } catch (IOException e) {
                    System.err.print("IOException occurred during parsing of Afm file: " + e.toString());
                }
            }
        }
    }

    /**
     * Reads the character metrics information from the given file.
     * @param file File to be parsed.
     * @throws IOException
     */
    private void parseCharacterMetrics(BufferedReader file) throws IOException {
        String currentLine = file.readLine();
        boolean characterMetricsEndFound = false;
        while (currentLine != null && !characterMetricsEndFound) {
            StringTokenizer st = new StringTokenizer(currentLine, " ;\t\n\r\f");
            String token = st.nextToken();
            if ("EndCharMetrics".equalsIgnoreCase(token)) {
                //if we've found the end of the character metrics, stop the loop and start processing the kerning data.
                characterMetricsEndFound = true;
                parseKerning(file);
                updateWidthAttributes();
                updateCharAttributes();
                updateFlags();
            } else {
                //read the character data and store it in the character metrics map.
                int c = CharacterMetric.DEFAULT_C_VALUE;
                int wx = CharacterMetric.DEFAULT_WX_VALUE;
                String name = "";
                int[] boundingBox = null;
                while (st.hasMoreTokens()) {
                    if ("C".equalsIgnoreCase(token)) {
                        c = Integer.valueOf(st.nextToken());
                    } else if ("WX".equalsIgnoreCase(token)) {
                        wx = Integer.valueOf(st.nextToken());
                    } else if ("N".equalsIgnoreCase(token)) {
                        name = st.nextToken();
                    } else if ("B".equalsIgnoreCase(token)) {
                        boundingBox = new int[] { Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),
                                Integer.parseInt(st.nextToken()) };
                    }
                    if (st.hasMoreTokens()) {
                        token = st.nextToken();
                    }
                }
                this.characterMetrics.put(name, new CharacterMetric(c, wx, name, boundingBox));
            }
            currentLine = file.readLine();
        }
    }

    /**
     * Reads the kerning information from the given file.
     * @param file File to be parsed.
     * @throws IOException
     */
    private void parseKerning(BufferedReader file) throws IOException {
        String currentLine = file.readLine();
        boolean kerningEndFound = false;
        while (currentLine != null && !kerningEndFound) {
            StringTokenizer st = new StringTokenizer(currentLine, " ;\t\n\r\f");
            String token = st.nextToken();
            if ("KPX".equalsIgnoreCase(token)) {
                String firstCharacter = st.nextToken();
                String secondCharacter = st.nextToken();
                int widthOffset = -Integer.valueOf(st.nextToken());
                this.kerningPairs.put(new KerningKey(firstCharacter, secondCharacter), widthOffset);
            } else if ("EndKernPairs".equalsIgnoreCase(token)) {
                kerningEndFound = true;
            }
            currentLine = file.readLine();
        }
    }

    /**
     * Updates the first char code and last char code.
     */
    private void updateCharAttributes() {
        int firstCharCode = characterMetrics.size();
        int lastCharCode = 0;
        for (CharacterMetric cm : characterMetrics.values()) {
            int c = cm.getC();
            if (c < firstCharCode && c >= 0) {
                firstCharCode = cm.getC();
            }
            if (c > lastCharCode) {
                lastCharCode = cm.getC();
            }
        }
        this.firstChar = firstCharCode;
        this.lastChar = lastCharCode;
    }

    /**
     * Updates the max width and average width variables.
     */
    private void updateWidthAttributes() {
        int totalWidth = 0;
        int characterAmount = characterMetrics.values().size();
        for (CharacterMetric cm : characterMetrics.values()) {
            int characterWidth = cm.getWx();
            if (characterWidth > this.maxWidth) {
                this.maxWidth = characterWidth;
            }
            totalWidth += characterWidth;
        }
        this.averageWidth = totalWidth / characterAmount;
    }

    /**
     * Updates the flag based on values of other attributes. 
     */
    private void updateFlags() {
        this.flags = 0;
        if (isFixedPitch) {
            flags |= FontMetricsFlagValues.FIXED_PITCH.getBitValue();
        }
        if ("AdobeStandardEncoding".equalsIgnoreCase(this.encodingScheme) || "StandardEncoding".equalsIgnoreCase(this.encodingScheme)) {
            flags |= FontMetricsFlagValues.NON_SYMBOLIC.getBitValue();
        } else {
            flags |= FontMetricsFlagValues.SYMBOLIC.getBitValue();
        }
        if (italicAngle != 0) {
            flags |= FontMetricsFlagValues.ITALIC.getBitValue();
        }
        if ("Bold".equalsIgnoreCase(weight)) {
            flags |= FontMetricsFlagValues.FORCE_BOLD.getBitValue();
        }
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setItalicAngle(double italicAngle) {
        this.italicAngle = italicAngle;
    }

    public void setFixedPitch(boolean fixedPitch) {
        this.isFixedPitch = fixedPitch;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public void setFontBBox(double[] fontBBox) {
        this.fontBBox = fontBBox;
    }

    public void setUnderlinePosition(int underlinePosition) {
        this.underlinePosition = underlinePosition;
    }

    public void setUnderlineThickness(int underlineThickness) {
        this.underlineThickness = underlineThickness;
    }

    public void setEncodingScheme(String encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    public void setCapHeight(int capHeight) {
        this.capHeight = capHeight;
    }

    public void setXHeight(int xHeight) {
        this.xHeight = xHeight;
    }

    public void setAscender(int ascender) {
        this.ascender = ascender;
    }

    public void setDescender(int descender) {
        this.descender = descender;
    }

    public void setStdHW(int stdHW) {
        this.stdHW = stdHW;
    }

    public void setStdVW(int stdVW) {
        this.stdVW = stdVW;
    }

    /**
     * @return the fontName
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @return the familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * @return the weight
     */
    public String getWeight() {
        return weight;
    }

    /**
     * @return the italicAngle
     */
    public double getItalicAngle() {
        return italicAngle;
    }

    /**
     * @return the isFixedPitch
     */
    public boolean isFixedPitch() {
        return isFixedPitch;
    }

    /**
     * @return the characterSet
     */
    public String getCharacterSet() {
        return characterSet;
    }

    /**
     * @return the fontBBox
     */
    public double[] getFontBBox() {
        return fontBBox;
    }

    /**
     * @return the underlinePosition
     */
    public int getUnderlinePosition() {
        return underlinePosition;
    }

    /**
     * @return the underlineThickness
     */
    public int getUnderlineThickness() {
        return underlineThickness;
    }

    /**
     * @return the encodingScheme
     */
    public String getEncodingScheme() {
        return encodingScheme;
    }

    /**
     * @return the capHeight
     */
    public int getCapHeight() {
        return capHeight;
    }

    /**
     * @return the xHeight
     */
    public int getxHeight() {
        return xHeight;
    }

    /**
     * @return the ascender
     */
    public int getAscender() {
        return ascender;
    }

    /**
     * @return the descender
     */
    public int getDescender() {
        return descender;
    }

    /**
     * @return the stdHW
     */
    public int getStdHW() {
        return stdHW;
    }

    /**
     * @return the stdVW
     */
    public int getStdVW() {
        return stdVW;
    }

    /**
     * @return the characterMetrics
     */
    public Map<String, CharacterMetric> getCharacterMetrics() {
        return characterMetrics;
    }

    /**
     * Returns the character metric corresponding to the given name.
     * @param name Name of the character.
     * @return CharacterMetric for the given character, null if the character could not be found.
     */
    public CharacterMetric getCharacterMetric(String name) {
        return characterMetrics.get(name);
    }

    /**
     * @return the kerningPairs
     */
    public Map<KerningKey, Integer> getKerningPairs() {
        return kerningPairs;
    }

    /**
     * Returns the kerning value for the given characters.
     * @param characterName Name of first character.
     * @param secondCharacterName Name of second character.
     * @return Integer containing the offset between these two characters.
     */
    public Integer getKerning(String characterName, String secondCharacterName) {
        Integer offset = kerningPairs.get(new KerningKey(characterName, secondCharacterName));
        if (offset != null) {
            return offset.intValue();
        }
        return 0;
    }

    /**
     * @return the flags
     */
    public int getFlags() {
        return flags;
    }

    /**
     * @return the averageWidth
     */
    public int getAverageWidth() {
        return averageWidth;
    }

    /**
     * @return the maxWidth
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * @return the firstChar
     */
    public int getFirstChar() {
        return firstChar;
    }

    /**
     * @return the lastChar
     */
    public int getLastChar() {
        return lastChar;
    }

    /**
     * Class representing the metrics of a single character. 
     * @author Dylan de Wolff
     */
    public class CharacterMetric {
        private int c;
        private int wx;
        private String name;
        private int[] boundingBox;

        /**
         * Default value for the character code.
         */
        public static final int DEFAULT_C_VALUE = -1;
        /**
         * Default value for the width.
         */
        public static final int DEFAULT_WX_VALUE = 250;

        /**
         * Creates a new instance of CharacterMetric.
         * @param c Character code.
         * @param wx Character width.
         * @param name Name of the character.
         * @param boundingBox Bounding box of the character.
         */
        public CharacterMetric(int c, int wx, String name, int[] boundingBox) {
            this.c = c;
            this.wx = wx;
            this.name = name;
            this.boundingBox = boundingBox;
        }

        public int getC() {
            return c;
        }

        public int getWx() {
            return wx;
        }

        public String getName() {
            return name;
        }

        public int[] getBoundingBox() {
            return boundingBox;
        }
    }

    /**
     * Used as key for the kerning map in AFMParser. Contains two character names.
     * @author Dylan de Wolff
     * @see Type1FontMetrics
     */
    private class KerningKey {
        private final String characterName;
        private final String secondCharacterName;

        /**
         * Creates new instance of KerningKey with the given character names.
         * @param characterName First character name.
         * @param secondCharacterName Second character name.
         */
        public KerningKey(String characterName, String secondCharacterName) {
            this.characterName = characterName;
            this.secondCharacterName = secondCharacterName;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof KerningKey) {
                KerningKey other = (KerningKey) o;
                if (characterName.equals(other.characterName) && secondCharacterName.equals(other.secondCharacterName)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return characterName.hashCode() + secondCharacterName.hashCode();
        }
    }
}
