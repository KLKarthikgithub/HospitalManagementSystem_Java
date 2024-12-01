package com.hms.patients;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String URL = "jdbc:mysql://localhost:3306/hospital_database";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "Karthik@5484"; // Change to your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            while (true) {
            	//System.out.println("------------------------------");
                System.out.println(" -------******* Hospital Management System *******-------");
               // System.out.println("------------------------------");
                System.out.println("1. Create a New Patient");
                System.out.println("2. View All Patients Records");
                System.out.println("3. Update a Patient Record");
                System.out.println("4. Delete a Patient Record");
                System.out.println("5. Search a Patient Reocrd");
                System.out.println("6. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addPatient(scanner, connection);
                        break;
                    case 2:
                        viewPatients(connection);
                        break;
                    case 3:
                        updatePatient(scanner, connection);
                        break;
                    case 4:
                        deletePatient(scanner, connection);
                        break;
                    case 5:
                    	System.out.println("Search a Patient Record by using:");
                        searchPatient(scanner, connection);
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void addPatient(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("-----Creating a Patient Record-----");
        
        // Get patient details from user input
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter patient gender: ");
        String gender = scanner.nextLine();
        
        System.out.print("Enter patient contact: ");
        String contact = scanner.nextLine();
        
        // Get patient status
//        System.out.print("Enter patient status (Admitted/Discharged): ");
//        String status = scanner.nextLine();

        // Prepare the SQL statement
        String sql = "INSERT INTO patients (name, age, gender, contact, date_of_joining, status) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP(), ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set parameters for the prepared statement
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, contact);
            stmt.setString(5, "Admitted"); // Use the status provided by the user
            
            // Execute the update
            stmt.executeUpdate();
            System.out.println("Patient added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
        }
    }
    private static void viewPatients(Connection connection) throws SQLException {
        String sql = "SELECT * FROM patients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Print the table header
           
            // Check if the ResultSet is empty
            if (!rs.isBeforeFirst()) { // This checks if the ResultSet is empty
                System.out.println("Sorry.!!!-No patients found in the database...");
            } else {
            	 System.out.println("-----Getting All Patient Details in the Hospital-----");
            	 System.out.println("--------------------------------------------------------------------------------------------------------------------");
                 System.out.printf("|%-5s | %-20s |%-5s |%-10s |%-15s |%-15s 	|%-25s |%n", "P_ID", "Name", "Age", "Gender", "Contact", "Date of Joining", "Status");
                 System.out.println("--------------------------------------------------------------------------------------------------------------------");

                // Loop through the result set and print each row
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    String contact = rs.getString("contact");
                    Date dateOfJoining = rs.getDate("date_of_joining");
                    String status = rs.getString("status");

                    // Print each patient record in a formatted way
                    System.out.printf("|%-5s | %-20s |%-5s |%-10s |%-15s |%-15s 	|%-25s |%n",
                            id, name, age, gender, contact, dateOfJoining, status);
                    System.out.println("--------------------------------------------------------------------------------------------------------------------");
                }
            }
        }
    }


private static void updatePatient(Scanner scanner, Connection connection) throws SQLException {
    System.out.print("Enter patient name to update: ");
    String nameToUpdate = scanner.nextLine();

    // Check the current status of the patient by name
    String currentStatus = null;
    String checkStatusSql = "SELECT status FROM patients WHERE name = ?";
    try (PreparedStatement checkStatusStmt = connection.prepareStatement(checkStatusSql)) {
        checkStatusStmt.setString(1, nameToUpdate);
        ResultSet rs = checkStatusStmt.executeQuery();
        if (rs.next()) {
            currentStatus = rs.getString("status");
            System.out.println("Current status of patient " + nameToUpdate + ": " + currentStatus);
        } else {
            System.out.println("Patient not found.");
            return; // Exit if patient does not exist
        }
    }

    // Prepare the SQL update statement
    StringBuilder sql = new StringBuilder("UPDATE patients SET ");
    boolean first = true;

    // Ask if the user wants to change the status
    System.out.print("Do you want to change the status? (yes/no): ");
    String changeStatusResponse = scanner.nextLine().trim().toLowerCase();
    if (changeStatusResponse.equals("yes")) {
        // Ask for the new status as boolean
        System.out.print("Set status to Discharged? (yes/no): ");
        String admittedResponse = scanner.nextLine().trim().toLowerCase();
        String newStatus;
        if (admittedResponse.equals("yes")) {
            // Get current timestamp
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            newStatus = "Discharged on " + now.format(formatter);
        } else {
            newStatus = "Admitted";
        }
        sql.append("status = '").append(newStatus).append("'");
        first = false;
    }

    // Collect updates for other fields
    if (promptForUpdate(scanner, "Do you want to update the name? (yes/no): ")) {
        System.out.print("Enter new patient name: ");
        String newName = scanner.nextLine();
        sql.append(first ? "" : ", ").append("name = '").append(newName).append("'");
        first = false;
    }

    if (promptForUpdate(scanner, "Do you want to update the age? (yes/no): ")) {
        System.out.print("Enter new patient age: ");
        int newAge = scanner.nextInt();
        sql.append(first ? "" : ", ").append("age = ").append(newAge);
        first = false;
        scanner.nextLine(); // Consume newline
    }

    if (promptForUpdate(scanner, "Do you want to update the gender? (yes/no): ")) {
        System.out.print("Enter new patient gender: ");
        String newGender = scanner.nextLine();
        sql.append(first ? "" : ", ").append("gender = '").append(newGender).append("'");
        first = false;
    }

    if (promptForUpdate(scanner, "Do you want to update the contact? (yes/no): ")) {
        System.out.print("Enter new patient contact: ");
        String newContact = scanner.nextLine();
        sql.append(first ? "" : ", ").append("contact = '").append(newContact).append("'");
        first = false;
    }

    if (promptForUpdate(scanner, "Do you want to update the date of joining? (yes/no): ")) {
        System.out.print("Enter new date of joining (YYYY-MM-DD): ");
        String newDateStr = scanner.nextLine();
        sql.append(first ? "" : ", ").append("date_of_joining = '").append(Date.valueOf(newDateStr)).append("'");
        first = false;
    }

    // If no updates were specified, notify the user
    if (first) {
        System.out.println("No details were updated.");
        return;
    }

    // Complete the SQL statement with the WHERE clause
    sql.append(" WHERE name = '").append(nameToUpdate).append("'");

    // Execute the update
    try (Statement stmt = connection.createStatement()) {
        int rowsUpdated = stmt.executeUpdate(sql.toString());
      
        if (rowsUpdated > 0) {
            System.out.println("Patient updated successfully.");
        } else {
            System.out.println("Patient not found or no changes made.");
        }
    }
}

