package org.toucanpdf.state;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Arrays;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.toucanpdf.api.BaseImage;
import org.toucanpdf.model.Alignment;
import org.toucanpdf.model.Image;
import org.toucanpdf.model.ImageType;
import org.toucanpdf.model.Position;
import org.toucanpdf.model.Space;
import org.toucanpdf.model.state.StatePage;
import org.toucanpdf.utility.FloatEqualityTester;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseStateImageTest {
    private BaseStateImage image;

    @Before
    public void setup() {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("hammock.jpg");
        image = new BaseStateImage(100, 110, input, ImageType.JPEG);
        image.on(100, 100);
    }

    @Test
    public void copy() {
        //the copy method uses the copy constructor of the BaseImage class and does not require further testing
        Image image2 = (Image) image.copy();
        Assert.assertEquals(image.getHeight(), 100, FloatEqualityTester.EPSILON);
    }

    @Test
    public void getContentSize() {
        assertEquals(100, image.getContentHeight(null), FloatEqualityTester.EPSILON);
        assertEquals(110, image.getContentWidth(null, null), FloatEqualityTester.EPSILON);
    }

    @Test
    public void getPositionAtTest() {
        assertEquals(0, image.getPositionAt(150).length);
        assertEquals(100, image.getPositionAt(100)[0]);
    }

    @Test
    public void getUsedSpaces() {
        assertEquals(0, image.getUsedSpaces(150, 200).size());
        image.allowWrapping(true);
        assertEquals(100, image.getUsedSpaces(100, 200).get(0).getStartPoint());
        assertEquals(210, image.getUsedSpaces(100, 200).get(0).getEndPoint());
        image.allowWrapping(false);
        assertEquals(0, image.getUsedSpaces(100, 200).get(0).getStartPoint());
        assertEquals(200, image.getUsedSpaces(100, 200).get(0).getEndPoint());
    }

    @Test
    public void processContentSizeTest(@Mocked final StatePage page) {
        new NonStrictExpectations() {
            {
                page.getHeight();
                returns(200);
                page.getWidth();
                returns(200);
                page.getLeading();
                returns(10);
                page.getOpenPosition(anyDouble, anyDouble, null, anyDouble);
                returns(new Position(20, 200));
                page.getOpenSpacesIncludingHeight(null, anyBoolean, anyDouble, anyDouble, null);
                returns(Arrays.asList(new Space(10, 10, 10)), Arrays.asList(new Space(10, 200, 200)));
            }
        };
        image.on(0, 200);
        image.height(100);
        image.width(100);
        image.processContentSize(page);
        assertEquals(new Position(20, 200), image.getPosition());
        image.align(Alignment.CENTERED);
        image.on(0, 200);
        image.processContentSize(page);
        assertEquals(new Position(55, 200), image.getPosition());
        image.align(Alignment.RIGHT);
        image.on(0, 200);
        image.processContentSize(page);
        assertEquals(new Position(100, 200), image.getPosition());
        image.align(Alignment.JUSTIFIED);
        image.on(0, 200);
        image.processContentSize(page);
        assertEquals(new Position(10, 200), image.getPosition());
    }

    @Test
    public void testSettersGetters() {
        Image image2 = new BaseImage();
        image.setOriginalObject(image2);
        assertEquals(image2, image.getOriginalObject());
        image.marginRight(5);
        image.marginLeft(6);
        assertEquals(5, image.getRequiredSpaceRight(), FloatEqualityTester.EPSILON);
        assertEquals(6, image.getRequiredSpaceLeft(), FloatEqualityTester.EPSILON);
    }
}
