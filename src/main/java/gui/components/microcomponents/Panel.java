package gui.components.microcomponents;

import gui.LoadOutput;
import org.bouncycastle.openpgp.PGPException;
import org.ice1000.jimgui.JImGui;

import java.io.IOException;

public interface Panel {
    void render(JImGui imgui, LoadOutput loadOutput) throws PGPException, IOException;
    void toggle();
    boolean isOpen();
}
