<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<center>
		<div class="LoginPage">
		<form method="post" action="Login">
			<label >Login</label> <br>
			<input class="Login" type="text" placeholder="Login" name="Login">
			<label >Password</label> <br>
			<input class="Password" type="password" placeholder="Password" name="Password"> <br> <br>
			<button class="SingIN" type="sumbit">Sign In</button>
		</form>
		</div>
	</center>
	<style>
		body {
		background: linear-gradient(to right, lightblue, pink);
		font-family: 'Monaco';
		}
		
		div.LoginPage{  
        width: 382px;  
        overflow: hidden;  
        max-width: 500px;
  		margin: auto; 
        padding: 80px;
        }
        
        label{
        	font-size:30px;
        }
        
		input.Login,input.Password,button.SingIN{
			width: 300px;  
   		 	height: 30px;  
    		border: none;  
    		border-radius: 35px;  
    		padding-left: 8px; 
		}

	</style>
</body>
</html>