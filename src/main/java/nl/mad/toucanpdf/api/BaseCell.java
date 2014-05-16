package nl.mad.toucanpdf.api;

import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;

public class BaseCell extends AbstractCell implements Cell {
    private PlaceableDocumentPart content;

    public BaseCell(PlaceableDocumentPart content) {
        this.content = content;
    }

    public BaseCell(Cell c) {
        super(c);
        this.content = c.getContent();
    }

    public BaseCell() {

    }

    @Override
    public PlaceableDocumentPart getContent() {
        return content;
    }

}
