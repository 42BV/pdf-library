package nl.mad.toucanpdf.state;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.toucanpdf.api.BasePage;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

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
                returns(new LinkedList<int[]>(Arrays.asList(new int[] { 0, 20 })));
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
        assertEquals(20, p.getMarginTop());
        assertEquals(20, p.getFilledHeight(), FloatEqualityTester.EPSILON);
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
                returns(new LinkedList<int[]>(Arrays.asList(new int[] { 30, 60 })));
                text3.getPosition();
                returns(new Position(0, 50));
            }
        };
        page.add(text2);
        page.add(text3);
        List<int[]> openSpaces = page.getOpenSpacesIncludingHeight(new Position(0, 110), true, 0.0, 0.0, text);
        assertEquals(20, openSpaces.get(0)[0]);
        assertEquals(30, openSpaces.get(0)[1]);
        assertEquals(90, openSpaces.get(0)[2]);
        assertEquals(30, openSpaces.get(1)[0]);
        assertEquals(60, openSpaces.get(1)[1]);
        assertEquals(60, openSpaces.get(1)[2]);
        assertEquals(60, openSpaces.get(2)[0]);
        assertEquals(87, openSpaces.get(2)[1]);
        assertEquals(90, openSpaces.get(2)[2]);

        assertEquals(0, page.getOpenSpacesIncludingHeight(null, true, 0.0, 0.0, text).size());
    }

    @Test
    public void testSettersGetters() {
        Page orig = new BasePage(20, 20);
        page.setOriginalObject(orig);
        assertEquals(orig, page.getOriginalObject());

        page.setFilledHeight(15);
        assertEquals(15, page.getFilledHeight(), FloatEqualityTester.EPSILON);
        assertEquals(55, page.getRemainingHeight(), FloatEqualityTester.EPSILON);
        page.setFilledWidth(10);
        assertEquals(10, page.getFilledWidth(), FloatEqualityTester.EPSILON);
        assertEquals(60, page.getRemainingWidth(), FloatEqualityTester.EPSILON);
    }
}
