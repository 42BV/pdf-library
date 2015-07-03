package nl.mad.toucanpdf.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import nl.mad.toucanpdf.api.AbstractTable;
import nl.mad.toucanpdf.api.BaseCell;
import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.model.state.StateCell;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StateTable;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseStateTable extends AbstractTable implements StateTable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseStateTable.class);
    //columns that require a width less than the table width * this percentage will be prioritized during the dividing of width
    private static final double PRIORITY_COLUMN_WIDTH_PERCENTAGE = 0.2;
    private static final double MINIMUM_PAGE_HEIGHT_REQUIRED = 0.25;
    private DocumentPart originalObject;
    private List<StateCell> content = new LinkedList<StateCell>();
    private TableRow header = null;
    private List<TableRow> rows;

    //TODO: use these values to determine whether or not to repeat headers
    private boolean original = true;
    private double[] originalColumnWidths;

    /**
     * Creates a new instance of BaseStateTable.
     *
     * @param pageWidth Width of the page.
     */
    public BaseStateTable(int pageWidth) {
        super(pageWidth);
    }

    /**
     * Creates a new instance of BaseStateTable and copies the given table.
     * The content of the given table is not copied.
     *
     * @param table Table to copy from.
     */
    public BaseStateTable(Table table) {
        super(table);
        if (table instanceof StateTable) {
            copyStateTable((StateTable) table);
        }
    }

    private void copyStateTable(StateTable table) {
        this.original = table.isOriginal();
        this.originalColumnWidths = table.getOriginalWidths();
        TableRow tableHeader = table.getHeader();
        if (tableHeader != null && tableHeader.getContent() != null) {
            this.header = new TableRow(table.getHeader());
        }
    }

    /**
     * Creates a new instance of BaseStateTable and copies the given StateTable.
     * The content of the given table is not copied.
     *
     * @param table Table to copy from.
     * @param widths The widths of the columns of the table
     */
    public BaseStateTable(StateTable table, double[] widths) {
        super(table);
        this.originalColumnWidths = widths;
        this.original = false;
    }

    @Override
    public double getRequiredSpaceAbove() {
        return marginTop;
    }

    @Override
    public double getRequiredSpaceBelow() {
        return height + marginBottom;
    }

    @Override
    public void setOriginal(boolean original) {
        this.original = original;
    }

    @Override
    public boolean isOriginal() {
        return this.original;
    }

    @Override
    public double[] getOriginalWidths() {
        return this.originalColumnWidths;
    }

    @Override
    public TableRow getHeader() {
        return header;
    }

    @Override
    public void setHeader(TableRow header) {
        this.header = header;
    }

    @Override
    public StateTable processContentSize(StatePage page) {
        return this.processContentSize(page, this.isWrappingAllowed(), true, false);
    }

    @Override
    public StateTable processContentSize(StatePage page, boolean wrapping, boolean processAlignment, boolean fixed) {
        return this.processContentSize(page, wrapping, processAlignment, true, fixed, false);
    }

    /**
     * Processes the size of the table/cells and the positioning of said objects.
     *
     * @param page               Page the table will be on.
     * @param wrapping           Whether wrapping should be allowed.
     * @param processAlignment   Whether alignment should be processed.
     * @param processPositioning Whether positioning should be processed.
     * @param fixed              Whether the table has a fixed position.
     * @param ignoreOverflow     Whether the table should process overflow.
     * @return StateTable instance if there was overflow, null otherwise (always null if ignoreOverflow set to true).
     */
    public StateTable processContentSize(StatePage page, boolean wrapping, boolean processAlignment, boolean processPositioning, boolean fixed,
            boolean ignoreOverflow) {
        List<StateCell> tableContent = copyContent();
        rows = new LinkedList<>();
        addRowTo(rows);
        int availableHeight = 0;
        height = marginBottom + marginTop;

        this.determineCellBorders(tableContent);
        this.determineCellPadding(tableContent);

        //if not fixed, calculate a position for this table and process alignment if needed
        if (!fixed) {
            Position p = calculatePosition(page);
            if (p == null) {
                return this;
            }
            if (processAlignment && processPositioning) {
                p.adjustX(calculateAlignment(page));
            }
            this.setPosition(p);
            List<int[]> openSpaces = page.getOpenSpacesIncludingHeight(p, true, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this);
            availableHeight = openSpaces.get(0)[2];
        }

        divideColumnsOverRows(tableContent, rows);

        //finalize the cell widths, heights and execute positioning
        Position cellPos = new Position(this.getPosition());

        if (original) {
            this.originalColumnWidths = calculateColumnWidths(rows);
        }
        double[] widths = this.originalColumnWidths;

        //add cells to fill up empty columns in the table
        if (getDrawFiller()) {
            fillEmptyCells(rows, tableContent);
        }

        StateTable overflow = null;

        boolean overflowDetected = false;
        int index = 0;
        int overflowRow = rows.size();
        while (!overflowDetected && index < rows.size()) {
            TableRow row = rows.get(index);
            //apply the calculated widths to the columns
            applyColumnWidths(widths, row.getContent());

            //for height determination we first need to position each cell
            positionCellsForRow(row.getContent(), cellPos, widths);
            
            //determine height for row and apply it to each column in this row
            determineRowHeight(rows, rows.indexOf(row), page.getLeading());
            if (!fixed) {
                if (!ignoreOverflow) {
                    overflow = processOverflow(rows, index, availableHeight);
                    overflowDetected = (overflow != null);
                } else {
                    overflowDetected = isHeightCausingOverflow(rows.get(index), availableHeight);
                }
            }

            if (overflowDetected) {
                overflowRow = Math.min(rows.size(), index + 1);
            } else {
                applyColumnHeights(row);

                //adjust the cell position for the next row, meaning going down on the y axis and resetting the x axis
                double heightIncrease = row.getMaxHeight();
                this.height += heightIncrease;
                cellPos.adjustY(-heightIncrease);
                cellPos.setX(this.getPosition().getX());
                index++;
            }
        }
        //after all individual rows have been processed, increase the height of cells that occupy more than one row
        //TODO: Test that this is not going to cause issues in overflow detection (if second row of cell with rowspawn 2 causes the overflow)
        applyRowSpanHeights(rows);

        for (int i = 0; i < overflowRow; ++i) {
            TableRow row = rows.get(i);
            for (Cell c : row.getContent()) {
                if (c != null) {
                    ((StateCell) c).processContentSize(page.getLeading());
                }
            }
        }

        List<StateCell> cells = combineCellsFromRows(rows);
        if (processPositioning) {
            for(StateCell c : cells) {
                c.processVerticalAlignment();
            }
            this.content = cells;
            this.adjustFilledHeight(page);
        } else {
            this.height = Math.min((int) (MINIMUM_PAGE_HEIGHT_REQUIRED * page.getHeight()), this.height);
        }
        return overflow;
    }

    private void determineCellPadding(List<StateCell> tableContent) {
        for (StateCell stateCell : tableContent) {
            Double padding = stateCell.getPadding();
            if (padding == null) {
                stateCell.padding(this.padding);
            }
        }
    }

    private void determineCellBorders(List<StateCell> tableContent) {
        for (StateCell stateCell : tableContent) {
            Double border = stateCell.getBorderWidth();
            if (border == null) {
                stateCell.border(this.borderWidth);
            }
        }
    }

    private List<StateCell> combineCellsFromRows(List<TableRow> rows) {
        List<StateCell> cells = new LinkedList<>();
        for (TableRow row : rows) {
            Cell[] rowCells = row.getContent();
            for (int i = 0; i < columnAmount; ++i) {
                StateCell c = (StateCell) rowCells[i];
                if (c != null) {
                    cells.add(c);
                }
            }
        }
        return cells;
    }

    private StateTable processOverflow(List<TableRow> rows, int index, int availableHeight) {
        TableRow current = rows.get(index);
        StateTable overflow = null;
        if (isHeightCausingOverflow(current, availableHeight)) {
            overflow = new BaseStateTable(this, originalColumnWidths);
            overflow.setOriginal(false);
            overflow.setHeader(this.header);

            int rowSize = rows.size();
            for (int i = index; i < rowSize; ++i) {
                for (Cell cell : rows.get(i).getContent()) {
                    overflow.addCell(cell);
                }
            }

            for (int i = rowSize - 1; i >= index; --i) {
                for (Cell cell : rows.get(i).getContent()) {
                    this.content.remove(cell);
                }
                rows.remove(i);
            }
        }
        return overflow;
    }

    private boolean isHeightCausingOverflow(TableRow current, int availableHeight) {
        return (current.getMaxHeight() + this.height > availableHeight);
    }

    private void fillEmptyCells(List<TableRow> rows, List<StateCell> tableContent) {
        for (int currentRow = 0; currentRow < rows.size(); ++currentRow) {
            TableRow row = rows.get(currentRow);
            Cell[] content = row.getContent();

            for (int i = 0; i < this.columnAmount; ++i) {
                if (content[i] == null && !columnsInUse(i, 1, rows, currentRow)) {
                    StateCell c = new BaseStateCell();
                    c.border(this.borderWidth).padding(this.padding);
                    tableContent.add(c);
                    content[i] = c;
                }
            }
        }
    }

    private void applyRowSpanHeights(List<TableRow> rows) {
        //go through all rows and cells
        for (int rowIndex = 0; rowIndex < rows.size(); ++rowIndex) {
            TableRow row = rows.get(rowIndex);
            Cell[] cells = row.getContent();
            for (int cellIndex = 0; cellIndex < cells.length; ++cellIndex) {
                Cell c = cells[cellIndex];
                //if the cell occupies multiple rows
                if (c != null && c.getRowSpan() > 1) {
                    double extraHeight = 0;
                    //calculate the extra height we need to add to the column
                    for (int span = 1; span < c.getRowSpan(); ++span) {
                        extraHeight += rows.get(rowIndex + span).getMaxHeight();
                    }
                    c.height(c.getHeight() + extraHeight);
                }
            }
        }
    }

    private void applyColumnHeights(TableRow row) {
        for (Cell c : row.getContent()) {
            if (c != null) {
                StateCell cell = (StateCell) c;
                cell.height(row.getMaxHeight());
            }
        }
    }

    private void divideColumnsOverRows(List<StateCell> tableContent, List<TableRow> rows) {
        int currentRow = 0;
        int currentColumn = 0;
        this.height = 0;
        double widthUsedInRow = 0;

        if (header != null && (original || !original && this.isRepeatingHeader())) {
            rows.add(0, header);
            currentRow = 1;
            if (this.content.size() == 0) {
                rows.remove(1);
            }
        }

        for (StateCell c : tableContent) {
            //make sure the column span on each cell does not exceed the max column amount
            c.columnSpan(Math.min(c.getColumnSpan(), this.columnAmount));

            //check if current cell columns fit on this row
            boolean validSpaceFound = false;
            while (!validSpaceFound) {
                //if the current column + the column span of the cell will actually still fit on this row
                boolean validColumn = currentColumn + c.getColumnSpan() <= this.columnAmount;
                if (validColumn && !columnsInUse(currentColumn, c.getColumnSpan(), rows, currentRow)) {
                    //place the cell if a valid space has been found
                    validSpaceFound = true;
                    rows.get(currentRow).getContent()[currentColumn] = c;
                    widthUsedInRow += c.getRequiredWidth();
                    currentColumn += c.getColumnSpan();

                    //if the cell in question has no custom width specified
                    if (c.getStateCellContent() != null && c.getStateCellContent().getSpecifiedWidth() == 0) {
                        rows.get(currentRow).addNoWidthSpecifiedCell(c);
                    }

                    if (c.getRowSpan() > 1) {
                        addRowsTo(rows, c.getRowSpan() - 1);
                    }
                } else if (validColumn) {
                    //if the column is valid but the space was in use, increase the current column and try again
                    currentColumn++;
                } else {
                    //if the column is not valid, we've reached the end of the row and need to continue with the next one, sometimes this row might already exist
                    if (currentRow + 2 > rows.size()) {
                        addRowTo(rows);
                    }
                    rows.get(currentRow).setWidthUsed(widthUsedInRow);
                    currentRow++;
                    currentColumn = 0;
                }
            }
        }
        if (this.header == null && rows.size() > 0) {
            header = new TableRow(rows.get(0));
            Cell[] headerContent = this.header.getContent();
            for (int i = 0; i < headerContent.length; ++i) {
                if (headerContent[i] != null) {
                    tableContent.remove(0);
                    this.content.remove(0);
                }
            }
        }
    }

    private double[] calculateColumnWidths(List<TableRow> rows) {
        ColumnPossibleWidth[] minColumnWidths = new ColumnPossibleWidth[this.columnAmount];
        double[] columnWidths = new double[this.columnAmount];
        ColumnPossibleWidth[] maxColumnWidthsPossible = new ColumnPossibleWidth[this.columnAmount];
        
        for (int i = 0; i < this.columnAmount; ++i) {
            columnWidths[i] = 0;
        }

        List<Double> widths = new LinkedList<>();
        for (TableRow row : rows) {
            for (Cell cell : row.getContent()) {
                if (cell != null) {
                    widths.add(((StateCell) cell).getRequiredWidth());
                }
            }
        }

        //iterate through each cell and determine for each column what the largest width is
        rows.stream().forEach(row -> determineMaxWidthsForRow(columnWidths, row.getContent()));

        //determine the max possible column width (how long would it need to be in order to get all text on one line, this will always return required weight in case of an image)
        rows.stream().forEach(row -> determineTotalRequiredWidthsForRow(maxColumnWidthsPossible, row.getContent()));

        rows.stream().forEach(row -> determineMinWidthsForRow(minColumnWidths, row.getContent()));

        double remainingWidth = this.width;
        //determine how much width is left over to spread over the columns
        for (double columnWidth : columnWidths) {
            remainingWidth -= columnWidth;
        }

        //first we'll add width to each cell, that has no specified width, to make sure they reach the minimum value that they require
        List<ColumnPossibleWidth> allMinPossibleWidths = new ArrayList<>();
        Collections.addAll(allMinPossibleWidths, minColumnWidths);
        
        /**the required cell widths have already been set, so we can filter those out, we'll also want to sort by size to make sure 
           that overly large min. widths aren't processed first (this is likely caused by strings of text without spacing)*/
        List<ColumnPossibleWidth> requiredSortedMinWidths = allMinPossibleWidths.stream()
                .filter(minWidth -> minWidth != null && minWidth.getWidth() > columnWidths[minWidth.getColumn()])
                .sorted((minWidth, minWidth2) -> Double.compare(minWidth.getWidth(), minWidth2.getWidth()))
                .collect(Collectors.toList());
        
        remainingWidth = addExtraWidthToColumns(columnWidths, remainingWidth, requiredSortedMinWidths);

        //if we still have width remaining after meeting the minimum required width, we'll try to match the largest possible widths
        if(remainingWidth > 0) {
            List<ColumnPossibleWidth> allMaxPossibleWidths = new ArrayList<>();
            Collections.addAll(allMaxPossibleWidths, maxColumnWidthsPossible);
            //for the first two steps we'll only want to add width to columns for which the width was not specified by the user
            List<ColumnPossibleWidth> nonFulfilledMaxPossibleWidths = allMaxPossibleWidths.stream()
                    .filter(maxWidth -> maxWidth != null && maxWidth.getWidth() > columnWidths[maxWidth.getColumn()])
                    .collect(Collectors.toList());

            //filter the columns by length, we want to add the extra width to the small ones first to avoid singular words to be spread out over two lines and such
            List<ColumnPossibleWidth> smallMaxPossibleWidths = nonFulfilledMaxPossibleWidths.stream().filter(
                    maxWidth -> maxWidth.getWidth() <= this.width * PRIORITY_COLUMN_WIDTH_PERCENTAGE).collect(Collectors.toList());

            //add width to all small columns
            remainingWidth = addExtraWidthToColumns(columnWidths, remainingWidth, smallMaxPossibleWidths);

            /**if all the columns beneath the threshold have reached the width they need to fit on a single line,
               but we still have extra width to add we'll spread it over any columns that do not have a specified width*/
            if (remainingWidth > 0) {
                //collect all widths that have not been reached yet
                List<ColumnPossibleWidth> largeMaxPossibleWidths = nonFulfilledMaxPossibleWidths.stream()
                        .filter(maxWidth -> !smallMaxPossibleWidths.contains(maxWidth))
                        .collect(Collectors.toList());

                double totalExtraWidthRequired = largeMaxPossibleWidths.stream()
                        .mapToDouble(maxWidth -> (maxWidth.getWidth() - columnWidths[maxWidth.getColumn()]))
                        .sum();
                //if the total width required to fit all cells is larger than the width available, we'll divide it percentage wise. Meaning that the largest column will get the largest amount of the remaining width.
                if (totalExtraWidthRequired > remainingWidth) {
                    spreadRemainingWidthProcentually(columnWidths, remainingWidth, largeMaxPossibleWidths, totalExtraWidthRequired);
                } else {
                    //if we do have the width to fill up the columns, simply add this width and if anything still remains after that spread it equally among all cells without a specified width
                    remainingWidth = addExtraWidthToColumns(columnWidths, remainingWidth, largeMaxPossibleWidths);
                    spreadRemainingWidth(columnWidths, remainingWidth, nonFulfilledMaxPossibleWidths);
                }
            }
        }
        return columnWidths;
    }

    private void determineMinWidthsForRow(ColumnPossibleWidth[] minColumnWidths, Cell[] content) {
        for (int i = 0; i < content.length; ++i) {
            StateCell cell = (StateCell) content[i];
            if (cell != null) {
                double minWidth = cell.getWidth();
                if (minWidth == 0) {
                    minWidth = cell.getStateCellContent() != null ? cell.getStateCellContent().getMinimumWidth() + (cell.getPadding() * 2) + cell.getBorderWidth() : 0;
                }
                minWidth = minWidth / cell.getColumnSpan();
                if(minColumnWidths[i] == null || minWidth > minColumnWidths[i].getWidth()) {
                    minColumnWidths[i] = new ColumnPossibleWidth(minWidth, i);
                }
            }
        }
    }

    private void determineMaxWidthsForRow(double[] columnWidths, Cell[] content) {
        for (int i = 0; i < content.length; ++i) {
            StateCell cell = (StateCell) content[i];
            if (cell != null) {
                columnWidths[i] = Math.max(cell.getRequiredWidth() / cell.getColumnSpan(), columnWidths[i]);
            }
        }
    }

    private void determineTotalRequiredWidthsForRow(ColumnPossibleWidth[] maxColumnWidthPossible, Cell[] content) {
        for (int i = 0; i < content.length; ++i) {
            StateCell cell = (StateCell) content[i];
            if (cell != null) {
                double totalWidthRequired = cell.getWidth();
                if (totalWidthRequired == 0) {
                    totalWidthRequired = cell.getStateCellContent() != null ? cell.getStateCellContent().getTotalRequiredWidth() + (cell.getPadding() * 2) + cell.getBorderWidth() : 0;
                }
                totalWidthRequired = totalWidthRequired / cell.getColumnSpan();
                if (maxColumnWidthPossible[i] == null || totalWidthRequired > maxColumnWidthPossible[i].getWidth()) {
                    maxColumnWidthPossible[i] = new ColumnPossibleWidth(totalWidthRequired, i);
                }
            }
        }
    }

    private double addExtraWidthToColumns(double[] columnWidths, double remainingWidth, List<ColumnPossibleWidth> maxPossibleWidths) {
        int i = 0;
        while (remainingWidth > 0 && i < maxPossibleWidths.size()) {
            ColumnPossibleWidth columnMaxPossibleWidth = maxPossibleWidths.get(i);
            double columnWidth = columnWidths[columnMaxPossibleWidth.getColumn()];
            //make sure we do not add more width than available
            double extraWidth = Math.min(remainingWidth, columnMaxPossibleWidth.getWidth() - columnWidth);
            remainingWidth -= extraWidth;
            columnWidths[columnMaxPossibleWidth.getColumn()] = columnWidth + extraWidth;
            ++i;
        }
        return remainingWidth;
    }

    private void spreadRemainingWidthProcentually(double[] columnWidths, double remainingWidth, List<ColumnPossibleWidth> possibleWidths,
            double totalExtraWidthRequired) {
        double increasePerWidthUnit = remainingWidth / totalExtraWidthRequired;
        for (ColumnPossibleWidth possibleWidth : possibleWidths) {
            double columnWidth = columnWidths[possibleWidth.getColumn()];
            double extraWidthRequired = possibleWidth.getWidth() - columnWidth;
            columnWidths[possibleWidth.getColumn()] = columnWidth + (increasePerWidthUnit * extraWidthRequired);
        }
    }

    private void spreadRemainingWidth(double[] columnWidths, double remainingWidth, List<ColumnPossibleWidth> maxPossibleWidths) {
        if (remainingWidth > 0) {
            double widthPerColumn = remainingWidth / maxPossibleWidths.size();
            for (ColumnPossibleWidth expandableColumn : maxPossibleWidths) {
                if (expandableColumn != null) {
                    columnWidths[expandableColumn.getColumn()] = columnWidths[expandableColumn.getColumn()] + widthPerColumn;
                }
            }
        }
    }

    private void positionCellsForRow(Cell[] content, Position cellPos, double[] widths) {
        for (int rowNumber = 0; rowNumber < content.length; ++rowNumber) {
            StateCell cell = (StateCell) content[rowNumber];
            double adjustX = widths[rowNumber];
            if (cell != null) {
                cell.setPosition(new Position(cellPos));
                // adjustX += cell.getBorderWidth();
            }
            cellPos.adjustX(adjustX);
        }
    }

    private void applyColumnWidths(double[] columnWidths, Cell[] content) {
        for (int i = 0; i < content.length; ++i) {
            StateCell cell = (StateCell) content[i];
            if (cell != null) {
                double width = columnWidths[i];
                for (int span = 1; span < cell.getColumnSpan(); ++span) {
                    width += columnWidths[i + span];
                }
                cell.width(width);
            }
        }
    }

    private List<StateCell> copyContent() {
        return content.stream().map(BaseStateCell::new).collect(Collectors.toList());
    }

    private void determineRowHeight(List<TableRow> rows, int currentRow, int leading) {
        double maxHeight = 0;
        double borderWidth = 0;
        boolean required = false;
        TableRow row = rows.get(currentRow);
        for (int cellCount = 0; cellCount < columnAmount; ++cellCount) {
            Cell c = row.getContent()[cellCount];

            //if the cell is null it might be because there is a cell above this row with a rowspan of more than one
            if (c == null) {
                for (int rowCount = currentRow - 1; rowCount >= 0; --rowCount) {
                    TableRow aboveRow = rows.get(rowCount);
                    c = aboveRow.getContent()[cellCount];
                    if (c != null && (c.getRowSpan() - 1 + rowCount) >= currentRow) {
                        break;
                    } else if (c != null) {
                        c = null;
                        break;
                    }
                }
            }

            if (c != null) {
                StateCell cell = (StateCell) c;
                double height = cell.getRequiredHeight(leading) / cell.getRowSpan();

                if (height > maxHeight) {
                    maxHeight = height;
                    if (cell.getStateCellContent() != null) {
                        required = cell.getStateCellContent().getSpecifiedWidth() != 0;
                    }
                    borderWidth = cell.getBorderWidth();
                }
            }
        }

        row.setMaxHeight(maxHeight);
        row.setMaxHeightRequired(required);
        row.setBorderWidthForMax(borderWidth);
    }

    /**
     * Determines if the given columns are occupied or available
     *
     * @param currentColumn
     * @param columnSpan
     * @param rows
     * @param currentRow
     * @return
     */
    private boolean columnsInUse(int currentColumn, int columnSpan, List<TableRow> rows, int currentRow) {
        //we need to go through all rows and columns to make sure the requested columns are not occupied due to rowspawn/columnspan of other cells
        for (int rowNumber = 0; rowNumber <= currentRow; ++rowNumber) {
            Cell[] row = rows.get(rowNumber).getContent();
            for (int column = 0; column < this.columnAmount; ++column) {
                Cell c = row[column];
                //if on the same row as the columns being checked and another column occupies the column being checked due to columnspan
                //or if not on the current row, but on the same columns for a different row, check if the rowspan of the column occupies the columns being checked
                //rowspan is 1 by default (which is already accounted for by using the rownumber) and therefore requires the minus 1
                if (c != null &&
                        ((rowNumber == currentRow && (column + c.getColumnSpan() - 1) >= currentColumn) ||
                        (column <= currentColumn && cellBetweenColumnAndSpan(column, c.getColumnSpan(), currentColumn, columnSpan) &&
                        (rowNumber + c.getRowSpan() - 1) >= currentRow))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean cellBetweenColumnAndSpan(int cellColumn, int cellColumnSpan, int columnToCheck, int spanToCheck) {
        for (int i = cellColumn; i <= (cellColumn + cellColumnSpan - 1); ++i) {
            if (i >= columnToCheck && i <= (columnToCheck + spanToCheck - 1)) {
                return true;
            }
        }
        return false;
    }

    private TableRow addRowTo(List<TableRow> rows) {
        this.addRowsTo(rows, 1);
        return rows.get(rows.size() - 1);
    }

    private void addRowsTo(List<TableRow> rows, int count) {
        for (int i = 0; i < count; ++i) {
            rows.add(new TableRow(this.columnAmount));
        }
    }

    private double calculateAlignment(StatePage page) {
        double largestWidth = 0;
        for (int[] openSpace : page.getOpenSpacesOn(this.getPosition(), false, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this)) {
            largestWidth = Math.max(largestWidth, openSpace[1] - openSpace[0]);
        }
        double remainder = largestWidth - this.getWidth();
        double adjustment = 0;
        if (remainder > 0) {
            switch (this.getAlignment()) {
            case CENTERED:
                adjustment += remainder / 2;
                break;
            case RIGHT:
                adjustment += remainder;
                break;
            default:
                break;
            }
        }
        return adjustment;
    }

    private Position calculatePosition(StatePage page) {
        double requiredSpaceAbove = this.getRequiredSpaceAbove();
        double requiredSpaceBelow = this.getRequiredSpaceBelow();
        Position pos = new Position(this.getPosition());
        List<int[]> openSpaces = page.getOpenSpacesIncludingHeight(pos, true, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this);
        boolean tablePositioned = false;
        while (pos != null && !tablePositioned) {
            int i = 0;
            while (!tablePositioned && i < openSpaces.size()) {
                int[] openSpace = openSpaces.get(i);
                if (pos.getX() < openSpace[0]) {
                    pos.setX(openSpace[0]);
                }
                int openSpaceWidth = (openSpace[1] - openSpace[0]);
                if (openSpaceWidth >= this.getWidth() && openSpace[2] >= this.getHeight()) {
                    tablePositioned = true;
                    this.setPosition(pos);
                }
                ++i;
            }
            if (!tablePositioned) {
                requiredSpaceAbove += page.getLeading();
                pos = page.getOpenPosition(requiredSpaceAbove, requiredSpaceBelow, this, this.width);
                if (pos != null) {
                    openSpaces = page.getOpenSpacesIncludingHeight(pos, true, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this);
                }
            }
        }
        return pos;
    }

    private void adjustFilledHeight(StatePage page) {
        page.setFilledHeight(page.getFilledHeight() + this.getHeight() + this.getMarginBottom() + this.getMarginTop() + Page.DEFAULT_NEW_LINE_SIZE * 2);
    }

    @Override
    public double getContentHeight(Page page) {
        return this.getHeight();
    }

    @Override
    public double getContentWidth(Page page, Position position) {
        return this.getWidth();
    }

    @Override
    public void updateHeight(StatePage page) {
        this.processContentSize(page, false, false, false, false, true);
    }

    @Override
    public int[] getPositionAt(double height) {
        Position pos = getPosition();
        if (FloatEqualityTester.lessThanOrEqualTo(height, pos.getY() + this.getRequiredSpaceAbove())
                && FloatEqualityTester.greaterThanOrEqualTo(height, pos.getY() - this.getRequiredSpaceBelow())) {
            return new int[] { (int) pos.getX() };
        }
        return new int[] {};
    }

    @Override
    public List<int[]> getUsedSpaces(double height, int pageWidth) {
        Position pos = getPosition();
        List<int[]> space = new LinkedList<int[]>();
        if (FloatEqualityTester.lessThanOrEqualTo(height, pos.getY() + this.getRequiredSpaceAbove())
                && FloatEqualityTester.greaterThanOrEqualTo(height, pos.getY() - this.getRequiredSpaceBelow())) {
            if (wrappingAllowed) {
                space.add(new int[] { (int) this.getPosition().getX() - marginLeft, (int) (this.getPosition().getX() + getWidth() + marginRight) });
            } else {
                space.add(new int[] { 0, pageWidth });
            }
        }
        return space;
    }

    @Override
    public DocumentPart getOriginalObject() {
        return this.originalObject;
    }

    @Override
    public void setOriginalObject(DocumentPart originalObject) {
        if (this.originalObject == null) {
            this.originalObject = originalObject;
        }
    }

    @Override
    public Cell addCell(PlaceableDocumentPart part) {
        Cell c = new BaseCell(part);
        this.addCell(c);
        return c;
    }

    @Override
    public Cell addCell(String s) {
        return this.addCell(new BaseStateText(s));
    }

    @Override
    public Table addCell(Cell c) {
        if (c != null) {
            if (c instanceof StateCell) {
                this.content.add((StateCell) c);
            } else {
                this.content.add(new BaseStateCell(c));
            }
        }
        return this;
    }

    @Override
    public List<Cell> getContent() {
        List<Cell> cells = new LinkedList<Cell>();
        cells.addAll(this.content);
        return cells;
    }

    @Override
    public PlaceableDocumentPart copy() {
        return new BaseStateTable(this);
    }

    @Override
    public List<StateCell> getStateCellCollection() {
        LinkedList<StateCell> cells = new LinkedList<>();
        cells.addAll(this.content);
        return cells;
    }

    @Override
    public double getRequiredSpaceLeft() {
        return marginLeft;
    }

    @Override
    public double getRequiredSpaceRight() {
        return marginRight;
    }

    @Override
    public Table removeContent() {
        this.content = new LinkedList<StateCell>();
        return this;
    }

    private class ColumnPossibleWidth {
        private Double width;
        private int column;
        private boolean required;

        public ColumnPossibleWidth(Double width, int column) {
            this.width = width;
            this.column = column;
            required = false;
        }

        public Double getWidth() {
            return width;
        }

        public int getColumn() {
            return column;
        }

        public boolean isRequired() {
            return required;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }
    }

    public class TableRow {
        private Cell[] content;
        private double maxHeight = 0;
        private double widthUsed = 0;
        private List<StateCell> noWidthSpecified;
        private boolean maxHeightRequired;
        private double borderWidthForMax;

        private TableRow(int columns) {
            content = new Cell[columns];
            noWidthSpecified = new ArrayList<>();
        }

        /**
         * Creates a copy of the given table row
         * @param tableRow row to copy
         */
        public TableRow(TableRow tableRow) {
            this(tableRow.getContent().length);
            Cell[] oldContent = tableRow.getContent();
            List<StateCell> oldNoWidthSpecified = tableRow.getCellsWithNoSpecifiedWidth();

            for (int i = 0; i < oldContent.length; ++i) {
                Cell c = oldContent[i];
                if (c != null) {
                    BaseStateCell newCell = new BaseStateCell(c);
                    content[i] = newCell;
                    if (oldNoWidthSpecified.contains(c)) {
                        noWidthSpecified.add(newCell);
                    }
                }
            }

            this.maxHeight = tableRow.getMaxHeight();
            this.widthUsed = tableRow.getWidthUsed();
            this.maxHeightRequired = tableRow.isMaxHeightRequired();
            this.borderWidthForMax = tableRow.getBorderWidthForMax();
        }

        public Cell[] getContent() {
            return content;
        }

        public void setContent(Cell[] content) {
            this.content = content;
        }

        public double getMaxHeight() {
            return maxHeight;
        }

        public void setMaxHeight(double maxHeight) {
            this.maxHeight = maxHeight;
        }

        public double getWidthUsed() {
            return widthUsed;
        }

        public void setWidthUsed(double widthUsed) {
            this.widthUsed = widthUsed;
        }

        public void addNoWidthSpecifiedCell(StateCell cell) {
            noWidthSpecified.add(cell);
        }

        public List<StateCell> getCellsWithNoSpecifiedWidth() {
            return noWidthSpecified;
        }

        public void setMaxHeightRequired(boolean maxHeightRequired) {
            this.maxHeightRequired = maxHeightRequired;
        }

        public boolean isMaxHeightRequired() {
            return maxHeightRequired;
        }

        public void setBorderWidthForMax(double borderWidthForMax) {
            this.borderWidthForMax = borderWidthForMax;
        }

        public double getBorderWidthForMax() {
            return borderWidthForMax;
        }
    }

}
