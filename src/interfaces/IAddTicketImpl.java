package interfaces;

import javax.servlet.http.HttpServletRequest;

import model.Ticket;


public interface IAddTicketImpl extends IAddTicket {
	
	int TICKET_STATUS_UNCHECKED = 2;
	public Ticket addTicket(String ip,HttpServletRequest req);
	public Ticket alterTicketDoc(String ip,HttpServletRequest req);
}
