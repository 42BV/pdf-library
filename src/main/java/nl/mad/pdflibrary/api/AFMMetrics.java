package nl.mad.pdflibrary.api;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import nl.mad.pdflibrary.model.FontMetrics;

public class AFMMetrics implements FontMetrics {
    private String fontName;
    private String fullName;
    private String familyName;
    private String weight;
    private int italicAngle;
    private boolean isFixedPitch;
    private String characterSet;
    private int[] fontBBox;
    private int UnderlinePosition;
    private int UnderlineThickness;
    private String encodingScheme;
    private int CapHeight;
    private int xHeight;
    private int ascender;
    private int descender;
    private int stdHW;
    private int stdVW;

    private Map<String, CharacterMetric> characterMetrics;
    private Map<String, KerningPair> kerningPairs;

    private AFMMetrics() {
        characterMetrics = new HashMap<>();
        kerningPairs = new HashMap<>();
    }

    public static FontMetrics parse(String filename) throws IOException {
        AFMMetrics afm = new AFMMetrics();
        String currentLine;
        RandomAccessFile file = new RandomAccessFile(filename, "r");
        while ((currentLine = file.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(currentLine, " ;\t\n\r\f");
            String token = st.nextToken();
            if ("FontName".equalsIgnoreCase(token)) {
                afm.setFontName(st.nextToken());
            } else if ("FullName".equalsIgnoreCase(token)) {
                afm.setFontName(st.nextToken());
            } else if ("FamilyName".equalsIgnoreCase(token)) {
                afm.setFamilyName(st.nextToken());
            } else if ("Weight".equalsIgnoreCase(token)) {
                afm.setWeight(st.nextToken());
            } else if ("ItalicAngle".equalsIgnoreCase(token)) {
                afm.setItalicAngle(Integer.valueOf(st.nextToken()));
            } else if ("IsFixedPitch".equalsIgnoreCase(token)) {
                afm.setFixedPitch("true".equals(st.nextToken()));
            } else if ("CharacterSet".equalsIgnoreCase(token)) {
                afm.setCharacterSet(st.nextToken());
            } else if ("FontBBox".equalsIgnoreCase(token)) {
                int[] fontBBox = { Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken()),
                        Integer.valueOf(st.nextToken()) };
                afm.setFontBBox(fontBBox);
            } else if ("UnderLinePosition".equalsIgnoreCase(token)) {
                afm.setUnderlinePosition(Integer.valueOf(st.nextToken()));
            } else if ("UnderLineThickness".equalsIgnoreCase(token)) {
                afm.setUnderlineThickness(Integer.valueOf(st.nextToken()));
            } else if ("EncodingScheme".equalsIgnoreCase(token)) {
                afm.setEncodingScheme(st.nextToken());
            } else if ("CapHeight".equalsIgnoreCase(token)) {
                afm.setCapHeight(Integer.valueOf(st.nextToken()));
            } else if ("XHeight".equalsIgnoreCase(token)) {
                afm.setxHeight(Integer.valueOf(st.nextToken()));
            } else if ("Ascender".equalsIgnoreCase(st.nextToken())) {
                afm.setAscender(Integer.valueOf(st.nextToken()));
            } else if ("Descender".equalsIgnoreCase(st.nextToken())) {
                afm.setDescender(Integer.valueOf(st.nextToken()));
            } else if ("StdHW".equalsIgnoreCase(st.nextToken())) {
                afm.setStdHW(Integer.valueOf(st.nextToken()));
            } else if ("StdVW".equalsIgnoreCase(st.nextToken())) {
                afm.setStdVW(Integer.valueOf(st.nextToken()));
            }
        }
        return null;
    }

    @Override
    public int getWidth(char c) {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getItalicAngle() {
        return italicAngle;
    }

    public void setItalicAngle(int italicAngle) {
        this.italicAngle = italicAngle;
    }

    public boolean isFixedPitch() {
        return isFixedPitch;
    }

    public void setFixedPitch(boolean isFixedPitch) {
        this.isFixedPitch = isFixedPitch;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public int[] getFontBBox() {
        return fontBBox;
    }

    public void setFontBBox(int[] fontBBox) {
        this.fontBBox = fontBBox;
    }

    public int getUnderlinePosition() {
        return UnderlinePosition;
    }

    public void setUnderlinePosition(int underlinePosition) {
        UnderlinePosition = underlinePosition;
    }

    public int getUnderlineThickness() {
        return UnderlineThickness;
    }

    public void setUnderlineThickness(int underlineThickness) {
        UnderlineThickness = underlineThickness;
    }

    public String getEncodingScheme() {
        return encodingScheme;
    }

    public void setEncodingScheme(String encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    public int getCapHeight() {
        return CapHeight;
    }

    public void setCapHeight(int capHeight) {
        CapHeight = capHeight;
    }

    public int getxHeight() {
        return xHeight;
    }

    public void setxHeight(int xHeight) {
        this.xHeight = xHeight;
    }

    public int getAscender() {
        return ascender;
    }

    public void setAscender(int ascender) {
        this.ascender = ascender;
    }

    public int getDescender() {
        return descender;
    }

    public void setDescender(int descender) {
        this.descender = descender;
    }

    public int getStdHW() {
        return stdHW;
    }

    public void setStdHW(int stdHW) {
        this.stdHW = stdHW;
    }

    public int getStdVW() {
        return stdVW;
    }

    public void setStdVW(int stdVW) {
        this.stdVW = stdVW;
    }

    public Map<String, CharacterMetric> getCharacterMetrics() {
        return characterMetrics;
    }

    public void setCharacterMetrics(Map<String, CharacterMetric> characterMetrics) {
        this.characterMetrics = characterMetrics;
    }

    public Map<String, KerningPair> getKerningPairs() {
        return kerningPairs;
    }

    public void setKerningPairs(Map<String, KerningPair> kerningPairs) {
        this.kerningPairs = kerningPairs;
    }

    private class CharacterMetric {
        private int c;
        private float wx;
        private String name;
        private int[] boundingBox;

        public CharacterMetric(int c, float wx, String name, int[] boundingBox) {
            this.c = c;
            this.wx = wx;
            this.name = name;
            this.boundingBox = boundingBox;
        }

        public int getC() {
            return c;
        }

        public float getWx() {
            return wx;
        }

        public String getName() {
            return name;
        }

        public int[] getBoundingBox() {
            return boundingBox;
        }
    }

    private class KerningPair {
        private String secondCharacter;
        private int widthOffset;

        public KerningPair(String secondCharacter, int widthOffset) {
            this.secondCharacter = secondCharacter;
            this.widthOffset = widthOffset;
        }

        public String getSecondCharacter() {
            return secondCharacter;
        }

        public int getWidthOffset() {
            return widthOffset;
        }
    }
}
