package nl.mad.toucanpdf.utility;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FloatEqualityTesterTest {
	double a = 0.5;
	double b = 0.8;
	double c = 0.5;
	float fa = 0.4f;
	float fb = 0.6f;
	float fc = 0.4f;
	
	@Test
	public void testDoubleMethods() {
		//equals
		assertEquals(false, FloatEqualityTester.equals(a, b));
		assertEquals(true, FloatEqualityTester.equals(a, c));
		//greaterThan
		assertEquals(false, FloatEqualityTester.greaterThan(a, b));
		assertEquals(true, FloatEqualityTester.greaterThan(b, a));
		//lessThan
		assertEquals(false, FloatEqualityTester.lessThan(b, a));
		assertEquals(true, FloatEqualityTester.lessThan(a, b));		
	}
	
	@Test
	public void testFloatMethods() {
		//equals
		assertEquals(false, FloatEqualityTester.equals(fa, fb));
		assertEquals(true, FloatEqualityTester.equals(fa, fc));
		//greaterThan
		assertEquals(false, FloatEqualityTester.greaterThan(fa, fb));
		assertEquals(true, FloatEqualityTester.greaterThan(fb, fa));
		//lessThan
		assertEquals(false, FloatEqualityTester.lessThan(fb, fa));
		assertEquals(true, FloatEqualityTester.lessThan(fa, fb));		
	}
}
