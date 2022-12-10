import java.sql.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
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

		menu: while (true) {
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
				reservations.FR3();
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
		return DriverManager.getConnection(System.getenv("HP_JDBC_URL"), System.getenv("HP_JDBC_USER"),
				System.getenv("HP_JDBC_PW"));
	}

	/*
	 * FR1: Rooms and Rates. When this option is selected, the system shall output a
	 * list of rooms to the user sorted by popularity (highest to lowest, see below
	 * for definition of â€œpopularityâ€�) Include in your output all columns from
	 * the rooms table, as well as the following:
	 * 
	 * Room popularity score: number of days the room has been occupied during the
	 * previous 180 days divided by 180 (round to two decimal places) Next available
	 * check-in date. Length in days and check out date of the most recent
	 * (completed) stay in the room.
	 */
	// TODO: HARIS KHAN
	public static void FR1() {
		String query="WITH totalCheckInDays as (SELECT RoomName,SUM(CASE WHEN DATEDIFF(Checkout, CURDATE()-INTERVAL 180 day) < DATEDIFF(Checkout,Checkin) THEN DATEDIFF(Checkout, CURDATE()-INTERVAL 180 day) WHEN DATEDIFF(CheckOut,Checkin) > 180 then '180' ELSE DATEDIFF(CheckOut,Checkin) END) AS Total FROM lab7_rooms JOIN lab7_reservations ON lab7_reservations.Room = lab7_rooms.RoomCode GROUP BY RoomName),nextAvailableCheck AS (SELECT MAX(CheckOut) as lastCheckOut, RoomName FROM lab7_rooms JOIN lab7_reservations ON lab7_reservations.Room = lab7_rooms.RoomCode group by RoomName),mostRecentLength AS (SELECT RoomName, DATEDIFF(checkout, checkin) as totalStay FROM nextAvailableCheck, lab7_reservations WHERE lab7_reservations.Checkout = lastCheckOut) SELECT DISTINCT totalCheckInDays.RoomName AS RoomName, Round(Total/180, 2) as Popularity_Score, lastCheckOut as Next_Available_Date, totalStay as Most_Recent_Completed_Stay FROM totalCheckInDays,nextAvailableCheck, mostRecentLength WHERE totalCheckInDays.RoomName = nextAvailableCheck.RoomName AND nextAvailableCheck.RoomName = mostRecentLength.RoomName";
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"), System.getenv("HP_JDBC_USER"),
				System.getenv("HP_JDBC_PW"))) {
			try (PreparedStatement preparedStatement = conn
					.prepareStatement(query)) {
				ResultSet rs = preparedStatement.executeQuery();
				String [] fields={
						"RoomName",
						"Popularity_Score",
						"Next_Available_Date",
						"Most_Recent_Completed_Stay"
				};
				while (rs.next()) {
					for(int i=0;i<fields.length;i++){
						System.out.println(fields[i]+": "+rs.getString(fields[i]));
					}
					System.out.println("=================================");
				}

			} catch (SQLException e) {
				e.printStackTrace();
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



		// TODO: JOE
	public static void FR2() {
		// Hashmap to store user input
		HashMap<String, String> userInput = new HashMap<String, String>();
		Scanner sc = new Scanner(System.in);
		String[] fields = { "FirstName", "LastName", "RoomCode", "bedType", "CheckIn", "Checkout", "Kids", "Adults" };
		boolean similarDateFlag = false;
		// Take in user input for each field
		for (int i = 0; i < fields.length; i++) {
			System.out.println("Specify a value for " + fields[i]);
			System.out.print(">>> ");
			String newField = sc.nextLine();
			userInput.put(fields[i], newField);

		}

		if (Integer.parseInt(userInput.get("Adults")) + Integer.parseInt(userInput.get("Kids")) > maxOccOfInn()) {
			System.out.println(
					"No suitable rooms are available, exceeds max occupancy of inn. To reserve block of rooms submit multiple requests");
			return;
		}

		System.out.println("Searching for rooms...");
		// Search for rooms
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"), System.getenv("HP_JDBC_USER"),
				System.getenv("HP_JDBC_PW"))) {

			// Create the query
			String query = "SELECT DISTINCT lab7_rooms.* FROM lab7_rooms JOIN lab7_reservations ON RoomCode=Room WHERE RoomCode NOT IN (SELECT DISTINCT Room FROM lab7_reservations WHERE (Checkin <= ? AND Checkout > ?) OR (Checkin >= ? AND Checkout< ?) OR (Checkin <= ? AND Checkout >= ?))";
			int numVars = 6;

			// Append to the query if the user specified a value for a field
			if (!userInput.get("RoomCode").equals("Any")) {
				query += " AND RoomCode = ?";
				numVars++;
			}

			if (!userInput.get("Kids").equals("") && !userInput.get("Adults").equals("")) {
				query += " AND maxOcc = ? + ?";
				numVars += 2;
			} else if (!userInput.get("Kids").equals("")) {
				query += " AND maxOcc = ?";
				numVars++;
			} else if (!userInput.get("Adults").equals("")) {
				query += " AND maxOcc = ?";
				numVars++;
			}

			if (!userInput.get("bedType").equals("Any")) {
				query += " AND bedType = ?";
				numVars++;
			}

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				// Set the values for the query
				for (int i = 1; i <= 6; i += 2) {
					preparedStatement.setString(i, userInput.get("CheckIn"));
					preparedStatement.setString(i + 1, userInput.get("CheckOut"));
				}

				if (!userInput.get("bedType").equals("Any")) {
					preparedStatement.setString(numVars, userInput.get("bedType"));
					numVars--;
				}

				if (!userInput.get("Adults").equals("")) {
					preparedStatement.setString(numVars, userInput.get("Adults"));
					numVars--;
				}

				if (!userInput.get("Kids").equals("")) {
					preparedStatement.setString(numVars, userInput.get("Kids"));
					numVars--;
				}

				if (!userInput.get("RoomCode").equals("Any")) {
					preparedStatement.setString(numVars, userInput.get("RoomCode"));
					numVars--;
				}

				ResultSet rs = preparedStatement.executeQuery();
				ArrayList<HashMap<String, String>> reservations = new ArrayList<HashMap<String, String>>();
				while (rs.next()) {
					HashMap<String, String> reservation = new HashMap<String, String>();
					reservation.put("RoomCode", rs.getString("RoomCode"));
					reservation.put("RoomName", rs.getString("RoomName"));
					reservation.put("Beds", rs.getString("Beds"));
					reservation.put("bedType", rs.getString("bedType"));
					reservation.put("maxOcc", rs.getString("maxOcc"));
					reservation.put("basePrice", rs.getString("basePrice"));
					reservation.put("decor", rs.getString("decor"));
					reservations.add(reservation);

					System.out.print("Option " + reservations.size() + ": | ");
					System.out.print("Room Code: " + rs.getString("RoomCode") + " | ");
					System.out.print("Room Name: " + rs.getString("RoomName") + " | ");
					System.out.print("Beds: " + rs.getString("Beds") + " | ");
					System.out.print("Bed Type: " + rs.getString("bedType") + " | ");
					System.out.print("Maximum Occupancy: " + rs.getString("maxOcc") + " | ");
					System.out.print("Base Price: " + rs.getString("basePrice") + " | ");
					System.out.println("Decor: " + rs.getString("decor"));
				}
				// If the query returns no results return other results
				if (reservations.size() == 0) {
					System.out.print("No exact matches found...");
					querySimilarDateRange(userInput, reservations);
					similarDateFlag = true;

					System.out.println("No exact matches found...");
					System.out.println("Similar matches below:");
					for (int i = 0; i < 5; i++) {
						System.out.print("Option " + i + ": | ");
						System.out.print("Room Code: " + reservations.get(i).get("RoomCode") + " | ");
						System.out.print("Room Name: " + reservations.get(i).get("RoomName") + " | ");
						System.out.print("Beds: " + reservations.get(i).get("Beds") + " | ");
						System.out.print("Bed Type: " + reservations.get(i).get("bedType") + " | ");
						System.out.print("Maximum Occupancy: " + reservations.get(i).get("maxOcc") + " | ");
						System.out.print("Base Price: " + reservations.get(i).get("basePrice") + " | ");
						System.out.print("Decor: " + reservations.get(i).get("decor") + " | ");
						System.out.println("Adjusted CheckIn/ CheckOut dates: " + reservations.get(i).get("CheckIn")
								+ " to " + reservations.get(i).get("CheckOut"));
					}
				}
				System.out.println("Please choose one of the following options (Q - exit to menu): ");
				System.out.print(">>>");
				String input = sc.nextLine();

				if (input.toLowerCase().equals("q")) {
					conn.rollback();
					return;
				}

				int option = Integer.parseInt(input) - 1;

				System.out.println("Confirm the following: ");
				System.out.println(
						"First name, last name: " + userInput.get("FirstName") + " " + userInput.get("LastName"));
				System.out.println("Room code, room name, bed type: " + reservations.get(option).get("RoomCode") + ", "
						+ reservations.get(option).get("RoomName") + ", " + reservations.get(option).get("bedType"));
				if (similarDateFlag) {
					System.out.println("Adjusted CheckIn/ CheckOut dates: " + reservations.get(option).get("CheckIn")
							+ " to " + reservations.get(option).get("CheckOut"));
				} else {
					System.out.println("Check In and Check Out dates: " + userInput.get("CheckIn") + " to "
							+ userInput.get("Checkout"));
				}

				System.out.println("Number of adults: " + userInput.get("Adults"));
				System.out.println("Number of kids: " + userInput.get("Kids"));
				
				//find rate
				// PreparedStatement numWeekendsStatement = conn.prepareStatement("SELECT ( Floor(DATEDIFF(? , ? )/7) * 2)+(CASE WHEN DAYOFWEEK( ? ) = '1' THEN 1 ELSE 0 END)+(CASE WHEN DAYOFWEEK( ? )   = '7' THEN 1 ELSE 0 END) as wknds, DATEDIFF( ? , ? ) as dys");
				PreparedStatement numWeekendsStatement = conn.prepareStatement("SELECT ( Floor(DATEDIFF(? , ? )/7) * 2)+(CASE WHEN DAYOFWEEK( ? ) = '1' THEN 1 ELSE 0 END)+(CASE WHEN DAYOFWEEK( ? )   = '7' THEN 1 ELSE 0 END) as wknds, DATEDIFF( ? , ? ) as dys");
				if(similarDateFlag) {
					numWeekendsStatement.setString(1, reservations.get(option).get("CheckOut"));
					numWeekendsStatement.setString(2, reservations.get(option).get("CheckIn"));
					numWeekendsStatement.setString(3, reservations.get(option).get("CheckIn"));
					numWeekendsStatement.setString(4, reservations.get(option).get("CheckOut"));
					numWeekendsStatement.setString(5, reservations.get(option).get("CheckOut"));
					numWeekendsStatement.setString(6, reservations.get(option).get("CheckIn"));
				}else {
					numWeekendsStatement.setString(1, userInput.get("Checkout"));
					numWeekendsStatement.setString(2, userInput.get("CheckIn"));
					numWeekendsStatement.setString(3, userInput.get("CheckIn"));
					numWeekendsStatement.setString(4, userInput.get("Checkout"));
					numWeekendsStatement.setString(5, userInput.get("Checkout"));
					numWeekendsStatement.setString(6, userInput.get("CheckIn"));
				}
				
				ResultSet numWeekendsRS = numWeekendsStatement.executeQuery();
				if (rs.next()){
					int numWeekends = numWeekendsRS.getInt("wknds");
					int numDays = numWeekendsRS.getInt("dys");
					System.out.println(numWeekends + " " + numDays);
				}

				
			} catch (SQLException e) {
				e.printStackTrace();
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	private static int maxOccOfInn() {
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"), System.getenv("HP_JDBC_USER"),
				System.getenv("HP_JDBC_PW"))) {
			String sql = "Select max(maxOcc) as mx From lab7_rooms";

			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
				rs.next();
				return rs.getInt("mx");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void querySimilarDateRange(HashMap<String, String> userInput,
			ArrayList<HashMap<String, String>> reservations) {
		int count = 1;
		String query = "SELECT DISTINCT lab7_rooms.* FROM lab7_rooms JOIN lab7_reservations ON RoomCode=Room WHERE RoomCode NOT IN (SELECT DISTINCT Room FROM lab7_reservations WHERE (Checkin <= ? AND Checkout > ?) OR (Checkin >= ? AND Checkout< ?) OR (Checkin <= ? AND Checkout >= ?)) AND maxOcc = ? + ?";
		while (reservations.size() <= 5) {
			try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
					System.getenv("HP_JDBC_USER"), System.getenv("HP_JDBC_PW"))) {
				try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
					String checkin = "DATE_ADD('" + userInput.get("CheckIn") + "', INTERVAL " + count + " DAY)";
					String checkout = "DATE_ADD('" + userInput.get("Checkout") + "', INTERVAL " + count + " DAY)";
					System.out.println(checkin);
					System.out.println(checkout);
					preparedStatement.setString(1, checkin);
					preparedStatement.setString(2, checkout);
					preparedStatement.setString(3, checkin);
					preparedStatement.setString(4, checkout);
					preparedStatement.setString(5, checkin);
					preparedStatement.setString(6, checkout);
					preparedStatement.setString(7,userInput.get("Kids"));
					preparedStatement.setString(8,userInput.get("Adults"));
					ResultSet rs = preparedStatement.executeQuery();
					while (rs.next()) {
						// store reservation information in hashmap
						HashMap<String, String> reservation = new HashMap<String, String>();
						reservation.put("RoomCode", rs.getString("RoomCode"));
						reservation.put("RoomName", rs.getString("RoomName"));
						reservation.put("Beds", rs.getString("Beds"));
						reservation.put("bedType", rs.getString("bedType"));
						reservation.put("maxOcc", rs.getString("maxOcc"));
						reservation.put("basePrice", rs.getString("basePrice"));
						reservation.put("decor", rs.getString("decor"));
						
						ResultSet adjustedCheckInRS = conn.createStatement().executeQuery("Select "+checkin + " as DY");
						adjustedCheckInRS.next();
						System.out.println(adjustedCheckInRS.getDate("DY").toString());
						reservation.put("CheckIn",adjustedCheckInRS.getDate("DY").toString());
						
						ResultSet adjustedCheckOutRS = conn.createStatement().executeQuery("Select "+checkout + " as DY");
						adjustedCheckOutRS.next();
						reservation.put("CheckOut" ,adjustedCheckOutRS.getDate("DY").toString());
								
						reservations.add(reservation);
					}
					count++;
				} catch (SQLException e) {
					e.printStackTrace();
					conn.rollback();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// TODO: ISHAN
	/**
	 * Allow the user to make changes to an existing reservation. Accept from the
	 * user a reservation code and new values for any of the following: • First name
	 * • Last name • Begin date • End date • Number of children • Number of adults
	 * Allow the user to provide a new value or to indicate “no change” for a given
	 * field. Update the reservation based on any new information provided. If the
	 * user requests different begin and/or end dates, make sure to check whether
	 * the new begin and end dates conflict with another reservation in the system.
	 */
	public void FR3() {
		// Create menu for user to select which field to change
		String option;
		boolean empty = false;
		Scanner sc = new Scanner(System.in);

		while (!empty) {
			// Request User for Reservation Code
			System.out.println(
					"Please enter the reservation code of the reservation you would like to change, OR Q to return to main menu");
			System.out.print(">>> ");
			String reservationCode = sc.nextLine();
			if (reservationCode.equals("Q")) {
				break;
			}

			// Retrieve reservation code information and validate it exists
			HashMap<String, String> reservationPresent = getReservationCode(reservationCode);
			if (reservationPresent == null) {
				System.out.println("Reservation Code " + reservationCode + " not present in records!");
				continue;
			}

			/**
			 * Allow the user to make changes to an existing reservation. Update the
			 * reservation based on any new information provided.
			 */
			// create deep copy of reservationPresent
			HashMap<String, String> reservationNew = new HashMap<String, String>();
			for (String key : reservationPresent.keySet()) {
				reservationNew.put(key, reservationPresent.get(key));
			}

			String[] fields = { "FirstName", "LastName", "CheckIn", "Checkout", "Adults", "Kids" };
			for (int i = 0; i < fields.length; i++) {
				System.out.println("Specify a new value for " + fields[i]
						+ " or press enter to keep the current value (" + reservationPresent.get(fields[i]) + ")");
				System.out.print(">>> ");
				String newField = sc.nextLine();
				if (!newField.equals("")) {
					reservationNew.put(fields[i], newField);
				}

			}

			// Check if new reservation conflicts with existing reservations (Trigger)
			// create trigger conflicting_record_presence AFTER UPDATE ON lab7_reservations
			// for each row
			// BEGIN
			// DECLARE code_failure varchar(100);
			// SELECT Code into code_failure FROM lab7_reservations WHERE (
			// (New.Checkin<=Checkin AND New.Checkout>Checkin)
			// OR (New.Checkin>=Checkin AND New.Checkout<Checkout)
			// OR (New.Checkin<=Checkout AND New.Checkout>=Checkout)
			// ) AND New.Room=Room AND New.Code!=Code
			// ;
			// if (code_failure is not null) then
			// SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Updated stay interval conflicts
			// with another entry(s)';
			// END IF;
			// if (New.Checkin<=New.Checkout) then
			// SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Check-In Date must be before
			// Checkout Date';
			// end if;
			// END;
			// Update reservation in database
			try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
					System.getenv("HP_JDBC_USER"), System.getenv("HP_JDBC_PW"))) {
				try (PreparedStatement preparedStatement = conn.prepareStatement(
						"UPDATE lab7_reservations SET FirstName=?, LastName=?, CheckIn=?, Checkout=?, Adults=?, KIDS=? WHERE CODE=?")) {
					preparedStatement.setString(1, reservationNew.get("FirstName"));
					preparedStatement.setString(2, reservationNew.get("LastName"));
					preparedStatement.setString(3, reservationNew.get("CheckIn"));
					preparedStatement.setString(4, reservationNew.get("Checkout"));
					preparedStatement.setString(5, reservationNew.get("Adults"));
					preparedStatement.setString(6, reservationNew.get("Kids"));
					preparedStatement.setString(7, reservationCode);
					preparedStatement.executeUpdate();
				}
			} catch (SQLException e) {
				System.out.println("Error: " + e.getMessage());
			}

		}

	}

	/**
	 * Retrieve reservation information for a specified reservation code Meant for
	 * validation of existance of reservation code for FR3
	 */
	private HashMap<String, String> getReservationCode(String reservationCode) {
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"), System.getenv("HP_JDBC_USER"),
				System.getenv("HP_JDBC_PW"))) {
			try (PreparedStatement preparedStatement = conn
					.prepareStatement("SELECT * FROM lab7_reservations WHERE CODE = ?")) {
				preparedStatement.setString(1, reservationCode.toString());
				ResultSet rs = preparedStatement.executeQuery();
				if (!rs.isBeforeFirst()) {
					return null;
				} else {
					while (rs.next()) {
						// store reservation information in hashmap
						HashMap<String, String> reservation = new HashMap<String, String>();
						reservation.put("CODE", rs.getString("CODE"));
						reservation.put("Room", rs.getString("Room"));
						reservation.put("CheckIn", rs.getString("CheckIn"));
						reservation.put("Checkout", rs.getString("Checkout"));
						reservation.put("Rate", rs.getString("Rate"));
						reservation.put("LastName", rs.getString("LastName"));
						reservation.put("FirstName", rs.getString("FirstName"));
						reservation.put("Adults", rs.getString("Adults"));
						reservation.put("Kids", rs.getString("Kids"));
						return reservation;
					}
					return null;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				conn.rollback();
				System.out.println("Reservation " + reservationCode + " does not exist.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * FR2: Reservations. When this option is selected, your system shall accept
	 * from the user the following information: First name Last name A room code to
	 * indicate the specific room desired (or â€œAnyâ€� to indicate no preference) A
	 * desired bed type (or â€œAnyâ€� to indicate no preference) Begin date of stay
	 * End date of stay Number of children Number of adults With this information,
	 * the system shall produce a numbered list of available rooms, along with a
	 * prompt to allow booking by option number. If no exact matches are found, the
	 * system should suggest 5 possibilities for different rooms or dates. These 5
	 * possibilities should be chosen based on similarity to the desired reservation
	 * (similarity defined as nearby dates, rooms with similar features or decor, or
	 * logic of your own choosing) For every option presented, maximum room
	 * occupancy must be considered and the dates must not overlap with another
	 * existing reservation. If the requested person count (children plus adults)
	 * exceeds the maximum capacity of any one room at the Inn, print a message
	 * indicating that no suitable rooms are available. To reserve a block of rooms,
	 * a user must submit multiple reservation requests. At the prompt, the user may
	 * decide to cancel the current request, which will return the user to the main
	 * menu. If the user chooses to book one of the room options presented, they
	 * will enter the option number at the prompt. After a choice is made, provide
	 * the user with a confirmation screen that displays the following: First name,
	 * last name Room code, room name, bed type Begin and end date of stay Number of
	 * adults Number of children Total cost of stay, based on a sum of the
	 * following: Number of weekdays multiplied by room base rate Number of weekend
	 * days multiplied by 110% of the room base rate
	 * 
	 * Allow the user to cancel, returning to the main menu, or confirm, which will
	 * finalize their reservation and create an entry in the lab7 reservations
	 * table. The Rate column should contain the computed nightly rate (ie. total
	 * cost of stay divided by number of nights, rounded to the nearest penny)
	 */

	/*
	 * FR4: Reservation Cancellation. Allow the user to cancel an existing
	 * reservation. Accept from the user a reservation code, confirm the
	 * cancellation, then remove the reservation record from the database.
	 */
	// TODO: ALEX - complete
	public static void FR4() {
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"), System.getenv("HP_JDBC_USER"),
				System.getenv("HP_JDBC_PW"))) {
			Scanner scanner = new Scanner(System.in);

			System.out.print("\nEnter a reservation code to cancel\n>>> ");
			int reservationCode = scanner.nextInt();
			scanner.nextLine();

			System.out.print("Are you sure you want to cancel your reservation? (Y/N)\n>>> ");
			String confirmCancel = scanner.nextLine();

			if (confirmCancel.equalsIgnoreCase("y")) {
				try (PreparedStatement preparedStatement = conn
						.prepareStatement("DELETE FROM lab7_reservations WHERE Code = ?")) {
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
	 * FR3: Reservation Change. Allow the user to make changes to an existing
	 * reservation. Accept from the user a reservation code and new values for any
	 * of the following: First name Last name Begin date End date Number of children
	 * Number of adults Allow the user to provide a new value or to indicate â€œno
	 * changeâ€� for a given field. Update the reservation based on any new
	 * information provided. If the user requests different begin and/or end dates,
	 * make sure to check whether the new begin and end dates conflict with another
	 * reservation in the system.
	 */

	/*
	 * FR5: Detailed Reservation Information. Present a search prompt or form that
	 * allows the user to enter any combination of the fields listed below (a blank
	 * entry should indicate â€œAnyâ€�). For all fields except dates, permit partial
	 * values using SQL LIKE wildcards (for example: GL% should be allowed as a last
	 * name search value) First name Last name A range of dates Room code
	 * Reservation code Using the search information provided, display a list of all
	 * matching reservations found in the database. The list shall show the contents
	 * of every attribute from the lab7 reservations table (as well as the full name
	 * of the room, and any extra information about the room you wish to add).
	 */
	// TODO: ALEX - complete
	public static void FR5() {

		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"), System.getenv("HP_JDBC_USER"),
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

			try (PreparedStatement preparedStatement = conn.prepareStatement(
					"SELECT * FROM lab7_reservations WHERE FirstName like ? and LastName like ? and CheckIn >= ?  and Checkout <= ? and Room like ? and CODE like ?")) {
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
	 * FR6: Revenue. When this option is selected, your system shall provide a
	 * month-by-month overview of revenue for the current calendar year, based on
	 * SQLâ€˜s CURRENT DATE variable. For the purpose of this assignment, revenue
	 * from a reservation should be recognized in the month and year when the
	 * reservation ends. For example a reservations that begins on October 30th,
	 * 2022 and ends on November 2nd, 2022 would be considered November 2022
	 * revenue. Your system shall display a list of rooms, and, for each room, 13
	 * columns: 12 columns showing dollar revenue for each month and a 13th column
	 * to display total yearly revenue for the room. There shall also be a totals
	 * row in the table, which contains column totals. All amounts should be rounded
	 * to the nearest whole dollar.
	 */
	public static void FR6() {
		// TODO Ishan
		/*
		 * SELECT Room, MONTH(lab7_reservations.Checkout) AS Month,
		 * SUM(DateDiff(Checkout,Checkin)*Rate) FROM lab7_reservations JOIN lab7_rooms
		 * ON lab7_rooms.RoomCode=lab7_reservations.Room WHERE
		 * YEAR(lab7_reservations.Checkout)=YEAR(CURDATE()) GROUP BY
		 * Room,MONTH(lab7_reservations.Checkout) ORDER BY Room,Month
		 */
		// Perform SQL query for monthly revenue for each room
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"), System.getenv("HP_JDBC_USER"),
				System.getenv("HP_JDBC_PW"))) {
			try (PreparedStatement preparedStatement = conn.prepareStatement(
					"SELECT Room, MONTH(lab7_reservations.Checkout) AS Month,ROUND(SUM(DateDiff(Checkout,Checkin)*Rate),2) AS MonthlyRevenue FROM lab7_reservations JOIN lab7_rooms ON lab7_rooms.RoomCode=lab7_reservations.Room WHERE YEAR(lab7_reservations.Checkout)=YEAR(CURDATE()) GROUP BY Room,MONTH(lab7_reservations.Checkout) ORDER BY Room,Month")) {
				ResultSet rs = preparedStatement.executeQuery();
				HashMap<String, ArrayList<Double>> monthlyRevenues = new HashMap<String, ArrayList<Double>>();

				while (rs.next()) {
					String room = rs.getString("Room");
					int month = rs.getInt("Month");
					double revenue = rs.getDouble("MonthlyRevenue");
					// Create new arraylist if room is not in hashmap
					if (!monthlyRevenues.containsKey(room)) {
						monthlyRevenues.put(room, new ArrayList<Double>(Collections.nCopies(13, 0.0)));

					}
					monthlyRevenues.get(room).set(month - 1, revenue);
					// Add Yearly revenue to last index
					double yearlyRevenue = revenue + monthlyRevenues.get(room).get(12);
					monthlyRevenues.get(room).set(12, yearlyRevenue);

				}
				// Print monthly revenue for each room
				System.out.println("Room\tJan\tFeb\tMar\tApr\tMay\tJun\tJul\tAug\tSep\tOct\tNov\tDec\tTotal");
				for (String room : monthlyRevenues.keySet()) {
					System.out.print(room + "\t");

					for (int i = 0; i < 12; i++) {
						if (monthlyRevenues.get(room).size() > i) {
							int profit = monthlyRevenues.get(room).get(i).intValue();
							System.out.print("$" + profit + "\t");
						} else {
							System.out.print("0\t");
						}
					}
					int yearlyRevenue = monthlyRevenues.get(room).get(12).intValue();
					System.out.println("$" + yearlyRevenue);
				}
				// Print monthly revenue for each room
			} catch (SQLException e) {
				e.printStackTrace();
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Demo2 - Establish JDBC connection, execute SELECT query, read & print result
	public void demo2() throws SQLException {

		System.out.println("demo2: List content of hp_goods table\r\n");
		printEnv();

		// Step 1: Establish connection to RDBMS
		try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"), System.getenv("HP_JDBC_USER"),
				System.getenv("HP_JDBC_PW"))) {
			// Step 2: Construct SQL statement
			String sql = "SELECT * FROM lab7_rooms LIMIT 1";

			// Step 3: (omitted in this example) Start transaction

			// Step 4: Send SQL statement to DBMS
			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

				// Step 5: Receive results
				while (rs.next()) {
					// RoomCode RoomName Beds bedType maxOcc basePrice decor
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
