package service;

import interfaces.IForgotPasswordDao;
import interfaces.IForgotPasswordImpl;
import dao.ForgotPasswordDao;

public class ForgotPasswordImpl implements IForgotPasswordImpl {
	
	public String getPass(String ip,String emailId,String ans)
	{
		String component = "getPass";
		String pass = "";
		IForgotPasswordDao forgotPasswordDao = new ForgotPasswordDao();
		try
		{
			pass = forgotPasswordDao.retrievePass(ip,emailId,ans);
		}
		catch(Exception ex)
		{
			System.out.println("Exception during forgot password:           "+ex);
			Misc.writeToFile(ForgotPasswordImpl.class, component, "", ex.toString());
		}
		return pass;
	}
}
