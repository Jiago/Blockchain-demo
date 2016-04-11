package au.gov.rba.transaction;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import au.gov.rba.transaction.dto.Transactions;

import com.google.gson.Gson;

@Path("/transactions")
public class TransactionDAO {

	private List<Transaction> transactionList = new ArrayList();

	public static final TransactionDAO INSTANCE = new TransactionDAO();

	public void updateAll(List<Transaction> transactionList) {
		INSTANCE.transactionList = transactionList;
	}

	public void updateAll(Transactions transactions) {
		INSTANCE.transactionList.addAll(transactions.getTransactions());
		Set<Transaction> set = new LinkedHashSet<Transaction>(
				INSTANCE.transactionList);
		INSTANCE.transactionList = new ArrayList<Transaction>(set);
	}

	public void add(Transaction transaction) {
		INSTANCE.transactionList.add(transaction);
	}

	public long getSumOfAllTransactionKeys() {
		long sum = 1;
		for (Transaction transaction : INSTANCE.transactionList) {
			sum += transaction.getKey();
		}
		return sum;
	}

	@GET
	@Path("getall")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllTransactions() {
		//System.out.println("Number of transactions : "
			//	+ INSTANCE.transactionList.size());
		return new Gson().toJson(new Transactions(INSTANCE.transactionList));
	}

	public List<Transaction> getAll() {
		return INSTANCE.transactionList;
	}
}
