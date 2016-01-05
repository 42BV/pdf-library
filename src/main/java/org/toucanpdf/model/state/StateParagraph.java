package org.toucanpdf.model.state;

import java.util.List;

import org.toucanpdf.model.Paragraph;

/**
 * Interface for paragraph objects that are used in calculating the state of the document.
 * 
 * @author Dylan de Wolff
 */
public interface StateParagraph extends StatePlaceableDocumentPart, Paragraph {
    /**
     * Calculates the size of the content within the paragraph.
     * @param page Page the paragraph is on.
     * @param fixedPosition Whether the paragraph has fixed positioning.
     * @return a paragraph object if there is overflow, null otherwise
     */
    Paragraph processContentSize(StatePage page, boolean fixedPosition);

    /**
     * Returns the collection of StateText objects from this paragraph.
     * @return List containing the StateText objects.
     */
    List<StateText> getStateTextCollection();
}
