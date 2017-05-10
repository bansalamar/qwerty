package interfaces;

public interface IForgotPasswordDao {

	String retrievePass(String ip,String emailId, String ans) throws Exception;
	
	public String getQuest(String emailId) throws Exception;

}
