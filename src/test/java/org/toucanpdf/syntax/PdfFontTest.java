package org.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.toucanpdf.api.BaseFont;
import org.toucanpdf.model.Font;
import org.toucanpdf.model.FontFamily;
import org.toucanpdf.model.FontMetrics;
import org.toucanpdf.model.PdfNameValue;
import org.toucanpdf.pdf.syntax.PdfArray;
import org.toucanpdf.pdf.syntax.PdfFont;
import org.toucanpdf.pdf.syntax.PdfIndirectObjectReference;
import org.toucanpdf.pdf.syntax.PdfName;
import org.toucanpdf.pdf.syntax.PdfNumber;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PdfFontTest {
    private PdfFont pdfFont;
    private Font font;

    @Before
    public void setUp() throws Exception {
        font = new BaseFont();
        pdfFont = new PdfFont(font, null);
    }

    @Test
    public void testFontExtraction() throws IOException {
        assertEquals(pdfFont.get(PdfNameValue.TYPE), new PdfName(PdfNameValue.FONT));
        FontFamily base = font.getFontFamily();
        FontMetrics metrics = base.getMetricsForStyle(font.getStyle());
        assertEquals(pdfFont.get(PdfNameValue.BASE_FONT), new PdfName(base.getNameOfStyle(font.getStyle())));
        assertEquals(pdfFont.get(PdfNameValue.SUB_TYPE), new PdfName(base.getSubType().getPdfNameValue()));
        Assert.assertEquals(((PdfNumber) (pdfFont.get(PdfNameValue.FIRST_CHAR))).getNumber(), metrics.getFirstCharCode(), FloatEqualityTester.EPSILON);
        assertEquals(((PdfNumber) (pdfFont.get(PdfNameValue.LAST_CHAR))).getNumber(), metrics.getLastCharCode(), FloatEqualityTester.EPSILON);
        pdfFont.writeToFile(new ByteArrayOutputStream());
        assertEquals(((PdfArray) (pdfFont.get(PdfNameValue.WIDTHS))).getSize(), metrics.getWidths(metrics.getFirstCharCode(), metrics.getLastCharCode()).size());
    }

    @Test
    public void testFontDescriptor() {
        PdfIndirectObjectReference reference = new PdfIndirectObjectReference(4, 1);
        pdfFont.setFontDescriptorReference(reference);
        assertEquals(reference, pdfFont.get(PdfNameValue.FONT_DESCRIPTOR));
    }

    @Test
    public void testFontEncoding() {
        //default encoding test
        PdfName encoding = new PdfName(PdfNameValue.ASCII_HEX_DECODE);
        pdfFont.setFontEncoding(encoding);
        assertEquals(encoding, pdfFont.get(PdfNameValue.ENCODING));
        //custom encoding test
        PdfIndirectObjectReference reference = new PdfIndirectObjectReference(5, 1);
        //pdfFont.setFontEncodingReference(reference);
        //assertEquals(reference, pdfFont.get(PdfNameValue.ENCODING));
    }
}
