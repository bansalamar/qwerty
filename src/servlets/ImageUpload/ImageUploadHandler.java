package servlets.ImageUpload;

import interfaces.IEditUserDetailsImpl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LoginSignup;
import service.EditUserDetailsImpl;
import service.Misc;

@WebServlet("/ImageUploadHandler")
@MultipartConfig(location="D:\\Data")
public class ImageUploadHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		synchronized (this)
		{
			HttpSession session = req.getSession();
			String ip = req.getRemoteAddr();
			try
			{
				System.err.println(req.getParameter("user_id"));
				int id = Integer.parseInt(req.getParameter("user_id"));
				IEditUserDetailsImpl editUserDetails = new EditUserDetailsImpl();
				LoginSignup user = editUserDetails.uploadImage(id,req, ip);
				if(user.getErrMsg().equalsIgnoreCase("No error"))
				{
					session.setAttribute("user",new Misc().getUserObjFromSession(id));
					session.setAttribute("queryMsg","Updated Successfully :)");
				}
				else
				{
					session.setAttribute("queryMsg",user.getErrMsg());
				}
			}
			catch(Exception ex)
			{
				System.out.println("Exception at servlet while saving image: "+ex);
				session.setAttribute("queryMsg","Some Exception Occurs!");
			}
			session.setAttribute("openPage", "'../jsp/editprofile.jsp'");
			resp.sendRedirect("jsp/userDetails.jsp");
		}
	}
}
