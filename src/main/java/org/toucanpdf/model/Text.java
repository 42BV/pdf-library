package org.toucanpdf.model;

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
     * Sets the width and height scale of the text.
     * @param scaleX new width scale.
     * @param scaleY new height scale.
     * @return the Text object
     */
    Text scale(double scaleX, double scaleY);

    /**
     * Sets the width scale of the text.
     * @param scaleX new scale.
     * @return the Text object
     */
    Text scaleX(double scaleX);

    /**
     * Returns the height scale of the text.
     * @return double containing the height scale.
     */
    double getScaleY();

    /**
     * Sets the height scale of the text.
     * @param scaleY new scale.
     * @return the Text object
     */
    Text scaleY(double scaleY);

    /**
     * Sets the x and y shearing for this text.
     * @param shearX The x shearing.
     * @param shearY The y shearing.
     * @return the Text object.
     */
    Text shear(double shearX, double shearY);

    /**
     * Returns the shear (tilt) of the text on the X-axis.
     * @return double containing the shear value.
     */
    double getShearX();

    /**
     * Sets the shear (tilt) of the text on the X-axis.
     * @param shearX new shear.
     * @return the Text object.
     */
    Text shearX(double shearX);

    /**
     * Returns the shear (tilt) of the text on the Y-axis.
     * @return double containing the shear value.
     */
    double getShearY();

    /**
     * Sets the shear (tilt) of the text on the Y-axis.
     * @param shearY new shear.
     * @return shearY
     */
    Text shearY(double shearY);

    /**
     * Returns the text value.
     * @return string containing the text.
     */
    String getText();

    /**
     * Sets the text that this object should represent.
     * @param text String containing the new text.
     * @return the Text object
     */
    Text text(String text);

    /**
     * Returns the font that this text uses.
     * @return Font used by this text.
     */
    Font getFont();

    /**
     * Sets the font that this text uses.
     * @param font Font to be used.
     * @return the Text object
     */
    Text font(Font font);

    /**
     * Returns the size of the text.
     * @return Int containing the size of the text.
     */
    int getTextSize();

    /**
     * Sets the size of the text.
     * @param textSize Size of the text.
     * @return the Text object
     */
    Text size(int textSize);

    /**
     * Places the text on the given position.
     * @param x Position x of the text.
     * @param y Position y of the text.
     * @return the Text object
     */
    @Override
    Text on(int x, int y);

    /**
     * Places the text on the given position.
     * @param position Position of the text.
     * @return the Text object
     */
    @Override
    Text on(Position position);

    /**
     * Checks if the matrix of this text object is equal to the matrix of the given text object.
     * @param text Text object to check.
     * @return true if the matrices are equal, false otherwise.
     */
    boolean textMatrixEquals(Text text);

    /**
     * Sets the alignment of the text.
     * @param alignment The alignment to use.
     * @return the Text object.
     */
    @Override
    Text align(Alignment alignment);

    /**
     * Sets the compression method to use for this text object. The default compression is Flate.
     * @param method The compression method to use.
     * @return the Text object.
     */
    Text compress(Compression method);

    /**
     * Returns the compression method this text object uses.
     * @return Compression object.
     */
    Compression getCompressionMethod();

    /**
     * Sets the top margin of the text.
     * @param marginTop the top margin to set
     * @return This text.
     */
    @Override
    Text marginTop(int marginTop);

    /**
     * Sets the bottom margin of the text.
     * @param marginBottom the bottom margin to set
     * @return This text.
     */
    @Override
    Text marginBottom(int marginBottom);

    /**
     * Sets the right margin of the text.
     * @param marginRight the right margin to set
     * @return This text.
     */
    @Override
    Text marginRight(int marginRight);

    /**
     * Sets the left margin of the text.
     * @param marginLeft the left margin to set
     * @return This text.
     */
    @Override
    Text marginLeft(int marginLeft);

    /**
     * Sets the color of the text
     * @param color Color to use
     * @return This text.
     */
    Text color(Color color);

    /**
     * Returns the color of the text
     * @return color used by this text
     */
    Color getColor();
}
