//package nl.mad.pdflibrary.syntax;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import nl.mad.pdflibrary.api.BaseText;
//import nl.mad.pdflibrary.model.Text;
//
//public class PdfTextCollection extends AbstractPdfObject {
//    private List<PdfText> textList;
//    private Text previousText;
//    private boolean inParagraph;
//
//    public PdfTextCollection(boolean inParagraph) {
//        super(PdfObjectType.TEXT);
//        textList = new ArrayList<PdfText>();
//        this.inParagraph = inParagraph;
//    }
//
//    public String addText(Text text, PdfIndirectObject font, PdfPage page, int leading) {
//        boolean ignoreMatrix = getIgnoreMatrix(text);
//        boolean ignorePosition = getIgnorePosition();
//
//        if (previousText == null) {
//            if (inParagraph) {
//                this.markParagraph(page, leading);
//            }
//        }
//
//        PdfStream ts = page.getCurrentStream();
//
//        PdfText pdfText = new PdfText();
//        String overflow = "";
//        //We skip the matrix if requested
//        if (ignoreMatrix) {
//            pdfText.addFont(font, text.getTextSize());
//            overflow = pdfText.addTextString(text, page, leading);
//        } else {
//            //calculate the position if we are not supposed to use the values from the text object
//            if (ignorePosition) {
//                calculatePosition(text, true);
//            }
//            overflow = pdfText.addText(text, getPdfFont(text.getFont()), currentPage, defaultLeading, inParagraph);
//        }
//        ts.add(pdfText);
//        //if a part of the text doesn't fit on the current page we create a new page and add the text to the next one.
//        if (overflow != "") {
//            this.addPage();
//            Text overflowText = new BaseText(text);
//            overflowText.setText(overflow);
//            calculatePosition(overflowText, false);
//            this.addText(overflowText, false, false, inParagraph);
//        }
//
//        return "";
//    }
//
//    private boolean getIgnoreMatrix(Text text) {
//        return (previousText != null && inParagraph);
//        //NO SUPPORT YET FOR MORE THAN ONE MATRIX IN A PARAGRAPH
//        //                    else if (!text.textMatrixEquals(previousText)) {
//        //                        ignoreMatrix = false;
//        //                    }
//    }
//
//    private boolean getIgnorePosition() {
//        return previousText != null && inParagraph;
//    }
//
//    /**
//     * Marks the start or end of a paragraph. Should be called before and after adding all the text objects for this collection if 
//     * they are in a paragraph together.
//     * @param page Current page.
//     * @param leading Space between lines.
//     */
//    private void markParagraph(PdfPage page, int leading) {
//        page.setFilledHeight(page.getFilledHeight() + leading);
//        page.setFilledWidth(0);
//    }
//
//}
