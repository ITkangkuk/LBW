package db;

public class DbMain {

   public static void main(String[] args) {
      DbService dbService = new DbService();
      dbService.dbSelectWifiInfo();
   }
}
