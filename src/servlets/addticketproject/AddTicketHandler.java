package servlets.addticketproject;

import interfaces.IAddTicketImpl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Ticket;
import service.AddTicketImpl;

@WebServlet("/AddTicketHandler")
@MultipartConfig
public class AddTicketHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		synchronized (this) 
		{
			HttpSession session = request.getSession();
			try
			{
				String ip = request.getRemoteAddr();
				IAddTicketImpl addTicket = new AddTicketImpl();
				Ticket ticket = addTicket.addTicket(ip,request);
				if(ticket.getErrMsg().equalsIgnoreCase("No Error"))
				{
				//	response.sendRedirect("jsp/userDetails.jsp?msg1=Added Successfully!... :)");
					session.setAttribute("queryMsg","Added Successfully :)");
				}
				else
				{
				 //	response.sendRedirect("jsp/userDetails.jsp?msg1="+ticket.getErrMsg());
					session.setAttribute("queryMsg",ticket.getErrMsg());
				}
			}
			catch(Exception ex)
			{
				System.out.println("exception at addticket servlet: "+ex);
				session.setAttribute("queryMsg","Some Exception Occurs!");
			}
			session.setAttribute("openPage", "'../jsp/addTicket.jsp'");
			session.setAttribute("tabToActive","'ticket'");
			response.sendRedirect("jsp/userDetails.jsp");
		}
	}
}
