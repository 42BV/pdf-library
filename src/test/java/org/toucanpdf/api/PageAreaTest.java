package org.toucanpdf.api;

import static org.junit.Assert.assertEquals;
import org.toucanpdf.model.PageArea;
import org.toucanpdf.model.Text;

import org.junit.Before;
import org.junit.Test;

public class PageAreaTest {
    private PageArea pageArea;

    @Before
    public void setUp() {
        pageArea = new BasePageArea(100);
    }

    @Test
    public void testAttributes() {
        pageArea.addAttribute("Test", "Test");
        assertEquals("Test", pageArea.getAttribute("Test"));
        assertEquals(1, pageArea.getAttributes().size());
    }

    @Test
    public void testContentAdding() {
        Text t = new BaseText("Test");
        pageArea.add(t);
        assertEquals(t, pageArea.getContent().get(0));
        pageArea.add(null);
        assertEquals(1, pageArea.getContent().size());
    }
}
