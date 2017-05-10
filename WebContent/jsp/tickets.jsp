
<%@page import="service.TicketDetails"%>
<%@page import="model.LoginSignup"%>
<style>
.tree {
	min-height: 20px;
	padding: 19px;
	margin-bottom: 20px;
	background-color: #fbfbfb;
	border: 1px solid #999;
	-webkit-border-radius: 4px;
	-moz-border-radius: 4px;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
	-moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.05)
}

.tree li {
	list-style-type: none;
	margin: 0;
	padding: 10px 5px 0 5px;
	position: relative;
	margin-left: -18px;
}

.tree li::before,.tree li::after {
	content: '';
	left: -20px;
	position: absolute;
	right: auto
}

.tree li::before {
	border-left: 1px solid #999;
	bottom: 50px;
	height: 100%;
	top: 0;
	width: 1px
}

.tree li::after {
	border-top: 1px solid #999;
	height: 20px;
	top: 25px;
	width: 25px
}

.tree li span {
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border: 1px solid #999;
	border-radius: 5px;
	display: inline-block;
	padding: 3px 8px;
	text-decoration: none
}

.tree li.parent_li>span {
	cursor: pointer
}

.tree>ul>li::before,.tree>ul>li::after {
	border: 0
}

.tree li:last-child::before {
	height: 30px
}

.tree li.parent_li>span:hover,.tree li.parent_li>span:hover+ul li span {
	background: #eee;
	border: 1px solid #94a0b4;
	color: #000
}
</style>
<script>
	$('.tree-toggle').click(function() {
		$(this).parent().children('ul.tree1').toggle(500);
	});
	$(function() {
		$('.tree-toggle').parent().children('ul.tree1').toggle(500);
	});
</script>
<script>
		$('#myTable').find('tr').click( function(){
			  document.getElementById("radbtn").innerHTML="ADD";
			  document.getElementById("esbtn").innerHTML = "ADD";
			  document.getElementById("crbtn").innerHTML = "ADD";
			  var row=$(this).index()+1;
			  document.getElementById("tNo").innerHTML= document.getElementById("myTable").rows[row].cells.namedItem("tno").innerHTML;
			  document.getElementById("rem").innerHTML= document.getElementById("myTable").rows[row].cells.namedItem("remarks").innerHTML;
			  document.getElementById("changer").innerHTML= document.getElementById("myTable").rows[row].cells.namedItem("B/P_mng").innerHTML;
			  document.getElementById("radStatus").innerHTML= document.getElementById("myTable").rows[row].cells.namedItem("radstatus").innerHTML;
			  document.getElementById("esStatus").innerHTML= document.getElementById("myTable").rows[row].cells.namedItem("esstatus").innerHTML;
			  document.getElementById("crStatus").innerHTML= document.getElementById("myTable").rows[row].cells.namedItem("crstatus").innerHTML;
			  if(document.getElementById("radStatus").innerHTML == "Submitted")
			  {
				  document.getElementById("radbtn").innerHTML = "UPDATE";
			  }
			  if(document.getElementById("esStatus").innerHTML == "Submitted")
			  {
				  document.getElementById("esbtn").innerHTML = "UPDATE";
			  }
			  if(document.getElementById("crStatus").innerHTML == "Submitted")
			  {
				  document.getElementById("crbtn").innerHTML = "UPDATE";
			  }
			  
			});
	
	function remarksId(button) {
		//var e = window.event, btn = e.target || e.srcElement;
		document.getElementById("t_id").value = button.id;
		document.getElementById("btnValue").value = button.value;
	}
	
	function submitRemarks() {
		if (document.getElementById("remarks").value == undefined
				|| document.getElementById("remarks").value == ""
				|| document.getElementById("remarks").value == "undefined") {
			if (document.getElementById("btnValue").value == "Precise") {
				swal("","You Have Found It Precise....Please Enter Remarks... :) ");
				return;
			} else {
				swal("","You Have Found It Biased....Please Enter Remarks... :) ");
				return;
			}
		}

		if(document.getElementById("remarks").value.length > 250)
		{
			swal("","Remarks limit exceeded!");
			return;
		}
		
		document.getElementById("remarksForm").submit();
	}
	
	function addUpdateDoc(doc)
	{
		var tNum = document.getElementById("tNo").innerHTML;
		var tStatus = doc.innerHTML;
		var btn = doc.id;
		$('#docAlterModal').modal('show');
		document.getElementById("doctype").value = btn;
		document.getElementById("docstatus").value = tStatus;
		document.getElementById("t_no").value = tNum;
		
	}
</script>
<%
	LoginSignup user = (LoginSignup) session.getAttribute("user");
	TicketDetails details = new TicketDetails();
	out.print(details.getTicketDetails(user.getuId()));
%>

