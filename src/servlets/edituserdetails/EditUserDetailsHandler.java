package servlets.edituserdetails;

import interfaces.IEditUserDetailsImpl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LoginSignup;
import service.EditUserDetailsImpl;
import service.Misc;

@WebServlet("/EditUserDetailsHandler")
public class EditUserDetailsHandler extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		synchronized (this) 
		{
			String ip = request.getRemoteAddr();
			HttpSession session = request.getSession();
			IEditUserDetailsImpl editUserDetails = new EditUserDetailsImpl();
			if (request.getParameter("flag") == null)
			{
				try 
				{
					String userID = request.getParameter("userid");
					int id = Integer.parseInt(userID.substring(4));
					String fname = request.getParameter("fname");
					String lname = request.getParameter("lname");
					String emailid = request.getParameter("emailid");

					String oldPwd = request.getParameter("password");
					System.out.println("old: " + oldPwd);
					String newPassword = request.getParameter("newPassword");
					System.out.println(fname + " " + lname + " " + emailid
							+ " " + userID);

					LoginSignup in_SignUp = new LoginSignup();
					in_SignUp.setuId(id);
					in_SignUp.setEmailID(emailid);
					in_SignUp.setuFName(fname);
					in_SignUp.setuLName(lname);
					in_SignUp.setErrMsg("");
					if (oldPwd != "" || oldPwd != null) {
						in_SignUp.setuPassword(oldPwd);
					}

					LoginSignup user = editUserDetails.editUserDetailsSave(ip,in_SignUp, newPassword);
					if (user.getErrMsg().equalsIgnoreCase("No error"))
					{
						session.setAttribute("user",new Misc().getUserObjFromSession(id));
						//response.sendRedirect("jsp/userDetails.jsp?msg2=Updated Successfully :)");
						session.setAttribute("queryMsg","Updated Successfully :)");
					} else {
						//response.sendRedirect("jsp/userDetails.jsp?msg2="+ user.getErrMsg());
						session.setAttribute("queryMsg",user.getErrMsg());
					}
				}
				catch(Exception ex)
				{
					System.out.println("Exception while update details of user: "+ex);
					session.setAttribute("queryMsg","Some Exception Occurs!");
				}
				session.setAttribute("openPage", "'../jsp/editprofile.jsp'");
				response.sendRedirect("jsp/userDetails.jsp");
			} 
			else 
			{
				String userID = request.getParameter("user_id").trim();
				String project = request.getParameter("pro_names");
				String subProject = request.getParameter("sub_pro_names");
				if (project == null) {
					project = "0";
				}
				
				if (subProject == null) {
					subProject = "0";
				}
				
				System.out.println("in servlet");
				System.out.println(project+" "+subProject);
				try 
				{
					String result = editUserDetails.saveUserDetails(ip,Integer.parseInt(userID), project.trim(),subProject.trim());
					if (result.equalsIgnoreCase("proDefault")) 
					{
						//response.sendRedirect("jsp/userDetails.jsp?msg2=Enter Project Name!");
						session.setAttribute("queryMsg","Enter Project Name!");
						session.setAttribute("openPage", "'../jsp/editprofile.jsp'");
					}
					else if(result.equalsIgnoreCase("mngrExist"))
					{
						System.out.println("Manager Exist Already");
						session.setAttribute("queryMsg","Manager Already Exists...Please Contact Higher Authorities!");
						session.setAttribute("openPage", "'../jsp/editprofile.jsp'");
						
					}
					else if (result.substring(0,result.indexOf('@')).equalsIgnoreCase("success")) 
					{
						System.err.println("Project add success");
						session.setAttribute("user", new Misc().getUserObjFromSession(Integer.parseInt(userID)));
						//response.sendRedirect("jsp/userDetails.jsp?msg1=Added Successfully :)");
						session.setAttribute("queryMsg","Added Successfully :)");
						if(result.substring(result.indexOf('@')+1).equals("4"))
						{
							session.setAttribute("openPage", "'../jsp/addTicket.jsp'");
							session.setAttribute("tabToActive","'ticket'");
						}
						else
						{
							session.setAttribute("openPage", "'../jsp/editprofile.jsp'");
						}
					}
				} 
				catch (Exception e) 
				{
					System.out.println("Exception in saving user details projects, subprojects servlet:"+ e);
					session.setAttribute("queryMsg","Some Exception Occurs!");
					session.setAttribute("openPage", "'../jsp/editprofile.jsp'");
				}
				response.sendRedirect("jsp/userDetails.jsp");
			}
		}
	}
}
