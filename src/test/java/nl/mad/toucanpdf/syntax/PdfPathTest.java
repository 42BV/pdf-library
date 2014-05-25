package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import nl.mad.toucanpdf.pdf.syntax.PdfPath;
import nl.mad.toucanpdf.utility.ByteEncoder;

public class PdfPathTest {
	private PdfPath path;
	
	@Before
	public void setupTest() {
		path = new PdfPath();
	}
	
	@Test
	public void testRectangleDrawing() {
		path.drawRectangle(9, 10, 11, 12);
		String expectedResult = "9.0 10.0 11.0 12.0 re ";
		assertEquals(expectedResult, ByteEncoder.getString(path.getByteRepresentation()));
	}
	
	@Test
	public void testLineDrawing() {
		path.moveTo(10, 11);
		String expectedResult = "10.0 11.0 m ";
		assertEquals(expectedResult, ByteEncoder.getString(path.getByteRepresentation()));	
		path.drawLine(10, 5);
		expectedResult += "10.0 5.0 l ";
		assertEquals(expectedResult, ByteEncoder.getString(path.getByteRepresentation()));	
	}
	
	@Test
	public void testPathDraw() {
		path.strokePath();
		String expectedResult = " S\n";
		assertEquals(expectedResult, ByteEncoder.getString(path.getByteRepresentation()));	
		path.fillPath();
		expectedResult += " f\n";
		assertEquals(expectedResult, ByteEncoder.getString(path.getByteRepresentation()));	
	}
	
	@Test
	public void testPathClosing() {
		path.closeSubpath();
		String expectedResult = " h ";
		assertEquals(expectedResult, ByteEncoder.getString(path.getByteRepresentation()));	
		path.closeSubpathAndStrokeLine();
		expectedResult += "s\n";
		assertEquals(expectedResult, ByteEncoder.getString(path.getByteRepresentation()));	
		
	}
}
