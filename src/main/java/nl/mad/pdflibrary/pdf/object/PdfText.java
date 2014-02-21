package nl.mad.pdflibrary.pdf.object;

import nl.mad.pdflibrary.document.Text;
import nl.mad.pdflibrary.pdf.PdfDocument;
import nl.mad.pdflibrary.pdf.utility.ByteEncoder;

/**
 * PdfText stores the pdf stream version of a Text object. 
 * @author Dylan de Wolff
 * @see PdfStream
 * @see Text
 */
public class PdfText extends AbstractPdfObject {

    public PdfText(Text text) {
        super(PdfObjectType.TEXT);
        this.addFont(PdfDocument.getPdfFont(text.getFont()), text.getFont().getSize());
        this.addPosition(text.getPositionX(), text.getPositionY());
        this.addText(text.getText());
    }

    public PdfText() {
        super(PdfObjectType.TEXT);
    }

    /**
     * Converts the given position values to a text matrix and adds this to the byte representation
     * This should be done before adding the text
     * @param positionX
     * @param positionY
     */
    public void addPosition(int positionX, int positionY) {
        String byteRep = "1 0 0 1 " + positionX + " " + positionY + " Tm\n";
        this.addToByteRepresentation(ByteEncoder.getBytes(byteRep));
    }

    //TODO: How to avoid hardcoding these strings?
    /**
     * Adds the byte representation for the given font and font size
     * This should be done before adding the text
     * @param font
     * @param fontSize
     */
    public void addFont(PdfIndirectObject font, int fontSize) {
        String byteRep = "/" + font.getReference().getResourceReference() + " " + fontSize + " Tf\n";
        this.addToByteRepresentation(ByteEncoder.getBytes(byteRep));
    }

    /**
     * Adds the byte representation for the given text
     * @param text
     */
    public void addText(String text) {
        String byteRep = "(" + text + ") Tj\n";
        this.addToByteRepresentation(ByteEncoder.getBytes(byteRep));
    }
}
