package org.toucanpdf.api;

import java.util.LinkedList;
import java.util.List;

import org.toucanpdf.model.Cell;
import org.toucanpdf.model.PlaceableDocumentPart;
import org.toucanpdf.model.Table;

public class BaseTable extends AbstractTable implements Table {
    private List<Cell> content = new LinkedList<Cell>();

    public BaseTable(int pageWidth) {
        super(pageWidth);
    }

    public BaseTable(Table table) {
        super(table);
        this.content = table.getContent();
    }

    @Override
    public Cell addCell(PlaceableDocumentPart part) {
        Cell c = new BaseCell(part);
        this.content.add(c);
        return c;
    }

    @Override
    public Cell addCell(String s) {
        return this.addCell(new BaseText(s));
    }

    @Override
    public BaseTable addCell(Cell c) {
        this.content.add(c);
        return this;
    }

    @Override
    public List<Cell> getContent() {
        return this.content;
    }

    @Override
    public PlaceableDocumentPart copy() {
        return new BaseTable(this);
    }

    @Override
    public Table removeContent() {
        this.content = new LinkedList<Cell>();
        return this;
    }
}
