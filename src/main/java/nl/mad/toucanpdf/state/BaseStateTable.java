package nl.mad.toucanpdf.state;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.api.AbstractTable;
import nl.mad.toucanpdf.api.BaseCell;
import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.model.state.StateCell;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StateTable;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

public class BaseStateTable extends AbstractTable implements StateTable {
    private DocumentPart originalObject;
    private List<StateCell> content = new LinkedList<StateCell>();

    public BaseStateTable(int pageWidth) {
        super(pageWidth);
    }

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
        return this.processContentSize(page, this.wrappingAllowed(), true);
    }

    @Override
    public boolean processContentSize(StatePage page, boolean wrapping, boolean processAlignment) {
        return this.processContentSize(page, wrapping, processAlignment, true);
    }

    public boolean processContentSize(StatePage page, boolean wrapping, boolean processAlignment, boolean processPositioning) {
        int totalCellAmount = this.getContent().size();
        MathContext mc = new MathContext(20, RoundingMode.HALF_UP);
        int columnAmount = this.getColumnAmount();
        BigDecimal cellWidth = new BigDecimal(this.getWidth() / columnAmount);
        BigDecimal availableWidth = new BigDecimal(this.width);
        List<StateCell> currentRowCells = new LinkedList<StateCell>();
        int currentFilledColumns = 0;
        double rowHeight = 0;
        Position p = calculatePosition(page);
        if (p == null) {
            return true;
        }
        if (processAlignment && processPositioning) {
            p.adjustX(calculateAlignment(page));
        }
        this.setPosition(p);
        this.height = 0;
        Position cellPos = new Position(this.getPosition());

        // TODO: Support custom widths
        for (int i = 0; i < totalCellAmount; ++i) {
            StateCell c = content.get(i);
            c.columnSpan(Math.min(columnAmount, c.getColumnSpan()));
            int columns = c.getColumnSpan();
            int remainder = (columnAmount - currentFilledColumns);
            if (remainder == 0) ++remainder;
            BigDecimal remainingColumns = new BigDecimal(remainder);
            cellWidth = availableWidth.divide(remainingColumns, mc);
            if (c.getContent() instanceof Text) {
                System.out.println(((Text) c.getContent()).getText());
            }
            if (currentFilledColumns + columns <= columnAmount) {
                BigDecimal reqWidth = calculateWidth(c.getRequiredWidth(), availableWidth, cellWidth, c);
                availableWidth = availableWidth.subtract(reqWidth);
                processCellAddition(c, cellPos, currentRowCells, reqWidth, processPositioning);
                rowHeight = calculateHeight(rowHeight, c, page, height);
                currentFilledColumns += columns;
            } else {
                if (processPositioning && this.getDrawFiller()) {
                    fillRemainderOfRow(rowHeight, cellWidth, columnAmount - currentFilledColumns, cellPos);
                }
                processRow(currentRowCells, rowHeight);
                this.height += rowHeight;
                currentFilledColumns = columns;
                currentRowCells = new LinkedList<StateCell>();
                cellPos.setX(this.getPosition().getX());
                cellPos.setY(cellPos.getY() - rowHeight);
                availableWidth = new BigDecimal(this.width);
                BigDecimal reqWidth = calculateWidth(c.getRequiredWidth(), availableWidth, availableWidth.divide(new BigDecimal(columnAmount), mc), c);
                availableWidth = availableWidth.subtract(reqWidth);
                processCellAddition(c, cellPos, currentRowCells, reqWidth, processPositioning);
                rowHeight = calculateHeight(0, c, page, height);
            }
            if (processPositioning) {
                c.processContentSize(page.getLeading(), this.borderWidth);
            }

            if (i == (totalCellAmount - 1)) {
                if (processPositioning && this.getDrawFiller()) {
                    remainder = columnAmount - currentFilledColumns;
                    if (remainder == 0) ++remainder;
                    cellWidth = availableWidth.divide(new BigDecimal(remainder), mc);
                    fillRemainderOfRow(rowHeight, cellWidth, columnAmount - currentFilledColumns, cellPos);
                }
                processRow(currentRowCells, rowHeight);
                this.height += rowHeight;
                if (!wrapping && processPositioning) {
                    this.adjustFilledHeight(page);
                }
                return false;
            }
        }
        return false;
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

    private double calculateHeight(double rowHeight, StateCell c, StatePage page, double currentTableHeight) {
        if (c.getContent() != null) {
            double requiredHeight = c.getRequiredHeight(page.getLeading(), this.borderWidth);
            if (requiredHeight + currentTableHeight > page.getHeightWithoutMargins()) {
                c.setContent(null);
                return rowHeight;
                //TODO: LOG content too large
            }
            return Math.max(rowHeight, requiredHeight + (this.borderWidth * 2));
        } else {
            return Math.max(rowHeight, c.getHeight());
        }
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
            requiredSpaceAbove += page.getLeading();
            pos = page.getOpenPosition(requiredSpaceAbove, requiredSpaceBelow, this, this.width);
            if (pos != null) {
                openSpaces = page.getOpenSpacesIncludingHeight(pos, true, this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow(), this);
            }
        }
        return pos;
    }

    private void processCellAddition(StateCell c, Position cellPos, List<StateCell> currentRowCells, BigDecimal reqWidth, boolean processPositioning) {
        c.width(reqWidth.doubleValue());
        c.setPosition(new Position(cellPos));
        if (processPositioning) {
            currentRowCells.add(c);
            cellPos.adjustX(reqWidth.doubleValue());
        }
    }

    private BigDecimal calculateWidth(double requiredWidth, BigDecimal availableWidth, BigDecimal cellWidth, StateCell c) {
        BigDecimal defaultWidth = cellWidth.multiply(new BigDecimal(c.getColumnSpan()));
        if (requiredWidth != 0) {
            BigDecimal reqWidth = new BigDecimal(requiredWidth);
            if (reqWidth.compareTo(availableWidth) == 1) {
                // TODO: LOG & handle the overflow, just returning default will likely cause issues when actually placing the content
                c.setContent(null);
                return defaultWidth;
            }
            if (reqWidth.compareTo(defaultWidth) != 0) {
                reqWidth = reqWidth.add(new BigDecimal(borderWidth));
            }
            return defaultWidth.max(reqWidth);
        } else {
            return defaultWidth;
        }
    }

    private List<Cell> fillRemainderOfRow(double rowHeight, BigDecimal cellWidth, int remainingColumns, Position pos) {
        for (int i = 0; i < remainingColumns; ++i) {
            StateCell c = new BaseStateCell();
            c.setPosition(new Position(pos));
            this.addCell((c.width(cellWidth.doubleValue()).height(rowHeight)));
            pos.adjustX(cellWidth.doubleValue());
        }
        return this.getContent();
    }

    private void processRow(List<StateCell> currentRowCells, double cellHeight) {
        for (Cell c : currentRowCells) {
            c.height(cellHeight);
        }
    }

    private void adjustFilledHeight(StatePage page) {
        page.setFilledHeight(page.getFilledHeight() + this.getHeight() + Page.DEFAULT_NEW_LINE_SIZE * 2);
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
        return this.processContentSize(page, false, false, false);
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
        	if(wrappingAllowed) {
            space.add(new int[] { (int) this.getPosition().getX(), (int) (this.getPosition().getX() + getWidth()) });
        	} else {
        		space.add(new int[] { 0, pageWidth });
        	}
        }
        return space;
    }

    @Override
    public void setOriginalObject(DocumentPart originalObject) {
        if (this.originalObject == null) {
            this.originalObject = originalObject;
        }
    }

    @Override
    public DocumentPart getOriginalObject() {
        return this.originalObject;
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
    public void removeContent() {
        this.content = new LinkedList<StateCell>();
    }

}
