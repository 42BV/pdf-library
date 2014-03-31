package nl.mad.pdflibrary.syntax;

import static org.junit.Assert.assertEquals;
import nl.mad.pdflibrary.api.BaseFont;
import nl.mad.pdflibrary.model.PdfNameValue;
import nl.mad.pdflibrary.pdf.syntax.PdfArray;
import nl.mad.pdflibrary.pdf.syntax.PdfDictionary;
import nl.mad.pdflibrary.pdf.syntax.PdfFont;
import nl.mad.pdflibrary.pdf.syntax.PdfIndirectObject;
import nl.mad.pdflibrary.pdf.syntax.PdfName;
import nl.mad.pdflibrary.pdf.syntax.PdfObjectType;
import nl.mad.pdflibrary.pdf.syntax.PdfPage;
import nl.mad.pdflibrary.pdf.syntax.PdfStream;
import nl.mad.pdflibrary.pdf.syntax.PdfString;
import nl.mad.pdflibrary.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class PdfPageTest {
    private PdfPage page;

    @Before
    public void setUp() throws Exception {
        page = new PdfPage(200, 201);
    }

    @Test
    public void testCreation() {
        assertEquals(200, page.getWidth());
        assertEquals(201, page.getHeight());
        assertEquals(PdfObjectType.PAGE, page.getType());
        assertEquals(0, page.getFilledWidth(), FloatEqualityTester.EPSILON);
        assertEquals(0, page.getFilledHeight(), FloatEqualityTester.EPSILON);
        assertEquals(4, ((PdfArray) page.get(PdfNameValue.MEDIA_BOX)).getSize());
    }

    @Test
    public void testAdd() {
        //test adding font resource
        PdfIndirectObject indirectObject = new PdfIndirectObject(1, 0, new PdfFont(new BaseFont()), true);
        page.add(indirectObject);
        PdfDictionary resources = (PdfDictionary) page.get(new PdfName(PdfNameValue.RESOURCES));
        PdfDictionary fontResource = (PdfDictionary) resources.get(new PdfName(PdfNameValue.FONT));
        assertEquals(true, fontResource.containsValue(indirectObject.getReference()));
        //try adding same object again (this should fail), if it's added it would be under the resource reference R2
        page.add(indirectObject);
        assertEquals(false, fontResource.containsKey(new PdfName("R2")));
        //add new font 
        indirectObject = new PdfIndirectObject(2, 0, new PdfFont(new BaseFont()), true);
        page.add(indirectObject);
        assertEquals(true, fontResource.containsValue(indirectObject.getReference()));

        //test adding content stream
        PdfStream stream = new PdfStream();
        indirectObject = new PdfIndirectObject(3, 0, stream, true);
        page.add(indirectObject);
        assertEquals(stream, page.getCurrentStream());
        assertEquals(1, ((PdfArray) page.get(new PdfName(PdfNameValue.CONTENTS))).getSize());

        //test adding non-supported object
        indirectObject = new PdfIndirectObject(4, 0, new PdfString(), true);
        page.add(indirectObject);
        assertEquals(1, ((PdfArray) page.get(new PdfName(PdfNameValue.CONTENTS))).getSize());
        assertEquals(false, fontResource.containsValue(indirectObject.getReference()));
    }

    @Test
    public void testStreamActions() {
        assertEquals(true, page.streamEmpty());
        PdfStream stream = new PdfStream();
        page.setCurrentStream(stream);
        assertEquals(true, page.streamEmpty());
        stream.add(new PdfName("test"));
        assertEquals(false, page.streamEmpty());

    }
}
