/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel.gui;

import forgel.MainWindow;
import javax.swing.JDialog;

/**
 *
 * @author mafolz
 */
public class HardwareModalDialog extends JDialog {

	public HardwareModalDialog() {
		this.setModal(true);
		this.add(new HardwareOptions());
		this.setSize(800, 600);
	}

}
