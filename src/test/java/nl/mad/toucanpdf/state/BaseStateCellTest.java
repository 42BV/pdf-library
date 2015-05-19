package nl.mad.toucanpdf.state;

import static org.junit.Assert.assertEquals;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.toucanpdf.api.BaseCell;
import nl.mad.toucanpdf.api.BaseImage;
import nl.mad.toucanpdf.api.BaseParagraph;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.state.StateCellContent;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

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
        cell.processContentSize(0, 0);
        cell.setPosition(new Position());
        assertEquals(0, cell.getRequiredHeight(0, 0), FloatEqualityTester.EPSILON);
        assertEquals(0, cell.getRequiredWidth(), FloatEqualityTester.EPSILON);
        cell.setContent(content);
        assertEquals(100, cell.getRequiredHeight(0, 0), FloatEqualityTester.EPSILON);
        assertEquals(50, cell.getRequiredWidth(), FloatEqualityTester.EPSILON);
        cell.height(300);
        cell.width(200);
        assertEquals(300, cell.getRequiredHeight(0, 0), FloatEqualityTester.EPSILON);
        assertEquals(200, cell.getRequiredWidth(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testContent() {
        cell.content(new BaseText());
        assertEquals(null, cell.getContent());
        cell.content(new BaseStateCellText(""));
        assertEquals(DocumentPartType.TEXT, cell.getContent().getType());
    }
}
