
<%@page import="dao.AddProjectDao"%>
<%@page import="interfaces.IAddTicketDao"%>
<%@page import="dao.ForgotPasswordDao"%>
<%@page import="interfaces.IAddProjectDao"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="interfaces.IForgotPasswordDao"%>
<%@page import="model.SubProjects"%>
<%
	synchronized (this) {
		if (request.getParameter("project") == null) {
			IForgotPasswordDao forgotPassword = new ForgotPasswordDao();
			try {
				String question = forgotPassword.getQuest(request
						.getParameter("emailId"));
				if(question.equals(""))
				{
					out.print("Check Your Email ID");
				}
				else
				{
					out.print(question);
				}
			} catch (Exception ex) {
				out.print("Exception Occurs!");
			}
		} else if (request.getParameter("project") != null) {
			try {
				IAddProjectDao subList = new AddProjectDao();
				List<SubProjects> list = subList.getSubProList(request
						.getParameter("project"));
				if (list.size() == 0) {
					out.print("Nothing");
				} else {
					String subPros = "";
					for (int i = 0; i < list.size(); i++) {
						subPros += list.get(i);
						if (i != list.size() - 1) {
							subPros += ",";
						}
						if (i % 3 == 0 && i != 0) {
							subPros += "\n";
						}
					}
					out.print(subPros);
				}

			} catch (Exception ex) {
				out.print("Exception Occurs!");
			}

		}
	}
%>




