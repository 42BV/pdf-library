package nl.mad.pdflibrary.pdf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import nl.mad.pdflibrary.pdf.PdfBody;
import nl.mad.pdflibrary.pdf.object.PdfIndirectObject;
import nl.mad.pdflibrary.pdf.object.PdfName;
import nl.mad.pdflibrary.pdf.object.PdfPage;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class PdfBodyTest {
    private PdfBody body;

    @Before
    public void setUp() throws Exception {
        body = new PdfBody();
    }

    @Test
    public void testPdfBody() {
        int amountOfObjectsExpected = 2;
        assertEquals("Not all necessary objects have been created.", amountOfObjectsExpected, body.getTotalIndirectObjectsAmount());
    }

    @Test
    public void testAddObject() {
        PdfName test = new PdfName("nl");
        body.addObject(test);
        int expectedObjectPos = 1;
        assertEquals("Object has not been correctly added to the body.", test, body.getAllIndirectObjects().get(expectedObjectPos).getObject());
    }

    @Test
    public void testAddPage() {
        PdfPage page = new PdfPage(200, 200);
        body.addPage(page);
        int expectedObjectPos = 2;
        assertEquals("Page has not been correctly added to the body.", page, body.getAllIndirectObjects().get(expectedObjectPos).getObject());

    }

    @Test
    public void testStartByteSetting() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        String testString = "nl";
        dos.writeBytes(testString);
        body.writeToFile(dos);
        int expectedStartByte = testString.length();
        assertEquals("The start byte is incorrect.", expectedStartByte, body.getAllIndirectObjects().get(0).getStartByte());
    }

    @Test
    public void testGetIndirectObjects() {
        PdfName test = new PdfName("nl");
        body.addObject(test);
        PdfPage page = new PdfPage(200, 200);
        body.addPage(page);

        int expectedTotalSize = 4;
        int expectedSize = 1;
        ArrayList<PdfIndirectObject> allIndirectObjects = body.getAllIndirectObjects();
        ArrayList<PdfIndirectObject> indirectObjects = body.getIndirectObjects();
        assertEquals("The amount of objects retrieved is incorrect. ", expectedTotalSize, body.getTotalIndirectObjectsAmount());
        assertEquals("The amount of objects retrieved is incorrect. ", expectedTotalSize, allIndirectObjects.size());
        assertEquals("The amount of objects retrieved is incorrect. ", expectedSize, indirectObjects.size());
        assertEquals("The first object was not the catalog. ", body.getCatalog(), allIndirectObjects.get(0));
        assertEquals("The first object was not the nl object. ", test, indirectObjects.get(0).getObject());
    }
}
