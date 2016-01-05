package org.toucanpdf.pdf.syntax;

import org.toucanpdf.model.Image;
import org.toucanpdf.model.Position;
import org.toucanpdf.utility.Constants;

/**
 * This class represents an image in the PDF syntax. This class should be added to an PdfStream in order to draw the image.
 * @author Dylan de Wolff
 *
 */
public class PdfImage extends PdfGraphicsState {
    private static final String SHOW_XOBJECT = " Do" + Constants.LINE_SEPARATOR_STRING;
    private static final String TRANSLATE = " cm" + Constants.LINE_SEPARATOR_STRING;

    /**
     * Creates a new instance of PdfImage.
     * @param resourceReference The reference to the image resource.
     * @param image The image to draw.
     */
    public PdfImage(String resourceReference, Image image) {
        super(PdfObjectType.IMAGE);
        addTranslation(image);
        drawObject(resourceReference);
    }

    /**
     * Specifies the image translation based on the given image.
     * @param image Image to draw.
     */
    private void addTranslation(Image image) {
        if (image != null) {
            Position pos = image.getPosition();
            this.addToByteRepresentation(image.getWidth() + " 0 0 " + image.getHeight() + " " + pos.getX() + " " + (pos.getY() - image.getHeight()) + TRANSLATE);
        }
    }

    /**
     * Specifies that the image should be drawn now.
     * @param resource Resource reference for the image.
     */
    private void drawObject(String resource) {
        this.addToByteRepresentation("/" + resource + SHOW_XOBJECT);
    }

}
