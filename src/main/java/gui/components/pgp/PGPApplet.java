package gui.components.pgp;

import gui.LoadOutput;
import gui.components.microcomponents.Panel;
import gui.components.microcomponents.SelectionButton;
import gui.components.pgp.Decrypt;
import gui.components.pgp.Encrypt;
import org.bouncycastle.openpgp.PGPException;
import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.JImStr;

import java.io.IOException;

public class PGPApplet implements Panel {
    private boolean open = false;
    private Decrypt decrypt = new Decrypt();
    private Encrypt encrypt = new Encrypt();

    public void render(JImGui imgui, LoadOutput loadOutput) throws PGPException, IOException {
        imgui.begin(new JImStr("PGP"), loadOutput.getWindowPresets());
        imgui.columns(2, "pgpcolumns", false);
        if (imgui.beginChild0("Decrypt Applet", 175, 175)) {
            SelectionButton.add(imgui, loadOutput, "decrypt", 50);

            if (imgui.invisibleButton("d", 175, 175)) {
                decrypt.toggle();
            }
            imgui.endChild();
        }
        imgui.nextColumn();

        if (imgui.beginChild0("Encrypt Applet", 175, 175)) {
            SelectionButton.add(imgui, loadOutput, "encrypt", 60);

            if (imgui.invisibleButton("encrypt", 175, 175)) {
                encrypt.toggle();
            }
            imgui.endChild();
        }
        imgui.end();

        if(decrypt.isOpen()){
            decrypt.render(imgui, loadOutput);
        }

        if(encrypt.isOpen()){
            encrypt.render(imgui, loadOutput);
        }
    }

    public void toggle() {
        open = !open;
    }

    public boolean isOpen() {
        return open;
    }
}
