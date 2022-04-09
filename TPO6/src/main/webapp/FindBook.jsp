<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<center>
	<h1>Enter The Author Or Title Of The Book</h1>
	<div class="FindForm">
		<form method = "post" action="FindBook">
			<input class="Title" type="text" name="Title" placeholder="Title" /> <br> <br>
			<input class="Author" type="text" name="Author" placeholder="Author" /> <br> <br>
			<input class="SearchB" type="submit" value="Szukaj" />
		</form>
	</div>
	</center>
	<br>
	<br>
	<button class="Back" onclick="location.href = 'Menu.jsp'">Back to Menu</button>
	<style>
		
		body {
		background: linear-gradient(to right, lightblue, pink);;
		font-family: 'Monaco';
		}
		
		h1{
			font-size:70px;
			color: yellow;
			font-family: 'Lucida Handwriting';
		}
		
		div.FindForm{  
        width: 382px;  
        overflow: hidden;  
        max-width: 500px;
  		margin: auto; 
        padding: 80px;
        }
        
        input.Title,input.Author,input.SearchB,button.Back{
       		width: 300px;  
   		 	height: 30px;  
    		border: none;  
    		border-radius: 35px;  
    		padding-left: 8px;
    		size:50;
        }
	</style>
</body>
</html>