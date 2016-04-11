package au.gov.rba.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Random;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import au.gov.rba.account.UserAccount;
import au.gov.rba.account.UserAccountDAO;
import au.gov.rba.account.dto.UserAccounts;
import au.gov.rba.comm.HttpClient;
import au.gov.rba.mining.Mine;
import au.gov.rba.mining.MineRequest;
import au.gov.rba.mining.MineResponse;
import au.gov.rba.transaction.Transaction;
import au.gov.rba.transaction.TransactionDAO;
import au.gov.rba.transaction.dto.Transactions;

import com.google.gson.Gson;

@Path("/requests")
public class Starter {

	final static UserAccount localUser = new UserAccount(0, new BigDecimal(
			"10000"), "TestUser", 10, 8080);
	final static TransactionDAO transactionDAO = TransactionDAO.INSTANCE;
	final static UserAccountDAO userAccountDAO = UserAccountDAO.INSTANCE;
	final static Mine mine = new Mine();
	static String serDserDir = "";
	static long privateKey = 0;

	public static void main(final String args[]) {
		localUser.setPort(Integer.parseInt(args[0]));
		localUser.setId(Long.parseLong(args[1]));
		localUser.setPublicKey(Long.parseLong(args[4]));
		userAccountDAO.add(localUser);
		serDserDir = args[2];
		privateKey = Long.parseLong(args[3]);

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					startJetty(Integer.parseInt(args[0]));
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		});
		t.start();

		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(serDserDir + "/userAccounts"
					+ localUser.getId() + ".ser");

			ObjectInputStream in = new ObjectInputStream(fileIn);
			UserAccountDAO.INSTANCE.updateAll((UserAccounts) in.readObject());
			in.close();
			fileIn.close();

			fileIn = new FileInputStream(serDserDir + "/transactions"
					+ localUser.getId() + ".ser");

			in = new ObjectInputStream(fileIn);
			TransactionDAO.INSTANCE.updateAll((Transactions) in.readObject());
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		while (true) {
			printOptions();
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			switch (option) {
			case 1:
				System.out.print("Enter Name : ");
				String userName = sc.next();
				System.out.print("Enter Public Key: ");
				int publicKey = sc.nextInt();
				System.out.print("Enter Port: ");
				int port = sc.nextInt();
				addUser(new Gson().toJson(new UserAccount(userAccountDAO
						.getAll().size(), new BigDecimal("10000"), userName,
						publicKey, port)));
				break;
			case 2:
				System.out.print("Enter Payee Id : ");
				long payeeId = sc.nextLong();
				System.out.print("Enter Amount : ");
				String transferAmout = sc.next();
				System.out.print("Enter Comment: ");
				sc.useDelimiter(System.getProperty("line.separator"));
				String userComment = sc.next();
				long signature = privateKey * new Random().nextInt(10);
				Transaction transaction = generateTransaction(new Transaction(
						TransactionDAO.INSTANCE.getAll().size(),
						transactionDAO.getSumOfAllTransactionKeys(),
						localUser.getId(), payeeId, new BigDecimal(
								transferAmout), userComment, signature, 0));
				MineRequest request = generateMineRequest(transaction);
				for (UserAccount userAccount : UserAccountDAO.INSTANCE.getAll()) {
					try {
						if (userAccount.getId() != localUser.getId()) {
							new HttpClient().sendRequest("http://localhost:"
									+ userAccount.getPort()
									+ "/requests/mineRequest", "POST", "",
									new Gson().toJson(request));
						}
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}

				break;
			case 3:
				System.out.println(userAccountDAO.getAll());
				break;
			case 4:
				System.out.println(transactionDAO.getAll());
				break;
			case 5:
				System.out.print("Enter Payee Id : ");
				payeeId = sc.nextLong();
				System.out.print("Enter Amount : ");
				transferAmout = sc.next();
				System.out.print("Enter Comment: ");
				sc.useDelimiter(System.getProperty("line.separator"));
				userComment = sc.next();
				signature = localUser.getPublicKey();
				transaction = generateTransaction(new Transaction(
						(long) new Random(100).nextInt(),
						transactionDAO.getSumOfAllTransactionKeys(),
						localUser.getId(), payeeId, new BigDecimal(
								transferAmout), userComment, signature, 0));
				request = generateMineRequest(transaction);
				request.getTransaction().setSignature(-2322);
				System.out.println("Request : " + request);
				if (isValidateMineRequest(request)) {
					MineResponse mineResponse = processMineRequest(request);
					validateAndProcessMineResponse(mineResponse, false);
				} else {
					System.out
							.println("Invalid user signature in mine request!!");
				}
				break;
			case 6:
				System.out.print("Enter Payee Id : ");
				payeeId = sc.nextLong();
				System.out.print("Enter Amount : ");
				transferAmout = sc.next();
				System.out.print("Enter Comment: ");
				sc.useDelimiter(System.getProperty("line.separator"));
				userComment = sc.next();
				signature = localUser.getPublicKey();
				transaction = generateTransaction(new Transaction(
						(long) new Random(100).nextInt(),
						transactionDAO.getSumOfAllTransactionKeys(),
						localUser.getId(), payeeId, new BigDecimal(
								transferAmout), userComment, signature, 0));
				request = generateMineRequest(transaction);
				request.getTransaction().setKey(204848473);
				System.out.println("Request : " + request);
				if (isValidateMineRequest(request)) {
					MineResponse mineResponse = processMineRequest(request);
					validateAndProcessMineResponse(mineResponse, false);
				} else {
					System.out
							.println("Invalid blockchain data in mine request!!");
				}
				break;
			case 7:
				System.out.print("Enter Payee Id : ");
				payeeId = sc.nextLong();
				System.out.print("Enter Amount : ");
				transferAmout = sc.next();
				System.out.print("Enter Comment: ");
				sc.useDelimiter(System.getProperty("line.separator"));
				userComment = sc.next();
				signature = localUser.getPublicKey();
				transaction = generateTransaction(new Transaction(
						(long) new Random(100).nextInt(),
						transactionDAO.getSumOfAllTransactionKeys(),
						localUser.getId(), payeeId, new BigDecimal(
								transferAmout), userComment, signature, 0));

				request = generateMineRequest(transaction);
				if (isValidateMineRequest(request)) {
					MineResponse mineResponse = processMineRequest(request);
					mineResponse.setOperand(0);
					System.out.println("Response : " + mineResponse);
					validateAndProcessMineResponse(mineResponse, false);
				} else {
					System.out.println("Invalid mine request !!");
				}
				break;
			case 8:
				try {
					for (int i = 8080; i < 8100; i++) {

						String allAccounts = new HttpClient().sendRequest(
								"http://localhost:" + i + "/users/getall",
								"GET", "", "");
						UserAccountDAO.INSTANCE.updateAll(new Gson().fromJson(
								allAccounts, UserAccounts.class));
					}
					break;

				} catch (IOException e) {
					// e.printStackTrace();

				}
				break;
			case 9:
				for (int i = 8080; i < 8100; i++) {
					try {

						String allTransactions = new HttpClient()
								.sendRequest("http://localhost:" + i
										+ "/transactions/getall", "GET", "", "");
						TransactionDAO.INSTANCE.updateAll(new Gson().fromJson(
								allTransactions, Transactions.class));

					} catch (IOException e) {
						// e.printStackTrace();
					}

				}
				break;
			case 999:
				try {
					FileOutputStream fileOut = new FileOutputStream(serDserDir
							+ "/userAccounts" + localUser.getId() + ".ser");
					ObjectOutputStream out = new ObjectOutputStream(fileOut);

					out.writeObject(new UserAccounts(UserAccountDAO.INSTANCE
							.getAll()));
					out.close();
					fileOut.close();

					fileOut = new FileOutputStream(serDserDir + "/transactions"
							+ +localUser.getId() + ".ser");
					out = new ObjectOutputStream(fileOut);

					out.writeObject(new Transactions(TransactionDAO.INSTANCE
							.getAll()));
					out.close();
					fileOut.close();

				} catch (IOException e) {
				} finally {
					System.exit(0);
				}
				break;

			}
		}
	}

	@POST
	@Path("/mineRequest")
	@Consumes(MediaType.TEXT_PLAIN)
	public void mineRequest(String mineRequest) {
		System.out.println("Mine Request received : " + mineRequest);
		MineRequest request = new Gson().fromJson(mineRequest,
				MineRequest.class);
		System.out.println("Mine request : " + request);
		if (isValidateMineRequest(request)) {
			MineResponse mineResponse = processMineRequest(request);
			System.out.println("Mine Response : " + mineResponse);
			try {
				UserAccount senderUserAccount = UserAccountDAO.INSTANCE
						.findById(request.getTransaction().getPayerId());
				new HttpClient().sendRequest("http://localhost:"
						+ senderUserAccount.getPort()
						+ "/requests/mineResponse", "POST", "",
						new Gson().toJson(mineResponse));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		} else {
			System.out.println("Invalid mine request !!");
		}
	}

	@POST
	@Path("/mineResponse")
	@Consumes(MediaType.TEXT_PLAIN)
	public void mineResponse(String mineResponse) {
		System.out.println("Mine Response : " + mineResponse);
		validateAndProcessMineResponse(
				new Gson().fromJson(mineResponse, MineResponse.class), true);
	}

	@POST
	@Path("/updateChain")
	@Consumes(MediaType.TEXT_PLAIN)
	public void updateChain(String mineResponse) {
		System.out.println("Update Chain Mine Response : " + mineResponse);
		validateAndProcessMineResponse(
				new Gson().fromJson(mineResponse, MineResponse.class), false);
	}

	public static boolean isValidateMineRequest(MineRequest request) {
		UserAccount senderUserAccount = userAccountDAO.findById(request
				.getTransaction().getPayerId());
		long signature = request.getTransaction().getSignature();
		if (signature % senderUserAccount.getPublicKey() != 0) {
			System.out.println("Unauthenticated user !!");
			return false;
		}

		if (senderUserAccount.getBalance()
				.subtract(request.getTransaction().getAmount())
				.compareTo(BigDecimal.ZERO) < 0) {
			System.out.println("Invalid amount !!");
			return false;
		}

		if (transactionDAO.getSumOfAllTransactionKeys() != request
				.getTransaction().getKey()) {
			System.out.println("Invalid blockchain request !!");
			return false;
		}

		return true;
	}

	private static void validateAndProcessMineResponse(
			MineResponse mineResponse, boolean sendUpdate) {
		UserAccount senderUserAccount = userAccountDAO.findById(mineResponse
				.getTransaction().getPayerId());
		long signature = mineResponse.getTransaction().getSignature();
		boolean isValid = true;
		if (signature % senderUserAccount.getPublicKey() != 0) {
			System.out.println("Unauthenticated sender invalid mine response !!");
			isValid = false;
		}
		UserAccount minerUserAccount = userAccountDAO.findById(mineResponse.getSolverUserId());
		signature = mineResponse.getSignature();
		if(signature % minerUserAccount.getPublicKey() != 0) {
			System.out.println("Unauthenticated miner invalid mine response !!");
			isValid = false;
		}
		if (isValid) {
			isValid = mine.validateMineResponse(mineResponse);
			if (isValid) {
				transactionDAO.add(mineResponse.getTransaction());
				UserAccount payer = userAccountDAO.findById(mineResponse
						.getTransaction().getPayerId());
				UserAccount payee = userAccountDAO.findById(mineResponse
						.getTransaction().getPayeeId());
				userAccountDAO.updateBalance(payer, payer.getBalance()
						.subtract(mineResponse.getTransaction().getAmount()));
				userAccountDAO.updateBalance(
						payee,
						payee.getBalance().add(
								mineResponse.getTransaction().getAmount()));
				UserAccount solver = UserAccountDAO.INSTANCE
						.findById(mineResponse.getSolverUserId());
				userAccountDAO.updateBalance(solver,
						solver.getBalance().add(new BigDecimal("100")));

				if (sendUpdate) {

					for (UserAccount userAccount : UserAccountDAO.INSTANCE
							.getAll()) {
						if (userAccount.getId() != localUser.getId()) {
							try {
								new HttpClient().sendRequest(
										"http://localhost:"
												+ userAccount.getPort()
												+ "/requests/updateChain",
										"POST", "",
										new Gson().toJson(mineResponse));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								// e.printStackTrace();
							}
						}
					}
				}
			} else {
				System.out.println("Invalid mine response !!");
			}
		}

	}

	private static void printOptions() {
		System.out.println("********* Welcome ************");
		System.out.println("1. Add User");
		System.out.println("2. Pay");
		System.out.println("3. Balances");
		System.out.println("4. Transactions");
		System.out.println("5. Invalid user signature in mine request");
		System.out.println("6. Invalid blockchain data in mine request");
		System.out.println("7. Invalid mine response");
		System.out.println("8. Update All Users");
		System.out.println("9. Update All Transactions");
		System.out.println("999. Exit");
	}

	@POST
	@Path("post")
	@Consumes(MediaType.APPLICATION_JSON)
	public static void addUser(@QueryParam("userAccount") String userAccount) {
		userAccountDAO.add(new Gson().fromJson(userAccount, UserAccount.class));
		System.out.println(userAccountDAO.getAll());
	}

	public static Transaction generateTransaction(Transaction transaction) {
		return transaction;
	}

	public static MineRequest generateMineRequest(Transaction transaction) {
		return new MineRequest(transaction.getKey(), 40, "/", transaction);
	}

	public static MineResponse processMineRequest(MineRequest request) {
		return mine.mine(request, localUser, privateKey);
	}

	public static void startJetty(int port) throws Exception {

		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(port);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(
				org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		// Tells the Jersey Servlet which REST service/class to load.
		jerseyServlet.setInitParameter(
				"jersey.config.server.provider.classnames",
				UserAccountDAO.class.getCanonicalName() + ","
						+ TransactionDAO.class.getCanonicalName() + ","
						+ Mine.class.getCanonicalName() + ","
						+ Starter.class.getCanonicalName());

		try {
			jettyServer.start();
			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}
	}
}
