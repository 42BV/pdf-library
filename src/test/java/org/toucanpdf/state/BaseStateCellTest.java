package org.toucanpdf.state;

import static org.junit.Assert.assertEquals;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.toucanpdf.api.BaseCell;
import org.toucanpdf.api.BaseImage;
import org.toucanpdf.api.BaseParagraph;
import org.toucanpdf.api.BaseText;
import org.toucanpdf.model.DocumentPartType;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.state.StateCellContent;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class BaseStateCellTest {
    private BaseStateCell cell;

    @Before
    public void setUp() {
        cell = new BaseStateCell();
    }

    @Test
    public void testCopy() {
        BaseStateCell cell2 = new BaseStateCell(new BaseCell(new BaseText("Test")));
        assertEquals("Test", ((BaseStateCellText) cell2.getStateCellContent()).getText());
        cell2 = new BaseStateCell(new BaseCell(new BaseParagraph()));
        assertEquals(null, cell2.getContent());
        cell2 = new BaseStateCell(new BaseCell(new BaseImage()));
        assertEquals(DocumentPartType.IMAGE, cell2.getContent().getType());
    }

    @Test
    public void getContentSize(@Mocked final StateCellContent content) {
        new NonStrictExpectations() {
            {
                content.calculateContentHeight(anyDouble, anyDouble, null, anyBoolean);
                returns(100.0);
                content.getRequiredWidth();
                returns(50.0);
            }
        };
        cell.border(1);
        cell.padding(2);
        cell.processContentSize(0);
        cell.setPosition(new Position());
        //5 due to padding (times 2) and border
        assertEquals(5, cell.getRequiredHeight(0), FloatEqualityTester.EPSILON);
        assertEquals(0, cell.getRequiredWidth(), FloatEqualityTester.EPSILON);
        cell.setContent(content);
        assertEquals(105, cell.getRequiredHeight(0), FloatEqualityTester.EPSILON);
        assertEquals(54, cell.getRequiredWidth(), FloatEqualityTester.EPSILON);
        cell.height(300);
        cell.width(200);
        assertEquals(300, cell.getRequiredHeight(0), FloatEqualityTester.EPSILON);
        assertEquals(204, cell.getRequiredWidth(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testContent() {
        cell.content(new BaseText());
        assertEquals(null, cell.getContent());
        cell.content(new BaseStateCellText(""));
        assertEquals(DocumentPartType.TEXT, cell.getContent().getType());
    }
}
