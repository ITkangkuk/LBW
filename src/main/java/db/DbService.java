package db;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DbService {
   public DbService() {
   }

   public String dbInsertWifiInfo() {
      String count = "";
      OkHttpClient client = (new OkHttpClient.Builder()).build();
      String url = "http://openapi.seoul.go.kr:8088/4d5056536f6461763535585977616a/json/TbPublicWifiInfo/1/5/";
      Request request = (new Request.Builder()).url(url).addHeader("Content-Type", "application/json").get().build();

      try {
         Response response = client.newCall(request).execute();
         if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
               String bodyStr = body.string();
               JsonParser parser = new JsonParser();
               JsonObject jobj = (JsonObject)parser.parse(bodyStr);
               JsonObject jobj_list = jobj.getAsJsonObject("TbPublicWifiInfo");
               count = jobj_list.get("list_total_count").toString();
               this.insertEveryData(Integer.parseInt(count));
            }
         }
      } catch (IOException var11) {
         var11.printStackTrace();
      }

      return count;
   }

   public void insertEveryData(Integer cnt) {
      Integer roll1 = (int)Math.floor((double)(cnt / 1000));
      Integer roll2 = cnt % 1000;
      System.out.println("roll1" + roll1);
      System.out.println("roll2" + roll2);
   }

   public static void dbInsert() {
      String url = "jdbc:mariadb://localhost:3306/zero_db";
      String dbUserId = "root";
      String dbPassword = "mariadb";

      try {
         Class.forName("org.mariadb.jdbc.Driver");
      } catch (ClassNotFoundException var28) {
         var28.printStackTrace();
      }

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      ResultSet rs = null;
      String memberTypeValue = "email";

      try {
         connection = DriverManager.getConnection(url, dbUserId, dbPassword);
         String sql = "select *from zerobase_memberwhere type = ?";
         preparedStatement = connection.prepareStatement(sql);
         preparedStatement.setString(1, memberTypeValue);
         int affected = preparedStatement.executeUpdate();
         if (affected > 0) {
            System.out.println("저장 성공");
         } else {
            System.out.println("저장 실패");
         }

         rs = preparedStatement.executeQuery(sql);

         while(rs.next()) {
            String memberType = rs.getString("name");
            String memberPassword = rs.getString("password");
            System.out.println(memberType + "," + memberPassword);
         }
      } catch (SQLException var29) {
         var29.printStackTrace();
      } finally {
         try {
            if (!rs.isClosed()) {
               rs.close();
            }
         } catch (SQLException var27) {
            var27.printStackTrace();
         }

         try {
            if (!preparedStatement.isClosed()) {
               preparedStatement.close();
            }
         } catch (SQLException var26) {
            var26.printStackTrace();
         }

         try {
            if (!connection.isClosed()) {
               connection.close();
            }
         } catch (SQLException var25) {
            var25.printStackTrace();
         }

      }

   }

   public static void dbSelect() {
      String url = "jdbc:mariadb://localhost:3306/zero_db";
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
      String memberTypeValue = "email";

      try {
         connection = DriverManager.getConnection(url, dbUserId, dbPassword);
         String sql = "select *from zerobase_memberwhere type = ?";
         preparedStatement = connection.prepareStatement(sql);
         preparedStatement.setString(1, memberTypeValue);
         rs = preparedStatement.executeQuery(sql);

         while(rs.next()) {
            String memberType = rs.getString("name");
            String memberPassword = rs.getString("password");
            System.out.println(memberType + "," + memberPassword);
         }
      } catch (SQLException var28) {
         var28.printStackTrace();
      } finally {
         try {
            if (!rs.isClosed()) {
               rs.close();
            }
         } catch (SQLException var26) {
            var26.printStackTrace();
         }

         try {
            if (!preparedStatement.isClosed()) {
               preparedStatement.close();
            }
         } catch (SQLException var25) {
            var25.printStackTrace();
         }

         try {
            if (!connection.isClosed()) {
               connection.close();
            }
         } catch (SQLException var24) {
            var24.printStackTrace();
         }

      }

   }
}
