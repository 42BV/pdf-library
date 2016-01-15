package org.toucanpdf.state;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.toucanpdf.api.BaseCell;
import org.toucanpdf.api.BaseImage;
import org.toucanpdf.api.BaseTable;
import org.toucanpdf.api.BaseText;
import org.toucanpdf.model.*;
import org.toucanpdf.model.state.StateCell;
import org.toucanpdf.model.state.StateCellText;
import org.toucanpdf.model.state.StatePage;
import org.toucanpdf.model.state.StateTable;
import org.toucanpdf.state.Table.BaseStateTable;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Assert;
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
    public void testPositioning() {
        StatePage page = new BaseStatePage(800, 800);

        table.columns(2);
        table.drawFillerCells(false);
        BaseStateCellText text = new BaseStateCellText("Test");
        BaseStateCellText text2 = new BaseStateCellText("Test2");
        table.addCell(text);
        table.addCell(text2).columnSpan(2);
        table.addCell(text);
        table.addCell(text);
        table.addCell(new BaseCell().height(20));
        StateTable overflow = table.processContentSize(page);
        Assert.assertNull(overflow);
        List<StateCell> cells = table.getStateCellCollection();
        Assert.assertEquals(5, cells.size());

        Cell c1 = cells.get(0);
        Cell c2 = cells.get(1);
        Cell c3 = cells.get(2);
        Cell c4 = cells.get(3);
        Cell c5 = cells.get(4);

        assertEquals(new Position(20, 797), table.getPosition());
        assertEquals(58.296, table.getContentHeight(page), FloatEqualityTester.EPSILON);
        assertEquals(100, table.getContentWidth(page, table.getPosition()), FloatEqualityTester.EPSILON);
        //header position is always null, width and height are also undetermined
        assertEquals(new Position(20, 797), c1.getPosition());
        assertEquals(47, c1.getWidth(), FloatEqualityTester.EPSILON);
        assertEquals(19.232, c1.getHeight(), FloatEqualityTester.EPSILON);
        assertEquals(new Position(67, 797), c2.getPosition());
        assertEquals(19.232, c2.getHeight(), FloatEqualityTester.EPSILON);
        assertEquals(53, c2.getWidth(), FloatEqualityTester.EPSILON);
        assertEquals(new Position(20, 777.768), c3.getPosition());
        assertEquals(19.064, c3.getHeight(), FloatEqualityTester.EPSILON);
        assertEquals(47.0, c3.getWidth(), FloatEqualityTester.EPSILON);
        assertEquals(new Position(67, 777.768), c4.getPosition());
        assertEquals(19.064, c4.getHeight(), FloatEqualityTester.EPSILON);
        assertEquals(53, c4.getWidth(), FloatEqualityTester.EPSILON);
        assertEquals(new Position(20, 758.7040000000001), c5.getPosition());
        assertEquals(20, c5.getHeight(), FloatEqualityTester.EPSILON);
        assertEquals(47.0, c5.getWidth(), FloatEqualityTester.EPSILON);
        assertEquals(new Position(), text.getPosition());
        assertEquals(new Position(), text2.getPosition());
    }

    @Test
    public void testHeightUpdating() {
        StatePage page = new BaseStatePage(800, 800);
        table.addCell("Test");
        table.updateHeight(page);
        assertEquals(19.064, table.getHeight(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testVerticalAlignment() {
        StatePage page = new BaseStatePage(800, 800);
        StatePage page2 = new BaseStatePage(800, 800);
        table.columns(2);
        table.width(200);
        BaseStateTable table2 = new BaseStateTable(table);

        //vertical alignment is only used when there is a height difference between columns
        table.addCell("Test");
        table.addCell("Test test test test test test test test");
        table.processContentSize(page);
        List<StateCell> cells = table.getStateCellCollection();
        StateCellText textObj = (StateCellText) cells.get(0).getStateCellContent();
        assertEquals(new Position(25.5, 783.304), textObj.getTextSplit().entrySet().iterator().next().getKey());

        table2.addCell("Test");
        table2.addCell("Test test test test test test test test");
        table2.verticalAlign(true);
        table2.processContentSize(page2);
        cells = table2.getStateCellCollection();
        textObj = (StateCellText) cells.get(0).getStateCellContent();
        assertEquals(new Position(25.5, 778.2719999999999), textObj.getTextSplit().entrySet().iterator().next().getKey());
    }

    @Test
    public void testGettingUsedSpaces() {
        table.allowWrapping(true);
        table.width(300);
        table.marginBottom(20);
        table.marginTop(10);
        table.on(100, 100);
        List<Space> usedSpaces = table.getUsedSpaces(110, 600);
        assertEquals(100, usedSpaces.get(0).getStartPoint());
        assertEquals(400, usedSpaces.get(0).getEndPoint());
        table.allowWrapping(false);
        usedSpaces = table.getUsedSpaces(110, 600);
        assertEquals(0, usedSpaces.get(0).getStartPoint());
        assertEquals(600, usedSpaces.get(0).getEndPoint());
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
    public void testOverflow(@Mocked final StatePage page) {
        new NonStrictExpectations() {
            {
                page.getOpenSpacesIncludingHeight(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<Space>(), new ArrayList<>(Arrays.asList(new Space(0, 110, 50))));
                page.getLeading();
                returns(0);
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 50));
                page.getHeightWithoutMargins();
                returns(50, 0);
                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<>(Arrays.asList(new Space(0, 110))));
                page.getWidthWithoutMargins();
                returns(100);
            }
        };

        StateTable table = new BaseStateTable(100);
        table.columns(1);
        table.addCell("jantje");
        table.addCell("jantje");
        table.addCell("jantje");
        table.addCell("jantje");
        table.addCell("jantje");
        table.addCell("jantje");
        table.addCell("jantje");

        StateTable overflow = table.processContentSize(page, false, true, false);
        Assert.assertNotNull(overflow);
        Assert.assertEquals(2, table.getContent().size());
        Assert.assertEquals(5, overflow.getContent().size());

        table = new BaseStateTable(100);
        table.columns(1);
        table.addCell("header");
        table.addCell("jantje");
        table.addCell("jantje");

        table.repeatHeader(true);
        overflow = table.processContentSize(page, false, true, false);
        Assert.assertNotNull(overflow);
        List<Cell> content = table.getContent();
        List<Cell> overflowContent = overflow.getContent();
        Assert.assertEquals(2, content.size());
        //header should've been set but not yet added to the overflow content
        Assert.assertEquals(1, overflowContent.size());
        Assert.assertEquals(content.get(0).getWidth(), overflowContent.get(0).getWidth(), 0.01);

        //after processing the overflow table the header should've been added, resulting in a content size of 2
        overflow.processContentSize(page, false, true, false);
        Assert.assertEquals(2, overflow.getContent().size());
    }

    @Test
    public void testTooLargeContentRemoval(@Mocked final StatePage page) {
        new NonStrictExpectations() {
            {
                page.getOpenSpacesIncludingHeight(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<Space>(), new ArrayList<>(Arrays.asList(new Space(0, 500, 500))));
                page.getLeading();
                returns(0);
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 0));
                page.getHeightWithoutMargins();
                returns(500, 0);
                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<>(Arrays.asList(new Space(0, 500))));
                page.getWidthWithoutMargins();
                returns(100);
            }
        };

        InputStream input = this.getClass().getClassLoader().getResourceAsStream("hammock.jpg");
        Image i = new BaseImage(input, ImageType.JPEG).align(Alignment.CENTERED).width(120);

        table.columns(2);
        table.addCell("test");
        table.addCell(new BaseCell(i));

        table.processContentSize(page, false, false, true, false, false);
        Assert.assertTrue(table.getContent().get(1).getContent() == null);
        Assert.assertFalse(table.getContent().get(0).getContent().equals(null));
    }

    @Test
    public void testAlignment(@Mocked final StatePage page) {
        new NonStrictExpectations() {
            {
                page.getOpenSpacesIncludingHeight(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<Space>(), Arrays.asList(new Space(0, 10, 10), new Space(10, 300, 400)));
                page.getLeading();
                returns(10);
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(100, 100));
                page.getHeightWithoutMargins();
                returns(400, 0);
                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<>(Arrays.asList(new Space(0, 200), new Space(250, 500))));
            }
        };
        table.width(50);
        table.on(100, 100);
        table.align(Alignment.LEFT);
        table.processContentSize(page, false, true, true, false, true);
        assertEquals(100, table.getPosition().getX(), FloatEqualityTester.EPSILON);
        table.align(Alignment.RIGHT);
        table.processContentSize(page, false, true, true, false, true);
        assertEquals(350, table.getPosition().getX(), FloatEqualityTester.EPSILON);
        table.on(100, 100);
        table.align(Alignment.CENTERED);
        table.processContentSize(page, false, true, true, false, true);
        assertEquals(225, table.getPosition().getX(), FloatEqualityTester.EPSILON);
        table.on(100, 100);
        table.align(Alignment.JUSTIFIED);
        table.processContentSize(page, false, true, true, false, true);
        assertEquals(100, table.getPosition().getX(), FloatEqualityTester.EPSILON);
    }
}
