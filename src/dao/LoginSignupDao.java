package dao;

import interfaces.IEditUserDetailsDao;
import interfaces.ILoginDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.LoginSignup;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import service.Misc;
import util.HibernateUtils;

public class LoginSignupDao implements ILoginDao {
	
	SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
	
	public List<Object> getUserObj(String ip,LoginSignup loginSignup) throws Exception
	{
		List<Object> details = new ArrayList<>();
		String component = "getUserObj";
		if(sessionFactory == null)
		{
			Misc.writeToFile(LoginSignupDao.class,component,ip,"Session Factory in NULL.");
			throw new Exception();
		}
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(LoginSignup.class);
			criteria.add(Restrictions.like("uId",loginSignup.getuId()));
			criteria.add(Restrictions.like("userActiveOrNot",'Y'));
			details.add(0,"OK");
			details.add(1,criteria.uniqueResult());
			session.close();
		}
		catch(Exception ex)
		{
			System.out.println("Exception while login: "+ex);
			Misc.writeToFile(LoginSignupDao.class,component,ip,
					"[Login-Id: "+loginSignup.getuId()+"] -: "+ex.toString());
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
		return details;
	}
	
	public int saveNewUser(String ip,LoginSignup loginSignup) throws Exception
	{
		String component = "saveNewUser";
		if(sessionFactory == null)
		{
			Misc.writeToFile(LoginSignupDao.class,component,ip,"Session Factory in NULL.");
			throw new Exception();
		}
		
		Serializable ser =  null;
		int id = -1;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try 
		{
			ser = session.save(loginSignup);
			if (ser != null) 
				id = (Integer) ser;

			transaction.commit();
			session.close();
		} 
		catch (Exception e) 
		{
			System.out.println(e);
			System.out.println("Rolling back the transaction");
			if(transaction != null)
				transaction.rollback();
			Misc.writeToFile(LoginSignupDao.class,component,ip,
					"["+loginSignup.getuFName()+" "+loginSignup.getuLName()+"] -: "+e.toString());
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
		return id;
	}

	@Override
	public boolean emailAvailability(String ip,String emailID) throws Exception
	{
		String component = "emailAvailability";
		if(sessionFactory == null)
		{
			Misc.writeToFile(LoginSignupDao.class,component,ip,"Session Factory in NULL.");
			throw new Exception();
		}
		boolean available = false;
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(LoginSignup.class);
			criteria.add(Restrictions.like("emailID", emailID));
		//	criteria.add(Restrictions.eq("userActiveOrNot", 'Y'));
			if(criteria.list().size() > 0)
				available =  true;
			session.close();
		}
		catch(Exception ex)
		{
			Misc.writeToFile(LoginSignupDao.class,component,ip,ex.toString());
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
		return available;
	}

	@Override
	public void setActiveReportingMngr(String ip,LoginSignup signIn)
	{
		String component = "setActiveReportingMngr";
		if(sessionFactory == null)
		{
			Misc.writeToFile(LoginSignupDao.class,component,ip,"Session Factory in NULL.");
		}
		
		Session session = null;
		Transaction transaction = null;
		try
		{
			Misc misc = new Misc();
			session = sessionFactory.openSession();
			IEditUserDetailsDao dao = new EditUserDetailsDao();
			int pID = signIn.getPositionId();
			int proID =  signIn.getProjects().getProject_id();
			int subProID = 0;
			if(signIn.getSubProject() != null)
			{
				subProID = signIn.getSubProject().getSubPro_id();
			}
			
			int rpMngrID = dao.findReportingManager(session, proID, subProID, pID);
			System.err.println("id's are:   "+rpMngrID+" "+signIn.getManager().getuId());
			if(rpMngrID != signIn.getManager().getuId())
			{
				transaction = session.beginTransaction();
				LoginSignup rpMngr = misc.getUserObj(session, rpMngrID);
				LoginSignup userObj = misc.getUserObj(session, signIn.getuId());
				userObj.setManager(rpMngr);
				transaction.commit();
				session.close();
				signIn.setManager(rpMngr);
				Misc.writeToFile(LoginSignupDao.class,component,ip,
						"[Login-Id: "+signIn.getuId()+"] -: Reporting Manager Changed!");
				System.err.println("reporting manager set");
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex);
			if(transaction != null)
				transaction.rollback();
			Misc.writeToFile(LoginSignupDao.class,component,ip,
					"[Login-Id: "+signIn.getuId()+"] -: "+ex.toString());
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
}
