import java.io.IOException;

import nl.mad.pdflibrary.DocumentBuilder;
import nl.mad.pdflibrary.api.BaseImage;
import nl.mad.pdflibrary.api.BaseText;
import nl.mad.pdflibrary.api.state.DocumentState;
import nl.mad.pdflibrary.model.Alignment;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Text;

public class Main {

    /**
     * This is only for testing purposes
     * @param args 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        testCutOff();
        presentation1();
        presentation2();
        presentation3();
        presentation4();
        presentation5();
        //documentStateTest();
    }

    private static void testCutOff() {
        DocumentBuilder builder = new DocumentBuilder();
        builder.addPage().marginLeft(20);
        builder.addText("van ambitities naar resultaatgericht ondernemen’").on(400, 100).font(builder.createFont().bold()).size(13);
        builder.finish();
    }

    private static void presentation1() {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("General");
        Page page = builder.addPage().marginLeft(20).marginTop(20).marginRight(20);
        builder.addText("Fixed text").on(20, 820);
        builder.addText("Document Title").size(20).font(builder.addFont().bold());
        builder.addParagraph()
                .addText(
                        builder.createText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed aliquam lorem mauris, vitae vestibulum sapien bibendum sit amet. Mauris quis est et magna lobortis viverra. Quisque vitae elementum magna. Phasellus sagittis quis mauris eu consequat. Vivamus rutrum nisi eros, eu sagittis ipsum euismod a. Fusce nec nibh eget nulla egestas egestas. Praesent pellentesque nisl sed mollis ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Suspendisse gravida, est eget auctor dignissim, lacus enim hendrerit lorem, porttitor lacinia quam turpis a tortor. Ut porta convallis sem, a congue eros convallis quis. Praesent sed nisl eget lacus congue gravida. Nulla eget nunc molestie turpis molestie venenatis. Nunc blandit commodo ipsum vitae bibendum. Nulla vestibulum pretium lacus, non vehicula tortor pretium in. Quisque vehicula dui ac tellus accumsan, sed pulvinar sem euismod. Suspendisse sagittis dictum sagittis. "))
                .addText(
                        builder.createText("Sed fermentum, nunc et dapibus viverra, eros ligula tempor nulla, non pulvinar eros lacus at turpis. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam et enim et felis porta gravida. Pellentesque ullamcorper risus quis cursus rhoncus. Aenean fermentum dui a condimentum eleifend. Sed consequat at mi at tincidunt. Donec lobortis bibendum sem, ut blandit urna blandit a. Sed tristique, metus sed blandit pretium, felis arcu mollis ante, eget mattis lacus dolor at sem. "));
        builder.finish();
    }

    private static void presentation2() {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("Cutoff & overflow");
        Page page = builder.addPage();
        page.marginLeft(20)
                .marginTop(20)
                .marginRight(20)
                .marginBottom(20)
                .add(new BaseImage(750, page.getWidth()))
                .add(builder
                        .createText("Ditisechteenhelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelehelelangetekst."))
                .add(builder
                        .createText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed aliquam lorem mauris, vitae vestibulum sapien bibendum sit amet. Mauris quis est et magna lobortis viverra. Quisque vitae elementum magna. Phasellus sagittis quis mauris eu consequat. Vivamus rutrum nisi eros, eu sagittis ipsum euismod a. Fusce nec nibh eget nulla egestas egestas. Praesent pellentesque nisl sed mollis ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Suspendisse gravida, est eget auctor dignissim, lacus enim hendrerit lorem, porttitor lacinia quam turpis a tortor. Ut porta convallis sem, a congue eros convallis quis. Praesent sed nisl eget lacus congue gravida. Nulla eget nunc molestie turpis molestie venenatis. Nunc blandit commodo ipsum vitae bibendum. Nulla vestibulum pretium lacus, non vehicula tortor pretium in. Quisque vehicula dui ac tellus accumsan, sed pulvinar sem euismod. Suspendisse sagittis dictum sagittis. "));
        builder.finish();
    }

    private static void presentation3() {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("Master pages");
        //creating master page
        Page page = builder.createPage().marginLeft(20).marginTop(20).marginRight(20);
        page.add(builder.createText("Master page content").size(16).font(builder.createFont().italic()));
        //adding page with master page
        builder.addPage().master(page).add(builder.createText("Page content"));
        builder.finish();
    }

    private static void presentation4() {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("Anchors");
        builder.addPage().marginTop(20).marginBottom(20).marginLeft(20).marginRight(20);
        Text firstParagraphSection = builder.createText("First paragraph section");
        BaseImage image = new BaseImage(200, 200);
        builder.addParagraph().addText(firstParagraphSection).addText(builder.createText("Second paragraph section")).addAnchor(image)
                .leftOf(firstParagraphSection);
        builder.finish();
    }

    private static void presentation5() {
        DocumentBuilder builder = new DocumentBuilder();
        builder.title("Alignment");
        builder.addPage().marginTop(20).marginBottom(20).marginLeft(20).marginRight(20);
        builder.addText("Left aligned").align(Alignment.LEFT); //LEFT alignment is default
        builder.addText("Centered").align(Alignment.CENTERED);
        builder.addText("Right aligned").align(Alignment.RIGHT);
        Text justified = builder
                .addText("Justified. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed aliquam lorem mauris, vitae vestibulum sapienbibendumsitamet. Mauris quis est et magna lobortisviverra. Quisque vitaeelementummagna. Phasellus sagittis quis mauris eu consequat. Vivamus rutrum nisi eros, eu sagittis ipsum euismod a. ");
        justified.align(Alignment.JUSTIFIED);
        builder.finish();
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
        BaseImage image = new BaseImage(300, 100);
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
        par.addAnchor(image).leftOf(secondParagraphText);
        par.addAnchor(new BaseImage(100, 100)).rightOf(secondParagraphText);
        par.addAnchor(new BaseImage(200, 300)).leftOf(thirdParagraphText);
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
        DocumentState state = builder.getPreview();//.getPages().get(0).getContent().get(1))).getTextSplit().toString();
        BaseText jantje = ((BaseText) state.getPages().get(0).getContent().get(6));
        builder.finish();
    }
}
