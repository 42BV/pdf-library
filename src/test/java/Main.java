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

import org.apache.log4j.BasicConfigurator;

public class Main {

    /**
     * This is only for testing purposes.
     * @param args 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        presentation1();
        //presentation1();
        //newTest();
        //documentStateTest();
    }

    private static void presentation1() throws FileNotFoundException {
        Font DEFAULT_FONT = new BaseFont(FontFamilyType.HELVETICA, FontStyle.NORMAL);
        BasicConfigurator.configure();
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("pres1");
        Image i1 = new BaseImage(new FileInputStream("C:/Users/Dylan/Pictures/001.jpg"), "penguin.jpg").allowWrapping(false).width(200, true);
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
        //TODO: It is actually just fine, no clue why there were issues on the PC when adding a huge amount of images.
        //DO ALIGNMENT FOR CONTENT IN TABLES (seems to be something wrong with right alignment on text)
        //ALLOW INVERT COLOR OPTION ON IMAGES (this is better than just forcing CMYK to inverted)
        //cell width slightly too large
        //Image i1 = new BaseImage(new FileInputStream("/home/dylan/Documents/mario.jpg"), ImageType.JPEG).allowWrapping(true).height(230).width(170);

        //builder.addPart(i1);
        //builder.addPart(i1);
        //builder.addPart(i1);

        table.addCell(new BaseCell(cont2).columnSpan(1));
        table.addCell(new BaseCell(cont).columnSpan(3));
        //cont3 not showing up?
        table.addCell(new BaseCell(cont3).columnSpan(1));
        //table.addCell(new BaseCell(new BaseImage(i1)));
        //System.out.println("CEll height: " + table.getContent().get(3).getHeight());
        table.addCell(new BaseCell(cont5).columnSpan(2));
        //builder.addPart(table);

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
        //builder.addText("Hello this is fixed").on(30, 795).marginTop(190).marginBottom(100);
        //System.out.println(builder.getPreview().getPages().get(0).getContent());
        builder.finish();
    }

    private static void presentation2() throws FileNotFoundException {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("pres1");
        builder.addPage().marginTop(20).marginBottom(20).marginLeft(20).marginRight(20);
        builder.addImage(new FileInputStream("/home/dylan/Documents/mario.jpg"), ImageType.JPEG).allowWrapping(true).height(230).width(170);
        builder.addImage(new FileInputStream("/home/dylan/Documents/penguin.jpg"), ImageType.JPEG).align(Alignment.RIGHT).height(230).width(170);
        //PlaceableDocumentPart part = new BaseTable();
        //part.setPosition(new Position(400, 50));
        //builder.addPart(part);
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
                .createText("First paragraph section. ëëëë (Lorem). (Zowel intern als extern) ipsum dolor sit amet, consectetur adipiscing elit. Sed aliquam lorem mauris, vitae vestibulum sapien bibendum sit amet. Mauris quis est et magna lobortis viverra. Quisque vitae elementum magna. Phasellus sagittis quis mauris eu consequat. Vivamus rutrum nisi eros, eu sagittis ipsum euismod a. Fusce nec nibh eget nulla egestas egestas. Praesent pellentesque nisl sed mollis ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Suspendisse gravida, est eget auctor dignissim, lacus enim hendrerit lorem, porttitor lacinia quam turpis a tortor. Ut porta convallis sem, a congue eros convallis quis. Praesent sed nisl eget lacus congue gravida.");
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
        //TODO: Update builder for images
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
