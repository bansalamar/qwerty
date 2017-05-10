package servlets.add_pm_tl;

import interfaces.IAddPMTL;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LoginSignup;

import service.AddPMTLImpl;


@WebServlet("/AddPMTLHandler")
public class AddPMTLHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String urlForward = "jsp/userDetails.jsp";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		synchronized (this) 
		{
			HttpSession session = request.getSession();
			String fname          = request.getParameter("fName");
			String lname          = request.getParameter("lName");
			String emailID        = request.getParameter("emailID");
			String projectName    = request.getParameter("pro_names");
			try{
				IAddPMTL addPMTL = new AddPMTLImpl();
				if(request.getParameter("pmtl").equalsIgnoreCase("PM"))
				{
					//PM
					LoginSignup pm = addPMTL.savePM(fname,lname,emailID,projectName);
					if(pm.getErrMsg().equalsIgnoreCase("NO ERROR"))
					{
					//	response.sendRedirect(urlForward+"ADDED SUCCESSFULLY....(ID : ISO_"+pm.getuId()+" )");
						session.setAttribute("queryMsg","ADDED SUCCESSFULLY....(ID : ISO_"+pm.getuId()+" )");
					}
					else
					{
					//	response.sendRedirect(urlForward+pm.getErrMsg());
						session.setAttribute("queryMsg",pm.getErrMsg());
					}
				}
				else
				{
					//TL
					
					String subProjectName = request.getParameter("sub_pro_names");
					LoginSignup tl = addPMTL.saveTL(fname,lname,emailID,projectName,subProjectName);
					if(tl.getErrMsg().equalsIgnoreCase("NO ERROR"))
					{
					//	response.sendRedirect(urlForward+"ADDED SUCCESSFULLY....(ID : ISO_"+tl.getuId()+" )");
						session.setAttribute("queryMsg","ADDED SUCCESSFULLY....(ID : ISO_"+tl.getuId()+" )");
					}
					else
					{
					//	response.sendRedirect(urlForward+tl.getErrMsg());
						session.setAttribute("queryMsg",tl.getErrMsg());
					}
				}
			}
			catch(Exception ex)
			{
				session.setAttribute("queryMsg","Some Exception Occurs!");
				System.out.println("Exception while saving pm or tl:  "+ex);
			}
			response.sendRedirect("jsp/userDetails.jsp");
			session.setAttribute("openPage", "'../jsp/addPMTL.jsp'");
			session.setAttribute("tabToActive","'pmtl'");
		}
	}

}
