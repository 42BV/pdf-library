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
	public boolean processContentSize(StatePage page) {
		return this.processContentSize(page, this.wrappingAllowed(), true);
	}

	@Override
	public boolean processContentSize(StatePage page, boolean wrapping,
			boolean processAlignment) {
		return this.processContentSize(page, wrapping, processAlignment, true);
	}

	public boolean processContentSize(StatePage page, boolean wrapping,
			boolean processAlignment, boolean processPositioning) {
		int columnAmount = this.getColumnAmount();
		List<StateCell> content = new LinkedList<StateCell>(this.content);
		double cellWidth = (double) this.getWidth() / columnAmount;
		double availableWidth = this.width;
		List<StateCell> currentRowCells = new LinkedList<StateCell>();
		int currentFilledColumns = 0;
		double rowHeight = 0;
		Position p = calculatePosition(page);
		if (p == null) {
			return true;
		}
		this.height = 0;
		Position cellPos = new Position(this.getPosition());

		// TODO: Support custom widths
		for (int i = 0; i < content.size(); ++i) {
			System.out.println("Copntent size: " + content.size());
			cellWidth = availableWidth / (columnAmount - currentFilledColumns);
			System.out.println("CellWidth : " + cellWidth
					+ ", availableWidth: " + availableWidth
					+ ", columnAmount: " + columnAmount + ", currentFilled: "
					+ currentFilledColumns);
			StateCell c = content.get(i);
			c.columnSpan(Math.min(columnAmount, c.getColumnSpan()));
			int columns = c.getColumnSpan();
			this.checkCellHeight(c, page);

			if (currentFilledColumns + columns <= columnAmount) {
				double reqWidth = calculateWidth(c.getRequiredWidth(),
						availableWidth, cellWidth, c);
				System.out.println("Required width: " + reqWidth);
				availableWidth -= reqWidth;
				processCellAddition(c, cellPos, currentRowCells, reqWidth,
						rowHeight, processPositioning);
				rowHeight = Math.max(rowHeight,
						c.getRequiredHeight(page.getLeading()));
				currentFilledColumns += columns;
			} else {
				if(processPositioning) {
				fillRemainderOfRow(rowHeight, cellWidth, columnAmount
						- currentFilledColumns, cellPos);
				}
				processRow(currentRowCells, rowHeight);
				this.height += rowHeight;
				currentFilledColumns = columns;
				currentRowCells = new LinkedList<StateCell>();
				cellPos.setX(this.getPosition().getX());
				cellPos.setY(cellPos.getY() - rowHeight);
				availableWidth = this.width;
				double reqWidth = calculateWidth(c.getRequiredWidth(),
						availableWidth, availableWidth / columnAmount, c);
				availableWidth -= reqWidth;
				processCellAddition(c, cellPos, currentRowCells, reqWidth,
						rowHeight, processPositioning);
				rowHeight = c.getRequiredHeight(page.getLeading());
			}
			if (processPositioning) {
				c.processContentSize(page.getLeading());
			}

			if (i == (content.size() - 1)) {
				if(processPositioning) {
				fillRemainderOfRow(rowHeight, cellWidth, columnAmount
						- currentFilledColumns, cellPos);
				}
				processRow(currentRowCells, rowHeight);
				this.height += rowHeight;
			}
		}
		return false;
	}

	private void checkCellHeight(StateCell c, StatePage page) {
		if(c.getContent() != null) {
			double height = c.getRequiredHeight(page.getLeading());
			if(height > page.getHeightWithoutMargins()) {
				c.setContent(null);
				//TODO: LOG content too large
			}
		}
	}

	private Position calculatePosition(StatePage page) {
		double requiredSpaceAbove = this.getRequiredSpaceAbove();
		double requiredSpaceBelow = this.getRequiredSpaceBelow();
		Position pos = new Position(this.getPosition());
		List<int[]> openSpaces = page.getOpenSpacesIncludingHeight(pos, true,
				this.getRequiredSpaceAbove(), this.getRequiredSpaceBelow());
		boolean tablePositioned = false;
		while (pos != null && !tablePositioned) {
			int i = 0;
			System.out.println(openSpaces);
			while (!tablePositioned && i < openSpaces.size()) {
				int[] openSpace = openSpaces.get(i);
				if (pos.getX() < openSpace[0]) {
					pos.setX(openSpace[0]);
				}
				int openSpaceWidth = (openSpace[1] - openSpace[0]);
				System.out.println(openSpaceWidth);
				if (openSpaceWidth >= this.getWidth()
						&& openSpace[2] >= this.getHeight()) {
					tablePositioned = true;
					this.setPosition(pos);
				}
				++i;
			}
			System.out.println(pos);
			requiredSpaceAbove += page.getLeading();
			pos = page.getOpenPosition(requiredSpaceAbove, requiredSpaceBelow,
					this.width);
			if (pos != null) {
				openSpaces = page.getOpenSpacesIncludingHeight(pos, true,
						this.getRequiredSpaceAbove(),
						this.getRequiredSpaceBelow());
			}
		}
		return pos;
	}

	private void processCellAddition(StateCell c, Position cellPos,
			List<StateCell> currentRowCells, double reqWidth, double rowHeight,
			boolean processPositioning) {
		if (processPositioning) {
			c.width(reqWidth);
			c.setPosition(new Position(cellPos));
			currentRowCells.add(c);
			cellPos.adjustX(reqWidth);
		}
	}

	private double calculateWidth(double requiredWidth, double availableWidth,
			double cellWidth, StateCell c) {
		double defaultWidth = cellWidth * c.getColumnSpan();
		if (requiredWidth != 0) {
			if (requiredWidth > availableWidth) {
				// TODO: LOG & handle the overflow, just returning default will likely cause issues when actually placing the content
				c.setContent(null);
				return defaultWidth;
			}
			return requiredWidth;
		} else {
			return defaultWidth;
		}
	}

	private List<Cell> fillRemainderOfRow(double rowHeight, double cellWidth,
			int remainingColumns, Position pos) {
		if (remainingColumns > 0) {
			for (int i = 0; i < remainingColumns; ++i) {
				StateCell c = new BaseStateCell();
				c.setPosition(new Position(pos.getX(), pos.getY()));
				c.setFiller(true);
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
		page.setFilledHeight(page.getFilledHeight() + this.getHeight()
				+ Page.DEFAULT_NEW_LINE_SIZE * 2);
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
		this.processContentSize(page, false, false, false);
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
