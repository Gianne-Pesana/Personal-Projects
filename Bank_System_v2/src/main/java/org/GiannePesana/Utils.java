package org.GiannePesana;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Utils {
    public static void threadSleep()  {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Error!");
            throw new RuntimeException(e);
        }
    }

    public static void threadSleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pressAnyKeyToContinue() {
        System.out.println("\n\nPress any key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static String formatString(String input) {
        input = input.trim();
        if(!input.contains(" ")) {
            return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
        }

        return input.substring(0, 1).toUpperCase()
                + input.substring(1, input.indexOf(" ")).toLowerCase()
                + " "
                + formatString(input.substring(input.indexOf(" ") + 1));
    }

    public static String invalidInputMessage() {
        return "Invalid Input!";
    }

    public static String getDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();

        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(date) + "," + dateTime.format(time);
    }
}
