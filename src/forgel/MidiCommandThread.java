/*
 * Calls Midi Actions to Hearth in a new Thread.
 * The crittical code place was saved with semaphores to prevent
 * race conditions.
 */
package forgel;

/**
 *
 * @author mafolz
 */
public class MidiCommandThread extends Thread {

	public static volatile boolean double_drives = false;
	public static volatile int spread_drive = 0;

	private byte[] b;
	private long timeStamp;

	public MidiCommandThread(byte[] b, long timeStamp) {
		this.b = b;
		this.timeStamp = timeStamp;
		start();
	}

	public void run() {
		if (b[0] == (byte) 0x90) {
			if (double_drives) {
				Hearth.getInstance().callNote(b[1], timeStamp, spread_drive / 1000.0);
				Hearth.getInstance().callNote(b[1], timeStamp, (spread_drive / 1000.0) * (-1));

			} else {
				Hearth.getInstance().callNote(b[1], timeStamp);
			}
		} else if (b[0] == (byte) 0x80) {
			Hearth.getInstance().stopNote(b[1], timeStamp);
			if (double_drives) {
				Hearth.getInstance().stopNote(b[1], timeStamp);
			}
		}
	}
}
