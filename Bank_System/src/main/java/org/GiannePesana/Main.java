package org.GiannePesana;

public class Main {
    public static void main(String[] args) {
        if (args != null && args.length > 0 && args[0].equals("admin")) {
//            AdminController.authPage();
        }
        UserController.authPage();
    }
}