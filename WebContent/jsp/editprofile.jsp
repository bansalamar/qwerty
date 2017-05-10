<%@page import="model.SubProjects"%>
<%@page import="model.LoginSignup"%>
<%@page import="java.util.List"%>
<jsp:useBean id="obj1" class="dao.AddProjectDao" scope="session" />

<script type="text/javascript">
var request=new XMLHttpRequest();  

function subProList()
{
	removeOptions(document.getElementById("dropdown_subProjects"));
	var subPros = new Array();
		var pro = document.getElementById("dropdown_projects").value;
		if(pro != "default"){  
	 		var url="../jsp/getquestion_subPro.jsp?project="+pro;  
			try{  
				request.onreadystatechange = function(){  
					if(request.readyState==4){  
						var subPro=request.responseText; 
						if(subPro.trim() != "Nothing" && subPro.trim() != "Exception" ){
							var subProList = document.getElementById("dropdown_subProjects");
							subProList.disabled = false;

	subPros=subPro.trim().split(",");
							 

for(var i = 0; i < subPros.length; i++) {
    var opt = document.createElement('option');
    opt.innerHTML = subPros[i];
    opt.value = subPros[i];
    subProList.appendChild(opt);
}
						}
						else if(subPro.trim() == "Nothing" && subPro.trim() != "Exception" ){
							var subProList = document.getElementById("dropdown_subProjects");
							subProList.disabled = true;
							}
						else if(subPro.trim() == "Exception"){
							swal("Some Exception Occurs","");
							}
					}  
				};//end of function  
				request.open("GET",url,true);  
				request.send();  
			}catch(e){alert("Unable to connect to server");} 
		}
	}



function removeOptions(selectbox)
{
    var i;
    for(i=selectbox.options.length-1;i>=0;i--)
    {
        selectbox.remove(i);
    }
}

function checkSame()
{
	var oldPassword = document.getElementById("password").value;
	var newPassword = document.getElementById("newPassword").value;
	var confirmPassword = document.getElementById("confirmPassword").value;
	if(oldPassword != "")
	{
		if(newPassword == "" || confirmPassword == "")
		{
			swal("Complete Details Required To Change Password!","");
			return;		
		}		
	}
	if(oldPassword == "" && (newPassword != "" || confirmPassword != ""))
	{
		swal("Enter Your Password!","");
		return;		

	}
	
	if(newPassword == confirmPassword)
	{
		document.getElementById("editProfile").submit();
	}
	else
	{
		swal("Passwords must be same","");
		document.getElementById("newPassword").value = "";
		document.getElementById("confirmPassword").value = "";
	}
}
	</script>
<script>

<%LoginSignup in_SignUp = (LoginSignup)session.getAttribute("user");
if(in_SignUp.getProjects() != null){%>
document.getElementById("addPro").innerHTML="CHANGE PROJECT";
<%}else{
	if(in_SignUp.getPositionId()!=1){
%>

document.getElementById("addPro").innerHTML="ADD PROJECT";
<%}
	}%>

</script>
<ul id="myTab" class="nav nav-tabs">
	<li class="active"><a href="#home" data-toggle="tab"
		onclick="document.getElementById('divId').style.visibility = 'hidden'">
			BASIC DETAILS </a></li>

	<%if(in_SignUp.getPositionId()!=1) { %>
	<li><a href="#projects" data-toggle="tab" id="addPro"
		onclick="document.getElementById('divId').style.visibility = 'visible'"></a></li>
	<%} %>
</ul>

