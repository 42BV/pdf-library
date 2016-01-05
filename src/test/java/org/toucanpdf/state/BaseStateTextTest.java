package org.toucanpdf.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.toucanpdf.api.BaseText;
import org.toucanpdf.model.Alignment;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Space;
import org.toucanpdf.model.state.StatePage;
import org.toucanpdf.model.state.StateText;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseStateTextTest {
    private BaseText text;
    private StatePage page;

    @Before
    public void setUp() {
        page = new BaseStatePage(110, 100);
        text = new BaseText();
        text.size(11);
    }

    @Test
    public void testPositioning(@Mocked final StatePage page) {
        text.text("test test test test test test test test test test test test test test test test test test test test test");
        BaseStateText stateText = new BaseStateText(text);
        new NonStrictExpectations() {
            {
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 90));

                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new LinkedList<Space>(Arrays.asList(new Space(0, 30), new Space(79, 110))),
                        new LinkedList<Space>(Arrays.asList(new Space(0, 110))));

                page.getOpenPosition(anyDouble, anyDouble, anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 80.1), new Position(0, 70.19999999999999), null);

                page.getWidth();
                returns(110);

                page.getHeight();
                returns(100);

                page.getMarginRight();
                returns(0);
            }
        };

        Position pos = page.getOpenPosition(stateText.getRequiredSpaceAbove(), stateText.getRequiredSpaceBelow(), stateText, 10);
        stateText.setPosition(pos);

        //test wrapping around fixed text
        stateText.processContentSize(page, 0, false);
        Map<Position, String> textSplit = stateText.getTextSplit();
        double height = pos.getY();
        int i = 0;
        Position position = new Position(pos);
        String expectedString = "test ";
        for (Entry<Position, String> entry : textSplit.entrySet()) {
            assertTrue(position.equals(entry.getKey()));
            assertEquals(expectedString, entry.getValue());
            if (i == 0) {
                position.adjustX(79);
                expectedString = "test test ";
            } else {
                expectedString = "test test test test test test ";
                position.adjustY(-(stateText.getRequiredSpaceAboveLine() + stateText.getRequiredSpaceBelowLine() + page.getLeading()));
                position.setX(0);
            }
            ++i;
        }
        stateText = new BaseStateText(text);
        StateText overflow = stateText.processContentSize(page, 0, false);
        assertNotEquals(null, overflow);

        StateText fixed = new BaseStateText("Fixed test");
        fixed.setPosition(new Position(30, 90));
        fixed.processContentSize(page, 0, true);
        assertEquals(new Position(30, 90), fixed.getPosition());
    }

    @Test
    public void testCutoff(@Mocked final StatePage page) {
        text.text("Thisisareallyreallyreallyreallyreallyreallyreallyreallyreallylongstring");
        BaseStateText stateText = new BaseStateText(text);
        new NonStrictExpectations() {
            {
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 90));

                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new LinkedList<Space>(Arrays.asList(new Space(0, 35), new Space(40, 110))));

                page.getWidth();
                returns(110);

                page.getHeight();
                returns(100);

                page.getMarginRight();
                returns(0);
            }
        };
        stateText.processContentSize(page, 0, false);
        assertEquals("Thisis-", stateText.getTextSplit().get(new Position(0.0, -1.0)));
    }

    @Test
    public void testAlignment(@Mocked final StatePage page) {
        text.text("Test");
        BaseStateText stateText = new BaseStateText(text);
        new NonStrictExpectations() {
            {
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 30));

                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new LinkedList<>(Arrays.asList(new Space(0, 30))));

                page.getWidth();
                returns(30);

                page.getHeight();
                returns(100);

                page.getMarginRight();
                returns(0);
            }
        };
        stateText.align(Alignment.CENTERED);
        stateText.processContentSize(page, 0, false);
        assertEquals("Test ", stateText.getTextSplit().get(new Position(3.1639999999999997, -1.0)));
        stateText.align(Alignment.RIGHT);
        stateText.processContentSize(page, 0, false);
        assertEquals("Test ", stateText.getTextSplit().get(new Position(6.327999999999999, -1.0)));
        stateText.text("Test Test Test Test Test Test Test Test Test");
        stateText.align(Alignment.JUSTIFIED);
        stateText.processContentSize(page, 0, false);
        Assert.assertEquals(2.6649999999999999, stateText.getJustificationOffset().get(new Position(0.0, -1.0)), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testUsedSpaces(@Mocked final StatePage page) {
        text.text("Test");
        BaseStateText stateText = new BaseStateText(text);
        stateText.setPosition(new Position(0, 100));
        new NonStrictExpectations() {
            {
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 90));

                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new LinkedList<>(Arrays.asList(new Space(0, 90))));

                page.getWidth();
                returns(90);

                page.getHeight();
                returns(100);

                page.getMarginRight();
                returns(0);
            }
        };
        stateText.processContentSize(page, 0, false);
        List<Space> usedSpaces = stateText.getUsedSpaces(100, 90);
        assertEquals(0, usedSpaces.get(0).getStartPoint());
        assertEquals(20, usedSpaces.get(0).getEndPoint());

        stateText.marginTop(10);
        stateText.marginBottom(10);
        usedSpaces = stateText.getUsedSpaces(100, 90);
        assertEquals(0, usedSpaces.get(0).getStartPoint());
        assertEquals(90, usedSpaces.get(0).getEndPoint());
    }

    @Test
    public void testContentSizes(@Mocked final StatePage page) {
        text.text("Test test test test test test test test test test ");
        BaseStateText stateText = new BaseStateText(text);
        stateText.setPosition(new Position(0, 100));
        new NonStrictExpectations() {
            {
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 30));

                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new LinkedList<>(Arrays.asList(new Space (0, 90))));

                page.getOpenPosition(anyDouble, anyDouble, anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 80), new Position(0, 70));

                page.getWidth();
                returns(90);

                page.getHeight();
                returns(100);

                page.getMarginRight();
                returns(0);
            }
        };
        stateText.processContentSize(page, 10, false);
        assertEquals(39.9, stateText.getContentHeight(page), FloatEqualityTester.EPSILON);
        assertEquals(32.387, stateText.getContentHeightUnderBaseLine(page), FloatEqualityTester.EPSILON);
        assertEquals(77.0, stateText.getContentWidth(page, new Position(0, 100)), FloatEqualityTester.EPSILON);
        assertEquals(0, stateText.getPositionAt(100)[0]);
        stateText.setMarginLeft(5);
        Assert.assertEquals(5, stateText.getMarginLeft());
    }
}
