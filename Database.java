import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/PatientManagementDB";
    private static final String USER = "your_mysql_username";
    private static final String PASSWORD = "your_mysql_password";

    private Connection connection;

    // Constructor to establish a database connection
    public Database() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Add a new patient
    public void addPatient(Patient patient) throws SQLException {
        String query = "INSERT INTO patients (name, age, gender, contact_number, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getContactNumber());
            stmt.setString(5, patient.getAddress());
            stmt.executeUpdate();
        }
    }

    // Retrieve all patients
    public List<Patient> getAllPatients() throws SQLException {
        String query = "SELECT * FROM patients";
        List<Patient> patients = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                patients.add(new Patient(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("contact_number"),
                    rs.getString("address")
                ));
            }
        }
        return patients;
    }

    // Update a patient by ID
    public void updatePatient(int id, Patient patient) throws SQLException {
        String query = "UPDATE patients SET name = ?, age = ?, gender = ?, contact_number = ?, address = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, patient.getName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getContactNumber());
            stmt.setString(5, patient.getAddress());
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    // Delete a patient by ID
    public void deletePatient(int id) throws SQLException {
        String query = "DELETE FROM patients WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Close the database connection
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}

