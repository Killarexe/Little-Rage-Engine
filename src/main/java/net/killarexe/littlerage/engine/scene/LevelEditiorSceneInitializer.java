package net.killarexe.littlerage.engine.scene;

import imgui.ImGui;
import imgui.ImVec2;
import net.killarexe.littlerage.engine.gameObject.*;
import net.killarexe.littlerage.engine.gameObject.components.*;
import net.killarexe.littlerage.engine.renderer.*;
import net.killarexe.littlerage.engine.util.AssetPool;
import org.joml.Vector2f;

public class LevelEditiorSceneInitializer extends SceneInitializer{
    private SpriteSheet spriteSheet;
    private GameObject levelEditorStuff;

    public LevelEditiorSceneInitializer(){
        logger.info("Changing to LevelEditior Scene");
    }

    @Override
    public void init(Scene scene) {
        spriteSheet = AssetPool.getSpriteSheet("assets\\textures\\tile\\Tile1.png");
        SpriteSheet gizmos = AssetPool.getSpriteSheet("assets\\textures\\gui\\gizmos.png");

        this.levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setDoSerialization(false);
        levelEditorStuff.addComponents(new MouseControls());
        levelEditorStuff.addComponents(new GridLines());
        levelEditorStuff.addComponents(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponents(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorStuff);
    }

    @Override
    public void loadResoucres(Scene scene) {
        AssetPool.getShader("assets\\shaders\\default.glsl");

        AssetPool.addSpriteSheet("assets\\textures\\tile\\Tile1.png",
                new SpriteSheet(AssetPool.getTexture("assets\\textures\\tile\\Tile1.png"),
                        16, 16, 64, 0));

        AssetPool.addSpriteSheet("assets\\textures\\gui\\gizmos.png",
                new SpriteSheet(AssetPool.getTexture("assets\\textures\\gui\\gizmos.png"),
                        24, 48, 3, 0));

        for (GameObject g: scene.getGameObjects()){
            if(g.getComponents(SpriteRenderer.class) != null){
                SpriteRenderer renderer = g.getComponents(SpriteRenderer.class);
                if(renderer.getTexture() != null){
                    renderer.setTexture(AssetPool.getTexture(renderer.getTexture().getFilePath()));
                }
            }
        }
    }

    @Override
    public void imgui() {
        ImGui.begin("Editor Debug");
        levelEditorStuff.imgui();
        ImGui.end();

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
