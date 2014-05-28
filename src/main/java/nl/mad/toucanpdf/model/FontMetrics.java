package nl.mad.toucanpdf.model;

import java.util.List;

/**
 * Interface for font metric.
 * @author Dylan de Wolff
 *
 */
public interface FontMetrics {

    /**
     * This value is added by default to any leading calculations.
     */
    int DEFAULT_LEADING_ADDITION = 5;

    /**
     * Returns width of the character corresponding to the given unicode character code.
     * @param characterCode Code of character.
     * @return Width of the character.
     */
    int getWidth(int characterCode);

    /**
     * Returns width of the character corresponding to the given character name.
     * @param name Name of the character.
     * @return Width of the character.
     */
    int getWidth(String name);

    /**
     * Returns the kerning (width offset between two characters) value between the given unicode character codes.
     * @param characterCode First character code.
     * @param secondCharacterCode Second character code.
     * @return int containing the kerning value.
     */
    int getKerning(int characterCode, int secondCharacterCode);

    /**
     * Returns the kerning (width offset between two characters) value between the given character names.
     * @param characterName Name of the first character.
     * @param secondCharacterName Name of the second character.
     * @return int containing the kerning value.
     */
    int getKerning(String characterName, String secondCharacterName);

    /**
     * Returns the name of the font as stated in the metrics file.
     * @return String containing the name.
     */
    String getFontName();

    /**
     * Returns the family of the font as stated in the metrics file.
     * @return String containing the family name.
     */
    String getFontFamily();

    /**
     * Returns integer containing flags specifying various characteristics of the font.
     * @return int containing the flag value.
     */
    int getFlags();

    /**
     * Returns the bounding box of the font.
     * @return Double array with the bounding box values.
     */
    double[] getFontBoundingBox();

    /**
     * Returns the angle of the font, anything other than zero means the font is italic.
     * @return double containing the angle.
     */
    double getItalicAngle();

    /**
     * Returns the ascent of the font.
     * @return int containing the ascent.
     */
    int getAscent();

    /**
     * Returns the descent of the font.
     * @return int containing the descent.
     */
    int getDescent();

    /**
     * Returns the descent of the font in points.
     * @return double containing the descent in points.
     */
    double getDescentPoint();

    /**
     * Returns the ascent of the font in points.
     * @return double containing the ascend in points.
     */
    double getAscentPoint();

    /**
     * Returns the leading of the font.
     * @return int containing the leading.
     */
    int getLeading();

    /**
     * Returns the cap height of the font.
     * @return int containing the cap height.
     */
    int getCapHeight();

    /**
     * Returns the X height of the font.
     * @return int containing the X height.
     */
    int getXHeight();

    /**
     * Returns the vertical stem value of the file.
     * @return int containing the stem value.
     */
    int getStemV();

    /**
     * Returns the horizontal stem value of the font.
     * @return int containing the stem value.
     */
    int getStemH();

    /**
     * Returns the average width of the characters in this font.
     * @return int containing average width.
     */
    int getAvgWidth();

    /**
     * Returns the widest character width of this font. 
     * @return int containing the maximum width.
     */
    int getMaxWidth();

    /**
     * Returns the character width that should be used if the width of a character is missing.
     * @return int containing the width.
     */
    int getMissingWidth();

    /**
     * Returns the character code of the last character in the font.
     * @return int containing the character code.
     */
    int getLastCharCode();

    /**
     * Returns the character code of the first character in the font.
     * @return int containing the character code.
     */
    int getFirstCharCode();

    /**
     * Returns a list containing the widths of all characters in the font.
     * @return List of integers containing width values.
     */
    List<Integer> getWidths();

    /**
     * Returns a list containing the widths of all characters from the given first character code to the last character code.
     * @param firstCharCode First character code.
     * @param lastCharCode Last character code.
     * @return List of integers containing width values.
     */
    List<Integer> getWidths(int firstCharCode, int lastCharCode);

    /**
     * Returns the width of the given string. Can also return the width of the string after kerning is applied.
     * @param string The string to calculate the width of.
     * @param fontSize The size of the font.
     * @param kerning Whether or not kerning should be applied.
     * @return Width of the string. 
     */
    int getWidthOfString(String string, int fontSize, boolean kerning);

    /**
     * Returns the width of the given string in points. Can also return the width of the string after kerning is applied.
     * @param string The string to calculate the width of.
     * @param fontSize The size of the font.
     * @param kerning Whether or not kerning should be applied.
     * @return Width of the string in points.
     */
    double getWidthPointOfString(String string, int fontSize, boolean kerning);

    /**
     * Returns width of the character corresponding to the given unicode character code in points.
     * @param characterCode Code of character.
     * @return Width of the character in points.
     */
    double getWidthPoint(int characterCode);

    /**
     * Returns width of the character corresponding to the given character name in points.
     * @param characterName Name of character.
     * @return Width of the character in points.
     */
    double getWidthPoint(String characterName);

    /**
     * Returns the font program file in bytes. This is needed for fully embedding a font.
     * @return The font program file in a byte array.
     */
    byte[] getFontFile();

    /**
     * Returns the value needed to convert the unit used by the font to the points unit.
     * @return double containing the value needed to execute the conversion.
     */
    double getConversionToPointsValue();

    /**
     * Returns the height for the given text size.
     * @param textSize Size of the text.
     * @return double containing the height value.
     */
    double getLineHeightForSize(int textSize);

    /**
     * Returns the lenghts for the font program.
     * @return int array containing the lenghts.
     */
    int[] getFontProgramLengths();

    /**
     * Returns the encoding used by this font.
     * @return String containing the encoding.
     */
    String getEncodingScheme();
}
