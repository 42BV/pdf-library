package nl.mad.toucanpdf.api;

import java.util.LinkedList;
import java.util.List;

import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Table;

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
        //Cell c = new Cell(part);
        //this.content.add(c);
        return null;
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
}
