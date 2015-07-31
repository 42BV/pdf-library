package nl.mad.toucanpdf.state.Table;

import java.util.List;

import nl.mad.toucanpdf.model.Cell;

/**
 * Created by dylan on 30-7-15.
 */
public class StateTableColumnUsageDetector {

    private int columnAmount;

    public StateTableColumnUsageDetector(int columnAmount) {
        this.columnAmount = columnAmount;
    }

    public boolean columnsNotInUse(int checkedColumn, int checkedColumnSpan, List<StateTableRow> rows, int checkedRow) {
        //we need to go through all rows and columns to make sure the requested columns are not occupied due to rowspawn/columnspan of other cells
        for (int rowNumber = 0; rowNumber <= checkedRow; ++rowNumber) {
            Cell[] row = rows.get(rowNumber).getContent();
            for (int column = 0; column < this.columnAmount; ++column) {
                Cell cell = row[column];
                if (cell != null &&
                        (rowNumber == checkedRow && cellOccupiesCheckedColumn(checkedColumn, column, cell.getColumnSpan()) ||
                        (column <= checkedColumn && cellOccupiesCheckedCell(checkedColumn, checkedColumnSpan, checkedRow, rowNumber, column, cell)))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean cellOccupiesCheckedCell(int checkedColumn, int checkedColumnSpan, int checkedRow, int rowNumber, int column, Cell cell) {
        return cellOccupiesCheckedColumns(column, cell.getColumnSpan(), checkedColumn, checkedColumnSpan) &&
                cellOccupiesCheckedRows(checkedRow, rowNumber, cell.getRowSpan());
    }

    private boolean cellOccupiesCheckedRows(int checkedRow, int rowNumber, int rowSpan) {
        return (rowNumber + rowSpan - 1) >= checkedRow;
    }

    private boolean cellOccupiesCheckedColumn(int checkedColumn, int column, int columnSpan) {
        return (column + columnSpan - 1) >= checkedColumn;
    }

    private boolean cellOccupiesCheckedColumns(int cellColumn, int cellColumnSpan, int checkedColumn, int checkedColumnSpan) {
        for (int i = cellColumn; i <= (cellColumn + cellColumnSpan - 1); ++i) {
            if (i >= checkedColumn && i <= (checkedColumn + checkedColumnSpan - 1)) {
                return true;
            }
        }
        return false;
    }
}
