package org.toucanpdf.model;

import org.toucanpdf.api.BaseImage;

/**
 * This interface specifies which methods any Image class should implement. 
 * Instances of classes that implement this interface can be added to the DocumentBuilder in order to add images to the document.
 * @author Dylan de Wolff
 * @see BaseImage
 */
public interface Image extends PlaceableFixedSizeDocumentPart {

    /**
     * Sets the alignment of the image.
     * @param alignment Alignment to use.
     * @return this image instance.
     */
    @Override
    Image align(Alignment alignment);

    /**
     * Sets the height of the image.
     * @param height Height to use.
     * @return this image instance.
     */
    Image height(int height);

    /**
     * Sets the height of the image.
     * @param height Height to use.
     * @param scaleWidth Whether you wish to automatically scale the width of the image.
     * @return this image instance.
     */
    Image height(int height, boolean scaleWidth);

    /**
     * Sets the width of the image.
     * @param width Width to use.
     * @return this image instance.
     */
    Image width(int width);

    /**
     * Sets the width of the image.
     * @param width Width to use.
     * @param scaleHeight Whether you wish to automatically scale the height of the image.
     * @return this image instance.
     */
    Image width(int width, boolean scaleHeight);

    /**
     * Sets the position of the image.
     * @param x The x value of the position.
     * @param y The y value of the position.
     * @return this image instance.
     */
    @Override
    Image on(int x, int y);

    /**
     * Sets the position of the image.
     * @param position The position to use.
     * @return this image instance.
     */
    @Override
    Image on(Position position);

    /**
     * Returns the image parser.
     * @return The image parser used.
     */
    ImageParser getImageParser();

    /**
     * Sets the compression method to use for this image. Any default filter that apply for
     * the image format you are using are applied automatically. The compression that 
     * you specify here will be used on top of that.
     * @param method Method to use.
     * @return this image instance.
     */
    Image compress(Compression method);

    /**
     * Returns the compression method that is currently set.
     * @return Compression method that is currently set.
     */
    Compression getCompressionMethod();

    /**
     * Allows you to set whether other parts may wrap around this image.
     * @param isWrappable determines whether wrapping is allowed. True if it is allowed, false otherwise.
     * @return this image instance.
     */
    Image allowWrapping(boolean isWrappable);

    /**
     * Sets the top margin of the image.
     * @param marginTop the top margin to set
     * @return This image.
     */
    @Override
    Image marginTop(int marginTop);

    /**
     * Sets the bottom margin of the image.
     * @param marginBottom the bottom margin to set
     * @return This image.
     */
    @Override
    Image marginBottom(int marginBottom);

    /**
     * Sets the right margin of the image.
     * @param marginRight the right margin to set
     * @return This image.
     */
    @Override
    Image marginRight(int marginRight);

    /**
     * Sets the left margin of the image.
     * @param marginLeft the left margin to set
     * @return This image.
     */
    @Override
    Image marginLeft(int marginLeft);

    /**
     * Sets whether the image colors should be inverted.
     * @param invert Boolean determining if the colors should be inverted.
     * @return This image instance.
     */
    Image invertColors(boolean invert);

    /**
     * Returns whether the image colors are to be inverted.
     * @return true if the colors are to be inverted, false otherwise.
     */
    boolean getInvertColors();
}
