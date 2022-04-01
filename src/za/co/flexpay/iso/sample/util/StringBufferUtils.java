/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package za.co.flexpay.iso.sample.util;

/**
 * Utilise for utility methods for String and StringBuffer
 * 
 * @author daslan
 *
 */
public class StringBufferUtils {

	/**
	 * pad s according to config defined by parameters
	 * 
	 * @param s
	 * @param padLimit
	 *            upper bound to pad to
	 * @param padValue
	 * @param padOption
	 *            indicates what type of padding scheme to use
	 * @return padded String
	 */
	public static String pad(String s, int padLimit, String padValue, PaddingOptionsEnum padOption) {
		StringBuffer sb = new StringBuffer(s);
		if (sb.length() > 0) // ( padWhenLengthZero)
		{
			if ((sb != null) && (padLimit > 0)) {
				padLimit = padLimit - sb.length();

				for (int k = 0; k < padLimit; k++) {
					if (padOption.equals(PaddingOptionsEnum.APPEND)) {
						sb.append(padValue);
					} else {
						sb.insert(0, padValue);
					}

				}
			}
		}
		return sb.toString();

	}

	/**
	 * pad sb according to config defined by parameters
	 * 
	 * @param sb
	 * @param padLimit
	 *            upper bound to pad to
	 * @param padValue
	 * @param padOption
	 *            indicates what type of padding scheme to use
	 */
	public static void pad(StringBuffer sb, int padLimit, String padValue, PaddingOptionsEnum padOption) {
		if (sb.length() > 0) // ( padWhenLengthZero)
		{
			if ((sb != null) && (padLimit > 0)) {
				padLimit = padLimit - sb.length();

				for (int k = 0; k < padLimit; k++) {
					if (padOption.equals(PaddingOptionsEnum.APPEND)) {
						sb.append(padValue);
					} else {
						sb.insert(0, padValue);
					}

				}
			}
		}

	}

}
