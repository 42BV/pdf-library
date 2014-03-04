package nl.mad.pdflibrary.syntax;

import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.model.Text;
import nl.mad.pdflibrary.utility.ByteEncoder;

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
     * @param fontReference font for the text
     */
    public final void addText(Text text, PdfIndirectObject fontReference) {
        this.addFont(fontReference, text.getTextSize());
        this.addMatrix(text);
        this.addTextString(text);
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
    public void addTextString(Text text) {
        String byteRep = processKerning(text.getText(), text.getFont());
        this.addToByteRepresentation(ByteEncoder.getBytes(byteRep));
    }

    private String processKerning(String text, Font font) {
        FontMetrics metrics = font.getBaseFont().getMetricsForStyle(font.getStyle());
        StringBuilder sb = new StringBuilder("[(");
        for (int i = 0; i < text.length(); ++i) {
            sb.append(text.charAt(i));
            if (text.length() != i + 1) {
                int kernWidth = metrics.getKerning((int) text.charAt(i), (int) text.charAt(i + 1));
                if (kernWidth != 0) {
                    sb.append(") ");
                    sb.append(kernWidth);
                    sb.append(" (");
                }
            }
        }
        sb.append(")]TJ\n");
        return sb.toString();
    }
}
