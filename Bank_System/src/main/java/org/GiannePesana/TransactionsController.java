package org.GiannePesana;

import java.io.*;
import java.util.*;

public abstract class TransactionsController {
    public static String deposit = "deposit";
    public static String withdraw = "withdraw";

    private static Transactions transaction;
    private static UserAccount user;

    public static void saveTransaction(String type, UserAccount userTransaction, double amount) {
        try {
            user = userTransaction;
            String transactionID = generateTransactionID(type);
            String dateTime = Utils.getDateTime();

            transaction = new Transactions(type, transactionID, dateTime, amount);

            FileWriter outFile = new FileWriter("data\\transactions\\transactions.txt", true);
            String line = transaction.getType() + "|" + transaction.getTransactionID() + "|" + transaction.getDateTime() + "|" + user.getUsername() + "|" + user.getUserID() + "|" + transaction.getAmount() + "\n";

            outFile.append(line);
            outFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String generateTransactionID(String transactionType) {
        long timestamp = System.currentTimeMillis() / 1000;
        Random random = new Random();
        int randomNumber = random.nextInt(300);

        return String.format(
                "TX-%s-%d-%03d",
                transactionType.equals(deposit) ? "DPS" : "WD",
                timestamp,
                randomNumber
                );
    }

    static void showReceipt() {
        String receiptHeader =
                "===========================================\n" +
                "                   CS Bank\n" +
                "             Transaction Receipt\n" +
                "===========================================";

        String receiptData = String.format(
                "%-18s: %s\n" +
                "%-18s: %s\n" +
                "%-18s: %s\n\n" +
                "%-18s: %s\n" +
                "%-18s: %s",
                "Transaction ID", transaction.getTransactionID(),
                "Transaction type", transaction.getType(),
                "Date & Time", transaction.getDateTime(),
                "Amount", ("$" + transaction.getAmount()),
                "Updated balance", ("$" + user.getBalance())
                );

        String receiptFooter =
                "===========================================\n" +
                "     Thank you for banking with\n" +
                "            CS Bank!\n" +
                "===========================================";

        System.out.println(
                receiptHeader + "\n" +
                receiptData + "\n" +
                receiptFooter
                );
    }
}
