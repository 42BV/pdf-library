import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import nl.mad.toucanpdf.DocumentBuilder;
import nl.mad.toucanpdf.api.BaseImage;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.api.DocumentState;
import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Page;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.Text;

public class Main {

    /**
     * This is only for testing purposes.
     * @param args 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        //testCutOff();
        //presentation1();
        //presentation2();
        // presentation3();
        presentation4();
        //presentation5();
        //documentStateTest();
    }

    private static void presentation4() {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("Test");
        Page page = builder.addPage().marginTop(20).marginBottom(20).marginLeft(20).marginRight(20);
        System.out.println("DeveloperPage: " + page.toString());
        Text firstParagraphSection = builder
                .createText("First paragraph section. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed aliquam lorem mauris, vitae vestibulum sapien bibendum sit amet. Mauris quis est et magna lobortis viverra. Quisque vitae elementum magna. Phasellus sagittis quis mauris eu consequat. Vivamus rutrum nisi eros, eu sagittis ipsum euismod a. Fusce nec nibh eget nulla egestas egestas. Praesent pellentesque nisl sed mollis ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Suspendisse gravida, est eget auctor dignissim, lacus enim hendrerit lorem, porttitor lacinia quam turpis a tortor. Ut porta convallis sem, a congue eros convallis quis. Praesent sed nisl eget lacus congue gravida.");
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
        Image imageRight2 = new BaseImage(imageLeft);
        Image imageRight = new BaseImage(image);
        //builder.addPart(image);
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
        par.addAnchor(imageLeft).above(firstParagraphSection);
        par.addAnchor(imageLeft).leftOf(firstParagraphSection);
        par.addAnchor(imageLeft).leftOf(firstParagraphSection);
        par.addAnchor(imageLeft).leftOf(firstParagraphSection);
        par.addAnchor(imageLeft).leftOf(firstParagraphSection);
        par.addAnchor(imageLeft).leftOf(firstParagraphSection);
        par.addAnchor(imageLeft).leftOf(firstParagraphSection);
        //par.addAnchor(imageLeft).above(firstParagraphSection);
        //par.addAnchor(imageLeft).leftOf(firstParagraphSection);
        //par.addAnchor(imageLeft).leftOf(firstParagraphSection);
        //par.addAnchor(imageLeft).rightOf(firstParagraphSection);
        par.addAnchor(imageLeft).rightOf(firstParagraphSection);
        par.addAnchor(imageRight).leftOf(firstParagraphSection);
        //par.addAnchor(imageRight).rightOf(firstParagraphSection);
        //par.addAnchor(imageRight).leftOf(firstParagraphSection);
        DocumentState preview = builder.getPreview();
        System.out.println(preview.getPreviewFor(page));
        System.out.println(preview.getPreviewFor(text));

        for (Text t : preview.getPreviewFor(text)) {
            System.out.println("Text found: ");
            System.out.println("    " + t.getText());
        }
        for (Paragraph p : preview.getPreviewFor(par)) {
            System.out.println("Paragraph found: ");
            System.out.println("    " + p.toString());
            for (Text t : p.getTextCollection()) {
                System.out.println("          " + t.getText());
            }
        }

        try {
            builder.finish(new FileOutputStream("/home/dylan/Dropbox/42 - Stage/testPDFFiles/test.pdf"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
