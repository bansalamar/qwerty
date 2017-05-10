package servlets.loginsignup;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.LoginSignup;

import org.hibernate.Session;
import org.hibernate.Transaction;

import service.Misc;
import util.HibernateUtils;

@WebServlet("/AddChairman")
public class AddChairman extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try 
		{
			Session session = HibernateUtils.getSessionFactory().openSession();
			String a=request.getParameter("email");
			String d=request.getParameter("fname");
			String e=request.getParameter("lname");
			String f=request.getParameter("pass");
			String g=request.getParameter("quest");
			String h=request.getParameter("answ");
			if(a==null||a.equals("")||a.trim().length()==0||
				d==null|d.equals("")||d.trim().length()==0||
						e==null||e.equals("")||e.trim().length()==0||
								f==null||f.equals("")||f.trim().length()==0||
										g==null||g.equals("")||g.trim().length()==0||
										h==null||h.equals("")||h.trim().length()==0)
			{
				out.print("Enter all details");
			}
			else{
										
			LoginSignup in_SignUp = new LoginSignup();
			in_SignUp.setEmailID(a);
			in_SignUp.setPositionId(1);
			in_SignUp.setManager(null);
			in_SignUp.setuFName(d);
			in_SignUp.setuLName(e);
			//in_SignUp.setuPassword(new Misc().encodePassword(f));
			in_SignUp.setuPassword(f);
			in_SignUp.setUserQuest(g);
			in_SignUp.setUserQuestAns(h);
			in_SignUp.setUserActiveOrNot('Y');
			Transaction transaction = session.beginTransaction();
			int id = (Integer)session.save(in_SignUp);
			transaction.commit();
			session.close();
			out.print("Added Successfully!  Your id: ISO_"+id);
			}
		} catch (Exception ex) {
			System.out.println(ex);
			out.print("Exception");
		}
	}
	
	/*protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Session session = HibernateUtils.getSessionFactory().openSession();
			String a=request.getParameter("email");
			String b=request.getParameter("position");
			String c=request.getParameter("mng");
			String d=request.getParameter("fname");
			String e=request.getParameter("lname");
			String f=request.getParameter("pass");
			String g=request.getParameter("quest");
			String h=request.getParameter("answ");
			String i=request.getParameter("proname");
			String j=request.getParameter("subproname");
		LoginSignup in_SignUp = new LoginSignup();
		in_SignUp.setEmailID(a);
		in_SignUp.setPositionId(Integer.parseInt(b));
		
		in_SignUp.setManager((LoginSignup)session.get(LoginSignup.class,Integer.parseInt(c)));
		in_SignUp.setuFName(d);
		in_SignUp.setuLName(e);
		in_SignUp.setuPassword(new Misc().encodePassword(f));
		in_SignUp.setUserQuest(g);
		in_SignUp.setUserQuestAns(h);
		in_SignUp.setUserActiveOrNot('Y');
		int proId = new Misc().getProId(session,i);
		in_SignUp.setProjects((Projects)session.get(Projects.class, proId));
		int subproId = new Misc().getSubProId(session,j);
		if(subproId==0){
			in_SignUp.setSubProject(null);
		}else{
		in_SignUp.setSubProject((SubProjects)session.get(SubProjects.class, subproId));}
		Transaction transaction = session.beginTransaction();
		if(proId==0){System.out.println("enter pro or subpro");}else{
		session.save(in_SignUp);
		System.out.println("added");
		}
		transaction.commit();
		session.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}*/
}
