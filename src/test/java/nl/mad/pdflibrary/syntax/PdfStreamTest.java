package nl.mad.pdflibrary.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.mad.pdflibrary.model.PdfNameValue;
import nl.mad.pdflibrary.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class PdfStreamTest {
    private PdfStream stream;
    private PdfArray filters;

    @Before
    public void setUp() throws Exception {
        filters = new PdfArray();
        filters.addValue(new PdfName("test"));
        stream = new PdfStream(filters);
    }

    @Test
    public void testCreation() {
        assertEquals(PdfObjectType.STREAM, stream.getType());
        assertEquals(0, ((PdfNumber) stream.get(PdfNameValue.LENGTH)).getNumber(), FloatEqualityTester.EPSILON);
        assertEquals(filters, stream.get(PdfNameValue.FILTER));
    }

    @Test
    public void testAddFilter() {
        //add to exisiting filter array
        stream.addFilter(new PdfName("test2"));
        assertEquals(filters, stream.get(PdfNameValue.FILTER));

        //add to empty array
        stream = new PdfStream();
        stream.addFilter(new PdfName("test"));
        assertEquals(1, ((PdfArray) stream.get(PdfNameValue.FILTER)).getSize());
    }

    @Test
    public void testWriteToFile() throws IOException {
        PdfName content1 = new PdfName("test");
        PdfText content2 = new PdfText();
        PdfString content3 = new PdfString("test3");
        PdfString content4 = new PdfString("test4");
        stream.add(content1);
        stream.add(content2);
        stream.add(content3);
        stream.add(content4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        stream.writeToFile(baos);
        assertEquals("<<\n /Length 19\n /Filter [ /test ]\n>>\nstream\n/test\nBT\nET\n(test3)\n(test4)\nendstream", baos.toString());
    }
}
