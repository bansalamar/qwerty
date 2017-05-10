package dao;

import interfaces.ICompRegDao;
import model.LoginSignup;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import service.Misc;
import util.HibernateUtils;

public class CompRegDao implements ICompRegDao{

	SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
	
	@Override
	public LoginSignup compReg(LoginSignup signup) throws Exception
	{
		String component = "compReg";
		if(sessionFactory == null)
		{
			Misc.writeToFile(CompRegDao.class, component,"TL OR PM Work","Session Factory Is NULL!");
			throw new Exception();
		}
		LoginSignup iSignup = new LoginSignup();
		Misc misc = new Misc();
		Session session = null;
		Transaction transaction = null;
		try
		{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			LoginSignup user = misc.getUserObj(session, signup.getuId());
			//user.setuPassword(misc.encodePassword(signup.getuPassword()));
			user.setuPassword(signup.getuPassword());
			user.setUserQuest(signup.getUserQuest());
			user.setUserQuestAns(signup.getUserQuestAns());
			transaction.commit();
			session.close();
			iSignup.setErrMsg("No Error");
		}
		catch(Exception ex)
		{
			System.out.println("Exception while complete registration of system user: "+ex);
			Misc.writeToFile(CompRegDao.class, component, "TL OR PM Work",ex.toString());
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
		return iSignup;
	}
}
