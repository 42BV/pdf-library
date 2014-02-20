//package nl.pdflibrary.test.document;
//
//import static org.junit.Assert.assertEquals;
//
//import java.awt.Font;
//import java.io.IOException;
//
//import nl.mad.mad.Document;
//import nl.mad.mad.Text;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class DocumentTest {
//    private Document mad;
//
//    @Before
//    public void setUp() throws Exception {
//        mad = new Document();
//    }
//
//    @Test
//    public void testDocumentCreation() {
//        int pageNumberExpected = 1;
//        int actualPageNumber = mad.getPageNumber();
//        assertEquals("The first page was not added correctly.", pageNumberExpected, actualPageNumber);
//        assertEquals("The mad is already finished.", false, mad.getFinished());
//        assertEquals("The default font was not added to the font list.", Document.DEFAULT_FONT, mad.getFonts().get(0));
//        assertEquals("There is content in the newly created mad.", 0, mad.getDocumentParts().size());
//    }
//
//    @Test
//    public void testAddPart() {
//        Text part = new Text("Test");
//        mad.addPart(part);
//        assertEquals("The part was not added to the mad.", part, mad.getDocumentParts().get(0));
//        assertEquals("The default font was not added to the text.", Document.DEFAULT_FONT, part.getFont());
//
//        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
//        part = new Text("Test", f);
//        mad.addPart(part);
//        assertEquals("The new font was not added to the mad correctly.", mad.getFonts().get(1), part.getFont());
//    }
//
//    @Test
//    public void testAddFont() {
//        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 13);
//        mad.addFont(f);
//        assertEquals("The new font was not added to the mad correctly.", mad.getFonts().get(1), f);
//    }
//
//    @Test
//    public void testAddNewPage() {
//        int expectedAmount = mad.getPageNumber() + 1;
//        mad.addNewPage();
//        assertEquals("The new page has not been added correctly.", expectedAmount, mad.getPageNumber());
//    }
//
//    @Test
//    public void testFinish() throws IOException {
//        mad.finish();
//        assertEquals("The mad has not been finished.", true, mad.getFinished());
//    }
//
//}
