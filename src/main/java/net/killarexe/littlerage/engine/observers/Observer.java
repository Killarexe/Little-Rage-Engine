package net.killarexe.littlerage.engine.observers;

import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.observers.events.Event;

public interface Observer {

    void onNotify(GameObject object, Event event);

}
