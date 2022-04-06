package gui.components.pgp;

import crypto.pgp.painless.Account;
import gui.LoadOutput;
import gui.components.microcomponents.Panel;
import org.bouncycastle.openpgp.PGPException;
import org.ice1000.jimgui.*;
import org.pgpainless.PGPainless;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

public class Encrypt implements Panel {
    private String out = "";
    private boolean open = false;

    public void render(JImGui imgui, LoadOutput loadOutput)  {
        imgui.begin(new JImStr("Encrypt"), loadOutput.getWindowPresets());
        imgui.text("Encrypt: ");

        imgui.popFont();
        imgui.pushFont(loadOutput.getHacky16());
        imgui.inputTextMultiline("##", loadOutput.encr);
        imgui.popFont();
        imgui.pushFont(loadOutput.getFont24());

        if(imgui.button("submit")){
            //logic below
            Account a = loadOutput.getAcc();
            try {
                out = a.encryptArmored(loadOutput.encr.toString(), PGPainless.extractCertificate(a.getUser()));
            } catch (IOException | PGPException e) {
                e.printStackTrace();
            }
        }

        imgui.popFont();
        imgui.pushFont(loadOutput.getHacky16());
        imgui.text("\n" + out);
        imgui.popFont();
        imgui.pushFont(loadOutput.getFont24());

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
