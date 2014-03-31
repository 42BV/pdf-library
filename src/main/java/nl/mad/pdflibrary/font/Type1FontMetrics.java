package nl.mad.pdflibrary.font;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.font.parser.AfmParser;
import nl.mad.pdflibrary.font.parser.PfbParser;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.utility.Constants;
import nl.mad.pdflibrary.utility.UnicodeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing afm files and storing the metrics data found.
 * @author Dylan de Wolff
 * @see FontMetrics
 */
public class Type1FontMetrics implements FontMetrics {
    private final static Logger LOGGER = LoggerFactory.getLogger(Type1FontMetrics.class);
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
     * @param filename Name of the file.
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
        //TODO: Remove this as soon as it's no longer necessary to run the library on its own
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
        InputStream file = null;
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
        for (Type1CharacterMetric cm : afm.getCharacterMetrics().values()) {
            widths.add(cm.getWx());
        }
        return widths;
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
    public int getLeadingForSize(int textSize) {
        return (int) (this.getAscent() * textSize * CONVERSION_TO_POINTS) + DEFAULT_LEADING_ADDITION;
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
}
