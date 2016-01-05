package org.toucanpdf.state.Table;

import java.util.List;

import org.toucanpdf.model.state.StateCell;

public class StateTableCellPlacer {
    private List<StateCell> content;
    private int columnAmount;
    private StateTableColumnUsageDetector columnUsageDetector;

    private int currentColumn;
    private int currentRow;

    public StateTableCellPlacer(List<StateCell> content, int columnAmount) {
        this.content = content;
        this.columnAmount = columnAmount;
        this.columnUsageDetector = new StateTableColumnUsageDetector(columnAmount);
    }

    public List<StateTableRow> divideColumnsOverRows(List<StateTableRow> rows) {
        currentColumn = 0;
        double widthUsedInRow = 0;
        addRowTo(rows);
        currentRow = rows.size() - 1;

        for (StateCell c : content) {
            //check if current cell columns fit on this row
            boolean validSpaceFound = false;
            while (!validSpaceFound) {
                boolean contentFits = contentFitsOnThisRow(currentColumn, c.getColumnSpan());
                if (contentFits && columnUsageDetector.columnsNotInUse(currentColumn, c.getColumnSpan(), rows, currentRow)) {
                    validSpaceFound = true;
                    rows.get(currentRow).getContent()[currentColumn] = c;
                    widthUsedInRow += c.getRequiredWidth();
                    currentColumn += c.getColumnSpan();
                    addCellToRow(rows, currentRow, c);
                } else if (contentFits) {
                    //if content does fit but the column is in use, try again on next column
                    currentColumn++;
                } else {
                    //else move on to next row
                    if (currentRow + 2 > rows.size()) {
                        addRowTo(rows);
                    }
                    rows.get(currentRow).setWidthUsed(widthUsedInRow);
                    setIndicesToNextRow();
                }
            }
        }
        return rows;
    }

    private void setIndicesToNextRow() {
        currentRow++;
        currentColumn = 0;
    }

    private void addCellToRow(List<StateTableRow> rows, int currentRow, StateCell cell) {
        //if the cell in question has no custom width specified
        if (cell.getStateCellContent() != null && cell.getStateCellContent().getSpecifiedWidth() == 0) {
            rows.get(currentRow).addNoWidthSpecifiedCell(cell);
        }

        if (cell.getRowSpan() > 1) {
            addRowsTo(rows, cell.getRowSpan() - 1);
        }
    }

    private boolean contentFitsOnThisRow(int currentColumn, int columnSpan) {
        return (currentColumn + columnSpan <= this.columnAmount);
    }

    private StateTableRow addRowTo(List<StateTableRow> rows) {
        this.addRowsTo(rows, 1);
        return rows.get(rows.size() - 1);
    }

    private void addRowsTo(List<StateTableRow> rows, int count) {
        for (int i = 0; i < count; ++i) {
            rows.add(new StateTableRow(this.columnAmount));
        }
    }
}
