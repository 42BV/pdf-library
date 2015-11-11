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

    /**
     * Adds a cell to the table with the given part as it's content.
     * @param part Part to use as content.
     * @return the Cell that was added.
     */
    Cell addCell(PlaceableDocumentPart part);

    /**
     * Adds a cell with the given text as it's content.
     * @param s String to use as content.
     * @return the Cell that was added.
     */
    Cell addCell(String s);

    /**
     * Adds the given cell to the table.
     * @param c Cell to add.
     * @return the Cell that was added.
     */
    Table addCell(Cell c);

    /**
     * Sets the amount of columns the table has.
     * @param amountOfColumns Amount of columns.
     * @return The table instance.
     */
    Table columns(int amountOfColumns);

    /**
     * Returns the amount of columns.
     * @return int containing the amount of columns.
     */
    int getColumnAmount();

    /**
     * Returns the list of cells.
     * @return List of cells.
     */
    List<Cell> getContent();

    /**
     * Sets the border width to use. The width is rounded to a single decimal.
     * @param borderWidth width to use.
     * @return this table instance.
     */
    Table border(double borderWidth);

    /**
     * Returns the border width.
     * @return Double containing border width.
     */
    double getBorderWidth();

    /**
     * Sets whether generated cells should be drawn.
     * @param draw True if filler cells should be drawn, false otherwise.
     * @return This table instance.
     */
    Table drawFillerCells(boolean draw);

    /**
     * Returns whether generated cells will be drawn.
     * @return true if filler cells are drawn, false otherwise.
     */
    boolean getDrawFiller();

    /**
     * Removes all content from the table.
     * @return The table instance.
     */
    Table removeContent();

    /**
     * Whether or not headers should be repeated in case the table does not fit and has to be spread over multiple pages.
     * @param repeat True if the header should be repeated, false otherwise.
     * @return
     */
    Table repeatHeader(boolean repeat);

    /**
     * Returns whether the table headers are repeated.
     * @return boolean, true if repeating, false otherwise
     */
    boolean isRepeatingHeader();

    /**
     * Sets the padding used by the table
     * @param padding The padding to use.
     * @return the table instance.
     */
    Table padding(double padding);

    /**
     * Returns the padding used by the table
     * @return padding value used by table
     */
    double getPadding();

    /**
     * Returns whether the table content is vertically aligned or not
     * @return true if the content is vertically aligned, false otherwise
     */
    boolean isVerticalAligned();

    /**
     * Sets whether the table content should be vertically aligned or not
     * @param verticalAlign true if the content should be vertically aligned, false otherwise
     * @return this table instance
     */
    Table verticalAlign(boolean verticalAlign);

    /**
     * Returns whether the header (first row) of the table will be prioritized during the width determination.
     * If enabled the width calculator will try to spread the width in such a way that each header will fit without cutoff.
     * If disabled (default) the minimal width for each cell will be determined by the largest word or piece of content in the column.
     * The preferred option is to leave this option disabled unless dealing with very large tables for which the header row won't fit properly,
     * in that case this option will at least make the header row look better (usually at the cost of how well the rest of the table content looks!).
     * @return true if the header width will be prioritized, false otherwise
     */
    boolean isPrioritizingHeaderWidth();

    /**
     * Sets whether the table should prioritize the header (first row) minimal widths during width determination.
     * If enabled the width calculator will try to spread the width in such a way that each header will fit without cutoff.
     * If disabled (default) the minimal width for each cell will be determined by the largest word or piece of content in the column.
     * The preferred option is to leave this option disabled unless dealing with very large tables for which the header row won't fit properly,
     * in that case this option will at least make the header row look better (usually at the cost of how well the rest of the table content looks!).
     * @param prioritizeHeaderWidth true if the header rows widths should be prioritized, false otherwise
     * @return thist able instance
     */
    Table prioritizeHeaderWidth(boolean prioritizeHeaderWidth);

}
