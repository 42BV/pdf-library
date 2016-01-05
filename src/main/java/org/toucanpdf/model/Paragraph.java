package org.toucanpdf.model;

import java.util.List;

/**
 * Interface for paragraph classes. Paragraph objects store a collection of other document parts that should be kept together.
 * @author Dylan de Wolff
 */
public interface Paragraph extends PlaceableDocumentPart {

    /**
     * Adds text to the paragraph.
     * @param text Text to be added.
     * @return the paragraph.
     */
    Paragraph addText(Text text);

    /**
     * @param x The x position to place the paragraph.
     * @param y The y position to place the paragraph.
     * @return the paragraph.
     */
    Paragraph on(int x, int y);

    /**
     * @param position The position to place the paragraph.
     * @return the paragraph.
     */
    Paragraph on(Position position);

    /**
     * Returns list of all the text objects in this paragraph.
     * @return List containing all text objects in this paragraph.
     */
    List<Text> getTextCollection();

    /**
     * Adds a list of text to the paragraph.
     * @param textCollection List of text to add.
     * @return the paragraph.
     */
    Paragraph addText(List<Text> textCollection);

    /**
     * Adds an anchor to the paragraph. This allows for the addition of images and tables to a paragraph.
     * @param part The part you wish to attach to an anchor point.
     * @return The anchor instance.
     */
    Anchor addAnchor(PlaceableFixedSizeDocumentPart part);

    /**
     * Adds the given anchor to the paragraph. Anchors allow for the addition of images and tables to a paragraph.
     * @param anchor The anchor you wish to add to the paragraph.
     * @return The anchor instance.
     */
    Anchor addAnchor(Anchor anchor);

    /**
     * Returns a list containing the anchors for the given text.
     * @param t the text you want anchors from.
     * @return List of anchors.
     */
    List<Anchor> getAnchorsOn(Text t);

    /**
     * Sets the alignment of the paragraph, this overrides any alignment the text in this paragraph might have.
     * @param alignment The alignment to use.
     * @return the Paragraph object.
     */
    Paragraph align(Alignment alignment);

    /**
     * Returns a list containing all anchors of this paragraph.
     * @return List of anchors.
     */
    List<Anchor> getAnchors();

    /**
     * Sets the top margin of the paragraph.
     * @param marginTop the top margin to set
     * @return This paragraph.
     */
    Paragraph marginTop(int marginTop);

    /**
     * Sets the bottom margin of the paragraph.
     * @param marginBottom the bottom margin to set
     * @return This paragraph.
     */
    Paragraph marginBottom(int marginBottom);

    /**
     * Sets the right margin of the paragraph.
     * @param marginRight the right margin to set
     * @return This paragraph.
     */
    Paragraph marginRight(int marginRight);

    /**
     * Sets the left margin of the paragraph.
     * @param marginLeft the left margin to set
     * @return This paragraph.
     */
    Paragraph marginLeft(int marginLeft);
}
