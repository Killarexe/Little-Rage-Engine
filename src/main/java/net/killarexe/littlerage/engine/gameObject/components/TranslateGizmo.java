package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.editor.PropertiesWindow;
import net.killarexe.littlerage.engine.gameObject.GameObject;
import net.killarexe.littlerage.engine.gameObject.Prefabs;
import net.killarexe.littlerage.engine.renderer.Sprite;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TranslateGizmo extends Component{

    private Vector4f xAxisColor = new Vector4f(1,0,0,1);
    private Vector4f xAxisColorHover = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0,0,1,1);
    private Vector4f yAxisColorHover = new Vector4f();

    private Vector2f xAxisOffset = new Vector2f(100f, -7f);
    private Vector2f yAxisOffset = new Vector2f(80f, 60f);

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private GameObject activeGameobject = null;

    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;

    private PropertiesWindow window;

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow window){
        this.window = window;
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.xAxisSprite = this.xAxisObject.getComponents(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponents(SpriteRenderer.class);

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.setDoSerialization(false);
        this.yAxisObject.setDoSerialization(false);
    }

    @Override
    public void update(float dt) {
        this.activeGameobject = window.getActiveGameObject();
        if(this.activeGameobject != null){
            this.xAxisObject.transform.pos.set(this.activeGameobject.transform.pos);
            this.yAxisObject.transform.pos.set(this.activeGameobject.transform.pos);
            this.xAxisObject.transform.pos.add(xAxisOffset);
            this.yAxisObject.transform.pos.add(yAxisOffset);
            this.setActive();
        }else{
            this.setInactive();
        }
    }

    private void setActive(){
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive(){
        this.activeGameobject = null;
        this.yAxisSprite.setColor(new Vector4f(0,0,0,0));
        this.xAxisSprite.setColor(new Vector4f(0,0,0,0));
    }
}
