package org.toucanpdf.state.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.toucanpdf.model.Cell;
import org.toucanpdf.model.state.StateCell;
import org.toucanpdf.model.state.StateCellContent;
import org.toucanpdf.model.state.StatePlaceableFixedSizeDocumentPart;

public class StateTableColumnWidthCalculator {
    //columns that require a width less than the table width * this percentage will be prioritized during the dividing of width
    private static final double PRIORITY_COLUMN_WIDTH_PERCENTAGE = 0.2;
    private static final Logger LOGGER = LoggerFactory.getLogger(StateTableColumnWidthCalculator.class);

    private int columnAmount;
    private double width;
    private List<StateTableRow> rows;

    public StateTableColumnWidthCalculator(int columnAmount, double width) {
        this.columnAmount = columnAmount;
        this.width = width;
    }

    public double[] calculateColumnWidths(List<StateTableRow> rowList, boolean prioritizeHeaderWidth) {
        double[] columnWidths = getDefaultWidths();
        this.rows = rowList;

        determineMaxWidths(columnWidths);
        double remainingWidth = width - determineWidthUsed(columnWidths);
        remainingWidth = divideWidthToAchieveMinRequiredWidths(rows, columnWidths, remainingWidth, prioritizeHeaderWidth);
        if (remainingWidth > 0) {
            matchTotalRequiredWidths(columnWidths, remainingWidth);
        }
        return columnWidths;
    }

    private void determineMaxWidths(double[] columnWidths) {
        rows.stream().forEach(row -> determineMaxWidthsForRow(columnWidths, row.getContent()));
    }

    private double[] getDefaultWidths() {
        double[] columnWidths = new double[this.columnAmount];
        for (int i = 0; i < this.columnAmount; ++i) {
            columnWidths[i] = 0;
        }
        return columnWidths;
    }

    private double determineWidthUsed(double[] columnWidths) {
        double widthUsed = 0;
        for (double columnWidth : columnWidths) {
            widthUsed += columnWidth;
        }
        return widthUsed;
    }

    private double divideWidthToAchieveMinRequiredWidths(List<StateTableRow> rows, double[] columnWidths, double remainingWidth, boolean prioritizeHeaderWidth) {
        ColumnPossibleWidth[] minColumnWidths = determineMinWidthsForTable(rows, prioritizeHeaderWidth);

        //first we'll add width to each cell, that has no specified width, to make sure they reach the minimum value that they require
        List<ColumnPossibleWidth> allMinPossibleWidths = new ArrayList<>();
        Collections.addAll(allMinPossibleWidths, minColumnWidths);

        //filter out all columns that have a width specified by the user
        Predicate<ColumnPossibleWidth> defaultFilter = (minWidth) -> minWidth != null && minWidth.getWidth() > columnWidths[minWidth.getColumn()];
        boolean enoughSpace = checkRequiredSpaceAvailable(remainingWidth, allMinPossibleWidths, defaultFilter);
        //if there is not enough space, also filter out any columns that are too large
        Predicate<ColumnPossibleWidth> minFilter = determineSortingFilterToUseBasedOnAvailableSpace(defaultFilter, enoughSpace);
        List<ColumnPossibleWidth> requiredSortedMinWidths = getFilteredMinRequiredWidthsSortedBySize(allMinPossibleWidths, minFilter);

        remainingWidth = addExtraWidthToColumns(columnWidths, remainingWidth, requiredSortedMinWidths);
        //if there is not enough space to meet all min required values, we'll spread the remainder percentage wise
        if(!enoughSpace && remainingWidth > 0) {
            remainingWidth = spreadRemainderPercentageWiseOverMinPossibleWidths(columnWidths, remainingWidth, allMinPossibleWidths, defaultFilter);
        }
        return remainingWidth;
    }

    private ColumnPossibleWidth[] determineMinWidthsForTable(List<StateTableRow> rows, boolean prioritizeHeaderWidth) {
        ColumnPossibleWidth[] minColumnWidths = new ColumnPossibleWidth[this.columnAmount];
        if(prioritizeHeaderWidth) {
            determineMinWidthsForRow(minColumnWidths, rows.get(0).getContent());
        } else {
            rows.stream().forEach(row -> determineMinWidthsForRow(minColumnWidths, row.getContent()));
        }
        return minColumnWidths;
    }

