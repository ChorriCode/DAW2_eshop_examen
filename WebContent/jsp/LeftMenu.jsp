<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page language="java" contentType="text/html"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Hashtable"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<% String base = (String)application.getAttribute("base"); %>
<jsp:useBean id="dataManager" scope="application"
  class="eshop.model.DataManager"/>
<div class="menu"> 
  <div class="box">
    <div class="title">Quick Search</div>
    <p>Book Title/Author:</p>
    <form style="border: 0px solid; padding: 0; margin: 0;">
      <input type="hidden" name="action" value="search"/>
      <input id="text" type="text" name="keyword" size="15"/>
      <input id="submit" type="submit" value="Search"/>
      </form>
    </div>
  <div class="box">
    <div class="title">Categories</div>
<%
    Hashtable<String, String> categories = dataManager.getCategories();
    Enumeration<String> categoryIds = categories.keys();

    while (categoryIds.hasMoreElements()) {
      Object categoryId = categoryIds.nextElement();
      out.println("<p><a href=" + base + "?action=selectCatalog&id="
        + categoryId.toString() + ">" + categories.get(categoryId) + "</a></p>"
        );
      }
  %>
  <select name="category" id="">
        <c:forEach items="categories" var="category">
        <p>${category}</p>
        <c:out value="category"/>
      		<option value="category">${category}</option>
   		</c:forEach>
  </select>

    </div>
  </div>
