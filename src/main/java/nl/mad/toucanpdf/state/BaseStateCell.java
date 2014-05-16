package nl.mad.toucanpdf.state;

import nl.mad.toucanpdf.api.AbstractCell;
import nl.mad.toucanpdf.model.Cell;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.model.state.StateCell;
import nl.mad.toucanpdf.model.state.StateCellContent;

public class BaseStateCell extends AbstractCell implements StateCell {
    private StateCellContent content;

    public BaseStateCell(Cell c) {
        super(c);
        this.content = convertContent(c.getContent());
    }

    public BaseStateCell() {
        super();
    }

    private StateCellContent convertContent(PlaceableDocumentPart part) {
        if (part != null) {
            switch (part.getType()) {
            case TEXT:
                return new BaseStateCellText((Text) part);
            case IMAGE:
                return new BaseStateCellImage((Image) part);
            case PARAGRAPH:
                break;
            case TABLE:
                break;
            default:
                break;
            }
        }
        return null;
    }

    @Override
    public double getRequiredHeight(double leading) {
        if (this.content != null) {
            return Math.max(height, this.content.calculateContentHeight(this.getWidth(), leading, this.getPosition()));
        }
        return height;
    }

    @Override
    public Cell setPosition(Position position) {
        this.position = position;
        return this;
    }

    @Override
    public PlaceableDocumentPart getContent() {
        return this.content;
    }

    @Override
    public StateCellContent getStateCellContent() {
        return this.content;
    }

    @Override
    public double getRequiredWidth() {
        if (this.content != null) {
            return Math.max(width, this.content.getRequiredWidth());
        }
        return width;
    }
}
