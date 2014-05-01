package nl.mad.toucanpdf.model;

import java.util.Map;

/**
 * Interface for text objects that are used in calculating the state of the document.
 * 
 * @author Dylan de Wolff
 */
public interface StateText extends Text, StatePlaceableDocumentPart {

    /**
     * Returns map of positions and strings, each string represents a single line in the document based on the text from this object.
     * @return Map<Position, String> String map containing the lines and position of the lines.
     */
    Map<Position, String> getTextSplit();

    /**
     * Processes the text to check how the text will be divided into lines and where exactly the text is going to be positioned. 
     * @param page Page the text is on.
     * @param positionX The starting position of the text.
     * @param fixedPosition Whether or not the text is on a fixed position.
     * @return a text object if there is overflow, null otherwise
     */
    StateText processContentSize(StatePage page, double positionX, boolean fixedPosition);

    /**
     * Determines the height of the content underneath the actual position of the text.
     * @param page Page the text is on.
     * @return double containing the height of the content.
     */
    double getContentHeightUnderBaseLine(Page page);

    /**
     * Returns map containing the justification offset per line.
     * @return Map containing the offset per line.
     */
    Map<Position, Double> getJustificationOffset();
}
