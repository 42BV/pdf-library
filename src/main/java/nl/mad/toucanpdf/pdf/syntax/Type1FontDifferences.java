package nl.mad.toucanpdf.pdf.syntax;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.FontMetrics;
import nl.mad.toucanpdf.utility.UnicodeConverter;

public class Type1FontDifferences implements PdfFontDifferences {
    private Map<String, Integer> differences;
    private static final int OCTAL_CODE_CONVERSION = 8;
    private static final int OCTAL_CODE_LENGTH = 3;

    public Type1FontDifferences() {
        differences = new LinkedHashMap<>();
    }

    @Override
    public void updateDifferences(String s) {
        for (int i = 0; i < s.length(); ++i) {
            int charCode = s.codePointAt(i);
            String postscriptName = UnicodeConverter.getPostscriptForUnicode(charCode);
            if (!differences.containsKey(postscriptName)) {
                differences.put(postscriptName, differences.size());
            }
        }
    }

    @Override
    public void insertDifference(String characterName, int characterCode) {
        if (!differences.containsKey(characterName)) {
            differences.put(characterName, characterCode);
        }
    }

    @Override
    public String getNameOf(String octalCode) {
        int code = Integer.parseInt(octalCode, OCTAL_CODE_CONVERSION);
        return getNameOf(code);
    }

    @Override
    public String getNameOf(int code) {
        if (differences.containsValue(code)) {
            for (Entry<String, Integer> entry : differences.entrySet()) {
                if (entry.getValue().equals(code)) {
                    return entry.getKey();
                }
            }
        }
        return "";
    }

    @Override
    public Map<String, Integer> getDifferences() {
        return differences;
    }

    @Override
    public String convertString(String s) {
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            int charCode = s.codePointAt(i);
            String postscriptName = UnicodeConverter.getPostscriptForUnicode(charCode);
            if (differences.containsKey(postscriptName)) {
                int code = differences.get(postscriptName);
                newString.append("\\");
                newString.append(generateStringForDifferenceCode(code));
            }
        }
        return newString.toString();
    }

    private String generateStringForDifferenceCode(int code) {
        StringBuilder stringCode = new StringBuilder(Integer.toString(code, OCTAL_CODE_CONVERSION));
        int codeLength = stringCode.length();
        //fill up the code with 0's in case the length of the code is not equal to the required length
        for (int b = OCTAL_CODE_LENGTH; b > codeLength; --b) {
            stringCode.insert(0, 0);
        }
        return stringCode.toString();
    }

    @Override
    public List<Integer> generateWidthList(Font font) {
        FontMetrics metrics = font.getMetrics();
        return differences.entrySet().stream()
                .map(entry -> metrics.getWidth(entry.getKey()))
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
