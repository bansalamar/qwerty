package servlets.loginsignup;

import interfaces.ILoginUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LoginSignup;
import service.LoginSignupImpl;
import service.Misc;

@WebServlet("/LoginSignupHandler")
public class LoginSignupHandler extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	ILoginUser impl = new LoginSignupImpl();
	Misc misc = new Misc();
	String urlForward = "jsp/login.jsp";
	String toPage = "jsp/userDetails.jsp";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		
		synchronized (this) 
		{
			
			String ip = request.getRemoteAddr();
			if (request.getParameter("LogIn") != null) 
			{
				// Login code
				try
				{
					String id = request.getParameter("uId");
					String pass = request.getParameter("uPassword");

					LoginSignup logInStatus = impl.logIn(ip,id,pass);
					if (logInStatus.getErrMsg().equalsIgnoreCase("NO ERROR")) 
					{
						session.setAttribute("user",logInStatus);
						session.setAttribute("openPage", "'../jsp/tickets.jsp'");
						response.sendRedirect(toPage);
					}
					else if(logInStatus.getErrMsg().equalsIgnoreCase(ILoginUser.SYSTEM))
					{
						session.setAttribute("user",logInStatus);
						session.setAttribute("queryMsg",ILoginUser.SYSTEM);
						session.setAttribute("openPage", "'../jsp/systemUser.jsp'");
						response.sendRedirect(toPage);
					}
					else 
					{
						session.setAttribute("queryMsg", logInStatus.getErrMsg());
						response.sendRedirect(urlForward);
					}
				}
				catch(Exception ex)
				{
					System.out.println("exception at Login servlet during login");
					session.setAttribute("queryMsg","Some Exception Occurs!");
					response.sendRedirect(urlForward);
				}
			} 
			else 
			{
				// SignUp code
				try
				{
					LoginSignup loginSignup = new LoginSignup();
					loginSignup.setuFName(request.getParameter("userFName"));
					loginSignup.setuLName(request.getParameter("userLName"));
					loginSignup.setEmailID(request.getParameter("userEmailId"));
					loginSignup.setuPassword(request.getParameter("userPassword"));
					loginSignup.setPositionId(4);
					loginSignup.setUserActiveOrNot('Y');
					loginSignup.setUserQuest(request.getParameter("userQuestion"));
					loginSignup.setUserQuestAns(request.getParameter("userQuestion_Ans"));
					
					LoginSignup userReg = impl.signUp(ip,loginSignup);
					
					if(!userReg.getErrMsg().equalsIgnoreCase("NO ERROR"))
					{
						session.setAttribute("queryMsg", userReg.getErrMsg());
					}
					else 
					{
						session.setAttribute("queryMsg", "Account Created Successfully! THANKS :)");
						session.setAttribute("AccID", "Your User ID:  "+userReg.getModifiedId());
					}
				}
				catch(Exception ex)
				{	
					System.out.println("exception at Login servlet during signup");
					session.setAttribute("queryMsg","Some Exception Occurs!");
				}
				response.sendRedirect(urlForward);
			}	
		}
	}
}