    private double spreadRemainderPercentageWiseOverMinPossibleWidths(double[] columnWidths, double remainingWidth,
            List<ColumnPossibleWidth> allMinPossibleWidths, Predicate<ColumnPossibleWidth> defaultFilter) {
        List<ColumnPossibleWidth> largeWidths = getMinPossibleWidthsExceedingThreshold(allMinPossibleWidths, defaultFilter);
        double totalExtraWidthRequired = largeWidths.stream().mapToDouble(ColumnPossibleWidth::getWidth).sum();
        remainingWidth = spreadRemainingWidthPercentageWise(columnWidths, remainingWidth, largeWidths, totalExtraWidthRequired);
        return remainingWidth;
    }

    private List<ColumnPossibleWidth> getMinPossibleWidthsExceedingThreshold(List<ColumnPossibleWidth> allMinPossibleWidths,
            Predicate<ColumnPossibleWidth> defaultFilter) {
        return allMinPossibleWidths
                    .stream()
                    .filter(defaultFilter.and(minWidth -> minWidth.getWidth() >= (this.width * PRIORITY_COLUMN_WIDTH_PERCENTAGE)))
                    .collect(Collectors.toList());
    }

    private List<ColumnPossibleWidth> getFilteredMinRequiredWidthsSortedBySize(List<ColumnPossibleWidth> allMinPossibleWidths,
            Predicate<ColumnPossibleWidth> minFilter) {
        return allMinPossibleWidths
                    .stream()
                    .filter(minFilter)
                    .sorted((minWidth, minWidth2) -> Double.compare(minWidth.getWidth(), minWidth2.getWidth()))
                    .collect(Collectors.toList());
    }

    private Predicate<ColumnPossibleWidth> determineSortingFilterToUseBasedOnAvailableSpace(Predicate<ColumnPossibleWidth> defaultFilter, boolean enoughSpace) {
        Predicate<ColumnPossibleWidth> minFilter = defaultFilter;
        if(!enoughSpace) {
            minFilter = minFilter.and((minWidth) -> minWidth.getWidth() <= (this.width * PRIORITY_COLUMN_WIDTH_PERCENTAGE));
        }
        return minFilter;
    }

    private boolean checkRequiredSpaceAvailable(double remainingWidth, List<ColumnPossibleWidth> allMinPossibleWidths,
            Predicate<ColumnPossibleWidth> defaultFilter) {
        double totalColumnWidthRequired = allMinPossibleWidths.stream().filter(defaultFilter).mapToDouble(ColumnPossibleWidth::getWidth).sum();
        return totalColumnWidthRequired <= remainingWidth;
    }

    private void determineMinWidthsForRow(ColumnPossibleWidth[] minColumnWidths, Cell[] content) {
        for (int i = 0; i < content.length; ++i) {
            StateCell cell = (StateCell) content[i];
            if (cell != null) {
                //TODO: will this work if the image width is larger than the cell width
                double minWidth = cell.getWidth();
                if (minWidth == 0) {
                    minWidth = cell.getStateCellContent() != null ? cell.getStateCellContent().getMinimumWidth() + (cell.getPadding() * 2)
                            + cell.getBorderWidth() : 0;
                }
                ReplaceWidthIfNewWidthIsLarger(minColumnWidths, i, cell, minWidth);
            }
        }
    }

