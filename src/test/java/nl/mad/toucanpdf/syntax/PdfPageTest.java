package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import nl.mad.toucanpdf.api.BaseFont;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.PdfNameValue;
import nl.mad.toucanpdf.pdf.syntax.PdfArray;
import nl.mad.toucanpdf.pdf.syntax.PdfDictionary;
import nl.mad.toucanpdf.pdf.syntax.PdfFont;
import nl.mad.toucanpdf.pdf.syntax.PdfImage;
import nl.mad.toucanpdf.pdf.syntax.PdfImageDictionary;
import nl.mad.toucanpdf.pdf.syntax.PdfIndirectObject;
import nl.mad.toucanpdf.pdf.syntax.PdfName;
import nl.mad.toucanpdf.pdf.syntax.PdfObjectType;
import nl.mad.toucanpdf.pdf.syntax.PdfPage;
import nl.mad.toucanpdf.pdf.syntax.PdfStream;
import nl.mad.toucanpdf.pdf.syntax.PdfString;
import nl.mad.toucanpdf.utility.ByteEncoder;

import org.junit.Before;
import org.junit.Test;

public class PdfPageTest {
    private PdfPage page;

    @Before
    public void setUp() throws Exception {
        page = new PdfPage(200, 201, Page.DEFAULT_NEW_LINE_SIZE, 0);
    }

    @Test
    public void testCreation() {
        assertEquals(200, page.getWidth());
        assertEquals(201, page.getHeight());
        assertEquals(Page.DEFAULT_NEW_LINE_SIZE, page.getLeading());
        assertEquals(PdfObjectType.PAGE, page.getType());
        assertEquals(4, ((PdfArray) page.get(PdfNameValue.MEDIA_BOX)).getSize());
    }

    @Test
    public void testAdd() throws IOException {
        //test adding font resource
        PdfIndirectObject indirectObject = new PdfIndirectObject(1, 0, new PdfFont(new BaseFont(), null), true);
        page.add(indirectObject);
        PdfDictionary resources = (PdfDictionary) page.get(new PdfName(PdfNameValue.RESOURCES));
        PdfDictionary fontResource = (PdfDictionary) resources.get(new PdfName(PdfNameValue.FONT));
        assertEquals(true, fontResource.containsValue(indirectObject.getReference()));
        //try adding same object again (this should fail), if it's added it would be under the resource reference R2
        page.add(indirectObject);
        assertEquals(false, fontResource.containsKey(new PdfName("R2")));
        //add new font 
        indirectObject = new PdfIndirectObject(2, 0, new PdfFont(new BaseFont(), null), true);
        page.add(indirectObject);
        assertEquals(true, fontResource.containsValue(indirectObject.getReference()));
        //Try adding XObject
        indirectObject = new PdfIndirectObject(3, 0, new PdfImageDictionary(null), true);
        page.add(indirectObject);
        PdfDictionary XObjectResource = (PdfDictionary) resources.get(new PdfName(PdfNameValue.XOBJECT));
        assertEquals(true, XObjectResource.containsValue(indirectObject.getReference()));

        //test adding content stream
        PdfStream stream = new PdfStream();
        indirectObject = new PdfIndirectObject(4, 0, stream, true);
        page.add(indirectObject);
        assertEquals(stream, page.getCurrentStream());
        assertEquals(1, ((PdfArray) page.get(new PdfName(PdfNameValue.CONTENTS))).getSize());

        //test adding non-supported object
        indirectObject = new PdfIndirectObject(5, 0, new PdfString(), true);
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
    
    @Test
    public void testMarginsAndLeading() {
    	page.setLeading(1);
    	page.setMarginBottom(2);
    	page.setMarginLeft(3);
    	page.setMarginTop(4);
    	page.setMarginRight(5);
    	assertEquals(1, page.getLeading());
    	assertEquals(2, page.getMarginBottom());
    	assertEquals(3, page.getMarginLeft());
    	assertEquals(4, page.getMarginTop());
    	assertEquals(5, page.getMarginRight());
    }
}
