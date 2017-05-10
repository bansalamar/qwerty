<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="shortcut icon" href="../images/favi.ico" type="image/x-icon">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Add chairman</title>
</head>
<body>
	<div class="container">
		<h2>ADD CHAIRMAN</h2>
		<form action="../AddChairman">
			<div class="form-group">
				<label for="email">Email:</label> <input type="email"
					class="form-control" name="email">
			</div>
			<div class="form-group">
				<label>First Name:</label> <input type="text" class="form-control"
					name="fname">
			</div>
			<div class="form-group">
				<label>Last Name:</label> <input type="text" class="form-control"
					name="lname">
			</div>
			<div class="form-group">
				<label>Password:</label> <input type="password" class="form-control"
					name="pass">
			</div>
			<div class="form-group">
				<label>Security Question:</label> <input type="text"
					class="form-control" name="quest" value="What's your nick name?"
					readonly="readonly">
			</div>
			<div class="form-group">
				<label>Answer:</label> <input type="text" class="form-control"
					name="answ">
			</div>
			<button type="submit" class="btn btn-default">Submit</button>
		</form>
	</div>

</body>
</html>