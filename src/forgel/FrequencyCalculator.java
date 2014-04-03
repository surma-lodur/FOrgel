/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel;

import java.util.HashMap;

/**
 *
 * @author mafolz
 */
public class FrequencyCalculator {

	static double base_tone = 27.5; //A0

	static HashMap<Integer, Double> frequency_map = new HashMap();

	public static void calculate() {
		for (int i = 0; i < 220; i++) {
			double value = 440.0 * Math.pow(2.0, ((i - 49.0) / 12.0));
			frequency_map.put(i, value);
		}

		for (int i = 0; i > -220; i--) {
			double value = 440.0 * Math.pow(2.0, ((i - 49.0) / 12.0));
			frequency_map.put(i, value);
		}
	}

	public static double get(int index) {
		return frequency_map.get(index);
	}
}
