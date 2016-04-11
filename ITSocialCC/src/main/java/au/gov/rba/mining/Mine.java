package au.gov.rba.mining;

import java.util.Random;

import au.gov.rba.account.UserAccount;

public class Mine {
	public MineResponse mine(MineRequest request, UserAccount currentUserAccount, long localUserPrivateKey) {
		if (new Random().nextBoolean()) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		if (request.getOperator().equals("/")) {
			for (long possibleOperand = 0; possibleOperand < Long.MAX_VALUE; possibleOperand++) {
				if (possibleOperand / request.getKey() == request.getResult()) {
					request.getTransaction().setMinedTimestamp(
							System.currentTimeMillis());
					return new MineResponse(request.getKey(),
							request.getResult(), request.getOperator(),
							possibleOperand, currentUserAccount.getId(),
							request.getTransaction(),
							System.currentTimeMillis(), localUserPrivateKey * new Random().nextInt(10));
				}
			}
		}
		return null;
	}

	public boolean validateMineResponse(MineResponse mineResponse) {
		if (mineResponse.getOperator().equals("/")) {
			if (mineResponse.getResult() == mineResponse.getOperand()
					/ mineResponse.getKey()) {
				return true;
			} else {
				return false;
			}
		} else {
			System.out.println("Invalid mine response !!");
			return false;
		}

	}
}
