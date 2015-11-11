import java.io.IOException;

import nl.mad.toucanpdf.DocumentBuilder;
import nl.mad.toucanpdf.api.BaseFont;
import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.FontFamilyType;
import nl.mad.toucanpdf.model.FontStyle;
import nl.mad.toucanpdf.model.Table;

public class Main {

    /**
     * This is only for testing purposes.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        DocumentBuilder builder = new DocumentBuilder();
        builder.addPage().size(DocumentBuilder.A4_HEIGHT, DocumentBuilder.A4_WIDTH).marginBottom(50).marginTop(50).marginRight(50).marginLeft(50);
        Font def = new BaseFont(FontFamilyType.HELVETICA, FontStyle.NORMAL);
        Font italic = new BaseFont(builder.getDefaultFont()).italic();
        Font bolditalic = new BaseFont(FontFamilyType.HELVETICA, FontStyle.BOLDITALIC);
        Font bold = new BaseFont(builder.getDefaultFont()).bold();

        Table table = builder.addTable();
        table.border(0).padding(1).marginBottom(10).repeatHeader(true).columns(8).prioritizeHeaderWidth(true);

        addHeader(builder, bolditalic, table);
        addTestBody(builder, def, bold, table);

        builder.finish();
    }

    private static void addTestBody(DocumentBuilder builder, Font def, Font bold, Table table) {
        addFirstRow(builder, def, bold, table);
        addSecondRow(builder, def, bold, table);
    }

    private static void addFirstRow(DocumentBuilder builder, Font def, Font bold, Table table) {
        addBodyText("P van Plaats", builder, bold, table);
        addBodyText("Plaats 2.1.1 Onderzoek vaste locatie in eigendom versus verhuur", builder, def, table);
        table.addCell("");
        addBodyText("Pers. 1.1.2 Helder rollen en verantwoordelijkheden communiceren (o.a. speerpunten & gebiedsinformatie)", builder, def, table);
        addBodyText("Pers. 2.1.2 (1) kostencalculatie per project & (2) kostprijs calculatie vooraf", builder, def, table);
        table.addCell("");
        addBodyText("01-04-2015", builder, def, table);
        addBodyText("Alex/Hanneke/Robin", builder, def, table);
    }

    private static void addSecondRow(DocumentBuilder builder, Font def, Font bold, Table table) {
        addBodyText("Financieel op middellange termijn", builder, bold, table);
        addBodyText("Pers. 2.1 Kwaliteit van technische vaardigheden/didactische vaardigheden omhoog", builder, def, table);
        table.addCell("");
        addBodyText("Pers. 2.2 Vanuit stageplan en/of gekwalificeerde instructuers binnenhalen en vasthouden", builder, def, table);
        addBodyText("Pers. 2.1.1 Klanttevredenheidsonderzoek", builder, def, table);
        table.addCell("");
        addBodyText("01-04-2015", builder, def, table);
        addBodyText("Paul/Vivian/Alex/Mathilde", builder, def, table);
    }

    private static void addBodyText(String text, DocumentBuilder builder, Font font, Table table) {
        table.addCell(builder.createText(text).marginBottom(3).marginTop(3).marginRight(4).font(font));
    }

    private static void addHeader(DocumentBuilder builder, Font bolditalic, Table table) {
        addHeaderTextToDocument("Aandachtsgebied", builder, bolditalic, table);
        addHeaderTextToDocument("Strategische hoofddoelstelling", builder, bolditalic, table);
        addHeaderTextToDocument("Strategische subdoelstelling", builder, bolditalic, table);
        addHeaderTextToDocument("Strategische actie", builder, bolditalic, table);
        addHeaderTextToDocument("Meetinstrument", builder, bolditalic, table);
        addHeaderTextToDocument("Weging", builder, bolditalic, table);
        addHeaderTextToDocument("Deadline", builder, bolditalic, table);
        addHeaderTextToDocument("Uitvoerend persoon", builder, bolditalic, table);
    }

    private static void addHeaderTextToDocument(String text, DocumentBuilder builder, Font bolditalic, Table table) {
        table.addCell(builder.createText(text).marginBottom(6).marginRight(2).font(bolditalic)).border(0);
    }
}
