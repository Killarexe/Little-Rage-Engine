package net.killarexe.littlerage.engine.scene;

import imgui.ImGui;
import imgui.ImVec2;
import net.killarexe.littlerage.engine.gameObject.*;
import net.killarexe.littlerage.engine.gameObject.components.*;
import net.killarexe.littlerage.engine.renderer.Sprite;
import net.killarexe.littlerage.engine.renderer.SpriteSheet;
import net.killarexe.littlerage.engine.util.AssetPool;
import org.joml.Vector2f;

public class LevelEditiorScene extends Scene{
    private SpriteSheet spriteSheet;
    private GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

    public LevelEditiorScene(){
        logger.info("Changing to LevelEditior Scene");
    }

    @Override
    public void init() {
        camera = new Camera(new Vector2f(-250, 0));
        levelEditorStuff.addComponents(new MouseControls());
        levelEditorStuff.addComponents(new GridLines());
        levelEditorStuff.addComponents(new EditorCamera(this.camera));
        loadResoucres();
        spriteSheet = AssetPool.getSpriteSheet("assets\\textures\\tile\\Tile1.png");
    }

    private void loadResoucres(){
        AssetPool.getShader("assets\\shaders\\default.glsl");

        AssetPool.addSpriteSheet("assets\\textures\\tile\\Tile1.png",
                new SpriteSheet(AssetPool.getTexture("assets\\textures\\tile\\Tile1.png"),
                        16,
                        16,
                        64,
                        0));

        for (GameObject g: gameObjects){
            if(g.getComponents(SpriteRenderer.class) != null){
                SpriteRenderer renderer = g.getComponents(SpriteRenderer.class);
                if(renderer.getTexture() != null){
                    renderer.setTexture(AssetPool.getTexture(renderer.getTexture().getFilePath()));
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        this.camera.adjustProjection();
        boolean showFps = false;
        if(showFps) {
            logger.debug("FPS: " + (int) (1.0f / dt));
        }
        levelEditorStuff.update(dt);

        for(GameObject go : this.gameObjects){
            go.update(dt);
        }
    }

    @Override
    public void render() {
        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Tile Palette");

        ImVec2 windowPos = new ImVec2(10f, 10f);
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for(int i=0; i < spriteSheet.size(); i++){
            Sprite sprite = spriteSheet.getSprite(i);
            float spriteWidth = sprite.getWidth() * 3;
            float spriteHeight = sprite.getHeight() * 3;
            int id = sprite.getTexture().getTexID();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)){
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);
                //Attach this to the mouse cursor
                levelEditorStuff.getComponents(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i+1 < spriteSheet.size() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }


        }

        ImGui.end();
    }
}
