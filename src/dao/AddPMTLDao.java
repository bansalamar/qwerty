package dao;

import interfaces.IAddPMTLDao;
import interfaces.IEditUserDetailsDao;
import model.LoginSignup;
import model.Projects;
import model.SubProjects;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import service.GoogleDriveImpl;
import service.Misc;
import util.HibernateUtils;

import com.google.api.services.drive.Drive;

public class AddPMTLDao implements IAddPMTLDao {
	
	IEditUserDetailsDao dao = new EditUserDetailsDao();
	Misc misc = new Misc();
	SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
	
	@Override
	public LoginSignup addTL(LoginSignup tl,
			String projectName, String subProjectName) throws Exception {
		
		Session session = null;
		Transaction transaction = null;
		String component  = "addTL";
		if(sessionFactory == null)
		{
			Misc.writeToFile(AddPMTLDao.class, component, "MANAGER WORK","Session Factory is NULL");
			throw new Exception();
		}
		
		try 
		{
			session = sessionFactory.openSession();
			int proID = misc.getProId(session, projectName);
			int subProId = misc.getSubProId(session, subProjectName);
			SubProjects subProject = misc.getSubProObj(session, subProId);
			tl.setProjects(misc.getProObj(session, proID));

			tl.setSubProject(subProject);
			tl.setPositionId(position_ID_TL);
			tl.setUserActiveOrNot(activeFlag);
			int mngId = dao.findReportingManager(session, proID, subProId, 3);
			System.out.println("manager id:  " + mngId);
			tl.setManager(misc.getUserObj(session, mngId));

			LoginSignup activeTl = getActiveTL(session, proID, subProId);
			transaction = session.beginTransaction();
			Drive service = GoogleDriveImpl.getDriveService();
			if(service == null)
			{
				tl.setErrMsg("Some Error Occurs!");
				return tl;
			}
			if (activeTl != null) {
				String result = GoogleDriveImpl.removePermission(service,subProject.getSubProFolderId(), activeTl.getPermissionId());
				if(result.equals("OK"))
				{
					String permissionID = GoogleDriveImpl.doSharing(service,subProject.getSubProFolderId(), tl.getEmailID());
					
					tl.setPermissionId(permissionID);
					System.out.println("permission id on saving new tl if other present    "+permissionID);
				}
				Integer id = (Integer) session.save(tl);
				transaction.commit();
				if (id > 0) {
					System.out.println("going to change status of old after saving new tl");
					if (changeTLActiveFlag(session, activeTl)) {
						tl.setErrMsg("NO ERROR");
						tl.setuId(id);
					}
				}
			} else {
				String permissionID = GoogleDriveImpl.doSharing(service,subProject.getSubProFolderId(), tl.getEmailID());
				tl.setPermissionId(permissionID);
				System.out.println("permission id on saving new tl     "+permissionID);
				System.out.println("going to save new tl");
				Integer id = (Integer) session.save(tl);
				transaction.commit();
				if (id > 0) {
					tl.setErrMsg("NO ERROR");
					tl.setuId(id);
				}
			}
			session.close();
		}
		catch(Exception ex)
		{
			if(transaction != null)
				transaction.rollback();
			Misc.writeToFile(AddPMTLDao.class, component, "MANAGER WORK", ex.toString());
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
		return tl;
	}
	
	@Override
	public LoginSignup addPM(LoginSignup pm,String projectName) throws Exception {
		
		Session session = null;
		Transaction transaction = null;
		String component  = "addPM";
		
		if(sessionFactory == null)
		{
			Misc.writeToFile(AddPMTLDao.class, component, "MANAGER WORK","Session Factory is NULL");
			throw new Exception();
		}
		
		try
		{
			session = sessionFactory.openSession();
			int proID = misc.getProId(session, projectName);
			Projects project = misc.getProObj(session, proID);
			pm.setProjects(project);
			
			pm.setPositionId(position_ID_PM);
			pm.setUserActiveOrNot(activeFlag);
			int mngId = dao.findReportingManager(session, proID, 0, 2);
			pm.setManager(misc.getUserObj(session, mngId));
			LoginSignup activePm = getActivePM(session, proID);
			transaction = session.beginTransaction();
			Drive service = GoogleDriveImpl.getDriveService();
			if(service == null)
			{
				pm.setErrMsg("Some Error Occurs!");
				return pm;
			}
			if (activePm != null) 
			{
				String result = GoogleDriveImpl.removePermission(service,project.getProFolderId(), activePm.getPermissionId());
				if(result.equals("OK"))
				{
					String permissionID = GoogleDriveImpl.doSharing(service,project.getProFolderId(), pm.getEmailID());
					pm.setPermissionId(permissionID);
					System.out.println("permission id on saving new pm if other present     "+permissionID);
				}
				Integer id = (Integer)session.save(pm);
				transaction.commit();
				if (id > 0) {
					System.out.println("going to change status of old after saving new pm");
					if(changePMActiveFlag(session, activePm)){
						pm.setErrMsg("NO ERROR");
						pm.setuId(id);
					}
				}
			} 
			else
			{
				String permissionID = GoogleDriveImpl.doSharing(service,project.getProFolderId(), pm.getEmailID());
				pm.setPermissionId(permissionID);
				System.out.println("permission id on saving new pm     "+permissionID);
				System.out.println("going to save new pm");
				Integer id = (Integer)session.save(pm);
				transaction.commit();
				if (id > 0) 
				{
					pm.setErrMsg("NO ERROR");
					pm.setuId(id);
				}
			}
			session.close();
		}
		catch(Exception ex)
		{
			if(transaction != null)
				transaction.rollback();
			Misc.writeToFile(AddPMTLDao.class, component, "MANAGER WORK", ex.toString());
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
		return pm;
	}
	
	public boolean changePMActiveFlag(Session session, LoginSignup activeMng) {
		Transaction transaction = session.beginTransaction();
		try {
				activeMng.setUserActiveOrNot(deactiveFlag);
				activeMng.setPermissionId(null);
				transaction.commit();
		} catch (Exception e) {
			System.out.println("Exception while changing pm flag: "+e);
			if(transaction != null)
				transaction.rollback();
			return false;
		}
		return true;
	}
	private boolean changeTLActiveFlag(Session session, LoginSignup activeTl) {
		Transaction transaction = session.beginTransaction();
		try {
			activeTl.setUserActiveOrNot(deactiveFlag);
			activeTl.setPermissionId(null);
			transaction.commit();
		} catch (Exception e) {
			System.out.println("Exception while changing tl flag: "+e);
			if(transaction != null)
				transaction.rollback();
			return false;
		}
		return true;
	}
	
	private LoginSignup getActiveTL(Session session, int proID, int subProId) {
		Criteria criteria = session.createCriteria(LoginSignup.class);
		criteria.add(Restrictions.like("projects",
				misc.getProObj(session, proID)));
		criteria.add(Restrictions.like("subProject",
				misc.getSubProObj(session, subProId)));
		criteria.add(Restrictions.like("positionId", position_ID_TL));
		criteria.add(Restrictions.eq("userActiveOrNot", 'Y'));
		LoginSignup signup = (LoginSignup) criteria.uniqueResult();
		return signup;
	}

	public LoginSignup getActivePM(Session session, int proID) {
		Criteria criteria = session.createCriteria(LoginSignup.class);
		criteria.add(Restrictions.like("projects",
				misc.getProObj(session, proID)));
		criteria.add(Restrictions.like("positionId", position_ID_PM));
		criteria.add(Restrictions.eq("userActiveOrNot", 'Y'));
		LoginSignup signup = (LoginSignup) criteria.uniqueResult();
		return signup;
		
	}
}
