package service;

import interfaces.ILoginDao;
import interfaces.ILoginUser;

import java.util.List;

import model.LoginSignup;
import dao.LoginSignupDao;

public class LoginSignupImpl implements ILoginUser {

	@Override
	public LoginSignup logIn(String ip,String aId, String aPass) throws Exception
	{
		String component = "logIn";
		LoginSignup signIn = new LoginSignup();
		boolean isSystemGeneratedUser = false;
		try
		{
			int userId = getId(signIn,aId);
			if(userId <= 0)
			{
				return signIn;
			}
			
			signIn.setuId(userId);
			signIn.setuPassword(aPass);
			
			boolean result = new Misc().checkForEmptyOrNull(signIn,component); 
			if(!result)
			{
				signIn.setErrMsg("Enter Required Values!");
				return signIn;
			}

			Misc.writeToFile(LoginSignupImpl.class,component,ip,
					"[Login-Id: "+signIn.getuId()+"] -: Going to Login.");
			
			ILoginDao dao = new LoginSignupDao();
			List<Object> userObj = dao.getUserObj(ip,signIn);
			
			if(userObj.get(0).toString().equalsIgnoreCase("OK"))
			{
				LoginSignup userFromDB = (LoginSignup) userObj.get(1);
				
				if(userFromDB != null)
				{
//					if(Misc.decodePassword(userFromDB.getuPassword()).equalsIgnoreCase("SYSTEM"))
//						isSystemGeneratedUser = true;
					if(userFromDB.getuPassword().equalsIgnoreCase("SYSTEM"))
						isSystemGeneratedUser = true;
				}
				
				signIn = checkAuthentication(ip,userFromDB,signIn.getuPassword());
				
				if(signIn.getErrMsg().equals("NO ERROR"))
				{
					checkActiveReportingManager(ip,signIn);
				}
				
				if(signIn.getErrMsg().equals("NO ERROR") && 
						signIn.getUserQuest().equals("NIL") && isSystemGeneratedUser)
				{
					Misc.writeToFile(LoginSignupImpl.class,component,ip,
							"[Login-Id: "+signIn.getuId()+"] -: System Generated User!");
					System.out.println("system generated user");
					signIn.setErrMsg(SYSTEM);
				}
			}
						
			return signIn;
		} 
		catch(Exception ex)
		{
			System.out.println("Exception while LogIn:     "+ex);
			Misc.writeToFile(LoginSignupImpl.class,component,ip,
					"[Login-Id: "+signIn.getuId()+"] -: "+ex.toString());
			throw ex;
		}
	}
	
	private int getId(LoginSignup signIn, String aId) 
	{
		int id = 0;
		if (aId == null || aId.equals("") || aId.trim().length() == 0 
				|| aId.length() <= 4)
		{
			signIn.setErrMsg("Invalid UserID");
			return id;
		}
		if(!aId.substring(0,4).equalsIgnoreCase("ISO_"))
		{
			signIn.setErrMsg("Incorrect UserID");
			return id;
		}
		id = Integer.parseInt(aId.substring(4, aId.length()));
		if(id <= 0)
		{
			signIn.setErrMsg("Invalid UserID");
		}
		return id;
	}
	
	
	private void checkActiveReportingManager(String ip,LoginSignup signIn) {
		if(signIn.getManager() != null)
		{
			if(signIn.getProjects() != null)
			{
				ILoginDao dao = new LoginSignupDao();
				dao.setActiveReportingMngr(ip,signIn);
			}
		}
	}
	
	private LoginSignup checkAuthentication(String ip,LoginSignup aUserObj,String aPassword) {
		String passwordDB = "";
		String component = "checkAuthentication";
		if(aUserObj != null)
		{
			passwordDB = aUserObj.getuPassword();
		}
		else
		{
			aUserObj = new LoginSignup();
			aUserObj.setErrMsg("No Record Found!");
			return aUserObj;
		}
		aUserObj.setuPassword("");
		//passwordDB = Misc.decodePassword(passwordDB);
		System.err.println(passwordDB);
		if(aPassword.equals(passwordDB))
		{
			Misc.writeToFile(LoginSignupImpl.class,component,ip,
					"["+aUserObj.getuFName()+" "+aUserObj.getuLName()+"] -: Password matched.");
			aUserObj.setErrMsg("NO ERROR");
			return aUserObj;
		}

		Misc.writeToFile(LoginSignupImpl.class,component,ip,
				"["+aUserObj.getuFName()+" "+aUserObj.getuLName()+"] -: Password Not matched probably");
		
		aUserObj = new LoginSignup();
		aUserObj.setErrMsg("User Id or Password is incorrect!");
		return aUserObj;
	}
	
	@Override
	public LoginSignup signUp(String ip,LoginSignup loginSignup) throws Exception
	{
		String component = "signUp";
		int id = -1;
		try
		{
			ILoginDao dao = new LoginSignupDao();
			LoginSignup user = validateDetails(ip, loginSignup);
			if(!user.getErrMsg().equals(""))
			{
				return user;
			}
			id = dao.saveNewUser(ip, user);
			
		} 
		catch(Exception ex) 
		{
			System.out.println(ex);
			
			Misc.writeToFile(LoginSignupImpl.class,component,ip,
					"["+loginSignup.getuFName()+" "+loginSignup.getuLName()+"] -: "+ex.toString());
			
			throw ex;
		}
		
		if(id > 0)
		{
			loginSignup.setModifiedId(FOR_MODIFIED_ID+id);
			loginSignup.setErrMsg("NO ERROR");
			
			Misc.writeToFile(LoginSignupImpl.class,component,ip,
					"["+loginSignup.getuFName()+" "+loginSignup.getuLName()+
					" Id: "+loginSignup.getuId()+"] -: Sign Up Successfully.");
		}
		else
		{
			loginSignup.setErrMsg("Some Error Occurs....Please Contact Admin Dept!");
			
			Misc.writeToFile(LoginSignupImpl.class,component,ip,
					"["+loginSignup.getuFName()+" "+loginSignup.getuLName()+"] -: Problem while Sign Up!");
		}
		loginSignup.setuPassword("");
		return loginSignup;
	}

	public LoginSignup validateDetails(String ip,LoginSignup loginSignup) throws Exception
	{
		Misc misc = new Misc();
		String component = "validateDetails";
		try{
			loginSignup.setErrMsg("");
			boolean res = misc.checkForEmptyOrNull(loginSignup, "Signup");
			if (!res)
			{
				loginSignup.setErrMsg("Enter Required Details!");
				return loginSignup;
			}
			
			boolean emailRes = misc.validateEmail(loginSignup.getEmailID());
			if (!emailRes) 
			{
				loginSignup.setErrMsg("Enter Valid EmailId!");
				return loginSignup;
			}
			
			ILoginDao dao = new LoginSignupDao();
			boolean emailAvailability = dao.emailAvailability(ip,loginSignup.getEmailID());
			if(emailAvailability)
			{
				loginSignup.setErrMsg("Email ID Already Exists!");
				return loginSignup;
			}

			//String encodePassword = new Misc().encodePassword(loginSignup.getuPassword());
			String encodePassword = loginSignup.getuPassword();
			loginSignup.setuPassword(encodePassword);
		}
		catch(Exception ex){
			Misc.writeToFile(LoginSignupImpl.class,component,ip,ex.toString());
			throw ex;
		}
		return loginSignup;
	}
}
