package nl.mad.pdflibrary.syntax;

import java.util.List;

import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.model.PdfNameValue;

/**
 * PdfDictionary representing a font.
 * @author Dylan de Wolff
 */
public class PdfFont extends PdfDictionary {

    /**
     * Creates a new PdfFont instance from the given font.
     * @param font Font to use.
     */
    public PdfFont(Font font) {
        super(PdfObjectType.FONT);
        this.processFont(font);
    }

    /**
     * Fills the dictionary based on the attributes of the given font.
     * @param font Font that will be processed.
     */
    private void processFont(Font font) {
        put(PdfNameValue.TYPE, PdfNameValue.FONT);
        FontFamily base = font.getFontFamily();
        FontMetrics metrics = base.getMetricsForStyle(font.getStyle());
        put(PdfNameValue.BASE_FONT, new PdfName(base.getNameOfStyle(font.getStyle())));
        put(PdfNameValue.SUB_TYPE, base.getSubType().getPdfNameValue());
        put(PdfNameValue.FIRST_CHAR, new PdfNumber(metrics.getFirstCharCode()));
        put(PdfNameValue.LAST_CHAR, new PdfNumber(metrics.getLastCharCode()));
        List<Integer> widths = metrics.getWidths(metrics.getFirstCharCode(), metrics.getLastCharCode());
        put(PdfNameValue.WIDTHS, new PdfArray(PdfNumber.convertListOfValues(widths)));
    }

    /**
     * Sets the reference to the font descriptor.
     * @param fontDescriptor Reference to the font descriptor.
     */
    public void setFontDescriptorReference(PdfIndirectObjectReference fontDescriptor) {
        put(PdfNameValue.FONT_DESCRIPTOR, fontDescriptor);
    }

    /**
     * If the encoding is specified in a separate dictionary, use this method to set the reference.
     * @param encoding Reference to the used encoding.
     */
    public void setFontEncodingReference(PdfIndirectObjectReference encoding) {
        put(PdfNameValue.ENCODING, encoding);
    }

    /**
     * If the used encoding is a default one, use this method to set the encoding type.
     * @param encoding Encoding to use.
     */
    public void setFontEncoding(PdfName encoding) {
        put(PdfNameValue.ENCODING, encoding);
    }
}
