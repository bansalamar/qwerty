package dao;

import interfaces.IAddTicketDao;
import interfaces.IEditUserDetailsDao;

import java.util.Map;

import model.LoginSignup;
import model.Ticket;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.google.api.services.drive.Drive;

import service.GoogleDriveImpl;
import service.Misc;
import util.HibernateUtils;

public class AddTicketDao implements IAddTicketDao 
{
	SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
	
	@Override
	public Ticket saveTicket(String ip,Map<Integer, String> map,Ticket ticket, int uId) throws Exception 
	{
		String component = "saveTicket";
		if (sessionFactory == null) {
			System.out.println("return error");
			Misc.writeToFile(AddTicketDao.class, component,ip, "Session Factory Is NULL!");
			throw new Exception();
		}
	
		Session session = null;
		Transaction transaction = null;
		Ticket ticket1 = new Ticket(); 
		try
		{
			session = sessionFactory.openSession();
			LoginSignup user = new Misc().getUserObj(session, uId);
			LoginSignup mng = user.getManager();
			int pID = user.getPositionId();
			int proID =  user.getProjects().getProject_id();
			int subProID = 0;
			if(user.getSubProject() != null)
			{
				subProID = user.getSubProject().getSubPro_id();
			}
			doSaveUserDetails(session,user,map);
			IEditUserDetailsDao dao = new EditUserDetailsDao();
			int rpMngrID = dao.findReportingManager(session, proID, subProID, pID);
			if(rpMngrID != user.getManager().getuId())
			{
				System.err.println("reporting mng changed ...............reselect project and subproject");
				Misc.writeToFile(AddTicketDao.class, component,ip,"Reporting manager Changed!");
				ticket1.setErrMsg("Your manager has been changed. Please reselect your project from Edit " +
						"Details and Try Again!");
				if(map.get(TICKET_FOLDER_ID) != null || !map.get(TICKET_FOLDER_ID).equals(""))
				{
					Drive service = GoogleDriveImpl.getDriveService();
					GoogleDriveImpl.deleteFileFolder(service, map.get(TICKET_FOLDER_ID));
					System.err.println("Ticket Folder Deleted!");
				}
				
			}	
			else
			{
				transaction = session.beginTransaction();
				ticket.setProId(user.getProjects());
				ticket.setSubProId(user.getSubProject());
				ticket.setUserId(user);
				ticket.setMangId(mng);
				ticket.setTicketFID(map.get(TICKET_FOLDER_ID));
				ticket.setRadId(map.get(RAD_ID));
				ticket.setEsDocId(map.get(ES_DOC_ID));
				ticket.setCrDocId(map.get(CR_DOC_ID));
				session.save(ticket);
				transaction.commit();
				ticket1.setErrMsg("No Error");
			}
			session.close();
		}
		catch(Exception ex)
		{
			System.out.println("exception during saving the ticket: "+ex);
			if(transaction != null)
				transaction.rollback();
			Misc.writeToFile(AddTicketDao.class, component,ip, ex.toString());
			throw ex;
		}
		finally
		{
			if(session != null)
			{
				if(session.isOpen())
					session.close();
			}
		}
		return ticket1;
	}

	private void doSaveUserDetails(Session session, LoginSignup user,
			Map<Integer, String> map)
	{
		Transaction transaction = session.beginTransaction();
		
		if(map.get(USER_FOLDER_ID) != null)
			user.setUserFolderId(map.get(USER_FOLDER_ID));
		
		if(map.get(ISO_FOLDER_ID) != null)
			user.setIsoFolderId(map.get(ISO_FOLDER_ID));
		
		if(map.get(PERMISSION_ID) != null)
			user.setPermissionId(map.get(PERMISSION_ID));
		
		transaction.commit();
	}

