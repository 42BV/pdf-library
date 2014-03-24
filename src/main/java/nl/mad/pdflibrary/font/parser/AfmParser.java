package nl.mad.pdflibrary.font.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import nl.mad.pdflibrary.font.Type1CharacterMetric;
import nl.mad.pdflibrary.model.FontMetricsFlagValues;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is reponsible for parsing Afm files (contains Type 1 font metrics) and storing the data found. This is required to embed a Type 1 font.
 * 
 * @author Dylan de Wolff
 */
public class AfmParser {
    private final Logger logger = LoggerFactory.getLogger(AfmParser.class);
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
    private Map<String, Type1CharacterMetric> characterMetrics;
    private Map<KerningKey, Integer> kerningPairs;
    private int flags;
    private int averageWidth;
    private int maxWidth;
    private int firstChar;
    private int lastChar;
    private static final Map<String, ParsingAction> ACTION_MAP;

    static {
        ACTION_MAP = new HashMap<String, ParsingAction>();
        ACTION_MAP.put("FontName", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setFontName(st.nextToken());
            }
        });
        ACTION_MAP.put("FullName", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setFullName(st.nextToken());
            }
        });
        ACTION_MAP.put("FamilyName", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setFamilyName(st.nextToken());
            }
        });
        ACTION_MAP.put("Weight", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setWeight(st.nextToken());
            }
        });
        ACTION_MAP.put("ItalicAngle", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setItalicAngle(Double.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("IsFixedPitch", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setFixedPitch("true".equals(st.nextToken()));
            }
        });
        ACTION_MAP.put("CharacterSet", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setCharacterSet(st.nextToken());
            }
        });
        ACTION_MAP.put("FontBBox", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                double[] fontBoundingBox = { Double.valueOf(st.nextToken()), Double.valueOf(st.nextToken()), Double.valueOf(st.nextToken()),
                        Double.valueOf(st.nextToken()) };
                parser.setFontBBox(fontBoundingBox);
            }
        });
        ACTION_MAP.put("UnderLinePosition", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setUnderlinePosition(Integer.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("UnderlinePosition", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setUnderlinePosition(Integer.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("UnderlineThickness", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setUnderlineThickness(Integer.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("EncodingScheme", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setEncodingScheme(st.nextToken());
            }
        });
        ACTION_MAP.put("CapHeight", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setCapHeight(Integer.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("XHeight", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setXHeight(Integer.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("Ascender", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setAscender(Integer.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("Descender", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setDescender(Integer.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("StdHW", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setStdHW(Integer.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("StdVW", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.setStdVW(Integer.valueOf(st.nextToken()));
            }
        });
        ACTION_MAP.put("EndCharMetrics", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                parser.updateWidthAttributes();
                parser.updateCharAttributes();
                parser.updateFlags();
            }
        });
        ACTION_MAP.put("C", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                String token = st.nextToken();
                int c = Integer.valueOf(token);
                int wx = Type1CharacterMetric.DEFAULT_WX_VALUE;
                String name = "";
                int[] boundingBox = null;

                while (st.hasMoreTokens()) {
                    if ("WX".equalsIgnoreCase(token)) {
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
                Type1CharacterMetric cm = new Type1CharacterMetric(c, wx, name, boundingBox);
                parser.characterMetrics.put(name, cm);
            }
        });
        ACTION_MAP.put("KPX", new ParsingAction() {
            @Override
            public void execute(AfmParser parser, StringTokenizer st) {
                String firstCharacter = st.nextToken();
                String secondCharacter = st.nextToken();
                int widthOffset = -Integer.valueOf(st.nextToken());
                parser.createKerningEntry(firstCharacter, secondCharacter, widthOffset);
            }
        });

    }

    /**
     * Creates a new instance of AfmParser and parses the given file.
     * @param file InputStream for the afm file.
     */
    public AfmParser(InputStream file) {
        this();
        parse(new BufferedReader(new InputStreamReader(file)));
    }

    /**
     * Creates a new instance of AfmParser.
     */
    public AfmParser() {
        characterMetrics = new LinkedHashMap<String, Type1CharacterMetric>();
        kerningPairs = new LinkedHashMap<KerningKey, Integer>();
    }

    /**
     * Processes the given BufferedReader and stores the data found.
     * @param file BufferedReader to read from.
     */
    public final void parse(BufferedReader file) {
        if (file != null) {
            try {
                String currentLine = file.readLine();
                while (currentLine != null) {
                    StringTokenizer st = new StringTokenizer(currentLine, " ;\t\n\r\f");
                    String token = st.nextToken();

                    if (ACTION_MAP.containsKey(token)) {
                        ACTION_MAP.get(token).execute(this, st);
                    }
                    currentLine = file.readLine();
                }
                file.close();
            } catch (IOException e) {
                logger.error("IOException occured during the parsing of Afm file.");
            }
        }
    }

    /**
     * Updates the first char code and last char code.
     */
    private void updateCharAttributes() {
        int firstCharCode = characterMetrics.size();
        int lastCharCode = 0;
        for (Type1CharacterMetric cm : characterMetrics.values()) {
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
        for (Type1CharacterMetric cm : characterMetrics.values()) {
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

    protected void createKerningEntry(String firstCharacter, String secondCharacter, int widthOffset) {
        kerningPairs.put(new KerningKey(firstCharacter, secondCharacter), widthOffset);
    }

    private void setFontName(String fontName) {
        this.fontName = fontName;
    }

    private void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    private void setWeight(String weight) {
        this.weight = weight;
    }

    private void setItalicAngle(double italicAngle) {
        this.italicAngle = italicAngle;
    }

    private void setFixedPitch(boolean fixedPitch) {
        this.isFixedPitch = fixedPitch;
    }

    private void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    private void setFontBBox(double[] fontBBox) {
        this.fontBBox = fontBBox.clone();
    }

    private void setUnderlinePosition(int underlinePosition) {
        this.underlinePosition = underlinePosition;
    }

    private void setUnderlineThickness(int underlineThickness) {
        this.underlineThickness = underlineThickness;
    }

    private void setEncodingScheme(String encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    private void setCapHeight(int capHeight) {
        this.capHeight = capHeight;
    }

    private void setXHeight(int newXHeight) {
        this.xHeight = newXHeight;
    }

    private void setAscender(int ascender) {
        this.ascender = ascender;
    }

    private void setDescender(int descender) {
        this.descender = descender;
    }

    private void setStdHW(int stdHW) {
        this.stdHW = stdHW;
    }

    private void setStdVW(int stdVW) {
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
        return fontBBox.clone();
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
    public Map<String, Type1CharacterMetric> getCharacterMetrics() {
        return characterMetrics;
    }

    /**
     * Returns the character metric corresponding to the given name.
     * @param name Name of the character.
     * @return CharacterMetric for the given character, null if the character could not be found.
     */
    public Type1CharacterMetric getCharacterMetric(String name) {
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
     * Used for determining which action to execute when parsing the file.
     * @author Dylan de Wolff
     */
    private interface ParsingAction {
        void execute(AfmParser parser, StringTokenizer st);
    }

    /**
     * Used as key for the kerning map in AFMParser. Contains two character names.
     * @author Dylan de Wolff
     */
    private static class KerningKey {
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

        @Override
        public boolean equals(Object o) {
            if (o instanceof KerningKey) {
                KerningKey other = (KerningKey) o;
                if (characterName.equals(other.characterName) && secondCharacterName.equals(other.secondCharacterName)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return characterName.hashCode() + secondCharacterName.hashCode();
        }
    }
}
