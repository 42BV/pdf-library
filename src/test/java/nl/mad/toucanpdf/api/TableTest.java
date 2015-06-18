package nl.mad.toucanpdf.api;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Compression;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

public class TableTest {
    private Table table;

    @Before
    public void setUp() {
        table = new BaseTable(100);
    }

    @Test
    public void testWidth() {
        assertEquals(100, table.getWidth(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testCopy() {
        table.align(Alignment.RIGHT).border(3.0).columns(9).drawFillerCells(false).allowWrapping(true);
        table.width(100).on(100, 110).compress(Compression.ASCII_85);
        table.marginBottom(1).marginLeft(2).marginRight(3).marginTop(4);

        Table t2 = (Table) table.copy();
        assertEquals(Alignment.RIGHT, t2.getAlignment());
        assertEquals(3.0, t2.getBorderWidth(), FloatEqualityTester.EPSILON);
        assertEquals(9, t2.getColumnAmount());
        assertEquals(false, t2.getDrawFiller());
        assertEquals(true, t2.isWrappingAllowed());
        assertEquals(100, t2.getWidth(), FloatEqualityTester.EPSILON);
        assertEquals(new Position(100, 110), t2.getPosition());
        assertEquals(Compression.ASCII_85, t2.getCompressionMethod());
        assertEquals(1, t2.getMarginBottom());
        assertEquals(2, t2.getMarginLeft());
        assertEquals(3, t2.getMarginRight());
        assertEquals(4, t2.getMarginTop());
    }

    @Test
    public void testCellAdding() {
        table.addCell("Test");
        assertEquals(1, table.getContent().size());
        table.addCell(new BaseCell());
        assertEquals(2, table.getContent().size());
    }

    @Test
    public void testContentRemoval() {
        table.addCell("Test");
        table.removeContent();
        assertEquals(0, table.getContent().size());
    }

}
