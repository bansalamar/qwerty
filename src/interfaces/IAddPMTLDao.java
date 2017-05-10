package interfaces;

import model.LoginSignup;

public interface IAddPMTLDao {

	int position_ID_TL = 3;
	int position_ID_PM = 2;
	char activeFlag = 'Y';
	char deactiveFlag = 'N';
	
	public LoginSignup addTL(LoginSignup tl, String projectName, String subProjectName) throws Exception;
	public LoginSignup addPM(LoginSignup pm, String projectName) throws Exception;
}
