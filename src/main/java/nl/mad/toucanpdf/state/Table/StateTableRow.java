package nl.mad.toucanpdf.state.Table;

import java.util.ArrayList;
import java.util.List;

import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.state.StateCell;
import nl.mad.toucanpdf.state.BaseStateCell;

/**
 * Created by dylan on 30-7-15.
 */
public class StateTableRow {
    private Cell[] content;
    private double maxHeight = 0;
    private double widthUsed = 0;
    private List<StateCell> noWidthSpecified;
    private boolean maxHeightRequired;
    private double borderWidthForMax;

    public StateTableRow(int columns) {
        content = new Cell[columns];
        noWidthSpecified = new ArrayList<>();
     }

    /**
     * Creates a copy of the given table row
     * @param tableRow row to copy
     */
    public StateTableRow(StateTableRow tableRow) {
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
