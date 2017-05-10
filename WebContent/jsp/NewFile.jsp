<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class='tree well' style='overflow: auto; height: 597px;'>
		<ul>
			<li><span class='tree-toggle'>amar bansal</span><span>D</span>
			<ul class='tree1'>
					<li><div class='table-responsive'>
							<table class='table' style='margin-top: 20px;'>
								<thead>
									<tr>
										<th>#</th>
										<th>TICKET NO.</th>
										<th>DESCRIPTION</th>
										<th>SUB DATE</th>
										<th>STATUS</th>
										<th>SUB TO</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<tr class='alert alert-info'>
										<td>1</td>
										<td><a href='../jsp/userDetails.jsp?t_Num=T_1'>T_1</a></td>
										<td>webqot</td>
										<td>2016-10-12</td>
										<td>2</td>
										<td>Rajat Julka</td>
										<td><input type='button' class='btn btn-primary active'
											data-toggle='modal' data-target='#myModal' value='Biased'
											onclick='remarksId(this)' id='T_1'></input></td>
										<td><input type='button' class='btn btn-primary active'
											data-toggle='modal' data-target='#myModal' value='Precise'
											onclick='remarksId(this)' id='T_1'></input></td>
									</tr>
								</tbody>
							</table>
						</div></li>
				</ul></li>
			<li><span class='tree-toggle'>Biswa Dutta</span>
			<ul class='tree1'></ul></li>
		</ul>
	</div>
</body>
</html>