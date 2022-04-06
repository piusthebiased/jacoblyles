import crypto.pgp.painless.Account;
import crypto.pgp.painless.PGPIO;
import global.FileIO;
import gui.GuiConfig;
import gui.components.Clock;
import gui.components.Menu;
import gui.LoadOutput;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.NativeString;
import org.ice1000.jimgui.util.JniLoader;
import org.pgpainless.PGPainless;

import java.io.IOException;

public class Client {
    private static Menu m = new Menu();
    private static Clock c = new Clock();
    //private static

    public static void main(String[] args) throws IOException, PGPException {
        //initialize file systems
        Initialize.check();

        matrix.bot.Client cl = new matrix.bot.Client("http://matrix-client.matrix.org/");
        cl.login("thatonetestingaccount", "thisisaverybadpasswordlmao", loginData -> {
            if(cl.getLoginData().isSuccess()){
                //load listener
                cl.registerRoomEventListener(roomEvent -> {
                    if(roomEvent.size() != 0){
                        System.out.println(roomEvent.get(0).getContent());
                    }
                });

                //load gui
                JniLoader.load();
                try(JImGui imGui = new JImGui()) {
                    // * load font presets
                    imGui.setWindowTitle("jacoblyles");
                    NativeString decr = new NativeString(16);
                    NativeString encr = new NativeString(16);

                    LoadOutput loadOutput = GuiConfig.load(imGui);
                    loadOutput.setAcc(new Account(PGPIO.keyFromFile("res/painlessKeys/private.asc")));
                    loadOutput.setEncr(encr);
                    loadOutput.setDecr(decr);
                    loadOutput.setClient(cl);

                    while (!imGui.windowShouldClose()) {
                        imGui.initNewFrame();
                        // * load
                        GuiConfig.render(imGui, loadOutput);

                        // * render
                        c.render(imGui, loadOutput);
                        m.render(imGui, loadOutput);

                        // ! cannot modify, always last line
                        imGui.render();
                    }
                }

            } else {
                System.out.println("u died");
            }
        });
    }
}
