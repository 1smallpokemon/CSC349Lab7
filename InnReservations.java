import java.sql.*;
import java.util.Scanner;

// Classpaths
// java -cp mysql-connector-java-8.0.16.jar:. InnReservations 

public class InnReservations {

    public static void main(String[] args) {
        InnReservations reservations = new InnReservations();
        try {
            reservations.demo2();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        Scanner sc = new Scanner(System.in);
        boolean empty = false;

        menu:
        while (true) {
            if (!empty) {
                System.out.println("Please enter one of the following options");
                System.out.println("0 -- Quit");
                System.out.println("1 -- List Rooms");
                System.out.println("2 -- Search Rooms");
                System.out.println("3 -- Change Reservation");
                System.out.println("4 -- Cancel Reservation");
                System.out.println("5 -- View Reservation");
                System.out.println("6 -- Revenue");
                System.out.print(">>> ");
            }
            String userInput = sc.nextLine();
            switch (userInput) {
                case "0":
                    empty = false;
                    break menu;
                case "1":
                    empty = false;
                    FR1();
                    break;
                case "2":
                    empty = false;
                    FR2();
                    break;
                case "3":
                    empty = false;
                    FR3();
                    break;
                case "4":
                    empty = false;
                    FR4();
                    break;
                case "5":
                    empty = false;
                    FR5();
                    break;
                case "6":
                    empty = false;
                    FR6();
                    break;
                case "":
                    empty = true;
                    System.out.print(">>> ");
                    break;
                default:
                    System.out.println("Invalid option -- Exiting");
                    break menu;
            }
        }


    }

    public static Connection establishConnection() throws SQLException {
        return DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
                System.getenv("HP_JDBC_USER"),
                System.getenv("HP_JDBC_PW"));
    }

    /*
    FR1: Rooms and Rates. When this option is selected, the system shall output a list of rooms
    to the user sorted by popularity (highest to lowest, see below for definition of â€œpopularityâ€�)
    Include in your output all columns from the rooms table, as well as the following:

    Room popularity score: number of days the room has been occupied during the previous
    180 days divided by 180 (round to two decimal places)
    Next available check-in date.
    Length in days and check out date of the most recent (completed) stay in the room.
     */
    //TODO: HARIS KHAN
    public static void FR1() {

    }

    //TODO: JOE
    public static void FR2() {

    }

    //TODO: ISHAN
    /**
     * Allow the user to make changes to an existing reservation.
     * Accept from the user a reservation code and new values for any of the
     * following:
     * • First name
     * • Last name
     * • Begin date
     * • End date
     * • Number of children
     * • Number of adults
     * Allow the user to provide a new value or to indicate “no change” for a given
     * field. Update
     * the reservation based on any new information provided. If the user requests
     * different begin
     * and/or end dates, make sure to check whether the new begin and end dates
     * conflict with another
     * reservation in the system.
     */
    public void FR3() {
        // Create menu for user to select which field to change
        String option;
        boolean empty = false;
        Scanner sc = new Scanner(System.in);
        
        while (!empty){
            System.out.println("Please enter the reservation code of the reservation you would like to change");
            System.out.print(">>> ");
            int reservationCode = sc.nextInt();
            // ensure reservation code exists
            String reservation=getReservationCode(reservationCode);
            // if it does, then display menu
            System.out.println("Please enter the field you would like to change");
            System.out.println("1 -- First Name");
            System.out.println("2 -- Last Name");
            System.out.println("3 -- Begin Date");
            System.out.println("4 -- End Date");
            System.out.println("5 -- Number of Children");
            System.out.println("6 -- Number of Adults");
            System.out.print(">>> ");
            option = sc.nextLine();
        }
        
    }
    /**
     * Retrieve reservation information for a specified reservation code
     * Meant for validation of existance of reservation code for FR3
     */
    private String getReservationCode(int reservationCode) {
        try 
    }

    /*
    FR2: Reservations. When this option is selected, your system shall accept from the user the
    following information:
    First name
    Last name
    A room code to indicate the specific room desired (or â€œAnyâ€� to indicate no preference)
    A desired bed type (or â€œAnyâ€� to indicate no preference)
    Begin date of stay
    End date of stay
    Number of children
    Number of adults
    With this information, the system shall produce a numbered list of available rooms, along with
    a prompt to allow booking by option number. If no exact matches are found, the system should
    suggest 5 possibilities for different rooms or dates. These 5 possibilities should be chosen based
    on similarity to the desired reservation (similarity defined as nearby dates, rooms with similar
    features or decor, or logic of your own choosing) For every option presented, maximum room
    occupancy must be considered and the dates must not overlap with another existing reservation.
    If the requested person count (children plus adults) exceeds the maximum capacity of any one
    room at the Inn, print a message indicating that no suitable rooms are available. To reserve a
    block of rooms, a user must submit multiple reservation requests.
    At the prompt, the user may decide to cancel the current request, which will return the user to
    the main menu. If the user chooses to book one of the room options presented, they will enter
    the option number at the prompt. After a choice is made, provide the user with a confirmation
    screen that displays the following:
    First name, last name
    Room code, room name, bed type
    Begin and end date of stay
    Number of adults
    Number of children
    Total cost of stay, based on a sum of the following:
    Number of weekdays multiplied by room base rate
    Number of weekend days multiplied by 110% of the room base rate

    Allow the user to cancel, returning to the main menu, or confirm, which will finalize their
    reservation and create an entry in the lab7 reservations table. The Rate column should
    contain the computed nightly rate (ie. total cost of stay divided by number of nights, rounded
    to the nearest penny)
     */

