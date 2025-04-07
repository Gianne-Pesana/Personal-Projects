package org.GiannePesana;

import java.io.FileWriter;
import java.io.IOException;

public class TransactionsController {
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
}
