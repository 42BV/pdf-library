package nl.mad.toucanpdf.state;

import nl.mad.toucanpdf.DocumentBuilder;
import nl.mad.toucanpdf.api.BaseFont;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.FontFamilyType;
import nl.mad.toucanpdf.model.FontStyle;

import org.junit.Before;
import org.junit.Test;

public class TablePositioningTest {

    private BaseStateTable table;
    private Font defaultFont = new BaseFont(FontFamilyType.TIMES_ROMAN, FontStyle.NORMAL);
    private Font boldFont = new BaseFont(FontFamilyType.TIMES_ROMAN, FontStyle.BOLD);
    private Font boldItalicFont = new BaseFont(FontFamilyType.TIMES_ROMAN, FontStyle.BOLDITALIC);

    @Before
    public void setUp() {
        table = new BaseStateTable(DocumentBuilder.A4_WIDTH - 40);
    }

    @Test
    public void testTable1() {
        table.columns(3);

        table.addCell(new BaseText("Teamlid").font(boldItalicFont));
        table.addCell(new BaseText("Ambitie").font(boldItalicFont));
        table.addCell(new BaseText("Score").font(boldItalicFont));

        table.addCell(new BaseText("Marco van Ginkel").font(boldFont));
        table.addCell(new BaseText("Streven naar perfectie in de organisatie").font(defaultFont));
        table.addCell(new BaseText("8").font(defaultFont));

        table.addCell(new BaseText("Marco van Ginkel").font(boldFont));
        table.addCell(new BaseText("Winst maken.").font(defaultFont));
        table.addCell(new BaseText("10").font(defaultFont));

        table.addCell(new BaseText("Marco van Ginkel").font(boldFont));
        table.addCell(new BaseText("Herinvesteren winst voor oudedagsvoorziening.").font(defaultFont));
        table.addCell(new BaseText("7").font(defaultFont));

    }
}
