package db;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DbService {
  
	public String dbSelectWifiInfo() {
	
	    String count = "";
	    String url = "http://openapi.seoul.go.kr:8088/4d5056536f6461763535585977616a/json/TbPublicWifiInfo/1/5/";
	    
	    OkHttpClient client = new OkHttpClient();
	    
	    Request.Builder builder = new Request.Builder().url(url).get();
	    Request request = builder.build();
	    
	    try {
	      Response response = client.newCall(request).execute();
	      if (response.isSuccessful()) {
	        ResponseBody body = response.body();
	        if (body != null) {
	          String bodyStr = body.string();
	          //String to JSON
	          JsonParser parser = new JsonParser();
	          JsonObject jobj = (JsonObject)parser.parse(bodyStr);
	          JsonObject jobj_list = jobj.getAsJsonObject("TbPublicWifiInfo");
	          
	          //total count
	          count = jobj_list.get("list_total_count").toString();
	          insertEveryData(Integer.valueOf(count));
	        } 
	      } 
	    } catch (IOException e) {
	      e.printStackTrace();
	    } 
	    return count;
  }
  
  public void insertEveryData(Integer cnt) {
    Integer roll1 = Integer.valueOf((int)Math.floor((cnt.intValue() / 1000)));
    Integer roll2 = Integer.valueOf(cnt.intValue() % 1000);
    System.out.println("roll1" + roll1);
    System.out.println("roll2" + roll2);
    
    dbInsertWifiInfo(roll1, roll2);
  }
  
  
  public void dbInsertWifiInfo(Integer c1, Integer c2) {
		
	  //한번에 가져올 수 있는 개수가 1000개로 제한 되어있어 1000개 단위로 for문 돌려 insert
	  for(Integer j = 0; j <= c1 ; j++) {
		  Integer start_num = 1 + j * 1000;
		  Integer end_num = j == c1 ? start_num + c2 : j * 1000 + 1000;
		  
		  String url = "http://openapi.seoul.go.kr:8088/4d5056536f6461763535585977616a/json/TbPublicWifiInfo/"+start_num+"/"+end_num+"/";
	    
		  OkHttpClient client = new OkHttpClient();
		    
		  Request.Builder builder = new Request.Builder().url(url).get();
		  Request request = builder.build();
	    
		  try {
			  Response response = client.newCall(request).execute();
			  
			  if (response.isSuccessful()) {
				  ResponseBody body = response.body();
				  if (body != null) {
					  String bodyStr = body.string();
					  //String to JSON
					  JsonParser parser = new JsonParser();
					  JsonObject jobj = (JsonObject)parser.parse(bodyStr);
					  JsonObject jobj_list = jobj.getAsJsonObject("TbPublicWifiInfo");
					  
					  JsonArray row_list = (JsonArray) jobj_list.get("row");
					  
					  System.out.println("row_list"+row_list.size());
					  System.out.println("=============");
	          
					  String sql = "INSERT INTO WIFI_INFO"
					  		+ "(MGR_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, FLOOR, INSTL_TY, INSTL_MBY, SVC_SE, CMCWR, CNSTC_YEAR, INOUT_DOOR, REMARS3, LAT, LNT, WORK_DTTM)"
					  		+ "VALUES";
					  
					  for(Integer k = 0; k < row_list.size(); k ++) {
						  sql += "(";
						  JsonObject row_detail = (JsonObject) row_list.get(k);
						  
						  sql += row_detail.get("X_SWIFI_MGR_NO") + ",";
						  sql += row_detail.get("X_SWIFI_WRDOFC") + ",";
						  sql += row_detail.get("X_SWIFI_MAIN_NM") + ",";
						  sql += row_detail.get("X_SWIFI_ADRES1") + ",";
						  sql += row_detail.get("X_SWIFI_ADRES2") + ",";
						  sql += row_detail.get("X_SWIFI_INSTL_FLOOR") + ",";
						  sql += row_detail.get("X_SWIFI_INSTL_TY") + ",";
						  sql += row_detail.get("X_SWIFI_INSTL_MBY") + ",";
						  sql += row_detail.get("X_SWIFI_SVC_SE") + ",";
						  sql += row_detail.get("X_SWIFI_CMCWR") + ",";
						  sql += row_detail.get("X_SWIFI_CNSTC_YEAR") + ",";
						  sql += row_detail.get("X_SWIFI_INOUT_DOOR") + ",";
						  sql += row_detail.get("X_SWIFI_REMARS3") + ",";
						  sql += row_detail.get("LAT") + ",";
						  sql += row_detail.get("LNT") + ",";
						  sql += row_detail.get("WORK_DTTM");
						  
						  sql += ")";
						  
						  if(k != row_list.size() -1) {
							  sql += ",";
						  }
					  }
					  
					  dbInsert(sql);
					  
					  //total count
//					  insertEveryData(Integer.valueOf(count));
				  } 
			  } 
		  } catch (IOException var11) {
			  var11.printStackTrace();
		  } 
	}
  }
  
  public static void dbInsert(String sql) {
    String url = "jdbc:mariadb://localhost:3306/lbw";
    String dbUserId = "root";
    String dbPassword = "mariadb";
    try {
      Class.forName("org.mariadb.jdbc.Driver");
    } catch (ClassNotFoundException var28) {
      var28.printStackTrace();
    } 
    Connection connection = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    try {
      connection = DriverManager.getConnection(url, dbUserId, dbPassword);
//      String sql = "select *from zerobase_memberwhere type = ?";
      stmt = connection.createStatement();
      rs = stmt.executeQuery(sql);
//      preparedStatement.setString(1, memberTypeValue);
//      int affected = stmt.executeUpdate();
//      if (affected > 0) {
//        System.out.println("");
//      } else {
//        System.out.println("");
//      } 
//      rs = preparedStatement.executeQuery(sql);
//      while (rs.next()) {
//        String memberType = rs.getString("name");
//        String memberPassword = rs.getString("password");
//        System.out.println(String.valueOf(memberType) + "," + memberPassword);
//      } 
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (!rs.isClosed())
          rs.close(); 
      } catch (SQLException e1) {
        e1.printStackTrace();
      } 
      try {
        if (!stmt.isClosed())
        	stmt.close(); 
      } catch (SQLException e2) {
        e2.printStackTrace();
      } 
      try {
        if (!connection.isClosed())
          connection.close(); 
      } catch (SQLException e3) {
        e3.printStackTrace();
      } 
    } 
  }
  
  public List<DbBean> dbSelect(String lat, String lnt) {
	  
	List<DbBean> list = new ArrayList<>();
	
    String url = "jdbc:mariadb://localhost:3306/lbw";
    String dbUserId = "root";
    String dbPassword = "mariadb";
    
    try {
      Class.forName("org.mariadb.jdbc.Driver");
    } catch (ClassNotFoundException var27) {
      var27.printStackTrace();
    } 
    
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
//    Integer LAT_ = Integer.valueOf(lat);
//    Integer LNT_ = Integer.valueOf(lnt);
    
    try {
      connection = DriverManager.getConnection(url, dbUserId, dbPassword);
      String sql = "select CAST(ROUND(SQRT(ABS(LAT - ?) + ABS(LNT - ?)),4) as CHAR) as DISTANCE"
      		+ "      		, MGR_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, FLOOR, INSTL_TY, INSTL_MBY, SVC_SE, CMCWR, CNSTC_YEAR, INOUT_DOOR, REMARS3, LAT, LNT, WORK_DTTM"
      		+ "      		from WIFI_INFO"
      		+ "      		ORDER BY DISTANCE"
      		+ "      		limit 100;";
//    		  "select SQRT(ABS(LAT - ?) + ABS(LNT - ?)) as DISTANCE"
//      		+ ", MGR_NO, WRDOFC, MAIN_NM, ADRES1, ADRES2, FLOOR, INSTL_TY, INSTL_MBY, SVC_SE, CMCWR, CNSTC_YEAR, INOUT_DOOR, REMARS3, LAT, LNT, WORK_DTTM"
//      		+ "from WIFI_INFO"
//      		+ "order by DISTANCE";
      
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, lat);
      preparedStatement.setString(2, lnt);
      
      rs = preparedStatement.executeQuery();
      while (rs.next()) {
    	    String X_SWIFI_MGR_NO = rs.getString("MGR_NO");
    	    String X_SWIFI_WRDOFC = rs.getString("WRDOFC");
    	    String X_SWIFI_MAIN_NM = rs.getString("MAIN_NM");
    	    String X_SWIFI_ADRES1 = rs.getString("ADRES1");
    	    String X_SWIFI_ADRES2 = rs.getString("ADRES2");
    	    String X_SWIFI_INSTL_FLOOR = rs.getString("FLOOR");
    	    String X_SWIFI_INSTL_TY = rs.getString("INSTL_TY");
    	    String X_SWIFI_INSTL_MBY = rs.getString("INSTL_MBY");
    	    String X_SWIFI_SVC_SE = rs.getString("SVC_SE");
    	    String X_SWIFI_CMCWR = rs.getString("CMCWR");
    	    String X_SWIFI_CNSTC_YEAR = rs.getString("CNSTC_YEAR");
    	    String X_SWIFI_INOUT_DOOR = rs.getString("INOUT_DOOR");
    	    String X_SWIFI_REMARS3 = rs.getString("REMARS3");
    	    String LAT = rs.getString("LAT");
    	    String LNT = rs.getString("LNT");
    	    String WORK_DTTM = rs.getString("WORK_DTTM");
    	    String DISTANCE = rs.getString("DISTANCE");
    	    
    	    DbBean bean = new DbBean();
    	    bean.setX_SWIFI_MGR_NO(X_SWIFI_MGR_NO);
    	    bean.setX_SWIFI_WRDOFC(X_SWIFI_WRDOFC);
    	    bean.setX_SWIFI_MAIN_NM(X_SWIFI_MAIN_NM);
    	    bean.setX_SWIFI_ADRES1(X_SWIFI_ADRES1);
    	    bean.setX_SWIFI_ADRES2(X_SWIFI_ADRES2);
    	    bean.setX_SWIFI_INSTL_FLOOR(X_SWIFI_INSTL_FLOOR);
    	    bean.setX_SWIFI_INSTL_TY(X_SWIFI_INSTL_TY);
    	    bean.setX_SWIFI_INSTL_MBY(X_SWIFI_INSTL_MBY);
    	    bean.setX_SWIFI_SVC_SE(X_SWIFI_SVC_SE);
    	    bean.setX_SWIFI_CMCWR(X_SWIFI_CMCWR);
    	    bean.setX_SWIFI_CNSTC_YEAR(X_SWIFI_CNSTC_YEAR);
    	    bean.setX_SWIFI_INOUT_DOOR(X_SWIFI_INOUT_DOOR);
    	    bean.setX_SWIFI_REMARS3(X_SWIFI_REMARS3);
    	    bean.setLAT(LAT);
    	    bean.setLNT(LNT);
    	    bean.setWORK_DTTM(WORK_DTTM);
    	    bean.setDISTANCE(DISTANCE);

    	    list.add(bean);
    	    
      } 
      
      //히스토리 저장
      historyInsert(lat,lnt);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (!rs.isClosed())
          rs.close(); 
      } catch (SQLException e1) {
        e1.printStackTrace();
      } 
      try {
        if (!preparedStatement.isClosed())
          preparedStatement.close(); 
      } catch (SQLException e2) {
        e2.printStackTrace();
      } 
      try {
        if (!connection.isClosed())
          connection.close(); 
      } catch (SQLException e3) {
        e3.printStackTrace();
      } 
    } 
    return list;
  }
  
  public void historyInsert(String lat, String lnt) {
	    String url = "jdbc:mariadb://localhost:3306/lbw";
	    String dbUserId = "root";
	    String dbPassword = "mariadb";
	    try {
	      Class.forName("org.mariadb.jdbc.Driver");
	    } catch (ClassNotFoundException var28) {
	      var28.printStackTrace();
	    } 
	    Connection connection = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	      connection = DriverManager.getConnection(url, dbUserId, dbPassword);
	      String sql = "INSERT INTO LOCATION_HIST"
	      		+ "(LAT, LNT, `DATE`)"
	      		+ "VALUES("+lat+", "+lnt+", now());";
	      stmt = connection.createStatement();
	      rs = stmt.executeQuery(sql);
	      
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    } finally {
	      try {
	        if (!rs.isClosed())
	          rs.close(); 
	      } catch (SQLException e1) {
	        e1.printStackTrace();
	      } 
	      try {
	        if (!stmt.isClosed())
	        	stmt.close(); 
	      } catch (SQLException e2) {
	        e2.printStackTrace();
	      } 
	      try {
	        if (!connection.isClosed())
	          connection.close(); 
	      } catch (SQLException e3) {
	        e3.printStackTrace();
	      } 
	    } 
	  }
  
  public List<DbBean> historySelect() {
	  
		List<DbBean> list = new ArrayList<>();
		
	    String url = "jdbc:mariadb://localhost:3306/lbw";
	    String dbUserId = "root";
	    String dbPassword = "mariadb";
	    
	    try {
	      Class.forName("org.mariadb.jdbc.Driver");
	    } catch (ClassNotFoundException ce) {
	      ce.printStackTrace();
	    } 
	    
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    
	    try {
	      connection = DriverManager.getConnection(url, dbUserId, dbPassword);
	      String sql = "SELECT * FROM LOCATION_HIST ORDER BY `DATE` DESC";
	      
	      preparedStatement = connection.prepareStatement(sql);
	      
	      rs = preparedStatement.executeQuery();
	      while (rs.next()) {
	    	    String ID = rs.getString("ID");
	    	    String LAT = rs.getString("LAT");
	    	    String LNT = rs.getString("LNT");
	    	    String DATE = rs.getString("DATE");
	    	    
	    	    DbBean bean = new DbBean();
	    	    bean.setID(ID);
	    	    bean.setLAT(LAT);
	    	    bean.setLNT(LNT);
	    	    bean.setDATE(DATE);

	    	    list.add(bean);
	      } 
	      
	    } catch (SQLException e) {
	      e.printStackTrace();
	    } finally {
	      try {
	        if (!rs.isClosed())
	          rs.close(); 
	      } catch (SQLException e1) {
	        e1.printStackTrace();
	      } 
	      try {
	        if (!preparedStatement.isClosed())
	          preparedStatement.close(); 
	      } catch (SQLException e2) {
	        e2.printStackTrace();
	      } 
	      try {
	        if (!connection.isClosed())
	          connection.close(); 
	      } catch (SQLException e3) {
	        e3.printStackTrace();
	      } 
	    } 
	    return list;
	  }
  
  public void dbDeleteHist(String id) {
	    String url = "jdbc:mariadb://localhost:3306/lbw";
	    String dbUserId = "root";
	    String dbPassword = "mariadb";
	    try {
	      Class.forName("org.mariadb.jdbc.Driver");
	    } catch (ClassNotFoundException var28) {
	      var28.printStackTrace();
	    } 
	    Connection connection = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	      connection = DriverManager.getConnection(url, dbUserId, dbPassword);
	      String sql = "DELETE FROM LOCATION_HIST WHERE ID="+id;
	      stmt = connection.createStatement();
	      rs = stmt.executeQuery(sql);
	      
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    } finally {
	      try {
	        if (!rs.isClosed())
	          rs.close(); 
	      } catch (SQLException e1) {
	        e1.printStackTrace();
	      } 
	      try {
	        if (!stmt.isClosed())
	        	stmt.close(); 
	      } catch (SQLException e2) {
	        e2.printStackTrace();
	      } 
	      try {
	        if (!connection.isClosed())
	          connection.close(); 
	      } catch (SQLException e3) {
	        e3.printStackTrace();
	      } 
	    } 
	  }
}
