<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<h1>
		<sec:authentication property="principal.member.name" />
		<%-- <sec:authentication property="principal.member.username"/>
		<sec:authentication property="principal.member.password"/>  --%>
		님 Welcome! <a href="/boot/member/logout">로그아웃</a> <br> 현재 권한 :
		<sec:authentication property="principal.member.role" />
		<br>

		<sec:authorize access="hasRole('ADMIN')">
			<button>관리자페이지</button>
		</sec:authorize>
		<sec:authorize access="hasRole('MANAGER')">
			<button>매니저페이지</button>
		</sec:authorize>
		<sec:authorize access="hasRole('MEMBER')">
			<button>일반유저</button>
		</sec:authorize>
	</h1>

</body>
</html>