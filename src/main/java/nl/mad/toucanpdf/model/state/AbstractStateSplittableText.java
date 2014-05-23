package nl.mad.toucanpdf.model.state;

import java.util.LinkedHashMap;
import java.util.Map;

import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;

public abstract class AbstractStateSplittableText extends BaseText implements StateSplittableText {
    protected Map<Position, String> textSplit = new LinkedHashMap<Position, String>();
    protected Map<Position, Double> justificationOffset = new LinkedHashMap<Position, Double>();

    public AbstractStateSplittableText(String text) {
        super(text);
    }

    public AbstractStateSplittableText(Text part) {
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
}
