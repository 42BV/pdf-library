package nl.mad.toucanpdf.pdf.syntax;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.FontMetrics;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.StateText;
import nl.mad.toucanpdf.model.Text;

/**
 * PdfText stores the PDF stream version of a Text object. 
 * @author Dylan de Wolff
 * @see PdfStream
 * @see nl.mad.toucanpdf.model.Text
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
     * @param text StateText object to be added.
     * @param fontReference font for the text.
     * @param leading the space between two lines.
     * @return String containing overflow.
     */
    public String addText(StateText text, PdfIndirectObject fontReference, int leading) {
        this.addFont(fontReference, text.getTextSize());
        return this.addTextString(text, leading);
    }

    /**
     * Converts the given position values to a text matrix and adds this to the byte representation.
     * This should be done before adding the text.
     * @param text text to add to the document.
     */
    public void addMatrix(Text text) {
        this.addMatrix(text, null);
    }

    private void addMatrix(Text text, Position position) {
        this.addToByteRepresentation(createMatrix(text, position));
    }

    private String createMatrix(Text text, Position position) {
        Position pos = position;
        if (pos == null) {
            pos = text.getPosition();
        }
        return text.getScaleX() + " " + text.getShearX() + " " + text.getShearY() + " " + text.getScaleY() + " " + pos.getX() + " " + pos.getY() + " Tm\n";
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
     * @param leading Space between two lines.
     * @return String containing overflow.
     */
    public String addTextString(StateText text, int leading) {
        StringBuilder sb = new StringBuilder();
        Map<Position, String> textSplit = text.getTextSplit();
        Map<Position, Double> justification = text.getJustificationOffset();
        Set<Entry<Position, String>> entrySet = textSplit.entrySet();
        if (text.getAlignment().equals(Alignment.JUSTIFIED)) {
            System.out.println("");
            System.out.println("Printing out positions for textSplit: ");
            for (Entry<Position, String> entry : entrySet) {
                System.out.println("    Position = " + entry.getKey() + ", value = " + entry.getValue());
            }

            System.out.println("Printing out positions for justified: ");
            for (Entry<Position, Double> entry : justification.entrySet()) {
                System.out.println("    Position = " + entry.getKey() + ", value = " + entry.getValue());
            }
        }
        int i = 0;
        for (Entry<Position, String> entry : entrySet) {
            if (!"\n".equals(entry.getValue())) {
                if (Alignment.JUSTIFIED.equals(text.getAlignment())) {
                    System.out.println(text.getText());
                    System.out.println("Alignment is just!");
                    if (i != entrySet.size() - 1) {
                        sb.append(justification.get(entry.getKey()) + " Tw ");
                    }
                }
                sb.append(createMatrix(text, entry.getKey()));
                sb.append("[(");
                sb.append(this.processKerning(entry.getValue(), text.getFont()));
                sb.append(")] TJ");
            } else {
                sb.append(getNewLineStringForText(text, leading));
            }
            ++i;
            sb.append("\n");
        }
        this.addToByteRepresentation(sb.toString());
        return "";
    }

    private String getNewLineStringForText(Text text, int leading) {
        return " 0 " + -leading + " TD";
    }

    /**
     * Processes the kerning for the given text.
     * @param text Text to be processed.
     * @param font Font used for this text.
     * @return String with kerning.
     */
    private String processKerning(String text, Font font) {
        FontMetrics metrics = font.getMetrics();
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
