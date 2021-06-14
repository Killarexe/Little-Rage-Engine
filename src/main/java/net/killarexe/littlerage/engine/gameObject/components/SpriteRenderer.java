package net.killarexe.littlerage.engine.gameObject.components;

import imgui.ImGui;
import net.killarexe.littlerage.engine.editor.LRImGui;
import net.killarexe.littlerage.engine.renderer.Sprite;
import net.killarexe.littlerage.engine.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component{

    private Vector4f color = new Vector4f(1,1,1,1);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform)){
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    public Vector4f getColor(){
        return this.color;
    }

    public Texture getTexture(){
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords(){
        return sprite.getTexCoords();
    }

    public boolean isDirty(){return this.isDirty;}

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor(Vector4f color){
        if(!this.color.equals(color)) {
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public void setClean(){
        this.isDirty = false;
    }

    @Override
    public void imgui() {
        if(LRImGui.colorPicker4("Color Picker", this.color)){
            this.isDirty = true;
        }
        super.imgui();
    }

    public void setTexture(Texture texture){
        this.sprite.setTexture(texture);
    }
}
