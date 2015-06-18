package nl.mad.toucanpdf.api;

import java.text.DecimalFormat;

import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Compression;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Table;

public abstract class AbstractTable extends AbstractPlaceableFixedSizeDocumentPart implements Table {
    protected int columnAmount = 1;
    protected Compression compression;
    protected double borderWidth = 1.0;
    private boolean drawFillerCells = true;

    public AbstractTable(int pageWidth) {
        super(DocumentPartType.TABLE);
        height = 0;
        width = pageWidth;
    }

    public AbstractTable(Table table) {
        super(DocumentPartType.TABLE);
        this.width = table.getWidth();
        this.height = table.getHeight();
        this.columnAmount = table.getColumnAmount();
        this.compression = table.getCompressionMethod();
        this.wrappingAllowed = table.isWrappingAllowed();
        this.borderWidth = table.getBorderWidth();
        this.align(table.getAlignment());
        this.setPosition(table.getPosition());
        this.marginBottom = table.getMarginBottom();
        this.marginLeft = table.getMarginLeft();
        this.marginTop = table.getMarginTop();
        this.marginRight = table.getMarginRight();
        this.drawFillerCells = table.getDrawFiller();
    }

    @Override
    public Table columns(int amountOfColumns) {
        this.columnAmount = amountOfColumns;
        return this;
    }

    @Override
    public Table align(Alignment alignment) {
        this.setAlignment(alignment);
        return this;
    }

    @Override
    public Table width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public Table on(int x, int y) {
        return on(new Position(x, y));
    }

    @Override
    public Table on(Position position) {
        this.setPosition(position);
        return this;
    }

    @Override
    public Table compress(Compression method) {
        this.compression = method;
        return this;
    }

    @Override
    public Compression getCompressionMethod() {
        return this.compression;
    }

    @Override
    public Table allowWrapping(boolean isWrappable) {
        this.setWrappingAllowed(isWrappable);
        return this;
    }

    @Override
    public int getColumnAmount() {
        return this.columnAmount;
    }

    @Override
    public Table border(double border) {
        DecimalFormat df = new DecimalFormat("0.0");
        String newBorder = df.format(border);
        newBorder = newBorder.replace(',', '.');
        this.borderWidth = Math.max(0, Double.parseDouble(newBorder));
        return this;
    }

    @Override
    public double getBorderWidth() {
        return this.borderWidth;
    }

    @Override
    public Table marginTop(int marginTop) {
        this.setMarginTop(marginTop);
        return this;
    }

    @Override
    public Table marginBottom(int marginBottom) {
        this.setMarginBottom(marginBottom);
        return this;
    }

    @Override
    public Table marginRight(int marginRight) {
        this.setMarginRight(marginRight);
        return this;
    }

    @Override
    public Table marginLeft(int marginLeft) {
        this.setMarginLeft(marginLeft);
        return this;
    }

    @Override
    public Table drawFillerCells(boolean draw) {
        this.drawFillerCells = draw;
        return this;
    }

    @Override
    public boolean getDrawFiller() {
        return this.drawFillerCells;
    }
}
