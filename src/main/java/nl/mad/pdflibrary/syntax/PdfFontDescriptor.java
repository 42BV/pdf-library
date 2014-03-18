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
        put(PdfNameValue.TYPE, PdfNameValue.FONT_DESCRIPTOR);
        BaseFontFamily baseFont = font.getBaseFontFamily();
        String fontName = baseFont.getNameOfStyle(font.getStyle());
        put(PdfNameValue.FONT_NAME, new PdfName(fontName));
        FontMetrics metrics = baseFont.getMetricsForStyle(font.getStyle());
        put(PdfNameValue.FONT_FAMILY, new PdfName(metrics.getFontFamily()));
        put(PdfNameValue.FLAGS, new PdfNumber(metrics.getFlags()));
        PdfArray boundingBox = new PdfArray(PdfNumber.convertListOfValues(metrics.getFontBoundingBox()));
        put(PdfNameValue.FONT_BOUNDING_BOX, boundingBox);
        put(PdfNameValue.ITALIC_ANGLE, new PdfNumber(metrics.getItalicAngle()));
        put(PdfNameValue.ASCENT, new PdfNumber(metrics.getAscent()));
        put(PdfNameValue.DESCENT, new PdfNumber(metrics.getDescent()));
        put(PdfNameValue.LEADING, new PdfNumber(metrics.getLeading()));
        put(PdfNameValue.CAP_HEIGHT, new PdfNumber(metrics.getCapHeight()));
        put(PdfNameValue.XHEIGHT, new PdfNumber(metrics.getXHeight()));
        put(PdfNameValue.STEMV, new PdfNumber(metrics.getStemV()));
        put(PdfNameValue.STEMH, new PdfNumber(metrics.getStemH()));
        put(PdfNameValue.AVG_WIDTH, new PdfNumber(metrics.getAvgWidth()));
        put(PdfNameValue.MAX_WIDTH, new PdfNumber(metrics.getMaxWidth()));
        put(PdfNameValue.MISSING_WIDTH, new PdfNumber(metrics.getMissingWidth()));
    }

    /**
     * Sets the reference to the embedded font program file.
     * @param reference Reference to the font program file.
     * @param type Type of font.
     */
    public void setFontFileReference(PdfIndirectObjectReference reference, FontType type) {
        switch (type) {
        case TYPE1:
        case MMTYPE1:
            put(PdfNameValue.FONT_FILE, reference);
            break;
        case TRUETYPE:
            put(PdfNameValue.FONT_FILE2, reference);
            break;
        case TYPE3:
            put(PdfNameValue.FONT_FILE3, reference);
            break;
        default:
            break;
        }
    }
}
