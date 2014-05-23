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

    double getRequiredSpaceAboveLine();

    double getRequiredSpaceBelowLine();
}
