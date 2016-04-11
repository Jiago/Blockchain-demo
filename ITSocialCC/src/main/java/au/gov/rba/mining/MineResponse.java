package au.gov.rba.mining;

import au.gov.rba.transaction.Transaction;

public class MineResponse {
	@Override
	public String toString() {
		return "MineResponse [key=" + key + ", result=" + result
				+ ", operator=" + operator + ", operand=" + operand
				+ ", solverUserId=" + solverUserId + ", transaction="
				+ transaction + ", timestamp=" + timestamp + "]";
	}

	private long key;
	private long result;
	private String operator;
	private long operand;
	private long solverUserId;
	private Transaction transaction;
	private long timestamp;
	private long signature;

	public MineResponse(long key, long result, String operator, long operand,
			long solverUserId, Transaction transaction, long timestamp, long signature) {
		super();
		this.key = key;
		this.result = result;
		this.operator = operator;
		this.operand = operand;
		this.solverUserId = solverUserId;
		this.transaction = transaction;
		this.timestamp = timestamp;
		this.signature = signature;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public long getSolverUserId() {
		return solverUserId;
	}

	public void setSolverUserId(long solverUserId) {
		this.solverUserId = solverUserId;
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

	public void setOperand(long operand) {
		this.operand = operand;
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

	public long getOperand() {
		return operand;
	}

	public long getSignature() {
		return signature;
	}

	public void setSignature(long signature) {
		this.signature = signature;
	}

}
