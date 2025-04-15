package org.GiannePesana.User;

public class UserAccount {
    private String userID;
    private String firstName;
    private String lastName;
    private int age;
    private String username;
    private String pin;
    private double balance;
    private UserStatus status;
    private String creationDate;

    public UserAccount(String userID, String firstName, String lastName, int age, String username, String pin, double balance, UserStatus status, String creationDate) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.username = username;
        this.pin = pin;
        this.balance = balance;
        this.status = status;
        this.creationDate = creationDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "User " + userID;
    }
}
