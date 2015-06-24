package nl.mad.toucanpdf.model.state;

import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.Position;

/**
 * Defines the methods required by state table cells. These methods are used for the positioning of the cell.
 * @author Dylan
 */
public interface StateCell extends Cell {
    /**
     * Returns the height required to fully draw this cell.
     * @param leading Leading of the page.
     * @return the required height.
     */
    double getRequiredHeight(double leading);

    /**
     * Returns the width required to fully draw this cell.
     * @return the required width.
     */
    double getRequiredWidth();

    /**
     * Sets the position of the cell.
     * @param position Position to use.
     * @return this cell instance.
     */
    Cell setPosition(Position position);

    /**
     * Returns the content of the cell.
     * @return content of the cell.
     */
    StateCellContent getStateCellContent();

    /**
     * Processes the positioning and size of the cell.
     * @param leading Leading of the page.
     */
    void processContentSize(double leading);

    /**
     * Sets the content of the cell.
     * @param content The content to use.
     */
    void setContent(StateCellContent content);
}
