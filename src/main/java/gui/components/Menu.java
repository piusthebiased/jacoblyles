package gui.components;

import gui.LoadOutput;
import gui.components.matrix.MatrixApplet;
import gui.components.microcomponents.SelectionButton;
import gui.components.pgp.PGPApplet;
import org.bouncycastle.openpgp.PGPException;
import org.ice1000.jimgui.JImGui;
import org.ice1000.jimgui.JImStr;

import java.io.IOException;

public class Menu {
    public PGPApplet pgp = new PGPApplet();
    public MatrixApplet mat = new MatrixApplet();

    public void render(JImGui imGui, LoadOutput loadOutput) throws PGPException, IOException {
        // * begin main component launcher
        imGui.begin(new JImStr("Menu"), loadOutput.getWindowPresets());

        imGui.popFont();
        imGui.pushFont(loadOutput.getFont30());

        //block
        imGui.columns(2, "pgptab", false);
        if (imGui.beginChild0("PGP Applet", 175, 175)) {
            SelectionButton.add(imGui, loadOutput, "PGP", 70);

            if (imGui.invisibleButton("PGP Applet", 175, 175)) {
                pgp.toggle();
            }
            imGui.endChild();
        }
        imGui.nextColumn();

        if (imGui.beginChild0("Matrix Applet", 175, 175)) {
            SelectionButton.add(imGui, loadOutput, "Matrix", 50);

            if (imGui.invisibleButton("Matrix Applet", 175, 175)) {
                mat.toggle();
            }
            imGui.endChild();
        }

        imGui.end();

        //
        // so basically this will exist and cause toggled things to render
        //

        if(pgp.isOpen()){
            pgp.render(imGui, loadOutput);
        }

        if(mat.isOpen()){
            mat.render(imGui, loadOutput);
        }
    }
}
