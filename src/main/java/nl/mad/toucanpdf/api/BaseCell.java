package nl.mad.toucanpdf.api;

import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;

public class BaseCell extends AbstractCell implements Cell {
    private PlaceableDocumentPart content;

    public BaseCell(PlaceableDocumentPart content) {
        super();
        this.content = content;
    }

    public BaseCell(Cell c) {
        super(c);
        this.content = c.getContent();
    }

    public BaseCell() {
        super();
    }

    @Override
    public PlaceableDocumentPart getContent() {
        return content;
    }
    
    @Override
    public Cell content(PlaceableDocumentPart part) {
    	this.content = part;
    	return this;
    }

}
