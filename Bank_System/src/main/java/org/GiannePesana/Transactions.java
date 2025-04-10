package org.GiannePesana;

public class Transactions {
    private String type;
    private String transactionID;
    private String dateTime;
    private double amount;

    public Transactions() {
    }

    public Transactions(String type, String transactionID, String dateTime, double amount) {
        this.type = type;
        this.transactionID = transactionID;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
