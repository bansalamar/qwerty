package service;

import interfaces.ILoginUser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import util.HibernateUtils;

public class Misc {

	public boolean checkForEmptyOrNull(LoginSignup loginSignup, String phase){
		boolean check = true;
		if(phase.equalsIgnoreCase("logIn"))
		{
			
			if (loginSignup.getuPassword() == null
					|| loginSignup.getuPassword().equals("") || loginSignup.getuPassword().trim().length() == 0)
			{
				check = false;
			}
			
		}
		else if(phase.equalsIgnoreCase("edit"))
		{
			if(loginSignup.getUserQuest() == null || loginSignup.getUserQuest().equals("") || loginSignup.getUserQuest().trim().length() == 0
				||loginSignup.getUserQuestAns() == null || loginSignup.getUserQuestAns().equals("") || loginSignup.getUserQuestAns().trim().length() == 0
				||loginSignup.getuPassword() == null || loginSignup.getuPassword().equals("") || loginSignup.getuPassword().trim().length() == 0
				||loginSignup.getuId() <= 0)
			{
				check = false;
			}
		}
		else if(phase.equalsIgnoreCase("update"))
		{
			if(loginSignup.getEmailID() == null || loginSignup.getEmailID().equals("")  || loginSignup.getEmailID().trim().length() == 0
				||loginSignup.getuFName() == null || loginSignup.getuFName().equals("") || loginSignup.getuFName().trim().length() == 0
				||loginSignup.getuLName() == null || loginSignup.getuLName().equals("") || loginSignup.getuLName().trim().length() == 0
				||loginSignup.getuId() <= 0)
			{
				check = false;
			}
		}
		else
		{
			if(loginSignup.getuFName() == null || loginSignup.getuFName().equals("") || loginSignup.getuFName().trim().length() == 0
					||loginSignup.getuLName() == null || loginSignup.getuLName().equals("") || loginSignup.getuLName().trim().length() == 0
					||loginSignup.getEmailID() == null || loginSignup.getEmailID().equals("") || loginSignup.getEmailID().trim().length() == 0
					||loginSignup.getuPassword() == null || loginSignup.getuPassword().equals("") || loginSignup.getuPassword().trim().length() == 0
					||loginSignup.getUserQuest() == null || loginSignup.getUserQuest().equals("") || loginSignup.getUserQuest().trim().length() == 0
					||loginSignup.getUserQuestAns() == null || loginSignup.getUserQuestAns().equals("") || loginSignup.getUserQuestAns().trim().length() == 0
					 )
			{
				check = false;
			}
		}
		return check;
	}
	
	public boolean validateEmail(String emailId){
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(emailId);
		return matcher.matches();
	}
	
	@SuppressWarnings("unchecked")
	public int getProId(Session session,String proName){
		int proId = 0;
		Criteria criteria = session.createCriteria(Projects.class);
		criteria.setProjection(Projections.property("project_id"));
		criteria.add(Restrictions.like("project_name", proName));
		List<Integer> list = criteria.list(); 
		if(list.size() > 0){
			proId = list.get(0);
		}
		return proId;
	}
	
	@SuppressWarnings("unchecked")
	public int getSubProId(Session session,String subProName){
		int subProId = 0;
		Criteria criteria = session.createCriteria(SubProjects.class);
		criteria.setProjection(Projections.property("subPro_id"));
		criteria.add(Restrictions.like("subPro_name", subProName));
		List<Integer> list = criteria.list(); 
		if(list.size() > 0){
			subProId = list.get(0);
		}
		return subProId;
	}
	
	@SuppressWarnings("unchecked")
	public int getTicketId(Session session,String tNo)
	{
		int tId = 0;
		Criteria criteria = session.createCriteria(Ticket.class);
		criteria.add(Restrictions.like("ticketNum",tNo));
		criteria.setProjection(Projections.property("ticketId"));
		List<Integer> list = criteria.list(); 
		if(list.size() > 0)
		{
			tId = list.get(0);
		}
		return tId;
	}
	
	public LoginSignup getUserObj(Session session,int uId){
		LoginSignup in_SignUp = (LoginSignup)session.get(LoginSignup.class, uId);
		return in_SignUp;
	}
	
	public Projects getProObj(Session session,int pID) {
		Projects projects = (Projects) session.get(Projects.class,pID);
		return projects;
	}
	
	public Ticket getTicketObj(Session session,int tId) {
		Ticket ticket = (Ticket) session.get(Ticket.class,tId);
		return ticket;
	}
	
	public SubProjects getSubProObj(Session session,int sPID) {
		SubProjects subProjects = (SubProjects) session.get(SubProjects.class,sPID);
		return subProjects;
	}
	
	
	public LoginSignup getUserObjFromSession(int uId){
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.openSession();
		LoginSignup in_SignUp = (LoginSignup)session.get(LoginSignup.class, uId);
		session.close();
		return in_SignUp;
	}
	
	
	public static void writeToFile(Class<?> c,String methodName,String ip,String msg) {
		try {
			boolean emptyLine = true;
			File file=new File("D:/logs");
			if(!file.exists())
				file.mkdir();
			String filename = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
			String time = new SimpleDateFormat("hh:mm:ss").format(Calendar.getInstance().getTime());
			file = new File("D:/logs/"+filename + ".txt");
			if(!file.exists())
				emptyLine = false;
			FileWriter writer = new FileWriter(file, true);
			BufferedWriter bufWriter = new BufferedWriter(writer);
			if(emptyLine)
			{
				bufWriter.newLine();
			}
			bufWriter.write(c.getSimpleName()+" : "+methodName+" : "+time+" : "+ip+" --> "+msg);
			bufWriter.close();
		} 
		catch (Exception ex) 
		{
			System.out.println("Exception while writing file:   " + ex);
		}
	}
	
