package nl.mad.toucanpdf.state;

import static org.junit.Assert.assertEquals;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StateSpacing;
import nl.mad.toucanpdf.model.state.StateText;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class BaseStatePageTest {
	private StatePage page;
	
	@Before
	public void setUp() {
		page = new BaseStatePage(100, 100);
	}
	
	@Test
	public void testCopy() {
		page.marginTop(20);
		StatePage p = new BaseStatePage(page);
		assertEquals(20, p.getMarginTop());
		assertEquals(20, p.getFilledHeight(), FloatEqualityTester.EPSILON);		
	}
	
	@Test
	public void testOpenPositionRetrieval(@Mocked final BaseStateText text) {
		new NonStrictExpectations() { 
			{
				text.getRequiredSpaceLeft();
				returns(2.0);
				text.getRequiredSpaceRight();
				returns(3.0);				
			}
		};
		page = new BaseStatePage(20, 20);
		Position pos = page.getOpenPosition(10.0, 5.0, text);
		assertEquals(new Position(10, 10), pos);
		
	}
	
}
