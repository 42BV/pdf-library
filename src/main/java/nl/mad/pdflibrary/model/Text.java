package nl.mad.pdflibrary.model;

/**
 * Interface for Text classes. Text classes store the data necessary to add text to a document. 
 * @author Dylan de Wolff
 *
 */
public interface Text extends PlaceableDocumentPart {

    /**
     * Returns the width scale of the text.
     * @return Double containing the width scale.
     */
    double getScaleX();

    /**
     * Sets the width scale of the text.
     * @param scaleX new scale.
     */
    void setScaleX(double scaleX);

    /**
     * Returns the height scale of the text.
     * @return double containing the height scale.
     */
    double getScaleY();

    /**
     * Sets the height scale of the text.
     * @param scaleY new scale.
     */
    void setScaleY(double scaleY);

    /**
     * Returns the shear (tilt) of the text on the X-axis.
     * @return double containing the shear value.
     */
    double getShearX();

    /**
     * Sets the shear (tilt) of the text on the X-axis.
     * @param shearX new shear.
     */
    void setShearX(double shearX);

    /**
     * Returns the shear (tilt) of the text on the Y-axis.
     * @return double containing the shear value.
     */
    double getShearY();

    /**
     * Sets the shear (tilt) of the text on the Y-axis.
     * @param shearY new shear.
     */
    void setShearY(double shearY);

    /**
     * Returns the text value.
     * @return string containing the text.
     */
    String getText();

    /**
     * Sets the text that this object should represent.
     * @param text String containing the new text.
     */
    void setText(String text);

    /**
     * Returns the font that this text uses.
     * @return Font used by this text.
     */
    Font getFont();

    /**
     * Sets the font that this text uses.
     * @param font Font to be used.
     */
    void setFont(Font font);

    /**
     * Returns the size of the text.
     * @return Int containing the size of the text.
     */
    int getTextSize();

    /**
     * Sets the size of the text.
     * @param textSize Size of the text.
     */
    void setTextSize(int textSize);

    /**
     * Checks if the matrix of this text object is equal to the matrix of the given text object.
     * @param text Text object to check.
     * @return true if the matrices are equal, false otherwise.
     */
    boolean textMatrixEquals(Text text);

}
