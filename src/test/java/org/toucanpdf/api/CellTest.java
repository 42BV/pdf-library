package org.toucanpdf.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.toucanpdf.model.Cell;
import org.toucanpdf.model.Image;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class CellTest {
    private Cell c;

    @Before
    public void setup() {
        c = new BaseCell();
    }

    @Test
    public void testCopy() {
        c.columnSpan(5);
        c.width(20);
        c.height(30);
        Cell b = new BaseCell(c);
        assertTrue(b.getColumnSpan() == 5 && FloatEqualityTester.equals(b.getWidth(), 20.0) && FloatEqualityTester.equals(b.getHeight(), 30.0));
    }

    @Test
    public void testContentSetter() {
        Image i = new BaseImage();
        c.content(i);
        assertEquals(i, c.getContent());
    }
}
