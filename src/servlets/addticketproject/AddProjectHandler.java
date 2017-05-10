package servlets.addticketproject;

import interfaces.IAddProjectImpl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.AddProjectImpl;

@WebServlet("/AddProjectHandler")
public class AddProjectHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		synchronized (this) 
		{
			HttpSession session = request.getSession();
			String msg = "";
			String project_name = request.getParameter("pro_names");
			String project_name1 = request.getParameter("pro_name");
			
			String final_Pro_Name = "";
			
			if(null != project_name && !project_name.equals("default"))
			{
				final_Pro_Name = project_name;
			}
			else
			{
				final_Pro_Name = project_name1;
			}
			
			if(final_Pro_Name == null || final_Pro_Name == "")
			{
				msg = "Select/Add Project First!";
				//response.sendRedirect("jsp/userDetails.jsp?msg="+msg);
				session.setAttribute("queryMsg",msg);
			}
			else
			{
				String sub_project_name = request.getParameter("sub_pro_name");
				String sub_project_name1 = request.getParameter("sub_pro_name1");
				String sub_project_name2 = request.getParameter("sub_pro_name2");
				System.out.println(final_Pro_Name+" "+sub_project_name+" "+sub_project_name1+" "+sub_project_name2);
				try
				{
					int userID = Integer.parseInt(request.getParameter("userID"));
					Set<String> subPros = new HashSet<String>();
					if(sub_project_name != null && !sub_project_name.equals("") 
							&& sub_project_name.trim().length() != 0 && !sub_project_name.equalsIgnoreCase("null"))
					{
						subPros.add(sub_project_name);
					}
					if(sub_project_name1 != null && !sub_project_name1.equals("") 
							&& sub_project_name1.trim().length() != 0 && !sub_project_name1.equalsIgnoreCase("null"))
					{
						if(!sub_project_name1.trim().equalsIgnoreCase(sub_project_name.trim()))
							subPros.add(sub_project_name1);
					}
					if(sub_project_name2 != null && !sub_project_name2.equals("") 
							&& sub_project_name2.trim().length() != 0  && !sub_project_name2.equalsIgnoreCase("null"))
					{
						if(!sub_project_name2.trim().equalsIgnoreCase(sub_project_name1.trim()) 
								&& !sub_project_name2.trim().equalsIgnoreCase(sub_project_name.trim()))
							subPros.add(sub_project_name2);
					}
					
					IAddProjectImpl addProjectImpl = new AddProjectImpl();
					
					String result = addProjectImpl.saveProSubPro(userID,final_Pro_Name, subPros);
					
					if(result.equalsIgnoreCase("rollBack") || result.equalsIgnoreCase("Exception") )
					{
						msg = "Some Exception Occurs!";
						//response.sendRedirect("jsp/userDetails.jsp?msg="+msg);
						session.setAttribute("queryMsg",msg);
					}
					else if(result.equalsIgnoreCase("subpro size 0"))
					{
						msg = "Must Have Atleast 1 Sub Project!";
						//response.sendRedirect("jsp/userDetails.jsp?msg="+msg);
						session.setAttribute("queryMsg",msg);
					}
					else if(result.equalsIgnoreCase("API ERROR"))
					{
						msg = "Drive Connection TimeOut....Please Try Again!";
						//response.sendRedirect("jsp/userDetails.jsp?msg="+msg);
						session.setAttribute("queryMsg",msg);
					}
					else
					{
						String duplicate = "";
						System.out.println("at servlet"+result);
						if(result.indexOf('@') > 0)
						{
							String[] combo = result.split("@");
							result = combo[0];
							duplicate = "Already Saved: "+combo[1];
							session.setAttribute("duplicate", duplicate);
						}
						//response.sendRedirect("jsp/userDetails.jsp?msg="+result);
						session.setAttribute("queryMsg",result);
					}
				}
				catch(Exception ex)
				{
					System.out.println(ex);
					msg = "Some Exception Occurs!";
					//response.sendRedirect("jsp/userDetails.jsp?msg="+msg);
					session.setAttribute("queryMsg",msg);
				}
			}
			session.setAttribute("openPage", "'../jsp/addNewProject.jsp'");
			session.setAttribute("tabToActive","'nPro'");
			response.sendRedirect("jsp/userDetails.jsp");
		}
	}
}
