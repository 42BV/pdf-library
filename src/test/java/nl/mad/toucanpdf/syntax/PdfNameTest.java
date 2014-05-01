package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;

import nl.mad.toucanpdf.model.PdfNameValue;
import nl.mad.toucanpdf.pdf.syntax.PdfArray;
import nl.mad.toucanpdf.pdf.syntax.PdfName;

import org.junit.Before;
import org.junit.Test;

public class PdfNameTest {
    private PdfName name;

    @Before
    public void setUp() throws Exception {
        name = new PdfName("test");
    }

    @Test
    public void testNameSetting() {
        //test name setting by string, without prefix
        assertEquals("/test", name.getName());
        assertEquals("/test", new String(name.getByteRepresentation(), StandardCharsets.UTF_8));
        //test name setting by PdfNameValue
        name.setName(PdfNameValue.ASCENT);
        assertEquals("/Ascent", name.getName());
        assertEquals("/Ascent", new String(name.getByteRepresentation(), StandardCharsets.UTF_8));
        //test name setting by string, with prefix
        name.setName("/test");
        assertEquals("/test", name.getName());
        assertEquals("/test", new String(name.getByteRepresentation(), StandardCharsets.UTF_8));
    }

    @Test
    public void equals() {
        assertEquals(true, name.equals(new PdfName("test")));
        assertEquals(false, name.equals(new PdfArray()));
    }

}
