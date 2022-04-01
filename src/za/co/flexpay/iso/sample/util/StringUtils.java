/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package za.co.flexpay.iso.sample.util;

/**
 * Use to perform operations on String objects
 * 
 * @author daslan
 *
 */
public class StringUtils {

	/**
	 * 
	 * @param in
	 *            byte[] of incoming iso message to and from Socket
	 * @return hex respresentation for byte[] in
	 */
	public static synchronized String byteArrayToHexString(byte in[]) {

		byte ch = 0x00;

		int i = 0;

		if (in == null || in.length <= 0) {
			return null;
		}

		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

		StringBuffer out = new StringBuffer(in.length * 2);

		while (i < in.length) {

			ch = (byte) (in[i] & 0xF0); // Strip off high nibble

			ch = (byte) (ch >>> 4);
			// shift the bits down

			ch = (byte) (ch & 0x0F);
			// must do this is high order bit is on!

			out.append(pseudo[ch]); // convert the nibble to a String Character

			ch = (byte) (in[i] & 0x0F);

			out.append(pseudo[ch]); // convert the nibble to a String Character
									// i

			i++;

		}

		String rslt = new String(out);

		return rslt;

	}

	/**
	 * 
	 * @param s
	 *            hexstring
	 * @return byte[] representation hexstring s
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		String byteArrayString = "";
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));

			byteArrayString = byteArrayString + data[i / 2];
		}
		// logger.debug("byteArrayString<"+byteArrayString+">");
		return data;
	}

	/**
	 * 
	 * @param length
	 *            in decimal format
	 * @return hex representation of length
	 */
	public static String encodeLength(int length) {
		String hexLength = Integer.toHexString(length).toUpperCase();
		StringBuffer sb = new StringBuffer(hexLength);

		// return as hex string length 4
		// using padding util
		StringBufferUtils.pad(sb, 4, "0", PaddingOptionsEnum.INSERT);
		return sb.toString();
	}

}
