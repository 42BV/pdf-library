package nl.mad.pdflibrary.api;

import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.model.Alignment;
import nl.mad.pdflibrary.model.DocumentPartType;
import nl.mad.pdflibrary.model.Page;
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

    @Override
    public double getContentHeight(Page page) {
        return this.height;
    }

    @Override
    public int getContentWidth(Page page, Position position) {
        return this.width;
    }

    @Override
    public int[] getPositionAt(double height) {
        return new int[] { (int) this.getPosition().getX() };
    }

    @Override
    public List<int[]> getUsedSpaces(double height) {
        List<int[]> space = new LinkedList<int[]>();
        space.add(new int[] { (int) this.getPosition().getX(), (int) (this.getPosition().getX() + width) });
        return space;
    }

    @Override
    public double getRequiredSpaceAbove() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getRequiredSpaceBelow() {
        return this.height;
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
