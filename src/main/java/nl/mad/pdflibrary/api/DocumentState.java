package nl.mad.pdflibrary.api;

import java.util.ArrayList;
import java.util.List;

import nl.mad.pdflibrary.model.DocumentPart;
import nl.mad.pdflibrary.model.Page;
import nl.mad.pdflibrary.model.Position;
import nl.mad.pdflibrary.model.Text;

public class DocumentState {
    private List<Page> state;

    public DocumentState() {
        state = new ArrayList<>();
    }

    public void updateState(List<Page> builderState) {
        for (Page page : builderState) {
            Page newPage = new BasePage(page);
            state.add(newPage);
            newPage.addAll(page.getFixedPositionContent());
            processPositioning(page.getPositionlessContent(), newPage);
        }
    }

    private void processPositioning(List<DocumentPart> content, Page page) {
        for (DocumentPart p : content) {
            switch (p.getType()) {
            case TEXT:
                Text text = (Text) p;
                Position position = page.getOpenPosition();
                System.out.println(position.getX() + " " + position.getY());
                break;
            case PARAGRAPH:
                break;
            default:
                break;
            }
        }
    }
}
