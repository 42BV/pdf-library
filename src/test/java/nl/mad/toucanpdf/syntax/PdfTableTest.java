package nl.mad.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.state.StateCell;
import nl.mad.toucanpdf.model.state.StateTable;
import nl.mad.toucanpdf.pdf.syntax.PdfObjectType;
import nl.mad.toucanpdf.pdf.syntax.PdfTable;
import nl.mad.toucanpdf.state.BaseStateCell;
import nl.mad.toucanpdf.utility.ByteEncoder;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

public class PdfTableTest {
    @Test
    public void testDrawing(@Mocked final StateTable table, @Mocked final StateCell c1) {
        new NonStrictExpectations() {
            {
                table.getBorderWidth();
                returns(0.0, 0.0, 1.0, 1.0, -1.0, -1.0);

                table.getPosition();
                returns(new Position(100, 100));

                table.getHeight();
                returns(100.0);

                table.getWidth();
                returns(110.0);

                table.getStateCellCollection();
                returns(new LinkedList<StateCell>(Arrays.asList(c1, c1)));

                c1.getPosition();
                returns(new Position(110, 110), new Position(120, 120), new Position(110, 110), new Position(120, 120));

                c1.getHeight();
                returns(10.0, 10.0, 11.0, 11.0, 10.0, 10.0, 11.0, 11.0);

                c1.getWidth();
                returns(11.0, 12.0, 11.0, 12.0);
            }
        };

        //border 0.0
        PdfTable pTable = new PdfTable(table);
        String expectedResult = "110.0 100.0 11.0 10.0 re  S\n" + "120.0 109.0 12.0 11.0 re  S\n";
        String expectedResult1 = "0.0 w " + expectedResult;
        assertEquals(expectedResult1, ByteEncoder.getString(pTable.getByteRepresentation()));

        //border of 1.0
        pTable = new PdfTable(table);
        String expectedResult2 = "1.0 w " + expectedResult;
        assertEquals(expectedResult2, ByteEncoder.getString(pTable.getByteRepresentation()));

        //invalid border size
        pTable = new PdfTable(table);
        assertEquals("", ByteEncoder.getString(pTable.getByteRepresentation()));
    }
}
