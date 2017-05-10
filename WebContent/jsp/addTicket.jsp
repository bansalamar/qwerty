<%@page import="model.LoginSignup"%>
<%
	LoginSignup in_SignUp = (LoginSignup) session.getAttribute("user");
	if (in_SignUp.getProjects() == null) {
%>
<p style="color: red;margin-left: 330px;margin-top: 200px;font-weight: bold;">
You have to Enter project first to add ticket..... :)</p>
<%
	} else if (in_SignUp.getManager() == null) {
%>
<p style="color: red;margin-left: 330px;margin-top: 200px;font-weight: bold;">
This project is not yet assigned to any higher authority.....:|</p>
<%
	}

	else {
%>
<script>
function addTicket()
{
	var t_num = document.getElementById("tno").value.toLowerCase();
	if(t_num.indexOf("ticket#") < 0)
	{	
		if(t_num.indexOf("scr#") < 0)
		{
			swal("Please Enter Prefix Ticket#/SCR# Followed by Number","");
			return;
		}
	}
	
	document.getElementById("addTicketForm").submit();
	document.getElementById("btnAddTicket").disabled=true;
}
</script>
<form role="form" style="margin-top: 20px" class="form-horizontal"
	action="../AddTicketHandler" method="post" enctype="multipart/form-data" id="addTicketForm">
	<div class="form-group">
		<div class="col-sm-12">
			<label>TICKET/SCR NO.:</label> <input type="text"
				class="form-control" name="tno" placeholder="Must Contain Prefix Ticket#/SCR#" id="tno">
		</div>
	</div>
	<input type="hidden" value="<%=in_SignUp.getuId()%>" name="userId">
	<div class="form-group">
		<div class="col-sm-12">
			<label>DESCRIPTION:</label>
			<textarea class="form-control" rows="3" name="tdesc"></textarea>
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-12">
			<label>SUBMISSION DATE:</label> <input type="date"
				class="form-control" id="datepicker" name="tsubdate" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-4">
			<label>PROJECT NAME</label><input type="text" class="form-control"
				value="<%=in_SignUp.getProjects()%>" name="tproname"
				disabled="disabled" />
		</div>
		<div class="col-sm-4">
			<label>SUB PROJECT NAME</label><input type="text"
				class="form-control"
				value="<%String subPro = "No Sub Project";
				if (in_SignUp.getSubProject() != null) {
					subPro = in_SignUp.getSubProject().getSubPro_name();
				}
				out.print(subPro);%>"
				name="tsubproname" disabled="disabled" />
		</div>
		<div class="col-sm-4">
			<label>SUBMITTED TO</label><input type="text" class="form-control"
				disabled="disabled"
				value="<%=in_SignUp.getManager().getuFName().toUpperCase()
						+ " "
						+ in_SignUp.getManager().getuLName().toUpperCase()%>"
				name="tmng">
		</div>
	</div>
	
	<div class="form-group">
		<div class="col-sm-4">
			<label class="btn btn-default btn-file"><b>Requirement Analysis</b><input name="rad" type="file" id="rad"/>
			</label>
		</div>
		<div class="col-sm-4">
			<label class="btn btn-default btn-file"><b>Estimation Doc</b><input name="esdoc" type="file" id="esdoc" />
			</label>
		</div>
		<div class="col-sm-4">
			<label class="btn btn-default btn-file"><b>Change Request</b><input name="chgreq" type="file" id="chgreq" />
			</label>
		</div>
	</div>
	<button type="button" class="btn btn-primary btn-block" onclick="addTicket()" id="btnAddTicket">SUBMIT</button>
</form>
<%
	}
%>