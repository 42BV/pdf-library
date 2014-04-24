package nl.mad.pdflibrary.model;

/**
 * Alignment is used to specify the text alignment for text and paragraphs.
 * @author Dylan de Wolff
 */
public enum Alignment {
    /**
     * Aligns the text to the left side of the page. This is the default alignment. 
     */
    LEFT, /**
          * Aligns the text to the right side of the page. 
          */
    RIGHT, /**
           * Changes spreading between words to align both the left and right ends of each line.
           */
    JUSTIFIED, /**
               * Centers the text.
               */
    CENTERED
}
