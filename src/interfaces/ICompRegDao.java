package interfaces;

import model.LoginSignup;

public interface ICompRegDao {

	LoginSignup compReg(LoginSignup signup) throws Exception;
}
