package interfaces;

import model.LoginSignup;

public interface IAddPMTL {
	
	String password = "SYSTEM";
	String questAnsw  = "NIL"; 
	
	public LoginSignup savePM(String fname, String lname, String emailID, String projectName) throws Exception;
	public LoginSignup saveTL(String fname, String lname, String emailID, String projectName, String subProjectName) throws Exception;
}
