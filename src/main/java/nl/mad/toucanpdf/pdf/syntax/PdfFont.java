package nl.mad.toucanpdf.pdf.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.FontFamily;
import nl.mad.toucanpdf.model.FontMetrics;
import nl.mad.toucanpdf.model.PdfNameValue;
import nl.mad.toucanpdf.utility.RandomStringGenerator;

/**
 * PdfDictionary representing a font.
 * @author Dylan de Wolff
 */
public class PdfFont extends PdfDictionary {
    private PdfFontEncoding encoding;
    private Font font;
    private static final int SUBSET_INDICATOR_LENGTH = 6;

    /**
     * Creates a new PdfFont instance from the given font.
     * @param font Font to use.
     */
    public PdfFont(Font font, PdfIndirectObject encoding) {
        super(PdfObjectType.FONT);
        this.font = font;
        this.processFont();
        this.setFontEncodingReference(encoding);
    }

    /**
     * Fills the dictionary based on the attributes of the font.
     */
    private void processFont() {
        put(PdfNameValue.TYPE, PdfNameValue.FONT);
        FontFamily base = font.getFontFamily();
        FontMetrics metrics = base.getMetricsForStyle(font.getStyle());
        put(PdfNameValue.BASE_FONT, new PdfName(base.getNameOfStyle(font.getStyle())));
        put(PdfNameValue.SUB_TYPE, base.getSubType().getPdfNameValue());
        put(PdfNameValue.FIRST_CHAR, new PdfNumber(metrics.getFirstCharCode()));
        put(PdfNameValue.LAST_CHAR, new PdfNumber(metrics.getLastCharCode()));
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

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        this.addWidthsEntry();
        super.writeToFile(os);
    }

    private void addWidthsEntry() {
        FontMetrics metrics = font.getFontFamily().getMetricsForStyle(font.getStyle());
        List<Integer> widths;
        if (isFontUsingCustomEncoding()) {
            widths = encoding.getEncodingDifferences().generateWidthList(font);
            CreateFontSubset(widths);
        } else {
            widths = metrics.getWidths(metrics.getFirstCharCode(), metrics.getLastCharCode());
        }
        put(PdfNameValue.WIDTHS, new PdfArray(PdfNumber.convertListOfValues(widths)));
    }

    /**
     * Creates a font subset, meaning we manually decide the characters order of the font and which characters are actually used
     * This is required whenever we use custom encoding.
     * @param widths character widths
     */
    private void CreateFontSubset(List<Integer> widths) {
        put(PdfNameValue.FIRST_CHAR, new PdfNumber(0));
        put(PdfNameValue.LAST_CHAR, new PdfNumber(widths.size() - 1));
        put(PdfNameValue.BASE_FONT,
                new PdfName(RandomStringGenerator.generateRandomString(RandomStringGenerator.DEFAULT_CAPS_CHARACTERS, SUBSET_INDICATOR_LENGTH) + "+"
                        + font.getFontFamily().getNameOfStyle(font.getStyle())));
    }

    private boolean isFontUsingCustomEncoding() {
        return encoding != null && encoding.getEncodingDifferences() != null;
    }
}
