package nl.mad.pdflibrary.syntax;

import nl.mad.pdflibrary.model.BaseFontFamily;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.model.FontType;
import nl.mad.pdflibrary.model.PdfNameValue;

/**
 * FontDescriptor is a PdfDictionary containing specific font attributes. 
 * @author Dylan de Wolff
 *
 */
public class PdfFontDescriptor extends PdfDictionary {

    /**
     * Creates a new instance of PdfFontDescriptor and fills the descriptor with the given font.
     * @param font Font to process.
     */
    public PdfFontDescriptor(Font font) {
        super(PdfObjectType.DICTIONARY);
        fillDescriptor(font);
    }

    /**
     * Fills the descriptor based on the given font.
     * @param font Font to process.
     */
    private void fillDescriptor(Font font) {
        put(new PdfName(PdfNameValue.TYPE), new PdfName(PdfNameValue.FONT_DESCRIPTOR));
        BaseFontFamily baseFont = font.getBaseFont();
        String fontName = baseFont.getNameOfStyle(font.getStyle());
        put(new PdfName(PdfNameValue.FONT_NAME), new PdfName(fontName));
        FontMetrics metrics = baseFont.getMetricsForStyle(font.getStyle());
        put(new PdfName(PdfNameValue.FONT_FAMILY), new PdfName(metrics.getFontFamily()));
        put(new PdfName(PdfNameValue.FLAGS), new PdfNumber(metrics.getFlags()));
        PdfArray boundingBox = new PdfArray(PdfNumber.convertListOfValues(metrics.getFontBoundingBox()));
        put(new PdfName(PdfNameValue.FONT_BOUNDING_BOX), boundingBox);
        put(new PdfName(PdfNameValue.ITALIC_ANGLE), new PdfNumber(metrics.getItalicAngle()));
        put(new PdfName(PdfNameValue.ASCENT), new PdfNumber(metrics.getAscent()));
        put(new PdfName(PdfNameValue.DESCENT), new PdfNumber(metrics.getDescent()));
        put(new PdfName(PdfNameValue.LEADING), new PdfNumber(metrics.getLeading()));
        put(new PdfName(PdfNameValue.CAP_HEIGHT), new PdfNumber(metrics.getCapHeight()));
        put(new PdfName(PdfNameValue.XHEIGHT), new PdfNumber(metrics.getXHeight()));
        put(new PdfName(PdfNameValue.STEMV), new PdfNumber(metrics.getStemV()));
        put(new PdfName(PdfNameValue.STEMH), new PdfNumber(metrics.getStemH()));
        put(new PdfName(PdfNameValue.AVG_WIDTH), new PdfNumber(metrics.getAvgWidth()));
        put(new PdfName(PdfNameValue.MAX_WIDTH), new PdfNumber(metrics.getMaxWidth()));
        put(new PdfName(PdfNameValue.MISSING_WIDTH), new PdfNumber(metrics.getMissingWidth()));
    }

    public void setFontFileReference(PdfIndirectObjectReference reference, FontType type) {
        switch (type) {
        case TYPE1:
        case MMTYPE1:
            put(new PdfName(PdfNameValue.FONT_FILE), reference);
            break;
        case TRUETYPE:
            put(new PdfName(PdfNameValue.FONT_FILE2), reference);
            break;
        case TYPE3:
            put(new PdfName(PdfNameValue.FONT_FILE3), reference);
            break;
        default:
            break;
        }
    }
}
