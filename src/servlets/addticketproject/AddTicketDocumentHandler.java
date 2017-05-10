package servlets.addticketproject;

import java.io.IOException;

import interfaces.IAddTicketImpl;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Ticket;
import service.AddTicketImpl;

@WebServlet("/AddTicketDocumentHandler")
@MultipartConfig
public class AddTicketDocumentHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		synchronized (this) 
		{
			HttpSession session = request.getSession();
			try
			{
				String ip = request.getRemoteAddr();
		
				IAddTicketImpl impl = new AddTicketImpl();
				Ticket ticket = impl.alterTicketDoc(ip,request);
				if(ticket.getErrMsg().equalsIgnoreCase("no Error") || ticket.getErrMsg().equals("") )
				{
					session.setAttribute("queryMsg","Updated Successfully!");
				}
				else
				{
					session.setAttribute("queryMsg",ticket.getErrMsg());
				}
			}	
			catch(Exception ex)
			{
				System.out.println("exception at addticketdocument servlet: "+ex);
				session.setAttribute("queryMsg","Some Exception Occurs!");
			}
			session.setAttribute("openPage", "'../jsp/tickets.jsp'");
			session.setAttribute("tabToActive","'details'");
			response.sendRedirect("jsp/userDetails.jsp");
		}
	}
}
