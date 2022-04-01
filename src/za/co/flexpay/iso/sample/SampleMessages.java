package za.co.flexpay.iso.sample;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;

import za.co.flexpay.iso.sample.dto.IsoNotificationDTO;
import za.co.flexpay.iso.sample.util.PackagerFactory;
import za.co.flexpay.iso.sample.util.PaddingOptionsEnum;
import za.co.flexpay.iso.sample.util.StringBufferUtils;
import za.co.flexpay.iso.sample.util.StringUtils;

/**
 * The can has sample ISO messages that demonstrate the following:<br/>
 * 1. Pack and Unpack ISO Messages<br/>
 * 2. How to response to different messages with MTI's 08X0, 01X0, 06X0
 * 
 * Disclaimer: These are samples are to Illustrate what message ISO message to
 * and form Flexpay ISO Socket should look like. <br/>
 * The unpacking, processing and packing of these messages is solely the
 * responsibility of the Party that Flexpay Opens a Socket to send ISO messages
 * to. <br/>
 * Flexpay will not be responsible for any financial impact from these sample
 * messages.
 * 
 * 
 * @author daslan
 *
 */
public class SampleMessages {

	// some useful constants
	private static String AVAIL_BAL = "02";
	private static String ACT_BAL = "01";
	private static final String ZAR_CURRENCY_CODE = "710";
	private final static BigDecimal HUNDRED_BIG_D = new BigDecimal(100);

	/**
	 * Run the main class to see the input, processing and <br/>
	 * output of the the sample ISO message's
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			SampleMessages sampleMessage = new SampleMessages();

			// 0800 and 0810
			sampleMessage.generateFudge0810ToSignOn();

			// 0100 and 0110
			sampleMessage.generateFudge0110To0100();

			// 0600 message
			sampleMessage.testGenerate0600SampleMessages();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * generates a fake successful response to a 0800 Sign On message
	 * 
	 * @return the 0810 response represented as a byte[]
	 * @throws Exception
	 */
	public byte[] generateFudge0810ToSignOn() throws Exception {
		// Once Flexpay opens a Socket you can expect a SignOn request like this
		String hexRequest0100 = "004530383030C22000008000000004000005000000003035313030303230383233313332353339313332353339303630313030303230363130423020202020F0F0F0F0F0F0F0F0";

		byte[] byte0110 = approve0X00(hexRequest0100, "0800", "0810");
		return byte0110;

	}

	/**
	 * generates a fake successful response to a 0100 message
	 * 
	 * @return the 0110 response represented as a byte[]
	 * @throws Exception
	 */
	public byte[] generateFudge0110To0100() throws Exception {
		String hexRequest0100 = "00C73031303066FF440108F060003037323135303735383030313030303030303030303030363339393033323231373332303937303636383835353631303030303030303732363139313933323036303332323236303230333232303332313539313230373130363030313431323033323230303031383539373238353430303031343336313031202020202020202020436C69636B73204D696C7061726B204E43434C202020204A484220202020202020202020205A41463038415050524F564544373130373130";

		byte[] byte0110 = approve0X00(hexRequest0100, "0100", "0110");
		return byte0110;

	}

	/**
	 * Demonstrates how a transaction is represented as 0600 ISO message
	 * 
	 * @throws Exception
	 */
	public void testGenerate0600SampleMessages() throws Exception {

		Date currentDate = new Date();

		IsoNotificationDTO isoNotificationDTO = new IsoNotificationDTO();
		isoNotificationDTO.setActualBalance(new BigDecimal("3014.94"));
		isoNotificationDTO.setAvailableBalance(new BigDecimal("3198.71"));
		isoNotificationDTO.setCardId(new BigDecimal(2074680));
		isoNotificationDTO.setDateCreated(currentDate);
		isoNotificationDTO.setDateModified(currentDate);
		isoNotificationDTO.setDescription("CAPITEC   R LUTSCH            ");
		isoNotificationDTO.setRunningBalanceId(new BigDecimal(238057986));
		isoNotificationDTO.setTransactionAmount(new BigDecimal(2000.00));
		isoNotificationDTO.setTransactionDate(currentDate);
		isoNotificationDTO.setTransactionType("01");
		isoNotificationDTO.setTransId(new BigDecimal(290604657));

		//
		packUnpack06XX(isoNotificationDTO, "0600");

	}

