<%@page import="model.LoginSignup"%>
<%@page import="java.util.List"%>
<jsp:useBean id="obj1_1" class="dao.AddProjectDao" scope="session" />
<script type="text/javascript">
	var request = new XMLHttpRequest();

	function subProList() {
		removeOptions(document.getElementById("dropdown_subProjects"));
		var subPros = new Array();
		var pro = document.getElementById("dropdown_projects1").value;
		if (pro != "default") {
			var url = "../jsp/getquestion_subPro.jsp?project=" + pro;
			try {
				request.onreadystatechange = function() {
					if (request.readyState == 4) {
						var subPro = request.responseText;
						if (subPro.trim() != "Nothing"
								&& subPro.trim() != "Exception") {
							var subProList = document
									.getElementById("dropdown_subProjects");
							subProList.disabled = false;

							subPros = subPro.trim().split(",");

							for ( var i = 0; i < subPros.length; i++) {
								var opt = document.createElement('option');
								opt.innerHTML = subPros[i];
								opt.value = subPros[i];
								subProList.appendChild(opt);
							}
						} else if (subPro.trim() == "Nothing"
								&& subPro.trim() != "Exception") {
							var subProList = document
									.getElementById("dropdown_subProjects");
							subProList.disabled = true;
						} else if (subPro.trim() == "Exception") {
							swal("Some Exception Occurs", "");
						}
					}
				};//end of function  
				request.open("GET", url, true);
				request.send();
			} catch (e) {
				alert("Unable to connect to server");
			}
		}
	}

	function removeOptions(selectbox) {
		var i;
		for (i = selectbox.options.length - 1; i >= 0; i--) {
			selectbox.remove(i);
		}
	}
</script>
<br>
<div class="container-fluid">
	<div class="panel-group" id="accordion">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a data-toggle="collapse" data-parent="#accordion"
						href="#collapse1">ADD/UPDATE PROJECT MANAGER</a>
				</h4>
			</div>
			<div id="collapse1" class="panel-collapse collapse in">
				<div class="container-fluid">
					<form role="form" style="margin-top: 20px" class="form-horizontal"
						action="../AddPMTLHandler" method="post">
						<div class="form-group">
							<div class="col-sm-6">
								<label>FIRST NAME:</label> <input type="text"
									class="form-control" name="fName">
							</div>
							<div class="col-sm-6">
								<label>LAST NAME:</label> <input type="text"
									class="form-control" name="lName">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12">
								<label>EMAIL ID:</label> <input type="email"
									class="form-control" name="emailID">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12">
								<label>PROJECT NAME</label> <select class="form-control"
									id="dropdown_projects" name="pro_names">
									<option value="default">Select project</option>
									<%
										List<String> list = obj1_1.getProList();
										for (int i = 0; i < list.size(); i++) {
									%>
									<option value="<%=list.get(i)%>"><%=list.get(i)%></option>
									<%
										}
									%>
								</select>
							</div>
						</div>
						<input type="hidden" name="pmtl" value="PM">
						<input type="submit" class="btn btn-primary btn-block" value="SUBMIT" />
					</form>
					<br>
				</div>
			</div>
		</div>
		<br>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a data-toggle="collapse" data-parent="#accordion"
						href="#collapse2">ADD/UPDATE TEAM LEAD</a>
				</h4>
			</div>
			<div id="collapse2" class="panel-collapse collapse">
				<div class="container-fluid">
					<form role="form" style="margin-top: 20px" class="form-horizontal"
						action="../AddPMTLHandler" method="post">

						<div class="form-group">
							<div class="col-sm-6">
								<label>FIRST NAME:</label> <input type="text"
									class="form-control" name="fName">
							</div>
							<div class="col-sm-6">
								<label>LAST NAME:</label> <input type="text"
									class="form-control" name="lName">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12">
								<label>EMAIL ID:</label> <input type="email"
									class="form-control" name="emailID">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12">
								<label>PROJECT NAME</label> <select class="form-control"
									id="dropdown_projects1" name="pro_names"
									onchange="subProList()">
									<option value="default">Select project</option>
									<%
										List<String> list1 = obj1_1.getProList();
										for (int i = 0; i < list1.size(); i++) {
									%>
									<option value="<%=list1.get(i)%>"><%=list1.get(i)%></option>
									<%
										}
									%>
								</select>
							</div>
						</div>

						<div class="form-group">
							<div class="col-sm-12">
								<label>SUB PROJECT NAME</label> <select class="form-control"
									id="dropdown_subProjects" name="sub_pro_names"
									disabled="disabled">
								</select>
							</div>
						</div>
						<input type="hidden" name="pmtl" value="TL">
						<input type="submit" class="btn btn-primary btn-block" value="SUBMIT" />
					</form>
					<br>
				</div>
			</div>
		</div>

	</div>
</div>