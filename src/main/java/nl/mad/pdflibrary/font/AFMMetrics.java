package nl.mad.pdflibrary.font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.model.FontMetricsFlagValues;
import nl.mad.pdflibrary.utility.UnicodeConverter;

/**
 * This class is responsible for parsing afm files and storing the metrics data found.
 * @author Dylan de Wolff
 * @see FontMetrics
 */
public class AFMMetrics implements FontMetrics {
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
    private Map<CharacterKey, CharacterMetric> characterMetrics;
    private Map<KerningKey, Integer> kerningPairs;
    private int flags;
    private int averageWidth;
    private int maxWidth;
    private int firstChar;
    private int lastChar;

    /**
     * Creates a new instance of AFMMetrics and immediately parses the file corresponding to the given filename.
     * @param filename AFM file to be parsed.
     * @throws IOException
     */
    public AFMMetrics(String filename) throws IOException {
        characterMetrics = new LinkedHashMap<CharacterKey, CharacterMetric>();
        kerningPairs = new LinkedHashMap<KerningKey, Integer>();
        this.parse(filename);
    }

    /**
     * Parses the afm file corresponding to the given filename and returns an instance of FontMetrics containing the values parsed.
     * @param filename Name of the afm file.
     * @throws IOException
     */
    public void parse(String filename) throws IOException {
        RandomAccessFile file = getFile(filename);
        boolean metricsFound = false;
        if (file != null) {
            String currentLine = file.readLine();
            while (currentLine != null && !metricsFound) {
                StringTokenizer st = new StringTokenizer(currentLine, " ;\t\n\r\f");
                String token = st.nextToken();
                if ("FontName".equalsIgnoreCase(token)) {
                    this.setFontName(st.nextToken());
                } else if ("FullName".equalsIgnoreCase(token)) {
                    this.setFontName(st.nextToken());
                } else if ("FamilyName".equalsIgnoreCase(token)) {
                    this.setFamilyName(st.nextToken());
                } else if ("Weight".equalsIgnoreCase(token)) {
                    this.setWeight(st.nextToken());
                } else if ("ItalicAngle".equalsIgnoreCase(token)) {
                    this.setItalicAngle(Double.valueOf(st.nextToken()));
                } else if ("IsFixedPitch".equalsIgnoreCase(token)) {
                    this.setFixedPitch("true".equals(st.nextToken()));
                } else if ("CharacterSet".equalsIgnoreCase(token)) {
                    this.setCharacterSet(st.nextToken());
                } else if ("FontBBox".equalsIgnoreCase(token)) {
                    double[] bBox = { Double.valueOf(st.nextToken()), Double.valueOf(st.nextToken()), Double.valueOf(st.nextToken()),
                            Double.valueOf(st.nextToken()) };
                    this.setFontBBox(bBox);
                } else if ("UnderLinePosition".equalsIgnoreCase(token)) {
                    this.setUnderlinePosition(Integer.valueOf(st.nextToken()));
                } else if ("UnderLineThickness".equalsIgnoreCase(token)) {
                    this.setUnderlineThickness(Integer.valueOf(st.nextToken()));
                } else if ("EncodingScheme".equalsIgnoreCase(token)) {
                    this.setEncodingScheme(st.nextToken());
                } else if ("CapHeight".equalsIgnoreCase(token)) {
                    this.setCapHeight(Integer.valueOf(st.nextToken()));
                } else if ("XHeight".equalsIgnoreCase(token)) {
                    this.setxHeight(Integer.valueOf(st.nextToken()));
                } else if ("Ascender".equalsIgnoreCase(token)) {
                    this.setAscender(Integer.valueOf(st.nextToken()));
                } else if ("Descender".equalsIgnoreCase(token)) {
                    this.setDescender(Integer.valueOf(st.nextToken()));
                } else if ("StdHW".equalsIgnoreCase(token)) {
                    this.setStdHW(Integer.valueOf(st.nextToken()));
                } else if ("StdVW".equalsIgnoreCase(token)) {
                    this.setStdVW(Integer.valueOf(st.nextToken()));
                } else if ("StartCharMetrics".equalsIgnoreCase(token)) {
                    metricsFound = true;
                    parseCharacterMetrics(file);
                }
                currentLine = file.readLine();
            }
            file.close();
        }
    }

