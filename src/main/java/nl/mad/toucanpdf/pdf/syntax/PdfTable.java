package nl.mad.toucanpdf.pdf.syntax;

import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.state.StateCell;
import nl.mad.toucanpdf.model.state.StateTable;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

/**
 * This class represents a table in the PDF syntax.
 * In order to draw the table this class should be added to a PdfStream.
 * @author Dylan de Wolff
 *
 */
public class PdfTable extends PdfPath {
    private StateTable table;

    /**
     * Creates a new instance of PdfTable. 
     * @param table The table to represent.
     */
    public PdfTable(StateTable table) {
        super(PdfObjectType.TABLE);
        this.table = table;
        drawTableBorder();
        drawCells();
    }

    private void drawTableBorder() {
        if (FloatEqualityTester.greaterThan(table.getBorderWidth(), 0)) {
            Position pos = table.getPosition();
            this.setLineWidth(table.getBorderWidth());
            drawRectangle(pos.getX(), pos.getY() - table.getHeight(), table.getWidth(), table.getHeight());
            System.out.println("Table: " + pos.getX() + ", " + pos.getY() + ", " + table.getWidth() + ", " + table.getHeight());
            System.out.println("    " + (pos.getY() - table.getHeight()));
            this.strokePath();
        }
    }

    private void drawCells() {
        System.out.println(table);
        System.out.println(table.getContent());
        if (FloatEqualityTester.greaterThan(table.getBorderWidth(), 0)) {
            for (StateCell c : table.getStateCellCollection()) {
                Position pos = c.getPosition();
                drawRectangle(pos.getX(), pos.getY() - c.getHeight(), c.getWidth(), c.getHeight());
                System.out.println("Column: " + pos.getX() + " , " + pos.getY() + ", " + c.getWidth() + ", " + c.getHeight());
                System.out.println("    " + (pos.getY() - c.getHeight()));
                System.out.println("    " + c.getContent());
                this.strokePath();
            }
        }
    }
}
