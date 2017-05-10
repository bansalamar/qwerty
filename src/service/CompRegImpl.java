package service;

import dao.CompRegDao;
import model.LoginSignup;
import interfaces.ICompRegDao;
import interfaces.ICompRegImpl;

public class CompRegImpl implements ICompRegImpl {

	@Override
	public LoginSignup completeReg(int userId,String quest, String answ, String pass,
			String conPass) {
		
		LoginSignup signup = new LoginSignup();
		String component = "completeReg";
		try
		{
			if (!matchPass(pass, conPass)) {
				signup.setErrMsg("Passwords Must Match!");
				return signup;
			}

			signup.setuId(userId);
			signup.setUserQuest(quest);
			signup.setUserQuestAns(answ);
			signup.setuPassword(pass);

			if (!new Misc().checkForEmptyOrNull(signup, "edit")) {
				signup.setErrMsg("Enter Required Details!");
				return signup;
			}

			ICompRegDao comDao = new CompRegDao();
			signup = comDao.compReg(signup);
		}
		catch(Exception ex)
		{
			System.out.println("Exception while edit details of system generated user        "+ex);
			Misc.writeToFile(CompRegImpl.class, component, "TL OR PM Work", ex.toString());
			signup.setErrMsg("Some Exception Occurs!.... :(");
		}
		return signup;
	}

	public boolean matchPass(String aPass,String aConPass) {
		boolean match = false;
		if(aPass.equals(aConPass))
			match = true;
		
		return match;
	}
}
