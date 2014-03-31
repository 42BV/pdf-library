package nl.mad.pdflibrary.pdf.syntax;

import java.util.ArrayList;
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
    private int positionX;
    private boolean inParagraph;

    /**
     * Creates a new instance of PdfText.
     */
    public PdfText() {
        super(PdfObjectType.TEXT);
        inParagraph = false;
    }

    /**
     * Creates a new instance of PdfText. Use this constructor for text objects that are in a paragraph.
     * @param positionX Starting point of the paragraph.
     */
    public PdfText(int positionX) {
        super(PdfObjectType.TEXT);
        this.positionX = positionX;
        inParagraph = true;
    }

    /**
     * Adds the given Text object to the stream.
     * @param text Text object to be added.
     * @param fontReference font for the text.
     * @param page The page this text is added to.
     * @param leading Int containing the leading for this text.
     * @param ignorePosition Boolean specifying whether the position of the text should be ignored.
     * @return String containing overflow.
     */
    public String addText(Text text, PdfIndirectObject fontReference, PdfPage page, int leading, boolean ignorePosition) {
        this.addFont(fontReference, text.getTextSize());
        this.addMatrix(text);
        return this.addTextString(text, page, leading, ignorePosition);
    }

    /**
     * Converts the given position values to a text matrix and adds this to the byte representation.
     * This should be done before adding the text.
     * @param text text to add to the document
     */
    public void addMatrix(Text text) {
        String byteRep = text.getScaleX() + " " + text.getShearX() + " " + text.getShearY() + " " + text.getScaleY() + " " + text.getPosition().getX() + " "
                + text.getPosition().getY() + " Tm\n";
        this.addToByteRepresentation(byteRep);
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
        this.addToByteRepresentation(byteRep);
    }

    /**
     * Adds the byte representation for the given text string.
     * @param text String that is to be added.
     * @param page The page this text is added to.
     * @param leading Int containing the leading for this text.
     * @param ignorePosition Boolean specifying whether the position of the text should be ignored.
     * @return String containing overflow.
     */
    public String addTextString(Text text, PdfPage page, int leading, boolean ignorePosition) {
        List<String> splitStrings = processNewLines(text, page, leading, ignorePosition);
        StringBuilder sb = new StringBuilder();
        for (String s : splitStrings) {
            sb.append(s);
            sb.append("\n");
        }
        this.addToByteRepresentation(sb.toString());
        return "";
    }

    /**
     * Splits the text and adds new lines where needed.
     * @param text Text to be processed.
     * @param page Page the text will appear on.
     * @param leading Leading of the text.
     * @param ignorePosition Whether or not the position of the text should be ignored.
     * @return List of strings containing the split text.
     */
    private List<String> processNewLines(Text text, PdfPage page, int leading, boolean ignorePosition) {
        String textString = text.getText();
        int textSize = text.getTextSize();
        Font font = text.getFont();
        String[] strings = textString.split(" ");

        StringBuilder currentLine = new StringBuilder();
        ArrayList<String> processedStrings = new ArrayList<String>();

        double width = page.getFilledWidth();
        //if we aren't adding the given text object behind another text object
        if (!ignorePosition) {
            width += text.getPosition().getX();
        }

        FontMetrics metrics = font.getFontFamily().getMetricsForStyle(font.getStyle());
        for (int i = 0; i < strings.length; ++i) {
            //add the width of the string
            //TODO: Fix retrieving space width by unicode
            width += metrics.getWidthPointOfString(strings[i], textSize, true) + metrics.getWidthPoint("space");
            if (width > page.getWidth()) {
                currentLine = new StringBuilder(this.processKerning(currentLine.toString(), font));
                currentLine.append(" 0 " + -leading + " TD");
                processedStrings.add(currentLine.toString());
                page.setFilledHeight(page.getFilledHeight() + leading);
                currentLine = new StringBuilder();
                width = positionX;
            }
            //add the string and a space to the current line
            currentLine.append(strings[i]);
            currentLine.append(' ');
            //if we are at the last string
            if ((i + 1) == strings.length) {
                currentLine = new StringBuilder(this.processKerning(currentLine.toString(), font));
                processedStrings.add(currentLine.toString());
            }
        }

        page.setFilledHeight(page.getFilledHeight() + leading);
        if (inParagraph) {
            page.setFilledWidth(width);
        } else {
            page.setFilledWidth(0);
        }

        return processedStrings;
    }

    /**
     * Processes the kerning for the given text.
     * @param text Text to be processed.
     * @param font Font used for this text.
     * @return String with kerning.
     */
    private String processKerning(String text, Font font) {
        FontMetrics metrics = font.getFontFamily().getMetricsForStyle(font.getStyle());
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
        sb.append(")] TJ");
        return sb.toString();
    }

    public int getPositionX() {
        return this.positionX;
    }
}
