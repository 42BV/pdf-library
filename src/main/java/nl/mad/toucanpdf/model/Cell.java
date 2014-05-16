package nl.mad.toucanpdf.model;

/**
 * Defines the methods required by table cells. Cell objects can be added to a table and represent a single column.
 * @author Dylan de Wolff
 *
 */
public interface Cell {
    /**
     * Returns the content of this cell.
     * @return the content from this cell.
     */
    PlaceableDocumentPart getContent();

    /**
     * Sets the width of the cell. The specified width will be maintained unless the combined cell widths are too large
     * for the table.
     * @param newWidth The width to use.
     * @return the cell instance.
     */
    Cell width(double newWidth);

    /** 
     * Sets the height of the cell. The actual height of the column will eventually be determined 
     * by the highest value from either the specified height or the total height of the content. 
     * @param newHeight The height to use.
     * @return the cell instance.
     */
    Cell height(double newHeight);

    /**
     * Sets the amount of columns that this cells uses. The column span will eventually be automatically reduced
     * to the amount of columns in the table. If the appended cell has a larger column span than the amount of available
     * columns on the current row, the row will be filled up with empty columns.
     * @param newColumnSpan The column span to use.
     * @return the cell instance.
     */
    Cell columnSpan(int newColumnSpan);

    /**
     * Returns the column span of this cell.
     * @return column span value.
     */
    int getColumnSpan();

    /**
     * Returns the width of this cell.
     * @return the width value.
     */
    double getWidth();

    /**
     * Returns the height of this cell.
     * @return the height value.
     */
    double getHeight();

    /**
     * Returns the position of this cell.
     * @return the position of the cell.
     */
    Position getPosition();
}
