package interfaces;

import model.LoginSignup;
import model.Ticket;

import org.hibernate.Session;

public interface IEditUserDetailsDao {
	
	int TICKET_STATUS_PRECISE = 1;
	int TICKET_STATUS_BIASED = 0;
	
	public String persistUserDetails(String ip,int userID, String proName,String subProName) throws Exception;
	
	public int findReportingManager(Session session, int projectId,int subProjectId, int positionId);
	
	public int findHigherReportingMng(Session session, int positionId,int projectId);
	
	public LoginSignup editUserDetailsPersist(String ip,LoginSignup in_SignUp, String newPassword,boolean emailExist) throws Exception;
	
	public Ticket updateTicketDetails(String tNo,String remarks,String btnValue,int statusChanger) throws Exception;
	
	public boolean saveImagePath(int uId,String ip,String path) throws Exception;
	
}
