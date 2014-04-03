package nl.mad.pdflibrary.pdf.syntax;

import java.util.List;

import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontMetrics;
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
     * @param page PdfPage the text will be placed on.
     * @return String containing overflow.
     */
    public String addText(Text text, PdfIndirectObject fontReference, PdfPage page) {
        this.addFont(fontReference, text.getTextSize());
        this.addMatrix(text, page);
        return this.addTextString(text);
    }

    /**
     * Converts the given position values to a text matrix and adds this to the byte representation.
     * This should be done before adding the text.
     * @param text text to add to the document.
     * @param page page this text will be on.
     */
    public void addMatrix(Text text, PdfPage page) {
        System.out.println(text.getText());
        System.out.println(text.getPosition().getX());
        System.out.println(text.getPosition().getY());
        System.out.println();

        String byteRep = text.getScaleX() + " " + text.getShearX() + " " + text.getShearY() + " " + text.getScaleY() + " "
                + (text.getPosition().getX() + page.getMarginLeft()) + " " + (text.getPosition().getY() + page.getMarginTop()) + " Tm\n";
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
        List<String> textArray = text.getTextArray();
        for (int i = 0; i < textArray.size(); ++i) {
            if (!"\n".equals(textArray.get(i))) {
                sb.append("[(");
                sb.append(this.processKerning(textArray.get(i), text.getFont()));
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
