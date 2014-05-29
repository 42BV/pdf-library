package nl.mad.toucanpdf.pdf.syntax;

import java.util.List;
import java.util.Map;

import nl.mad.toucanpdf.model.Font;

public interface PdfFontDifferences {

    void updateDifferences(String s);

    Map<String, Integer> getDifferences();

    String convertString(String s);

	void insertDifference(String characterName, int characterCode);

	List<Integer> generateWidthList(Font font);

	String getNameOf(int code);

	String getNameOf(String octalCode);
}
