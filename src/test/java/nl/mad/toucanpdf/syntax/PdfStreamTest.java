package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.mad.toucanpdf.model.Compression;
import nl.mad.toucanpdf.model.PdfNameValue;
import nl.mad.toucanpdf.pdf.syntax.PdfArray;
import nl.mad.toucanpdf.pdf.syntax.PdfName;
import nl.mad.toucanpdf.pdf.syntax.PdfNumber;
import nl.mad.toucanpdf.pdf.syntax.PdfObjectType;
import nl.mad.toucanpdf.pdf.syntax.PdfStream;
import nl.mad.toucanpdf.pdf.syntax.PdfString;
import nl.mad.toucanpdf.pdf.syntax.PdfText;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class PdfStreamTest {
    private PdfStream stream;

    @Before
    public void setUp() throws Exception {
        stream = new PdfStream();
    }

    @Test
    public void testCreation() {
        assertEquals(PdfObjectType.STREAM, stream.getType());
        assertEquals(0, ((PdfNumber) stream.get(PdfNameValue.LENGTH)).getNumber(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testAddFilter() {
        //add to exisiting filter array
        stream.addFilter(Compression.ASCII_85);
        assertEquals(1, ((PdfArray) stream.get(PdfNameValue.FILTER)).getSize());
        assertEquals(new PdfName(Compression.ASCII_85.getPdfName()), ((PdfArray) stream.get(PdfNameValue.FILTER)).getValues().get(0));
    }

    @Test
    public void testWriteToFile() throws IOException {
        PdfName content1 = new PdfName("test");
        PdfText content2 = new PdfText(null);
        PdfString content3 = new PdfString("test3");
        PdfString content4 = new PdfString("test4");
        stream.add(content1);
        stream.add(content2);
        stream.add(content3);
        stream.add(content4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        stream.writeToFile(baos);
        assertEquals("<<\n /Length 28\n>>\nstream\n/test\nBT\nET\n(test3)\n(test4)\nendstream", baos.toString());
        stream.addFilter(Compression.FLATE);
        baos.reset();
        stream.writeToFile(baos);
        assertEquals(90, baos.size());
        baos.close();
    }
}
