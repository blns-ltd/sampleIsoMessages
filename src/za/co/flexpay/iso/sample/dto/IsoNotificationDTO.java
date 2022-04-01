/**
 *
 */
package za.co.flexpay.iso.sample.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Used to hold date that needs to transferred <br/>
 * to and from 3rd party ISO Client
 * 
 * @author daslan
 */
public class IsoNotificationDTO {

	private static final long serialVersionUID = 3103259995492808249L;

	private BigDecimal transId;
	private BigDecimal cardId;
	private String description;
	private String transactionType;
	private Date transactionDate;
	private BigDecimal transactionAmount;

	private BigDecimal runningBalanceId;

	private BigDecimal actualBalance;
	private BigDecimal availableBalance;

	private BigDecimal previousBalance;

	private Date dateCreated;
	private Date dateModified;

	public BigDecimal getTransId() {
		return transId;
	}

	public void setTransId(BigDecimal transId) {
		this.transId = transId;
	}

	public BigDecimal getCardId() {
		return cardId;
	}

	public void setCardId(BigDecimal cardId) {
		this.cardId = cardId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public BigDecimal getRunningBalanceId() {
		return runningBalanceId;
	}

	public void setRunningBalanceId(BigDecimal runningBalanceId) {
		this.runningBalanceId = runningBalanceId;
	}

	public BigDecimal getActualBalance() {
		return actualBalance;
	}

	public void setActualBalance(BigDecimal actualBalance) {
		this.actualBalance = actualBalance;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public BigDecimal getPreviousBalance() {
		return previousBalance;
	}

	public void setPreviousBalance(BigDecimal previousBalance) {
		this.previousBalance = previousBalance;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

}
