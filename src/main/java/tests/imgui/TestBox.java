package tests.imgui;

import org.ice1000.jimgui.*;
import org.ice1000.jimgui.util.JniLoader;

import java.net.URISyntaxException;

public class TestBox {
    public static final int LIMIT = 3;

    @SuppressWarnings("AccessStaticViaInstance")
    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        JniLoader.load();
        try (JImGui gui = new JImGui()) {
            NativeString ns = new NativeString(16);
            while (!gui.windowShouldClose()) {
                gui.initNewFrame();

                gui.inputTextMultiline("test", ns);
                if(gui.button("test")) {
                    System.out.println(ns.toString());
                }

                gui.render();
            }
        }
    }
}
