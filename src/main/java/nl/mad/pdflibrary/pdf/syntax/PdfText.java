package nl.mad.pdflibrary.pdf.syntax;

import java.util.Map;
import java.util.Map.Entry;

import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;

/**
 * PdfText stores the PDF stream version of a Text object. 
 * @author Dylan de Wolff
 * @see PdfStream
 * @see nl.mad.pdflibrary.model.Text
 */
public class PdfText extends AbstractPdfObject {

    /**
     * Creates a new instance of PdfText.
     */
    public PdfText() {
        super(PdfObjectType.TEXT);
    }

    /**
     * Adds the given Text object to the stream.
     * @param text Text object to be added.
     * @param fontReference font for the text.
     * @return String containing overflow.
     */
    public String addText(Text text, PdfIndirectObject fontReference) {
        this.addFont(fontReference, text.getTextSize());
        this.addMatrix(text);
        return this.addTextString(text);
    }

    /**
     * Converts the given position values to a text matrix and adds this to the byte representation.
     * This should be done before adding the text.
     * @param text text to add to the document.
     */
    public void addMatrix(Text text) {
        System.out.println(text.getText());
        System.out.println(text.getPosition().getX());
        System.out.println(text.getPosition().getY());
        System.out.println();
        this.addMatrix(text, null);
    }

    private void addMatrix(Text text, Position position) {
        Position pos = position;
        if (pos == null) {
            pos = text.getPosition();
        }
        String byteRep = text.getScaleX() + " " + text.getShearX() + " " + text.getShearY() + " " + text.getScaleY() + " " + pos.getX() + " " + pos.getY()
                + " Tm\n";
        this.addToByteRepresentation(byteRep);
    }

    /**
     * Adds the byte representation for the given font and font size.
     * This should be done before adding the text.
     * @param font IndirectObject containing the font.
     * @param fontSize Size of the font.
     */
    public void addFont(PdfIndirectObject font, int fontSize) {
        String byteRep = "/" + font.getReference().getResourceReference() + " " + fontSize + " Tf\n";
        this.addToByteRepresentation(byteRep);
    }

    /**
     * Adds the byte representation for the given text string.
     * @param text String that is to be added.
     * @return String containing overflow.
     */
    public String addTextString(Text text) {
        StringBuilder sb = new StringBuilder();
        Map<Position, String> textSplit = text.getTextSplit();
        for (Entry<Position, String> entry : textSplit.entrySet()) {
            if (!"\n".equals(entry.getValue())) {
                addMatrix(text, entry.getKey());
                sb.append("[(");
                sb.append(this.processKerning(entry.getValue(), text.getFont()));
                sb.append(")] TJ");
            } else {
                sb.append(getNewLineStringForText(text));
            }
            sb.append("\n");
        }
        this.addToByteRepresentation(sb.toString());
        return "";
    }

    private String getNewLineStringForText(Text text) {
        return " 0 " + -text.getFont().getLeading(text.getTextSize()) + " TD";
    }

    /**
     * Processes the kerning for the given text.
     * @param text Text to be processed.
     * @param font Font used for this text.
     * @return String with kerning.
     */
    private String processKerning(String text, Font font) {
        FontMetrics metrics = font.getFontFamily().getMetricsForStyle(font.getStyle());
        StringBuilder sb = new StringBuilder("");
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
        return sb.toString();
    }
}
