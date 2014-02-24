package nl.mad.pdflibrary.syntax;

import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.Text;
import nl.mad.pdflibrary.utility.ByteEncoder;
import nl.mad.pdflibrary.utility.PdfConstants;

/**
 * PdfText stores the PDF stream version of a Text object. 
 * @author Dylan de Wolff
 * @see PdfStream
 * @see nl.mad.pdflibrary.model.Text
 */
public class PdfText extends AbstractPdfObject {

    /**
     * Creates a new instance of PdfText.
     * @param text The text that needs to be represented.
     */
    public PdfText(Text text, PdfIndirectObject font) {
        super(PdfObjectType.TEXT);
        this.addText(text, font);
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
     * @param font font for the text
     */
    public final void addText(Text text, PdfIndirectObject font) {
        this.addFont(font, text.getTextSize());
        this.addMatrix(text);
        this.addTextString(text.getText());
    }

    /**
     * Converts the given position values to a text matrix and adds this to the byte representation.
     * This should be done before adding the text.
     * @param text text to add to the document
     */
    public void addMatrix(Text text) {
        String byteRep = text.getScaleX() + " " + text.getShearX() + " " + text.getShearY() + " " + text.getScaleY() + " " + text.getPositionX() + " "
                + text.getPositionY() + " Tm\n";
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
