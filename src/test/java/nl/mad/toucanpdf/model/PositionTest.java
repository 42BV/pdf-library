package nl.mad.toucanpdf.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PositionTest {
    private Position pos;

    @Before
    public void setUp() throws Exception {
        pos = new Position();
    }

    @Test
    public void testCustomValues() {
        assertEquals(false, pos.hasCustomPosition());
        assertEquals(false, pos.hasCustomXValue());
        assertEquals(false, pos.hasCustomYValue());
        pos.setX(10);
        assertEquals(true, pos.hasCustomXValue());
        assertEquals(true, pos.hasCustomPosition());
        pos.setY(20);
        assertTrue(pos.hasCustomYValue());
    }

    @Test
    public void testToString() {
        assertEquals("-1.0:-1.0", pos.toString());
    }

    @Test
    public void testEquals() {
        assertEquals(pos, new Position());
    }

}
