package ControlPanel;

import ControlPanel.GUI.ControlPanelGUI;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class ControlPanelComponent {
    static ObjectOutputStream oos;
    static ObjectInputStream ois;

    protected abstract void setControlPanelComponents(ControlPanelGUI controlPanelGUI);
}
