package net.killarexe.littlerage.engine.editor;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import net.killarexe.littlerage.engine.util.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LRImGui {

    private static float defaultColumnWidth = 220f;

    private static Logger logger = new Logger(LRImGui.class);

    public static void drawVec2Control(String label, Vector2f vals){
        drawVec2Control(label, vals, 0f, defaultColumnWidth);
    }

    public static void drawVec2Control(String label, Vector2f vals, float resetVal){
        drawVec2Control(label, vals, resetVal, defaultColumnWidth);
    }

    public static void drawVec2Control(String label, Vector2f vals, float resetVal, float columnWidth){

        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2f;
        Vector2f buttonSize = new Vector2f(lineHeight+3f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2f);

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1f);

        //X Button
        if(ImGui.button("X", buttonSize.x, buttonSize.y)){
            vals.x = resetVal;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValX = {vals.x};
        ImGui.dragFloat("##x", vecValX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        //Y Button
        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1f);
        if(ImGui.button("Y", buttonSize.x, buttonSize.y)){
            vals.x = resetVal;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValY = {vals.y};
        ImGui.dragFloat("##y", vecValY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        vals.x = vecValX[0];
        vals.y = vecValY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec3Control(String label, Vector3f values) {
        drawVec3Control(label, values, 0.0f, defaultColumnWidth);
    }

    public static void drawVec3Control(String label, Vector3f values, float resetValue) {
        drawVec3Control(label, values, resetValue, defaultColumnWidth);
    }

    public static void drawVec3Control(String label, Vector3f val, float resetVal, float columnWidth) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 3f) / 3f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1f);
        if (ImGui.button("X", buttonSize.x, buttonSize.y)) {
            val.x = resetVal;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {val.x};
        ImGui.dragFloat("##X", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1f);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y)) {
            val.y = resetVal;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {val.y};
        ImGui.dragFloat("##Y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.columns(1);
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.1f, 0.25f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.35f, 0.9f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.1f, 0.25f, 0.8f, 1.0f);
        if (ImGui.button("Z", buttonSize.x, buttonSize.y)) {
            val.z = resetVal;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesZ = {val.z};
        ImGui.dragFloat("##Z", vecValuesZ, 0.1f);
        ImGui.popItemWidth();
        ImGui.columns(1);

        val.x = vecValuesX[0];
        val.y = vecValuesY[0];
        val.z = vecValuesZ[0];

        ImGui.popStyleVar();
        ImGui.popID();
    }

    public static float dragFloat(String label, float val) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {val};
        ImGui.dragFloat("##dragFloat", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static int dragInt(String label, int val) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valArr = {val};
        ImGui.dragInt("##dragFloat", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static boolean colorPicker4(String label, Vector4f color) {
        boolean res = false;

        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColumnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorEdit4("##colorPicker", imColor)) {
            color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            res = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }
}
