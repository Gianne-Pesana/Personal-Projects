package org.GiannePesana.User;

public class UserService {
    UserRepository repo;


    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    boolean authenticateUser(String username, String PIN) {
        boolean usernameValidated = false;
        boolean pinValidated = false;
        UserAccount user = repo.findByUsername(username);

        // Validate username
        if (user != null) {
            usernameValidated = true;
        }

        // Validate PIN
        if (user.getPin().equals(PIN)) {
            pinValidated = true;
        }

        return usernameValidated && pinValidated;
    }
}
