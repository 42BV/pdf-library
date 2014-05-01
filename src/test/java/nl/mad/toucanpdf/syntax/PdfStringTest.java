package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import nl.mad.toucanpdf.pdf.syntax.PdfString;

import org.junit.Before;
import org.junit.Test;

public class PdfStringTest {
    private PdfString string;

    @Before
    public void setUp() throws Exception {
        string = new PdfString();
    }

    @Test
    public void testWriteToFile() throws IOException {
        string.setString("test");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        string.writeToFile(baos);
        assertEquals("Writing to file was done incorrectly. ", "(test)", baos.toString());
    }

    @Test
    public void testDate() {
        Calendar date = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String stringDate = "D:" + dateFormat.format(date.getTime());
        string.setString(date);

        assertEquals("PdfString did not correctly process date. ", stringDate, string.getString());
    }
}
