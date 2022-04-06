package gui.components.pgp;

import crypto.pgp.painless.Account;
import gui.LoadOutput;
import gui.components.microcomponents.Panel;
import org.bouncycastle.openpgp.PGPException;
import org.ice1000.jimgui.*;
import org.ice1000.jimgui.util.ColorUtil;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Decrypt implements Panel {
    private String out = "";
    private boolean open = false;

    public void render(JImGui imgui, LoadOutput loadOutput) {
        imgui.begin(new JImStr("Decrypt"), loadOutput.getWindowPresets());
        imgui.text("Decrypt: ");

        imgui.popFont();
        imgui.pushFont(loadOutput.getHacky16());
        imgui.textColored(JImVec4.fromHSV(0, 0.855f, 0.92f), "Ensure that these are YOUR keys");
        imgui.inputTextMultiline("###", loadOutput.getDecr());

        imgui.popFont();
        imgui.pushFont(loadOutput.getFont24());

        if(imgui.button("submit")){
            //logic below
            Account a = loadOutput.getAcc();
            try {
                out = a.decrypt(loadOutput.decr.toString());
            } catch (PGPException | IOException e) {
                e.printStackTrace();
            }
        }
        imgui.text("\n" + out);

        if(imgui.button("copy")){
            StringSelection ss = new StringSelection(out);
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(ss, null);
        }

        imgui.end();
    }

    public void toggle() {
        open = !open;
    }

    public boolean isOpen() {
        return open;
    }
}
