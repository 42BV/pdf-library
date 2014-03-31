package nl.mad.pdflibrary.structure;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import nl.mad.pdflibrary.pdf.structure.PdfBody;
import nl.mad.pdflibrary.pdf.syntax.PdfIndirectObject;
import nl.mad.pdflibrary.pdf.syntax.PdfName;
import nl.mad.pdflibrary.pdf.syntax.PdfPage;

import org.junit.Before;
import org.junit.Test;

public class PdfBodyTest {
    private PdfBody body;
    private final int pageSize = 200;

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
        PdfName test = new PdfName("Test");
        body.addObject(test);
        int expectedObjectPos = 2;
        assertEquals("Object has not been correctly added to the body.", test, body.getAllIndirectObjects().get(expectedObjectPos).getObject());
    }

    @Test
    public void testAddPage() {
        PdfPage page = new PdfPage(pageSize, pageSize);
        body.addPage(page);
        int expectedObjectPos = 2;
        assertEquals("Page has not been correctly added to the body.", page, body.getAllIndirectObjects().get(expectedObjectPos).getObject());

    }

    @Test
    public void testStartByteSetting() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        String testString = "test";
        dos.writeBytes(testString);
        body.writeToFile(dos);
        int expectedStartByte = testString.length();
        assertEquals("The start byte is incorrect.", expectedStartByte, body.getAllIndirectObjects().get(0).getStartByte());
    }

    @Test
    public void testGetIndirectObjects() {
        PdfName test = new PdfName("test");
        body.addObject(test);
        PdfPage page = new PdfPage(pageSize, pageSize);
        body.addPage(page);

        int expectedTotalSize = 4;
        int expectedSize = 1;
        List<PdfIndirectObject> allIndirectObjects = body.getAllIndirectObjects();
        List<PdfIndirectObject> indirectObjects = body.getIndirectObjects();
        assertEquals("The amount of objects retrieved is incorrect. ", expectedTotalSize, body.getTotalIndirectObjectsAmount());
        assertEquals("The amount of objects retrieved is incorrect. ", expectedTotalSize, allIndirectObjects.size());
        assertEquals("The amount of objects retrieved is incorrect. ", expectedSize, indirectObjects.size());
        assertEquals("The first object was not the catalog. ", body.getCatalog(), allIndirectObjects.get(0));
        assertEquals("The first object was not the nl object. ", test, indirectObjects.get(0).getObject());
    }

    @Test
    public void testWrite() {

    }
}
