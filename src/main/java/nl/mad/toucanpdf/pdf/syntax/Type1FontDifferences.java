package nl.mad.toucanpdf.pdf.syntax;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.mad.toucanpdf.utility.UnicodeConverter;

public class Type1FontDifferences implements PdfFontDifferences {
    private Map<String, Integer> differences;

    public Type1FontDifferences() {
        differences = new LinkedHashMap<String, Integer>();
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
    public Map<String, Integer> getDifferences() {
        return differences;
    }

    @Override
    public String convertString(String s) {
        for (int i = 0; i < 256; ++i) {
            if (!differences.containsValue(i)) {
                //differences.put(".notdef", i);
            }

        }
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            int charCode = s.codePointAt(i);
            System.out.println("Char at i = " + s.charAt(i) + ", charCode = " + s.codePointAt(i));
            String postscriptName = UnicodeConverter.getPostscriptForUnicode(charCode);
            System.out.println("Postscriptname = " + postscriptName);
            if (differences.containsKey(postscriptName)) {
                int code = differences.get(postscriptName);
                StringBuilder sCode = new StringBuilder(String.valueOf(code));
                System.out.println("Scode length: " + sCode.toString().length());
                int sCodeLength = sCode.length();
                for (int b = 3; b > sCodeLength; --b) {
                    //sCode.insert(0, 0);
                }
                // String bab = sCode.toString();
                //char[] chars = bab.toCharArray();
                //for (char c : chars) {
                newString.append("\\");
                newString.append(code);
                //}
            } else {
                //newString.append(s.charAt(i));
            }
        }
        for (Entry<String, Integer> entry : differences.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println(newString.toString());
        return newString.toString();
    }
}
