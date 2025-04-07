package org.GiannePesana;

public class UserAccount extends Account{
    private String pin;
    private String userID;
    private Double balance;
    private String status;

    // status flags
    public static String active = "active";
    public static String pending = "pending";
    public static String suspended = "suspended";

    public UserAccount(String pin, String userID, Double balance, String status) {
        this.pin = pin;
        this.userID = userID;
        this.balance = balance;
        this.status = status;
    }

    public UserAccount(String username, String pin, String userID, Double balance, String status) {
        super(username);
        this.pin = pin;
        this.userID = userID;
        this.balance = balance;
        this.status = status;
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
