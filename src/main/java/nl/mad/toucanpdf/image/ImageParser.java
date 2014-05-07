package nl.mad.toucanpdf.image;

import nl.mad.toucanpdf.model.ColorSpace;
import nl.mad.toucanpdf.model.Compression;
import nl.mad.toucanpdf.pdf.syntax.PdfImageDictionary;

/**
 * Interface for image parsers. This interface contains methods each ImageParser should implement.
 * These methods are needed to fill the PdfImageDictionary object.
 * @author Dylan de Wolff
 * @see PdfImageDictionary
 */
public interface ImageParser {

    /**
     * Returns the data for this image.
     * @return byte array containing the data for this image.
     */
    byte[] getData();

    /**
     * Returns the color space for this image.
     * @return The color space used for this image.
     */
    ColorSpace getColorSpace();

    /**
     * Returns the amount of bits per component this image uses.
     * @return The amount of bits per component.
     */
    int getBitsPerComponent();

    /**
     * Returns the default filter that should be applied.
     * @return The default filter of the image.
     */
    Compression getFilter();

    /**
     * Returns the original width of the image.
     * @return The original width.
     */
    int getWidth();

    /**
     * Returns the original height of the image.
     * @return The original height.
     */
    int getHeight();
}
