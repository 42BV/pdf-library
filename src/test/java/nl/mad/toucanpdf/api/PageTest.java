package nl.mad.toucanpdf.api;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import nl.mad.toucanpdf.api.BasePage;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Page;

import org.junit.Before;
import org.junit.Test;

public class PageTest {
    private Page page;

    @Before
    public void setUp() throws Exception {
        page = new BasePage(200, 200);
    }

    @Test
    public void testType() {
        assertEquals("Type was set incorrectly. ", DocumentPartType.PAGE, page.getType());
    }

    @Test
    public void testAdd() {
        page.add(new BaseText("test"));
        assertEquals("Content was not added correctly. ", 1, page.getContent().size());

        page.add(null);
        assertEquals("Content was not added correctly. ", 1, page.getContent().size());
        
        page.addAll(new ArrayList<DocumentPart>(Arrays.asList(new BaseText(), new BaseText())));
        assertEquals(3, page.getContent().size());
    }

    @Test
    public void testSize() {
        page.size(210, 211);
        assertEquals("Size was adjusted incorrectly. ", 210, page.getWidth());
        assertEquals("Size was adjusted incorrectly. ", 211, page.getHeight());
    }
    
    @Test
    public void testObjectRetrieval() {
    	page.add(new BaseText().on(100, 100));
    	page.add(new BaseText());
    	page.add(new BaseText());
    	
    	assertEquals(1, page.getFixedPositionContent().size());
    	assertEquals(2, page.getPositionlessContent().size());
    }
    
    @Test
    public void testMargins() {
    	page.marginBottom(1);
    	page.marginLeft(2);
    	page.marginRight(3);
    	page.marginTop(4);
    	assertEquals(1, page.getMarginBottom());
    	assertEquals(2, page.getMarginLeft());
    	assertEquals(3, page.getMarginRight());
    	assertEquals(4, page.getMarginTop());
    	assertEquals(195, page.getHeightWithoutMargins());
    	assertEquals(195, page.getWidthWithoutMargins());
    }
    
    @Test
    public void testLeading()  {
    	page.leading(10);
    	assertEquals(10, page.getLeading());
    	page.leading(-10);
    	//unchanged because leading < 0
    	assertEquals(10, page.getLeading());
    }
    
    @Test
    public void testMasterPage() {
    	Page master = new BasePage(50, 50);
    	master.add(new BaseText());
    	page.master(master);
    	assertEquals(50, page.getWidth());
    	assertEquals(50, page.getHeight());
    	assertEquals(master, page.getMasterPage());
    }
}
