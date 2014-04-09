package nl.mad.pdflibrary.api;

import java.util.LinkedList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Paragraph;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;

public class DocumentState {
    private List<Page> state;

    public DocumentState() {
        state = new LinkedList<>();
    }

    public void updateState(List<Page> builderState) {
        System.out.println("UPDATING STATE");
        state = new LinkedList<Page>();
        for (Page page : builderState) {
            Page newPage = new BasePage(page);
            state.add(newPage);
            processFixedPositionContent(page.getFixedPositionContent(), newPage);
            processPositioning(page.getPositionlessContent(), newPage);
        }
    }

    private void processFixedPositionContent(List<DocumentPart> content, Page page) {
        for (DocumentPart part : content) {
            switch (part.getType()) {
            case TEXT:
                Text text = new BaseText((Text) part);
                text.processContentSize(page, false, 0, true);
                page.add(text);
                break;
            case PARAGRAPH:
                Paragraph paragraph = new BaseParagraph((Paragraph) part);
                paragraph.processContentSize(page);
                page.add(paragraph);
                break;
            default:
                break;
            }
        }
    }

    private void processPositioning(List<DocumentPart> content, Page page) {
        int i = 0;
        boolean overflowFound = false;
        while (!overflowFound && i < content.size()) {
            DocumentPart p = content.get(i);
            switch (p.getType()) {
            case TEXT:
                Text text = new BaseText((Text) p);
                Position position = page.getOpenPosition(text.getLeading());
                text.setPosition(position);
                page.add(text);
                text.processContentSize(page, false, 0, false);
                break;
            case PARAGRAPH:
                Paragraph paragraph = new BaseParagraph((Paragraph) p);
                System.out.println("Retrieving pos for paragraph");
                Position pos = page.getOpenPosition(paragraph.getLeading());
                paragraph.setPosition(pos);
                page.add(paragraph);
                paragraph.processContentSize(page);
                break;
            default:
                page.add(p);
                break;
            }
            ++i;
        }
    }

    public List<Page> getPages() {
        return state;
    }
}
