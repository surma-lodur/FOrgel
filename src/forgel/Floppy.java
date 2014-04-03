/*
 * Represents one Floppy Drive
 */
package forgel;

import forgel.gui.MainWindow;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mafolz
 */
public class Floppy {

	/**
	 *****************************************************************
	 * Instance Methods
	 * ****************************************************************
	 */
	// see http://de.wikipedia.org/wiki/Frequenzen_der_gleichstufigen_Stimmung
	// 49 A4 Kammerton entspricht midi 69
	private short noteOffset; // 40 middle C
	private short noteLimit;
	private Map<Short, Double> noteMap;
	private boolean noteOn;
	private long timeStamp;
	private int index;

	public Floppy(int index) {
		this.noteOffset = 48; // one Octave is 12
		this.index = index;
		this.noteMap = new HashMap<>();
		mapFrequencies();
	}

	public int getIndex() {
		return index;
	}

	public void mapFrequencies() {
		for (short i = 0; i < 69; i++) {
			noteMap.put((short) (i + noteOffset), FrequencyCalculator.get(i - noteOffset));
		}

		System.out.println(noteMap.keySet());
		System.out.println(noteMap.values());
	} // mapFrequencies

	public boolean isIdle() {
		return !noteOn;
	}

	public byte[] play(short note, long timeStamp) {
		return play(note, timeStamp, 0.0);
	}

	/**
	 *
	 * @param note
	 * @param timeStamp
	 * @param spread	possible delpa to detune the floppy
	 * @return
	 */
	public byte[] play(short note, long timeStamp, double spread) {
		this.timeStamp = timeStamp;
		byte buffer[] = {};
		double hertz = getHertz(note) + getHertz(note) * spread;

		if (hertz < 0) {
			return buffer;
		}
		try {
			buffer = ("" + this.index + "-" + Double.toString(hertz) + ";").
							getBytes("iso-8859-1");

		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(Floppy.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.noteOn = true;
		return buffer;
	}

	public byte[] stop(long timeStamp) {
		if (!this.noteOn) {
			return new byte[0];
		}
		long timeDifference = timeStamp - this.timeStamp;
		byte buffer[] = {};
		try {
			//sleepAtLeast((int) (timeDifference / 1000) - ((timeDifference / 100000)));
			sleepAtLeast((int) ((timeDifference / 1000) * 0.202) / 1);

		} catch (InterruptedException ex) {
			Logger.getLogger(Floppy.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			buffer = ("" + this.index + "-0;").getBytes("iso-8859-1");
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(Floppy.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.noteOn = false;
		return buffer;
	}

	public byte[] stopDirect() {
		byte buffer[] = {};
		try {
			buffer = ("" + this.index + "-0;").getBytes("iso-8859-1");
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(Floppy.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.noteOn = false;
		return buffer;
	}

	/**
	 *****************************************************************
	 * Private Instance Methods
	 * ****************************************************************
	 */
	private double getHertz(short note) {
		if (noteMap.isEmpty()) {
			mapFrequencies();
		}

		if (noteMap.containsKey(note)) {
			return noteMap.get(note);
		} else {
			return -1.0;
		}
	} // getHertz

	private void sleepAtLeast(long millis) throws InterruptedException {
		MainWindow.printCom(new Integer(this.index).toString() + " hold " + new Long(millis).toString() + " ms");
		long t0 = System.currentTimeMillis();
		long millisLeft = millis;
		while (millisLeft > 0) {
			Thread.sleep(millisLeft);
			long t1 = System.currentTimeMillis();
			millisLeft = millis - (t1 - t0);
		}
	}
}
