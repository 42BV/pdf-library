package org.toucanpdf.state;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.toucanpdf.api.BasePage;
import org.toucanpdf.model.Page;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Space;
import org.toucanpdf.model.state.StatePage;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseStatePageTest {
    private StatePage page;
    @Mocked
    BaseStateText text;
    @Mocked
    BaseStateText text2;

    @Before
    public void setUp() {
        new NonStrictExpectations() {
            {
                text.getRequiredSpaceLeft();
                returns(2.0);
                text.getRequiredSpaceRight();
                returns(3.0);
                text.getRequiredSpaceBelow();
                returns(5.0);
                text2.getUsedSpaces(110, anyInt);
                returns(new LinkedList<Space>(Arrays.asList(new Space(0, 20))));
                text2.getPosition();
                returns(new Position(0, 110));
                text2.getPositionAt(110);
                returns(new int[] { 0 });
            }
        };
        page = new BaseStatePage(110, 110);
    }

    @Test
    public void testCopy() {
        page.marginTop(20);
        StatePage p = new BaseStatePage(page);
        Assert.assertEquals(20, p.getMarginTop());
        Assert.assertEquals(20, p.getFilledHeight(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testOpenPositionRetrieval() {
        page = new BaseStatePage(30, 30);
        page.marginBottom(0).marginTop(0);
        Position pos = page.getOpenPosition(10.0, 5.0, text, 0);
        assertEquals(new Position(20, 20), pos);

        page = new BaseStatePage(110, 110);
        page.add(text2);
        pos = page.getOpenPosition(0.0, 5.0, text);
        assertEquals(new Position(20, 110), pos);

        page = new BaseStatePage(10, 10);
        page.marginBottom(10);
        pos = page.getOpenPosition(0.0, 0.0, text);
        assertEquals(null, pos);

        page = new BaseStatePage(10, 10);
        page.marginBottom(6);
        pos = page.getOpenPosition(0.0, 5.0, text);
        assertEquals(null, pos);
    }

    @Test
    public void testAvailableWidth() {
        assertEquals(85, page.getTotalAvailableWidth(new Position(0, 110), 0.0, 0.0, text));
        page.add(text2);
        assertEquals(67, page.getTotalAvailableWidth(new Position(0, 110), 0.0, 0.0, text));
    }

    @Test
    public void testOpenSpacesIncludingHeight(@Mocked final BaseStateText text3) {
        new NonStrictExpectations() {
            {
                text3.getUsedSpaces(50, anyInt);
                returns(new LinkedList<Space>(Arrays.asList(new Space(30, 60))));
                text3.getPosition();
                returns(new Position(0, 50));
            }
        };
        page.add(text2);
        page.add(text3);
        List<Space> openSpaces = page.getOpenSpacesIncludingHeight(new Position(0, 110), true, 0.0, 0.0, text);
        assertEquals(20, openSpaces.get(0).getStartPoint());
        assertEquals(30, openSpaces.get(0).getEndPoint());
        assertEquals(90, openSpaces.get(0).getHeight().intValue());
        assertEquals(30, openSpaces.get(1).getStartPoint());
        assertEquals(60, openSpaces.get(1).getEndPoint());
        assertEquals(60, openSpaces.get(1).getHeight().intValue());
        assertEquals(60, openSpaces.get(2).getStartPoint());
        assertEquals(87, openSpaces.get(2).getEndPoint());
        assertEquals(90, openSpaces.get(2).getHeight().intValue());

        assertEquals(0, page.getOpenSpacesIncludingHeight(null, true, 0.0, 0.0, text).size());
    }

    @Test
    public void testSettersGetters() {
        Page orig = new BasePage(20, 20);
        page.setOriginalObject(orig);
        Assert.assertEquals(orig, page.getOriginalObject());

        page.setFilledHeight(15);
        assertEquals(15, page.getFilledHeight(), FloatEqualityTester.EPSILON);
        assertEquals(55, page.getRemainingHeight(), FloatEqualityTester.EPSILON);
        page.setFilledWidth(10);
        assertEquals(10, page.getFilledWidth(), FloatEqualityTester.EPSILON);
        assertEquals(60, page.getRemainingWidth(), FloatEqualityTester.EPSILON);
    }
}
