package nl.mad.pdflibrary.document;

/**
 * DocumentPart is the abstract class for all the different mad parts that can be added to an instance of Document
 * 
 * @author Dylan de Wolff
 * @see Document
 */

public abstract class AbstractDocumentPart {
    /**
     * Type of this part
     */
    private DocumentPartType type;
    private int positionX;
    private int positionY;

    /**
     * @param type Type of this part
     */
    public AbstractDocumentPart(DocumentPartType type) {
        this.type = type;
    }

    public DocumentPartType getType() {
        return type;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
