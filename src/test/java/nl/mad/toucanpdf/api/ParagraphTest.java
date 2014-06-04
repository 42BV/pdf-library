package nl.mad.toucanpdf.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.mad.toucanpdf.TestAppender;
import nl.mad.toucanpdf.api.BaseParagraph;
import nl.mad.toucanpdf.api.BaseText;
import nl.mad.toucanpdf.font.parser.PfbParser;
import nl.mad.toucanpdf.model.Alignment;
import nl.mad.toucanpdf.model.Anchor;
import nl.mad.toucanpdf.model.DocumentPartType;
import nl.mad.toucanpdf.model.Image;
import nl.mad.toucanpdf.model.ImageType;
import nl.mad.toucanpdf.model.Paragraph;
import nl.mad.toucanpdf.model.Position;
import nl.mad.toucanpdf.model.Text;
import nl.mad.toucanpdf.utility.FloatEqualityTester;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParagraphTest {
    private Paragraph paragraph;
    private Logger log = Logger.getRootLogger();
    private TestAppender appender = new TestAppender();

    @Before
    public void setUp() throws Exception {
        paragraph = new BaseParagraph();
        log.addAppender(appender);
    }

    @Test
    public void testType() {
        assertEquals("Document part type was not set correctly to PARAGRAPH", DocumentPartType.PARAGRAPH, paragraph.getType());
    }

    @Test
    public void testPartAdding() {
        Text t = new BaseText();
        paragraph.addText(t);
        assertEquals("The size of the text collection is incorrect. ", 1, paragraph.getTextCollection().size());
        assertEquals("The added text object is incorrect. ", t, paragraph.getTextCollection().get(0));

        List<Text> textList = new ArrayList<Text>(Arrays.asList(new BaseText("Test"), new BaseText("Test")));
        paragraph.addText(textList);
        assertEquals(3, paragraph.getTextCollection().size());
    }

    @Test
    public void testPositioning() {
        paragraph.on(200, 201);
        assertEquals("The position was not set correctly. ", 200, paragraph.getPosition().getX(), FloatEqualityTester.EPSILON);
        assertEquals("The position was not set correctly. ", 201, paragraph.getPosition().getY(), FloatEqualityTester.EPSILON);
    }

    @Test
    public void testAnchor() {
        Image i = new BaseImage(new byte[0], ImageType.JPEG);
        Text t = new BaseText("Test");
        paragraph.addText(t);
        Anchor a = paragraph.addAnchor(i);
        assertTrue(a != null);
        assertEquals(null, a.getAnchorPoint());
        assertEquals(1, paragraph.getAnchors().size());
        a.beneath(t);
        assertEquals(1, paragraph.getAnchorsOn(t).size());

        a = paragraph.addAnchor(i);
        assertEquals(2, paragraph.getAnchors().size());
        assertEquals(1, paragraph.getAnchorsOn(t).size());
        a.leftOf(t);
        assertEquals(2, paragraph.getAnchorsOn(t).size());

        a = new BaseAnchor(a, t);
        paragraph.addAnchor(a);
        assertEquals("The given anchor could not be added on the given text and location. Only a single anchor is allowed per location.", appender.messages
                .get(0).getMessage().toString());
        assertEquals(2, paragraph.getAnchors().size());
    }

    @Test
    public void testMargins() {
        paragraph.marginBottom(1).marginLeft(2).marginRight(3).marginTop(4);
        assertEquals(1, paragraph.getMarginBottom());
        assertEquals(2, paragraph.getMarginLeft());
        assertEquals(3, paragraph.getMarginRight());
        assertEquals(4, paragraph.getMarginTop());
    }

    @Test
    public void testAlignment() {
        paragraph.align(Alignment.RIGHT);
        assertEquals(Alignment.RIGHT, paragraph.getAlignment());
    }

    @Test
    public void testCopy() {
        paragraph.on(100, 110);
        paragraph.align(Alignment.CENTERED);
        Text t = new BaseText("Test");
        paragraph.addText(t);
        Paragraph p = (Paragraph) paragraph.copy();
        assertEquals(new Position(100, 110), p.getPosition());
        assertEquals(Alignment.CENTERED, p.getAlignment());
        assertEquals(0, p.getTextCollection().size());

        paragraph.addAnchor(new BaseAnchor(new BaseImage(new byte[0], ImageType.JPEG)).beneath(t));
        p = new BaseParagraph(paragraph, true);
        assertEquals(1, p.getTextCollection().size());
        assertEquals(1, p.getAnchors().size());
    }

    @After
    public void tearDown() {
        log.removeAppender(appender);
    }

}
