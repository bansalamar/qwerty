package dao;

import java.util.List;

import model.LoginSignup;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import service.Misc;
import util.HibernateUtils;

import interfaces.IForgotPasswordDao;

public class ForgotPasswordDao implements IForgotPasswordDao {
	
	SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
	
	@Override
	@SuppressWarnings("unchecked")
	public String retrievePass(String ip,String emailId, String ans) throws Exception
	{
		String pass = "";
		String component = "retrievePass";
		if(sessionFactory == null)
		{
			Misc.writeToFile(ForgotPasswordDao.class, component, ip, "Session Factory is NULL!");
			throw new Exception();
		}
		Session session = null;
		try
		{
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(LoginSignup.class);
			criteria.add(Restrictions.like("emailID", emailId));
			criteria.add(Restrictions.like("userQuestAns", ans));
			criteria.setProjection(Projections.property("uPassword"));
			List<Object> list = criteria.list();
			if(list.size() == 1)
			{
				pass = (String) list.get(0);
			}
			session.close();
		}
		catch(Exception ex)
		{
			Misc.writeToFile(ForgotPasswordDao.class, component, ip, ex.toString());
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
		
		return pass;
	}
	
	@Override
	public String getQuest(String emailId) throws Exception
	{
		String Quest = "";
		String component = "getQuest";
		if(sessionFactory == null)
		{
			Misc.writeToFile(ForgotPasswordDao.class, component, "", "Session Factory is NULL!");
			throw new Exception();
		}
		
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(LoginSignup.class);
		criteria.add(Restrictions.like("emailID", emailId));
		criteria.setProjection(Projections.property("userQuest"));
		@SuppressWarnings("unchecked")
		List<Object> list = criteria.list();
		if(list.size() == 1)
		{
			Quest = (String) list.get(0);
		}
		session.close();
		return Quest;
	}
}
