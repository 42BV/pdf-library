package nl.mad.pdflibrary.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable is part of an observer pattern implementation specifically for document parts. The class contains a list of observers and a couple of methods
 * to maintain the list.
 * @author Dylan de Wolff
 *
 */
public class Observable {
    private List<Observer> observers;

    /**
     * Creates a new instance of Observable.
     */
    public Observable() {
        observers = new ArrayList<Observer>();
    }

    /**
     * Adds an observer to the observer list (if the observer wasn't already in the list).
     * @param observer Observer to add.
     */
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Deletes an observer from the list.
     * @param observer Observer to remove.
     */
    public void deleteObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies observers of this object.
     * @param event Cause for this notification.
     * @param arg Argument for this notification.
     */
    public void notify(ObserverEvent event, DocumentPart arg) {
        for (int i = 0; i < observers.size(); ++i) {
            observers.get(i).update(this, event, arg);
        }
    }
}
