package nl.mad.toucanpdf.pdf.syntax;

import java.util.Map;

public interface PdfFontDifferences {

    void updateDifferences(String s);

    Map<String, Integer> getDifferences();

    String convertString(String s);
}
