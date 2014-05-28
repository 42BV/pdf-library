package nl.mad.toucanpdf.pdf.syntax;

import java.util.List;

import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.FontFamily;
import nl.mad.toucanpdf.model.FontMetrics;
import nl.mad.toucanpdf.model.PdfNameValue;

/**
 * PdfDictionary representing a font.
 * @author Dylan de Wolff
 */
public class PdfFont extends PdfDictionary {
    private PdfFontEncoding encoding;

    /**
     * Creates a new PdfFont instance from the given font.
     * @param font Font to use.
     */
    public PdfFont(Font font, PdfIndirectObject encoding) {
        super(PdfObjectType.FONT);
        this.processFont(font);
        this.setFontEncodingReference(encoding);
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
     * If the encoding is specified in a separate dictionary, use this method to set it.
     * @param newEncoding The encoding object to use.
     */
    public void setFontEncodingReference(PdfIndirectObject newEncoding) {
        if (newEncoding != null && newEncoding.getObject() instanceof PdfFontEncoding) {
            this.encoding = (PdfFontEncoding) newEncoding.getObject();
            this.put(PdfNameValue.ENCODING, newEncoding.getReference());
        }
    }

    /**
     * If the used encoding is a default one, use this method to set the encoding type.
     * @param newEncoding Encoding to use.
     */
    public void setFontEncoding(PdfName newEncoding) {
        put(PdfNameValue.ENCODING, newEncoding);
    }

    /**
     * Returns the used encoding.
     * @return PdfFontEncoding instance or null if this font does not use an external encoding dictionary.
     */
    public PdfFontEncoding getEncoding() {
        return this.encoding;
    }
}
