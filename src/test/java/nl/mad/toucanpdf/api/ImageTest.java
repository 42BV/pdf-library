package nl.mad.toucanpdf.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;


import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Compression;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.junit.Before;
import org.junit.Test;

public class ImageTest {
	private Image i;
	
	@Before
	public void setup() {

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("hammock.jpg");
		i = new BaseImage(input, ImageType.JPEG);	}

	
	@Test
	public void testConstructors(){
		assertTrue(i.getImageParser() != null);
		assertEquals(8, i.getImageParser().getBitsPerComponent());
		assertEquals(127.5, i.getHeight(), FloatEqualityTester.EPSILON);
	}
	
	@Test
	public void testSize() {
		assertEquals(127.5, i.getHeight(), FloatEqualityTester.EPSILON);
		assertEquals(127.5, i.getWidth(), FloatEqualityTester.EPSILON);
		assertEquals(100, i.height(100, true).getWidth(), FloatEqualityTester.EPSILON);
		assertEquals(110, i.width(110, true).getHeight(), FloatEqualityTester.EPSILON);
		i.width(120);
		assertEquals(120, i.getHeight(), FloatEqualityTester.EPSILON);
		assertEquals(120, i.getWidth(), FloatEqualityTester.EPSILON);
		i.height(130);
		assertEquals(130, i.getHeight(), FloatEqualityTester.EPSILON);
		assertEquals(130, i.getWidth(), FloatEqualityTester.EPSILON);
		i.width(110, false);
		assertEquals(130, i.getHeight(), FloatEqualityTester.EPSILON);
		assertEquals(110, i.getWidth(), FloatEqualityTester.EPSILON);
	}
	
	@Test
	public void testGetTypeFromFilename() {
		assertEquals(ImageType.JPEG, BaseImage.getTypeFromFilename(".jpg"));
		assertEquals(ImageType.BMP, BaseImage.getTypeFromFilename(".bmp"));
		assertEquals(ImageType.GIF, BaseImage.getTypeFromFilename(".gif"));
		assertEquals(ImageType.PNG, BaseImage.getTypeFromFilename(".png"));
	}
	
	@Test
	public void testMargins() {
		Image i = new BaseImage(new byte[0], ImageType.JPEG);
		i.marginBottom(1);
		assertEquals(1, i.getMarginBottom());
		i.marginTop(2);
		assertEquals(2, i.getMarginTop());
		i.marginLeft(3);
		assertEquals(3, i.getMarginLeft());
		i.marginRight(4);
		assertEquals(4, i.getMarginRight());
	}
	
	@Test
	public void testSettersGetters() {
		Image i = new BaseImage(new byte[0], ImageType.JPEG);
		i.allowWrapping(true);
		assertEquals(true, i.wrappingAllowed());
		i.compress(Compression.FLATE);
		assertEquals(Compression.FLATE, i.getCompressionMethod());
		i.on(100, 100);
		assertEquals(new Position(100, 100), i.getPosition());
		i.align(Alignment.CENTERED);
		assertEquals(Alignment.CENTERED, i.getAlignment());
	}
	
	@Test
	public void testCopy() {
		Image b = new BaseImage(i);
		assertTrue(b.getImageParser() != null);
		assertEquals(b.getWidth(), i.getWidth(), FloatEqualityTester.EPSILON);
		assertEquals(b.getCompressionMethod(), i.getCompressionMethod());
		b = (Image) i.copy();
		assertTrue(b.getImageParser() != null);
		assertEquals(b.getWidth(), i.getWidth(), FloatEqualityTester.EPSILON);
		assertEquals(b.getCompressionMethod(), i.getCompressionMethod());
	}
	
	
	
}
