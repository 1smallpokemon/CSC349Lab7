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

public class HastyPastry {
  public static void main(String[] args) {
    try {
      HastyPastry hp = new HastyPastry();
      int demoNum = Integer.parseInt(args[0]);
      /* 
      switch (demoNum) {
        case 1:
          hp.demo1();
          break;
        case 2:
          hp.demo2();
          break;
        case 3:
          hp.demo3();
          break;
        case 4:
          hp.demo4();
          break;
        case 5:
          hp.demo5();
          break;
      }

    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
    } catch (Exception e2) {
      System.err.println("Exception: " + e2.getMessage());
    }
    */
  }
}