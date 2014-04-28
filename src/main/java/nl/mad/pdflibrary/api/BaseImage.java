package nl.mad.pdflibrary.api;

import nl.mad.pdflibrary.model.Alignment;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.PlaceableDocumentPart;
import nl.mad.pdflibrary.model.Position;

public class BaseImage extends AbstractPlaceableDocumentPart {
    private int height;
    private int width;

    public BaseImage(int height, int width) {
        super(DocumentPartType.IMAGE);
        this.setPosition(new Position());
        this.height = height;
        this.width = width;
    }

    public BaseImage on(Position position) {
        this.setPosition(position);
        return this;
    }

    public BaseImage align(Alignment align) {
        this.setAlignment(align);
        return this;
    }

    @Override
    public PlaceableDocumentPart copy() {
        return new BaseImage(height, width);
    }

}
