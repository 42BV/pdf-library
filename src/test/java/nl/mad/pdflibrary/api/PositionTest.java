package nl.mad.pdflibrary.api;

import static org.junit.Assert.assertEquals;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class PositionTest {
    private Position pos;
    private final int testPositionValue = 200;

    @Before
    public void setUp() throws Exception {
        pos = new Position();
    }

    @Test
    public void testCustomPosition() {
        assertEquals("The custom positioning was not set correctly.", false, pos.hasCustomPosition());
        pos.setX(testPositionValue);
        assertEquals("The custom positioning was not set correctly.", true, pos.hasCustomPosition());
        pos.setY(testPositionValue);
        assertEquals("The custom positioning was not set correctly.", true, pos.hasCustomPosition());
        pos.setX(Position.UNUSED_VALUE);
        assertEquals("The custom positioning was not set correctly.", true, pos.hasCustomPosition());
    }

    @Test
    public void testPositioning() {
        pos.setX(testPositionValue);
        pos.setY(testPositionValue + 1);
        assertEquals("The positioning for X was not set correctly.", testPositionValue, pos.getX(), FloatEqualityTester.EPSILON);
        assertEquals("The positioning for Y was not set correctly.", testPositionValue + 1, pos.getY(), FloatEqualityTester.EPSILON);
    }
}
