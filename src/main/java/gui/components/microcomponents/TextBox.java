package gui.components.microcomponents;

import gui.LoadOutput;
import org.ice1000.jimgui.JImDrawList;
import org.ice1000.jimgui.JImGui;

public class TextBox {
    public static void add(JImGui imGui, LoadOutput loadOutput){
        // * this is a partial implementation
        JImDrawList dl = imGui.findWindowDrawList();
        float cposx = imGui.getWindowPosX();
        float cposy = imGui.getWindowPosY();

        //push font
        imGui.popFont();
        imGui.pushFont(loadOutput.getHacky16());




        //restore font
        imGui.popFont();
        imGui.pushFont(loadOutput.getFont24());


    }
}
