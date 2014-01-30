/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel.hearth;

import forgel.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author mafolz
 */
public class ComReceiver implements SerialPortEventListener {

	private SerialPort port;
	private StringBuffer receivedBytes;

	public ComReceiver(SerialPort port) {
		super();
		this.port = port;
	}

	@Override
	public void serialEvent(SerialPortEvent serialPortEvent) {
		try {
			readBytes();
		} catch (SerialPortException ex) {
			Logger.getLogger(Hearth.class.getName()).log(Level.SEVERE, null, ex);
		}
	} // serialEvent

	private void readBytes() throws SerialPortException {
		int byteCount = port.getInputBufferBytesCount();
		if (byteCount > 0) {
			storeSerialData(port.readBytes(byteCount));
		}
	} // readBytes

	private void storeSerialData(byte[] msg) {
		if (receivedBytes == null) {
			receivedBytes = new StringBuffer();
		}
		receivedBytes.append(new String(msg, Charset.forName("iso-8859-1")));
		//if (receivedBytes.indexOf(";") > 0) {
		if (receivedBytes.length() > 10) {
			MainWindow.printInCom(receivedBytes.toString());
			receivedBytes = null;
		}
	}
}
