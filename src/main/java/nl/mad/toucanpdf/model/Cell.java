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
     * Sets the amount of columns that this cells uses. If the given column span exceeds the total column count it will be reduced automatically.
     * If this cell has a larger column span than the amount of columns available in the current row, the remainder of that row will be filled with empty cells.
     * @param newColumnSpan The column span to use.
     * @return the cell instance.
     */
    Cell columnSpan(int newColumnSpan);

    /**
     * Sets the amount of rows that this cell uses. 
     * @param newRowSpan The row span to use.
     * @return the cell instance.
     */
    Cell rowSpan(int newRowSpan);

    /**
     * Sets the border size of the cell
     * @param borderSize The border size to use.
     * @return the cell instance.
     */
    Cell border(double borderSize);

    /**
     * Returns the column span of this cell.
     * @return column span value.
     */
    int getColumnSpan();

    /**
     * Returns the row span of this cell.
     * @return row span value.
     */
    int getRowSpan();

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
     * Returns the width of the border.
     * @return the border width.
     */
    Double getBorderWidth();

    /**
     * Returns the position of this cell.
     * @return the position of the cell.
     */
    Position getPosition();

    /**
     * Sets the content of this cell.
     * @param part Part to use as content.
     * @return The cell instance.
     */
    Cell content(PlaceableDocumentPart part);
}
