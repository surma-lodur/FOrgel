/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel.gui;

import forgel.Hearth;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author mafolz
 */
public class ComDisconnectListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (Hearth.getInstance() != null) {
			Hearth.getInstance().stopAllImediantly();
			Hearth.getInstance().disconnect();
		}
	}
}
