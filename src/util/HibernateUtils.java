package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtils {
	private HibernateUtils(){}
	
	private static SessionFactory sessionFactory = buildSessionFactory(); 
	
	@SuppressWarnings("deprecation")
	private static  SessionFactory buildSessionFactory() {
		System.out.println("con");
		try{
			return new Configuration().configure("configuration/hibernate.cfg.xml").buildSessionFactory();
		}
		catch(Exception ex){
			return null;
		}
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
