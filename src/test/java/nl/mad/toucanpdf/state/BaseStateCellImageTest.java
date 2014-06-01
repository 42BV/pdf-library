package nl.mad.toucanpdf.state;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import nl.mad.toucanpdf.api.BaseImage;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

public class BaseStateCellImageTest {
	private BaseStateCellImage image;
	private BaseImage origImage;
	
	@Before
	public void setUp() {
		origImage = new BaseImage(new byte[0], ImageType.JPEG);
		image = new BaseStateCellImage(origImage);
	}
	
	@Test
	public void testOriginalObject() {
		image.setOriginalObject(origImage);
		assertEquals(origImage, image.getOriginalObject());
		image.setOriginalObject(null);
		assertEquals(origImage, image.getOriginalObject());
	}
	
	@Test
	public void testHeightCalculation() {
		image.calculateContentHeight(300, 10, new Position(0, 200), false);
		assertEquals(new Position(-1, -1), image.getPosition());
		image.calculateContentHeight(300, 10, new Position(0, 200), true);
		assertEquals(new Position(0, 200), image.getPosition());
		image.marginTop(30);
		image.marginLeft(20);
		image.calculateContentHeight(300, 10, new Position(0, 200), true);
		assertEquals(new Position(20, 170), image.getPosition());
	}
	
	@Test
	public void getRequiredSpaces() {
		image.marginLeft(10);
		image.marginRight(20);
		image.width(100);
		assertEquals(10, image.getRequiredSpaceLeft(), FloatEqualityTester.EPSILON);
		assertEquals(20, image.getRequiredSpaceRight(), FloatEqualityTester.EPSILON);
		assertEquals(130, image.getRequiredWidth(), FloatEqualityTester.EPSILON);
	}
}
