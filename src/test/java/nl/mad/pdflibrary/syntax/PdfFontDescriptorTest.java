package nl.mad.pdflibrary.syntax;

import static org.junit.Assert.assertEquals;
import nl.mad.pdflibrary.api.BaseFont;
import nl.mad.pdflibrary.model.Font;
import nl.mad.pdflibrary.model.FontFamily;
import nl.mad.pdflibrary.model.FontMetrics;
import nl.mad.pdflibrary.model.FontType;
import nl.mad.pdflibrary.model.PdfNameValue;
import nl.mad.pdflibrary.pdf.syntax.PdfArray;
import nl.mad.pdflibrary.pdf.syntax.PdfFontDescriptor;
import nl.mad.pdflibrary.pdf.syntax.PdfIndirectObjectReference;
import nl.mad.pdflibrary.pdf.syntax.PdfName;
import nl.mad.pdflibrary.pdf.syntax.PdfNumber;
import nl.mad.pdflibrary.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class PdfFontDescriptorTest {
    private PdfFontDescriptor descriptor;
    private Font font;

    @Before
    public void setUp() throws Exception {
        font = new BaseFont();
        descriptor = new PdfFontDescriptor(font);
    }

    @Test
    public void testFontExtraction() {
        FontFamily baseFont = font.getFontFamily();
        String fontName = baseFont.getNameOfStyle(font.getStyle());
        FontMetrics metrics = baseFont.getMetricsForStyle(font.getStyle());
        PdfArray boundingBox = new PdfArray(PdfNumber.convertListOfValues(metrics.getFontBoundingBox()));

        assertEquals(new PdfName(PdfNameValue.FONT_DESCRIPTOR), descriptor.get(PdfNameValue.TYPE));
        assertEquals(new PdfName(fontName), descriptor.get(PdfNameValue.FONT_NAME));
        assertEquals(new PdfName(metrics.getFontFamily()), descriptor.get(PdfNameValue.FONT_FAMILY));
        assertEquals(new PdfNumber(metrics.getFlags()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.FLAGS)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(boundingBox.getSize(), ((PdfArray) descriptor.get(PdfNameValue.FONT_BOUNDING_BOX)).getSize());
        assertEquals(new PdfNumber(metrics.getItalicAngle()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.ITALIC_ANGLE)).getNumber(),
                FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getAscent()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.ASCENT)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getDescent()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.DESCENT)).getNumber(),
                FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getLeading()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.LEADING)).getNumber(),
                FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getCapHeight()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.CAP_HEIGHT)).getNumber(),
                FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getXHeight()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.XHEIGHT)).getNumber(),
                FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getStemV()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.STEMV)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getStemH()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.STEMH)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getAvgWidth()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.AVG_WIDTH)).getNumber(),
                FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getMaxWidth()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.MAX_WIDTH)).getNumber(),
                FloatEqualityTester.EPSILON);
        assertEquals(new PdfNumber(metrics.getMissingWidth()).getNumber(), ((PdfNumber) descriptor.get(PdfNameValue.MISSING_WIDTH)).getNumber(),
                FloatEqualityTester.EPSILON);
    }

    @Test
    public void testFontFileReference() {
        PdfIndirectObjectReference reference = new PdfIndirectObjectReference(1, 0);
        PdfIndirectObjectReference reference2 = new PdfIndirectObjectReference(2, 0);
        //test type 1 font
        descriptor.setFontFileReference(reference, FontType.TYPE1);
        assertEquals(reference, descriptor.get(PdfNameValue.FONT_FILE));
        //test type MMType1 font (should be on the same key as type1, so we use reference2 to check)
        descriptor.setFontFileReference(reference2, FontType.MMTYPE1);
        assertEquals(reference2, descriptor.get(PdfNameValue.FONT_FILE));
        //test type truetype
        descriptor.setFontFileReference(reference, FontType.TRUETYPE);
        assertEquals(reference, descriptor.get(PdfNameValue.FONT_FILE2));
        //test type 3
        descriptor.setFontFileReference(reference, FontType.TYPE3);
        assertEquals(reference, descriptor.get(PdfNameValue.FONT_FILE3));

    }

}
