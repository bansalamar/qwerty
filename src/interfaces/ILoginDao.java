package interfaces;

import java.util.List;

import model.LoginSignup;

public interface ILoginDao {

	public List<Object> getUserObj(String ip,LoginSignup loginSignup) throws Exception;
	public int saveNewUser(String ip,LoginSignup loginSignup) throws Exception;
	public boolean emailAvailability(String ip,String emailID) throws Exception;
	public void setActiveReportingMngr(String ip,LoginSignup signIn);
}
