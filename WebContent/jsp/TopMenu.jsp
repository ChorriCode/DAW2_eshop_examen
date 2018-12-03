<%@page language="java" contentType="text/html"%>

<%
  String base = (String)application.getAttribute("base");
  String imageURL = (String)application.getAttribute("imageURL");
  %>
<div class="header">
  <div class="logo">
    <p>Examen Java </p>
  </div>
  <form action="ctrlXYZ" method="post">
  	user:
  	<input type="text" name="user" />
  	password:
  	<input type="password" name="password" />
  	<input type="submit" value="login" />
  </form>
      
  <div class="cart">
  

    <a class="link2" href="<%=base%>?action=showCart">Show Cart
      <img src="<%=imageURL%>cart.gif" border="0"/></a>
    </div>
    
  </div>