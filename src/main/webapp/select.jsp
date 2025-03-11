<%@ page import="db.DbService" %>
<%@ page import="db.DbBean" %>
<%@ page import="java.util.List" %>
<%@ page import="okhttp3.Request" %>
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
td { text-align: center; height: 50px; font-weight: bold; border: solid; border-color: lightgray;}
table {width: 100%;}
</style>

<script src="http://code.jquery.com/jquery-latest.js"></script> 
<script>

	function clickLoc(){
		//내 위치 가져오기
		$("#lat").val(37.5544069);
		$("#lnt").val(126.8998666);
	}

</script>

<body>

	<% 
		String lat = request.getParameter("lat");
		String lnt = request.getParameter("lnt");
	
		DbService dbService = new DbService();
		List<DbBean> list = dbService.dbSelect(lat,lnt);
	%>

	<h1>와이파이 정보 구하기</h1>
	
	<a href="/">홈</a> | <a href="/hist.jsp">위치 히스토리 목록</a> | <a href="/insert.jsp">Open API 와이파이 정보 가져오기</a>
	
	<br><br>

	LAT: <input type="number" width="200px" id="lat"/>, LNT: <input type="number" width="200px" id="lnt"/> <button onclick="clickLoc()">내 위치 가져오기</button> <button>근처 WIFI 정보 보기</button>
	
	<br><br>
	
	<table>
		<!-- <colgroup>
			<col style="width:5%;"/>
			<col style="width:6%;"/>
			<col style="width:3%;"/>
			<col style="width:7%;"/>
			<col style="width:7%;"/>
			<col style="width:15%;"/>
			<col style="width:3%;"/>
			<col style="width:6%;"/>
			<col style="width:6%;"/>
			<col style="width:5%;"/>
			<col style="width:5%;"/>
			<col style="width:5%;"/>
			<col style="width:5%;"/>
			<col style="width:5%;"/>
			<col style="width:5%;"/>
			<col style="width:5%;"/>
			<col style="width:7%;"/>
		</colgroup> -->
		<tr>
			<th>거리(Km)</th> 	<!-- 1 -->
			<th>관리번호</th>		<!-- 2 -->
			<th>자치구</th>		<!-- 3 -->
			<th>와이파이명</th>	<!-- 4 -->
			<th>도로명주소</th>	<!-- 5 -->
			<th>상세주소</th>		<!-- 6 -->
			<th>설치위치(층)</th>	<!-- 7 -->
			<th>설치유형</th>		<!-- 8 -->
			<th>설치기관</th>		<!-- 9 -->
			<th>서비스구분</th>	<!-- 10 -->
			<th>망종류</th>		<!-- 11 -->
			<th>설치년도</th>		<!-- 12 -->
			<th>실내외구분</th>	<!-- 13 -->
			<th>WIFI접속환경</th>	<!-- 14 -->
			<th>X좌표</th>		<!-- 15 -->
			<th>Y좌표</th>		<!-- 16 -->
			<th>작업일자</th>		<!-- 17 -->
		</tr>
		
		<%
			for(DbBean dbBean : list){
		%>
		<tr>
			<td> <%=dbBean.getDISTANCE()%> </td>
			<td> <%=dbBean.getX_SWIFI_MGR_NO()%> </td>
			<td> <%=dbBean.getX_SWIFI_WRDOFC()%> </td>
			<td> <%=dbBean.getX_SWIFI_MAIN_NM()%> </td>
			<td> <%=dbBean.getX_SWIFI_ADRES1()%> </td>
			<td> <%=dbBean.getX_SWIFI_ADRES2()%> </td>
			<td> <%=dbBean.getX_SWIFI_INSTL_FLOOR()%> </td>
			<td> <%=dbBean.getX_SWIFI_INSTL_TY()%> </td>
			<td> <%=dbBean.getX_SWIFI_INSTL_MBY()%> </td>
			<td> <%=dbBean.getX_SWIFI_SVC_SE()%> </td>
			<td> <%=dbBean.getX_SWIFI_CMCWR()%> </td>
			<td> <%=dbBean.getX_SWIFI_CNSTC_YEAR()%> </td>
			<td> <%=dbBean.getX_SWIFI_INOUT_DOOR()%> </td>
			<td> <%=dbBean.getX_SWIFI_REMARS3()%> </td>
			<td> <%=dbBean.getLAT()%> </td>
			<td> <%=dbBean.getLNT()%> </td>
			<td> <%=dbBean.getWORK_DTTM()%> </td>
		</tr>
		<%
			}
		%>
	
	</table>
	
</body>
</html>