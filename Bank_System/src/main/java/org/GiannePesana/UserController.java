package org.GiannePesana;

import java.util.*;

public abstract class UserController {

    private static UserAccount user;

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
                "2.) Register\n\n" +
                "Enter your choice: ";
        Utils.clearConsole();
        System.out.print(menu);

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.next().toLowerCase();
        switch (choice) {
            case "1", "login" -> {
                Utils.clearConsole();
                userLogin();
            }
            case "2", "register" -> {
                Utils.clearConsole();
                userRegister();
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
                Utils.threadSleep(2000);
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
            UserService.updateUserData(user);
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
                            "Welcome, " + user.getFirstName() + " " + user.getLastName() + "!\n" +
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
                default -> {
                    System.out.println(Utils.invalidInputMessage());
                    Utils.threadSleep();
                }
            }
        }
    }

    private static void viewBalance() {
        System.out.println("Your current balance is: $" + user.getBalance());
    }

    private static void deposit() {
        double depositAmount = 0;
        do {
            Utils.clearConsole();
            System.out.println("======================\n" +
                                "Deposit\n" +
                                "======================");
            viewBalance();
            System.out.println(); // for formatting
            System.out.println("(Type -1 to cancel)");
            System.out.print("Enter amount to deposit: ");
            Scanner scanner = new Scanner(System.in);

            // Check if input is actually a number
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.next(); // Clear invalid input
                Utils.threadSleep();
                continue;
            }

            depositAmount = scanner.nextDouble();
            if (depositAmount == -1) return;

            if (depositAmount <= 0) {
                System.out.println("Invalid Amount!");
                Utils.threadSleep();
            }
        } while (depositAmount <= 0);

        double updatedBalance = user.getBalance() + depositAmount;
        user.setBalance(updatedBalance);

        UserService.updateUserData(user);
        TransactionsController.saveTransaction(TransactionsController.deposit, user, depositAmount);

        System.out.println();
        System.out.println(
                "$" + String.format("%,.2f ", depositAmount) +
                "has been deposited to your account."
        );
        Utils.threadSleep();

        Utils.clearConsole();
        TransactionsController.showReceipt();
        Utils.pressAnyKeyToContinue();
    }



    private static void withdraw() {
        double withdrawAmount = 0;


        while (true) {
            Utils.clearConsole();
            System.out.println("======================\n" +
                                "Withdraw\n" +
                                "======================");
            viewBalance();
            System.out.println(); // for formatting
            System.out.println("(Type -1 to cancel)");
            System.out.print("Enter amount to withdraw: ");
            Scanner scanner = new Scanner(System.in);

            // Check if input is actually a number
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.next(); // Clear invalid input
                Utils.threadSleep();
                continue;
            }

            withdrawAmount = scanner.nextDouble();

            if (withdrawAmount == -1) {
                return;
            }

            if (withdrawAmount > user.getBalance()) {
                System.out.println("Insufficient balance!");
                Utils.threadSleep();
                continue;
            }

            if (withdrawAmount <= 0) {
                System.out.println("Invalid amount.");
                Utils.threadSleep();
                continue;
            }

            break;
        }

        double updatedBalance = user.getBalance() - withdrawAmount;
        user.setBalance(updatedBalance);

        UserService.updateUserData(user);
        TransactionsController.saveTransaction(TransactionsController.withdraw, user, withdrawAmount);

        System.out.println();
        System.out.println(
                "$" + String.format("%,.2f ", withdrawAmount) +
                "has been withdrawn to your account."
        );

        Utils.threadSleep();

        Utils.clearConsole();
        TransactionsController.showReceipt();
        Utils.pressAnyKeyToContinue();
    }

    private static void userRegister() {
        Scanner scanner = new Scanner(System.in);
        String firstName = "";
        String lastName = "";
        String ageStr = "";
        int age = 0;
        String username = "";
        String pin;

        while (true) {
            Utils.clearConsole();
            if (!firstName.isEmpty())
                System.out.println("Enter First Name: " + firstName);
            if (!lastName.isEmpty())
                System.out.println("Enter Last Name: " + lastName);
            if (!ageStr.isEmpty())
                System.out.println("Enter your age: " + age);
            if (!username.isEmpty())
                System.out.println("Enter username: " + username);

            // validate first name
            if (firstName.isEmpty()) {
                System.out.print("Enter First Name: ");
                firstName = scanner.nextLine();

                if (UserService.equalSentinel(firstName)) return;
                if (!firstName.matches("[a-zA-Z]+")) {
                    System.out.println("Invalid name!");
                    firstName = "";
                    Utils.threadSleep();
                    Utils.clearConsole();
                    continue;
                }
            }

            // validate last name
            if (lastName.isEmpty()) {
                System.out.print("Enter Last Name: ");
                lastName = scanner.nextLine();

                if (UserService.equalSentinel(lastName)) return;

                if (!lastName.matches("[a-zA-Z]+")) {
                    System.out.println("Invalid name!");
                    lastName = "";
                    Utils.threadSleep();
                    Utils.clearConsole();
                    continue;
                }
            }

            // validate age
            if (ageStr.isEmpty()) {
                System.out.print("Enter Age: ");
                ageStr = scanner.nextLine();
                try {
                    age = Integer.parseInt(ageStr);

                    if (UserService.equalSentinel(ageStr)) return;

                    if (age < 18) {
                        System.out.println("Age must be 18 or above.");
                        ageStr = "";
                        Utils.threadSleep(2000);
                        Utils.clearConsole();
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Input! Please enter a number.");
                    ageStr = "";
                    Utils.threadSleep();
                    Utils.clearConsole();
                    continue;
                }
            }

            // validate username
            if (username.isEmpty()) {
                System.out.print("Enter Username: ");
                username = scanner.nextLine();
                if (UserService.equalSentinel(username)) return;

                if (username.isEmpty()) {
                    System.out.println("Invalid Input! Please enter a value.");
                    username = "";
                    Utils.threadSleep(2000);
                    Utils.clearConsole();
                    continue;
                }

                if (UserService.usernameExists(username)) {
                    System.out.println("Username already exists! Please try again.");
                    username = "";
                    Utils.threadSleep(2000);
                    Utils.clearConsole();
                    continue;
                }
            }

            // validate pin
            System.out.print("Enter PIN: ");
            pin = scanner.nextLine();

            if (pin.isEmpty()) {
                System.out.println("Invalid Input! Please enter a value.");
                Utils.threadSleep();
                Utils.clearConsole();
                continue;
            }

            if(pin.length() != 4) {
                System.out.println("PIN must be 4-digits");
                Utils.threadSleep(2000);
                Utils.clearConsole();
                continue;
            }

            break;
        }

        String userID = UserService.generateUserID();
        double balance = 0.0;
        String status = UserService.statusIntToString(UserService.pending);

        UserAccount user = new UserAccount(firstName, lastName, age, username, pin, userID, balance, status);
        UserService.createAccount(user);

        System.out.println();
        System.out.println("Account has been created successfully.");
        System.out.println("Please wait for approval by the administrator.");
        Utils.threadSleep(3000);
    }
}