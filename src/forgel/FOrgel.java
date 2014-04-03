/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel;

import forgel.gui.MainWindow;

/**
 *
 * @author mafolz
 */
public class FOrgel {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// precalculate Frequencies
		FrequencyCalculator.calculate();
		//for (short i = -60; i < 80; i++) {
		//System.out.println("" + i + "|" + FrequencyCalculator.get(i));
		//}
		MainWindow.getInstance();

	}
}
