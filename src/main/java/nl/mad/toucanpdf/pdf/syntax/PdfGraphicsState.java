package nl.mad.toucanpdf.pdf.syntax;

import java.io.IOException;
import java.io.OutputStream;

import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.Position;

public class PdfGraphicsState extends AbstractPdfObject {
    private static final String SAVE_GRAPHICS_STATE = "q\n";
    private static final String RESTORE_GRAPHICS_STATE = "Q\n";
    private static final String SHOW_XOBJECT = " Do\n";
    private static final String TRANSLATE = " cm\n";

    public PdfGraphicsState(String resourceReference, Image image) {
        super(PdfObjectType.IMAGE);
        this.setByteRepresentation(SAVE_GRAPHICS_STATE);
        addTranslation(image);
        drawObject(resourceReference);
    }

    public void addTranslation(Image image) {
        Position pos = image.getPosition();
        this.addToByteRepresentation(image.getWidth() + " 0 0 " + image.getHeight() + " " + pos.getX() + " " + (pos.getY() - image.getHeight()) + TRANSLATE);
    }

    public void drawObject(String resource) {
        this.addToByteRepresentation("/" + resource + SHOW_XOBJECT);
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        this.addToByteRepresentation(RESTORE_GRAPHICS_STATE);
        super.writeToFile(os);
    }
}
