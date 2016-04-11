package au.gov.rba.account;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserAccount implements Serializable {

	private static final long serialVersionUID = 1L;

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
		UserAccount other = (UserAccount) obj;
		if (id != other.id)
			return false;
		return true;
	}

	private long id;
	private BigDecimal balance;
	private String userName;
	private long publicKey;
	private long port;

	public UserAccount(long id, BigDecimal balance, String userName,
			long publicKey, long port) {
		super();
		this.id = id;
		this.balance = balance;
		this.userName = userName;
		this.publicKey = publicKey;
		this.port = port;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(long publicKey) {
		this.publicKey = publicKey;
	}

	public long getPort() {
		return port;
	}

	public void setPort(long port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "UserAccount [id=" + id + ", balance=" + balance + ", userName="
				+ userName + ", publicKey=" + publicKey + ", port=" + port
				+ "]";
	}

}
