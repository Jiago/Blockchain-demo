package au.gov.rba.transaction.dto;

import java.io.Serializable;
import java.util.List;

import au.gov.rba.transaction.Transaction;

public class Transactions implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	private List<Transaction> transactions;

	public Transactions(List<Transaction> transactions) {
		super();
		this.transactions = transactions;
	}
}
