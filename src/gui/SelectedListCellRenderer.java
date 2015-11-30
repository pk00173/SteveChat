package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import client.ClientUser;

public class SelectedListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		ClientUser client = (ClientUser) value;
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (isSelected) {
        	 c.setBackground(Color.GREEN);
           
        }
        if(client.hasUnreadMsg()){
        	 c.setBackground(Color.ORANGE);
        }
        
        setText(client.getUsername());
        
        return c;
    }
}