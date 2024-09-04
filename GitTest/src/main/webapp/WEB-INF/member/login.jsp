<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<link rel = "stylesheet" href="/boot/resources/css/style.css">
</head>
<body>
	<div class="wrapper fadeInDown">
		<div id="formContent">
			<!-- Tabs Titles -->

			<!-- Icon -->
			<div class="fadeIn first">
				<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ17Yx5Plqg2BkNRVW27nbBx9dpwKK5ockuGw&s" id="icon"
					alt="User Icon" />
			</div>

			<!-- Login Form -->
			<form action="/boot/member/login" method="post">
				<input type="text" id="login" class="fadeIn second" name="username"placeholder="login"> 
				<input type="text" id="password"class="fadeIn third" name="password" placeholder="password"> 
				<input type="submit" class="fadeIn fourth" value="Log In">
			</form>

			<!-- Remind Passowrd -->
			<div id="formFooter">
				<a class="underlineHover" href="#">Forgot Password?</a>
			</div>

		</div>
	</div>
</body>
</html>