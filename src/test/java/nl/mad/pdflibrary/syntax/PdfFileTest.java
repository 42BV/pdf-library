package nl.mad.pdflibrary.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.mad.pdflibrary.utility.ByteEncoder;

import org.junit.Before;
import org.junit.Test;

public class PdfFileTest {
    PdfFile file;

    @Before
    public void setUp() throws Exception {
        file = new PdfFile(ByteEncoder.getBytes("test"));
    }

    @Test
    public void testWriteToFile() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String expectedResult = "test\n";
        file.writeToFile(baos);
        assertEquals("Write to file gave wrong output. ", expectedResult, baos.toString());
    }
}
