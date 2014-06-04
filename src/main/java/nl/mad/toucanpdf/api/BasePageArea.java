package nl.mad.toucanpdf.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.mad.toucanpdf.model.DocumentPart;
import nl.mad.toucanpdf.model.PageArea;
import nl.mad.toucanpdf.model.PlaceableDocumentPart;

public class BasePageArea implements PageArea {
    private int height = 20;
    private Map<String, String> attributes = new HashMap<String, String>();
    private List<DocumentPart> content = new ArrayList<DocumentPart>();

    public BasePageArea(int newHeight) {
        this.height = newHeight;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public PageArea height(int height) {
        this.height = height;
        return this;
    }

    @Override
    public PageArea addAttribute(String key, String value) {
        this.attributes.put(key, value);
        return this;
    }

    @Override
    public String getAttribute(String key) {
        return this.attributes.get(key);
    }

    @Override
    public PageArea add(DocumentPart part) {
        if (part != null) {
            content.add(part);
        }
        return this;
    }

    @Override
    public List<DocumentPart> getContent() {
        return this.content;
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }
}
