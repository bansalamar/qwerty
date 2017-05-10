package interfaces;

import model.LoginSignup;

public interface ICompRegImpl {

	LoginSignup completeReg(int userId,String quest, String answ, String pass, String conPass);
}
