package gui.components.matrix;

import gui.LoadOutput;
import gui.components.microcomponents.Panel;
import org.bouncycastle.openpgp.PGPException;
import org.ice1000.jimgui.JImDrawList;
import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.JImStr;
import org.ice1000.jimgui.util.ColorUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatrixApplet implements Panel {
    private boolean open;

    public void render(JImGui imgui, LoadOutput loadOutput) throws PGPException, IOException {
        imgui.begin(new JImStr("Matrix"), loadOutput.getWindowPresets());

        imgui.text("new window");

        imgui.end();
    }

    public void toggle() {
        open = !open;
    }

    public boolean isOpen() {
        return open;
    }
}
