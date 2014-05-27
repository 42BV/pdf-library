package nl.mad.toucanpdf.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.FontMetrics;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.model.state.StateCellText;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

public class BaseStateCellText extends AbstractStateSplittableText implements StateCellText {
    private final static int REQUIRED_WIDTH = 50;

    public BaseStateCellText(String text) {
        super(text);
    }

    public BaseStateCellText(Text text) {
        super(text);
    }

    private DocumentPart originalObject;

    @Override
    public double calculateContentHeight(double availableWidth, double leading, Position position, boolean processPositioning) {
        textSplit = new LinkedHashMap<Position, String>();
        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(getText().split(" ")));
        strings.add("");
        leading += getRequiredSpaceBelowLine();
        int textSize = this.getTextSize();
        double width = 0;
        FontMetrics metrics = getFont().getMetrics();
        StringBuilder currentLine = new StringBuilder();
        Position pos = new Position(position);
        pos.adjustY(-marginTop);
        pos.adjustX(marginLeft);
        availableWidth -= marginRight + marginLeft;
        int lineAdditions = 0;

        for (int i = 0; i < strings.size(); ++i) {
            String s = strings.get(i);
            System.out.println("Handling string: " + s);
            double oldWidth = width;
            width += metrics.getWidthPointOfString(s, textSize, true) + (metrics.getWidthPoint("space") * textSize);
            if (FloatEqualityTester.greaterThan(width, availableWidth)) {
                String line = processCutOff(currentLine, s, oldWidth, availableWidth, strings, i);
                System.out.println("What is s: " + s);
                processLineAddition(processPositioning, pos, leading, line, metrics.getWidthPointOfString(line, textSize, true), availableWidth);
                lineAdditions += 1;
                width = 0;
                currentLine = new StringBuilder();
            } else if (i == (strings.size() - 1)) {
                currentLine.append(s);
                System.out.println(currentLine.toString());
                processLineAddition(processPositioning, pos, leading, currentLine.toString(),
                        metrics.getWidthPointOfString(currentLine.toString(), textSize, true), availableWidth);
                lineAdditions += 1;
            } else {
                currentLine.append(s + " ");
            }
        }
        return (lineAdditions * (leading + this.getRequiredSpaceAboveLine() + this.getRequiredSpaceBelowLine())) + marginTop + marginBottom;
    }

    private void processLineAddition(boolean processPositioning, Position pos, double leading, String line, double width, double availableSpace) {
        if (processPositioning) {
            pos.adjustY(-(getRequiredSpaceAboveLine() + leading));
            pos = processAlignment(line, pos, width, availableSpace);
            System.out.println("Putting in textsplit " + line);
            textSplit.put(new Position(pos), line);
            pos.adjustY(-getRequiredSpaceBelowLine());
        }
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
        strings.add(currentTextIndex + 1, currentString.substring(charsAdded, currentString.length()));
        return currentLine.toString();
    }

    /**
     * Processes the alignment of a single given line.
     * @param line Line to process.
     * @param position Position the line is on.
     * @param width Width of the given line.
     * @param openSpaceSize Size of the open space the line is filling.
     * @return a new position object that has been adjusted for the alignment.
     */
    private Position processAlignment(String line, Position position, double width, double openSpaceSize) {
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

    @Override
    public double getRequiredSpaceAbove() {
        return getRequiredSpaceAboveLine() + marginTop;
    }

    @Override
    public double getRequiredSpaceBelow() {
        return getRequiredSpaceBelowLine() + marginBottom;
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
        return this.marginLeft + marginRight + REQUIRED_WIDTH;
    }

    @Override
    public double getRequiredSpaceLeft() {
        return marginLeft;
    }

    @Override
    public double getRequiredSpaceRight() {
        return marginRight;
    }
}
