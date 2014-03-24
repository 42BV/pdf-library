package nl.mad.pdflibrary.model;

import java.util.List;

/**
 * Interface for page classes. Page classes store the data necessary to add a page to a document.
 * @author Dylan de Wolff
 *
 */
public interface Page extends DocumentPart {

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
}
