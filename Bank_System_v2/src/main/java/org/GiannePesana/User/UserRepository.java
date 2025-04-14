package org.GiannePesana.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserRepository {
    String filepath;

    public UserRepository(String filepath) {
        this.filepath = filepath;
    }

    public ArrayList<UserAccount> getAllUsers() {
        ArrayList <UserAccount> users = new ArrayList<>();

        try (Scanner inFile = new Scanner(new FileReader(this.filepath))) {
            String line;
            while (inFile.hasNextLine()) {
                line = inFile.nextLine();
                users.add(parseCSV(line));
            }
            return users;
        }
        catch (IOException e) {
            System.out.println("Something went wrong.");
        }
        return null;
    }

    public void saveAllUsers(ArrayList <UserAccount> users) {
        try (FileWriter writer = new FileWriter(filepath)) {
            for (UserAccount user : users) {
                writer.write(toCSV(user) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error in saving users: " + e.getMessage());
        }
    }

    public UserAccount findByUsername(String username) {
        ArrayList <UserAccount> users = getAllUsers();
        for (UserAccount user : users) {
            if (user.getUsername().equals(username))
                return user;
        }

        return null;
    }

    public void addUser(UserAccount newUser) {
        ArrayList <UserAccount> users = getAllUsers();
        users.add(newUser);

    }



    private UserAccount parseCSV(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 8) {
            System.out.println("Error in parsing CSV data.");
            return null;
        }
        return new UserAccount(
                parts[0], // User ID
                parts[1], // First Name
                parts[2], // Last Name
                parts[3], // Username
                parts[4], // pin
                Double.parseDouble(parts[5]), // Balance
                UserStatus.valueOf(parts[6]), // Status
                parts[7] // Creation Date
        );
    }

    private String toCSV(UserAccount user) {
        return user.getUserID() + "|" +
                user.getFirstName() + "|" +
                user.getLastName() + "|" +
                user.getUsername() + "|" +
                user.getPin() + "|" +
                user.getBalance() + "|" +
                user.getStatus().name() + "|" +
                user.getCreationDate();
    }
}
