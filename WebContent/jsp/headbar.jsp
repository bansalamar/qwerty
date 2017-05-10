
<%@page import="model.LoginSignup"%>
<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                        
      </button>
      <a class="navbar-brand" href="#" id="site-name"><b><i>ISO TRACKER</i></b></a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#home">HOME</a></li>
        <li><a href="#about">TOOL</a></li>
          <li><a href="#contact">CONTACT US</a></li>
          <%
          LoginSignup user = (LoginSignup)session.getAttribute("user");
          if(user == null){ %>
        <li><a href="jsp/login.jsp">SIGN-UP /LOGIN</a></li>
        <%}else { %>
        <li><a href="jsp/userDetails.jsp"><%= user.getuFName()+" "+user.getuLName()%></a></li>
        <%} %>
      </ul>
    </div>
  </div>
</nav>