	/**
	 * take a transaction and convert to ISO message is byte[]<br/>
	 * NOTE: Unpacking just done to illustrate an avenue to <br/>
	 * pursue for debugging
	 * 
	 * @param isoNotificationDTO
	 *            which hold the transaction info
	 * @param mti
	 *            message type identifier (i.e. 08X0, 01X0, 06X0)
	 * @return SO message in byte[]
	 */
	private byte[] packUnpack06XX(IsoNotificationDTO isoNotificationDTO, String mti) {
		try {
			ISOMsg isoMsg = new ISOMsg();
			ISOPackager customPackager = PackagerFactory.getInstance().getPackager();
			isoMsg.setPackager(customPackager);

			// MTI - message type identifier
			isoMsg.set(0, mti);

			// CardID
			isoMsg.set(2, isoNotificationDTO.getCardId().toPlainString());

			/// Transaction type code
			isoMsg.set(115, isoNotificationDTO.getTransactionType());

			// DE 6—Amount, Cardholder Billing - n12
			isoMsg.set(6, StringBufferUtils.pad(toCents(isoNotificationDTO.getTransactionAmount()), 12, "0", PaddingOptionsEnum.INSERT));

			// DE 11—System Trace Audit Number (STAN)
			String currentDateString = convertDateToString(isoNotificationDTO.getTransactionDate(), "HHmmss");
			isoMsg.set(11, currentDateString);

			// DE 7—Transmission Date and Time - n10
			// Subfield 1—Date n-4 - MMDD
			String currentMMDD = convertDateToString(isoNotificationDTO.getTransactionDate(), "MMdd");

			// Subfield 2—Time n-6 - hhmmss
			isoMsg.set(7, currentMMDD + currentDateString);

			// DE39
			// Use this in the response 0610
			// isoMsg.set(39, "00");

			// DE 37—Retrieval Reference Number
			isoMsg.set(37, isoNotificationDTO.getRunningBalanceId().toPlainString());

			// 00 if successfully received notification, else 96
			// Transaction Description
			isoMsg.set(43, isoNotificationDTO.getDescription().length() > 40 ? isoNotificationDTO.getDescription().substring(0, 39) : isoNotificationDTO.getDescription());

			// include both available and actual balance in DE54
			String balances = parseBalance("01", isoNotificationDTO.getActualBalance(), ACT_BAL) + parseBalance("01", isoNotificationDTO.getAvailableBalance(), AVAIL_BAL);

			// DE 54—Additional Amounts
			isoMsg.set(54, balances);

			isoMsg.dump(System.out, mti + " before pack >>");

			byte[] byte0600 = buildResponseMSG(isoMsg, "0600");

			return byte0600;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * builds an approved ISO message response
	 * 
	 * @param hexRequest0X00
	 *            - incoming ISO message is formatted in hex <br/>
	 *            (i.e. the incoming ISO message was received as byte[] and
	 *            converted to hex)
	 * @return 0X10 response in byteArray
	 * @throws Exception
	 */
	public byte[] approve0X00(String hexRequest0X00, String incomingMti, String outgoingMti) throws Exception {

		// unpack 0X00
		ISOMsg isoMsg0X00 = unpack(hexRequest0X00, incomingMti);

		/**
		 * NOTE: Third party to then extract info from isoMsg0X00<br/>
		 * for internal // processing
		 */

		// assemble 0X10 from isoMsg0X00
		ISOMsg isoMsg0X10 = isoMsg0X00;

		//
		isoMsg0X10.set(0, outgoingMti);

		// set the response code to 00 if approved
		isoMsg0X10.set(39, "00");

		byte[] byte0X10 = buildResponseMSG(isoMsg0X10, outgoingMti);

		return byte0X10;

	}

	/**
	 * structure balance for DE54(i.e. Data Element or Field 54) <br/>
	 * according to Customer Interface spec
	 * 
	 * @param accountType
	 *            the type of account (i.e. savings or cheques)
	 * @param balance
	 * 
	 * @param balanceType
	 *            (i.e. actual or available)
	 * @return
	 */
	private static String parseBalance(String accountType, BigDecimal balance, String balanceType) {
		// set AMOUNT_AND_TRANSACTION to Zero
		// StringBuffer sb = new StringBuffer("0");
		// StringBufferUtils.pad(sb, 12, "0", PaddingOptionsEnum.INSERT);

		// if (logger.isDebugEnabled()) {
		// logger.debug("AVAILABLE_BALANCE<" + amount2.toPlainString() + ">");
		// }

		String balanceInCents = toCents(balance);

		// pad to 12
		balanceInCents = StringBufferUtils.pad(balanceInCents, 12, "0", PaddingOptionsEnum.INSERT);

		// if (logger.isDebugEnabled()) {
		// logger.debug("AVAILABLE_BALANCE(cents with zeros)<" +
		// amount2.toPlainString() + ">");
		// }

		char debitCreditIndicator = 'C';
		if (balance.doubleValue() < 0) {
			debitCreditIndicator = 'D';
		}

		String addtionalAmount = accountType + balanceType + ZAR_CURRENCY_CODE + debitCreditIndicator + balanceInCents;
		return addtionalAmount;
	}

	/**
	 * build the response ISO message in byte[],<br/>
	 * adding the message length at the beginning ,<br/>
	 * which is 2 bytes(i.e. 4 hex in length)
	 * 
	 * @param returnISOMsg
	 *            the iso message to parse usually the response ISO Message
	 * @param mti
	 *            message type identifier to use in DE0 for the response (i.e.
	 *            0810, 0110, 0610)
	 * @return returnISOMsg in byte[]
	 * @throws ISOException
	 * @throws Exception
	 */
	public static byte[] buildResponseMSG(ISOMsg returnISOMsg, String mti) throws ISOException, Exception {

		byte[] returnByteArray;

		ISOPackager customPackager = PackagerFactory.getInstance().getPackager();
		returnISOMsg.setPackager(customPackager);
		byte[] bodyByteArray = returnISOMsg.pack();
		String hexString = StringUtils.byteArrayToHexString(bodyByteArray);

		int hexStringLength = StringUtils.hexStringToByteArray(hexString).length;
		// if ((logger.isDebugEnabled())) {
		// logger.debug("hexStringLength >" + hexStringLength + "<");
		// }
		//

		// convert to hex
		String lengthInHex = StringUtils.encodeLength(hexStringLength);
		// if ((logger.isDebugEnabled())) {
		// logger.debug("lengthInHex >" + lengthInHex + "<");
		// }
		//

		// //insert as "header" to hexString
		hexString = lengthInHex + hexString;
		// if ((logger.isDebugEnabled())) {
		// logger.debug("hexString with length in hex >" + hexString + "<");
		// }

		returnByteArray = StringUtils.hexStringToByteArray(hexString);

		// for Debugging just to check return Message is correct
		ISOMsg returnISOMsg2 = new ISOMsg();
		returnISOMsg2.setPackager(customPackager);
		returnISOMsg2.unpack(returnISOMsg.pack());
		// if (logger.isDebugEnabled()) {
		// logger.debug("returnISOMsg " + mti + " AFTER hex encoding >");
		// returnISOMsg2.dump(System.out, "");
		// }
		//
		//

		String messageToSendHex = StringUtils.byteArrayToHexString(returnByteArray);
		System.out.println("packed message in hex:" + messageToSendHex);

		// unpack for debugging purposes
		unpack(messageToSendHex, mti);

		return returnByteArray;
	}

	/**
	 * given a hexstring unpacks the the ISO message
	 * 
	 * @param sourceString
	 *            hexstring representation fo ISO message
	 * @param mti
	 *            message type identifier (i.e. 0810, 0110, 0610) uses for
	 *            debugging
	 * @return
	 */
	private static ISOMsg unpack(String sourceString, String mti) {
		try {
			ISOMsg isoMsg = new ISOMsg();
			ISOPackager customPackager = PackagerFactory.getInstance().getPackager();
			isoMsg.setPackager(customPackager);

			System.out.println("sourceString.substring(4).length(): " + sourceString.substring(4).length());

			byte[] b = StringUtils.hexStringToByteArray(sourceString.substring(4));
			isoMsg.unpack(b);

			isoMsg.dump(System.out, "[" + mti + "] >>");

			return isoMsg;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 
	 * @param date
	 *            of java.util.Date
	 * @param dateFormatStr
	 * @return a date in String format
	 */
	private static String convertDateToString(java.util.Date date, String dateFormatStr) {
		String strDate = null;
		if (date != null) {
			DateFormat dateformat = new SimpleDateFormat(dateFormatStr);
			strDate = dateformat.format(date);
		}
		return strDate;
	}

	/**
	 * Convert amount into unsigned cents string format
	 * 
	 * @param amount
	 * @return
	 */
	public static String toCents(BigDecimal amount) {
		BigDecimal amountBD = amount.multiply(HUNDRED_BIG_D);
		String amountInCents = String.valueOf(amountBD.abs().toBigInteger().intValue());
		return amountInCents;
	}

}
