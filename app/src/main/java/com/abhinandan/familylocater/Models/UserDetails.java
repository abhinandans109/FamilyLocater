package com.abhinandan.familylocater.Models;

public class UserDetails {
    String Email,Password,ContactNo,id;

    public UserDetails() {
    }

    public UserDetails(String email, String password, String contactNo, String id) {
        Email = email;
        Password = password;
        ContactNo = contactNo;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDetails(String email, String password, String contactNo) {
        Email = email;
        Password = password;
        ContactNo = contactNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }
}
