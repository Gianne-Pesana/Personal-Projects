package org.GiannePesana;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserController {

    static UserAccount user;

    private static String titleHeader =
            "                                                      \n" +
                    " ,-----. ,---.      ,-----.                  ,--.     \n" +
                    "'  .--./'   .-'     |  |) /_  ,--,--.,--,--, |  |,-.  \n" +
                    "|  |    `.  `-.     |  .-.  \\' ,-.  ||      \\|     /  \n" +
                    "'  '--'\\.-'    |    |  '--' /\\ '-'  ||  ||  ||  \\  \\  \n" +
                    " `-----'`-----'     `------'  `--`--'`--''--'`--'`--' \n" +
                    "======================================================\n\n";

    public static void handleLoginPage() {
        String menu =
                titleHeader +
                "Choose operation:\n" +
                "1.) Login\n" +
                "2.) Create bank account\n\n" +
                "Enter your choice: ";
        Utils.clearConsole();
        System.out.print(menu);

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next();
        switch (choice) {
            case "1", "Login" -> {
                Utils.clearConsole();
                userLandingPage();
            }
            default -> System.out.println(Utils.invalidInputMessage());
        }
    }

    private static void userLandingPage() {
        user = authenticateUser();
        if(user == null) return; // ends the program



        boolean isRunning = true;
        while (isRunning) {
            Utils.clearConsole();
            String menu =
                    titleHeader +
                            "Welcome " + user.getUsername() + "!\n" +
                            "-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n" +
                            "1.) View balance\n" +
                            "2.) Deposit\n" +
                            "3.) Withdraw\n" +
                            "4.) Exit\n\n" +
                            "Choose Operation: ";
            System.out.print(menu);

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();

            switch (choice) {
                case "1", "View balance" -> {
                    Utils.clearConsole();
                    viewBalance();
                    Utils.pressAnyKeyToContinue();
                }
                case "2", "Deposit" -> {
                    Utils.clearConsole();
                    deposit();
                }
                case "3", "Withdraw" -> {
                    Utils.clearConsole();
                    withdraw();
                }
                case "4", "Exit" -> {
                    Utils.clearConsole();
                    isRunning = false;
                }
                default -> System.out.println(Utils.invalidInputMessage());
            }
        }
    }

    private static void viewBalance() {
        System.out.println("Your current balance is: " + user.getBalance());
    }

    private static void deposit() {
        double depositAmount;
        do {
            viewBalance();
            System.out.println(); // for formatting
            System.out.println("(Type -1 to cancel)");
            System.out.print("Enter amount to deposit: ");
            Scanner scanner = new Scanner(System.in);

            depositAmount = scanner.nextDouble();
            if (depositAmount == -1) return;

            if (depositAmount <= 0) {
                System.out.println("Invalid Amount!");
                Utils.threadSleep();
                Utils.clearConsole();
            }
        } while (depositAmount <= 0);

        System.out.println("gawas");

        user.setBalance(user.getBalance() + depositAmount);
        updateUserData();
        TransactionsController.saveTransaction(TransactionsController.deposit, user, depositAmount);
        System.out.println(depositAmount + " has been deposited to your account.");
        Utils.threadSleep(2000);
    }

    private static void withdraw() {

    }

    private static void updateUserData() {
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

        try {
            Scanner inTempFile = new Scanner(new FileReader(dirPath + "temp.txt"));
            FileWriter outFile = new FileWriter(dirPath + "users.txt");
            int i = 0;
            // use userID so that it will still work even if username is changed
            while (inTempFile.hasNextLine()) {
                String line = inTempFile.nextLine();
                String[] parts = line.split("\\|");

                if (parts[2].equals(user.getUserID().trim())) {
                    String newUserData = user.getUsername() + "|" + user.getPin() + "|" + user.getUserID() + "|" + user.getBalance() + "|" + user.getStatus();
                    System.out.println(newUserData);
                    outFile.append(newUserData + "\n");
                } else {
                    outFile.append(line + "\n");
                }
            }
            outFile.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("IO Exception occurred: " + e.getMessage(), e);
        }
    }

    private static UserAccount authenticateUser() {
        String inputUsername;
        String inputPin;
        int pinAttempts = 3;
        Scanner scanner = new Scanner(System.in);

        boolean usernameAuth = false;
        boolean pinAuth = false;

        UserAccount userAuth = null;
        while (!usernameAuth) {
            Utils.clearConsole();
            System.out.println(titleHeader);
            System.out.print("Enter your username: ");
            inputUsername = scanner.nextLine();

            userAuth = findAccount(inputUsername);
            if (userAuth != null) {
                usernameAuth = true;
            } else {
                System.out.println("Username not found!");
                Utils.threadSleep();
            }
        }

        while(!pinAuth && usernameAuth && pinAttempts > 0) {
            Utils.clearConsole();
            System.out.println(titleHeader);
            System.out.print("Enter your PIN: ");
            inputPin = scanner.nextLine();

            if(inputPin.equals(userAuth.getPin())) {
                pinAuth = true;
                break;
            }
            if (!pinAuth) {
                --pinAttempts;
                if (pinAttempts > 0) {
                    System.out.println("Invalid PIN. Attempts left: " + pinAttempts);
                }
                Utils.threadSleep();
            }
        }

        // check for max allowed attempts, then return to main menu
        if(!pinAuth) {
            userAuth.setStatus(UserAccount.suspended);
            System.out.println("Too many failed attempts. Exiting ...");
            Utils.threadSleep();
            return null;
        }

        if(!((userAuth.getStatus()).equals(UserAccount.active))) {
            System.out.println("Account is inactive. Contact administrator.");
            Utils.threadSleep();
            return null;
        }

        return userAuth;
    }

    private static UserAccount findAccount(String targetUsername) {
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
}
