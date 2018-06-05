package hsps.services.logic.basic;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    private List<Observer> observers;

    protected Subject() {
        observers = new ArrayList<Observer>();
    }

    public synchronized void addObserver(Observer arg) {
        if (arg != null && !observers.contains(arg))
            observers.add(arg);
    }

    public synchronized void removeObserver(Observer arg) {
        if (arg != null && observers.contains(arg))
            observers.remove(arg);
    }

    public synchronized void notifyObservers() {
        for (Observer o : observers)
            o.update();
    }

    public synchronized void notifyObserver(Observer arg) {
        arg.update();
    }
}