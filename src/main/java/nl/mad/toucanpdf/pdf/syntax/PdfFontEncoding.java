package nl.mad.toucanpdf.pdf.syntax;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.mad.toucanpdf.api.DocumentState;
import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.PdfNameValue;

public class PdfFontEncoding extends PdfDictionary {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfFontEncoding.class);
    private static final PdfName type = new PdfName("Encoding");
    private PdfFontDifferences differences = null;

    public PdfFontEncoding(Font font) {
        super(PdfObjectType.DICTIONARY);
        differences = createNewDifferences(font);
        this.put(new PdfName(PdfNameValue.TYPE), type);
    }

    private PdfFontDifferences createNewDifferences(Font font) {
        PdfFontDifferences dif = null;
        switch (font.getFontFamily().getSubType()) {
        case TYPE1:
            dif = new Type1FontDifferences();
            break;
        default:
            LOGGER.warn("The font of the given type: " + font.getFontFamily().getSubType() + " is not supported.");
            break;
        }
        return dif;
    }

    public void updateDifferences(String s) {
        if (differences != null) {
            differences.updateDifferences(s);
        }
    }

    public PdfFontDifferences getEncodingDifferences() {
        return this.differences;
    }

    @Override
    public void writeToFile(OutputStream os) throws IOException {
        if (differences != null) {
            this.addDifferencesEntry();
        }
        super.writeToFile(os);
    }

	private void addDifferencesEntry() {
		PdfArray differencesArray = new PdfArray();
        Entry<String, Integer> previousEntry = null;
        for (Entry<String, Integer> entry : this.differences.getDifferences().entrySet()) {
            if (previousEntry == null || (entry.getValue() - 1) != previousEntry.getValue()) {
                differencesArray.addValue(new PdfNumber(entry.getValue()));
            }
            differencesArray.addValue(new PdfName(entry.getKey()));
            previousEntry = entry;
        }
        this.put(new PdfName(PdfNameValue.DIFFERENCES), differencesArray);
	}
}
