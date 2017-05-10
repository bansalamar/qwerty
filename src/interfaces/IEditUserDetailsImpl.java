package interfaces;

import javax.servlet.http.HttpServletRequest;

import model.LoginSignup;
import model.Ticket;

public interface IEditUserDetailsImpl {
	
	public String saveUserDetails(String ip,int userID, String proName, String subProName) throws Exception;
	public LoginSignup editUserDetailsSave(String ip,LoginSignup in_SignUp, String newPassword) throws Exception;
	public Ticket editTicketDetails(String tNo, String remarks,String btnValue,int statusChanger);
	public LoginSignup uploadImage(int uId,HttpServletRequest req,String ip) throws Exception;
}
