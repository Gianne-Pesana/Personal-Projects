package org.GiannePesana;

import java.io.*;
import java.util.*;

public abstract class TransactionsController {
    public static String deposit = "deposit";
    public static String withdraw = "withdraw";

    public static void saveTransaction(String type, UserAccount user, double amount) {
        try {
            FileWriter outFile = new FileWriter("data\\transactions\\transactions.txt", true);
            String line = type + "|" + Utils.getDateTime() + "|" + user.getUsername() + "|" + user.getUserID() + "|" + amount + "\n";
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
}
