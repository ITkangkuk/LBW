<%@ page import="db.DbService" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		DbService dbService = new DbService();
		String totalCount = dbService.dbInsertWifiInfo();
	%>

	<h1>현재 데이터를 
	<%=totalCount%>
	개 가져왔습니다.</h1>
	

</body>
</html>