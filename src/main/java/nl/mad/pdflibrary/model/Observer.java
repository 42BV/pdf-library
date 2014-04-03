package nl.mad.pdflibrary.model;

public interface Observer {

    void update(Observable sender, ObserverEvent event, DocumentPart arg);
}
