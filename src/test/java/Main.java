import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.mad.toucanpdf.DocumentBuilder;
import nl.mad.toucanpdf.api.BaseCell;
import nl.mad.toucanpdf.api.BaseFont;
import nl.mad.toucanpdf.api.BaseImage;
import nl.mad.toucanpdf.api.BaseTable;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.api.DocumentState;
import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Font;
import nl.mad.toucanpdf.model.FontFamilyType;
import nl.mad.toucanpdf.model.FontStyle;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.Table;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.utility.UnicodeConverter;

import org.apache.log4j.BasicConfigurator;

public class Main {

    /**
     * This is only for testing purposes.
     * @param args 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        //newTable();
        testExampleTable();
        //testFootersHeaders();
        //testUnicodeConverter();
        //tableMain();
        //        presentation1();
        //        presentationN();
        //presentationN2();
        //        presentationN3();
        //presentation1();
        //newTest();
        //documentStateTest();
    }

    private static void newTable() {
        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder();
        builder.addPage().marginTop(200);
        Table table = builder.addTable().columns(4).drawFillerCells(false);
        Text t = builder.createText("jantje");
        table.addCell(new BaseCell(t).rowSpan(2));
        table.addCell(new BaseCell(t));
        table.addCell(new BaseCell(t));
        table.addCell(new BaseCell(t));
        table.addCell(new BaseCell(t));
        table.addCell(new BaseCell(t).rowSpan(2).columnSpan(2));
        table.addCell(new BaseCell(t));
        table.addCell(new BaseCell(t));
        builder.finish();
    }

    private static void testExampleTable() {
        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder().filename("jantje");
        builder.addPage().marginTop(20).marginLeft(20).marginRight(20).marginBottom(20);
        Table table = builder.addTable().columns(3).drawFillerCells(false).border(5);
        Font bold = new BaseFont(builder.getDefaultFont()).bold();
        builder.setDefaultMarginLeft(2);
        table.addCell(builder.createText("Teamlid").font(bold));
        table.addCell(builder.createText("Ambitie").font(bold));
        table.addCell(builder.createText("Score").font(bold));
        table.addCell(builder.createText("Erwin"));
        table.addCell(builder.createText("Alle medewerkers blijven in 2014 in dienst. er wordt een passende functie geboden"));
        table.addCell(builder.createText("6"));
        table.addCell(builder.createText("Erwin"));
        table.addCell(builder.createText("Ambitionplanner omzet € 50.000"));
        table.addCell(builder.createText("8"));
        table.addCell(builder.createText("Erwin"));
        table.addCell(builder.createText("Website wordt vernieuwd en ondersteunt daarmee doelstellingen"));
        table.addCell(builder.createText("8"));
        table.addCell(builder.createText("Hans-Peter"));
        table.addCell(builder.createText("tussen nu en 3 jaar omzetgroei naar 3 mio, ofwel per jaar 150.000"));
        table.addCell(builder.createText("10"));
        builder.finish();
    }

    private static void testFootersHeaders() {
        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("hf");
        Page masterPage = builder.createPage().marginTop(50);
        masterPage.addHeader().addAttribute("pageNumber", "1").add(new BaseText("Page %pageNumber of %totalPages").on(0, 810));
        Page page = builder.addPage().master(masterPage);
        DocumentState state = builder.getPreview();
        System.out.println(state.getPages().get(0).getHeader().getHeight());
        System.out.println(state.getPages().get(0).getHeader().getContent().get(0).getType());
        builder.finish();
    }

    private static void testUnicodeConverter() {
        System.out.println();
        System.out.println(UnicodeConverter.getPostscriptForUnicode((int) ' '));
        System.out.println(0x0020);
        Font font = new BaseFont(FontFamilyType.HELVETICA, FontStyle.NORMAL);
        System.out.println(font.getMetrics().getKerning((int) 'A', (int) 'C'));

        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("testttt");
        builder.addText("ACbcdefghijklmnopqrstuvwxyz12jantje\\").font(new BaseFont(FontFamilyType.HELVETICA, FontStyle.NORMAL));
        builder.finish();

    }

    private static void tableMain() {
        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder();
        Table table = builder.addTable().columns(2).drawFillerCells(false);
        table.addCell(new BaseCell().height(100).width(100).columnSpan(2));
        table.addCell(new BaseCell(new BaseText("Hellooooooooooooooooooooooooooo")).columnSpan(5));
        builder.finish();
    }

    private static void presentationN() {
        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("presLines");
        Table table = builder.addTable().columns(6).align(Alignment.CENTERED);
        table.addCell("Cell 1").columnSpan(3);
        table.addCell("Cell 2").columnSpan(2);
        table.addCell("Cell 3").columnSpan(1);
        table.addCell("Cell 4").columnSpan(1);
        table.addCell("Cell 5").columnSpan(1);
        table.addCell("Cell 6").columnSpan(1);
        table.addCell("Cell 7").columnSpan(1);
        table.addCell("Cell 8").columnSpan(1);
        table.addCell("Cell 9").columnSpan(1);
        builder.finish();

    }

    private static void presentationN2() throws FileNotFoundException {
        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder();
        Page page = builder.addPage().marginBottom(20).marginLeft(20).marginRight(20).marginTop(20);
        page.addHeader().addAttribute("title", "Header text").height(20).add(new BaseText("This is %title").on(20, 830));
        page.addFooter().height(100).add(new BaseText("Page %pageNumber of %totalPages").on(page.getWidth() - page.getMarginRight() - 100, 10))
                .add(new BaseTable(page.getWidthWithoutMargins()).addCell(new BaseCell(new BaseText("Cell text %pageNumber")).columnSpan(3)).on(0, 30));
        builder.setDefaultMarginBottom(0);
        builder.title("presMargins");
        builder.addText(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc id diam nec sapien consequat placerat. Curabitur fermentum lectus sed neque ultricies lobortis. Morbi a metus ut lectus pretium malesuada. Duis et nisl vel nisi interdum auctor. Duis quis dui nec orci fermentum venenatis. Quisque et nibh tristique, ullamcorper risus vel, molestie erat. Nullam vestibulum, purus a congue mollis, elit neque egestas mauris, vitae blandit sapien turpis vitae velit. Aliquam erat volutpat. Curabitur at accumsan massa. Proin gravida, sem ut feugiat accumsan, est mi blandit orci, at posuere dolor nunc sit amet eros. Maecenas est arcu, pretium et urna a, volutpat lobortis purus. Nullam varius magna odio, vitae dictum ligula sollicitudin sed. ")
                .marginBottom(0);
        Table table = builder.addTable().columns(6).align(Alignment.CENTERED).marginBottom(10).drawFillerCells(true);
        table.addCell("Cell 1").columnSpan(3);
        table.addCell("Cell 2").columnSpan(2);
        table.addCell("Cell 3").columnSpan(1);
        table.addCell("Cell 4").columnSpan(1);
        table.addCell("Cell 5").columnSpan(1);
        table.addCell("Cell 6").columnSpan(1);
        table.addCell("Cell 7").columnSpan(1);
        table.addCell("Cell 8").columnSpan(1);
        Image i1 = new BaseImage(new FileInputStream("B:/quickie-jpeg-100.jpg"), "001.jpg").allowWrapping(false).align(Alignment.CENTERED).width(100)
                .height(400).marginBottom(10).invertColors(false);
        builder.addPart(i1);
        builder.addText("Donec lorem massa, fermentum volutpat tincidunt sed, euismod ac felis. Morbi sagittis lobortis porta. Suspendisse condimentum magna nisi, quis pulvinar metus varius vitae. Fusce ut fermentum eros. Sed blandit varius ultricies. Nam fringilla, sapien id pellentesque accumsan, urna orci vehicula lorem, hendrerit mattis odio magna id massa. Suspendisse placerat nulla at ornare consequat. Pellentesque posuere quis orci vel tincidunt. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Suspendisse auctor neque a lacus egestas imperdiet in eu tellus. Proin diam dolor, mollis vel varius in, mollis sed felis. Proin sit amet luctus quam. ");
        builder.finish();
    }

    private static void presentationN3() throws FileNotFoundException {
        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder();
        builder.addPage().marginBottom(20).marginLeft(20).marginRight(20).marginTop(20);
        builder.setDefaultMarginBottom(0);
        builder.title("presMargins2");
        builder.addText(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc id diam nec sapien consequat placerat. Curabitur fermentum lectus sed neque ultricies lobortis. Morbi a metus ut lectus pretium malesuada. Duis et nisl vel nisi interdum auctor. Duis quis dui nec orci fermentum venenatis. Quisque et nibh tristique, ullamcorper risus vel, molestie erat. Nullam vestibulum, purus a congue mollis, elit neque egestas mauris, vitae blandit sapien turpis vitae velit. Aliquam erat volutpat. Curabitur at accumsan massa. Proin gravida, sem ut feugiat accumsan, est mi blandit orci, at posuere dolor nunc sit amet eros. Maecenas est arcu, pretium et urna a, volutpat lobortis purus. Nullam varius magna odio, vitae dictum ligula sollicitudin sed. ")
                .marginBottom(10);
        Table table = builder.addTable().columns(6).align(Alignment.CENTERED);
        table.addCell("Cell 1").columnSpan(3);
        table.addCell("Cell 2").columnSpan(2);
        table.addCell("Cell 3").columnSpan(1);
        table.addCell("Cell 4").columnSpan(1);
        table.addCell("Cell 5").columnSpan(1);
        table.addCell("Cell 6").columnSpan(1);
        table.addCell("Cell 7").columnSpan(1);
        table.addCell("Cell 8").columnSpan(1);
        table.addCell("Cell 9").columnSpan(1);
        Image i1 = new BaseImage(new FileInputStream("/home/dylan/Documents/penguin.jpg"), "penguin.jpg").allowWrapping(false).align(Alignment.CENTERED)
                .width(200).height(200);
        builder.addPart(i1);
        builder.addText("Donec lorem massa, fermentum volutpat tincidunt sed, euismod ac felis. Morbi sagittis lobortis porta. Suspendisse condimentum magna nisi, quis pulvinar metus varius vitae. Fusce ut fermentum eros. Sed blandit varius ultricies. Nam fringilla, sapien id pellentesque accumsan, urna orci vehicula lorem, hendrerit mattis odio magna id massa. Suspendisse placerat nulla at ornare consequat. Pellentesque posuere quis orci vel tincidunt. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Suspendisse auctor neque a lacus egestas imperdiet in eu tellus. Proin diam dolor, mollis vel varius in, mollis sed felis. Proin sit amet luctus quam. ");
        builder.finish();
    }

    private static void presentation1() throws FileNotFoundException {
        Font DEFAULT_FONT = new BaseFont(FontFamilyType.HELVETICA, FontStyle.NORMAL);
        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("pres1");
        Image i1 = new BaseImage(new FileInputStream("/home/dylan/Documents/penguin.jpg"), "penguin.jpg").allowWrapping(false).width(200, true);
        Image i2 = new BaseImage(i1).align(Alignment.CENTERED);
        Page page = builder.addPage().marginTop(20).marginBottom(20).marginLeft(20).marginRight(20);
        Text text = builder
                .createText(
                        "hi tst test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test end")
                .align(Alignment.RIGHT);
        Paragraph p = builder.addParagraph().addText(text).addText(builder.createText("Hello"));
        p.addAnchor(i1).leftOf(text);
        //p.addAnchor(i2).above(text);
        //p.addAnchor(i2).rightOf(text);
        //p.addAnchor(i2).beneath(text);

        builder.addText("Hi2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Text rwz = builder.createText("Dit is de titel van de tweede paragraaf").font(builder.createFont().bold());
        System.out.println("Marginade: " + rwz.getMarginBottom());
        builder.addParagraph()
                .addText(rwz)
                .addText(
                        builder.createText("Dit is de opvolgende tekst. Dit is de opvolgende tekst.Dit is de opvolgende tekst.Dit is de opvolgende tekst.Dit is de opvolgende tekst.Dit is de opvolgende tekst.Dit is de opvolgende tekst.Dit is de opvolgende tekst.Dit is de opvolgende tekst. Dit is de opvolgende tekst. Dit is de opvolgende tekst. Dit is de opvolgende tekst."));
        Table table = new BaseTable(page.getWidthWithoutMargins()).columns(5).border(1).align(Alignment.CENTERED);

        //table.setPosition(new Position(100, 100));
        Text cont = new BaseText(
                "Test test test test test test test test test test test test test test test test test test test test test test test test test test test test");
        ;
        Text cont2 = new BaseText("Test Test Test Test Tesese").align(Alignment.CENTERED);
        Text cont3 = new BaseText("Test Test Test Test Tese");
        Text cont4 = new BaseText("Test Test Test Test Teses");
        Text cont5 = new BaseText("Test Test Test Test Te");
        //DO ALIGNMENT FOR CONTENT IN TABLES (seems to be something wrong with right alignment on text)

        table.addCell(new BaseCell(cont2).columnSpan(1));
        table.addCell(new BaseCell(cont).columnSpan(3));
        table.addCell(new BaseCell(cont3).columnSpan(1));
        table.addCell(new BaseCell(cont5).columnSpan(2));

        Table table2 = builder.addTable().columns(7).align(Alignment.CENTERED);
        table2.addCell(builder.createText("Eric Meijer").font(DEFAULT_FONT).size(11)).width(100);
        table2.addCell(builder.createText("Robert Bor").font(DEFAULT_FONT).size(11));
        table2.addCell(builder.createText("Lucas Bos").font(DEFAULT_FONT).size(11));
        table2.addCell(builder.createText("Theo Essen").font(DEFAULT_FONT).size(11));
        table2.addCell(builder.createText("Jeroen van Schagen").font(DEFAULT_FONT).size(11));
        table2.addCell(builder.createText("Bas de Vos").font(DEFAULT_FONT).size(11));
        table2.addCell(builder.createText("Sander Benschop").font(DEFAULT_FONT).size(11));
        table2.addCell(builder.createText("Projectleider").font(DEFAULT_FONT).size(11)).width(100);
        for (int i = 0; i < 6; ++i) {
            table2.addCell(builder.createText("Teamlid").font(DEFAULT_FONT).size(11));
        }

        builder.addText("Heloooooooooooooooooooooooooooo");
        builder.finish();
    }

    private static void presentation2() throws FileNotFoundException {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("pres1");
        builder.addPage().marginTop(20).marginBottom(20).marginLeft(20).marginRight(20);
        builder.addImage(new FileInputStream("/home/dylan/Documents/mario.jpg"), ImageType.JPEG).allowWrapping(true).height(230).width(170);
        builder.addImage(new FileInputStream("/home/dylan/Documents/penguin.jpg"), ImageType.JPEG).align(Alignment.RIGHT).height(230).width(170);
        System.out.println(builder.getPreview().getPages().get(0).getContent());
        builder.finish();
    }

    public static void presentation21() {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("pres2");
        builder.addPage().marginTop(20).marginBottom(20).marginLeft(20).marginRight(20);
        Text text = builder.addText("Test text");
        DocumentState preview = builder.getPreview();
        List<Text> previewTextObjects = preview.getPreviewFor(text);
        builder.finish();
    }

    private static void newTest() {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("Test");
        Page page = builder.addPage().marginTop(20).marginBottom(20).marginLeft(20).marginRight(20);
        System.out.println("DeveloperPage: " + page.toString());
        Text firstParagraphSection = builder
                .createText("First paragraph section. Ã«Ã«Ã«Ã« (Lorem). (Zowel intern als extern) ipsum dolor sit amet, consectetur adipiscing elit. Sed aliquam lorem mauris, vitae vestibulum sapien bibendum sit amet. Mauris quis est et magna lobortis viverra. Quisque vitae elementum magna. Phasellus sagittis quis mauris eu consequat. Vivamus rutrum nisi eros, eu sagittis ipsum euismod a. Fusce nec nibh eget nulla egestas egestas. Praesent pellentesque nisl sed mollis ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Suspendisse gravida, est eget auctor dignissim, lacus enim hendrerit lorem, porttitor lacinia quam turpis a tortor. Ut porta convallis sem, a congue eros convallis quis. Praesent sed nisl eget lacus congue gravida.");
        InputStream img = null;
        InputStream img2 = null;
        try {
            img = new FileInputStream("/home/dylan/Documents/test.jpg");
            img2 = new FileInputStream("/home/dylan/Documents/mario.jpg");
        } catch (IOException e) {
            System.out.println("IOexception");
        }
        Image imageLeft = new BaseImage(200, 200, img, ImageType.JPEG);
        Image image = new BaseImage(100, 100, img2, ImageType.JPEG);
        Image image3 = new BaseImage(image).allowWrapping(true);
        builder.addPart(image3);
        builder.addPart(new BaseImage(image3).align(Alignment.RIGHT));
        Paragraph par = builder
                .addParagraph()
                .addText(firstParagraphSection)
                .addText(builder.createText("Second paragraph section"))
                .addText(
                        builder.createText("Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text "))
                .addText(
                        builder.createText("Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text Long text "))
                .addText(builder.createText("Long text Long text Long text Long "));

        Text text = builder
                .addText("Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text Loooooooooooooooooooooooooooooooong text wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie wookieedookie ");
        par.addAnchor(new BaseImage(image).align(Alignment.CENTERED)).above(firstParagraphSection);
        par.addAnchor(new BaseImage(image).align(Alignment.RIGHT)).leftOf(firstParagraphSection);
        par.addAnchor(image).rightOf(firstParagraphSection);
        par.addAnchor(image.align(Alignment.RIGHT)).beneath(firstParagraphSection);
        try {
            builder.finish(new FileOutputStream("/home/dylan/Dropbox/42 - Stage/testPDFFiles/test.pdf"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DocumentState preview = builder.getPreview();
        List<Image> previewResults = preview.getPreviewFor(image3);
        for (Image previewImage : previewResults) {
            System.out.println("Preview image3 pos: " + previewImage.getPosition());
        }
        List<Paragraph> previewResult = preview.getPreviewFor(par);
        System.out.println("Anchor 0 pos: " + previewResult.get(0).getAnchors().get(0).getPart().getPosition());
    }

    private static void documentStateTest() {
        DocumentBuilder builder = new DocumentBuilder();
        Page master = builder
                .createPage()
                .add(builder.createText("Welpiedelpie").align(Alignment.RIGHT))
                .add(builder.createText(
                        "Master Page Fixed Content Dayum Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son Son")
                        .on(65, 750)).leading(2).marginTop(50).marginRight(50).marginLeft(50).marginBottom(50);
        builder.addPage()
                .master(master)
                .leading(2)
                .marginTop(50)
                .marginRight(50)
                .marginLeft(50)
                .marginBottom(50)
                .add(new BaseText("This is the title").align(Alignment.JUSTIFIED).font(builder.addFont().boldItalic()).size(20))
                .add(new BaseText(
                        "I'lltellyouwhatIwantwhatIreallyreallywantI'lltellyouwhatIwantwhatIreallyreallywantI'lltellyouwhatIwantwhatIreallyreallywantI'lltellyouwhatIwantwhatIreallyreallywantI'lltellyouwhatIwantwhatIreallyreallywantI'lltellyouwhatIwantwhatIreallyreallywantI'lltellyouwhatIwantwhatIreallyreallywantI'lltellyouwhatIwantwhatIreallyreallywantI'lltellyouwhatIwantwhatIreallyreallywant"))
                .add(new BaseText("Fixed Test").on(0, 832).size(12))
                .add(new BaseText("Fixed Test 2").on(200, 815))
                .add(new BaseText("blabla").on(170, 820))
                .add(new BaseText(
                        "Stop you criminal scum! Stop you criminal scum! Stop you criminal scum! Stop you criminal scum! Stop you criminal scum! Stop you criminal scum! Stop you criminal scum! Stop you criminal scum!")
                        .on(100, 720).size(10))
                .add(new BaseText(
                        "Yoyoyo Yoyoyoyo yoyoyo yoyoyo yoyoyo yoyoyo yoyoyoyoyoyo yoyoyo yoyoyo yoyoyo yoyoyo yoyoyo yoyoyo dddddddbbbbbbbddddddddd ddddddbbbbbbbbbb")
                        .on(400, 200));
        //        "Test test 2Test test test test test test test test test test test test test test test test test test test test test testtest test test test test test test test test 2Test test test test test test test test test test test test test test test test test test test test test testtest test test test test test test test test 2Test test test test test test test test test test test test test test test test test test test test test testtest test test test test test test test test 2Test test test test test test test test test test test test test test test test test test test test test testtest test test test test test test test test"))
        //.add(new BaseText(
        //        "2Test test test test test test test test test test test test test test test test test test test test test testtest test test test test test test test test"));
        Text secondParagraphText = builder.createText("Oh boehoe hoe boe.");
        Text thirdParagraphText = builder.createText("Waka waka heehey");
        //BaseImage image = new BaseImage(300, 100);
        Paragraph par = builder
                .addParagraph()
                .align(Alignment.JUSTIFIED)
                .addText(
                        builder.createText("Paragraaftest boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe boe "))
                .addText(secondParagraphText)
                .addText(thirdParagraphText)
                .addText(builder.createText("Woeloewoeloe neeney"))
                .addText(builder.createText("Woeloewoeloe neeney"))
                .addText(builder.createText("Woeloewoeloe neeney"))
                .addText(
                        builder.createText("Test Test Test Test Test Test Test Test Test Test Test Test Test Test Test TestTest Test Test Test Test Test Test TestTest Test Test Test Test Test Test TestTest Test Test Test Test Test Test TestTest Test Test Test Test Test Test TestTest Test Test Test Test Test Test Test"));
        //par.addAnchor(image).leftOf(secondParagraphText);
        //par.addAnchor(new BaseImage(100, 100)).rightOf(secondParagraphText);
        //par.addAnchor(new BaseImage(200, 300)).leftOf(thirdParagraphText);
        builder.addText(
                "Woe loe loe loe neeneyeyeyeyeyeyeyeyeyeyeyeyeyeyeyeyeyeyeyeyeyey wekkie bekke zzzzzzzzzerreroeo kaskdsakdaksk skadkasdkask daskdkasdkas dkaskdkas dkasdkaskd asdkaskdask dkasdkas dkaskdks ")
                .align(Alignment.JUSTIFIED);
        StringBuilder s = new StringBuilder("");
        for (int i = 0; i < 200; ++i) {
            s.append("Test" + i);
            s.append(" ");
        }
        builder.addParagraph().addText(builder.createText(s.toString())).addText(builder.createText(s.toString())).addText(builder.createText(s.toString()))
                .addText(builder.createText(s.toString())).addText(builder.createText(s.toString())).addText(builder.createText(s.toString()))
                .addText(builder.createText(s.toString())).addText(builder.createText(s.toString())).addText(builder.createText(s.toString()));
        builder.addText("Woeloewoeloe neeneyd").align(Alignment.RIGHT);
        builder.addText("Woeloewoeloe neeneyd").align(Alignment.CENTERED);
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        builder.addText("Woeloewoeloe neeneyd");
        DocumentState state = builder.getPreview(); //.getPages().get(0).getContent().get(1))).getTextSplit().toString();
        BaseText jantje = ((BaseText) state.getPages().get(0).getContent().get(6));
        builder.finish();
    }
}
