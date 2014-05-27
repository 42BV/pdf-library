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
        drawCells();
    }

    private void drawCells() {
        if (FloatEqualityTester.greaterThan(table.getBorderWidth(), 0)) {
            for (StateCell c : table.getStateCellCollection()) {
                this.setLineWidth(table.getBorderWidth());
                Position pos = c.getPosition();
                System.out.println("Printing cell on : " + pos);
                System.out.println("    height: " + c.getHeight());
                System.out.println("    width: " + c.getWidth());
                drawRectangle(pos.getX(), pos.getY() - c.getHeight(), c.getWidth(), c.getHeight());
                this.strokePath();
            }
        }
    }
}
