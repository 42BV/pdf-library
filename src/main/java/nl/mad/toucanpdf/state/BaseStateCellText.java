package nl.mad.toucanpdf.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.FontMetrics;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.model.state.AbstractStateSplittableText;
import nl.mad.toucanpdf.model.state.StateCellText;

public class BaseStateCellText extends AbstractStateSplittableText implements StateCellText {

    public BaseStateCellText(String text) {
        super(text);
    }

    public BaseStateCellText(Text text) {
        super(text);
    }

    private DocumentPart originalObject;

    @Override
    public double calculateContentHeight(double availableWidth, double leading, Position position) {
        textSplit = new LinkedHashMap<Position, String>();
        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(getText().split(" ")));
        leading += getRequiredSpaceBelow();
        int textSize = this.getTextSize();
        double width = 0;
        FontMetrics metrics = getFont().getMetrics();
        StringBuilder currentLine = new StringBuilder();
        Position pos = new Position(position);

        for (int i = 0; i < strings.size(); ++i) {
            String s = strings.get(i);
            double oldWidth = width;
            width += metrics.getWidthPointOfString(s, textSize, true) + (metrics.getWidthPoint("space") * textSize);
            System.out.println("Width = " + width);
            if (width > availableWidth) {
                String line = processCutOff(currentLine, s, oldWidth, availableWidth, strings, i);
                pos.adjustY(-(getRequiredSpaceAbove() + leading));
                textSplit.put(new Position(pos.getX(), pos.getY()), line);
                pos.adjustY(-getRequiredSpaceBelow());
                width = 0;
                currentLine = new StringBuilder();
            } else if (i == (strings.size() - 1)) {
                currentLine.append(s);
                pos.adjustY(-(getRequiredSpaceAbove() + leading));
                textSplit.put(new Position(pos.getX(), pos.getY()), currentLine.toString());
                pos.adjustY(-getRequiredSpaceBelow());
            } else {
                currentLine.append(s + " ");
            }
        }
        return textSplit.size() * (leading + this.getRequiredSpaceAbove() + this.getRequiredSpaceBelow());
    }

    private String processCutOff(StringBuilder currentLine, String currentString, double currentWidth, double availableWidth, List<String> strings,
            int currentTextIndex) {
        char[] chars = currentString.toCharArray();
        FontMetrics metrics = getFont().getMetrics();
        int i = 0;
        int charsAdded = 0;
        int textSize = getTextSize();

        while (currentWidth < availableWidth && i < chars.length) {
            char c = chars[i];
            double characterSize = 0.0;
            if (i + 1 != chars.length) {
                characterSize = (((metrics.getWidth(c) - metrics.getKerning(c, chars[i + 1])) * textSize) * metrics.getConversionToPointsValue());
            } else {
                characterSize = metrics.getWidthPoint(c) * textSize;
            }
            currentWidth += characterSize;

            if (currentWidth < availableWidth) {
                currentLine.append(c);
                ++charsAdded;
            }
            ++i;
        }
        if (charsAdded > 0) {
            strings.add(currentTextIndex + 1, currentString.substring(charsAdded, currentString.length()));
        }
        System.out.println(currentLine.toString());
        return currentLine.toString();
    }

    @Override
    public double getRequiredSpaceAbove() {
        return getFont().getMetrics().getAscentPoint() * getTextSize();
    }

    @Override
    public double getRequiredSpaceBelow() {
        return Math.abs(getFont().getMetrics().getDescentPoint() * getTextSize());
    }

    @Override
    public void setOriginalObject(DocumentPart originalObject) {
        if (this.originalObject == null) {
            this.originalObject = originalObject;
        }
    }

    @Override
    public DocumentPart getOriginalObject() {
        return this.originalObject;
    }

    @Override
    public double getRequiredWidth() {
        return 0;
    }
}
