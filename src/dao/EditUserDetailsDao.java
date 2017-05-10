package dao;

import interfaces.IEditUserDetailsDao;
import interfaces.ILoginUser;

import java.util.ArrayList;
import java.util.List;

import model.LoginSignup;
import model.Projects;
import model.SubProjects;
import model.Ticket;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import service.GoogleDriveImpl;
import service.Misc;
import util.HibernateUtils;

import com.google.api.services.drive.Drive;

public class EditUserDetailsDao implements IEditUserDetailsDao {

	SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

	@Override
	public String persistUserDetails(String ip,int userID, String proName,String subProName) throws Exception 
	{
		String component = "persistUserDetails";
		if(sessionFactory == null)
		{
			System.out.println("session factory is null");
			Misc.writeToFile(EditUserDetailsDao.class,component, ip,"Session Factory Is NULL!");
			throw new Exception();
		}
		Session session = null;
		Transaction transaction = null;
		Misc misc = new Misc();

		try 
		{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			int proID = misc.getProId(session, proName);
			int subProID = misc.getSubProId(session, subProName);

			Projects projects = misc.getProObj(session, proID);
			SubProjects subProjects = misc.getSubProObj(session, subProID);
			LoginSignup logIn_SignUp = misc.getUserObj(session, userID);
			
			//for revoking permissions in case of change project
			String newPermissionId = "";
			if(logIn_SignUp.getPermissionId() != null)
			{
				//if(logIn_SignUp.getProjects().getProject_id() != proID){}
				String folderID = "";
				String permID = logIn_SignUp.getPermissionId();
				System.out.println(logIn_SignUp.getPositionId());
				
				int positionID = logIn_SignUp.getPositionId();
				Drive service = GoogleDriveImpl.getDriveService();
				if(positionID ==  ILoginUser.PROJECT_MANAGER_ID)
				{
					if(misc.isProHaveMngr(session, proID, subProID, logIn_SignUp.getPositionId()))
					{
						return "mngrExist";
					}
					System.out.println("sdfljhd: "+logIn_SignUp.getProjects().getProFolderId());
					folderID = logIn_SignUp.getProjects().getProFolderId();
					System.out.println(folderID);
					String res = GoogleDriveImpl.removePermission(service,folderID, permID);
					System.out.println(res+":                d;skj;sdlsd");
					if(res.equalsIgnoreCase("OK"))
					{
						newPermissionId = GoogleDriveImpl.doSharing(service,projects.getProFolderId(), logIn_SignUp.getEmailID());
					}
					logIn_SignUp.setPermissionId(newPermissionId);
				}
				else if(positionID ==  ILoginUser.TEAM_LEAD_ID)
				{
					if(misc.isProHaveMngr(session, proID, subProID, logIn_SignUp.getPositionId()))
					{
						return "mngrExist";
					}
					
					if(logIn_SignUp.getSubProject().getSubProFolderId() != null)
					{
						folderID = logIn_SignUp.getSubProject().getSubProFolderId();
					}
//					else
//					{
//						folderID = logIn_SignUp.getProjects().getProFolderId();
//					}
					String res = GoogleDriveImpl.removePermission(service,folderID, permID);
					if(res.equalsIgnoreCase("OK"))
					{
						newPermissionId = GoogleDriveImpl.doSharing(service,subProjects.getSubProFolderId(), logIn_SignUp.getEmailID());
					}
					logIn_SignUp.setPermissionId(newPermissionId);
				}
				else if(positionID == ILoginUser.DEVELOPER_ID)
				{
					folderID = logIn_SignUp.getUserFolderId();
					String toFolder = "";
					if(subProjects.getSubProFolderId() !=null )
					{
						toFolder = subProjects.getSubProFolderId();
					}
					else
					{
						toFolder = projects.getProFolderId();
					}
					GoogleDriveImpl.moveFolder(service,folderID,toFolder);
				}
			}
			
			/////////////////////////////////////////////////////

			int rmId = findReportingManager(session, proID, subProID,logIn_SignUp.getPositionId());

			System.out.println("rmId: " + rmId);

			logIn_SignUp.setProjects(projects);
			logIn_SignUp.setSubProject(subProjects);
			System.out.println("Old perm id:  "+logIn_SignUp.getPermissionId());
			System.out.println("new perm id:  "+newPermissionId);
			

			if (rmId != 0) 
			{
				logIn_SignUp.setManager((LoginSignup) session.get(LoginSignup.class, rmId));
			}

			logIn_SignUp.setProjects(projects);
			logIn_SignUp.setSubProject(subProjects);

			transaction.commit();
			session.close();

			return "success@"+logIn_SignUp.getPositionId();

		} 
		catch (Exception e) 
		{
			Misc.writeToFile(EditUserDetailsDao.class, component, ip, e.toString());
			if(transaction != null)
				transaction.rollback();
			System.out.println("edit details:" + e);
			throw e;
		}
		finally
		{
			if(session != null)
			{
				if(session.isOpen())
					session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int findReportingManager(Session session, int projectId,int subProjectId, int positionId) 
	{
		System.out.println(projectId + " " + subProjectId);
		int rm_id = 0;
		Criteria criteria = session.createCriteria(LoginSignup.class);
		if(positionId == 4)
		{
			//For Developers
			criteria.add(Restrictions.like("projects",(Projects) session.get(Projects.class, projectId)));
			criteria.add(Restrictions.ne("positionId", positionId));
			criteria.add(Restrictions.eq("userActiveOrNot", 'Y'));

			if (subProjectId != 0) 
			{
				criteria.add(Restrictions.like("subProject",(SubProjects) session.get(SubProjects.class, subProjectId)));
			}
			criteria.setProjection(Projections.property("uId"));
			List<Integer> row = criteria.list();
			if (row.size() == 0) 
			{
				positionId--; // for higher in hierarchy
				rm_id = findHigherReportingMng(session, positionId, projectId);
			} 
			else 
			{
				rm_id = row.get(0);
			}
			
		}
		else
		{
			//For TL,PM
			rm_id = findHigherReportingMng(session,positionId,projectId);
		}
		return rm_id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int findHigherReportingMng(Session session,int positionId,int projectId) 
	{
		System.out.println("in highermethod");
		int rmId = 0;
		while (positionId > 0) 
		{
			System.out.println("inloop");
			positionId--;
			System.out.println("position id:" + positionId);
			Criteria criteria = session.createCriteria(LoginSignup.class);
			if (positionId == ILoginUser.PROJECT_MANAGER_ID) 
			{
				criteria.add(Restrictions.like("projects",(Projects) session.get(Projects.class, projectId)));
			}
			criteria.add(Restrictions.eq("positionId", positionId));
			criteria.add(Restrictions.eq("userActiveOrNot", 'Y'));
			criteria.setProjection(Projections.property("uId"));
			List<Integer> row = criteria.list();
			if (row.size() != 0) 
			{
				rmId = row.get(0);
				break;
			}
		}
		System.out.println("mng Id: "+rmId);
		return rmId;
	}
	
	@Override
	public LoginSignup editUserDetailsPersist(String ip,LoginSignup in_SignUp, String newPassword,boolean emailExist) throws Exception 
	{
		String component = "editUserDetailsPersist";
		if(sessionFactory == null)
		{
			System.out.println("session factory is null");
			Misc.writeToFile(EditUserDetailsDao.class, component, ip, "Session Factory Is NULL!");
			throw new Exception();
		}
		Session session = null;
		Transaction transaction = null;
		try
		{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			LoginSignup signUpDB = new Misc().getUserObj(session, in_SignUp.getuId());
			if(!signUpDB.getEmailID().equalsIgnoreCase(in_SignUp.getEmailID()))
			{
				if(emailExist)
				{
					in_SignUp.setErrMsg("EmailId Already Exists!");
					return in_SignUp;
				}
				
				boolean emailRes = new Misc().validateEmail(in_SignUp.getEmailID());
				if (!emailRes) 
				{
					in_SignUp.setErrMsg("Enter Valid EmailId!");
					return in_SignUp;
				}
				Drive service = GoogleDriveImpl.getDriveService();
				if(signUpDB.getPermissionId() != null)
				{
					String folderID = "";
					String newPermissionId = "";
					String permID = signUpDB.getPermissionId();
					int positionID = signUpDB.getPositionId();
					if(positionID ==  ILoginUser.PROJECT_MANAGER_ID)
					{
						folderID = signUpDB.getProjects().getProFolderId();
						String res = GoogleDriveImpl.removePermission(service,folderID, permID);
						if(res.equalsIgnoreCase("OK"))
						{
							newPermissionId = GoogleDriveImpl.doSharing(service,signUpDB.getProjects().getProFolderId(), in_SignUp.getEmailID());
						}
						signUpDB.setPermissionId(newPermissionId);
					}
					else if(positionID ==  ILoginUser.TEAM_LEAD_ID)
					{
						if(signUpDB.getSubProject().getSubProFolderId() != null)
						{
							folderID = signUpDB.getSubProject().getSubProFolderId();
						}
						String res = GoogleDriveImpl.removePermission(service,folderID, permID);
						if(res.equalsIgnoreCase("OK"))
						{
							newPermissionId = GoogleDriveImpl.doSharing(service,signUpDB.getSubProject().getSubProFolderId(), in_SignUp.getEmailID());
						}
						signUpDB.setPermissionId(newPermissionId);
					}
					else if(positionID == ILoginUser.DEVELOPER_ID)
					{
						folderID = signUpDB.getUserFolderId();
						String res = GoogleDriveImpl.removePermission(service,folderID, permID);
						if(res.equalsIgnoreCase("OK"))
						{
							newPermissionId = GoogleDriveImpl.doSharing(service,signUpDB.getUserFolderId(), in_SignUp.getEmailID());
						}
						signUpDB.setPermissionId(newPermissionId);
					}
				}
				else if(signUpDB.getPermissionId() == null && signUpDB.getPositionId() == ILoginUser.CHAIRMAN_ID)
				{
						List<String> list = getProFolderIds(session,ip);
						if(list != null && list.size() > 0)
						{
							for(int i=0;i<list.size();i++)
							{
								String folderId = list.get(i);
								GoogleDriveImpl.doSharing(service,folderId, in_SignUp.getEmailID());
							}
						}
				}
			}
			if(in_SignUp.getuPassword() != "")
			{
				//if(in_SignUp.getuPassword().equals(Misc.decodePassword(signUpDB.getuPassword())))
				if(in_SignUp.getuPassword().equals(signUpDB.getuPassword()))
				{
					if(newPassword == null || newPassword.equalsIgnoreCase("null") || newPassword.trim().length() == 0)
					{
						in_SignUp.setErrMsg("Enter Valid Password!");
						return in_SignUp;
					}
					//signUpDB.setuPassword(new Misc().encodePassword(newPassword));
					signUpDB.setuPassword(newPassword);
				}
				else
				{
					System.out.println("Passwords not matched");
					in_SignUp.setErrMsg("Incorrect Password!");
					return in_SignUp;
				}
			}
			signUpDB.setuFName(in_SignUp.getuFName());
			signUpDB.setEmailID(in_SignUp.getEmailID());
			signUpDB.setuLName(in_SignUp.getuLName());
			transaction.commit();
			session.close();
			in_SignUp.setErrMsg("No error");
			return in_SignUp;
		}
		catch(Exception ex)
		{
			System.out.println("Exception while edit userdetails       :"+ex);
			Misc.writeToFile(EditUserDetailsDao.class, component, ip, ex.toString());
			if(transaction != null)
				transaction.rollback();
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
	}

	@Override
	public Ticket updateTicketDetails(String tNo,String remarks,String btnValue,int statusChanger)throws Exception
	{
		Ticket ticket1 = new Ticket();
		String component = "updateTicketDetails";
		if(sessionFactory == null)
		{
			System.out.println("session factory is null");
			Misc.writeToFile(EditUserDetailsDao.class, component,"MANAGER WORK","Session Factory Is NULL!");
			throw new Exception();
		}
		int ticketStatus = TICKET_STATUS_BIASED;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			int tId = new Misc().getTicketId(session, tNo);
			if (btnValue.equalsIgnoreCase("Precise"))
				ticketStatus = TICKET_STATUS_PRECISE;
			if (tId > 0) {
				Ticket ticket = new Misc().getTicketObj(session, tId);
				ticket.setStatus(ticketStatus);
				ticket.setRemarks(remarks);
				ticket.setStatusChanger(new Misc().getUserObj(session,
						statusChanger));
				transaction.commit();
				session.close();
				ticket1.setErrMsg("No Error");
			}
		}
		catch(Exception ex)
		{
			System.out.println("exception while update ticket status: "+ex);
			Misc.writeToFile(EditUserDetailsDao.class, component, "MANAGER WORK", ex.toString());
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

	@Override
	public boolean saveImagePath(int uId,String ip,String path) throws Exception 
	{
		String component = "saveImagePath";
		Session session = null;
		Transaction transaction = null;
		boolean saved = false; 
		if(sessionFactory == null)
		{
			System.out.println("Session factory null!");
			Misc.writeToFile(EditUserDetailsDao.class, component, ip, "Session Factory Is NULL!");
			throw new Exception();
		}
		try
		{
			Misc misc = new Misc();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			LoginSignup user = misc.getUserObj(session, uId);
			user.setUserPicPath(path);
			transaction.commit();
			session.close();
			saved = true;
		}
		catch(Exception ex)
		{
			System.out.println("exception while saving image to db"+ex);
			if(transaction != null)
				transaction.rollback();
			Misc.writeToFile(EditUserDetailsDao.class, component, ip, ex.toString());
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
		return saved;
	}
	
	@SuppressWarnings("unchecked")
	List<String> getProFolderIds(Session session,String ip)
	{
		List<String> list = new ArrayList<>();
		Criteria criteria = session.createCriteria(Projects.class);
		criteria.setProjection(Projections.property("proFolderId"));
		list = criteria.list();
		if(list.size() <= 0)
		{
			list = null;
		}
		return list;
	}
}