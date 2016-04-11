package au.gov.rba.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import au.gov.rba.account.dto.UserAccounts;

import com.google.gson.Gson;

@Path("/users")
public class UserAccountDAO {

	private List<UserAccount> userAccountList = new ArrayList();
	public static final UserAccountDAO INSTANCE = new UserAccountDAO();

	public void updateAll(List<UserAccount> userAccountList) {
		INSTANCE.userAccountList = userAccountList;
	}

	public void updateAll(UserAccounts userAccounts) {
		INSTANCE.userAccountList.addAll(userAccounts.getUserAccounts());
		Set<UserAccount> tmpSet = new LinkedHashSet<UserAccount>(
				INSTANCE.userAccountList);
		INSTANCE.userAccountList = new ArrayList<UserAccount>(tmpSet);
	}

	public void add(UserAccount userAccount) {
		INSTANCE.userAccountList.add(userAccount);
	}

	public void updateBalance(UserAccount userAccountParam, BigDecimal balance) {
		for (UserAccount userAccount : INSTANCE.userAccountList) {
			if (userAccount.getId() == userAccountParam.getId()) {
				userAccount.setBalance(balance);
			}
		}
	}

	public UserAccount findById(long id) {
		for (UserAccount userAccount : INSTANCE.userAccountList) {
			if (userAccount.getId() == id) {
				return userAccount;
			}
		}
		return null;
	}

	@GET
	@Path("getall")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllAccount() {
		return new Gson().toJson(new UserAccounts(INSTANCE.userAccountList));
	}

	public List<UserAccount> getAll() {
		return INSTANCE.userAccountList;
	}
}
