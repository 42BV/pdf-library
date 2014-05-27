package nl.mad.toucanpdf.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import nl.mad.toucanpdf.model.Anchor;
import nl.mad.toucanpdf.model.AnchorLocation;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Text;

public class AnchorTest {
	private Anchor a;
	private Image i;
	private Text t;
	
	@Before
	public void setUp() {
		i = new BaseImage(new byte[0], ImageType.JPEG);
		a = new BaseAnchor(i);
		t = new BaseText("Test");
		a.above(t);
	}
	
	@Test
	public void testPlacement() {
		assertEquals(AnchorLocation.ABOVE, a.getLocation());
		assertEquals(t, a.getAnchorPoint());
		a.beneath(t);
		assertEquals(AnchorLocation.BELOW, a.getLocation());
		assertEquals(t, a.getAnchorPoint());
		a.leftOf(t);
		assertEquals(AnchorLocation.LEFT, a.getLocation());
		assertEquals(t, a.getAnchorPoint());
		a.rightOf(t);
		assertEquals(AnchorLocation.RIGHT, a.getLocation());
		assertEquals(t, a.getAnchorPoint());
	}
	
	@Test
	public void testCopy() {
		Anchor b = new BaseAnchor(a, t);
		assertEquals(t, b.getAnchorPoint());
		assertEquals(AnchorLocation.ABOVE, b.getLocation());
		assertTrue(b.getPart() != null);		
	}
	
	@Test
	public void testAdjustPart() {
		a.part(new BaseImage(new byte[0], ImageType.BMP));
		assertTrue(a.getPart() != i);
	}
}
