package org.GiannePesana;

import java.io.*;
import java.util.*;

public abstract class UserService {

    protected static final int invalid = -1;
    protected static final int active = 0;
    protected static final int pending = 1;
    protected static final int suspended = 2;

    private static String dirPath = "data\\accounts\\";

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
        String firstName, lastName, username, pin, userID, status;
        int age;
        double balance;

        try (Scanner accAuth = new Scanner(new FileReader("data/accounts/users.txt"))) {
            while (accAuth.hasNextLine()) {
                String line = accAuth.nextLine().trim(); // Get the full line
                String[] parts = line.split("\\|");

                if (parts.length < 8) continue; // Skip malformed lines
                firstName = parts[0];
                lastName = parts[1];
                age = Integer.parseInt(parts[2]);
                username = parts[3];
                pin = parts[4];
                userID = parts[5];
                balance = Double.parseDouble(parts[6]);
                status = parts[7].trim();

                if (username.equals(targetUsername)) {
                    return new UserAccount(firstName, lastName, age, username, pin, userID, balance, status);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error 404, File not found");
        }
        return null;
    }

    static void updateUserData(UserAccount user) {

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

                if (parts[5].equals(user.getUserID().trim())) {
                    String newUserData =
                            user.getFirstName() + "|" + user.getLastName() + "|" +
                            user.getAge() + "|" + user.getUsername() + "|" +
                            user.getPin() + "|" + user.getUserID() + "|" +
                            user.getBalance() + "|" + user.getStatus();
                    outFile.append(newUserData + "\n");
                } else {
                    outFile.append(line + "\n");
                }
            }
            inTempFile.close();
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

    static boolean equalSentinel(double value) {
        return value == -1;
    }

    static boolean equalSentinel(String value) {
        return value.equals("-1");
    }

    static boolean usernameExists(String username) {
        UserAccount user = findAccount(username);
        return (user != null);
    }

    static void createAccount(UserAccount user) {
        try {
            FileWriter outFile = new FileWriter(dirPath + "users.txt", true);
            String data =
                    user.getFirstName() + "|" + user.getLastName() + "|" +
                    user.getAge() + "|" + user.getUsername() + "|" +
                    user.getPin() + "|" + user.getUserID() + "|" +
                    user.getBalance() + "|" + user.getStatus();
            outFile.append(data + "\n");
            outFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String generateUserID() {
        String lastUserID = "0000"; // default if no user exists
        try {
            Scanner inFile = new Scanner(new FileReader(dirPath + "users.txt"));
            while (inFile.hasNextLine()) {
                String[] parts = inFile.nextLine().split("\\|");
                if (parts.length < 8) continue;

                String userID = parts[5];
                if (Integer.parseInt(userID) > Integer.parseInt(lastUserID)) {
                    lastUserID = userID;
                }
            }

            int lastUserIDInt = Integer.parseInt(lastUserID);
            int generatedUserIDInt = lastUserIDInt + 1;
            inFile.close();
            // Format to 4-digit ID like 0001, 0234, etc.
            return String.format("%04d", generatedUserIDInt);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
