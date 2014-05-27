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
        if (FloatEqualityTester.greaterThanOrEqualTo(table.getBorderWidth(), 0)) {
            this.setLineWidth(table.getBorderWidth());
            for (StateCell c : table.getStateCellCollection()) {
                Position pos = c.getPosition();
                drawRectangle(pos.getX(), pos.getY() - c.getHeight(), c.getWidth(), c.getHeight());
                this.strokePath();
            }
        }
    }
}
