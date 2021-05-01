package net.killarexe.littlerage.engine.scene;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.util.Logger;

public class LevelScene extends Scene{

    Logger logger = new Logger(getClass());

    public LevelScene(){
        logger.info("Changing to Level Scene");
        Window.getInstance().r = 1;
        Window.getInstance().g = 1;
        Window.getInstance().b = 1;
    }

    @Override
    public void update(float deltaTime) {

    }
}
