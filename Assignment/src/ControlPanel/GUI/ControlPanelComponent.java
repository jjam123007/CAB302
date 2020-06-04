package ControlPanel.GUI;
/**
 * Classes that implement this interface must include the components from the control panel gui.
 * @author Nikolai Taufao | N10481087
 */
public interface ControlPanelComponent {

    /**
     * Select and set the required control panel Java swing components from the main GUI.
     * @param controlPanelGUI the control panel GUI superclass containing the ControlPanelGUI.form components.
     */
    void setControlPanelComponents(ControlPanelGUI controlPanelGUI);
}
