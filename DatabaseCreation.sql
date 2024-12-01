-- Create the database
CREATE DATABASE hospital;

-- Use the hospital database
USE hospital;

-- Create the patients table
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    contact VARCHAR(15) NOT NULL,
    date_of_joining DATETIME NOT NULL, -- Changed to DATETIME to include time
    status VARCHAR(50) NOT NULL -- Changed from ENUM to VARCHAR(50)
);

-- If you need to alter the status column after creating the table, you can use:
-- ALTER TABLE patients MODIFY status VARCHAR(50);
