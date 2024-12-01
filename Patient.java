package com.hms.patients;
import java.sql.Date;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String contact;
    private Date dateOfJoining;
    private String status;

    // Constructor
    public Patient(int id, String name, int age, String gender, String contact, Date dateOfJoining, String status) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contact = contact;
        this.dateOfJoining = dateOfJoining;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Date dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Age: %d, Gender: %s, Contact: %s, Date of Joining: %s, Status: %s",
                id, name, age, gender, contact, dateOfJoining, status);
    }
}
