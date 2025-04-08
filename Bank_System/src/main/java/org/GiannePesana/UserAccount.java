package org.GiannePesana;

import javax.management.RuntimeErrorException;

public class UserAccount extends Account{
    private String firstName;
    private String lastName;
    int age;
    private String pin;
    private String userID;
    private Double balance;
    private String status;

    // status flags
    public static String active = "active";
    public static String pending = "pending";
    public static String suspended = "suspended";

    public UserAccount() {}

    public UserAccount(String firstName, String lastName, int age, String username, String pin, String userID, Double balance, String status) {
        super(username);
        this.firstName = Utils.formatString(firstName);
        this.lastName = Utils.formatString(lastName);
        this.age = age;
        this.userID = userID;
        this.pin = pin;
        this.balance = balance;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Utils.formatString(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Utils.formatString(lastName);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age > 18) {
            this.age = age;
        } else {
            System.out.println("Internal error: Invalid age.");
        }
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals(active) || status.equals(pending) || status.equals(suspended)) {
            this.status = status;
        } else {
            System.out.println("Internal error: Invalid status");
        }
    }
}
