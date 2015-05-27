package nl.mad.toucanpdf.state;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.toucanpdf.api.BaseCell;
import nl.mad.toucanpdf.api.BaseTable;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class BaseStateTableTest {

    private BaseStateTable table;

    @Before
    public void setUp() {
        table = new BaseStateTable(100);
    }

    @Test
    public void testMargins() {
        table.marginTop(20);
        assertEquals(20, table.getMarginTop());
        assertEquals(20, table.getRequiredSpaceAbove(), FloatEqualityTester.EPSILON);
        table.marginBottom(30);
        assertEquals(30, table.getRequiredSpaceBelow(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testPositioning(@Mocked final StatePage page) {
        new NonStrictExpectations() {
            {
                page.getOpenSpacesIncludingHeight(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<int[]>(), new ArrayList<int[]>(Arrays.asList(new int[] { 0, 10, 10 }, new int[] { 10, 300, 400 })));
                page.getLeading();
                returns(10);
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(100, 100));
                page.getHeightWithoutMargins();
                returns(400, 0);
            }
        };
        table.columns(2);
        table.drawFillerCells(false);
        BaseStateCellText text = new BaseStateCellText("Test");
        BaseStateCellText text2 = new BaseStateCellText("Test2");
        table.addCell(text);
        table.addCell(text2).columnSpan(2);
        table.addCell(new BaseCell().height(20));
        table.processContentSize(page);
        List<Cell> cells = table.getContent();

        Cell c1 = cells.get(0);
        Cell c2 = cells.get(1);
        Cell c3 = cells.get(2);

        assertEquals(new Position(100, 100), table.getPosition());
        assertEquals(45.40399, table.getContentHeight(page), FloatEqualityTester.EPSILON);
        assertEquals(90, table.getContentWidth(page, table.getPosition()), FloatEqualityTester.EPSILON);
        assertEquals(new Position(100, 100), c1.getPosition());
        assertEquals(50, c1.getWidth(), FloatEqualityTester.EPSILON);
        assertEquals(25.404, c1.getHeight(), FloatEqualityTester.EPSILON);
        assertEquals(new Position(150, 100), c2.getPosition());
        assertEquals(25.404, c2.getHeight(), FloatEqualityTester.EPSILON);
        assertEquals(50, c2.getWidth(), FloatEqualityTester.EPSILON);
        assertEquals(20, c3.getHeight(), FloatEqualityTester.EPSILON);
        assertEquals(50, c3.getWidth(), FloatEqualityTester.EPSILON);
        assertEquals(new Position(), text.getPosition());
        assertEquals(new Position(), text2.getPosition());
    }

    @Test
    public void testHeightUpdating(@Mocked final StatePage page) {
        new NonStrictExpectations() {
            {
                page.getOpenSpacesIncludingHeight(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<int[]>(), new ArrayList<int[]>(Arrays.asList(new int[] { 0, 10, 10 }, new int[] { 10, 300, 400 })));
                page.getLeading();
                returns(10);
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(100, 100));
                page.getHeightWithoutMargins();
                returns(400, 0);
            }
        };
        table.addCell("Test");
        table.updateHeight(page);
        assertEquals(25.404, table.getHeight(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testGettingUsedSpaces() {
        table.allowWrapping(true);
        table.width(300);
        table.marginBottom(20);
        table.marginTop(10);
        table.on(100, 100);
        List<int[]> usedSpaces = table.getUsedSpaces(110, 600);
        assertEquals(100, usedSpaces.get(0)[0]);
        assertEquals(400, usedSpaces.get(0)[1]);
        table.allowWrapping(false);
        usedSpaces = table.getUsedSpaces(110, 600);
        assertEquals(0, usedSpaces.get(0)[0]);
        assertEquals(600, usedSpaces.get(0)[1]);
        usedSpaces = table.getUsedSpaces(130, 600);
        assertEquals(0, usedSpaces.size());
        usedSpaces = table.getUsedSpaces(70, 600);
        assertEquals(0, usedSpaces.size());
    }

    @Test
    public void testGettingPosition() {
        table.width(300);
        table.marginBottom(20);
        table.marginTop(10);
        table.on(100, 100);
        int[] positions = table.getPositionAt(110);
        assertEquals(100, positions[0]);
        assertEquals(1, positions.length);
        positions = table.getPositionAt(140);
        assertEquals(0, positions.length);
        positions = table.getPositionAt(70);
        assertEquals(0, positions.length);
    }

    @Test
    public void testSettersGetters() {
        Table table2 = new BaseTable(100);
        table.setOriginalObject(table2);
        assertEquals(table2, table.getOriginalObject());
        table.setOriginalObject(null);
        assertEquals(table2, table.getOriginalObject());
        table.marginLeft(20);
        assertEquals(20, table.getRequiredSpaceLeft(), FloatEqualityTester.EPSILON);
        table.marginRight(30);
        assertEquals(30, table.getRequiredSpaceRight(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testCopy() {
        table.marginBottom(10);
        table.addCell(new BaseText("Test"));
        BaseStateTable copy = (BaseStateTable) table.copy();
        assertEquals(10, copy.getMarginBottom());
        assertEquals(0, copy.getContent().size());
    }

    @Test
    public void testRemove() {
        table.addCell("Test");
        assertEquals(1, table.getContent().size());
        table.removeContent();
        assertEquals(0, table.getContent().size());
    }

    @Test
    public void testAlignment(@Mocked final StatePage page) {
        new NonStrictExpectations() {
            {
                page.getOpenSpacesIncludingHeight(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<int[]>(), new ArrayList<int[]>(Arrays.asList(new int[] { 0, 10, 10 }, new int[] { 10, 300, 400 })));
                page.getLeading();
                returns(10);
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(100, 100));
                page.getHeightWithoutMargins();
                returns(400, 0);
                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<int[]>(Arrays.asList(new int[] { 0, 200 }, new int[] { 250, 500 })));
            }
        };
        table.width(50);
        table.on(100, 100);
        table.align(Alignment.LEFT);
        table.processContentSize(page, false, true, true, false);
        assertEquals(100, table.getPosition().getX(), FloatEqualityTester.EPSILON);
        table.align(Alignment.RIGHT);
        table.processContentSize(page, false, true, true, false);
        assertEquals(300, table.getPosition().getX(), FloatEqualityTester.EPSILON);
        table.on(100, 100);
        table.align(Alignment.CENTERED);
        table.processContentSize(page, false, true, true, false);
        assertEquals(200, table.getPosition().getX(), FloatEqualityTester.EPSILON);
        table.on(100, 100);
        table.align(Alignment.JUSTIFIED);
        table.processContentSize(page, false, true, true, false);
        assertEquals(100, table.getPosition().getX(), FloatEqualityTester.EPSILON);
    }
}