// Helper method to prompt for updates
private static boolean promptForUpdate(Scanner scanner, String message) {
    System.out.print(message);
    String response = scanner.nextLine().trim().toLowerCase();
    return response.equals("yes");
}

    private static void deletePatient(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Enter patient ID to delete: ");
        int idToDelete = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "DELETE FROM patients WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idToDelete);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Patient deleted successfully.");
            } else {
                System.out.println("Patient not found.");
            }
        }
    }

    private static void searchPatient(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("Search Patient");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Age");
        System.out.println("3. Search by Status");
        System.out.println("4. Search by Date of Joining");
        System.out.println("5. Search by Multiple Criteria");
        System.out.print("Choose an option: ");
        int searchOption = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            switch (searchOption) {
                case 1:
                    System.out.print("Enter patient name to search: ");
                    String nameToSearch = scanner.nextLine();
                    sql = "SELECT * FROM patients WHERE name LIKE ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, "%" + nameToSearch + "%");
                    break;

                case 2:
                    System.out.print("Enter patient age to search: ");
                    int ageToSearch = scanner.nextInt();
                    sql = "SELECT * FROM patients WHERE age = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, ageToSearch);
                    break;

                case 3:
                    System.out.print("Enter patient status to search (Admitted/Discharged): ");
                    String statusToSearch = scanner.nextLine();
                    sql = "SELECT * FROM patients WHERE status = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, statusToSearch);
                    break;

                case 4:
                    System.out.print("Enter date of joining to search (YYYY-MM-DD): ");
                    String dateStr = scanner.nextLine();
                    Date dateOfJoining = Date.valueOf(dateStr);
                    sql = "SELECT * FROM patients WHERE date_of_joining = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setDate(1, dateOfJoining);
                    break;

                case 5:
                    System.out.print("Enter patient name to search (optional, leave blank for no filter): ");
                    String name = scanner.nextLine();
                    System.out.print("Enter patient age to search (optional, -1 for no filter): ");
                    int age = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter patient status to search (optional, leave blank for no filter): ");
                    String status = scanner.nextLine();
                    System.out.print("Enter date of joining to search (optional, leave blank for no filter): ");
                    String dateInput = scanner.nextLine();

                    StringBuilder query = new StringBuilder("SELECT * FROM patients WHERE 1=1");
                    if (!name.isEmpty()) {
                        query.append(" AND name LIKE ?");
                    }
                    if (age >= 0) {
                        query.append(" AND age = ?");
                    }
                    if (!status.isEmpty()) {
                        query.append(" AND status = ?");
                    }
                    if (!dateInput.isEmpty()) {
                        query.append(" AND date_of_joining = ?");
                    }

                    stmt = connection.prepareStatement(query.toString());

                    int paramIndex = 1;
                    if (!name.isEmpty()) {
                        stmt.setString(paramIndex++, "%" + name + "%");
                    }
                    if (age >= 0) {
                        stmt.setInt(paramIndex++, age);
                    }
                    if (!status.isEmpty()) {
                        stmt.setString(paramIndex++, status);
                    }
                    if (!dateInput.isEmpty()) {
                        Date dateToSearch = Date.valueOf(dateInput);
                        stmt.setDate(paramIndex++, dateToSearch);
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    return;
            }

            rs = stmt.executeQuery();
            boolean found = false;
            System.out.println("Search Results:");
            while (rs.next()) {
                found = true;
                int id = rs.getInt("id");
                String patientName = rs.getString("name");
                int patientAge = rs.getInt("age");
                String patientGender = rs.getString("gender");
                String patientContact = rs.getString("contact");
                Date dateOfJoiningResult = rs.getDate("date_of_joining");
                String patientStatus = rs.getString("status");
                System.out.printf("ID: %d, Name: %s, Age: %d, Gender: %s, Contact: %s, Date of Joining: %s, Status: %s%n",
                        id, patientName, patientAge, patientGender, patientContact, dateOfJoiningResult, patientStatus);
            }
            if (!found) {
                System.out.println("No patients found with the specified criteria.");
            }
        } catch (SQLException e) {
            System.err.println("SQL error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            // Close ResultSet and PreparedStatement if they were created
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
