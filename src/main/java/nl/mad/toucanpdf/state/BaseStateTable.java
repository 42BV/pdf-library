package nl.mad.toucanpdf.state;

import java.util.*;
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
import nl.mad.toucanpdf.model.state.StateCellContent;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StateTable;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseStateTable extends AbstractTable implements StateTable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseStateTable.class);
    private DocumentPart originalObject;
    private List<StateCell> content = new LinkedList<StateCell>();

    /**
     * Creates a new instance of BaseStateTable.
     * @param pageWidth Width of the page.
     */
    public BaseStateTable(int pageWidth) {
        super(pageWidth);
    }

    /**
     * Creates a new instance of BaseStateTable and copies the given table. 
     * The content of the given table is not copied.
     * @param table Table to copy from.
     */
    public BaseStateTable(Table table) {
        super(table);
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
    public boolean processContentSize(StatePage page) {
        return this.processContentSize(page, this.isWrappingAllowed(), true, false);
    }

    @Override
    public boolean processContentSize(StatePage page, boolean wrapping, boolean processAlignment, boolean fixed) {
        return this.processContentSize(page, wrapping, processAlignment, true, fixed);
    }

    /**
     * Processes the size of the table/cells and the positioning of said objects.
     * @param page Page the table will be on.
     * @param wrapping Whether wrapping should be allowed.
     * @param processAlignment Whether alignment should be processed.
     * @param processPositioning Whether positioning should be processed.
     * @param fixed Whether the table has a fixed position.
     * @return true if there was overflow, false otherwise.
     */
    public boolean processContentSize(StatePage page, boolean wrapping, boolean processAlignment, boolean processPositioning, boolean fixed) {
        List<StateCell> tableContent = copyContent();
        List<TableRow> rows = new LinkedList<>();
        addRowTo(rows);
        int currentRow = 0;
        int currentColumn = 0;
        this.height = 0;
        double widthUsedInRow = 0;

        //if not fixed, calculate a position for this table and process alignment if needed
        if (!fixed) {
            Position p = calculatePosition(page);
            if (p == null) {
                return true;
            }
            if (processAlignment && processPositioning) {
                p.adjustX(calculateAlignment(page));
            }
            this.setPosition(p);
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
                    if (((StateCellContent) c.getContent()).getSpecifiedWidth() == 0) {
                        rows.get(currentRow).addNoWidthSpecifiedCell(c);
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

        //finalize the cell widths, heights and execute positioning
        Position cellPos = new Position(this.getPosition());
        double[] widths = calculateColumnWidths(rows);
        for (TableRow row : rows) {
            //add cells to fill up empty columns in the table
            //fillEmptyCells(row.getContent());

            //apply the calculated widths to the columns
            applyColumnWidths(widths, row.getContent());

            //for height determination we first need to position each cell
            positionCellsForRow(row.getContent(), cellPos, widths);

            determineRowHeight(row, page.getLeading());
            for (Cell c : row.getContent()) {
                if (c != null) {
                    StateCell cell = (StateCell) c;
                    c.height((row.getMaxHeight() * c.getRowSpan()));
                    cell.processContentSize(page.getLeading(), this.borderWidth);
                }
            }
            double heightIncrease = row.getMaxHeight();// + borderWidth;
            this.height += heightIncrease;
            cellPos.adjustY(-heightIncrease);
            cellPos.setX(this.getPosition().getX());
        }

        if (processPositioning) {
            this.content = tableContent;
            this.adjustFilledHeight(page);
        }
        return false;
    }

    private double[] calculateColumnWidths(List<TableRow> rows) {
        double[] columnWidths = new double[this.columnAmount];
        ColumnMaxPossibleWidth[] maxColumnWidthPossible = new ColumnMaxPossibleWidth[this.columnAmount];
        for (int i = 0; i < this.columnAmount; ++i) {
            columnWidths[i] = 0;
        }

        //iterate through each cell and determine for each column what the largest width is
        rows.stream().forEach(row -> determineMaxWidthsForRow(columnWidths, row.getContent()));

        //determine the max possible column width (how long would it need to be in order to get all text on one line, this will always return required weight in case of an image)
        rows.stream().forEach(row -> determineTotalRequiredWidthsForRow(maxColumnWidthPossible, row.getContent()));

        double remainingWidth = this.width;
        //determine how much width is left over to spread over the columns
        for(double columnWidth : columnWidths) {
            remainingWidth -= columnWidth;
        }

        List<ColumnMaxPossibleWidth> sortedMaxPossibleWidths = new ArrayList<>();
        Collections.addAll(sortedMaxPossibleWidths, maxColumnWidthPossible);
        //sort the columns by length, we want to add the extra width to the small ones first to avoid singular words to be spread out over two lines and such
        Collections.sort(sortedMaxPossibleWidths, (ColumnMaxPossibleWidth c1, ColumnMaxPossibleWidth c2) -> c1.getWidth().compareTo(c2.getWidth()));

        remainingWidth = addExtraWidthToColumns(columnWidths, remainingWidth, sortedMaxPossibleWidths);

        //if all the columns have reached the width they need to fit on a single line, but we still have extra width to add we'll equally spread it over any columns that do not have a specified width
        if(remainingWidth > 0) {
            spreadRemainingWidth(columnWidths, remainingWidth, sortedMaxPossibleWidths);
        }

        return columnWidths;
    }

    private double addExtraWidthToColumns(double[] columnWidths, double remainingWidth, List<ColumnMaxPossibleWidth> sortedMaxPossibleWidths) {
        int i = 0;
        while(remainingWidth > 0 && i < sortedMaxPossibleWidths.size()) {
            ColumnMaxPossibleWidth columnMaxPossibleWidth = sortedMaxPossibleWidths.get(i);
            double columnWidth = columnWidths[columnMaxPossibleWidth.getColumn()];

            //if the max possible height is equal to the required column width, it means we're dealing with a manually specified width or an image. We don't want to add any extra width to such columns.
            if(columnMaxPossibleWidth.getWidth() != columnWidth) {
                //make sure we do not add more width than available
                double extraWidth = Math.min(remainingWidth, columnMaxPossibleWidth.getWidth() - columnWidth);
                remainingWidth -= extraWidth;
                columnWidths[columnMaxPossibleWidth.getColumn()] = columnWidth + extraWidth;
            } else {
                columnMaxPossibleWidth.setMaxRequired(true);
            }
            ++i;
        }
        return remainingWidth;
    }

    private void spreadRemainingWidth(double[] columnWidths, double remainingWidth, List<ColumnMaxPossibleWidth> sortedMaxPossibleWidths) {
        List<ColumnMaxPossibleWidth> expandableColumns = sortedMaxPossibleWidths.stream().filter(c -> !c.isMaxRequired()).collect(Collectors.toList());
        double widthPerColumn = remainingWidth / expandableColumns.size();
        for (ColumnMaxPossibleWidth expandableColumn : expandableColumns) {
            columnWidths[expandableColumn.getColumn()] = columnWidths[expandableColumn.getColumn()] + widthPerColumn;
        }
    }

    private void positionCellsForRow(Cell[] content, Position cellPos, double[] widths) {
        for (int rowNumber = 0; rowNumber < content.length; ++rowNumber) {
            StateCell cell = (StateCell) content[rowNumber];
            if (cell != null) {
                cell.setPosition(new Position(cellPos));
                cellPos.adjustX(cell.getRequiredWidth());
            } else {
                cellPos.adjustX(widths[rowNumber]);
            }
        }
    }

    private void applyColumnWidths(double[] columnWidths, Cell[] content) {
        for (int i = 0; i < content.length; ++i) {
            StateCell cell = (StateCell) content[i];
            if (cell != null) {
                cell.width(columnWidths[i] * cell.getColumnSpan());
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

    private void determineTotalRequiredWidthsForRow(ColumnMaxPossibleWidth[] maxColumnWidthPossible, Cell[] content) {
        for (int i = 0; i < content.length; ++i) {
            StateCell cell = (StateCell) content[i];
            if (cell != null) {
                double totalWidthRequired = cell.getStateCellContent().getTotalRequiredWidth();
                if (maxColumnWidthPossible[i] == null || totalWidthRequired > maxColumnWidthPossible[i].getWidth()) {
                    maxColumnWidthPossible[i] = new ColumnMaxPossibleWidth(totalWidthRequired, i);
                }
            }
        }
    }

    private List<StateCell> copyContent() {
        return content.stream().map(BaseStateCell::new).collect(Collectors.toList());
    }

    private void determineRowHeight(TableRow row, int leading) {
        double maxHeight = 0;
        boolean required = false;
        for (Cell c : row.getContent()) {
            if (c != null) {
                StateCell cell = (StateCell) c;
                double height = cell.getRequiredHeight(leading, this.borderWidth) / cell.getRowSpan();

                //TODO remove content if table too large  (what to do with tables that are simply more than one page long?

                if (height > maxHeight) {
                    maxHeight = height;
                    required = cell.getStateCellContent().getSpecifiedWidth() != 0;
                }

            }
        }
        row.setMaxHeight(maxHeight);
        row.setMaxHeightRequired(required);
    }

    /**
     * Determines if the given columns are occupied or available
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
                        (column >= currentColumn && column <= (currentColumn + columnSpan - 1) &&
                        (rowNumber + c.getRowSpan() - 1) >= currentRow))) {
                    return true;
                }
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
    public boolean updateHeight(StatePage page) {
        return this.processContentSize(page, false, false, false, false);
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
        if (c instanceof StateCell) {
            this.content.add((StateCell) c);
        } else {
            this.content.add(new BaseStateCell(c));
        }
        return this;
    }

    @Override
    public List<Cell> getContent() {
        List<Cell> cells = new LinkedList<Cell>();
        cells.addAll(content);
        return cells;
    }

    @Override
    public PlaceableDocumentPart copy() {
        return new BaseStateTable(this);
    }

    @Override
    public List<StateCell> getStateCellCollection() {
        return this.content;
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

    private class ColumnMaxPossibleWidth {
        private Double width;
        private int column;
        private boolean maxRequired;

        public ColumnMaxPossibleWidth(Double width, int column) {
            this.width = width;
            this.column = column;
            maxRequired = false;
        }

        public Double getWidth() {
            return width;
        }

        public int getColumn() {
            return column;
        }

        public boolean isMaxRequired() {
            return maxRequired;
        }

        public void setMaxRequired(boolean maxRequired) {
            this.maxRequired = maxRequired;
        }
    }

    private class TableRow {
        private Cell[] content;
        private double maxHeight = 0;
        private double widthUsed = 0;
        private List<StateCell> noWidthSpecified;
        private boolean maxHeightRequired;

        private TableRow(int columns) {
            content = new Cell[columns];
            noWidthSpecified = new ArrayList<>();
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
    }

}
