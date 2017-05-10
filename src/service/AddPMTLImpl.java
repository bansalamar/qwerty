package service;

import java.net.SocketTimeoutException;

import model.LoginSignup;
import dao.AddPMTLDao;
import interfaces.IAddPMTL;
import interfaces.IAddPMTLDao;

public class AddPMTLImpl implements IAddPMTL {
	IAddPMTLDao dao = new AddPMTLDao();
	
	@Override
	public LoginSignup savePM(String fname, String lname, String emailID,
			String projectName) throws Exception 
	{
		String component = "savePM";
		LoginSignup pm = new LoginSignup();
		if(!new Misc().checkforProject(projectName))
		{
			pm.setErrMsg("Project Name should be valid!");
			return pm;
		}
		
		pm.setuFName(fname);
		pm.setuLName(lname);
		pm.setEmailID(emailID);
		pm.setuPassword(password);
		pm.setUserQuest(questAnsw);
		pm.setUserQuestAns(questAnsw);
		
		try
		{
			new LoginSignupImpl().validateDetails("",pm);
			if(!pm.getErrMsg().equals(""))
			{
				System.out.println(pm.getErrMsg());
				return pm;
			}
			dao.addPM(pm,projectName);
			return pm;
		}
		catch(Exception ex){
			System.out.println(ex);
			Misc.writeToFile(AddPMTLImpl.class, component, "MANAGER WORK", ex.toString());
			if(ex instanceof SocketTimeoutException)
			{
				pm.setErrMsg("Drive Connection TimeOut....Please Try Again!");
				return pm;
			}
			else
			{
				throw ex;
			}
		}
	}

	@Override
	public LoginSignup saveTL(String fname, String lname, String emailID,
			String projectName, String subProjectName) throws Exception
	{
		String component  = "saveTL";
		LoginSignup tl = new LoginSignup();
		if(!new Misc().checkforProject(projectName))
		{
			tl.setErrMsg("Project Name should be valid!");
			return tl;
		}
		if(subProjectName == null)
		{
			tl.setErrMsg("Sub Project Name should be valid!");
			return tl;
		}
		tl.setuFName(fname);
		tl.setuLName(lname);
		tl.setEmailID(emailID);
		tl.setUserQuest(questAnsw);
		tl.setUserQuestAns(questAnsw);
		tl.setuPassword(password);
		try
		{
			new LoginSignupImpl().validateDetails("",tl);
			if(!tl.getErrMsg().equals(""))
			{
				return tl;
			}
			dao.addTL(tl,projectName,subProjectName);
			return tl;
		}
		catch(Exception ex){
			System.out.println(ex);
			Misc.writeToFile(AddPMTLImpl.class, component, "MANAGER WORK", ex.toString());
			if(ex instanceof SocketTimeoutException)
			{
				tl.setErrMsg("Problem In Connection With Drive......Please Try Again!");
				return tl;
			}
			else
			{
				throw ex;
			}
		}
	}
}
