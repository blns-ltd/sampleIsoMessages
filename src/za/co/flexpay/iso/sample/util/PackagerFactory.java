/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package za.co.flexpay.iso.sample.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

//import org.apache.log4j.Logger;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;

//import za.co.fnds.commons.enums.ApiChannel;
//import za.co.fnds.commons.error.ErrorCode;
//import za.co.fnds.commons.error.FNDSException;
//import za.co.fnds.commons.error.ResponseCode;

/**
 * Return the correct ISO Packager to use
 * 
 * @author akapp
 */
public class PackagerFactory {

	// private static Logger logger = Logger.getLogger(PackagerFactory.class);
	private static final PackagerFactory packagerFactory = new PackagerFactory();
	private static HashMap<String, Object> packagerMap = new HashMap<String, Object>();

	/**
	 * Private constructor
	 */
	private PackagerFactory() {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Return instance of PackagerFactory to requesting user
	 * 
	 * @return packagerFactory
	 */
	public static PackagerFactory getInstance() {
		return packagerFactory;
	}

	/**
	 * This method will return packer for handling the ISO Message received.
	 * 
	 * @return ISOPackager
	 * @throws FNDSException
	 */
	public synchronized ISOPackager getPackager() throws Exception {

		ISOPackager packager = null;
		String filename = null;

		try {
			filename = "mastercardCreditMerc0600.xml";

			Object packagerObject = packagerMap.get("THIRD_PARTY_ISO_CLIENT");
			if (packagerObject == null) {
				/**
				 * Read from file and store in map
				 */

				File file = new File(filename);
				// if ((file != null) && (logger.isDebugEnabled())) {
				// logger.debug("file path:" + file.getAbsolutePath());
				// }

				if (!file.exists()) {
					throw new FileNotFoundException("Invalid " + filename + " file specified." + file.getAbsolutePath());
				}

				// logger.debug("location of " + filename + " : " +
				// file.getAbsolutePath());
				/**
				 * TODO: FileInputStream input needs to be closed, need to find
				 * out where to Close
				 */
				FileInputStream input = new FileInputStream(file);

				packager = new GenericPackager(input);

				// if (logger.isDebugEnabled()) {
				// logger.debug("packagerObject " + apiChannel.name() + "
				// CREATING");
				// }

				packagerMap.put("THIRD_PARTY_ISO_CLIENT", packager);
				System.out.println("file path:" + file.getAbsolutePath());
			} else {
				packager = (GenericPackager) packagerObject;
			}

		} catch (Exception e) {

			e.printStackTrace();
			// logger.error("Error reading " + filename + " file in
			// PackagerFactory", e);
			throw new Exception("Error reading " + filename + " file in PackagerFactory", e);

		}

		return packager;
	}
}
