package dao;

import interfaces.IAddProjectDao;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.LoginSignup;
import model.Projects;
import model.SubProjects;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import service.GoogleDriveImpl;
import service.Misc;
import util.HibernateUtils;

import com.google.api.services.drive.Drive;

public class AddProjectDao implements IAddProjectDao {
	
	SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

	@Override
	public Set<String> checkSubProExists(Session session, Set<String> list,
			int proId, Set<String> duplicateSubPros) {

		Set<String> list1 = new HashSet<String>();
		if (proId == 0) 
		{
			return list;
		}
		Iterator<String> it = list.iterator();
		while (it.hasNext()) 
		{
			String subPro = it.next();
			Query query = session
					.createQuery("from SubProjects where subPro_name= :subPro and pro_id= :proId");
			query.setParameter("subPro", subPro);
			query.setParameter("proId", proId);
			int size = query.list().size();
			if (size == 0) 
			{
				list1.add(subPro);
			} else 
			{
				duplicateSubPros.add(subPro);
			}
		}
		return list1;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Projects checkProExists(String proName) 
	{

		Projects project = null;
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Projects where project_name= :proName");
		query.setParameter("proName", proName);
		List<Projects> pro = query.list();
		if (pro.size() == 1) 
		{
			project = pro.get(0);
		}
		session.close();
		return project;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getProList() 
	{

		List<String> proList = new ArrayList<>();
		if (sessionFactory == null) 
		{
			proList.add("Some Exception Occurs");
			return proList;
		}
		try 
		{
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Projects.class);
			criteria.setProjection(Projections.property("project_name"));
			List<String> row = criteria.list();
			session.close();
			return row;
		} 
		catch (Exception ex) 
		{
			System.out.println("Exception While getting proList:" + ex);
			proList.add("Some Exception Occurs");
			return proList;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubProjects> getSubProList(String pro) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("select subProjects from Projects where pro_name= :proName");
		query.setParameter("proName", pro);
		List<SubProjects> list = query.list();
		session.close();
		return list;
	}

	@Override
	public String addProSubPro(int userID,String proName, Set<String> subPros)
	{
		String returnResult = "";
		String component = "addProSubPro";
		if (sessionFactory == null)
		{
			returnResult = "Exception";
			Misc.writeToFile(AddProjectDao.class, component, "MANAGER WORK", "Session Factory Is NULL!");
			return returnResult;
		}
		Set<String> duplicateSubPros = new HashSet<>();
		Misc misc = new Misc();
		
		Session session = null;
		Transaction transaction = null;
		try 
		{
			session = sessionFactory.openSession();
		
			Projects projects = null;
		
			int proId = misc.getProId(session, proName);
		
			LoginSignup user = misc.getUserObj(session, userID);
		
		
			boolean isNew = false;
		
			if (proId == 0) 
			{
				isNew = true;
				projects = new Projects();
				projects.setProject_name(proName);
			} 
			else 
			{
				if (subPros.size() == 0) 
				{
					returnResult = "Project Already Saved!";
					return returnResult;
				}
				projects = (Projects) session.get(Projects.class, proId);
			}
			
			transaction = session.beginTransaction();
			String proFolderId = "";
			Drive service = GoogleDriveImpl.getDriveService();
			if(service == null)
			{
				return "API ERROR";
			}
			if(projects.getProFolderId() == null){
				proFolderId = GoogleDriveImpl.createParentFolder(service,projects.getProject_name());
			}
			else
			{
				proFolderId = projects.getProFolderId();
			}
			projects.setProFolderId(proFolderId);
			
			if(isNew)
			{
				GoogleDriveImpl.doSharing(service,proFolderId, user.getEmailID());
			}
			
			if (subPros.size() != 0) 
			{
				Set<String> subPros1 = checkSubProExists(session, subPros,proId, duplicateSubPros);
				if (subPros1.size() == 0) 
				{
					returnResult = "Sub Project(s) Already Saved!";
					return returnResult;
				}
				Iterator<String> itr = subPros1.iterator();
				SubProjects subprojects = null;
				
				while (itr.hasNext()) 
				{
					subprojects = new SubProjects();
					subprojects.setSubPro_name(itr.next().toString());
					String subProFolderId = GoogleDriveImpl.createChildFolder(service,proFolderId, subprojects.getSubPro_name());
					subprojects.setSubProFolderId(subProFolderId);
					subprojects.setPro_id(projects);
					session.save(subprojects);
				}
			} 
			else 
			{
				session.save(projects);
			}
			transaction.commit();
			session.close();
			returnResult = "Saved Successfully :)";
			
			if (duplicateSubPros.size() > 0) 
			{
				returnResult += "@";
				Iterator<String> it = duplicateSubPros.iterator();
				while (it.hasNext()) 
				{
					returnResult += "" + it.next() + ",";
				}
				returnResult = returnResult.substring(0,returnResult.length() - 1);
			}
		} 
		catch(SocketTimeoutException e)
		{
			if(transaction != null)
				transaction.rollback();
			Misc.writeToFile(AddProjectDao.class, component, "MANAGER WORK",e.toString());
			return "API ERROR";
		}
		catch (Exception ex) 
		{
			System.out.println("exception while saving the pro_subPro" + ex);
			if(transaction != null)
				transaction.rollback();
			Misc.writeToFile(AddProjectDao.class, component, "MANAGER WORK",ex.toString());
			returnResult = "rollBack";
		}
		finally
		{
			if(session != null)
			{
				if(session.isOpen())
					session.close();
			}
		}
		return returnResult;
	}
}
