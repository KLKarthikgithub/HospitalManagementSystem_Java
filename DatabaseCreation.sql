-- for creating a new database for patient reords in mysql workbench
CREATE DATABASE PatientManagementDB;

-- for use of database PatientManagementDB
USE PatientManagementDB;


--later

-- for creating a table in PatientManagementDB Database 
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    contact_number VARCHAR(15),
    address TEXT
);
