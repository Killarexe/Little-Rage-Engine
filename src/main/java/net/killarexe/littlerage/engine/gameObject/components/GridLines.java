package net.killarexe.littlerage.engine.gameObject.components;

import net.killarexe.littlerage.engine.Window;
import net.killarexe.littlerage.engine.gameObject.Camera;
import net.killarexe.littlerage.engine.renderer.DebugDraw;
import net.killarexe.littlerage.engine.util.Settings;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GridLines extends Component{
    @Override
    public void editorUpdate(float dt) {
        Camera camera = Window.getScene().camera();
        Vector2f cameraPos = camera.pos;
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_HEIGHT;
        float firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;

        int numVtLines = (int)(projectionSize.x * camera.getZoom() / Settings.GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y * camera.getZoom() / Settings.GRID_HEIGHT) + 2;

        float height = (int)(projectionSize.y * camera.getZoom()) + Settings.GRID_HEIGHT * 2;
        float width = (int)(projectionSize.x * camera.getZoom()) + Settings.GRID_WIDTH * 2;

        int maxLines = Math.max(numVtLines, numHzLines);
        Vector3f color = new Vector3f(0f, 0f, 0f);
        for (int i=0; i < maxLines; i++) {
            float x = firstX + (Settings.GRID_WIDTH * i);
            float y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }

            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}
