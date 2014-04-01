/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel;

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

	private static final double FREQUENCIES[] = {
		2.4306795603, // c-3
		2.5752152884,
		2.7283455581,
		2.8905814274,
		3.0624643436,
		3.2445679498,
		3.4375,
		3.6419043869,
		3.8584632911,
		4.0878994578,
		4.330978609,
		4.5885119987, //c-2
		4.8613591207,
		5.1504305768,
		5.4566911161,
		5.7811628549,
		6.1249286872,
		6.4891358997,
		6.875,
		7.2838087737,
		7.7169265821,
		8.1757989156,
		8.661957218, //C-1
		9.1770239974,
		9.7227182413,
		10.3008611535,
		10.9133822323,
		11.5623257097,
		12.2498573744,
		12.9782717994,
		13.75,
		14.5676175474,
		15.4338531643,
		16.3515978313,
		17.3239144361,
		18.3540479948,
		19.4454364826,
		20.6017223071,
		21.8267644646,
		23.1246514195,
		24.4997147489,
		25.9565435987,
		27.5, //A0
		29.1352,
		30.8677,
		32.7032,
		34.6478,
		36.7081,
		38.8909,
		41.2034,
		43.6535,
		46.2493,
		48.9994,
		51.9131,
		55.0,
		58.2705,
		61.7354,
		65.4064,
		69.2957,
		73.4162,
		77.7817,
		82.4069,
		87.3071,
		92.4986,
		97.9989,
		103.826,
		110.0,
		116.541,
		123.471,
		130.813,
		138.591,
		146.832, // Offset 60
		155.563,
		164.814,
		174.614,
		184.997,
		195.998,
		207.652,
		220.0,
		233.082,
		246.942,
		261.626,
		277.183,
		293.665,
		311.127,
		329.628,
		349.228,
		369.994,
		391.995,
		415.305,
		440.0,
		466.164,
		493.883,
		523.251,
		554.3651235866,
		587.3293892399,
		622.2538121323,
		659.2549492786,
		698.4562885343,
		739.9886607253,
		783.9906762828,
		830.6091878434,
		879.9997803559,
		932.3272903313,
		987.7663559701,
		1046.5020000001,
		1108.7302471733,
		1174.6587784799,
		1244.5076242647,
		1318.5098985572,
		1396.9125770688,
		1479.9773214507,
		1567.9813525657,
		1661.218375687,
		1759.9995607119,
		1864.6545806628,
		1975.5327119402,
		2093.0040000002,
		2217.4604943466,
		2349.31755696,
		2489.0152485295,
		2637.0197971145,
		2793.8251541377,
		2959.9546429015,
		3135.9627051316,
		3322.4367513741,
		3519.9991214239,
		3729.3091613258,
		3951.0654238807,
		4186.0080000007,
		4434.9209886935,
		4698.6351139202
	};
	/**
	 *****************************************************************
	 * Instance Methods
	 * ****************************************************************
	 */
	// see http://de.wikipedia.org/wiki/Frequenzen_der_gleichstufigen_Stimmung
	// 49 A4 Kammerton entspricht midi 69
	private short noteOffset; // 40 middle C
	private Map<Short, Double> noteMap;
	private boolean noteOn;
	private long timeStamp;
	private int index;

	public Floppy(int index) {
		this.noteOffset = 60;
		this.index = index;
		this.noteMap = new HashMap<>();
		mapFrequencies();
	}

	public int getIndex() {
		return index;
	}

	public void mapFrequencies() {
		short noteCount = noteOffset;
		for (double frequence : FREQUENCIES) {
			noteMap.put(noteCount, frequence);
			noteCount++;
		}
	} // mapFrequencies

	public boolean isIdle() {
		return !noteOn;
	}

	public byte[] play(short note, long timeStamp) {
		this.timeStamp = timeStamp;
		byte buffer[] = {};
		double hertz = getHertz(note);

		if (hertz < 0) {
			return buffer;
		}
		try {
			buffer = ("" + this.index + "-" + Double.toString(hertz) + ";").
							getBytes("iso-8859-1");

			//buffer = (Double.toString(hertz) + ";").
			//				getBytes("iso-8859-1");
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
			//buffer = ("0;").getBytes("iso-8859-1");
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
			//buffer = ("0;").getBytes("iso-8859-1");
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
