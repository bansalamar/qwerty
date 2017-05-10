<%@page import="service.Misc"%>
<%if(session.getAttribute("user") != null)
{
	response.sendRedirect("../jsp/userDetails.jsp");	
}else{ %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
	<link rel="shortcut icon" href="../images/favi.ico" type="image/x-icon">
   	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>LOGIN/SIGN-UP TRACKER</title>
    <link href='http://fonts.googleapis.com/css?family=Titillium+Web:400,300,600' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  	<link href="http://fonts.googleapis.com/css?family=Lato" rel="stylesheet" type="text/css">
  	<link href="http://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="../css/normalize.css">
    <link rel="stylesheet" href="../css/style.css">
    <script src="../dist/sweetalert.min.js"></script> 
	<link rel="stylesheet" type="text/css" href="../dist/sweetalert.css">
	
    <script>
    	var x = Math.floor((Math.random() * 10) + 1);    
     </script>
    <script type="text/javascript">
    function pass(){
    <%
	  if(session.getAttribute("pass") != null)
	  {
	  	String encPass = (String)session.getAttribute("pass");
	  	if(encPass.equalsIgnoreCase("no data Found!"))
	  	{%>
	  	  swal("No Data Found...Confirm Your Answer!","");	
	  	<%}
	  	else
	  	{
	  	%>
		  swal("Your's Password Is: <%=encPass%>","");  
		<%
		}
	  }
   	  session.setAttribute("pass",null);
	  %>
    }
      function  subornot() {
    	 if(document.getElementById("submit_button").innerHTML != "GET STARTED")
         {
    		var quest="";
    		if(x>0 && x<=3){
    			quest = "Whats Your Middle Name?";
    		}else if(x>3 && x<=6){
    			quest = "Whats Your Favourite Food?";
    		}else if(x>5 && x<=8){
    			quest = "Whats Your Birth Place?";    			
    		}else{
    			quest = "Whats Your Lucky Number?";
    		} 
    		document.getElementById("userQuestion").value = quest;
    	    document.getElementById("submit_button").innerHTML = "GET STARTED";
    	 }
    	 else
    	 {
    		 if(document.getElementById("userQuestion_Ans").value == "" ||document.getElementById("userQuestion_Ans").value == null || document.getElementById("userQuestion_Ans").value == undefined)
    		 {
    			 document.getElementById("submit_button").innerHTML = "ADD ANSWER";
    			 return;
    		 }
    		 if(document.getElementById("userQuestion").readOnly == false){
    			 swal("Don't be Over Smart!","");
    			 window.location.reload();
    			 return;
    		 }
    		 
    		 if(document.getElementById("userFName").value == "" || document.getElementById("userLName").value == "" || 
    		     document.getElementById("userEmailId").value == "" || document.getElementById("userPassword").value == "")   
    		 {
    			 swal("Enter Required Details!","");
    			 return;
    		 }
    		 document.getElementById("formLogin").submit();
    		 document.getElementById("submit_button").disabled=true;
    	}
      }
      </script>
  </head>
  <body onload="pass()">
  <%out.print(session.getAttribute("queryMsg"));
 	if(session.getAttribute("AccID")!=null)
 		out.print(session.getAttribute("AccID"));
  %>
  <%if(session.getAttribute("queryMsg")!=null && session.getAttribute("AccID")==null){ %>
  <div class="container-fluid" style="text-align: center;color:red;margin-top: 20px;">
  <p style="font-size: 16px;"><%=session.getAttribute("queryMsg") %></p>
  <%} %>
   </div>
  <%if(session.getAttribute("queryMsg")!=null && session.getAttribute("AccID")!=null){ %>
  <div class="container-fluid" style="text-align: center; color:#32CD32;margin-top: 20px;">
 <p style="font-size: 16px;"><%=session.getAttribute("queryMsg")%></p>
  <p style="font-size: 16px;"><%=session.getAttribute("AccID") %></p>
   </div>
  <%}
  session.setAttribute("queryMsg", null);
  session.setAttribute("AccID", null);
  %>
 
  <div id="error"></div>
    <div class="form">
      <ul class="tab-group">
        <li class="tab active"><a href="#signup">Sign Up</a></li>
        <li class="tab"><a href="#login">Log In</a></li>
      </ul>
      <div class="tab-content">
        <div id="signup">   
          <h1>Sign Up for Free</h1>
          <form action="../LoginSignupHandler" id="formLogin" method="post">
          <div class="top-row">
            <div class="field-wrap">
              <label style="font-weight: normal;">
                First Name<span class="req">*</span>
              </label>
              <input type="text" required autocomplete="off" name="userFName" id="userFName"/>
            </div>
            <div class="field-wrap">
              <label style="font-weight: normal;">
                Last Name<span class="req">*</span>
              </label>
              <input type="text" required autocomplete="off" name="userLName" id="userLName"/>
            </div>
          </div>
          <div class="field-wrap">
            <label style="font-weight: normal;">
              Email Address<span class="req">*</span>
            </label>
            <input type="email" required autocomplete="off" name="userEmailId" id="userEmailId"/>
          </div>
          <div class="field-wrap">
            <label style="font-weight: normal;">
              Set A Password<span class="req">*</span>
            </label>
            <input type="password" required autocomplete="off" name="userPassword" id="userPassword"/>
          </div>
     	 
          <div id="questans" class="collapse">
			<div class="field-wrap">
			<input type="text" id="userQuestion" 
					 name="userQuestion" readonly="readonly"/>
			</div>
			<div class="field-wrap">
			 <label style="font-weight: normal; color: white">
             Answer<span class="req">*</span>
            </label>
				<input type="text" id="userQuestion_Ans"
					 name="userQuestion_Ans"/>
			</div>
		</div>
          <button type="button" class="button button-block" name="LogOn" data-toggle="collapse" 
          data-target="#questans" id="submit_button" onclick="subornot()">ADD SECURITY QUESTION</button>
          </form>
        </div>
        <div id="login">   
          <h1>Welcome Back!</h1>
          <form action="../LoginSignupHandler" method="post">
            <div class="field-wrap">
            <label style="font-weight: normal;">
              User ID<span class="req">*</span>
            </label>
            <input type="text"required autocomplete="off" name="uId"/>
          </div>
          <div class="field-wrap">
            <label style="font-weight: normal;">
              Password<span class="req">*</span>
            </label>
            <input type="password"required autocomplete="off" name="uPassword"/>
          </div>
          <p class="forgot"><a href="#" data-target="#pwdModal" data-toggle="modal">Forgot Password?</a></p>
          <button class="button button-block" name="LogIn" type="submit">Log In</button>
          </form>
        </div>
      </div><!-- tab-content -->
</div> <!-- /form -->
<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
<script src="../js/index.js"></script>

<!-- /////////////////////// -->
<!--modal-->
<script>  
var request=new XMLHttpRequest();  
function getQuestion()
{
	var emailId = document.getElementById("email").value;
		var url = "../jsp/getquestion_subPro.jsp?emailId=" + emailId;

		try {
			request.onreadystatechange = function() {
				if (request.readyState == 4) {
					var val = request.responseText;
					if (val.trim() == "Exception") {
						swal("Some Exception Occurs", "");
					} else {
						document.getElementById('quest').value = val;
						document.getElementById('quest_ans1').disabled = false;
						document.getElementById('forgotbutton').disabled = false;
					}
				}
			}//end of function  
			request.open("GET", url, true);
			request.send();
		} catch (e) {
			alert("Unable to connect to server");
		}
	}
</script>  
<div id="pwdModal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
  <div class="modal-content">
      <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h1 class="text-center" style="color: black;margin-top: 50px;">What's My Password?</h1>
      </div>
      <div class="modal-body">
          <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="text-center">
                          
                          <p>Forgotten your password?Don't Worry! We are here to help</p>
                            <div class="panel-body">
                            <form action="../ForgotPasswordHandler" id="forgotForm" method="post">
                                <fieldset>
                                    <div class="form-group">
                                        <input class="form-control input-lg" placeholder="E-mail Address" name="email" id="email" type="email">
                                    </div>
                                    <input class="btn btn-lg btn-primary btn-block" value="My Question Please" type="button" onclick="getQuestion()">
                                     <div class="form-group"></div>
                                      <div class="form-group">
                                        <input class="form-control input-lg" placeholder="Security Question" name="quest_ans" id="quest" type="text" disabled="disabled">
                                    </div>
                                    <div class="form-group">
                                        <input class="form-control input-lg" placeholder="Answer" name="quest_ans1" id="quest_ans1" type="text" disabled="disabled">
                                    </div>
                                    <input class="btn btn-lg btn-primary btn-block" value="My Password Please" type="submit" disabled="disabled" id="forgotbutton">
                                </fieldset>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
      </div>
      <div class="modal-footer">
          <div class="col-md-12">
          <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
		  </div>	
      </div>
  </div>
  </div>
</div>
<!-- /////////////////////// -->
    
  </body>
</html>
<%}%>