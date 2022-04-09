<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<h1>Book List</h1>
	<center>
		<%
		ResultSet resultSet = (ResultSet) request.getAttribute("resultSet");
		if (resultSet.next() == false) {
		%>
		<h2>!No Books!</h2>
		<%
		} else {
		%>
		<table class="Table" border=1>
			<tr>
				<th>IdBook</th>
				<th>Title</th>
				<th>Price</th>
				<th>Author</th>
				<th>Description</th>
				<th>Availability</th>
			</tr>
			<%
			resultSet.beforeFirst();
			while (resultSet.next()) {
			%>
			<tr>
				<td><%=resultSet.getInt("IdBook")%></td>
				<td><%=resultSet.getString("BookName")%></td>
				<td><%=resultSet.getFloat("Price")%></td>
				<td><%=resultSet.getString("Author")%></td>
				<td><%=resultSet.getString("Description")%></td>
				<td><%=resultSet.getInt("Availability")%></td>
			</tr>
			<%} } resultSet.close();%>
		</table>
	</center>
	<br>
	<button class="Menu" onclick="location.href = 'Menu.jsp'">Back to Menu</button>
	<style>
	
	body{
		background: linear-gradient(to right, lightblue, pink);
	}
	
	table.Table {
		border-collapse: collapse;
		border-color: white;
	}
	
	th {
		background: #9932CC;
	}
	
	td, th {
		text-align: center;
	}
	
	h1{
		font-family: 'Lucida Handwriting';
		font-size:70px;
		color: yellow;	
	}
	
	h2{
		font-family: 'Lucida Handwriting';
		font-size:100px;
		color: red;	
	}
	
	button.Menu{
			width: 300px;  
   		 	height: 30px;  
    		border: none;  
    		border-radius: 35px;  
    		padding-left: 8px; 
	}
	
	</style>
</body>
</html>