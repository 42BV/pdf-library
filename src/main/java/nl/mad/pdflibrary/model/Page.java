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
    double CUT_OFF_POINT_PERCENTAGE = 0.7;
    /**
     * Default size of a new line.
     */
    int DEFAULT_NEW_LINE_SIZE = 3;

    /**
     * The minimal space required for wrapping.
     */
    int MINIMAL_AVAILABLE_SPACE_FOR_WRAPPING = 100;

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
     * @return int containing the space between two lines.
     */
    int getLeading();

    /**
     * Sets the leading value. The leading specifies the amount of space between two lines.
     * @param leading Space between two lines.
     * @return the page.
     */
    Page leading(int leading);

    /**
     * Sets the master page. The page will copy all attributes from the master page including content.
     * @param master The page to use as master page.
     * @return the page.
     */
    Page master(Page master);

    /**
     * Returns the master page of this page.
     * @return the master page.
     */
    Page getMasterPage();

}
