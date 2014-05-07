package nl.mad.toucanpdf.model;

/**
 * This interface specifies the methods that should be implemented by all document parts that are stored in the preview state.
 * @author Dylan de Wolff
 */
public interface StateDocumentPart extends DocumentPart {

    /**
     * Sets the original state's object that this state object represents.
     * @param originalObject Object this state object represents.
     */
    void setOriginalObject(DocumentPart originalObject);

    /**
     * Returns the object from the original state that this object represents.
     * @return The object from the original state this object represents.
     */
    DocumentPart getOriginalObject();
}
