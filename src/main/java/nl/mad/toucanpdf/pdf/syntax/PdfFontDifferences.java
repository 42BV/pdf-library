package nl.mad.toucanpdf.pdf.syntax;

import java.util.List;
import java.util.Map;

import nl.mad.toucanpdf.model.Font;

/**
 * Specifies the methods that all font differences implementations should use.
 * Font differences is used to create a subset of a font and to create a custom encoding. This allows for the support of special characters.
 * @author Dylan
 *
 */
public interface PdfFontDifferences {

    /**
     * Updates the differences map based on the characters in the given string.
     * @param s String to process.
     */
    void updateDifferences(String s);

    /**
     * Returns the differences map.
     * @return Map with as key the name of the character and as value the new character code.
     */
    Map<String, Integer> getDifferences();

    /**
     * Converts the given string to use the new encoding.
     * @param s String to convert.
     * @return the processed string.
     */
    String convertString(String s);

    /**
     * Inserts a difference into the map.
     * @param characterName The character name.
     * @param characterCode The new character code.
     */
    void insertDifference(String characterName, int characterCode);

    /**
     * Generates a list of widths based on the new encoding.
     * @param font Font to retrieve the widths from.
     * @return List of widths that corresponds to the new encoding.
     */
    List<Integer> generateWidthList(Font font);

    /**
     * Returns the character name corresponding to the given character code.
     * @param code Code to check.
     * @return String containing the character name.
     */
    String getNameOf(int code);

    /**
     * Returns the character name corresponding to the given octal code.
     * @param octalCode Octal code to check.
     * @return String containing the character name.
     */
    String getNameOf(String octalCode);
}