    /**
     * Finds the file by the given file name and returns it.
     * @param filename Name of the file.
     * @return RandomAccessFile for the given filename. Null if the file could not be found.
     * @throws FileNotFoundException
     */
    private RandomAccessFile getFile(String filename) throws FileNotFoundException {
        //add afm extension if it could not be found.
        if (!filename.toLowerCase().endsWith(".afm")) {
            filename += ".afm";
        }
        File file = new File(filename);
        //tries looking for the file in the resource folder
        if (!file.isFile()) {
            filename = FontMetrics.RESOURCE_LOCATION + filename;
            file = new File(filename);
        }
        if (file.isFile()) {
            return new RandomAccessFile(file, "r");
        }
        return null;

    }

    /**
     * Reads the character metrics information from the given file.
     * @param file File to be parsed.
     * @throws IOException
     */
    private void parseCharacterMetrics(RandomAccessFile file) throws IOException {
        String currentLine = file.readLine();
        boolean characterMetricsEndFound = false;
        while (currentLine != null && !characterMetricsEndFound) {
            StringTokenizer st = new StringTokenizer(currentLine, " ;\t\n\r\f");
            String token = st.nextToken();
            if ("EndCharMetrics".equalsIgnoreCase(token)) {
                characterMetricsEndFound = true;
                parseKerning(file);
                updateWidthAttributes();
                updateCharAttributes();
            } else {
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
                this.characterMetrics.put(new CharacterKey(c, name), new CharacterMetric(c, wx, name, boundingBox));
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
        System.out.println(characterMetrics.values().size());
    }

    /**
     * Reads the kerning information from the given file.
     * @param file File to be parsed.
     * @throws IOException
     */
    private void parseKerning(RandomAccessFile file) throws IOException {
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

    public void setxHeight(int xHeight) {
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

    @Override
    public int getWidth(int characterCode) {
        CharacterMetric metric = characterMetrics.get(new CharacterKey(characterCode));
        if (metric != null) {
            return metric.getWx();
        }
        return 0;
    }

    @Override
    public int getWidth(String name) {
        CharacterMetric metric = characterMetrics.get(new CharacterKey(name));
        if (metric != null) {
            return metric.getWx();
        }
        return 0;
    }

    @Override
    public String getFontFamily() {
        return familyName;
    }

    @Override
    public String getFontName() {
        return fontName;
    }

    @Override
    public int getFlags() {
        this.updateFlags();
        return this.flags;
    }

    @Override
    public int getKerning(int characterCode, int secondCharacterCode) {
        return this.getKerning(UnicodeConverter.getPostscriptForUnicode(characterCode), UnicodeConverter.getPostscriptForUnicode(secondCharacterCode));
    }

    @Override
    public int getKerning(String characterName, String secondCharacterName) {
        System.out.println("getKerningString: " + characterName + ", " + secondCharacterName);
        System.out.println(this.kerningPairs.get(new KerningKey(characterName, secondCharacterName)));
        Integer offset = kerningPairs.get(new KerningKey(characterName, secondCharacterName));
        if (offset != null) {
            return offset.intValue();
        }
        return 0;
    }

    /**
     * Updates the flag based on values of other attributes. 
     */
    private void updateFlags() {
        //Check for serif, all caps, etc?
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

    @Override
    public double[] getFontBoundingBox() {
        return this.fontBBox;
    }

    @Override
    public double getItalicAngle() {
        return this.italicAngle;
    }

    @Override
    public int getAscent() {
        return this.ascender;
    }

    @Override
    public int getDescent() {
        return this.descender;
    }

    @Override
    public int getLeading() {
        return 0;
    }

    @Override
    public int getCapHeight() {
        return this.capHeight;
    }

    @Override
    public int getXHeight() {
        return this.xHeight;
    }

    @Override
    public int getStemV() {
        return this.stdVW;
    }

    @Override
    public int getStemH() {
        return this.stdHW;
    }

    @Override
    public int getAvgWidth() {
        return this.averageWidth;
    }

    @Override
    public int getMaxWidth() {
        return this.maxWidth;
    }

    @Override
    public int getMissingWidth() {
        return this.averageWidth;
    }

    @Override
    public int getLastCharCode() {
        return this.lastChar;
    }

    @Override
    public List<Integer> getWidths() {
        List<Integer> widths = new ArrayList<Integer>();
        for (CharacterMetric cm : characterMetrics.values()) {
            widths.add(cm.getWx());
        }
        return widths;
    }

    @Override
    public List<Integer> getWidths(int firstCharCode, int lastCharCode) {
        List<Integer> widths = new ArrayList<Integer>();
        for (CharacterMetric cm : characterMetrics.values()) {
            int c = cm.getC();
            if (c >= firstChar && c <= lastChar) {
                widths.add(cm.getWx());
            }
        }
        return widths;
    }

    @Override
    public int getFirstCharCode() {
        return this.firstChar;
    }

    public String getFullName() {
        return fullName;
    }

    public String getWeight() {
        return weight;
    }

    public boolean isFixedPitch() {
        return isFixedPitch;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public int getUnderlinePosition() {
        return underlinePosition;
    }

    public int getUnderlineThickness() {
        return underlineThickness;
    }

    public String getEncodingScheme() {
        return encodingScheme;
    }

    /**
     * Inner class of AFMMetrics representing the metrics of a single character. 
     * @author Dylan de Wolff
     */
    private class CharacterMetric {
        private int c;
        private int wx;
        private String name;
        private int[] boundingBox;

        public static final int DEFAULT_C_VALUE = -1;
        public static final int DEFAULT_WX_VALUE = 250;

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
     * Used as key for the metrics map in AFMMetrics. Contains a character code and a character name, allowing 
     * access to values in the map by using either one of these atttributes.
     * @author Dylan de Wolff
     * @see AFMMetrics
     */
    private class CharacterKey {
        private final int characterCode;
        private final String characterName;
        private final boolean onlyCheckName;
        private final boolean onlyCheckCode;

        /**
         * Creates a new instance of CharacterKey with only a code. This will result in ignoring the name when comparing it to other character key instances.
         * @param code Character code for this key.
         */
        public CharacterKey(int code) {
            this.characterCode = code;
            this.characterName = "";
            this.onlyCheckName = false;
            this.onlyCheckCode = true;
        }

        /**
         * Creates a new instance of CharacterKey with only a name. This will result in ignoring the code when comparing it to other character key instances.
         * @param name Character name for this key.
         */
        public CharacterKey(String name) {
            this.characterCode = 0;
            this.characterName = name;
            this.onlyCheckName = true;
            this.onlyCheckCode = false;
        }

        /**
         * Creates a new instance of CharacterKey with both a name and code.
         * @param code Character code for this key.
         * @param name Character name for this key.
         */
        public CharacterKey(int code, String name) {
            this.characterCode = code;
            this.characterName = name;
            this.onlyCheckName = false;
            this.onlyCheckCode = false;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof CharacterKey) {
                CharacterKey other = (CharacterKey) o;
                if (onlyCheckName) {
                    return this.equalsName(other);
                } else if (onlyCheckCode) {
                    return this.equalsCode(other);
                } else {
                    return (this.equalsName(other) && this.equalsCode(other));
                }
            }
            return false;
        }

        private boolean equalsName(CharacterKey o) {
            if (characterName.equals(o.characterName)) {
                return true;
            }
            return false;
        }

        private boolean equalsCode(CharacterKey o) {
            if (characterCode == o.characterCode) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return characterCode + characterName.hashCode();
        }
    }

    /**
     * Used as key for the kerning map in AFMMetrics. Contains two character names.
     * @author Dylan de Wolff
     * @see AFMMetrics
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

    @Override
    public String getWidthOfString(String string, boolean kerning) {
        // TODO Auto-generated method stub
        return null;
    }
}
