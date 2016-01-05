package org.toucanpdf.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.toucanpdf.model.Page;
import org.toucanpdf.model.PdfNameValue;
import org.toucanpdf.pdf.syntax.PdfIndirectObject;
import org.toucanpdf.pdf.syntax.PdfName;
import org.toucanpdf.pdf.syntax.PdfObjectType;
import org.toucanpdf.pdf.syntax.PdfPage;
import org.toucanpdf.pdf.syntax.PdfPageTree;

import org.junit.Before;
import org.junit.Test;

public class PdfPageTreeTest {
    private PdfPageTree pageTree;

    @Before
    public void setUp() throws Exception {
        pageTree = new PdfPageTree();
    }

    @Test
    public void testType() {
        assertEquals(PdfObjectType.PAGETREE, pageTree.getType());
        assertEquals(new PdfName(PdfNameValue.PAGES), pageTree.get(PdfNameValue.TYPE));
    }

    @Test
    public void testSizeRetrieval() {
        //test retrieval on empty tree
        assertEquals(1, pageTree.getSize());
        //test retrieval on tree with tree node
        PdfPageTree pageTree2 = new PdfPageTree();
        pageTree2.add(new PdfIndirectObject(1, 0, new PdfPage(10, 10, Page.DEFAULT_NEW_LINE_SIZE, 0), true));
        pageTree.add(new PdfIndirectObject(2, 0, pageTree2, true));
        assertEquals(3, pageTree.getSize());
        //test retrieval on tree with both tree node and leaf node
        pageTree.add(new PdfIndirectObject(3, 0, new PdfPage(10, 10, Page.DEFAULT_NEW_LINE_SIZE, 0), true));
        assertEquals(4, pageTree.getSize());
    }

    @Test
    public void testObjectRetrieval() {
        //test object retrieval on empty tree
        assertEquals(0, pageTree.getPageTreeObjects().size());
        //test retrieval on tree with tree node
        PdfPageTree pageTree2 = new PdfPageTree();
        pageTree2.add(new PdfIndirectObject(1, 0, new PdfPage(10, 10, Page.DEFAULT_NEW_LINE_SIZE, 0), true));
        pageTree.add(new PdfIndirectObject(2, 0, pageTree2, true));
        assertEquals(2, pageTree.getPageTreeObjects().size());
        //test retrieval on tree with both tree node and leaf node
        pageTree.add(new PdfIndirectObject(3, 0, new PdfPage(10, 10, Page.DEFAULT_NEW_LINE_SIZE, 0), true));
        assertEquals(3, pageTree.getPageTreeObjects().size());
    }

    @Test
    public void testWriteToFile() throws IOException {
        String expectedResult = "<<\n /Kids [ 1 0 R ]\n /Type /Pages\n /Count 1\n>>";
        pageTree.add(new PdfIndirectObject(1, 0, new PdfPage(200, 200, Page.DEFAULT_NEW_LINE_SIZE, 0), true));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pageTree.writeToFile(baos);
        String result = baos.toString();
        assertTrue(result.startsWith("<<\n"));
        assertTrue(result.contains("/Kids [ 1 0 R ]\n"));
        assertTrue(result.contains("/Type /Pages\n"));
        assertTrue(result.contains("/Count 1\n"));
        assertTrue(result.endsWith("\n>>"));
    }
}
