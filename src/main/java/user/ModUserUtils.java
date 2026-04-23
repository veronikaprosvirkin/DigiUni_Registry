package user;

import java.util.List;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

import utils.input.InputUtils;

// NETWORK IMPORTS
import service.NetworkClient;
import service.Request;
import service.Response;

public class ModUserUtils {

    // Notice: Removed UserService, University, UniversityService!
    public static void showUserMenu(Scanner scanner) {
        System.out.println("1. Add User");
        System.out.println("2. Delete User");
        System.out.println("3. Edit User's role");
        System.out.println("4. Show all");
        System.out.println("0. Back");

        int workWithUser = InputUtils.readInt(scanner, "> ", 0, 4);

        if (workWithUser == 1) {
            addUser(scanner);
        } else if (workWithUser == 2) {
            deleteUser(scanner);
        } else if (workWithUser == 3) {
            editUserRole(scanner);
        } else if (workWithUser == 4) {
            showAllUsers(scanner);
        }
    }

    // show menu for roles
    private static Role chooseRole(Scanner scanner) {
        System.out.println("Choose role: ");
        System.out.println("1. Admin");
        System.out.println("2. Manager");
        System.out.println("3. User");
        int roleChoice = InputUtils.readInt(scanner, "> ", 1, 3);
        return switch (roleChoice) {
            case 1 -> Role.ADMIN;
            case 2 -> Role.MANAGER;
            case 3 -> Role.USER;
            default -> Role.USER;
        };
    }

    // ===== WORK WITH USERS (CLIENT) ===== //

    // add user
    private static void addUser(Scanner scanner) {
        String username = InputUtils.readLine(scanner, "Enter username: ", true, true);
        String password = InputUtils.readLine(scanner, "Enter password: ", true, true);
        Role selectedRole = chooseRole(scanner);

        // NETWORK CALL
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("role", selectedRole.name()); // Передаємо роль як текст (String)

        Response res = NetworkClient.sendRequest(new Request("REGISTER_USER", data));
        System.out.println(res.getMessage());
    }

    // delete user
    private static void deleteUser(Scanner scanner) {
        String username = InputUtils.readLine(scanner, "Enter username of user you want to delete: ", true, true);

        // NETWORK CALL
        Response res = NetworkClient.sendRequest(new Request("DELETE_USER", username));
        System.out.println(res.getMessage());
    }

    // edit user role
    private static void editUserRole(Scanner scanner) {
        String username = InputUtils.readLine(scanner, "Enter username of user you want to edit: ", true, true);
        Role selectedRole = chooseRole(scanner);

        // NETWORK CALL
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("role", selectedRole.name()); // Передаємо роль як текст (String)

        Response res = NetworkClient.sendRequest(new Request("EDIT_USER_ROLE", data));
        System.out.println(res.getMessage());
    }

    // show all users
    private static void showAllUsers(Scanner scanner) {
        // NETWORK CALL
        Response res = NetworkClient.sendRequest(new Request("GET_ALL_USERS"));

        if (res.isSuccess() && res.getData() != null) {
            @SuppressWarnings("unchecked")
            List<User> users = (List<User>) res.getData();

            if (users.isEmpty()) {
                System.out.println("No users found!");
            } else {
                System.out.println("--- Users List ---");
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);
                    System.out.println((i + 1) + ". Username: " + u.getUsername() + " | Role: " + u.getRole());
                }
            }
        } else {
            System.out.println("Failed to load users: " + res.getMessage());
        }
        InputUtils.pause(scanner);
    }
}