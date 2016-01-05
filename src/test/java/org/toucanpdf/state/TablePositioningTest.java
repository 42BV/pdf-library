package org.toucanpdf.state;

import org.toucanpdf.DocumentBuilder;
import org.toucanpdf.api.BaseFont;
import org.toucanpdf.api.BaseText;
import org.toucanpdf.model.Font;
import org.toucanpdf.model.FontFamilyType;
import org.toucanpdf.model.FontStyle;
import org.toucanpdf.state.Table.BaseStateTable;

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
