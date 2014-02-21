package nl.mad.pdflibrary.pdf.object;

import nl.mad.pdflibrary.document.Text;
import nl.mad.pdflibrary.pdf.PdfDocument;
import nl.mad.pdflibrary.pdf.utility.ByteEncoder;

/**
 * PdfText stores the PDF stream version of a Text object. 
 * @author Dylan de Wolff
 * @see PdfStream
 * @see Text
 */
public class PdfText extends AbstractPdfObject {

    /**
     * Creates a new instance of PdfText.
     * @param text The text that needs to be represented.
     */
    public PdfText(Text text) {
        super(PdfObjectType.TEXT);
        this.addText(text);
    }

    /**
     * Creates a new instance of PdfText.
     */
    public PdfText() {
        super(PdfObjectType.TEXT);
    }

    /**
     * Adds the given Text object to the stream.
     * @param text Text object to be added.
     */
    public final void addText(Text text) {
        this.addFont(PdfDocument.getPdfFont(text.getFont()), text.getFont().getSize());
        this.addPosition(text.getPositionX(), text.getPositionY());
        this.addTextString(text.getText());
    }

    /**
     * Converts the given position values to a text matrix and adds this to the byte representation.
     * This should be done before adding the text.
     * @param positionX X Position of the text.
     * @param positionY Y position of the text.
     */
    public void addPosition(int positionX, int positionY) {
        String byteRep = "1 0 0 1 " + positionX + " " + positionY + " Tm\n";
        this.addToByteRepresentation(ByteEncoder.getBytes(byteRep));
    }

    //TODO: How to avoid hardcoding these strings?
    /**
     * Adds the byte representation for the given font and font size.
     * This should be done before adding the text.
     * @param font IndirectObject containing the font.
     * @param fontSize Size of the font.
     */
    public void addFont(PdfIndirectObject font, int fontSize) {
        String byteRep = "/" + font.getReference().getResourceReference() + " " + fontSize + " Tf\n";
        this.addToByteRepresentation(ByteEncoder.getBytes(byteRep));
    }

    /**
     * Adds the byte representation for the given text string.
     * @param text String that is to be added.
     */
    public void addTextString(String text) {
        String byteRep = "(" + text + ") Tj\n";
        this.addToByteRepresentation(ByteEncoder.getBytes(byteRep));
    }
}
