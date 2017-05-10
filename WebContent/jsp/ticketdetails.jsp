<%@page import="interfaces.IAddTicketDao"%>
<%@page import="interfaces.IAddTicketImpl"%>
<%@page import="interfaces.IEditUserDetailsDao"%>
<%@page import="interfaces.IAddTicket"%>
<%@page import="model.LoginSignup"%>
<%@page import="model.Ticket"%>
<%@page import="service.TicketDetails"%>
<body>
<%

String t_Num = request.getParameter("t_Num"); 
if(t_Num == null)
{	
	out.print("Some Error Occurs");
}
else
{
	Ticket ticket = new TicketDetails().getTicketDetailsForMngr(t_Num.trim()); 
	if(ticket == null)
	{
		out.print("Ticket Not Found!");
	}
	else
	{	
		String subBy = ticket.getUserId().getuFName()+" "+ticket.getUserId().getuLName();;
		String statusChangerName = ""; 
		String docStatus = "Not Yet Submitted";
		String radStatus = docStatus;
		String esStatus = docStatus;
		String crStatus = docStatus;
		if(ticket.getRadId() != null)
			radStatus = "Submitted";
		if(ticket.getEsDocId() != null)
			esStatus = "Submitted";
		if(ticket.getCrDocId() != null)
			crStatus =  "Submitted";
		
		LoginSignup statusChanger = ticket.getStatusChanger(); 
		if(statusChanger != null)
		{
			statusChangerName = statusChanger.getuFName()+" "+statusChanger.getuLName();
		}
		String status = "";
		if(ticket.getStatus() == IEditUserDetailsDao.TICKET_STATUS_BIASED)
		{
			status = "BIASED";	
		}
		else if(ticket.getStatus() == IEditUserDetailsDao.TICKET_STATUS_PRECISE)
		{
			status = "PRECISE";
		}
		else if(ticket.getStatus() == IAddTicketImpl.TICKET_STATUS_UNCHECKED )
		{
		 	status = "NOT CHECKED YET";
		}
		else if(ticket.getStatus() == IAddTicketDao.TICKET_UPDATED_STATUS)
		{
			status = "UPDATED BY USER";	
		}
	%>
<form style="margin-top: 20px" class="form-horizontal">
	<div class="form-group">
		<div class="col-sm-6">
			<label>TICKET/SCR NO.:</label> <input type="text"
				class="form-control" value="<%=t_Num.trim()%>" disabled="disabled" >
		</div>
		<div class="col-sm-6">
			<label>SUBMITTED BY:</label> <input type="text"
				class="form-control" value="<%=subBy%>" disabled="disabled" >
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-12">
			<label>DESCRIPTION:</label>
			<input type="text"
				class="form-control" value="<%=ticket.getDesciption()%>" disabled="disabled" >
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-12">
			<label>SUBMISSION DATE:</label> <input type="text"
				class="form-control"  value="<%=ticket.getSubmittedDate()%>" disabled="disabled" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-4">
			<label>PROJECT NAME</label><input type="text" class="form-control"
				value="<%=ticket.getProId().getProject_name()%>" disabled="disabled" />
		</div>
		<div class="col-sm-4">
			<label>SUB PROJECT NAME</label><input type="text"
				class="form-control"
				value="<%String subPro = "No Sub Project";
				if (ticket.getSubProId() != null) {
					subPro = ticket.getSubProId().getSubPro_name();
				}
				out.print(subPro);%>" disabled="disabled" />
		</div>
		<div class="col-sm-4">
			<label>SUBMITTED TO</label><input type="text" class="form-control"
				disabled="disabled"
				value="<%=ticket.getMangId().getuFName().toUpperCase()
						+ " "
						+ ticket.getMangId().getuLName().toUpperCase()%>">
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-6">
			<label>VERIFIED BY:</label> <input type="text"
				class="form-control"  value="<%=statusChangerName%>" disabled="disabled" />
		</div>
		<div class="col-sm-6">
			<label>STATUS:</label> <input type="text"
				class="form-control"  value="<%=status%>" disabled="disabled" />
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-12">
			<label>REMARKS:</label> <input type="text"
				class="form-control"  value="<%=ticket.getRemarks()%>" disabled="disabled" />
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-4">
			<label>RAD STATUS:</label> <input type="text"
				class="form-control"  value="<%=radStatus%>" disabled="disabled" />
		</div>
		<div class="col-sm-4">
			<label>ESTIMATION DOC STATUS:</label> <input type="text"
				class="form-control"  value="<%=esStatus%>" disabled="disabled" />
		</div>
		<div class="col-sm-4">
			<label>CHANGE REQUEST DOC STATUS:</label> <input type="text"
				class="form-control"  value="<%=crStatus%>" disabled="disabled" />
		</div>
	</div>
	</form>
	<%}
	}%>
</body>
