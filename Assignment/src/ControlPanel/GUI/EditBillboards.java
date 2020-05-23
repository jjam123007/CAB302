package ControlPanel.GUI;

import Billboard.BillboardRequest;
import Billboard.BillboardRequestType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EditBillboards extends ControlPanelGUI {
    public EditBillboards(ControlPanelGUI controlPanelGUI) throws IOException, ClassNotFoundException {
        super(controlPanelGUI);
        System.out.println(super.rowToEdit);

        editUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int billboardId = Integer.valueOf(editBbID.getText());
                String billboardName = editBbName.getText();
                String billboardMessage = editBbMsg.getText();
                String billboardInformation = editBbInfo.getText();
                String billboardUrl = editBbImgLink.getText();
                System.out.println("Data Updated");
                System.out.println(billboardName+" "+billboardMessage+" "+billboardInformation+" "+billboardUrl);

                try {
                    Object[] newTable = {billboardId,billboardName,billboardMessage,billboardInformation,billboardUrl};
                    BillboardRequest edit = new BillboardRequest(BillboardRequestType.edit, newTable,"");
                    oos.writeObject(edit);
                    oos.flush();

                    viewTable.getModel().setValueAt(billboardId,rowToEdit,0);
                    viewTable.getModel().setValueAt(billboardName,rowToEdit,1);
                    viewTable.getModel().setValueAt(billboardMessage,rowToEdit,2);
                    viewTable.getModel().setValueAt(billboardInformation,rowToEdit,3);
                    viewTable.getModel().setValueAt(billboardUrl,rowToEdit,4);
                    rowToEdit = -1;

                } catch (IOException ex) {
                    ex.printStackTrace();
                }


                editBbName.setText("");
                editBbMsg.setText("");
                editBbInfo.setText("");
                editBbImgLink.setText("");
                editBbID.setText("");
                JOptionPane.showMessageDialog(controlPanel,"Success","message",JOptionPane.NO_OPTION);
                billboardsPane.setSelectedIndex(0);
            }
        });
    }
}
