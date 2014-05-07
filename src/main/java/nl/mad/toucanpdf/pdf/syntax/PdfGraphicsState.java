package nl.mad.toucanpdf.pdf.syntax;

import java.io.IOException;
import java.io.OutputStream;

import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.Position;

/**
 * This class represents a graphics state within the PDF specification. 
 * PdfGraphicsState offers several methods to translate and show an image. Custom drawings are not currently supported.
 * This class should be added to a stream in order to have the image show up in the document.
 * @author Dylan de Wolff
 *
 */
public class PdfGraphicsState extends AbstractPdfObject {
    private static final String SAVE_GRAPHICS_STATE = "q\n";
    private static final String RESTORE_GRAPHICS_STATE = "Q\n";
    private static final String SHOW_XOBJECT = " Do\n";
    private static final String TRANSLATE = " cm\n";

    /**
     * Creates a new instance of PdfGraphicsState.
     * @param resourceReference The resource reference for the image being drawn.
     * @param image Image to draw.
     */
    public PdfGraphicsState(String resourceReference, Image image) {
        super(PdfObjectType.IMAGE);
        this.setByteRepresentation(SAVE_GRAPHICS_STATE);
        addTranslation(image);
        drawObject(resourceReference);
    }

    /**
     * Specifies the image translation based on the given image.
     * @param image Image to draw.
     */
    private void addTranslation(Image image) {
        Position pos = image.getPosition();
        this.addToByteRepresentation(image.getWidth() + " 0 0 " + image.getHeight() + " " + pos.getX() + " " + (pos.getY() - image.getHeight()) + TRANSLATE);
    }

    /**
     * Specifies that the image should be drawn now.
     * @param resource Resource reference for the image.
     */
    private void drawObject(String resource) {
        this.addToByteRepresentation("/" + resource + SHOW_XOBJECT);
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        this.addToByteRepresentation(RESTORE_GRAPHICS_STATE);
        super.writeToFile(os);
    }
}
