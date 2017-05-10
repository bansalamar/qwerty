package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import interfaces.IEditUserDetailsDao;
import interfaces.IEditUserDetailsImpl;
import interfaces.ILoginDao;
import model.LoginSignup;
import model.Ticket;
import dao.EditUserDetailsDao;
import dao.LoginSignupDao;

public class EditUserDetailsImpl implements IEditUserDetailsImpl {

	@Override
	public String saveUserDetails(String ip,int userID, String proName, String subProName) throws Exception 
	{
		if(!new Misc().checkforProject(proName)){
			return "proDefault";
		}
		IEditUserDetailsDao editUserDetails = new EditUserDetailsDao();
		String message = editUserDetails.persistUserDetails(ip,userID,proName,subProName);

		return message;
	}

	@Override
	public LoginSignup editUserDetailsSave(String ip,LoginSignup in_SignUp, String newPassword) throws Exception
	{
		if(!new Misc().checkForEmptyOrNull(in_SignUp, "update"))
		{
			in_SignUp.setErrMsg("Enter Required Values!");
			return in_SignUp;
		}
		ILoginDao dao = new LoginSignupDao();
		boolean emailAvailability = false; 
		
		emailAvailability = dao.emailAvailability("",in_SignUp.getEmailID());
		
		IEditUserDetailsDao editUserDetails = new EditUserDetailsDao();
		editUserDetails.editUserDetailsPersist(ip,in_SignUp,newPassword,emailAvailability);
		return in_SignUp;
	}

	@Override
	public Ticket editTicketDetails(String tNo,String remarks,String btnValue,int statusChanger) {
		IEditUserDetailsDao eDao = new EditUserDetailsDao();
		Ticket ticket = new  Ticket();
		try
		{
			if(!checkValues(tNo, remarks, btnValue, statusChanger))
			{
				ticket.setErrMsg("Enter Required Values!");
				return ticket;
			}
			ticket = eDao.updateTicketDetails(tNo,remarks,btnValue,statusChanger);
		}
		catch(Exception ex)
		{
			System.out.println("Exception while updating ticket status:         "+ex);
			ticket.setErrMsg("Some Exception Occurs!");
		}
		return ticket;
	}
	
	boolean checkValues(String tNo,String rem,String btnValue,int stsCngr)
	{
		boolean check = true;
		if(tNo == null || tNo.equals("") || tNo.trim().length() == 0
				||rem == null || rem.equals("") || rem.trim().length() == 0
				||btnValue == null ||btnValue.equals("") || btnValue.trim().length() == 0
				||stsCngr <= 0)
		{
			check = false;	
		}
		return check;
	}

	@Override
	public LoginSignup uploadImage(int uId,HttpServletRequest req,String ip) throws Exception
	{
		LoginSignup loginSignup = new LoginSignup();
		String component = "uploadImage";
		IEditUserDetailsDao eDao = new EditUserDetailsDao();
		try
		{
			Part p = req.getPart("userImage");
			if(p.getSize() == 0)
			{
				loginSignup.setErrMsg("Please Select File!");
				return loginSignup;
			}
			String res = saveImageToLocal(p);
			System.err.println("result"+res);
			if(res.equalsIgnoreCase("ext error"))
			{
				loginSignup.setErrMsg("Please Select jpg/jpeg/png Image!");
				return loginSignup;
			}
			else if(res.equals(""))
			{
				loginSignup.setErrMsg("Some Error Occurs!");
				return loginSignup;
			}
			
			boolean result = eDao.saveImagePath(uId,ip,res);
			
			if(result)
			{
				loginSignup.setErrMsg("No Error");
			}
			else
			{
				loginSignup.setErrMsg("Some Error Occurs!");
			}
			return loginSignup;
		}
		catch(Exception ex)
		{
			System.out.println("Exception while saving image:  "+ex);
			Misc.writeToFile(EditUserDetailsImpl.class, component, ip,ex.toString());
			throw ex;
		}
	}

	private String saveImageToLocal(Part p) throws Exception 
	{
		AddTicketImpl impl = new AddTicketImpl();
		String fileName = "";
		try
		{
			fileName = impl.extractFileName(p);
			System.out.println(fileName);
			if (!fileName.equals("") || fileName != null
					|| fileName.trim().length() > 0) 
			{
				if(fileName.indexOf(".jpg") > 0 || fileName.indexOf(".jpeg") > 0 || fileName.indexOf(".png") > 0)
				{
					p.write(fileName);
				}
				else
				{
					return "ext error";
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("exception while saving image to local"+ex);
			throw ex;
		}
		System.out.println(fileName);
		return fileName;
	}
}
