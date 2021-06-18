package net.killarexe.littlerage.engine.scene;

import net.killarexe.littlerage.engine.util.Logger;

public abstract class SceneInitializer {

    protected Logger logger = new Logger(SceneInitializer.class);

    public abstract void init(Scene scene);
    public abstract void loadResoucres(Scene scene);
    public abstract void imgui();
}
