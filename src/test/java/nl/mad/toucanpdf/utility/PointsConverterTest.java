package nl.mad.toucanpdf.utility;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PointsConverterTest {

	@Test
	public void testConversion() {
		assertEquals(13.5, PointsConverter.getPointsForPixels(18.0), FloatEqualityTester.EPSILON);
		assertEquals(12, PointsConverter.getPixelsForPoints(9), FloatEqualityTester.EPSILON);
	}
}
