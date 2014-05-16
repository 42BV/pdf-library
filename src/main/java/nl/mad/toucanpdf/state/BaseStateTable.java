package nl.mad.toucanpdf.state;

import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.api.AbstractTable;
import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.model.state.StateCell;
import nl.mad.toucanpdf.model.state.StatePage;
import nl.mad.toucanpdf.model.state.StateTable;

public class BaseStateTable extends AbstractTable implements StateTable {
    private DocumentPart originalObject;
    private List<StateCell> content = new LinkedList<StateCell>();

    public BaseStateTable(int pageWidth) {
        super(pageWidth);
    }

    public BaseStateTable(Table table) {
        super(table);
        System.out.println("Copying table");
    }

    @Override
    public double getRequiredSpaceAbove() {
        return 0;
    }

    @Override
    public double getRequiredSpaceBelow() {
        return this.getHeight();
    }

    @Override
    public void processContentSize(StatePage page) {
        this.processContentSize(page, this.wrappingAllowed(), true);
    }

    @Override
    public void processContentSize(StatePage page, boolean wrapping, boolean processAlignment) {
        int columnAmount = this.getColumnAmount();
        List<StateCell> content = new LinkedList<StateCell>(this.content);
        double cellWidth = (double) this.getWidth() / columnAmount;
        double availableWidth = this.width;
        this.height = 0;
        List<StateCell> currentRowCells = new LinkedList<StateCell>();
        int currentFilledColumns = 0;
        double rowHeight = 0;
        Position cellPos = new Position(this.getPosition());

        for (int i = 0; i < content.size(); ++i) {
            cellWidth = availableWidth / (columnAmount - currentFilledColumns);
            System.out.println("CellWidth : " + cellWidth + ", availableWidth: " + availableWidth + ", columnAmount: " + columnAmount + ", currentFilled: "
                    + currentFilledColumns);
            StateCell c = content.get(i);
            if (c.getContent() != null && c.getContent().getType().equals(DocumentPartType.IMAGE)) {
                System.out.println("ITS THE IMAGE");
            }
            c.columnSpan(Math.min(columnAmount, c.getColumnSpan()));
            int columns = c.getColumnSpan();
            //TODO: Support custom widths
            System.out.println("availableWidth: " + availableWidth);

            if (currentFilledColumns + columns <= columnAmount) {
                double reqWidth = calculateWidth(c.getRequiredWidth(), availableWidth, cellWidth, columns);
                System.out.println("Required width: " + reqWidth);
                availableWidth -= reqWidth;
                c.width(reqWidth);
                c.setPosition(new Position(cellPos));
                currentRowCells.add(c);
                cellPos.setX(cellPos.getX() + reqWidth);
                rowHeight = Math.max(rowHeight, c.getRequiredHeight(page.getLeading()));
                currentFilledColumns += columns;
            } else {
                fillRemainderOfRow(rowHeight, cellWidth, columnAmount - currentFilledColumns, cellPos);
                processRow(currentRowCells, rowHeight);
                this.height += rowHeight;
                currentFilledColumns = columns;
                currentRowCells = new LinkedList<StateCell>();
                cellPos.setX(this.getPosition().getX());
                cellPos.setY(cellPos.getY() - rowHeight);
                c.setPosition(new Position(cellPos));
                availableWidth = this.width;
                double reqWidth = calculateWidth(c.getRequiredWidth(), availableWidth, availableWidth / columnAmount, columns);
                System.out.println("Required width new row: " + reqWidth);
                availableWidth -= reqWidth;
                c.width(reqWidth);
                cellPos.adjustX(reqWidth);
                rowHeight = c.getRequiredHeight(page.getLeading());
                currentRowCells.add(c);
            }

            if (i == (content.size() - 1)) {
                fillRemainderOfRow(rowHeight, cellWidth, columnAmount - currentFilledColumns, cellPos);
                processRow(currentRowCells, rowHeight);
                this.height += rowHeight;
            }
        }
    }

    private double calculateWidth(double requiredWidth, double availableWidth, double cellWidth, double columns) {
        double defaultWidth = cellWidth * columns;
        if (requiredWidth != 0) {
            if (requiredWidth > availableWidth) {
                //TODO: LOG & handle the overflow
            }
            return requiredWidth;
        } else {
            return defaultWidth;
        }
    }

    private List<Cell> fillRemainderOfRow(double rowHeight, double cellWidth, int remainingColumns, Position pos) {
        if (remainingColumns > 0) {
            for (int i = 0; i < remainingColumns; ++i) {
                Cell c = new BaseStateCell().setPosition(new Position(pos.getX(), pos.getY()));
                this.addCell((c.width(cellWidth).height(rowHeight)));
                pos.adjustX(cellWidth);
            }
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
    public int[] getPositionAt(double height) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<int[]> getUsedSpaces(double height) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cell addCell(String s) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<StateCell> getStateCellCollection() {
        return this.content;
    }

}
