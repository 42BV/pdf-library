package nl.pdflibrary.pdf.object;

import nl.pdflibrary.document.AbstractDocumentPart;
import nl.pdflibrary.document.Text;
import nl.pdflibrary.pdf.PdfDocument;

//text commands shouldn't be coming out of nowhere!
/**
 * PdfTextStream is used for containing and displaying text on the page
 * @author Dylan de Wolff
 */
public class PdfTextStream extends PdfStream {
    /**
     * Specifies the command used to specify the start of a text stream
     */
    private static final byte[] BEGIN_TEXT_STREAM = "BT\n".getBytes();
    /**
     * Specifies the command used to specify the end of a text stream
     */
    private static final byte[] END_TEXT_STREAM = "ET\n".getBytes();

    /**
     * Creates a new instance of PdfTextStream
     */
    public PdfTextStream() {
        super();
    }

    public PdfTextStream(AbstractDocumentPart part) {
        super();
        this.addCommands(part);
    }

    /* (non-Javadoc)
     * @see PDF.PdfStream#addCommands(Document.DocumentPart)
     */
    @Override
    public void addCommands(AbstractDocumentPart part) {
        if (part instanceof Text) {
            Text text = (Text) part;
            this.addCommand(PdfDocument.getPdfFont(text.getFont()), text.getFont().getSize());
            this.addCommand(text.getPositionX(), text.getPositionY());
            this.addCommand(text.getText());
        }
    }

    /**
     * Adds the necessary commands for a font
     * @param font
     * @param fontSize
     */
    public void addCommand(PdfIndirectObject font, int fontSize) {
        String byteRep = "/" + font.getReference().getResourceReference() + " " + fontSize + " Tf\n";
        this.addToByteRepresentation(byteRep.getBytes());
    }

    /**
     * Adds the necessary commands for text
     * @param text
     */
    public void addCommand(String text) {
        String byteRep = "(" + text + ") Tj\n";
        this.addToByteRepresentation(byteRep.getBytes());
    }

    /**
     * Adds the necessary commands for positioning of text
     * @param x
     * @param y
     */
    public void addCommand(int x, int y) {
        String byteRep = "1 0 0 1 " + x + " " + y + " Tm\n";
        this.addToByteRepresentation(byteRep.getBytes());
    }

    @Override
    protected byte[] getWriteBeforeStreamContent() {
        return BEGIN_TEXT_STREAM;
    }

    @Override
    protected byte[] getWriteAfterStreamContent() {
        return END_TEXT_STREAM;
    }

}
