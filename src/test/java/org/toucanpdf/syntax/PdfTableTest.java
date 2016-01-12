package org.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;

import org.toucanpdf.model.Position;
import org.toucanpdf.model.state.StateCell;
import org.toucanpdf.model.state.StateTable;
import org.toucanpdf.pdf.syntax.PdfTable;
import org.toucanpdf.state.BaseStateCell;
import org.toucanpdf.state.Table.BaseStateTable;
import org.toucanpdf.utility.ByteEncoder;

import org.junit.Test;

public class PdfTableTest {
    @Test
    public void testDrawing() {

        StateTable table = new BaseStateTable(500);
        table.width(110).border(0).on(new Position(100, 100));

        StateCell cell = new BaseStateCell();
        cell.setPosition(new Position(110, 110)).width(11).height(10);
        StateCell cell2 = new BaseStateCell();
        cell2.setPosition(new Position(120, 120)).width(12).height(11);

        table.addCell(cell);
        table.addCell(cell2);

        //border 0, meaning there is nothing to be drawn
        PdfTable pTable = new PdfTable(table);
        assertEquals("", ByteEncoder.getString(pTable.getByteRepresentation()));

        //border of 1
        cell.border(1);
        cell2.border(1);

        String expectedResult = "1.0 w 110.0 100.0 11.0 10.0 re  S\n" + "1.0 w 120.0 109.0 12.0 11.0 re  S\n";
        pTable = new PdfTable(table);
        assertEquals(expectedResult, ByteEncoder.getString(pTable.getByteRepresentation()));

        //invalid border size
        cell.border(-1);
        cell2.border(-1);
        pTable = new PdfTable(table);
        assertEquals("", ByteEncoder.getString(pTable.getByteRepresentation()));
    }
}
