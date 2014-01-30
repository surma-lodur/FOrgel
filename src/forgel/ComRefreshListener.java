/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jssc.SerialPortList;

/**
 *
 * @author mafolz
 */
public class ComRefreshListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		MainWindow.getInstance().port.removeAllItems();
		for (String port : SerialPortList.getPortNames()) {
			MainWindow.getInstance().port.addItem(port);
		}
		MainWindow.getInstance().port.repaint();
	}
}
