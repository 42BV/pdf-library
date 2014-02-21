package test.java.nl.mad.pdflibrary.pdf;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import nl.mad.pdflibrary.pdf.PdfCrossReferenceTable;
import nl.mad.pdflibrary.pdf.object.PdfIndirectObject;
import nl.mad.pdflibrary.pdf.object.PdfName;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class PdfCrossReferenceTableTest {
    private PdfCrossReferenceTable xref;

    @Before
    public void setUp() throws Exception {
        xref = new PdfCrossReferenceTable();
    }

    @Test
    public void testFillTableWithIndirectObjects() {
        ArrayList<PdfIndirectObject> testArray = new ArrayList<PdfIndirectObject>();
        testArray.add(new PdfIndirectObject(1, 0, new PdfName("nl"), true));
        xref.fillTableWithIndirectObjects(testArray);

        assertEquals("Cross reference number is incorrect. ", true, xref.isObjectInTable(1));
        assertEquals("The amount of cross references is incorrect. ", 1, xref.getCrossReferenceAmount());
    }

    @Test
    public void testWriteToFile() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        ArrayList<PdfIndirectObject> testArray = new ArrayList<PdfIndirectObject>();
        testArray.add(new PdfIndirectObject(1, 0, new PdfName("nl"), true));
        xref.fillTableWithIndirectObjects(testArray);
        dos.writeChars("a");
        xref.writeToFile(dos);

        assertEquals("Start byte is incorrect. ", "2".getBytes(), xref.getStartByte());
        //TODO: nl the writing itself, also nl the inner cross reference class somehow?
    }
}
