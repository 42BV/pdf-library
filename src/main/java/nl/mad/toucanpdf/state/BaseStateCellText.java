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

public class BaseStateCellText extends AbstractStateText implements StateCellText {
    private final static int REQUIRED_WIDTH = 10;
    private final static int DEFAULT_TOTAL_WIDTH = 2;
    private DocumentPart originalObject;
    private double height = 0;

    public BaseStateCellText(String text) {
        super(text);
    }

    public BaseStateCellText(Text text) {
        super(text);
        if (text instanceof BaseStateText) {
            this.originalObject = ((BaseStateText) text).getOriginalObject();
        }
    }

    @Override
    public double calculateContentHeight(double availableWidth, double leading, Position position, boolean processPositioning) {
        textSplit = new LinkedHashMap<Position, String>();
        ArrayList<String> strings = new ArrayList<String>(Arrays.asList(getText().split(" ")));
        strings.add("");
        int textSize = this.getTextSize();
        double width = 0;
        FontMetrics metrics = getFont().getMetrics();
        StringBuilder currentLine = new StringBuilder();
        Position pos = SetupPosition(position);
        availableWidth -= marginRight + marginLeft;
        int lineAdditions = 0;
        String firstLine = null;
        String lastLine = null;
        double spaceSize = (metrics.getWidthPoint("space") * textSize);

        for (int i = 0; i < strings.size(); ++i) {
            String s = strings.get(i);
            double oldWidth = width;
            double stringWidth = metrics.getWidthPointOfString(s, textSize, true);
            if (currentLine.length() != 0) {
                width += spaceSize;
            }
            width += stringWidth;
            if (FloatEqualityTester.greaterThan(width, availableWidth)) {
                String line = currentLine.toString();
                //if the current word is actually larger than the total available width, cut it off
                if (FloatEqualityTester.greaterThan(stringWidth, availableWidth)) {
                    line = processCutOff(currentLine, s, oldWidth, availableWidth, strings, i);
                } else {
                    //else simply move this word to the next line
                    strings.add(i + 1, s);
                }
                if (lineAdditions == 0) {
                    firstLine = line;
                }
                lineAdditions += 1;
                processLineAddition(processPositioning, pos, leading, line, metrics.getWidthPointOfString(line, textSize, true), availableWidth);
                width = 0;
                currentLine = new StringBuilder();
            } else if (i == (strings.size() - 1) && (currentLine.length() != 0 || !s.isEmpty())) {
                currentLine.append(s);
                lineAdditions += 1;
                lastLine = currentLine.toString();
                processLineAddition(processPositioning, pos, leading, lastLine,
                        metrics.getWidthPointOfString(currentLine.toString(), textSize, true), availableWidth);
            } else {
                currentLine.append(s + " ");
            }
        }
        //content height is equal to the amount of lines times leading and margins, we have to deduct leading once because the first line does not have leading
        return determineTotalContentHeight(lineAdditions, metrics, firstLine, lastLine);
    }

    private Position SetupPosition(Position position) {
        Position pos = new Position(position);
        pos.adjustY(-marginTop);
        pos.adjustX(marginLeft);
        return pos;
    }

    @Override
    public void processVerticalAlignment(double height) {
        //we want to use general font values in this calculation to make sure single lines of text are placed on the same baseline. 
        //If we're using the ascent/descent of individual lines to determine vertical alignment, the text will always be slightly off due to some lines being higher
        double diff = (height - marginTop - marginBottom) - determineContentHeightWithGeneralFontValues(this.textSplit.size(), this.getFont().getMetrics());
        double yAdjustment = diff / 2;
        if (this.textSplit.size() > 0 && diff > 0) {
            for (Position p : textSplit.keySet()) {
                p.adjustY(-yAdjustment);
            }
        }
    }

    private double determineContentHeightWithGeneralFontValues(int lines, FontMetrics metrics) {
        int size = this.getTextSize();
        return lines * ((metrics.getAscentPoint() * size) + Math.abs(metrics.getDescentPoint() * size));
    }

    private double determineTotalContentHeight(int lineAdditions, FontMetrics metrics, String first, String last) {
        double contentHeight = marginTop + marginBottom;
        if (lineAdditions > 0) {
            if (last == null && first != null) {
                last = first;
            } else if (first == null && last != null) {
                first = last;
            }
            double ascent = metrics.getAscentForStringPoint(first) * this.getTextSize();
            double descent = Math.abs(metrics.getDescentForStringPoint(last) * this.getTextSize());
            contentHeight += ascent + descent;

            if (lineAdditions > 1) {
                contentHeight += (((lineAdditions - 1) * (this.getRequiredSpaceAboveLine() + this.getRequiredSpaceBelowLine()))) + lineAdditions;
            }
        }
        height = contentHeight;
        return contentHeight;
    }

    private void processLineAddition(boolean processPositioning, Position pos, double leading, String line, double width, double availableSpace) {
        if (processPositioning) {
            double yAdjustment = textSplit.size() > 0 ? -(getRequiredSpaceAboveLine() + leading) : -getRequiredSpaceAboveLine();
            pos.adjustY(yAdjustment);
            pos = processAlignment(line, pos, width, availableSpace);
            textSplit.put(new Position(pos), line);
            pos.adjustY(-getRequiredSpaceBelowLine());
        }
    }

    private String processCutOff(StringBuilder currentLine, String currentString, double currentWidth, double availableWidth, List<String> strings,
            int currentTextIndex) {
        char[] chars = currentString.toCharArray();
        int i = 0;

        int charsAdded = 0;
        while (currentWidth < availableWidth && i < chars.length) {
            char c = chars[i];
            Character next = (i + 1 != chars.length) ? chars[i + 1] : null;

            double characterSize = getCharacterSize(c, next);
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

    private double getCharacterSize(char character, Character nextCharacter) {
        FontMetrics metrics = getFont().getMetrics();
        int textSize = getTextSize();

        double characterSize;
        if (nextCharacter != null) {
            characterSize = (((metrics.getWidth(character) - metrics.getKerning(character, nextCharacter)) * textSize) * metrics.getConversionToPointsValue());
        } else {
            characterSize = metrics.getWidthPoint(character) * textSize;
        }
        return characterSize;
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
    public double getSpecifiedWidth() {
        return 0;
    }

    @Override
    public double getTotalRequiredWidth() {
        char[] characters = this.getText().toCharArray();
        double totalWidthRequired = DEFAULT_TOTAL_WIDTH + marginLeft + marginRight;

        for (int i = 0; i < characters.length; ++i) {
            Character next = (i + 1 != characters.length) ? characters[i + 1] : null;
            totalWidthRequired += getCharacterSize(characters[i], next);
        }
        return totalWidthRequired;
    }

    @Override
    public double getMinimumWidth() {
        FontMetrics metrics = getFont().getMetrics();
        String[] words = this.getText().split(" ");
        double largestWordLength = 0;
        for (String word : words) {
            double length = metrics.getWidthPointOfString(word, this.getTextSize(), true);
            if (length > largestWordLength) {
                largestWordLength = length;
            }
        }
        return largestWordLength + marginLeft + marginRight;
    }
}
