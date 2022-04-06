package gui;

import org.ice1000.jimgui.JImFont;
import org.ice1000.jimgui.JImFontAtlas;
import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.JImVec4;
import org.ice1000.jimgui.flag.JImWindowFlags;

public class GuiConfig {
    public static LoadOutput load(JImGui imGui){
        LoadOutput loadOutput = new LoadOutput();

        // * load window presets
        int windowFlags = JImWindowFlags.MenuBar + JImWindowFlags.NoTitleBar;

        loadOutput.setWindowPresets(windowFlags);

        // * load font, CTX: .ttf fonts arent as friendly in JImGui, need to create each font individually
        JImFontAtlas fonts = imGui.getIO().getFonts();
        JImFont[] fontsizes = new JImFont[9];

        JImFont defaultFont = fonts.addDefaultFont();
        for(int i = 0; i < 6; i++){
            JImFont font = fonts.addFontFromFile("res/fonts/equip.ttf", 12 + (6*i));
            if(font.isLoaded()){
                System.out.println(font.debugName());
            }
            fontsizes[i] = font;
        }

        loadOutput.setFont12(fontsizes[0]);
        loadOutput.setFont18(fontsizes[1]);
        loadOutput.setFont24(fontsizes[2]);
        loadOutput.setFont30(fontsizes[3]);
        loadOutput.setFont36(fontsizes[4]);
        loadOutput.setFont42(fontsizes[5]);
        loadOutput.setDefaultFont(defaultFont);
        loadOutput.setHacky16(fonts.addFontFromFile("res/fonts/hacky.ttf", 16));

        return loadOutput;
    }

    public static void render(JImGui imGui, LoadOutput loadOutput){
        // * gen. design
        imGui.setBackground(JImVec4.fromHSV(0f, 0f, 0f));
        imGui.pushFont(loadOutput.getFont18());

        // * debugging
        imGui.showMetricsWindow();
    }
}
