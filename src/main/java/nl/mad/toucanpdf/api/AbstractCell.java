package nl.mad.toucanpdf.api;

import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.utility.Constants;

public abstract class AbstractCell implements Cell {
    protected double width = 0;
    protected double height = 0;
    protected int columnSpan = 1;
    protected int rowSpan = 1;
    protected double padding = 5;
    protected Double border;
    protected Position position;

    public AbstractCell() {

    }

    public AbstractCell(Cell c) {
        this.width = c.getWidth();
        this.height = c.getHeight();
        this.columnSpan = c.getColumnSpan();
        this.rowSpan = c.getRowSpan();
        this.position = c.getPosition();
        this.border = c.getBorderWidth();
        this.padding = c.getPadding();
    }

    @Override
    public Cell width(double newWidth) {
        if (newWidth > 0) {
            this.width = newWidth;
        }
        return this;
    }

    @Override
    public Cell height(double newHeight) {
        if (newHeight > 0) {
            this.height = newHeight;
        }
        return this;
    }

    @Override
    public Cell columnSpan(int newColumnSpan) {
        this.columnSpan = newColumnSpan;
        return this;
    }

    @Override
    public Cell rowSpan(int newRowSpan) {
        this.rowSpan = newRowSpan;
        return this;
    }

    @Override
    public Cell border(double borderSize) {
        borderSize = Math.min(borderSize, Constants.MAX_BORDER_SIZE);
        borderSize = Math.max(borderSize, Constants.MIN_BORDER_SIZE);
        this.border = borderSize;
        return this;
    }

    @Override
    public Double getBorderWidth() {
        return this.border;
    }

    @Override
    public int getColumnSpan() {
        return this.columnSpan;
    }

    @Override
    public int getRowSpan() {
        return this.rowSpan;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public double getPadding() {
        return padding;
    }

    @Override
    public Cell padding(double padding) {
        this.padding = padding;
        return this;
    }
}
