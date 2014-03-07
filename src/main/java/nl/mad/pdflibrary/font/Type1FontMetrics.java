package nl.mad.pdflibrary.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.font.parser.AfmParser;
import nl.mad.pdflibrary.font.parser.AfmParser.CharacterMetric;
import nl.mad.pdflibrary.font.parser.PfbParser;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.utility.UnicodeConverter;

/**
 * This class is responsible for parsing afm files and storing the metrics data found.
 * @author Dylan de Wolff
 * @see FontMetrics
 */
public class Type1FontMetrics implements FontMetrics {
    private AfmParser afm;
    private PfbParser pfb;
    private String filename;
    private final double conversionToPoints = 0.001;

    /**
     * Creates a new instance of Type1FontMetrics and immediately parses the file corresponding to the given filename.
     * @param filename Font file to be parsed.
     * @throws IOException
     */
    public Type1FontMetrics(String filename) throws IOException {
        this.filename = filename;
        this.parseAfm();
    }

    /**
     * Creates a new AFMParser, which starts the parsing of the afm file, and stores the parser.
     * @throws IOException
     */
    public void parseAfm() {
        InputStream file = getFile(".afm");
        afm = new AfmParser(file);
    }

    /**
     * Finds the file by the given file name and returns it.
     * @param filename Name of the file.
     * @param extension File extension to look for.
     * @return InputStream for the given filename. Null if the file could not be found.
     * @throws FileNotFoundException
     */
    private InputStream getFile(String extension) {
        String localFilename = filename;
        //add afm extension if it could not be found.
        if (!localFilename.toLowerCase().endsWith(extension)) {
            localFilename += extension;
        }
        File file = new File(localFilename);
        //tries looking for the file in the resource folder
        if (!file.isFile()) {
            localFilename = FontMetrics.RESOURCE_LOCATION + localFilename;
            file = new File(localFilename);
        }
        if (file.isFile()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                System.err.print("File was not found.");
            }
        }
        return null;
    }

    @Override
    public byte[] getFontFile() {
        if (this.pfb != null) {
            return pfb.getPfbData();
        } else {
            return this.parsePfb();
        }
    }

    private byte[] parsePfb() {
        InputStream file = getFile(".pfb");
        if (file != null) {
            pfb = new PfbParser(file);
            return pfb.getPfbData();
        }
        return new byte[0];
    }

    @Override
    public int getWidth(int characterCode) {
        return this.getWidth(UnicodeConverter.getPostscriptForUnicode(characterCode));
    }

    @Override
    public int getWidth(String name) {
        CharacterMetric metric = afm.getCharacterMetric(name);
        if (metric != null) {
            return metric.getWx();
        }
        return 0;
    }

    @Override
    public String getFontFamily() {
        return afm.getFamilyName();
    }

    @Override
    public String getFontName() {
        return afm.getFontName();
    }

    @Override
    public int getFlags() {
        return afm.getFlags();
    }

    @Override
    public int getKerning(int characterCode, int secondCharacterCode) {
        return this.getKerning(UnicodeConverter.getPostscriptForUnicode(characterCode), UnicodeConverter.getPostscriptForUnicode(secondCharacterCode));
    }

    @Override
    public int getKerning(String characterName, String secondCharacterName) {
        return afm.getKerning(characterName, secondCharacterName);
    }

    @Override
    public double[] getFontBoundingBox() {
        return afm.getFontBBox();
    }

    @Override
    public double getItalicAngle() {
        return afm.getItalicAngle();
    }

    @Override
    public int getAscent() {
        return afm.getAscender();
    }

    @Override
    public int getDescent() {
        return afm.getDescender();
    }

    @Override
    public int getLeading() {
        return 0;
    }

    @Override
    public int getCapHeight() {
        return afm.getCapHeight();
    }

    @Override
    public int getXHeight() {
        return afm.getxHeight();
    }

    @Override
    public int getStemV() {
        return afm.getStdVW();
    }

    @Override
    public int getStemH() {
        return afm.getStdHW();
    }

    @Override
    public int getAvgWidth() {
        return afm.getAverageWidth();
    }

    @Override
    public int getMaxWidth() {
        return afm.getMaxWidth();
    }

    @Override
    public int getMissingWidth() {
        return afm.getAverageWidth();
    }

    @Override
    public int getLastCharCode() {
        return afm.getLastChar();
    }

    @Override
    public List<Integer> getWidths() {
        List<Integer> widths = new ArrayList<Integer>();
        for (CharacterMetric cm : afm.getCharacterMetrics().values()) {
            widths.add(cm.getWx());
        }
        return widths;
    }

    @Override
    public List<Integer> getWidths(int firstCharCode, int lastCharCode) {
        List<Integer> widths = new ArrayList<Integer>();
        for (CharacterMetric cm : afm.getCharacterMetrics().values()) {
            int c = cm.getC();
            if (c >= firstCharCode && c <= lastCharCode) {
                widths.add(cm.getWx());
            }
        }
        return widths;
    }

    @Override
    public int getWidthOfString(String string, int fontSize, boolean kerning) {
        int width = 0;
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            width += this.getWidth(UnicodeConverter.getPostscriptForUnicode((int) chars[i]));
            if (kerning && (i + 1 != chars.length)) {
                width -= this.getKerning((int) chars[i], (int) chars[i]);
            }
        }
        return width * fontSize;
    }

    @Override
    public double getWidthPoint(int charCode) {
        return this.getWidthPoint(UnicodeConverter.getPostscriptForUnicode(charCode));
    }

    @Override
    public double getWidthPoint(String charName) {
        return this.getWidth(charName) * conversionToPoints;
    }

    @Override
    public double getWidthPointOfString(String string, int fontSize, boolean kerning) {
        return (this.getWidthOfString(string, fontSize, kerning) * conversionToPoints);
    }

    @Override
    public int getFirstCharCode() {
        return afm.getFirstChar();
    }

    @Override
    public double getConversionToPointsValue() {
        return this.conversionToPoints;
    }

    @Override
    public int getLeadingForSize(int textSize) {
        return (int) (this.getAscent() * textSize * conversionToPoints) + DEFAULT_LEADING_ADDITION;
    }

    public String getFullName() {
        return afm.getFullName();
    }

    public String getWeight() {
        return afm.getWeight();
    }

    public boolean isFixedPitch() {
        return afm.isFixedPitch();
    }

    public String getCharacterSet() {
        return afm.getCharacterSet();
    }

    public int getUnderlinePosition() {
        return afm.getUnderlinePosition();
    }

    public int getUnderlineThickness() {
        return afm.getUnderlineThickness();
    }

    public String getEncodingScheme() {
        return afm.getEncodingScheme();
    }
}
