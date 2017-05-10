<%@page import="model.LoginSignup"%>
<%LoginSignup user = (LoginSignup)session.getAttribute("user"); %>
<div id="modalgenuser" class="modal fade" data-backdrop="static"
	data-keyboard="false">

	<div class="modal-dialog">
		<div class="modal-content">
		<form action="../CompleteRegHandler" method="post">
			<div class="modal-header">
				<h4 class="modal-title" style="text-align: center;">PLEASE COMPLETE THE REGISTRATION TO PROCEED!</h4>
			</div>
			<div class="modal-body">
				<div class="container-fluid">
						<div class="form-group">
							<div class="col-sm-12">
								<label>SECURITY QUESTION:</label> <input type="text"
									class="form-control" name="userQuest" readonly="readonly"
									value="Whats Your Birth Place?">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12">
								<label>ANSWER:</label> <input type="text" class="form-control"
									name="userAnsw">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12">
								<label>PASSWORD:</label> <input type="password"
									class="form-control" name="pwrd">
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-12">
								<label>CONFIRM PASSWORD:</label> <input type="password"
									class="form-control" name="conpwrd">
							</div>
						</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="submit" class="btn btn-default">UPDATE</button>
			</div>
			<input type="hidden" value="<%=user.getuId()%>" name = "userId">
			</form>
		</div>
	</div>
</div>


<script>
	$('#modalgenuser').modal('show');
</script>