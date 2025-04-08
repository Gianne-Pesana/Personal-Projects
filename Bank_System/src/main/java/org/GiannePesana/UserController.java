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

    public static void authPage() {
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
                userLogin();
            }
            default -> System.out.println(Utils.invalidInputMessage());
        }
    }

    private static void userLogin() {
        Scanner scanner = new Scanner(System.in);
        boolean usernameAuth = false;
        boolean pinAuth = false;
        int pinAttempts = 3;

        String inputUsername = "";
        String inputPin = "";
        String targetPin = "";

        // username validation
        while(!usernameAuth) {
            Utils.clearConsole();
            System.out.println(titleHeader);
            System.out.print("Enter your username: ");
            inputUsername = scanner.nextLine();
            // handle status flags
            int accountStatus = UserService.authenticateUsername(inputUsername);

            if (accountStatus == UserService.suspended) {
                System.out.println(UserService.getUserStatusMessage(accountStatus));
                Utils.threadSleep(2000);
                return;
            } else if (accountStatus == UserService.active) {
                usernameAuth = true;
            } else {
                System.out.println(UserService.getUserStatusMessage(accountStatus));
            }
        }

        user = UserService.findAccount(inputUsername);
        targetPin = user.getPin();

        // pin validation
        while (pinAttempts > 0) {
            Utils.clearConsole();
            System.out.println(titleHeader);
            System.out.print("Enter your PIN: ");
            inputPin = scanner.nextLine();

            if(inputPin.equals(targetPin)) {
                pinAuth = true;
                break;
            }

            if (--pinAttempts > 0) {
                System.out.println("Invalid PIN. Attempts left: " + pinAttempts);
            }
            Utils.threadSleep();
        }

        if (!pinAuth) {
            System.out.println("Too many failed attempts. Account Suspended. \nExiting...");
            user.setStatus(UserService.statusIntToString(UserService.suspended));
            Utils.threadSleep(2000);
        }

        if (usernameAuth && pinAuth) userLandingPage();
    }

    private static void userLandingPage() {
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

        user.setBalance(user.getBalance() + depositAmount);
        UserService.updateUserData(user);
        TransactionsController.saveTransaction(TransactionsController.deposit, user, depositAmount);
        System.out.println(depositAmount + " has been deposited to your account.");
        Utils.threadSleep(2000);
    }

    private static void withdraw() {

    }
}