package org.GiannePesana;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserService {

    protected static final int invalid = -1;
    protected static final int active = 0;
    protected static final int pending = 1;
    protected static final int suspended = 2;

    // find and check account status
    static int authenticateUsername(String username) {
        UserAccount user = findAccount(username);
        if (user == null) return invalid;
        return statusStringToInt(user.getStatus());
    }

    public static int statusStringToInt(String status) {
        if(status.equals("active")) return active;
        else if (status.equals("pending")) return pending;
        else if (status.equals("suspended")) return suspended;
        else return -1;
    }

    public static String statusIntToString(int status) {
        if (status == active) return "active";
        else if (status == pending) return "pending";
        else if (status == suspended) return "suspended";
        else return "invalid";
    }

    public static String getUserStatusMessage(int status) {
        if (status == active) return "Account is active.";
        else if (status == pending) return "Account is waiting for approval. Contact administrator.";
        else if (status == suspended) return "Account has been suspended. Contact administrator.";
        else return "Error: Account not found.";
    }

    static UserAccount findAccount(String targetUsername) {
        String username, pin, userID, status;
        double balance;

        try (Scanner accAuth = new Scanner(new FileReader("data/accounts/users.txt"))) {
            while (accAuth.hasNextLine()) {
                String line = accAuth.nextLine().trim(); // Get the full line
                String[] parts = line.split("\\|");

                if (parts.length < 5) continue; // Skip malformed lines

                username = parts[0];
                pin = parts[1];
                userID = parts[2];
                balance = Double.parseDouble(parts[3]);
                status = parts[4].trim();

                if (username.equals(targetUsername)) {
                    return new UserAccount(username, pin, userID, balance, status);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error 404, File not found");
        }
        return null;
    }

    static void updateUserData(UserAccount user) {
        String dirPath = "data\\accounts\\";
        // copy user data to temp file
        try {
            Scanner inFile = new Scanner(new FileReader(dirPath + "users.txt"));
            FileWriter outTempFile = new FileWriter(dirPath + "temp.txt");

            while(inFile.hasNextLine()) {
                String line = inFile.nextLine();
                outTempFile.append(line + "\n");
            }
            outTempFile.close();
            inFile.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred: " + e.getMessage(), e);
        }

        // replacing data
        try {
            Scanner inTempFile = new Scanner(new FileReader(dirPath + "temp.txt"));
            FileWriter outFile = new FileWriter(dirPath + "users.txt");
            // use userID so that it will still work even if username is changed
            while (inTempFile.hasNextLine()) {
                String line = inTempFile.nextLine();
                String[] parts = line.split("\\|");

                if (parts[2].equals(user.getUserID().trim())) {
                    String newUserData = user.getUsername() + "|" + user.getPin() + "|" + user.getUserID() + "|" + user.getBalance() + "|" + user.getStatus();
                    outFile.append(newUserData + "\n");
                } else {
                    outFile.append(line + "\n");
                }
            }
            outFile.close();

            // clears the temp file
            FileWriter clearTempFile = new FileWriter(dirPath + "temp.txt");
            clearTempFile.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred: " + e.getMessage(), e);
        }
    }
}
