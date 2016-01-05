package org.toucanpdf.state;

import static org.junit.Assert.assertEquals;

import java.util.Map.Entry;

import org.toucanpdf.api.BaseText;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Text;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseStateCellTextTest {
    private BaseStateCellText text;

    @Before
    public void setUp() {
        text = new BaseStateCellText("Test Test Test Test");
    }

    @Test
    public void testContentHeightCalculation() {
        double height = text.calculateContentHeight(30, 10, new Position(50, 50), true);
        Assert.assertEquals(44.464, height, FloatEqualityTester.EPSILON);
        assertEquals(4, text.getTextSplit().size());
        Position expected = new Position(50, 41.804);
        for (Entry<Position, String> entry : text.getTextSplit().entrySet()) {
            assertEquals(expected, entry.getKey());
            expected.adjustY(-(7.396 + text.getRequiredSpaceAboveLine() + text.getRequiredSpaceBelowLine()));
        }
    }

    @Test
    public void testGettersSetters() {
        Text text2 = new BaseText("Test");
        text.setOriginalObject(text2);
        assertEquals(text2, text.getOriginalObject());
        text.setOriginalObject(null);
        assertEquals(text2, text.getOriginalObject());
        text.marginLeft(10).marginRight(20);
        assertEquals(40, text.getRequiredWidth(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testCopy() {
        Text text2 = new BaseStateCellText(text);
        assertEquals("Test Test Test Test", text2.getText());
    }

}
