package net.killarexe.littlerage.engine.imGui;

import imgui.ImGui;
import net.killarexe.littlerage.engine.observers.EventSystem;
import net.killarexe.littlerage.engine.observers.events.Event;
import net.killarexe.littlerage.engine.observers.events.EventType;

public class MenuBar {

    public void imgui(){

        ImGui.beginMenuBar();

        if(ImGui.beginMenu("File")){

            if(ImGui.menuItem("Save", "Ctrl+S")){
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if(ImGui.menuItem("Load", "Ctrl+O")){
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            if(ImGui.menuItem("Close", "Ctrl+Esc")){
                EventSystem.notify(null, new Event(EventType.CloseLevel));
            }

            ImGui.endMenu();
        }

        ImGui.endMenuBar();
    }
}
