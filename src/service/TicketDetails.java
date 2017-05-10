package service;

import interfaces.IAddTicketDao;
import interfaces.IAddTicketImpl;
import interfaces.IEditUserDetailsDao;
import interfaces.ILoginUser;

import java.util.Iterator;
import java.util.Set;

import model.LoginSignup;
import model.Ticket;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import util.HibernateUtils;

public class TicketDetails {
	SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
	String sHtml = "";
	
	public String getTicketDetails(int userId) 
	{
		String component = "getTicketDetails";
		if (sessionFactory == null) 
		{
			System.out.println("Session Factory is null!");
			Misc.writeToFile(TicketDetails.class, component,"", "Session Factory Is NULL!");
			return "Some Error Occurs!";
		}
		
		Session session = null;
		
		try
		{
			session = sessionFactory.openSession();
			LoginSignup in_SignUp = (LoginSignup) session.get(LoginSignup.class, userId);
			if(in_SignUp != null)
			{
				System.out.println("user in ticekt details:  "+in_SignUp);
				if(in_SignUp.getPositionId() != ILoginUser.DEVELOPER_ID)
				{
					getHierarchy(in_SignUp, userId);
				}
				else
				{
					System.out.println("single user");
					getSingleUser(in_SignUp);
				}	
				sHtml += "</div>";
				session.close();
				System.out.println(sHtml);
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception while getting ticket details: "+ex);
			Misc.writeToFile(TicketDetails.class, component, "", ex.toString());
		}
		finally
		{
			if(session != null)
				if(session.isOpen())
					session.close();
		}
		return sHtml;
	}

	private void  getSingleUser(LoginSignup in_SignUp) 
	{
		sHtml = "<div class='table-responsive'>"
				+ "<table class='table' style='margin-top: 20px;' id='myTable'>"
				+ "<thead>" + "<tr>" + "<th>#</th>" + "<th>TICKET NO.</th>"
				+ "<th>DESCRIPTION</th>" + "<th>SUB DATE</th>"
				+ "<th>STATUS</th>"
				+ "<th>SUB TO</th>" + "</tr>" + "</thead>" + "<tbody>";

		Set<Ticket> tickets = getTickets(in_SignUp);
		Iterator<Ticket> iterator = tickets.iterator();
		int i = 0;
		String statusChangerName = "";
		String status = "Not Submitted Yet";
		String radStatus = status;
		String esStatus = status;
		String crStatus = status;
		LoginSignup statusChanger;
		while (iterator.hasNext()) {
			System.out.println("in ");
			i++;
			Ticket ticket = (Ticket) iterator.next();
			if(ticket.getRadId() != null)
			{
				radStatus = "Submitted";
			}
			if(ticket.getEsDocId() != null)
			{	
				esStatus = "Submitted";
			}
			if(ticket.getCrDocId() != null)
			{
				crStatus = "Submitted";
			}
			statusChanger = ticket.getStatusChanger(); 
			if(statusChanger != null)
			{
				statusChangerName = statusChanger.getuFName()+" "+statusChanger.getuLName();
			}
			sHtml += "<tr";
			if(ticket.getStatus() == IEditUserDetailsDao.TICKET_STATUS_BIASED){
				sHtml += " class='alert alert-danger'";
			}
			else if(ticket.getStatus() == IEditUserDetailsDao.TICKET_STATUS_PRECISE)
			{
				sHtml += " class='alert alert-success'";
			}else if(ticket.getStatus() == IAddTicketImpl.TICKET_STATUS_UNCHECKED)
			{
				sHtml += " class='alert alert-info'";
			}
			else if(ticket.getStatus() == IAddTicketDao.TICKET_UPDATED_STATUS)
			{
				sHtml += " class='alert alert-warning'";
			}
			sHtml += " data-toggle='modal' data-target='#myModal1' style='cursor: pointer;'>" + "<td>" + i + "</td>" + "<td id='tno'>"
					+ ticket.getTicketNum() + "</td>" + "<td>"
					+ ticket.getDesciption() + "</td>" + "<td>"
					+ ticket.getSubmittedDate() + "</td>" + "<td>"
					+ ticket.getStatus() + "</td>" + "<td>"
					+ ticket.getMangId().getuFName() + " "
					+ ticket.getMangId().getuLName() + "</td>" + "<td style='display:none;' id='remarks'>"
					+ ticket.getRemarks() + "</td>"+"<td style='display:none;' id='B/P_mng'>"
					+ statusChangerName+ "</td>"+
					"<td style='display:none;' id='radstatus'>"
					+ radStatus + "</td>"+
					"<td style='display:none;' id='esstatus'>"
					+ esStatus + "</td>"+
					"<td style='display:none;' id='crstatus'>"
					+crStatus+ "</td>"+"</tr>";
			statusChangerName = "";
		    radStatus = status;
			esStatus = status;
			crStatus = status;
		}
		sHtml += "</tbody></table>";
	}
		
	private Set<LoginSignup> getSubordinates(LoginSignup signUp) {
		return signUp.getSubordinates();
	}

	private Set<Ticket> getTickets(LoginSignup signUp) {
		return signUp.getTickets();
	}

	private void getHierarchy(LoginSignup in_SignUp, int userID) 
	{
		String status = "";
		//boolean haveSubOrd = false;
		if (userID != 0) {
			System.out.println("creating head"); 
			sHtml += "<div class='tree well' style='overflow: auto; height: 597px;'>"
					+ "<ul>";
		}

		Set<LoginSignup> subOrdinates = getSubordinates(in_SignUp);
//		if(subOrdinates.size() == 0 && !haveSubOrd)
//		{
//			System.err.println("subordinate size zero");
//			sHtml = "<div><p style='margin-left: 350px;margin-top: 200px;font-weight: bolder;'>Nothing to show right Now!</p></div>";
//		}
//		else
//		{
			if (subOrdinates.size() > 0) {
			
			//haveSubOrd = true;
			Iterator<LoginSignup> itr = subOrdinates.iterator();
			while (itr.hasNext()) {
				LoginSignup subOrd = (LoginSignup) itr.next();
				System.out.println(subOrd.getuFName());
				sHtml += "<li><span class='tree-toggle'";
				
				if(subOrd.getUserActiveOrNot() == 'N')
				{
					sHtml += " style = 'background-color : red'";
				}
				
				sHtml += ">" + subOrd.getuFName()
				+ " " + subOrd.getuLName() + "</span>";
				
				
				if (subOrd.getPositionId() == ILoginUser.DEVELOPER_ID) 
				{
					sHtml +="<span>D</span>";
					Set<Ticket> tickets = getTickets(subOrd);
					if (tickets.size() > 0) {
						sHtml +="<ul class='tree1'><li>"+ 
								"<div class='table-responsive'>"
								+ "<table class='table' style='margin-top: 20px;'>"
								+ "<thead>" + "<tr>" 
								+ "<th>#</th>"
								+ "<th>TICKET NO.</th>"
								+ "<th>DESCRIPTION</th>" 
								+ "<th>SUB DATE</th>"
								+ "<th>STATUS</th>" 
								+ "<th>SUB TO</th>" 
								+ "<th></th>"
								+ "</tr>" + "</thead>"
								+ "<tbody>";
					}
					else{
						sHtml +="</li>";
						
					}
					Iterator<Ticket> iterator = tickets.iterator();
					int i = 0;
					while (iterator.hasNext()) {
						System.out.println("loading ticket");
						i++;
						Ticket ticket = (Ticket) iterator.next();
						System.out.println(ticket.getStatus());
						sHtml += "<tr";
						if(ticket.getStatus() == IEditUserDetailsDao.TICKET_STATUS_BIASED)
						{
							status = "Precise";
							sHtml += " class='alert alert-danger'";
						}
						else if(ticket.getStatus() == IEditUserDetailsDao.TICKET_STATUS_PRECISE)
						{
							status = "Biased";
							sHtml += " class='alert alert-success'";
						}
						else if(ticket.getStatus() == IAddTicketImpl.TICKET_STATUS_UNCHECKED)
						{
							status = "Biased";
							sHtml += " class='alert alert-info'";
						}
						else if(ticket.getStatus() == IAddTicketDao.TICKET_UPDATED_STATUS)
						{
							status = "Biased";
							sHtml += " class='alert alert-warning'";
						}
						sHtml += ">" + "<td>" + i + "</td>" 
								+ "<td><a href='../jsp/userDetails.jsp?t_Num="+ticket.getTicketNum().trim()+"'>"+ ticket.getTicketNum() + "</a></td>" 
								+ "<td>"+ ticket.getDesciption() + "</td>" 
								+ "<td>"+ ticket.getSubmittedDate() + "</td>"
								+ "<td>"+ ticket.getStatus() + "</td>" 
						//		+ "<td><textarea rows='2' cols='10' name='remarks'></textarea></td>" 
								+ "<td>"+ ticket.getMangId().getuFName() + " "
								+ ticket.getMangId().getuLName() + "</td>"

								+ "<td><input type='button' class='btn btn-primary active' data-toggle='modal' " 
								+"data-target='#myModal' value='"+status+"'"
								+" onclick='remarksId(this)' id='"+ticket.getTicketNum()+"'></input></td>";
						if(ticket.getStatus() == IAddTicketImpl.TICKET_STATUS_UNCHECKED 
								|| ticket.getStatus() == IAddTicketDao.TICKET_UPDATED_STATUS)
						{
							status = "Precise";
							sHtml += "<td><input type='button' class='btn btn-primary active' data-toggle='modal' " 
									+"data-target='#myModal' value='"+status+"'"
									+" onclick='remarksId(this)' id='"+ticket.getTicketNum()+"'></input></td>";
						}else{
							sHtml += "<td></td>";
						}
								sHtml += "</tr>";
					}
					if (tickets.size() > 0)
					{
						System.out.println("closing table");
						sHtml += "</tbody></table></div></li></ul></li>";
					}
				}
				else 
				{
					sHtml += "<ul class='tree1'>";
					getHierarchy(subOrd, 0);
				}
				if(!itr.hasNext())
				{
					sHtml += "</ul>";
				}
				
			}//while end
			System.out.println("end while");
			
		} else {
			sHtml += "</ul></li>";
		}
	//}
}
	
	public Ticket getTicketDetailsForMngr(String tNum)
	{
		System.err.println(tNum);
		Ticket ticket = null;
		Misc misc = new Misc();
		Session session = null;
		String component = "getTicketDetailsForMngr";
		try
		{
			session = sessionFactory.openSession();
			int tId = misc.getTicketId(session, tNum);
			ticket = misc.getTicketObj(session, tId);
		}
		catch(Exception ex)
		{
			System.out.println("Exception while getting ticket detail for mngr: "+ex);
			Misc.writeToFile(TicketDetails.class, component, "", ex.toString());
		}
		finally
		{
			if(session != null)
				session.close();
		}
		return ticket;
	}
	public static void main(String[] args) {
		new TicketDetails().getTicketDetailsForMngr("5414");
	}
}