package org.toucanpdf.model.state;

import org.toucanpdf.model.Page;

/**
 * Interface for text objects that are used in calculating the state of the document.
 * 
 * @author Dylan de Wolff
 */
public interface StateText extends StatePlaceableDocumentPart, StateSplittableText {

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

}
