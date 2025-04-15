package org.GiannePesana.User;

import org.GiannePesana.Utils;

import java.util.Scanner;

public class UserController {
    UserService service;
    public String titleHeader =
            "███████╗██╗  ██╗██╗   ██╗    ██████╗  █████╗ ███╗   ██╗██╗  ██╗\n" +
            "██╔════╝██║ ██╔╝╚██╗ ██╔╝    ██╔══██╗██╔══██╗████╗  ██║██║ ██╔╝\n" +
            "███████╗█████╔╝  ╚████╔╝     ██████╔╝███████║██╔██╗ ██║█████╔╝ \n" +
            "╚════██║██╔═██╗   ╚██╔╝      ██╔══██╗██╔══██║██║╚██╗██║██╔═██╗ \n" +
            "███████║██║  ██╗   ██║       ██████╔╝██║  ██║██║ ╚████║██║  ██╗\n" +
            "╚══════╝╚═╝  ╚═╝   ╚═╝       ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝\n" +
            "================================================================\n\n";

    public UserController(UserService service) {
        this.service = service;
    }

    public void showMainMenu() {
        String menu =
            titleHeader +
            "Choose operation:\n" +
            "1.) Login\n" +
            "2.) Register\n" +
            "3.) Exit\n\n" +
            "Enter your choice: ";

        System.out.println(menu);

        Scanner scanner = new Scanner(System.in);

        String choice = scanner.next().toLowerCase();
        switch (choice) {
            case "1","login" -> handleUserLogin();
            case "2", "register" -> handleUserRegister();
            case "3", "exit" -> System.exit(0);
            default -> System.out.println("Invalid Input!");

        }
    }

    private void handleUserLogin() {
        Scanner scanner = new Scanner(System.in);
        String username, PIN;
        boolean userValidated;
        do {
            Utils.clearConsole();
            System.out.println(titleHeader);

            System.out.print("Enter username: ");
            username = scanner.nextLine();

            System.out.print("Enter PIN: ");
            PIN = scanner.nextLine();

            if (username.equals("exit") || PIN.equals("exit")) {
                return;
            }

            userValidated = service.authenticateUser(username, PIN);
            if (!userValidated) {
                System.out.println("Invalid credentials. Please try again.");
                Utils.threadSleep(1500);
            }
        } while (!userValidated);

        scanner.close();

        user
    }

    private void handleUserRegister() {

    }


}
