package org.toucanpdf.model.state;

import org.toucanpdf.model.PlaceableFixedSizeDocumentPart;

/**
 * This interface specifies the methods all fixed size document parts in the preview state should implement.
 * @author Dylan de Wolff
 *
 */
public interface StatePlaceableFixedSizeDocumentPart extends StatePlaceableDocumentPart, PlaceableFixedSizeDocumentPart {

    /**
     * Processes the size of the part and positions it accordingly.
     * @param page The page to add the part to.
     * @return boolean true if the document part causes overflow (doesn't fit on the page), false otherwise
     */
    boolean processContentSize(StatePage page);

    /**
     * Processes the size of the part and positions it accordingly.
     * @param page The page to add the part to.
     * @param wrapping Whether wrapping around the part should be allowed.
     * @param processAlignment Whether alignment should be applied.
     * @param fixed Whether the object has a fixed position.
     * @return boolean true if the document part causes overflow (doesn't fit on the page), false otherwise
     */
    boolean processContentSize(StatePage page, boolean wrapping, boolean processAlignment, boolean fixed);

}
