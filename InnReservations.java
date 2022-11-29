import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

// Classpaths



public class InnReservations {

  public static void main(String[] args) {
    InnReservations reservations = new InnReservations();
    try {
      reservations.demo2();
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage());
    }
    


      

   
  }

    // Demo2 - Establish JDBC connection, execute SELECT query, read & print result
    public void demo2() throws SQLException {

      System.out.println("demo2: List content of hp_goods table\r\n");
      printEnv();
      
      // Step 1: Establish connection to RDBMS
      try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
          System.getenv("HP_JDBC_USER"),
          System.getenv("HP_JDBC_PW"))) {
        // Step 2: Construct SQL statement
        String sql = "SELECT * FROM lab7_rooms LIMIT 1";

        // Step 3: (omitted in this example) Start transaction

        // Step 4: Send SQL statement to DBMS
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

          // Step 5: Receive results
          while (rs.next()) {
            //RoomCode	RoomName	Beds	bedType	maxOcc	basePrice	decor
            String RoomCode = rs.getString("RoomCode");
            String RoomName = rs.getString("RoomName");
            int Beds = rs.getInt("Beds");
            String bedType = rs.getString("bedType");
            int maxOcc = rs.getInt("maxOcc");
            double basePrice = rs.getDouble("basePrice");
            String decor = rs.getString("decor");
            
            System.out.print(RoomCode);
            System.out.print("\t");
            System.out.print(RoomName);
            System.out.print("\t");
            System.out.print(Beds);
            System.out.print("\t");
            System.out.print(bedType);
            System.out.print("\t");
            System.out.print(maxOcc);
            System.out.print("\t");
            System.out.print(basePrice);
            System.out.print("\t");
            System.out.print(decor);
            System.out.println();
          }
        }

        // Step 6: (omitted in this example) Commit or rollback transaction
      }
      // Step 7: Close connection (handled by try-with-resources syntax)
    }

    public void printEnv() {
      System.out.println(System.getenv("HP_JDBC_URL"));
      System.out.println(System.getenv("HP_JDBC_USER"));
      System.out.println(System.getenv("HP_JDBC_PW"));
    }
    
}



