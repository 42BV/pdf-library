package org.toucanpdf.state.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.toucanpdf.api.AbstractTable;
import org.toucanpdf.api.BaseCell;
import org.toucanpdf.model.Cell;
import org.toucanpdf.model.DocumentPart;
import org.toucanpdf.model.Page;
import org.toucanpdf.model.PlaceableDocumentPart;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Space;
import org.toucanpdf.model.Table;
import org.toucanpdf.model.state.StateCell;
import org.toucanpdf.model.state.StateCellContent;
import org.toucanpdf.model.state.StatePage;
import org.toucanpdf.model.state.StateTable;
import org.toucanpdf.state.BaseStateCell;
import org.toucanpdf.state.BaseStateText;
import org.toucanpdf.utility.FloatEqualityTester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseStateTable extends AbstractTable implements StateTable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseStateTable.class);
    private static final double MINIMUM_PAGE_HEIGHT_REQUIRED = 0.25;
    private DocumentPart originalObject;
    private List<StateCell> content = new LinkedList<StateCell>();
    private StateTableRow header = null;
    private List<StateTableRow> rows;
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
        StateTableRow tableHeader = table.getHeader();
        if (tableHeader != null && tableHeader.getContent() != null) {
            this.header = new StateTableRow(table.getHeader());
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
    public StateTableRow getHeader() {
        return header;
    }

    @Override
    public void setHeader(StateTableRow header) {
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
        int availableHeight = 0;
        height = marginBottom + marginTop;

        this.determineCellBorders(tableContent);
        this.determineCellPadding(tableContent);

        //if not fixed, calculate a position for this table and process alignment if needed
        if (!fixed) {
            Position p = determinePositionForTable(page, processAlignment, processPositioning);
            if (p == null) {
                return this;
            }
            availableHeight = calculateAvailableHeight(page, p);
        }

        validateColumnSpans(tableContent);
        placeContentInRows(tableContent);

        //finalize the cell widths, heights and execute positioning
        Position cellPos = new Position(this.getPosition());

        double[] widths = determineCellWidths();

        //add cells to fill up empty columns in the table
        if (getDrawFiller()) {
            fillEmptyCells(rows, tableContent);
        }

        return applyCellSize(page, processPositioning, fixed, ignoreOverflow, availableHeight, cellPos, widths);
    }

    private StateTable applyCellSize(StatePage page, boolean processPositioning, boolean fixed, boolean ignoreOverflow, int availableHeight, Position cellPos,
            double[] widths) {
        StateTable overflow = null;
        boolean overflowDetected = false;
        int index = 0;
        int overflowRow = rows.size();
        while (!overflowDetected && index < rows.size()) {
            StateTableRow row = rows.get(index);
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
                double heightIncrease = row.getMaxHeight();
                this.height += heightIncrease;
                adjustCellPositionForNextRow(cellPos, heightIncrease);
                index++;
            }
        }

        //after all individual rows have been processed, increase the height of cells that occupy more than one row
        //TODO: Test that this is not going to cause issues in overflow detection (if second row of cell with rowspawn 2 causes the overflow)
        applyRowSpanHeights(rows);

        ProcessContentSizeOfCells(page, overflowRow);

        List<StateCell> cells = combineCellsFromRows(rows);
        if (processPositioning) {
            FinalizePositioning(page, cells);
        } else {
            this.height = Math.min((int) (MINIMUM_PAGE_HEIGHT_REQUIRED * page.getHeight()), this.height);
        }
        return overflow;
    }

    private void adjustCellPositionForNextRow(Position cellPos, double heightIncrease) {
        cellPos.adjustY(-heightIncrease);
        cellPos.setX(this.getPosition().getX());
    }

    private double[] determineCellWidths() {
        if (original) {
            StateTableColumnWidthCalculator calculator = new StateTableColumnWidthCalculator(this.columnAmount, this.width);
            this.originalColumnWidths = calculator.calculateColumnWidths(rows, this.isPrioritizingHeaderWidth());
        }
        return this.originalColumnWidths;
    }

    private void placeContentInRows(List<StateCell> tableContent) {
        addHeaderToRows(rows);
        StateTableCellPlacer cellPlacer = new StateTableCellPlacer(tableContent, this.columnAmount);
        rows = cellPlacer.divideColumnsOverRows(rows);
        if (this.header == null && rows.size() > 0) {
            this.setNewHeaderBasedOnContent(tableContent);
        }
    }

    private void addHeaderToRows(List<StateTableRow> rows) {
        if (header != null && (original || this.isRepeatingHeader())) {
            rows.add(0, header);
        }
    }

    private void validateColumnSpans(List<StateCell> tableContent) {
        for (StateCell c : tableContent) {
            //make sure the column span on each cell does not exceed the max column amount
            c.columnSpan(Math.min(c.getColumnSpan(), this.columnAmount));
        }
    }

    private Position determinePositionForTable(StatePage page, boolean processAlignment, boolean processPositioning) {
        Position p = calculatePosition(page);
        if (p != null) {
            applyAlignmentToPosition(page, processAlignment, processPositioning, p);
            this.setPosition(p);
        }
        return p;
    }

    private int calculateAvailableHeight(StatePage page, Position position) {
        int availableHeight;
        List<Space> openSpaces = page.getOpenSpacesIncludingHeight(position, true, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this);
        availableHeight = openSpaces.get(0).getHeight();
        return availableHeight;
    }

    private void applyAlignmentToPosition(StatePage page, boolean processAlignment, boolean processPositioning, Position poisition) {
        if (processAlignment && processPositioning) {
            poisition.adjustX(calculateAlignment(page));
        }
    }

    private void ProcessContentSizeOfCells(StatePage page, int overflowRow) {
        for (int i = 0; i < overflowRow; ++i) {
            StateTableRow row = rows.get(i);
            for (Cell c : row.getContent()) {
                if (c != null) {
                    ((StateCell) c).processContentSize(page.getLeading());
                }
            }
        }
    }

    private void FinalizePositioning(StatePage page, List<StateCell> cells) {
        if (this.isVerticalAligned()) {
            for (StateCell c : cells) {
                c.processVerticalAlignment();
            }
        }
        this.content = cells;
        this.adjustFilledHeight(page);
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

    private List<StateCell> combineCellsFromRows(List<StateTableRow> rows) {
        List<StateCell> cells = new LinkedList<>();
        for (StateTableRow row : rows) {
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

    private StateTable processOverflow(List<StateTableRow> rows, int index, int availableHeight) {
        StateTableRow current = rows.get(index);
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
                removeRow(rows, i);
            }
        }
        return overflow;
    }

    private void removeRow(List<StateTableRow> rows, int i) {
        for (Cell cell : rows.get(i).getContent()) {
            this.content.remove(cell);
        }
        rows.remove(i);
    }

    private boolean isHeightCausingOverflow(StateTableRow current, int availableHeight) {
        return (current.getMaxHeight() + this.height > availableHeight);
    }

    private void fillEmptyCells(List<StateTableRow> rows, List<StateCell> tableContent) {
        StateTableColumnUsageDetector detector = new StateTableColumnUsageDetector(this.columnAmount);
        for (int currentRow = 0; currentRow < rows.size(); ++currentRow) {
            StateTableRow row = rows.get(currentRow);
            Cell[] content = row.getContent();

            for (int i = 0; i < this.columnAmount; ++i) {
                if (content[i] == null && detector.columnsNotInUse(i, 1, rows, currentRow)) {
                    StateCell c = new BaseStateCell();
                    c.border(this.borderWidth).padding(this.padding);
                    tableContent.add(c);
                    content[i] = c;
                }
            }
        }
    }

    private void applyRowSpanHeights(List<StateTableRow> rows) {
        //go through all rows and cells
        for (int rowIndex = 0; rowIndex < rows.size(); ++rowIndex) {
            StateTableRow row = rows.get(rowIndex);
            Cell[] cells = row.getContent();
            for (Cell c : cells) {
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

    private void applyColumnHeights(StateTableRow row) {
        for (Cell c : row.getContent()) {
            if (c != null) {
                StateCell cell = (StateCell) c;
                cell.height(row.getMaxHeight());
            }
        }
    }

    private void setNewHeaderBasedOnContent(List<StateCell> tableContent) {
        header = new StateTableRow(rows.get(0));
        Cell[] headerContent = this.header.getContent();
        for (Cell aHeaderContent : headerContent) {
            if (aHeaderContent != null) {
                removeHeaderFromContentLists(tableContent);
            }
        }
    }

    private void removeHeaderFromContentLists(List<StateCell> tableContent) {
        tableContent.remove(0);
        this.content.remove(0);
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

    private void determineRowHeight(List<StateTableRow> rows, int currentRow, int leading) {
        double maxHeight = 0;
        StateCell highest = null;

        StateTableRow row = rows.get(currentRow);
        for (int cellCount = 0; cellCount < columnAmount; ++cellCount) {
            Cell c = row.getContent()[cellCount];
            //if the cell is null it might be because there is a cell above this row with a rowspan of more than one
            if (c == null) {
                c = getCellAbove(rows, currentRow, cellCount);
            }

            if (c != null) {
                StateCell cell = (StateCell) c;
                double height = cell.getRequiredHeight(leading) / cell.getRowSpan();
                if (height > maxHeight) {
                    maxHeight = height;
                    highest = cell;
                }
            }
        }

        if (highest != null) {
            setMaxHeightForRow(maxHeight, highest, row);
        }
    }

    private void setMaxHeightForRow(double maxHeight, StateCell highest, StateTableRow row) {
        row.setMaxHeight(maxHeight);
        StateCellContent content = highest.getStateCellContent();
        row.setMaxHeightRequired(content != null && content.getSpecifiedWidth() != 0);
        row.setBorderWidthForMax(highest.getBorderWidth());
    }

    private Cell getCellAbove(List<StateTableRow> rows, int currentRow, int cellCount) {
        Cell c = null;
        for (int rowCount = currentRow - 1; rowCount >= 0; --rowCount) {
            StateTableRow aboveRow = rows.get(rowCount);
            c = aboveRow.getContent()[cellCount];
            if (c != null && (c.getRowSpan() - 1 + rowCount) >= currentRow) {
                break;
            } else if (c != null) {
                c = null;
                break;
            }
        }
        return c;
    }

    private double calculateAlignment(StatePage page) {
        double largestWidth = 0;
        for (Space openSpace : page.getOpenSpacesOn(this.getPosition(), false, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this)) {
            largestWidth = Math.max(largestWidth, openSpace.getEndPoint() - openSpace.getStartPoint());
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
        List<Space> openSpaces = page.getOpenSpacesIncludingHeight(pos, true, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this);
        boolean tablePositioned = false;
        while (pos != null && !tablePositioned) {
            int i = 0;
            while (!tablePositioned && i < openSpaces.size()) {
                Space openSpace = openSpaces.get(i);
                if (pos.getX() < openSpace.getStartPoint()) {
                    pos.setX(openSpace.getStartPoint());
                }
                int openSpaceWidth = (openSpace.getEndPoint() - openSpace.getStartPoint());
                if (openSpaceWidth >= this.getWidth() && openSpace.getHeight() >= this.getHeight()) {
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
        if (isTableOnGivenHeight(height)) {
            return new int[] { (int) getPosition().getX() };
        }
        return new int[] {};
    }

    @Override
    public List<Space> getUsedSpaces(double height, int pageWidth) {
        List<Space> space = new LinkedList<>();
        if (isTableOnGivenHeight(height)) {
            if (wrappingAllowed) {
                space.add(new Space((int) this.getPosition().getX() - marginLeft, (int) (this.getPosition().getX() + getWidth() + marginRight)));
            } else {
                space.add(new Space(0, pageWidth));
            }
        }
        return space;
    }

    private boolean isTableOnGivenHeight(double height) {
        Position pos = getPosition();
        return FloatEqualityTester.lessThanOrEqualTo(height, pos.getY() + this.getRequiredSpaceAbove())
                && FloatEqualityTester.greaterThanOrEqualTo(height, pos.getY() - this.getRequiredSpaceBelow());
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
        List<Cell> cells = new LinkedList<>();
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
        this.content = new LinkedList<>();
        return this;
    }
}
