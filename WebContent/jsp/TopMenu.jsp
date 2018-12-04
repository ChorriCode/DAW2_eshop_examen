<%@page language="java" contentType="text/html"%>
<%@ page import="java.util.*, eshop.beans.* "%>
<%
  String base = (String)application.getAttribute("base");
  String imageURL = (String)application.getAttribute("imageURL");
  %>
<div class="header">
  <div class="logo">
    <p>Examen Java </p>
  </div>
  <form action="" method="post">
  	<input type="hidden" name="action" value="login"/>
  	user:
  	<input type="text" name="user" />
  	password:
  	<input type="password" name="password" />
  	<input type="submit" value="login" />
  </form>
   <p style="text-align:center">${error}</p>
  <div class="cart">
  
	<% 
	
	Hashtable<String, CartItem> shoppingCart = (Hashtable<String, CartItem>) session.getAttribute("carrito");
	if (!shoppingCart.isEmpty()) {%>
    <a class="link2" href="<%=base%>?action=showCart">Show Cart
      <img src="<%=imageURL%>cart.gif" border="0"/></a>
    </div>
    <%} %>
  </div>