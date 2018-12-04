<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page language="java" contentType="text/html"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Hashtable, java.util.ArrayList"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	String base = (String) application.getAttribute("base");
%>
<jsp:useBean id="dataManager" scope="application"
	class="eshop.model.DataManager" />
<div class="menu">
	<div class="box">
		<div class="title">Quick Search</div>
		<p>Book Title/Author:</p>
		<form style="border: 0px solid; padding: 0; margin: 0;">
			<input type="hidden" name="action" value="search" /> <input id="text"
				type="text" name="keyword" size="15" /> <input id="submit"
				type="submit" value="Search" />
		</form>
	</div>
	<div class="box">
		<div class="title">Categories</div>
		<%
			Hashtable<String, String> categories = dataManager.getCategories();
			Enumeration<String> categoryIds = categories.keys();
			while (categoryIds.hasMoreElements()) {
				Object categoryId = categoryIds.nextElement();
				out.println("<p><a href=" + base + "?action=selectCatalog&id=" + categoryId.toString() + ">"
						+ categories.get(categoryId) + "</a></p>");
			}
		%>
		<form action="ctrlXYZ">
			<input type="hidden" name="action" value="selectCatalog" />
			<select name="id">
				<c:set var="categories" value="${dataManager.getCategories()}" />
				<c:forEach items="${categories.keys()}" var="item" >
					<option value="${item.toString()}">${categories.get(item)}</option>
				</c:forEach>
			</select>
			<input type="submit" value="enviar" />
		</form>



	</div>
</div>
