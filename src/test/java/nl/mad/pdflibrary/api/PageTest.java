package nl.mad.pdflibrary.api;

import static org.junit.Assert.assertEquals;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Page;

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
    }

    @Test
    public void testSize() {
        page.size(210, 211);
        assertEquals("Size was adjusted incorrectly. ", 210, page.getWidth());
        assertEquals("Size was adjusted incorrectly. ", 211, page.getHeight());
    }
}
