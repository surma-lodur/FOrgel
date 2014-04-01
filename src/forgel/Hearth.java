/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel;

import forgel.hearth.ComReceiver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author mafolz
 */
public class Hearth {

	private volatile static Hearth instance;
	private static int drive_count = 2;

	public static Hearth getInstance(String port, int floppy_count) {
		drive_count = floppy_count;
		if (instance == null) {
			instance = new Hearth(port, drive_count);
		}
		if (instance.isNeccessaryToReconnect(port)) {
			instance.disconnectIfNecessary();
			instance.connectAndSetup(port);
		}
		if (instance.isNeccessaryToReinitDrives(drive_count)) {
			instance.setDriveCount(drive_count);
		}
		return instance;
	}

	public static Hearth getInstance(String port) {
		return Hearth.getInstance(port, drive_count);
	}

	public static Hearth getInstance() {
		return instance;
	}

	public static int getDriveCount() {
		return drive_count;
	}

	/**
	 *****************************************************************
	 * Instance Methods
	 * ****************************************************************
	 */
	private String portName;
	private List<Floppy> drives;
	private static volatile HashMap<Short, Floppy> notePlayingDrives;
	private SerialPort serialPort;

	private Hearth(String port, int floppy_count) {
		drives = new ArrayList<>();
		for (int i = 1; i <= floppy_count; ++i) {
			drives.add(new Floppy(i));
		}
		notePlayingDrives = new HashMap<>();

		disconnectIfNecessary();
		connectAndSetup(port);

	}

	private Hearth(String port) {
		this(port, 4);
	} // Constructor

	public void callNote(short note, long timeStamp) {
		attachFloppy(note);
		if (!notePlayingDrives.containsKey(note)) {
			MainWindow.printCom("No free Drive for " + note);
			return;
		}

		Floppy drive = notePlayingDrives.get(note);

		byte buffer[] = drive.play(note, timeStamp);
		if (buffer.length == 0) {
			MainWindow.printCom("No Freq for " + note);
		} else {
			MainWindow.printCom(
							drive.getIndex() + "|"
							+ String.format("%02X ", note)
							+ "> "
							+ new String(buffer));
			sendSerial(buffer);
		}
	} // callNote

	public void stopNote(short note, long timeStamp) {
		if (!notePlayingDrives.containsKey(note)) {
			MainWindow.printCom("! No Drive with " + String.format("%02X ", note) + "|" + notePlayingDrives.size());

		} else {
			Floppy drive = notePlayingDrives.get(note);
			byte buffer[] = drive.stop(timeStamp);
			MainWindow.printCom(
							drive.getIndex()
							+ "|"
							+ String.format("%02X ", note)
							+ "< "
							+ new String(buffer));
			sendSerial(buffer);
			notePlayingDrives.remove(note);
		}
	} // stopNote

	public void stopAllImediantly() {
		for (Floppy drive : drives) {
			sendSerial(drive.stopDirect());
		}

		notePlayingDrives = new HashMap<>();
		MainWindow.printCom("PANIK");
	}

	public SerialPort port() {
		return serialPort;
	}

	public void disconnect() {
		try {
			serialPort.closePort();
			MainWindow.printCom("Disconnected");
		} catch (SerialPortException ex) {
			Logger.getLogger(Hearth.class
							.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public int driveCount() {
		return drives.size();
	}

	public void setDriveCount(int floppy_count) {
		if (drives.size() < floppy_count) {
			for (int i = drives.size(); i <= floppy_count; ++i) {
				drives.add(new Floppy(i));
			}
		} else if (drives.size() > floppy_count) {
			while (drives.size() > floppy_count) {
				drives.remove(drives.size() - 1);
			}
		}
		return;
	}

	public boolean isNeccessaryToReconnect(String port) {
		if (!portName.equals(port)) {
			MainWindow.printCom("port changed to " + port);
			return true;

		} else if (!serialPort.isOpened()) {
			MainWindow.printCom("port was closed");
			return true;

		}
		return false;
	}

	public boolean isNeccessaryToReinitDrives(int floppy_count) {
		if (drives.size() != floppy_count) {
			MainWindow.printCom("change floppy's from " + drives.size() + " to " + floppy_count);
			return true;
		}
		return false;
	}

	/**
	 * ***************************************************************
	 * private methods
	 * ***************************************************************
	 */
	private void attachFloppy(short note) {
		Floppy drive = getFreeFloppy();
		if (drive != null) {
			if (!notePlayingDrives.containsKey(note)) {
				notePlayingDrives.put(note, drive);
			}
		}
	}

	private Floppy getFreeFloppy() {
		Iterator<Floppy> it = drives.iterator();
		while (it.hasNext()) {
			Floppy drive = it.next();
			if (drive.isIdle()) {
				return drive;
			}
		}
		return null;
	}

	private void sendSerial(byte data[]) {
		if (serialPort == null || !serialPort.isOpened()) {
			return;
		}
		try {
			serialPort.writeBytes(data);
		} catch (Exception ex) {
			MainWindow.printCom(ex);
			Logger.getLogger(Hearth.class
							.getName()).log(Level.SEVERE, null, ex);
		}
	} // sendSerial

	private void disconnectIfNecessary() {
		if (serialPort != null) {
			disconnect();
		}
	} // disconnectIfNecessary

	private void connectAndSetup(String port) {
		this.portName = port;
		try {
			serialPort = new SerialPort(port);
			MainWindow.printCom("Port opened: " + serialPort.openPort());
			serialPort.setParams(SerialPort.BAUDRATE_9600,
							SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
			MainWindow.printCom("Params setted: " + serialPort.setParams(9600, 8, 1, 0));

			serialPort.setEventsMask(SerialPort.MASK_RXCHAR);
			serialPort.addEventListener(new ComReceiver(serialPort));

		} catch (SerialPortException ex) {
			MainWindow.printCom(ex);
			Logger
							.getLogger(Hearth.class
											.getName()).log(Level.SEVERE, null, ex);
		}
	} // connectAndSetup
}