    /*
    FR4: Reservation Cancellation. Allow the user to cancel an existing reservation. Accept
    from the user a reservation code, confirm the cancellation, then remove the reservation record
    from the database.
     */
    //TODO: ALEX - complete
    public static void FR4() {
        try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
                System.getenv("HP_JDBC_USER"),
                System.getenv("HP_JDBC_PW"))) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("\nEnter a reservation code to cancel\n>>> ");
            int reservationCode = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Are you sure you want to cancel your reservation? (Y/N)\n>>> ");
            String confirmCancel = scanner.nextLine();

            if (confirmCancel.equalsIgnoreCase("y")) {
                try (PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM lab7_reservations WHERE Code = ?")) {
                    preparedStatement.setInt(1, reservationCode);
                    preparedStatement.execute();
                    System.out.println("Reservation " + reservationCode + " has been cancelled.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    conn.rollback();
                    System.out.println("Reservation " + reservationCode + " does not exist.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    FR3: Reservation Change. Allow the user to make changes to an existing reservation.
    Accept from the user a reservation code and new values for any of the following:
    First name
    Last name
    Begin date
    End date
    Number of children
    Number of adults
    Allow the user to provide a new value or to indicate â€œno changeâ€� for a given field. Update
    the reservation based on any new information provided. If the user requests different begin
    and/or end dates, make sure to check whether the new begin and end dates conflict with another
    reservation in the system.
     */

    /*
    FR5: Detailed Reservation Information. Present a search prompt or form that allows the
    user to enter any combination of the fields listed below (a blank entry should indicate â€œAnyâ€�).
    For all fields except dates, permit partial values using SQL LIKE wildcards (for example: GL%
    should be allowed as a last name search value)
    First name
    Last name
    A range of dates
    Room code
    Reservation code
    Using the search information provided, display a list of all matching reservations found in the
    database. The list shall show the contents of every attribute from the lab7 reservations table
    (as well as the full name of the room, and any extra information about the room you wish to
    add).
     */
    //TODO: ALEX - complete
    public static void FR5() {

        try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
                System.getenv("HP_JDBC_USER"),
                System.getenv("HP_JDBC_PW"))) {
            StringBuilder firstNameFormatted = new StringBuilder();
            StringBuilder lastNameFormatted = new StringBuilder();
            StringBuilder roomCodeFormatted = new StringBuilder();
            StringBuilder reservationCodeFormatted = new StringBuilder();
            firstNameFormatted.append("%");
            lastNameFormatted.append("%");
            roomCodeFormatted.append("%");
            reservationCodeFormatted.append("%");
            String firstName = "";
            String lastName = "";
            String startDate = "";
            String endDate = "";
            String roomCode = "";
            String reservationCode = "";

            Scanner scanner = new Scanner(System.in);

            System.out.print("\nEnter your first name\n>>> ");
            firstName = scanner.nextLine();
            firstNameFormatted.append(firstName);
            firstNameFormatted.append("%");

            System.out.print("\nEnter your last name\n>>> ");
            lastName = scanner.nextLine();
            lastNameFormatted.append(lastName);
            lastNameFormatted.append("%");

            System.out.print("\nEnter start date (YYYY-MM-DD)\n>>> ");
            startDate = scanner.nextLine();

            System.out.print("\nEnter end date (YYYY-MM-DD)\n>>> ");
            endDate = scanner.nextLine();

            System.out.print("\nEnter room code\n>>> ");
            roomCode = scanner.nextLine();
            roomCodeFormatted.append(roomCode);
            roomCodeFormatted.append("%");

            System.out.print("\nEnter reservation code\n>>> ");
            reservationCode = scanner.nextLine();
            reservationCodeFormatted.append(reservationCode);
            reservationCodeFormatted.append("%");

            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM lab7_reservations WHERE FirstName like ? and LastName like ? and CheckIn >= ?  and Checkout <= ? and Room like ? and CODE like ?")) {
                preparedStatement.setString(1, firstNameFormatted.toString());
                preparedStatement.setString(2, lastNameFormatted.toString());
                preparedStatement.setString(3, startDate);
                preparedStatement.setString(4, endDate);
                preparedStatement.setString(5, roomCodeFormatted.toString());
                preparedStatement.setString(6, reservationCodeFormatted.toString());
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    System.out.println("-----");
                    System.out.println("Reservation:");
                    System.out.println("Code: " + rs.getString("CODE"));
                    System.out.println("Room: " + rs.getString("Room"));
                    System.out.println("Check In: " + rs.getString("CheckIn"));
                    System.out.println("Check Out: " + rs.getString("Checkout"));
                    System.out.println("Rate: " + rs.getString("Rate"));
                    System.out.println("Last Name: " + rs.getString("LastName"));
                    System.out.println("First Name: " + rs.getString("FirstName"));
                    System.out.println("Adults: " + rs.getString("Adults"));
                    System.out.println("Kids: " + rs.getString("Kids"));
                    System.out.println("-----");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback();
                System.out.println("Reservation " + reservationCode + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    FR6: Revenue. When this option is selected, your system shall provide a month-by-month
    overview of revenue for the current calendar year, based on SQLâ€˜s CURRENT DATE variable.
    For the purpose of this assignment, revenue from a reservation should be recognized in the month
    and year when the reservation ends. For example a reservations that begins on October 30th,
    2022 and ends on November 2nd, 2022 would be considered November 2022 revenue.
    Your system shall display a list of rooms, and, for each room, 13 columns: 12 columns showing
    dollar revenue for each month and a 13th column to display total yearly revenue for the room.
    There shall also be a totals row in the table, which contains column totals. All amounts
    should be rounded to the nearest whole dollar.
     */
    public static void FR6() {

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
