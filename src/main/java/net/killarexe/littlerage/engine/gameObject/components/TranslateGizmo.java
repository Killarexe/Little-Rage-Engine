package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.renderer.Sprite;
import org.joml.Vector4f;

public class TranslateGizmo extends Component{

    private Vector4f xAxisColor = new Vector4f(1,0,0,1);
    private Vector4f xAxisColorHover = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0,0,1,1);
    private Vector4f yAxisColorHover = new Vector4f();

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private GameObject activeGameobject = null;

    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;

    public TranslateGizmo(Sprite arrowSprite){

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void imgui() {

    }
}
