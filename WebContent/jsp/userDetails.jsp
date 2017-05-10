<%@page import="interfaces.ILoginUser"%>
<%if(session.getAttribute("user") == null)
{
	response.sendRedirect("../jsp/login.jsp");
}
else
{ %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="shortcut icon" href="../images/favi.ico" type="image/x-icon">
<%@include file="/jsp/script.jsp"%>
    <script src="../dist/sweetalert.min.js"></script> 
	<link rel="stylesheet" type="text/css" href="../dist/sweetalert.css">
<link rel="stylesheet" type="text/css" href="../css/userDetails.css">
<title>USER DETAILS</title>


<script type="text/javascript">
	function loadPage(page, id)
	{
			var r = document.getElementsByClassName("active");
			if(r[0]!=undefined)
			{
				r[0].className = "";
			}
			document.getElementById(id).className = "active";
			$('#main_div').load(page);	
	}
	
	function onBodyLoad()
	{
		document.getElementById("details").className = "active";
		<%
			String result = (String)session.getAttribute("queryMsg");
			String openPage = (String)session.getAttribute("openPage");  
			String tabToActive = (String)session.getAttribute("tabToActive");
			
			if(result != null)
			{
				%>
				document.getElementById("details").className = "";
				<%
				if(result.equalsIgnoreCase(ILoginUser.SYSTEM))
				{%>
					$('#main_div').load(<%=openPage%>);//systemuser.jsp
				<%}else
				{%>
					swal("<%=result%>","");
					$('#main_div').load(<%=openPage%>);
					<%if(tabToActive != null)
					{%>		
						document.getElementById(<%=tabToActive%>).className = "active";	
				<%
					}
				}
			}
			else
			{
				String tNum = request.getParameter("t_Num");
				if(tNum == null)
				{%>
				$('#main_div').load("../jsp/tickets.jsp");//ticket.jsp
			<%
				}else
				{%>
				document.getElementById("details").className = "";
				$('#main_div').load('ticketdetails.jsp?t_Num=<%=tNum%>');
				<%}
			}
			
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setDateHeader("Expires", 0);
			response.setHeader("Pragma", "no-cache");
			%>
	}

	function searchTicket()
	{
		var t_num = document.getElementById("searchTicket").value.toLowerCase();
		if(t_num.indexOf("ticket#") >= 0 || t_num.indexOf("scr#") >= 0)
		{	
			swal("Please Enter Number Only!","");
			return;
		}
		document.getElementById("details").className = "";
		$('#main_div').load('ticketdetails.jsp?t_Num='+t_num);	
	}
	
	function disable()
	{
		if(document.getElementById("new_not").innerHTML == "Select"){
			document.getElementById("dropdown_projects").disabled = false;
			document.getElementById("new_not").innerHTML = "New";
			document.getElementById("new_pro_name").value = "";
		}
		else{
		 document.getElementById("dropdown_projects").disabled = true;
		 document.getElementById("new_not").innerHTML = "Select";
		 document.getElementById("dropdown_projects").value = "default";
		}
	}
	
	function changeImg() 
	{
		var image = document.getElementById("plus_minus");
		if (image.src.match("plus")) {
	        image.src = "../images/minus.ico";
	    } else {
	    	document.getElementById("sub1").value = "";
	    	document.getElementById("sub2").value = "";
	        image.src = "../images/plus.ico";
	    }
	}


	function changePassImg()
	{
		var image = document.getElementById("changeOrNot");
		if (image.src.match("changepass")) {
	        image.src = "../images/donotchange.png";
	    } else {
	    	document.getElementById("password").value = "";
	    	document.getElementById("newPassword").value = "";
	    	document.getElementById("confirmPassword").value = "";
	        image.src = "../images/changepass.png";
	    }
		
	}
		
</script>
</head>
<body data-spy="scroll" data-target=".navbar" data-offset="50" onload="onBodyLoad()">
	<%@include file="/jsp/userDetailBar.jsp"%>
	<div class="container-fluid" style="margin-top: 50px;">
		<div class="row content">
			<div class="col-sm-3 sidenav">
				<ul class="nav nav-pills nav-stacked">
<!-- 					<li id="dash"><a href="#"
						onclick="loadPage('dashboard.jsp','dash');">DASHBOARD</a></li> -->
 					<%if(user.getPositionId()==4){ %>
					<li id="ticket"><a href="#"
						onclick="loadPage('addTicket.jsp','ticket');">ADD TICKET</a></li>
						<%} %>
					<li id="details"><a href="#"
						onclick="loadPage('tickets.jsp','details');">DETAILS</a></li>
						<%if(user.getPositionId()==1){ %>
					<li id="nPro"><a href="#"
						onclick="loadPage('addNewProject.jsp','nPro');" id="nPro1">ADD PROJECT</a></li>
					<li id="pmtl"><a href="#"
						onclick="loadPage('addPMTL.jsp','pmtl');">ADD/UPDATE PM/TL</a></li>
						<%}%>
				</ul>
				<br>
				<div class="input-group">
					<input type="text" class="form-control"
						placeholder="Ticket/SCR No." id="searchTicket"> <span
						class="input-group-btn">
						<button class="btn btn-default" type="button" onclick="searchTicket()">
							<span class="glyphicon glyphicon-search"></span>
						</button>
					</span>
				</div>
			</div>

			<div class="col-sm-9">
				<div id="main_div" class="container-fluid">
				
				</div>

			</div>
		</div>
	</div>
</body>
</html>

<%
session.setAttribute("queryMsg", null);
session.setAttribute("openPage", null);
session.setAttribute("tabToActive", null);
}%>