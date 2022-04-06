package gui.components;

import gui.LoadOutput;
import gui.components.microcomponents.Panel;
import org.ice1000.jimgui.JImDrawList;
import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.JImStr;
import org.ice1000.jimgui.util.ColorUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Clock implements Panel {
    private boolean open = false;

    public void render(JImGui imgui, LoadOutput loadOutput) {
        imgui.begin(new JImStr("Clock"), loadOutput.getWindowPresets());

        imgui.popFont();
        imgui.pushFont(loadOutput.getFont42());

        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MM/dd/yyyy \n   hh:mm:ss");

        JImDrawList dl = imgui.findWindowDrawList();
        float cposx = imgui.getWindowPosX();
        float cposy = imgui.getWindowPosY();

        dl.addText(75 + cposx, 30 + cposy, ColorUtil.colorU32(255, 255, 255, 255), f.format(ldt));

        //set font back to default
        imgui.popFont();
        imgui.pushFont(loadOutput.getFont24());

        imgui.end();
    }

    public void toggle() {
        open = !open;
    }

    public boolean isOpen() {
        return open;
    }
}
