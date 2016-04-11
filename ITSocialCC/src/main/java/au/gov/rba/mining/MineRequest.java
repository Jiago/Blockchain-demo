package au.gov.rba.mining;

import au.gov.rba.transaction.Transaction;

public class MineRequest {
	@Override
	public String toString() {
		return "MineRequest [key=" + key + ", result=" + result + ", operator="
				+ operator + ", transaction=" + transaction + "]";
	}

	private long key;
	private long result;
	private String operator;
	private Transaction transaction;

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public void setResult(long result) {
		this.result = result;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public long getKey() {
		return key;
	}

	public long getResult() {
		return result;
	}

	public String getOperator() {
		return operator;
	}

	public MineRequest(long key, long result, String operator,
			Transaction transaction) {
		super();
		this.key = key;
		this.result = result;
		this.operator = operator;
		this.transaction = transaction;
	}

}
