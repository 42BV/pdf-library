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
    public void setScaleX(double scaleX);

    /**
     * Returns the height scale of the text.
     * @return double containing the height scale.
     */
    public double getScaleY();

    /**
     * Sets the height scale of the text.
     * @param scaleY new scale.
     */
    public void setScaleY(double scaleY);

    /**
     * Returns the shear (tilt) of the text on the X-axis.
     * @return double containing the shear value.
     */
    public double getShearX();

    /**
     * Sets the shear (tilt) of the text on the X-axis.
     * @param shearX new shear.
     */
    public void setShearX(double shearX);

    /**
     * Returns the shear (tilt) of the text on the Y-axis.
     * @return double containing the shear value.
     */
    public double getShearY();

    /**
     * Sets the shear (tilt) of the text on the Y-axis.
     * @param shearY new shear.
     */
    public void setShearY(double shearY);

    /**
     * Returns the text value.
     * @return string containing the text.
     */
    public String getText();

    /**
     * Sets the text that this object should represent.
     * @param text String containing the new text.
     */
    public void setText(String text);

    /**
     * Returns the font that this text uses.
     * @return Font used by this text.
     */
    public Font getFont();

    /**
     * Sets the font that this text uses.
     * @param font Font to be used.
     */
    public void setFont(Font font);

    /**
     * Returns the size of the text.
     * @return Int containing the size of the text.
     */
    public int getTextSize();

    /**
     * Sets the size of the text.
     * @param textSize Size of the text.
     */
    public void setTextSize(int textSize);

    /**
     * Checks if the matrix of this text object is equal to the matrix of the given text object.
     * @param text Text object to check.
     * @return true if the matrices are equal, false otherwise.
     */
    public boolean textMatrixEquals(Text text);

}
