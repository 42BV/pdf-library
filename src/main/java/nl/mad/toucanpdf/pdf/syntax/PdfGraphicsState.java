package nl.mad.toucanpdf.pdf.syntax;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class represents a graphics state within the PDF specification. 
 * This class is extended by other objects such as PdfImage or PdfTable. The PdfGraphicsState class itself only
 * offers the very basic functionality required for drawing graphical objects.
 * @see PdfImage
 * @see PdfTable
 * @author Dylan de Wolff
 *
 */
public abstract class PdfGraphicsState extends AbstractPdfObject {
    private static final String SAVE_GRAPHICS_STATE = "q\n";
    private static final String RESTORE_GRAPHICS_STATE = "Q\n";

    /**
     * Creates a new instance of PdfGraphicsState.
     * @param type Type of the graphics object.
     */
    public PdfGraphicsState(PdfObjectType type) {
        super(type);
        this.setByteRepresentation(SAVE_GRAPHICS_STATE);
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        this.addToByteRepresentation(RESTORE_GRAPHICS_STATE);
        super.writeToFile(os);
    }
}
