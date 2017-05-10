<%@page import="model.LoginSignup"%>
<%@page import="java.util.List"%>
<jsp:useBean id="obj" class="dao.AddProjectDao" scope="session" />
<script type="text/javascript">
var request=new XMLHttpRequest();  
function subPro()
	{
		var pro = document.getElementById("dropdown_projects").value;
		if(pro != "default"){  
	 		var url="../jsp/getquestion_subPro.jsp?project="+pro;  
			try{  
				request.onreadystatechange = function(){  
					if(request.readyState==4){  
						var subPro=request.responseText; 
						if(subPro.trim() != "Nothing" && subPro.trim() != "Exception" ){
						swal("Sub Project(s) Added : \n"+subPro.trim(),"");
						}
						else if(subPro.trim() == "Exception"){
							swal("Some Exception Occurs","");
							}
					}  
				}//end of function  
				request.open("GET",url,true);  
				request.send();  
			}catch(e){alert("Unable to connect to server");} 
		}
	}
	</script>
	
<form role="form" style="margin-top: 20px" class="form-horizontal"
	action="../AddProjectHandler" method="post">
	<%if(session.getAttribute("duplicate")!=null) {%>

<div style="color: #337ab7;text-align: center;font-weight: bolder; font-size: 20px;"><%=session.getAttribute("duplicate") %>
</div>
<%
session.setAttribute("duplicate", null); } %>
	
	<div class="form-group">
		<div class="col-sm-12">
			<label>PROJECT NAME</label> <select class="form-control"
				id="dropdown_projects" name="pro_names" onchange="subPro()">
				<option value="default">Select Existing project</option>
				<%List<String> list = obj.getProList();
				for(int i=0;i<list.size();i++){
				%>
				<option value="<%=list.get(i)%>"><%=list.get(i) %></option>
				<%} %>
			</select>
		</div>
	</div>
	<div style="margin-left: 95%">
		<a href="#" data-toggle="collapse" data-target="#demo"
			onclick="disable()" id="new_not">New</a>
	</div>
	<div class="col-sm-12">
		<div id="demo" class="collapse">
			<div class="form-group">
				<label>NEW PROJECT NAME</label> <input id="new_pro_name" type="text"
					class="form-control" name="pro_name" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-12">
			<label>SUB PROJECT NAME</label> <input type="text"
				class="form-control" name="sub_pro_name" placeholder="Leave if not available"/>
		</div>
	</div>

	<div style="margin-left: 95%">
		<a href="#" data-toggle="collapse" data-target="#demo1"><img
			id="plus_minus" src="../images/plus.ico" width="25" height="25"
			onclick="changeImg()"></a>
	</div>
	<div class="col-sm-12">
		<div id="demo1" class="collapse">
			<div class="form-group">
				<input type="text" class="form-control"
					placeholder="Sub Project(Leave if not available)" id="sub1"
					name="sub_pro_name1" /><BR /> 
				<input type="text"
					class="form-control"
					placeholder="Sub Project(Leave if not available)" id="sub2"
					name="sub_pro_name2" />
			</div>
		</div>
	</div><%LoginSignup user = (LoginSignup)session.getAttribute("user");%>
	<input type="hidden" name="userID" value="<%=user.getuId()%>"/>
	<button type="submit" class="btn btn-primary btn-block">SUBMIT</button>
</form>
