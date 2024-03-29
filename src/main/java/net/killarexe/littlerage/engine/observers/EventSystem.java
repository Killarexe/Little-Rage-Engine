package net.killarexe.littlerage.engine.observers;

import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.observers.events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSystem {

    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer){
        observers.add(observer);
    }

    public static void notify(GameObject object, Event event){
        for (Observer observer: observers) {
            observer.onNotify(object, event);
        }
    }
}