    private void matchTotalRequiredWidths(double[] columnWidths, double remainingWidth) {
        ColumnPossibleWidth[] maxColumnWidthsPossible = new ColumnPossibleWidth[this.columnAmount];
        //determine the max possible column width (how long would it need to be in order to get all text on one line, this will always return required weight in case of an image)
        rows.stream().forEach(row -> determineTotalRequiredWidthsForRow(maxColumnWidthsPossible, row.getContent()));

        List<ColumnPossibleWidth> allMaxPossibleWidths = new ArrayList<>();
        Collections.addAll(allMaxPossibleWidths, maxColumnWidthsPossible);
        //we'll only want to add width to columns for which the width was not specified by the user
        List<ColumnPossibleWidth> maxPossibleWidthsNotSpecifiedByUser = getMaxPossibleWidthsNotSpecifiedByUser(columnWidths, allMaxPossibleWidths);
        //filter the columns by length, we want to add the extra width to the small ones first to avoid small sentences to be spread out over two lines and such
        List<ColumnPossibleWidth> smallMaxPossibleWidths = filterByWidthSmallerThanPriorityWidth(maxPossibleWidthsNotSpecifiedByUser);

        //try to reach the small max possible widths
        remainingWidth = addExtraWidthToColumns(columnWidths, remainingWidth, smallMaxPossibleWidths);

        if (remainingWidth > 0) {
            spreadRemainingWidthOverMaxPossibleWidthsExceedingThreshold(columnWidths, remainingWidth, maxPossibleWidthsNotSpecifiedByUser, smallMaxPossibleWidths);
        }
    }

    private List<ColumnPossibleWidth> filterByWidthSmallerThanPriorityWidth(List<ColumnPossibleWidth> nonFulfilledMaxPossibleWidths) {
        return nonFulfilledMaxPossibleWidths.stream().filter(
                maxWidth -> maxWidth.getWidth() <= this.width * PRIORITY_COLUMN_WIDTH_PERCENTAGE).collect(Collectors.toList());
    }

    private List<ColumnPossibleWidth> getMaxPossibleWidthsNotSpecifiedByUser(double[] columnWidths, List<ColumnPossibleWidth> allMaxPossibleWidths) {
        return allMaxPossibleWidths.stream()
                    .filter(maxWidth -> maxWidth != null && maxWidth.getWidth() > columnWidths[maxWidth.getColumn()])
                    .collect(Collectors.toList());
    }

    private void spreadRemainingWidthOverMaxPossibleWidthsExceedingThreshold(double[] columnWidths, double remainingWidth,
            List<ColumnPossibleWidth> maxPossibleWidthsNotSpecifiedByUser, List<ColumnPossibleWidth> smallMaxPossibleWidths) {
        List<ColumnPossibleWidth> largeMaxPossibleWidths = getLargeMaxPossibleWidthsNotSpecifiedByUser(maxPossibleWidthsNotSpecifiedByUser,
                smallMaxPossibleWidths);
        double totalExtraWidthRequired = sumOfLargeMaxPossibleWidths(columnWidths, largeMaxPossibleWidths);

        if (totalExtraWidthRequired > remainingWidth) {
            spreadRemainingWidthPercentageWise(columnWidths, remainingWidth, largeMaxPossibleWidths, totalExtraWidthRequired);
        } else {
            spreadRemainingWidthOverLargeMaxPossibleWidths(columnWidths, remainingWidth, maxPossibleWidthsNotSpecifiedByUser, largeMaxPossibleWidths);
        }
    }

    private void spreadRemainingWidthOverLargeMaxPossibleWidths(double[] columnWidths, double remainingWidth,
            List<ColumnPossibleWidth> maxPossibleWidthsNotSpecifiedByUser, List<ColumnPossibleWidth> largeMaxPossibleWidths) {
        remainingWidth = addExtraWidthToColumns(columnWidths, remainingWidth, largeMaxPossibleWidths);
        //if anything remains after all required widths have been achieved, simply divide the remainder equally
        spreadRemainingWidth(columnWidths, remainingWidth, maxPossibleWidthsNotSpecifiedByUser);
    }

    private double sumOfLargeMaxPossibleWidths(double[] columnWidths, List<ColumnPossibleWidth> largeMaxPossibleWidths) {
        return largeMaxPossibleWidths.stream()
                .mapToDouble(maxWidth -> (maxWidth.getWidth() - columnWidths[maxWidth.getColumn()]))
                .sum();
    }

    private List<ColumnPossibleWidth> getLargeMaxPossibleWidthsNotSpecifiedByUser(List<ColumnPossibleWidth> maxPossibleWidthsNotSpecifiedByUser,
            List<ColumnPossibleWidth> smallMaxPossibleWidths) {
        return maxPossibleWidthsNotSpecifiedByUser.stream()
                .filter(maxWidth -> !smallMaxPossibleWidths.contains(maxWidth))
                .collect(Collectors.toList());
    }

