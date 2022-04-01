/*
 * jPOS Project [http://jpos.org] Copyright (C) 2000-2012 Alejandro P. Revilla
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULA
 * R PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package za.co.flexpay.iso.sample.util;

import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOStringFieldPackager;
import org.jpos.iso.LiteralInterpreter;
import org.jpos.iso.NullPrefixer;
import org.jpos.iso.Prefixer;
import org.jpos.iso.RightTPadder;

/**
 * Overridden from jPOS Project [http://jpos.org] Copyright (C) 2000-2012
 * Alejandro P. Revilla <br/>
 * For debugging purposes
 * 
 * The IF_CHAR packager pads to the right with spaces, truncating data that is
 * too long. It uses a literal interpreter and has no length prefix.
 * 
 * @author jonathan.oconnor@xcom.de
 * @author apr@cs.com.uy
 * @version $Id$
 * @see ISOComponent
 */
public class DE61SF14 extends ISOStringFieldPackager {
	// private static Logger logger = Logger.getLogger("DE61SF14");

	/** Used for the GenericPackager. */
	public DE61SF14() {
		super(0, null, RightTPadder.SPACE_PADDER, LiteralInterpreter.INSTANCE, NullPrefixer.INSTANCE);
	}

	/**
	 * @param len
	 *            - field len
	 * @param description
	 *            symbolic descrption
	 */
	public DE61SF14(int len, String description) {
		super(len, description, RightTPadder.SPACE_PADDER, LiteralInterpreter.INSTANCE, NullPrefixer.INSTANCE);
		// super(RightTPadder.SPACE_PADDER, LiteralInterpreter.INSTANCE,
		// NullPrefixer.INSTANCE);
		// System.out.println("DE61SF14");
	}

	public int unpack(ISOComponent c, byte[] b, int offset) throws ISOException {
		// System.out.println("DE61SF14 2>>" + b.length);
		// System.out.println("DE61SF14 2 - c.toString())>>" + c.toString());
		// if (logger.isDebugEnabled()) {
		// logger.debug("DE61SF14 2 - offset>>" + offset);
		// }
		Prefixer prefixer = NullPrefixer.INSTANCE;
		LiteralInterpreter interpreter = LiteralInterpreter.INSTANCE;
		int len = prefixer.decodeLength(b, offset);

		// if (logger.isDebugEnabled()) {
		// logger.debug("DE61SF14 2 - len>>" + len);
		// }

		// if (len == -1) {
		// // The prefixer doesn't know how long the field is, so use
		// // maxLength instead
		// len = getLength();
		// System.out.println("DE61SF14 2 - getLength()>>" + len);
		// } else if (getLength() > 0 && len > getLength()) {
		// throw new ISOException("Field length " + len + " too long. Max: " +
		// getLength());
		// }

		int lenLen = prefixer.getPackedLength();
		// if (logger.isDebugEnabled()) {
		// logger.debug("DE61SF14 2 - lenLen>>" + lenLen);
		// }
		if (len == -1) {
			len = b.length - offset;
			// if (logger.isDebugEnabled()) {
			// logger.debug("DE61SF14 2 - length of 61.14>>" + len);
			// }
			// String s = new String(b);
			// System.out.println("DE61SF14 2 - s>>" + s);
		}
		String unpacked = interpreter.uninterpret(b, offset + lenLen, len);

		c.setValue(unpacked);
		return lenLen + interpreter.getPackedLength(len);
	}
}
