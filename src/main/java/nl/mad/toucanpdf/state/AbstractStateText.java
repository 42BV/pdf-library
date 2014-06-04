package nl.mad.toucanpdf.state;

import java.util.LinkedHashMap;
import java.util.Map;

import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.model.state.StateSpacing;
import nl.mad.toucanpdf.model.state.StateSplittableText;

public abstract class AbstractStateText extends BaseText implements StateSplittableText, StateSpacing {
    protected Map<Position, String> textSplit = new LinkedHashMap<Position, String>();
    protected Map<Position, Double> justificationOffset = new LinkedHashMap<Position, Double>();

    public AbstractStateText(String text) {
        super(text);
    }

    public AbstractStateText(Text part) {
        super(part);
    }

    @Override
    public Map<Position, String> getTextSplit() {
        return this.textSplit;
    }

    @Override
    public Map<Position, Double> getJustificationOffset() {
        return this.justificationOffset;
    }

    @Override
    public double getRequiredSpaceAboveLine() {
        return (getFont().getMetrics().getAscentPoint() * getTextSize());
    }

    @Override
    public double getRequiredSpaceBelowLine() {
        return Math.abs(getFont().getMetrics().getDescentPoint() * getTextSize());
    }

    @Override
    public double getRequiredSpaceLeft() {
        return marginLeft;
    }

    @Override
    public double getRequiredSpaceRight() {
        return marginRight;
    }

    @Override
    public double getRequiredSpaceAbove() {
        return this.getRequiredSpaceAboveLine() + marginTop;
    }

    @Override
    public double getRequiredSpaceBelow() {
        return this.getRequiredSpaceBelowLine() + marginBottom;
    }

    /**
     * Processes the alignment of a single given line.
     * @param line Line to process.
     * @param position Position the line is on.
     * @param width Width of the given line.
     * @param openSpaceSize Size of the open space the line is filling.
     * @return a new position object that has been adjusted for the alignment.
     */
    protected Position processAlignment(String line, Position position, double width, double openSpaceSize) {
        Position newPos = new Position(position);
        double remainingWidth = openSpaceSize - width;
        switch (getAlignment()) {
        case RIGHT:
            newPos.setX(position.getX() + remainingWidth);
            break;
        case CENTERED:
            newPos.setX(position.getX() + (remainingWidth / 2));
            break;
        case JUSTIFIED:
            int wordAmount = Math.max((line.split(" ").length - 1), 0);
            double offset = remainingWidth / wordAmount;
            justificationOffset.put(newPos, offset);
            break;
        default:
            break;
        }
        return newPos;
    }
}
