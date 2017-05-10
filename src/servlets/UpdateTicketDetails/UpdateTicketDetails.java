package servlets.UpdateTicketDetails;

import interfaces.IEditUserDetailsImpl;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Ticket;
import service.EditUserDetailsImpl;

@WebServlet("/UpdateTicketDetails")
public class UpdateTicketDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		synchronized (this) 
		{
			HttpSession session = request.getSession();
			IEditUserDetailsImpl  eImpl = new EditUserDetailsImpl();
			try
			{
				int statusChanger = Integer.parseInt(request.getParameter("user_id"));
				String tNo = request.getParameter("t_id");
				String remarks = request.getParameter("remarks");
				String btnValue = request.getParameter("btnValue");
				Ticket ticket = eImpl.editTicketDetails(tNo,remarks,btnValue,statusChanger);
				if(ticket.getErrMsg().equals("No Error"))
				{
					//response.sendRedirect("jsp/userDetails.jsp?msg5=Updated Successfully!");
					session.setAttribute("queryMsg","Updated Successfully!");
				}
				else
				{
					//response.sendRedirect("jsp/userDetails.jsp?msg5="+ticket.getErrMsg());
					session.setAttribute("queryMsg",ticket.getErrMsg());
				}
			}
			catch(Exception ex)
			{
				System.out.println("exception on update ticket servlet: "+ex);
				session.setAttribute("queryMsg","Some Exception Occurs!");
			}
			session.setAttribute("openPage", "'../jsp/tickets.jsp'");
			session.setAttribute("tabToActive","'details'");
			response.sendRedirect("jsp/userDetails.jsp");
		}
	}
}
