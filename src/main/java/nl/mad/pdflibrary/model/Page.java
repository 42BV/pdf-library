package nl.mad.pdflibrary.model;

import java.util.List;

/**
 * Interface for page classes. Page classes store the data necessary to add a page to a document.
 * @author Dylan de Wolff
 *
 */
public interface Page extends DocumentPart {
    /**
     * Percentage of the page that should be filled.
     */
    public static final double CUT_OFF_POINT_PERCENTAGE = 0.9;
    /**
     * Default size of a new line.
     */
    public static final int DEFAULT_NEW_LINE_SIZE = 10;

    /**
     * Adds a new part to the page content.
     * @param part DocumentPart to add.
     * @return the page object.
     */
    Page add(DocumentPart part);

    /**
     * Sets the size of the page to the given size.
     * @param width width of the page.
     * @param height height of the page.
     * @return the page object.
     */
    Page size(int width, int height);

    /**
     * Returns the content of this page.
     * @return List containing DocumentParts.
     */
    List<DocumentPart> getContent();

    /**
     * Retrieves the width of the page.
     * @return Width of the page.
     */
    int getWidth();

    /**
     * Retrieves the height of the page.
     * @return Height of the page.
     */
    int getHeight();

    /**
     * Retrieves the filled width of this page.
     * @return Filled width of the page.
     */
    double getFilledWidth();

    /**
     * Retrieves the filled height of this page.
     * @return Filled height of the page.
     */
    double getFilledHeight();

    /**
     * Retrieves the filled width of this page.
     * @param filledWidth New filled width of the page.
     */
    void setFilledWidth(double filledWidth);

    /**
     * Retrieves the filled height of this page.
     * @param filledHeight New filled height of the page.
     */
    void setFilledHeight(double filledHeight);

    /**
     * Sets the page to use for overflowing content.
     */
    void overflowPage(Page page);

    /**
     * Returns the page that is used for overflowing content.
     * @return Page used for overflowing content.
     */
    Page getOverflowPage();

    /**
     * Sets the top margin of the page.
     * @param marginTop the top margin to set
     * @return This page.
     */
    Page marginTop(int marginTop);

    /**
     * @return the marginTop
     */
    int getMarginTop();

    /**
     * Sets the bottom margin of the page.
     * @param marginBottom the bottom margin to set
     * @return This page.
     */
    Page marginBottom(int marginBottom);

    /**
     * @return the bottom margin
     */
    int getMarginBottom();

    /**
     * Sets the right margin of the page.
     * @param marginRight the right margin to set
     * @return This page.
     */
    Page marginRight(int marginRight);

    /**
     * @return the right margin
     */
    int getMarginRight();

    /**
     * Sets the left margin of the page.
     * @param marginLeft the left margin to set
     * @return This page.
     */
    Page marginLeft(int marginLeft);

    /**
     * @return the left margin
     */
    int getMarginLeft();

    /**
     * @return height of the page without margins.
     */
    int getHeightWithoutMargins();

    /**
     * @return width of the page without margins.
     */
    int getWidthWithoutMargins();

    /**
     * @return the unfilled height.
     */
    double getRemainingHeight();

    /**
     * @return the unfilled width.
     */
    double getRemainingWidth();

    /**
     * Returns the content that have a explicit position.
     * @return list of document parts that have fixed positions
     */
    List<DocumentPart> getFixedPositionContent();

    /**
     * Returns the content that have no custom position specified.
     * @return List of document parts.
     */
    List<DocumentPart> getPositionlessContent();

    /**
     * Adds the list of parts to the content of this page.
     * @param parts List of parts to add.
     * @return the page.
     */
    Page addAll(List<DocumentPart> parts);

    /**
     * Calculates and returns an open position.
     * @param elementHeight height of the element that you are looking to position
     * @return Position that is available to use.
     */
    Position getOpenPosition(int elementHeight);

    /**
     * Calculates and returns an open position on or after the given height.
     * @param height Offset to find a position.
     * @return Position that is available to use.
     */
    Position getOpenPosition(int height, int elementHeight);

    /**
     * Returns the available spaces on the given line.
     * @param pos Position to check on.
     * @param ignoreSpacesBeforePositionWidth Whether or not to ignore open spaces that come before the given position's x value.
     * @return List of int arrays, each contains the start- and end point of the space.
     */
    List<int[]> getOpenSpacesOn(Position pos, boolean ignoreSpacesBeforePositionWidth);

    /**
     * Returns the available width at the given position.
     * @param position Position to check.
     * @return int containing the available width value.
     */
    int checkAvailableWidth(Position position);
}
