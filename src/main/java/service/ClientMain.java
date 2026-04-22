package service;

import javafx.application.Platform;
import user.LoginCredentials;
import user.User;
import user.MainMenu;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        Platform.startup(() -> {});
        Platform.setImplicitExit(false);

        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Welcome to DigiUni Client ---");

        User currentUser = null;

        while (true) {
            if (currentUser == null) {
                System.out.println("Please log in.");
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                // Send login request to Server
                Request loginReq = new Request("LOGIN", new LoginCredentials(username, password));
                Response res = NetworkClient.sendRequest(loginReq);

                if (res.isSuccess() && res.getData() != null) {
                    currentUser = (User) res.getData();
                    System.out.println("Login successful! Welcome, " + currentUser.getUsername());
                } else {
                    System.out.println("Login failed: " + res.getMessage());
                    continue;
                }
            }

            MainMenu.showMenu(scanner, currentUser);

            currentUser = null;
        }
    }
}