<div id="myTabContent" class="tab-content">
	<div class="tab-pane fade in active" id="home">
		<h1>Edit Profile</h1>
		<hr>
		<div class="row">
			<!-- left column -->
			<%-- <div class="col-md-3">
			<form action="../ImageUploadHandler" enctype="multipart/form-data" method="post">
			
				<div class="text-center"><%if(in_SignUp.getUserPicPath()!=null){%>
					<img src="../images/<%=in_SignUp.getUserPicPath()%>" class="avatar img-circle"
						alt="Error"  height="150" width="150" >					
				<%}else{%>
					
					<img src="../images/defaultImg.jpg" class="avatar img-circle" alt="Error">
				<%}%>
					<h6>Upload a different photo...</h6>
					<input type="file" class="form-control" name="userImage" id="userImage">
				</div>
				<input type="hidden" name="user_id" value="<%=in_SignUp.getuId()%>">
				<br>
				<input type="submit" class="btn btn-primary btn-block" value="Upload">
			</form>
			</div> --%>

			<!-- edit form column -->
			<div class="col-md-12 personal-info">
				<h3>Personal info</h3>

				<form class="form-horizontal" role="form" action="../EditUserDetailsHandler" id="editProfile" method="post">
					<div class="form-group">
						<label class="col-lg-3 control-label">First name:</label>
						<div class="col-lg-8">
							<input class="form-control" type="text" value="<%=in_SignUp.getuFName()%>" name="fname">
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">Last name:</label>
						<div class="col-lg-8">
							<input class="form-control" type="text" value="<%=in_SignUp.getuLName()%>" name= "lname">
						</div>
					</div>
					<div class="form-group">
						<label class="col-lg-3 control-label">Email:</label>
						<div class="col-lg-8">
							<input class="form-control" type="text"
								value="<%=in_SignUp.getEmailID()%>" name="emailid">
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label">UserID:</label>
						<div class="col-md-8">
							<input class="form-control" type="text" value="ISO_<%=in_SignUp.getuId()%>" name="userid" readonly="readonly">
						</div>
					</div>

					<div style="margin-left: 90%">
						<a href="#" data-toggle="collapse" data-target="#change"><img
							id="changeOrNot" src="../images/changepass.png" width="25" height="25"
							onclick="changePassImg()"></a>
					</div>

					<div class="col-sm-12">
						<div id="change" class="collapse">
							<div class="form-group">
								<label class="col-md-3 control-label">Password:</label>
								<div class="col-md-8">
									<input class="form-control" type="password" value=""
										name="password" id="password">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">New Password:</label>
								<div class="col-md-8">
									<input class="form-control" type="password" value=""
										name="newPassword" id="newPassword">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">Confirm password:</label>
								<div class="col-md-8">
									<input class="form-control" type="password" value=""
										name="confirmPassword" id="confirmPassword">
								</div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-3 control-label"></label>
						<div class="col-md-8">
							<input type="button" class="btn btn-primary" value="Save Changes" onclick="checkSame()">
							 <!-- <input type="reset" class="btn btn-default"
								value="Cancel"> -->
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
<div id="divId" style="color: #337ab7;text-align: center;font-weight: bolder; font-size: 20px;visibility: hidden;"><%
if(in_SignUp.getProjects() != null){%>
Project:<%=in_SignUp.getProjects()%>&nbsp;&nbsp;&nbsp;&nbsp;Sub-Project:<%
SubProjects subProjects = in_SignUp.getSubProject();
String name = (subProjects == null) ? "No Sub Project" : subProjects.getSubPro_name();
out.print(name);
 %> 

<%}%>
</div>
	<div class="tab-pane fade" id="projects">
	<form role="form" style="margin-top: 20px" class="form-horizontal" action="../EditUserDetailsHandler" method="post">

	<div class="form-group">
		<div class="col-sm-12">
			<label>PROJECT NAME</label> 
			<%if(in_SignUp.getPositionId()!=2){
			%>
			<select class="form-control" id="dropdown_projects" name="pro_names" onchange="subProList()">
				<option value="default">Select Existing project</option>
				<%List<String> list = obj1.getProList();
				for(int i=0;i<list.size();i++){
				%>
				<option value="<%=list.get(i)%>"><%=list.get(i) %></option>
				<%} %>
			</select>
			<%}else{
				%>
				<select class="form-control" id="dropdown_projects" name="pro_names">
				<option value="default">Select Existing project</option>
				<%List<String> list = obj1.getProList();
				for(int i=0;i<list.size();i++){
				%>
				<option value="<%=list.get(i)%>"><%=list.get(i) %></option>
				<%} %>
			</select>
				<%
				
			} %>
			
		</div>
	</div>
<%if(in_SignUp.getPositionId()!=2){ %>
	<div class="form-group">
		<div class="col-sm-12">
			<label>SUB PROJECT NAME</label> <select class="form-control"
				id="dropdown_subProjects" name="sub_pro_names" disabled="disabled">
				
			</select>
		</div>
	</div>
	<%} %>
	<input type="hidden" value="<%=in_SignUp.getuId()%>" name="user_id">
	<input type="hidden" value="PROJECT" name = "flag">

	<button type="submit" class="btn btn-primary btn-block">SUBMIT</button>
</form>

	</div>

</div>
