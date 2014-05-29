package nl.mad.toucanpdf.state;

import static org.junit.Assert.assertEquals;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.toucanpdf.api.BaseAnchor;
import nl.mad.toucanpdf.api.BaseImage;
import nl.mad.toucanpdf.api.BaseParagraph;
import nl.mad.toucanpdf.api.BaseTable;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StateText;

import org.junit.Before;
import org.junit.Test;

public class BaseStateParagraphTest {
	private BaseStateParagraph paragraph;
	
	@Before
	public void setup() {
		paragraph = new BaseStateParagraph();
	}
	
	@Test
	public void testCopy() {
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
	public void testPositioning(@Mocked final StatePage page) {
//		new NonStrictExpectations() {
//			{
//				
//			}
//		};
//		paragraph.on(0, 100);
//		StateText text1 = new BaseStateText("Test1");
//		paragraph.addText(text1);
//		paragraph.processContentSize(page, false);
		//SOMEHOW MOCK ONLY PROCESSCONTENTSIZE FROM TEXT, not the rest
		
	}
}
