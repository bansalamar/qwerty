<%@page import="model.LoginSignup"%>
<nav class="navbar navbar-default navbar-fixed-top">

<%
LoginSignup user = (LoginSignup)session.getAttribute("user");
String name =user.getuFName()+" "+user.getuLName(); 
%>
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                        
      </button>
      <a class="navbar-brand" href="#" id="user-name">
      <b><i>
      <%=name %>
      </i></b>
      </a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right">
        <li><a href="../index.jsp">HOME</a></li>
        <li id="editpro"><a href="#"
						onclick="loadPage('editprofile.jsp','editpro')">EDIT DETAILS</a></li>
        <li><a href="../jsp/logout.jsp">LOGOUT</a></li>
      </ul>
    </div>
  </div>
</nav>