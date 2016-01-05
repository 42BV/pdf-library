package org.toucanpdf.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.toucanpdf.api.BaseAnchor;
import org.toucanpdf.api.BaseImage;
import org.toucanpdf.api.BaseParagraph;
import org.toucanpdf.api.BaseTable;
import org.toucanpdf.api.BaseText;
import org.toucanpdf.model.Alignment;
import org.toucanpdf.model.Anchor;
import org.toucanpdf.model.Page;
import org.toucanpdf.model.Paragraph;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Space;
import org.toucanpdf.model.Text;
import org.toucanpdf.model.state.StatePage;
import org.toucanpdf.model.state.StateParagraph;
import org.toucanpdf.model.state.StateText;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class BaseStateParagraphTest {
    private BaseStateParagraph paragraph;

    @Before
    public void setup() {
        paragraph = new BaseStateParagraph();
    }

    @Test
    public void testCopyConstructor() {
        Paragraph p = new BaseParagraph();
        Text t = new BaseText("Test");
        p.addText(t);
        p.addAnchor(new BaseAnchor(new BaseImage()).above(t));
        p.addAnchor(new BaseAnchor(new BaseTable(100)).beneath(t));
        Paragraph p2 = new BaseStateParagraph(p, false);
        assertEquals(p.getAlignment(), p2.getAlignment());
        assertEquals(p.getMarginBottom(), p2.getMarginBottom());
        assertEquals(p.getMarginLeft(), p2.getMarginLeft());
        assertEquals(p.getMarginRight(), p2.getMarginRight());
        assertEquals(p.getMarginBottom(), p2.getMarginBottom());
        p2 = new BaseStateParagraph(p, true);
        assertEquals(1, p2.getTextCollection().size());
        assertEquals(2, p2.getAnchors().size());
    }

    @Test
    public void testAdd() {
        paragraph.addText(new BaseStateText("Test1"));
        paragraph.addText(new BaseStateText("Test2"));
        assertEquals(2, paragraph.getTextCollection().size());
    }

    @Test
    public void testPositioning(@Mocked final StatePage page) {
        new NonStrictExpectations() {
            {
                page.getWidthWithoutMargins();
                returns(400);
                page.getHeightWithoutMargins();
                returns(500);
                page.getOpenSpacesOn(null, anyBoolean, anyDouble, anyDouble, null);
                returns(new ArrayList<Space>(Arrays.asList(new Space(0, 400))));
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(0, 500), new Position(0, 450), new Position(0, 500));
            }
        };

        new MockUp<BaseStateText>() {

            @Mock
            public StateText processContentSize(Invocation inv, StatePage page, double posX, boolean fixedPosition) {
                if (inv.getInvocationIndex() != 2) {
                    return null;
                } else {
                    return new BaseStateText("overflow");
                }
            }

            @Mock
            public double getContentHeight(Page page) {
                return 100;
            }
        };

        new MockUp<BaseStateImage>() {
            @Mock
            public boolean processContentSize(StatePage page, boolean wrapping, boolean alignment, boolean fixed) {
                return false;
            }
        };

        paragraph.marginLeft(10);
        paragraph.marginTop(20);
        paragraph.marginBottom(30);
        paragraph.on(0, 100);
        paragraph.addText(new BaseStateText("Test1"));
        paragraph.addText(new BaseStateText("Test"));
        paragraph.processContentSize(page, false);
        assertEquals(10, paragraph.getTextCollection().get(0).getMarginLeft());
        assertEquals(20, paragraph.getTextCollection().get(0).getMarginTop());
        assertEquals(0, paragraph.getTextCollection().get(0).getMarginBottom());
        assertEquals(30, paragraph.getTextCollection().get(1).getMarginBottom());
        Position expected = new Position(0, 500);
        for (Text text : paragraph.getTextCollection()) {
            assertEquals(expected, text.getPosition());
            expected.adjustY(-50);
        }

        StateParagraph p2 = new BaseStateParagraph(paragraph, true);
        Paragraph overflow = p2.processContentSize(page, false);
        assertEquals(1, p2.getTextCollection().size());
        assertTrue(overflow != null);
        assertEquals(2, overflow.getTextCollection().size());
        Anchor a = new BaseAnchor(new BaseStateImage(new BaseImage()).height(100).width(100)).above(paragraph.getTextCollection().get(0));
        paragraph.addAnchor(a);
        Anchor a2 = new BaseAnchor(new BaseStateImage(new BaseImage()).height(100).width(100)).leftOf(paragraph.getTextCollection().get(0));
        paragraph.addAnchor(a2);
        Anchor a3 = new BaseAnchor(new BaseStateImage(new BaseImage()).height(100).width(100)).beneath(paragraph.getTextCollection().get(0));
        paragraph.addAnchor(a3);
        Anchor a4 = new BaseAnchor(new BaseStateImage(new BaseImage()).height(100).width(100)).rightOf(paragraph.getTextCollection().get(0));
        paragraph.addAnchor(a4);
        paragraph.processContentSize(page, false);
        assertEquals(new Position(0, 500), a.getPart().getPosition());
        assertEquals(new Position(0, 351), a2.getPart().getPosition());
        assertEquals(new Position(100, 342.804), paragraph.getTextCollection().get(0).getPosition());
        assertEquals(242.804, a3.getPart().getPosition().getY(), FloatEqualityTester.EPSILON);
        assertEquals(new Position(300, 351), a4.getPart().getPosition());
    }

    @Test
    public void testGetUsedSpaces() {
        new MockUp<BaseStateText>() {
            @Mock
            public List<Space> getUsedSpaces(double height, int pageWidth) {
                return Arrays.asList(new Space(0, 100));
            }
        };
        paragraph.addText(new BaseStateText("Test"));
        paragraph.addText(new BaseStateText("Test2"));
        List<Space> usedSpaces = paragraph.getUsedSpaces(100, 900);
        assertEquals(2, usedSpaces.size());
        assertEquals(0, usedSpaces.get(0).getStartPoint());
        assertEquals(0, usedSpaces.get(1).getStartPoint());
        assertEquals(100, usedSpaces.get(0).getEndPoint());
        assertEquals(100, usedSpaces.get(1).getEndPoint());
    }

    @Test
    public void testGetContentSize(@Mocked final BaseStateText text) {
        new NonStrictExpectations() {
            {
                text.getContentHeight(null);
                returns(10.0, 12.0);
                text.getContentWidth(null, null);
                returns(100.0, 90.0);
            }
        };
        paragraph.addText(text);
        paragraph.addText(text);
        assertEquals(22, paragraph.getContentHeight(null), FloatEqualityTester.EPSILON);
        assertEquals(100, paragraph.getContentWidth(null, null), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testGetPositionAt(@Mocked final BaseStateText text) {
        new NonStrictExpectations() {
            {
                text.getPositionAt(anyDouble);
                returns(new int[] { 12, 14 }, new int[] { 1 });
            }
        };
        paragraph.addText(text);
        paragraph.addText(text);
        int[] positions = paragraph.getPositionAt(100);
        assertEquals(12, positions[0]);
        assertEquals(14, positions[1]);
        assertEquals(1, positions[2]);
    }

    @Test
    public void getRequiredSpaces(@Mocked final BaseStateText text) {
        new NonStrictExpectations() {
            {
                text.getRequiredSpaceAbove();
                returns(10.0);
                text.getRequiredSpaceBelow();
                returns(11.0);
            }
        };
        paragraph.marginLeft(15);
        paragraph.marginRight(20);
        assertEquals(0, paragraph.getRequiredSpaceAbove(), FloatEqualityTester.EPSILON);
        assertEquals(0, paragraph.getRequiredSpaceBelow(), FloatEqualityTester.EPSILON);
        assertEquals(15, paragraph.getRequiredSpaceLeft(), FloatEqualityTester.EPSILON);
        assertEquals(20, paragraph.getRequiredSpaceRight(), FloatEqualityTester.EPSILON);
        paragraph.addText(text);
        assertEquals(10, paragraph.getRequiredSpaceAbove(), FloatEqualityTester.EPSILON);
        paragraph.addAnchor(new BaseAnchor(new BaseStateImage(new BaseImage()).marginTop(20)).above(paragraph.getTextCollection().get(0)));
        assertEquals(20, paragraph.getRequiredSpaceAbove(), FloatEqualityTester.EPSILON);
        assertEquals(11, paragraph.getRequiredSpaceBelow(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testCopy() {
        paragraph.align(Alignment.RIGHT);
        paragraph.marginBottom(10).marginLeft(17).marginRight(1).marginTop(33);
        Paragraph p = (Paragraph) paragraph.copy();
        assertEquals(p.getAlignment(), paragraph.getAlignment());
        assertEquals(p.getMarginBottom(), paragraph.getMarginBottom());
        assertEquals(p.getMarginLeft(), paragraph.getMarginLeft());
        assertEquals(p.getMarginRight(), paragraph.getMarginRight());
        assertEquals(p.getMarginBottom(), paragraph.getMarginBottom());
    }

    @Test
    public void testStateTextRetrieval() {
        paragraph.addText(new BaseStateText("Test"));
        assertEquals(1, paragraph.getStateTextCollection().size());
    }

    @Test
    public void testOriginalObject() {
        Paragraph p = new BaseParagraph();
        paragraph.setOriginalObject(p);
        assertEquals(p, paragraph.getOriginalObject());
        paragraph.setOriginalObject(null);
        assertEquals(p, paragraph.getOriginalObject());
    }
}
