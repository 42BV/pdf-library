package nl.mad.toucanpdf.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.Text;

import org.junit.Before;
import org.junit.Test;

public class DocumentStateTest {
    private DocumentState state;
    private List<Page> builderState;
    private Page page1;
    private Page page2;

    @Before
    public void setUp() {
        state = new DocumentState();
        builderState = new LinkedList<Page>();
        Page master = new BasePage(200, 200).add(new BaseText("Master test"));
        page1 = new BasePage(200, 200).master(master);
        page2 = new BasePage(500, 500);
        builderState.add(page1);
        builderState.add(page2);
    }

    @Test
    public void testPageTransfer() {
        state.updateState(builderState);
        assertEquals(2, state.getPages().size());
        //master page content has been added to page1
        assertEquals(1, state.getPages().get(0).getContent().size());
    }

    @Test
    public void testFixedContentPositioning() {
        page1.add(new BaseText("Test").on(10, 10));
        page1.add(new BaseImage(new byte[0], ImageType.JPEG).on(150, 150));
        page1.add(new BaseParagraph().addText(new BaseText("Test Paragraph")).on(20, 20));
        page1.add(new BaseTable(100).on(30, 30).addCell(new BaseCell()));
        state.updateState(builderState);

        Page page = state.getPreviewFor(page1).get(0);
        //content.get(0) is the text from the master page
        assertEquals(DocumentPartType.TEXT, page.getContent().get(1).getType());
        assertTrue(page.getContent().get(1) != null);
        assertEquals(DocumentPartType.IMAGE, page.getContent().get(2).getType());
        assertEquals(DocumentPartType.PARAGRAPH, page.getContent().get(3).getType());
        assertEquals(DocumentPartType.TABLE, page.getContent().get(4).getType());
    }

    @Test
    public void testPreview() {
        Text t = new BaseText("Test");
        Paragraph p = new BaseParagraph();
        Image i = new BaseImage(new byte[0], ImageType.JPEG);
        page1.add(t);
        page1.add(p);
        page1.add(i);
        state.updateState(builderState);
        assertEquals(1, state.getPreviewFor(page1).size());
        assertEquals(4, state.getPreviewFor(page1).get(0).getContent().size());
        assertEquals(1, state.getPreviewFor(t).size());
        assertEquals("Test", state.getPreviewFor(t).get(0).getText());
        assertEquals(1, state.getPreviewFor(p).size());
        assertEquals(1, state.getPreviewFor(i).size());

    }
}