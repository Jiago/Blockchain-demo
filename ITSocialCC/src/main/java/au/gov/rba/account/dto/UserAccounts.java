package au.gov.rba.account.dto;

import java.io.Serializable;
import java.util.List;

import au.gov.rba.account.UserAccount;

public class UserAccounts implements Serializable {

	private static final long serialVersionUID = 1L;
	List<UserAccount> userAccounts;

	public UserAccounts(List<UserAccount> userAccountList) {
		super();
		this.userAccounts = userAccountList;
	}

	public List<UserAccount> getUserAccounts() {
		return userAccounts;
	}

	public void setUserAccounts(List<UserAccount> userAccounts) {
		this.userAccounts = userAccounts;
	}

}
