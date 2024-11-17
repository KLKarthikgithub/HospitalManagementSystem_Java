import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class HospitalManagementSystem {

    public static void main(String[] args) {
        try (Database db = new Database(); Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nHospital Management System - Patient Records");
                System.out.println("1. Add Patient");
                System.out.println("2. View All Patients");
                System.out.println("3. Update Patient");
                System.out.println("4. Delete Patient");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
switch (choice) {
    case 1:
        System.out.println("Enter Patient Details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
        System.out.print("Gender (Male/Female/Other): ");
        String gender = scanner.nextLine();
        System.out.print("Contact Number: ");
        String contactNumber = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();

        Patient newPatient = new Patient(name, age, gender, contactNumber, address);
        db.addPatient(newPatient);
        System.out.println("Patient added successfully!");
        break;

    case 2:
        List<Patient> patients = db.getAllPatients();
        System.out.println("\nPatient Records:");
        for (Patient patient : patients) {
            System.out.println(patient);
        }
        break;

    case 3:
        System.out.print("Enter the ID of the patient to update: ");
        int updateId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.println("Enter Updated Details:");
        System.out.print("Name: ");
        name = scanner.nextLine();
        System.out.print("Age: ");
        age = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
        System.out.print("Gender (Male/Female/Other): ");
        gender = scanner.nextLine();
        System.out.print("Contact Number: ");
        contactNumber = scanner.nextLine();
        System.out.print("Address: ");
        address = scanner.nextLine();

        Patient updatedPatient = new Patient(name, age, gender, contactNumber, address);
        db.updatePatient(updateId, updatedPatient);
        System.out.println("Patient updated successfully!");
        break;

    case 4:
        System.out.print("Enter the ID of the patient to delete: ");
        int deleteId = scanner.nextInt();
        db.deletePatient(deleteId);
        System.out.println("Patient deleted successfully!");
        break;

    case 5:
        System.out.print("Enter the ID of the patient to find: ");
        int findId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline
        Patient foundPatient = db.findPatientById(findId);
        if (foundPatient != null) {
            System.out.println("Patient Details: " + foundPatient);
        } else {
            System.out.println("Patient with ID " + findId + " not found.");
        }
        break;

    case 6:
        System.out.println("Exiting the system. Goodbye!");
        return;

    default:
        System.out.println("Invalid choice. Please try again.");
}

            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
