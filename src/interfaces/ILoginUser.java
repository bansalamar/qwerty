package interfaces;

import model.LoginSignup;

public interface ILoginUser {

	String FOR_MODIFIED_ID = "ISO_";
	int CHAIRMAN_ID = 1;
	int PROJECT_MANAGER_ID = 2;
	int TEAM_LEAD_ID = 3;
	int DEVELOPER_ID = 4;
	int T_ARRAY_LENGTH = 7;
	
	final String SYSTEM = "SYSTEM_USER";

	LoginSignup logIn(String ip, String aId, String aPass) throws Exception;

	LoginSignup signUp(String ip, LoginSignup loginSignup) throws Exception;
	
}
