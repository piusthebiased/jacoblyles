package gui.components.microcomponents;

import gui.LoadOutput;
import org.ice1000.jimgui.JImDrawList;
import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.util.ColorUtil;

public class SelectionButton {
    public static void add(JImGui imGui, LoadOutput loadOutput, String name, int xtextpos){
        // * this is a partial implementation

        JImDrawList dl = imGui.findWindowDrawList();
        float cposx = imGui.getWindowPosX();
        float cposy = imGui.getWindowPosY();

        imGui.popFont();
        imGui.pushFont(loadOutput.getFont24());

        if(!imGui.isWindowHovered()){
            dl.addRectFilled(0 + cposx, 0 + cposy, 175 + cposx, 175 + cposy, ColorUtil.colorU32(232, 235, 240, 200));
            dl.addText(xtextpos + cposx, 135 + cposy, ColorUtil.colorU32(0, 0, 0, 255), name);
        } else{
            dl.addRectFilled(0 + cposx, 0 + cposy, 175 + cposx, 175 + cposy, ColorUtil.colorU32(232, 235, 240, 255));
            dl.addText(xtextpos + cposx, 135 + cposy, ColorUtil.colorU32(0, 0, 0, 255), name);
        }
    }
}
