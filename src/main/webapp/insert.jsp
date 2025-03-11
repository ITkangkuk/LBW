<%@ page import="db.DbService" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
</head>
<style>
	.center-text { justify-self: center; }
</style>
<body>
	<%
		//DbService dbService = new DbService();
		//String totalCount = dbService.dbSelectWifiInfo();
	%>

	<h1 class="center-text">
<%-- 	<%=totalCount%> --%>
	개의 WIFI 정보를 정상적으로 저장하였습니다.
	</h1>
	
	<h4 class="center-text"><a href="/">홈 으로 가기</a></h4>
	

</body>
</html>