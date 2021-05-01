package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.util.Logger;

public abstract class Component {

    public GameObject gameObject = null;
    protected Logger logger = new Logger(getClass());
    public abstract void update(float dt);
    public void start(){}
}