	public void deleteUser(int user_id) {
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.openSession();
		
		Transaction transaction = session.beginTransaction();
		
		LoginSignup in_SignUp = (LoginSignup) session.get(LoginSignup.class, user_id);
		
		Set<Ticket> tickets = in_SignUp.getTickets();
		Iterator<Ticket> iterator = tickets.iterator();
		while (iterator.hasNext()) {System.out.println("hey");
			Ticket ticket = (Ticket) iterator.next();
			session.delete(ticket);
		}
		in_SignUp.setProjects(null);
		in_SignUp.setManager(null);
		in_SignUp.setSubProject(null);
		session.delete(in_SignUp);
		transaction.commit();
		session.close();
		System.out.println("done");
	}

/*	public String encodePassword(String origPwd) {
		String encopass = "";
			for (int i = 0; i < origPwd.length(); i++) 
			{
				char each = origPwd.charAt(i);
				int ascii = (int)(each);
				if(i%2==0)
				{
					ascii = ascii+27;
					
				}
				else
				{
					ascii = ascii + 30;
				}
				each = (char)ascii;
				
				encopass += each;
			}
		return encopass;
	
	}
	
	public static String decodePassword(String encoded) {

		String decopass = "";
			for (int i = 0; i < encoded.length(); i++) 
			{
				char each = encoded.charAt(i);
				int ascii = (int)(each);
				if(i%2==0)
				{
					ascii = ascii - 27;
				}
				else
				{
					ascii = ascii - 30;
				}
				each = (char)ascii;
				
				decopass+=each;
			}
		return decopass;
	}*/
	
	@SuppressWarnings("unchecked")
	public LoginSignup getActiveReportingManager(Session session , int projectId, int subProId, int positionId)
	{
		LoginSignup signup = null;
		List<LoginSignup> managers = new ArrayList<LoginSignup>();
		int positionToFind = -1;
		
		Projects project = getProObj(session, projectId);
		
		Criteria criteria = session.createCriteria(LoginSignup.class);
		criteria.add(Restrictions.like("projects",project));
		if(positionId == ILoginUser.DEVELOPER_ID)
		{
			positionToFind = ILoginUser.TEAM_LEAD_ID;
			SubProjects subProject = getSubProObj(session, subProId);
			criteria.add(Restrictions.like("subProject",subProject));
		}
		else if(positionId == ILoginUser.TEAM_LEAD_ID)
		{
			positionToFind = ILoginUser.PROJECT_MANAGER_ID;
		}
		
		criteria.add(Restrictions.like("positionId",positionToFind));
	
		managers = criteria.list();
		System.out.println(criteria.list());
		if(managers.size() > 1)
		{
			Iterator<LoginSignup> iterator = managers.iterator();
			while (iterator.hasNext()) 
			{
				LoginSignup loginSignup = (LoginSignup) iterator.next();
				System.out.println(loginSignup);
				Character activeFlag = loginSignup.getUserActiveOrNot();
				if(activeFlag != null && activeFlag == 'Y')
				{
					signup = loginSignup;
					System.out.println(signup.getuFName()+" "+signup.getManager().getuFName());
					break;
				}
			}
		}
		
		return signup;
	}
	
	public boolean isProHaveMngr(Session session , int projectId, int subProId, int positionId)
	{
		Projects project = getProObj(session, projectId);
		
		Criteria criteria = session.createCriteria(LoginSignup.class);
		criteria.add(Restrictions.like("projects",project));
		
		if(positionId == ILoginUser.TEAM_LEAD_ID)
		{
			SubProjects subProject = getSubProObj(session, subProId);
			criteria.add(Restrictions.like("subProject",subProject));
		}
		criteria.add(Restrictions.like("positionId",positionId));
	
		if(criteria.list().size() >= 1)
		{
			System.out.println("have manager");
			return true;
		}
		return false;
	}
	
	public boolean checkforProject(String pro)
	{
		boolean check = true;
		System.err.println(pro);
		if(pro.equalsIgnoreCase("default") || pro.equals("") || pro == null || pro.trim().length() == 0)
			check = false;

		return check;
	}
	public String extractTicketNum(String tNum)
	{
		String num = "";
		if(tNum.substring(0, 7).equalsIgnoreCase("Ticket#"))
		{
			num = tNum.substring(7);
		}
		else if(tNum.substring(0, 4).equalsIgnoreCase("SCR#"))
		{
			num = tNum.substring(4);
		}
		return num.trim();
		
	} 
	
public static void main(String[] args) {
	Session session = HibernateUtils.getSessionFactory().openSession();
	System.out.println(new Misc().isProHaveMngr(session,1, 3, 3));
}	
	
}

