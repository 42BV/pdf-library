package org.toucanpdf.model.state;

import java.util.List;

import org.toucanpdf.model.Page;
import org.toucanpdf.model.PlaceableDocumentPart;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Space;

/**
 * This interface specifies the methods that should be implemented by all placeable document parts in the preview state.
 * @author Dylan de Wolff
 *
 */
public interface StatePlaceableDocumentPart extends StateDocumentPart, PlaceableDocumentPart, StateSpacing {
    /**
     * Returns the height of this part's content. This value will not always be representative of the actual height of the part if the position 
     * was not specified by the user.
     * @param page page the content will be on.
     * @return double containing height.
     */
    double getContentHeight(Page page);

    /**
     * Returns the width of this part's content. This value will not always be representative of the actual width of the part if the position 
     * was not specified by the user.
     * @param page Page the content will be on.
     * @param position Position to check the width of.
     * @return int containing width.
     */
    double getContentWidth(Page page, Position position);

    /**
     * Returns x values of positions of this object at the given height.
     * @param height Height to check.
     * @return the x values of the positions (there can be several different positions at the same height due to text wrapping and such)
     * of this object at the given height. Returns -1 if the object is not at the given height.
     */
    int[] getPositionAt(double height);

    /**
     * Returns a list of int arrays containing the spaces used by the text. Each int array contains the starting x value of the text and the ending x value.
     * @param height the y value where the spaces should be retrieved for.
     * @param pageWidth the width of the page the content is on.
     * @return List of spaces.
     */
    List<Space> getUsedSpaces(double height, int pageWidth);
}
