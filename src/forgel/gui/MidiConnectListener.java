/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel.gui;

import forgel.MidiListener;
import forgel.gui.MainWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import javax.swing.JComboBox;

/**
 *
 * @author mafolz
 */
public class MidiConnectListener implements ActionListener {

	public static MidiDevice device;
	private static Transmitter transmitter;
	private static MidiListener midiReceiver;

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (device != null) {
				midiReceiver.close();;
				transmitter.close();
				device.close();
			}
			MidiDevice.Info info = (MidiDevice.Info) ((JComboBox) e.getSource()).getSelectedItem();
			midiReceiver = new MidiListener();
			device = MidiSystem.getMidiDevice(info);
			device.open();
			transmitter = device.getTransmitter();
			transmitter.setReceiver(midiReceiver);
			MainWindow.print(device.getDeviceInfo().getName());
		} catch (MidiUnavailableException ex) {
			MainWindow.print(e);
			Logger.getLogger(MidiConnectListener.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
