/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel;

import forgel.gui.MainWindow;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;

/**
 *
 * @author mafolz
 */
public class MidiListener implements Receiver {

	private Map<Byte, String> commandMap = new HashMap<>();

	public MidiListener() {
		commandMap.put((byte) 0x90, "Note On");
		commandMap.put((byte) 0x80, "Note Off");
		//commandMap.put((byte) -48, "Channel Pressure");
		//commandMap.put((byte) 0xa0, "Polyphonic-Key");
		//commandMap.put((byte) 0xb0, "CC");
		//commandMap.put((byte) 0xc0, "Kanalausklang");
		//commandMap.put((byte) 0xe0, "Pitch-Blend");
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		byte[] b = message.getMessage();
		try {
			printMessage(b, timeStamp);
			if (commandMap.containsKey(b[0])) {
				if (Hearth.getInstance() != null) {
					new MidiCommandThread(b, timeStamp);
				}

			}
		} catch (Exception ex) {
			MainWindow.print(ex);
			Logger.getLogger(Hearth.class.getName()).log(Level.SEVERE, null, ex);
		}
	} // send

	@Override
	public void close() {
		MainWindow.print("Close");
	}

	public static MidiDevice.Info[] getDevices() {
		return MidiSystem.getMidiDeviceInfo();
	}

	private void printMessage(byte[] msg, long timeStamp) {
		if (!commandMap.containsKey(msg[0])) {
			return;
		}
		StringBuilder buffer = new StringBuilder();
		for (byte word : msg) {
			buffer.append(String.format("%02X ", word));
		}
		MainWindow.print("MIDI: " + buffer.toString() + " " + timeStamp);
	}
}
