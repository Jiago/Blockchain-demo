package au.gov.rba.transaction;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public Transaction(long id, long key, long payerId, long payeeId,
			BigDecimal amount, String comment, long signature,
			long minedTimestamp) {
		super();
		this.id = id;
		this.key = key;
		this.payerId = payerId;
		this.payeeId = payeeId;
		this.amount = amount;
		this.comment = comment;
		this.signature = signature;
		this.minedTimestamp = minedTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public long getMinedTimestamp() {
		return minedTimestamp;
	}

	public void setMinedTimestamp(long minedTimestamp) {
		this.minedTimestamp = minedTimestamp;
	}

	private long id;
	private long signature;
	private long minedTimestamp;

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	private long key;
	private long payerId;
	private long payeeId;
	private BigDecimal amount;
	private String comment;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPayerId() {
		return payerId;
	}

	public void setPayerId(long payerId) {
		this.payerId = payerId;
	}

	public long getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(long payeeId) {
		this.payeeId = payeeId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getComment() {
		return comment;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", signature=" + signature
				+ ", minedTimestamp=" + minedTimestamp + ", key=" + key
				+ ", payerId=" + payerId + ", payeeId=" + payeeId + ", amount="
				+ amount + ", comment=" + comment + "]";
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getSignature() {
		return signature;
	}

	public void setSignature(long signature) {
		this.signature = signature;
	}

}
