package nl.mad.toucanpdf.model.state;

import java.util.Map;

import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;

public interface StateSplittableText extends Text {
    /**
     * Returns map of positions and strings, each string represents a single line in the document based on the text from this object.
     * @return Map<Position, String> String map containing the lines and position of the lines.
     */
    Map<Position, String> getTextSplit();

    /**
     * Returns map containing the justification offset per line.
     * @return Map containing the offset per line.
     */
    Map<Position, Double> getJustificationOffset();

    /**
     * Returns the space required above each line.
     * @return double containing the required space.
     */
    double getRequiredSpaceAboveLine();

    /**
     * Returns the space required below each line.
     * @return double containing the required space.
     */
    double getRequiredSpaceBelowLine();
}
