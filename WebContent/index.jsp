<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="shortcut icon" href="images/favi.ico" type="image/x-icon">
<%@include file="/jsp/script.jsp"%>
<link rel="stylesheet" type="text/css" href="css/mainPage.css">
<title>ISO TRACKER</title>
<style>
.person {

	border: 10px solid transparent;
	border-color: #f1f1f1;
}
</style>
</head>
<body data-spy="scroll" data-target=".navbar" data-offset="50">

	<%@include file="/jsp/headbar.jsp"%>
	<div class="container-fluid" id="home"
		style="padding-left: 0; padding-right: 0;">
		<%@include file="/jsp/slider.jsp"%>
	</div>
	<div class="container-fluid">
		<div id="about" class="container text-center">
			<br />
			<br />
			<br />
			<h3>
				<b>ISO TRACKER</b>
			</h3>
			<p>
				<em>You will Love it!</em>
			</p>
			<br /><br />
			<h4>
				An User-Friendly tool to operate.<br /><br />
				All your ISO Docs related work under Single Unit.<br /><br />
				This tool will help you tracking your tickets/SCR's ISO Documents.<br /><br />
				Tickets/SCR's Documents will directly gets uploaded to Google Drive.<br /><br />
				Reporting Manager can change the status of Ticket Documents after reviewing.<br /><br />
			</h4>
			
		</div>
	</div>
	<div class="container-fluid">
		<div id="contact" class="container text-center">
			<div class="row">
				<br /> <br />
				<br />
				<div class="col-md-6">
					<br />
					<p class="text-center">
						<strong></strong>
					</p>
					<br> <a href="#" data-toggle="collapse"> <img
						src="images/Amar.jpg" class="img-circle person" alt="Error">
					</a>
					<div id="demo">
						<strong>Amardeep Singh</strong>
						<p>amardeep.singh@trantorinc.com</p>
					</div>
				</div>
				<div class="col-md-6">
					<br />
					<p class="text-center">
						<strong></strong>
					</p>
					<br> <a href="#" data-toggle="collapse"> <img
						src="images/Bhanu.jpg" class="img-circle person" alt="Error">
					</a>
					<div id="demo2">
						<strong>Bhanu Priya</strong>
						<p>bhanu.priya@trantorinc.com</p>
					</div>
				</div>
			</div>
		</div>
		<div style="background-color: white;">
			<p align="center">
				<b>ISO Tracker&nbsp;&copy&nbsp;2016</b>
			</p>
		</div>
	</div>
</body>
</html>