    private void ReplaceWidthIfNewWidthIsLarger(ColumnPossibleWidth[] possibleWidths, int column, StateCell cell, double newWidth) {
        newWidth = newWidth / cell.getColumnSpan();
        ColumnPossibleWidth possibleWidth = possibleWidths[column];
        if (possibleWidth == null || newWidth > possibleWidth.getWidth()) {
            possibleWidths[column] = new ColumnPossibleWidth(newWidth, column);
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
                    totalWidthRequired = cell.getStateCellContent() != null ? cell.getStateCellContent().getTotalRequiredWidth() + (cell.getPadding() * 2)
                            + cell.getBorderWidth() : 0;
                }
                ReplaceWidthIfNewWidthIsLarger(maxColumnWidthPossible, i, cell, totalWidthRequired);
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

    private double spreadRemainingWidthPercentageWise(double[] columnWidths, double remainingWidth, List<ColumnPossibleWidth> possibleWidths,
            double totalExtraWidthRequired) {
        double increasePerWidthUnit = remainingWidth / totalExtraWidthRequired;
        for (ColumnPossibleWidth possibleWidth : possibleWidths) {
            double columnWidth = columnWidths[possibleWidth.getColumn()];
            double extraWidthRequired = possibleWidth.getWidth() - columnWidth;
            columnWidths[possibleWidth.getColumn()] = columnWidth + (increasePerWidthUnit * extraWidthRequired);
            remainingWidth -= (increasePerWidthUnit * extraWidthRequired);
        }
        return remainingWidth;
    }

    private void spreadRemainingWidth(double[] columnWidths, double remainingWidth, List<ColumnPossibleWidth> maxPossibleWidths) {
        if (remainingWidth > 0) {
            double widthPerColumn = remainingWidth / maxPossibleWidths.size();
            maxPossibleWidths.stream()
                    .filter(expandableColumn -> expandableColumn != null)
                    .forEach(expandableColumn -> columnWidths[expandableColumn.getColumn()] = columnWidths[expandableColumn.getColumn()] + widthPerColumn);
        }
    }


    public List<StateTableRow> removeCellContentWithLargestMinimalWidth(boolean prioritizeHeaderWidth) {
        int endIndex = prioritizeHeaderWidth ? 1 : this.rows.size();

        int rowOfLargestMinRequiredWidth = 0;
        int columnOfLargestMinRequiredWidth = 0;
        double largestMinRequiredWidth = 0;

        for(int i = 0; i < endIndex; ++i) {
            ColumnPossibleWidth[] minColumnWidths = new ColumnPossibleWidth[this.columnAmount];
            determineMinWidthsForRow(minColumnWidths, rows.get(i).getContent());
            for(ColumnPossibleWidth minColumnWidth : minColumnWidths) {
                if(minColumnWidth != null && minColumnWidth.getWidth() > largestMinRequiredWidth) {
                    rowOfLargestMinRequiredWidth = i;
                    columnOfLargestMinRequiredWidth = minColumnWidth.getColumn();
                    largestMinRequiredWidth = minColumnWidth.getWidth();
                }
            }
        }

        StateCell largestMinWidthCell = (StateCell) rows.get(rowOfLargestMinRequiredWidth).getContent()[columnOfLargestMinRequiredWidth];
        StateCellContent largestMinWidthCellContent = (StateCellContent) largestMinWidthCell.getContent();

        if(largestMinWidthCell.getWidth() > 0 && !(largestMinWidthCellContent instanceof StatePlaceableFixedSizeDocumentPart)){
            LOGGER.warn("The minimal required width of the columns is larger than the width of the table. " +
                    "Removing user specified width from table on row " + rowOfLargestMinRequiredWidth +
                    " and column " + columnOfLargestMinRequiredWidth + " and will proceed with recalculation of " +
                    "required width after this removal.");
            largestMinWidthCell.width(0);
        } else {
            LOGGER.warn("The minimal required width of the columns is larger than the width of the table. " +
                    "Removing largest piece of content from the table on row " + rowOfLargestMinRequiredWidth +
                    " and column " + columnOfLargestMinRequiredWidth + " and will proceed with recalculation of " +
                    "required width after this removal.");
            largestMinWidthCell.setContent(null);
        }

        return rows;
    }
}
