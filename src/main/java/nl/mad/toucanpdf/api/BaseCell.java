package nl.mad.toucanpdf.api;

import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;

/**
 * BaseCell is the default implementation of the cell interface. Instances of this class can be added to tables.
 * @author Dylan
 *
 */
public class BaseCell extends AbstractCell implements Cell {
    private PlaceableDocumentPart content;

    /**
     * Creates a new instance of BaseCell.
     * @param content The content of the cell.
     */
    public BaseCell(PlaceableDocumentPart content) {
        super();
        this.content = content;
    }

    /**
     * Creates a copy of the given cell object.
     * @param c Cell to copy.
     */
    public BaseCell(Cell c) {
        super(c);
        this.content = c.getContent();
    }

    /**
     * Creates a new BaseCell instance.
     */
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
