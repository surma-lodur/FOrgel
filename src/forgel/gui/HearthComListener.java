/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel.gui;

import forgel.Hearth;
import forgel.gui.MainWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author mafolz
 */
public class HearthComListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		Hearth.getInstance((String) MainWindow.getInstance().port.getSelectedItem());
	}
}
