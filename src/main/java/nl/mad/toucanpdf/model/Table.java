package nl.mad.toucanpdf.model;

import java.util.List;

public interface Table extends PlaceableFixedSizeDocumentPart {
    /**
     * Sets the alignment of the table.
     * @param alignment Alignment to use.
     * @return this table instance.
     */
    Table align(Alignment alignment);

    /**
     * Sets the width of the table.
     * @param width Width to use.
     * @return this table instance.
     */
    Table width(int width);

    /**
     * Sets the position of the table.
     * @param x The x value of the position.
     * @param y The y value of the position.
     * @return this table instance.
     */
    Table on(int x, int y);

    /**
     * Sets the position of the table.
     * @param position The position to use.
     * @return this table instance.
     */
    Table on(Position position);

    /**
     * Sets the compression method to use for this table. 
     * @param method Method to use.
     * @return this table instance.
     */
    Table compress(Compression method);

    /**
     * Returns the compression method that is currently set.
     * @return Compression method that is currently set.
     */
    Compression getCompressionMethod();

    /**
     * Allows you to set whether other parts may wrap around this table.
     * @param isWrappable determines whether wrapping is allowed. True if it is allowed, false otherwise.
     * @return this table instance.
     */
    Table allowWrapping(boolean isWrappable);

    /**
     * Sets the top margin of the table.
     * @param marginTop the top margin to set
     * @return This table.
     */
    Table marginTop(int marginTop);

    /**
     * Sets the bottom margin of the image.
     * @param marginBottom the bottom margin to set
     * @return This image.
     */
    Table marginBottom(int marginBottom);

    /**
     * Sets the right margin of the table.
     * @param marginRight the right margin to set
     * @return This table.
     */
    Table marginRight(int marginRight);

    /**
     * Sets the left margin of the table.
     * @param marginLeft the left margin to set
     * @return This table.
     */
    Table marginLeft(int marginLeft);

    Cell addCell(PlaceableDocumentPart part);

    Cell addCell(String s);

    Table addCell(Cell c);

    Table columns(int amountOfColumns);

    int getColumnAmount();

    List<Cell> getContent();

    /**
     * Sets the border width to use. The width is rounded to a single decimal.
     * @param borderWidth width to use.
     * @return this table instance.
     */
    Table border(double borderWidth);

    double getBorderWidth();
}
