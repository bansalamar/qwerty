package interfaces;

import java.util.Map;

import model.Ticket;


public interface IAddTicketDao extends IAddTicket {
	public Ticket saveTicket(String ip,Map<Integer, String> map, Ticket ticket, int uId) throws Exception;
	
	Ticket checkTicketNum(String ip,String tnum) throws Exception;
	
	String getDocId(String ip,String doctype, String tno) throws Exception;
	
	public Ticket updateTicketDoc(String ip,String doctype, String tno, String id) throws Exception;
	
	public String getTicketFolderID(String ip,String tno) throws Exception;
	
	int TICKET_UPDATED_STATUS = 3;
}
