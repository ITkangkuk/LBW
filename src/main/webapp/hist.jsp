<%@ page import="db.DbService" %>
<%@ page import="db.DbBean" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>와이파이 정보 구하기</title>
</head>
<style>
th { background-color: #00ae69; color: white; padding: 5px 10px; border-color: white; border-width: 1px; }
td { text-align: center; height: 50px; font-weight: bold;}
table {width: 100%;}
</style>

<script>

function delHist(id_){
	location.href = "/delete.jsp?id="+id_;
}

</script>

<body>

	<%
		DbService dbService = new DbService();
		List<DbBean> list = dbService.historySelect();
	%>

	<h1>위치 히스토리 목록</h1>
	
	<a href="/">홈</a> | <a href="/hist.jsp">위치 히스토리 목록</a> | <a href="/insert.jsp">Open API 와이파이 정보 가져오기</a>
	
	<br><br>
	
	<table>
		<colgroup>
			<col style="width:5%;"/>
			<col style="width:25%;"/>
			<col style="width:25%;"/>
			<col style="width:35%;"/>
			<col style="width:10%;"/>
		</colgroup>
		<tr>
			<th>ID</th> 	<!-- 1 -->
			<th>X좌표</th>	<!-- 2 -->
			<th>Y좌표</th>	<!-- 3 -->
			<th>조회일자</th>	<!-- 4 -->
			<th>비고</th>		<!-- 5 -->
		</tr>
		<%
		for(DbBean dbBean : list){
		%>
		<tr>
			<td> <%=dbBean.getID()%> </td>
			<td> <%=dbBean.getLAT()%> </td>
			<td> <%=dbBean.getLNT()%> </td>
			<td> <%=dbBean.getDATE()%> </td>
			<td> 
				<button onclick="delHist('<%=dbBean.getID()%>')">삭제</button>
			</td>
		</tr>
		<%
			}
		%>
	
	</table>
	
</body>
</html>