<form action="../UpdateTicketDetails" id="remarksForm" method="post">
	<input type="hidden" id="t_id" name="t_id"> 
	<input type="hidden" id="btnValue" name="btnValue">
	<input type="hidden" value="<%LoginSignup loginSignup = (LoginSignup)session.getAttribute("user");
	out.print(loginSignup.getuId());
	%>" name="user_id">
	<div id="myModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">Add Remarks</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<input type="text" class="form-control" id="remarks"
							name="remarks" maxlength="250">
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary"
						onclick="submitRemarks()">Change Status</button>
				</div>
			</div>
		</div>
	</div>
</form>

<div id="myModal1" class="modal fade">

	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title">DETAILS</h4>
			</div>
			<div class="modal-body">
  					<div class="panel-group">
    					<div class="panel panel-primary">
					      	<div class="panel-heading">Ticket Number</div>
      						<div class="panel-body"><p id="tNo"></p></div>
    					</div>
    					<div class="panel panel-primary">
					      	<div class="panel-heading">Verified By</div>
      						<div class="panel-body"><p id="changer"></p></div>
    					</div>
    					<div class="panel panel-primary">
					      	<div class="panel-heading">Remarks</div>
      						<div class="panel-body"><p id="rem"></p></div>
    					</div>
    					<div class="panel panel-primary">
					      	<div class="panel-heading">RAD Status</div>
      						<div class="panel-body"><p id="radStatus"></p>
      							<div align="right"><button type="button" class="btn btn-default" id="radbtn" onclick="addUpdateDoc(this)"></button></div>
      						</div>
    					</div>
    					<div class="panel panel-primary">
					      	<div class="panel-heading">Estimation Doc Status</div>
      						<div class="panel-body"><p id="esStatus"></p>
      							<div align="right"><button type="button" class="btn btn-default" id="esbtn" onclick="addUpdateDoc(this)"></button></div>
      						</div>
    					</div>
    					<div class="panel panel-primary">
					      	<div class="panel-heading">Change Request Doc Status</div>
      						<div class="panel-body"><p id="crStatus"></p>
      							<div align="right"><button type="button" class="btn btn-default" id="crbtn" onclick="addUpdateDoc(this)"></button></div>
      						</div>
    					</div>
  					</div>
			</div>
			<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
		</div>
	</div>
</div>

<form action="../AddTicketDocumentHandler" method="post" enctype="multipart/form-data">
<div id="docAlterModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">SELECT DOCUMENT</h4>
				</div>
				<input type="hidden" name="doctype" id="doctype" />
				<input type="hidden" name="btnid" id="docstatus" />
				<input type="hidden" name="t_no" id="t_no" />
				<div class="modal-body">
					<div class="form-group">
						<input name="docToAlter" type="file" id="docToAlter"/>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-primary">Submit</button>
				</div>
			</div>
		</div>
	</div>
</form>
<!-- <div class="tree well" style="overflow: auto; height: 597px;">
	<ul>
		<li><span class="tree-toggle"> Parent</span>//biswa
			<ul class="tree1">
				<li><span class="tree-toggle"> Child</span>//madan
					<ul class="tree1">
						<li><span class="tree-toggle"> Grand Child</span>
							<ul class="tree1">
								<li>
									<div class="table-responsive">
										<table class="table" style="margin-top: 20px;">
											<thead>
												<tr>
													<th>#</th>
													<th>TICKET NO.</th>
													<th>DESCRIPTION</th>
													<th>SUB DATE</th>
													<th>STATUS</th>
													<th>REMARKS</th>
													<th>SUB TO</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td>1</td>
													<td>Ticket #123232</td>
													<td>john@example.com</td>
													<td>sds</td>
													<td>sdsds</td>
													<td>
														<form role="form">
															<div class="form-group">
																<textarea class="form-control" rows="1" id="comment"></textarea>
															</div>
														</form>
													</td>
												</tr>
											</tbody>
										</table>
									</div>
								</li>
							</ul></li>
					</ul></li>

			</ul></li>
		<li><span class="tree-toggle"> Parent2</span>
			<ul class="tree1">
				<li><span> Child</span></li>
			</ul></li>
	</ul>
</div> -->

<!-- <div class="table-responsive">
 <table class="table" style="margin-top: 20px;">
    <thead>
      <tr>
        <th>S NO.</th>
        <th>TICKET NO.</th>
        <th>DESCRIPTION</th>
        <th>SUBMISSION DATE</th>
        <th>STATUS</th>
        <th>REMARKS</th>
         <th>SUBMITTED TO</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>1</td>
        <td>Ticket #123232</td>
        <td>john@example.com</td>
        <td>sds</td>
        <td>sdsds</td>
        <td>  <form role="form">
    <div class="form-group">
      <textarea class="form-control" rows="1" id="comment"></textarea>
    </div>
  </form>
</td>
      </tr>
    </tbody>
  </table>
  </div> -->