	@Override
	public Ticket checkTicketNum(String ip,String tNum) throws Exception
	{
		String component = "checkTicketNum";
		if (sessionFactory == null)
		{
			Misc.writeToFile(AddTicketDao.class, component,ip, "Session Factory Is NULL!");
			throw new Exception();
		}
		
		Ticket ticket = null;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Ticket.class);
			criteria.add(Restrictions.like("ticketNum", tNum));
			ticket = (Ticket)criteria.uniqueResult();
			session.close();
		}
		catch(Exception ex)
		{
			System.out.println("Exception while checking ticket number during add ticket");
			Misc.writeToFile(AddTicketDao.class, component,ip, ex.toString());
			throw ex;
		}
		finally
		{
			if(session != null)
			{
				if(session.isOpen())
					session.close();
			}
		}
		return ticket;
	}

	@Override
	public String getDocId(String ip,String doctype, String tno) throws Exception 
	{	
		String component = "getDocId";
		if (sessionFactory == null)
		{
			Misc.writeToFile(AddTicketDao.class, component,ip, "Session Factory Is NULL!");
			throw new Exception();
		}
	
		Misc misc = new Misc();
		Ticket ticket = null;
		Session session = null;
		String fileIdToGet = "";
		try
		{
			session = sessionFactory.openSession();
			int tId = misc.getTicketId(session, tno);
			ticket = misc.getTicketObj(session, tId);
		
			if(doctype.equals("radbtn"))
			{	
				fileIdToGet = ticket.getRadId();
			}
			else if(doctype.equals("crbtn"))
			{
				fileIdToGet = ticket.getCrDocId();
			}
			else if(doctype.equals("esbtn"))
			{
				fileIdToGet = ticket.getEsDocId();
			}
			session.close();
		}
		catch(Exception ex)
		{
			System.out.println("Exception while fetching docid");
			Misc.writeToFile(AddTicketDao.class, component,ip, ex.toString());
			throw ex;
		}
		finally
		{
			if(session != null)
			{
				if(session.isOpen())
					session.close();
			}
		}
		return fileIdToGet;
	}


	@Override
	public Ticket updateTicketDoc(String ip,String doctype, String tno, String newFileId)
			throws Exception 
	{
		String component = "updateTicketDoc";
		if (sessionFactory == null)
		{
			Misc.writeToFile(AddTicketDao.class, component,ip, "Session Factory Is NULL!");
			throw new Exception();
		}
		
		Misc misc = new Misc();
		Ticket ticket = null;
		Session session = null;
		Transaction tx = null;
		try
		{
			session = sessionFactory.openSession();
			int tId = misc.getTicketId(session, tno);
			ticket = misc.getTicketObj(session, tId);
			tx = session.beginTransaction();
					
			if(ticket.getStatus() == IEditUserDetailsDao.TICKET_STATUS_BIASED)
			{
				ticket.setStatus(IAddTicketDao.TICKET_UPDATED_STATUS);
			}
			if(doctype.equals("radbtn"))
			{
				ticket.setRadId(newFileId);
			}
			else if(doctype.equals("crbtn"))
			{
				ticket.setCrDocId(newFileId);
			}
			else if(doctype.equals("esbtn"))
			{
				ticket.setEsDocId(newFileId);
			}	
			tx.commit();
			session.close();
			ticket.setErrMsg("No Error");
		}
		catch(Exception ex)
		{
			System.out.println("Exception while updating ticket document");
			if(tx != null)
				tx.rollback();
			
			Misc.writeToFile(AddTicketDao.class,component,ip, ex.toString());
			throw ex;
		}
		finally
		{
			if(session != null)
			{
				if(session.isOpen())
					session.close();
			}
		}
		return ticket;
	}

	@Override
	public String getTicketFolderID(String ip,String tno) throws Exception 
	{
		String component = "getTicketFolderID"; 
		if (sessionFactory == null)
		{
			Misc.writeToFile(AddTicketDao.class, component,ip, "Session Factory Is NULL!");
			throw new Exception();
		}
		
		Misc misc = new Misc();
		Ticket ticket = null;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			int tId = misc.getTicketId(session, tno);
			ticket = misc.getTicketObj(session, tId);
			session.close();
		}
		catch(Exception ex)
		{
			System.out.println("Exception while fetching ticket folder id");
			Misc.writeToFile(AddTicketDao.class, component,ip, ex.toString());
			throw ex;
		}
		finally
		{
			if(session != null)
			{
				if(session.isOpen())
					session.close();
			}
		}
		return ticket.getTicketFID();
	}
}
