package org.toucanpdf.font;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.toucanpdf.font.parser.AfmParser;
import org.toucanpdf.font.parser.PfbParser;
import org.toucanpdf.model.FontMetrics;
import org.toucanpdf.utility.Constants;
import org.toucanpdf.utility.UnicodeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing afm files and storing the metrics data found.
 * @author Dylan de Wolff
 * @see FontMetrics
 */
public class Type1FontMetrics implements FontMetrics {
    private static final Logger LOGGER = LoggerFactory.getLogger(Type1FontMetrics.class);
    private AfmParser afm;
    private PfbParser pfb;
    private String filename;

    /**
     * Value used to convert from the unit used in afm to the unit used by PDF.
     */
    private static final double CONVERSION_TO_POINTS = 0.001;

    /**
     * Creates a new instance of Type1FontMetrics and immediately parses the file corresponding to the given filename.
     * @param filename Font file to be parsed.
     * @throws FileNotFoundException 
     */
    public Type1FontMetrics(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.parseAfm();
    }

    /**
     * Creates a new AFMParser, which starts the parsing of the afm file, and stores the parser.
     * @throws FileNotFoundException 
     */
    public void parseAfm() throws FileNotFoundException {
        InputStream file;
        file = getFile(".afm");
        afm = new AfmParser(file);
    }

    /**
     * Finds the file by the given file name and returns it.
     * @param extension File extension to look for.
     * @return InputStream for the given filename. Null if the file could not be found.
     * @throws FileNotFoundException
     */
    private InputStream getFile(String extension) throws FileNotFoundException {
        String localFilename = filename;
        //add afm extension if it could not be found.
        if (!localFilename.toLowerCase().endsWith(extension)) {
            localFilename += extension;
        }
        InputStream in = getClass().getResourceAsStream(Constants.RESOURCES + localFilename);
        if (in == null) {
            in = this.getClass().getClassLoader().getResourceAsStream(localFilename);
            if (in == null) {
                LOGGER.error("Could not find .afm file corresponding to the given filename: " + filename + ". You should not use this font any further.");
                throw new FileNotFoundException("Could not find .afm file corresponding to the given filename: " + filename);
            }
        }
        return in;
    }

    @Override
    public byte[] getFontFile() {
        if (this.pfb != null) {
            return pfb.getPfbData();
        } else {
            return this.parsePfb();
        }
    }

    /**
     * Starts parsing the pfb file if it hasn't been parsed already.
     * @return byte array containing the parsed file. Will be an empty array if the file could not be found.
     */
    private byte[] parsePfb() {
        InputStream file;
        try {
            file = getFile(".pfb");
        } catch (FileNotFoundException e) {
            LOGGER.info("Could not find Pfb file for {}, will continue without embedding Pfb data.", filename);
            return new byte[0];
        }
        pfb = new PfbParser(file);
        return pfb.getPfbData();
    }

    @Override
    public int getWidth(int characterCode) {
        return this.getWidth(UnicodeConverter.getPostscriptForUnicode(characterCode));
    }

    @Override
    public int getWidth(String name) {
        Type1CharacterMetric metric = afm.getCharacterMetric(name);
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
    public int getAscentForString(String text) {
        return getBoundingBoxValueForTextOnIndex(text, 3, true);
    }

    @Override
    public double getAscentForStringPoint(String text) {
        return getAscentForString(text) * CONVERSION_TO_POINTS;
    }

    @Override
    public int getDescent() {
        return afm.getDescender();
    }

    @Override
    public int getDescentForString(String text) {
        return getBoundingBoxValueForTextOnIndex(text, 1, false);
    }

    @Override
    public double getDescentForStringPoint(String text) {
        return getDescentForString(text) * CONVERSION_TO_POINTS;
    }

    private int[] getBoundingBoxForCharacter(String charName) {
        Type1CharacterMetric metric = afm.getCharacterMetric(charName);
        if (metric != null) {
            return metric.getBoundingBox();
        }
        return null;
    }

    /**
     * Returns either the lowest or highset bounding box value found within the text on the given bounding box index (e.g. the first bbox value is for descent so that means passing 1)
     * @param text Text to check the highest value for
     * @param bbIndex the index to check on
     * @param highest if true the method will return the highest value, if false the method will return the lowest value found
     * @return int containing the value
     */
    private int getBoundingBoxValueForTextOnIndex(String text, int bbIndex, boolean highest) {
        int value = 0;
        char[] chars = text.toCharArray();
        for (char aChar : chars) {
            int[] boundingBox = this.getBoundingBoxForCharacter(UnicodeConverter.getPostscriptForUnicode((int) aChar));
            if (boundingBox != null) {
                int bBoxValue = boundingBox[bbIndex];
                value = highest ? Math.max(value, bBoxValue) : Math.min(value, bBoxValue);
            }
        }
        return value;
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
        return afm.getCharacterMetrics().values().stream().map(Type1CharacterMetric::getWx).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getWidths(int firstCharCode, int lastCharCode) {
        List<Integer> widths = new ArrayList<Integer>();
        for (int i = firstCharCode; i < lastCharCode + 1; ++i) {
            Type1CharacterMetric cm = afm.getCharacterMetric(UnicodeConverter.getPostscriptForUnicode(i));
            if (cm != null) {
                widths.add(cm.getWx());
            } else {
                widths.add(0);
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
                width -= this.getKerning((int) chars[i], (int) chars[i + 1]);
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
        return this.getWidth(charName) * CONVERSION_TO_POINTS;
    }

    @Override
    public double getWidthPointOfString(String string, int fontSize, boolean kerning) {
        return (this.getWidthOfString(string, fontSize, kerning) * CONVERSION_TO_POINTS);
    }

    @Override
    public int getFirstCharCode() {
        return afm.getFirstChar();
    }

    @Override
    public double getConversionToPointsValue() {
        return CONVERSION_TO_POINTS;
    }

    @Override
    public double getLineHeightForSize(int textSize) {
        return (this.getAscent() + Math.abs(this.getDescent())) * textSize * CONVERSION_TO_POINTS;
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

    public String getFilename() {
        return this.filename;
    }

    @Override
    public int[] getFontProgramLengths() {
        return pfb.getLengths();
    }

    @Override
    public double getDescentPoint() {
        return (this.getDescent() * CONVERSION_TO_POINTS);
    }

    @Override
    public double getAscentPoint() {
        return (this.getAscent() * CONVERSION_TO_POINTS);
    }
}
