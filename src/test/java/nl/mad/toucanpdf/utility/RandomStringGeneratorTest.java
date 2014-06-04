package nl.mad.toucanpdf.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomStringGeneratorTest {

    @Test
    public void testGeneration() {
        String random = RandomStringGenerator.generateRandomString(6);
        assertEquals(6, random.length());
        random = RandomStringGenerator.generateRandomString("AB", 5);
        assertTrue(random.matches("[A-B]{5}"));
    }
}
