package org.toucanpdf.pdf.syntax;

import org.toucanpdf.model.Position;
import org.toucanpdf.model.state.StateCell;
import org.toucanpdf.model.state.StateTable;
import org.toucanpdf.utility.FloatEqualityTester;

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
        for (StateCell c : table.getStateCellCollection()) {
            Double border = c.getBorderWidth();
            if (border != null && FloatEqualityTester.greaterThan(c.getBorderWidth(), 0)) {
                this.setLineWidth(c.getBorderWidth());
                Position pos = c.getPosition();
                drawRectangle(pos.getX(), pos.getY() - c.getHeight(), c.getWidth(), c.getHeight());
                this.strokePath();
            }
        }
    }
}
