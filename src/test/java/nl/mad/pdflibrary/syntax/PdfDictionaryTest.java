package nl.mad.pdflibrary.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.mad.pdflibrary.model.PdfNameValue;

import org.junit.Before;
import org.junit.Test;

public class PdfDictionaryTest {
    private PdfDictionary dictionary;

    @Before
    public void setUp() throws Exception {
        dictionary = new PdfDictionary(PdfObjectType.DICTIONARY);
    }

    @Test
    public void testContain() {
        PdfName key = new PdfName(PdfNameValue.ASCENT);
        PdfNumber value = new PdfNumber(1);
        dictionary.put(key, value);
        assertEquals("Method returned incorrect result. ", true, dictionary.containsKey(key));
        assertEquals("Method returned incorrect result. ", true, dictionary.containsValue(value));

    }

    @Test
    public void testWriteToFile() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dictionary.put(PdfNameValue.ASCENT, new PdfNumber(1));
        String expectedResult = "<<\n /Ascent 1\n>>";
        dictionary.writeToFile(baos);
        assertEquals("The write output was not as expected.", expectedResult, baos.toString());
    }
}
