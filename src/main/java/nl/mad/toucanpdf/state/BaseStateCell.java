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
    private boolean filler;

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
            return Math.max(height, this.content.calculateContentHeight(this.getWidth(), leading, this.getPosition(), false));
        }
        return height;
    }
    
    @Override
    public void processContentSize(double leading) {
    	if(this.content != null) {
    		this.content.calculateContentHeight(this.getWidth(), leading, this.getPosition(), true);
    	}
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
    
    @Override
    public boolean isFiller() {
    	return this.filler;
    }
    
    @Override
    public Cell setFiller(boolean filler) {
    	this.filler = filler;
		return this;
    }
    
    @Override
    public void setContent(StateCellContent content) {
    	this.content = content;
    }
}
