package org.toucanpdf.pdf.syntax;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.toucanpdf.model.Alignment;
import org.toucanpdf.model.Font;
import org.toucanpdf.model.FontMetrics;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Text;
import org.toucanpdf.model.state.StateSplittableText;
import org.toucanpdf.utility.Constants;

/**
 * PdfText stores the PDF stream version of a Text object. 
 * @author Dylan de Wolff
 * @see PdfStream
 * @see Text
 */
public class PdfText extends AbstractPdfObject {
    private static final String WORD_SPACING = " Tw ";
    private static final String MATRIX = " Tm" + Constants.LINE_SEPARATOR_STRING;
    private static final String FONT = " Tf" + Constants.LINE_SEPARATOR_STRING;
    private PdfFontDifferences differences = null;
    private static final int OCTAL_CODE_LENGTH = 4;

    /**
     * Creates a new instance of PdfText.
     * @param fontObj font to use for this text
     */
    public PdfText(PdfFont fontObj) {
        super(PdfObjectType.TEXT);
        if (fontObj != null && fontObj.getEncoding() != null) {
            differences = fontObj.getEncoding().getEncodingDifferences();
        }
    }

    /**
     * Creates the PDF syntax for the given text object.
     * @param text StateText object to be added.
     * @param fontReference font for the text.
     * @param leading the space between two lines.
     */
    public void addText(StateSplittableText text, PdfIndirectObject fontReference, int leading) {
        this.addFont(fontReference, text.getTextSize());
        this.addTextString(text, leading);
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
        return text.getScaleX() + " " + text.getShearX() + " " + text.getShearY() + " " + text.getScaleY() + " " + pos.getX() + " " + pos.getY() + MATRIX;
    }

    /**
     * Adds the byte representation for the given font and font size.
     * This should be done before adding the text.
     * @param font IndirectObject containing the font.
     * @param fontSize Size of the font.
     */
    public void addFont(PdfIndirectObject font, int fontSize) {
        String byteRep = "/" + font.getReference().getResourceReference() + " " + fontSize + FONT;
        this.addToByteRepresentation(byteRep);
    }

    /**
     * Adds the byte representation for the given text object.
     * @param text Text object that is to be added.
     * @param leading Space between two lines.
     */
    public void addTextString(StateSplittableText text, int leading) {
        Map<Position, String> textSplit = text.getTextSplit();
        Map<Position, Double> justification = text.getJustificationOffset();
        Set<Entry<Position, String>> entrySet = textSplit.entrySet();
        addTextString(entrySet, text, justification, leading);
    }

    private void addTextString(Set<Entry<Position, String>> entrySet, Text text, Map<Position, Double> justification, int leading) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Entry<Position, String> entry : entrySet) {
            if (!isNewLineIndicator(entry)) {
                if (textAlignmentIsJustifiedAndNotLastEntry(entrySet, text, i)) {
                    sb.append(justification.get(entry.getKey())).append(WORD_SPACING);
                }

                String textToProcess = determineTextToProcessBasedOnFontDifferences(entry);
                sb.append(createMatrix(text, entry.getKey()));
                sb.append(text.getColor().toString()).append(" rg ");
                sb.append("[(").append(this.processKerning(textToProcess, text.getFont())).append(")] TJ");
            } else {
                sb.append(getNewLineStringForText(leading));
            }
            ++i;
            sb.append(Constants.LINE_SEPARATOR_STRING);
        }
        this.addToByteRepresentation(sb.toString());
    }

    private String determineTextToProcessBasedOnFontDifferences(Entry<Position, String> entry) {
        String textToProcess;
        if (differences != null) {
            textToProcess = differences.convertString(entry.getValue());
        } else {
            textToProcess = getEscapedString(entry.getValue());
        }
        return textToProcess;
    }

    private boolean textAlignmentIsJustifiedAndNotLastEntry(Set<Entry<Position, String>> entrySet, Text text, int index) {
        return Alignment.JUSTIFIED.equals(text.getAlignment()) && (index != entrySet.size() - 1);
    }

    private boolean isNewLineIndicator(Entry<Position, String> entry) {
        return Constants.LINE_SEPARATOR_STRING.equals(entry.getValue());
    }

    private String getEscapedString(String value) {
        String newValue = "" + value;
        newValue = newValue.replace("(", "\\(");
        newValue = newValue.replace(")", "\\)");
        return newValue;
    }

    private String getNewLineStringForText(int leading) {
        return " 0 " + -leading + " TD";
    }

    /**
     * Processes the kerning for the given text.
     * @param text Text to be processed.
     * @param font Font used for this text.
     * @return String with kerning.
     */
    private String processKerning(String text, Font font) {
        if (differences != null) {
            return processOctalKerning(text, font);
        } else {
            return processCharacterKerning(text, font);
        }
    }

    private String processCharacterKerning(String text, Font font) {
        FontMetrics metrics = font.getMetrics();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < text.length(); ++i) {
            sb.append(text.charAt(i));
            if (text.length() != i + 1) {
                int kernWidth = metrics.getKerning((int) text.charAt(i), (int) text.charAt(i + 1));
                if (kernWidth != 0) {
                    sb.append(") ").append(kernWidth).append(" (");
                }
            }
        }
        return sb.toString();
    }

    private String processOctalKerning(String text, Font font) {
        FontMetrics metrics = font.getMetrics();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < text.length(); i += OCTAL_CODE_LENGTH) {
            String octalCodeForFirstCharacter = text.substring(i + 1, i + OCTAL_CODE_LENGTH);
            sb.append("\\").append(octalCodeForFirstCharacter);
            if (text.length() != i + OCTAL_CODE_LENGTH) {
                String octalCodeForSecondCharacter = text.substring(i + OCTAL_CODE_LENGTH + 1, i + (2 * OCTAL_CODE_LENGTH));
                String charName = differences.getNameOf(octalCodeForFirstCharacter);
                String secondCharName = differences.getNameOf(octalCodeForSecondCharacter);
                int kernWidth = metrics.getKerning(charName, secondCharName);
                if (kernWidth != 0) {
                    sb.append(") ").append(kernWidth).append(" (");
                }
            }
        }
        return sb.toString();
    }
}
