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

    /*
    FR1: Rooms and Rates. When this option is selected, the system shall output a list of rooms
    to the user sorted by popularity (highest to lowest, see below for definition of “popularity”)
    Include in your output all columns from the rooms table, as well as the following:

    •Room popularity score: number of days the room has been occupied during the previous
    180 days divided by 180 (round to two decimal places)
    •Next available check-in date.
    •Length in days and check out date of the most recent (completed) stay in the room.
     */
    public static void FR1() {

    }

    /*
    FR2: Reservations. When this option is selected, your system shall accept from the user the
    following information:
    •First name
    •Last name
    •A room code to indicate the specific room desired (or “Any” to indicate no preference)
    •A desired bed type (or “Any” to indicate no preference)
    •Begin date of stay
    •End date of stay
    •Number of children
    •Number of adults
    With this information, the system shall produce a numbered list of available rooms, along with
    a prompt to allow booking by option number. If no exact matches are found, the system should
    suggest 5 possibilities for different rooms or dates. These 5 possibilities should be chosen based
    on similarity to the desired reservation (“similarity” defined as nearby dates, rooms with similar
    features or decor, or logic of your own choosing) For every option presented, maximum room
    occupancy must be considered and the dates must not overlap with another existing reservation.
    If the requested person count (children plus adults) exceeds the maximum capacity of any one
    room at the Inn, print a message indicating that no suitable rooms are available. To reserve a
    block of rooms, a user must submit multiple reservation requests.
    At the prompt, the user may decide to cancel the current request, which will return the user to
    the main menu. If the user chooses to book one of the room options presented, they will enter
    the option number at the prompt. After a choice is made, provide the user with a confirmation
    screen that displays the following:
    •First name, last name
    •Room code, room name, bed type
    •Begin and end date of stay
    •Number of adults
    •Number of children
    •Total cost of stay, based on a sum of the following:
    – Number of weekdays multiplied by room base rate
    – Number of weekend days multiplied by 110% of the room base rate

    Allow the user to cancel, returning to the main menu, or confirm, which will finalize their
    reservation and create an entry in the lab7 reservations table. The Rate column should
    contain the computed nightly rate (ie. total cost of stay divided by number of nights, rounded
    to the nearest penny)
     */
    public static void FR2() {

    }

    /*
    FR3: Reservation Change. Allow the user to make changes to an existing reservation.
    Accept from the user a reservation code and new values for any of the following:
    •First name
    •Last name
    •Begin date
    •End date
    •Number of children
    •Number of adults
    Allow the user to provide a new value or to indicate “no change” for a given field. Update
    the reservation based on any new information provided. If the user requests different begin
    and/or end dates, make sure to check whether the new begin and end dates conflict with another
    reservation in the system.
     */
    public static void FR3() {

    }

    /*
    FR4: Reservation Cancellation. Allow the user to cancel an existing reservation. Accept
    from the user a reservation code, confirm the cancellation, then remove the reservation record
    from the database.
     */
    public static void FR4() {

    }

    /*
    FR5: Detailed Reservation Information. Present a search prompt or form that allows the
    user to enter any combination of the fields listed below (a blank entry should indicate “Any”).
    For all fields except dates, permit partial values using SQL LIKE wildcards (for example: GL%
    should be allowed as a last name search value)
    •First name
    •Last name
    •A range of dates
    •Room code
    •Reservation code
    Using the search information provided, display a list of all matching reservations found in the
    database. The list shall show the contents of every attribute from the lab7 reservations table
    (as well as the full name of the room, and any extra information about the room you wish to
    add).
     */
    public static void FR5() {

    }

    /*
    FR6: Revenue. When this option is selected, your system shall provide a month-by-month
    overview of revenue for the current calendar year, based on SQL‘s CURRENT DATE variable.
    For the purpose of this assignment, revenue from a reservation should be recognized in the month
    and year when the reservation ends. For example a reservations that begins on October 30th,
    2022 and ends on November 2nd, 2022 would be considered November 2022 revenue.
    Your system shall display a list of rooms, and, for each room, 13 columns: 12 columns showing
    dollar revenue for each month and a 13th column to display total yearly revenue for the room.
    There shall also be a “totals” row in the table, which contains column totals. All amounts
    should be rounded to the nearest whole dollar.
     */
    public static void FR6() {

    }

}
