package servlets.forgotpassword;

import interfaces.IForgotPasswordImpl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.ForgotPasswordImpl;

@WebServlet("/ForgotPasswordHandler")
public class ForgotPasswordHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		synchronized (this) 
		{
			HttpSession session = request.getSession();
			try
			{
				String ip = request.getRemoteAddr();
				String emailId = request.getParameter("email");
				String ans = request.getParameter("quest_ans1");
				IForgotPasswordImpl forgotPassword = new ForgotPasswordImpl();
				String pass = forgotPassword.getPass(ip,emailId,ans);
				if(pass.equals(""))
				{
					session.setAttribute("pass", "No Data Found!");
				}
				else
				{
					session.setAttribute("pass", pass);
				}
			}
			catch(Exception ex)
			{
				session.setAttribute("pass", "Some Exception Occurs!");
			}
			response.sendRedirect("jsp/login.jsp");
		}
	}
}
