package servlets.edituserdetails;

import interfaces.ICompRegImpl;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LoginSignup;

import service.CompRegImpl;

@WebServlet("/CompleteRegHandler")
public class CompleteRegHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		synchronized (this)
		{
			HttpSession session = request.getSession();
			try 
			{
				String quest = request.getParameter("userQuest");
				int userId = Integer.parseInt(request.getParameter("userId"));
				String answ = request.getParameter("userAnsw");
				String pass = request.getParameter("pwrd");
				String conPass = request.getParameter("conpwrd");
				ICompRegImpl comImpl = new CompRegImpl();
				LoginSignup user = comImpl.completeReg(userId, quest, answ,
						pass, conPass);

				if (user.getErrMsg().equalsIgnoreCase("No Error")) {
					// response.sendRedirect("jsp/userDetails.jsp?msg4=OK");
					session.setAttribute("queryMsg", "Updated Successfully!");
					session.setAttribute("openPage", "'../jsp/tickets.jsp'");
					session.setAttribute("tabToActive", "'details'");
				} else {
					// response.sendRedirect("jsp/userDetails.jsp?msg4="+user.getErrMsg());
					session.setAttribute("queryMsg", user.getErrMsg());
					session.setAttribute("openPage", "'../jsp/systemUser.jsp'");
				}
			}
			catch(Exception ex)
			{
				System.out.println("exception during complete reg!");
				session.setAttribute("queryMsg","Some Exception Occurs!");
				session.setAttribute("openPage", "'../jsp/systemUser.jsp'");
				
			}
			response.sendRedirect("jsp/userDetails.jsp");
		}
	}
}
