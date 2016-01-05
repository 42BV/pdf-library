package org.toucanpdf.font.parser;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.toucanpdf.TestAppender;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.log4j.Logger;

public class PfbParserTest {
    private Logger log = Logger.getRootLogger();
    private TestAppender appender = new TestAppender();

    @Before
    public void setUp() {
        log.addAppender(appender);
    }

    @Test
    public void testInvalidInputstreamParsing() {
        PfbParser parser = new PfbParser(null);
        assertEquals("Given inputstream in PFB parser is null", appender.messages.get(0).getMessage().toString());
        assertEquals("Received empty inputstream for .pfb file.", appender.messages.get(1).getMessage().toString());
        assertEquals(3, parser.getLengths().length);
        assertEquals(0, parser.getPfbData().length);
    }

    @Test
    public void testStreamParsing() {
        InputStream stream = PfbParserTest.class.getClassLoader().getResourceAsStream("Courier.pfb");
        PfbParser parser = new PfbParser(stream);
        assertEquals(1279, parser.getLengths()[0]);
        assertEquals(94768, parser.getPfbData().length);
    }

    @After
    public void tearDown() {
        log.removeAppender(appender);
    }

}
