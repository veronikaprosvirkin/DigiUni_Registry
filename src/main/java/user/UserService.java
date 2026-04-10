package user;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import lombok.Getter;
import utils.input.InputUtils;

public class UserService {
    private static final UserService INSTANCE = new UserService();
    private final List<User> users = new ArrayList<>();
    @Getter
    private User currentUser = null;

    private UserService() {
        seedDefaultUsers();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public static UserService createTestInstance() {
        return new UserService();
    }

    private void seedDefaultUsers() {
        users.add(new User("admin", "admin", Role.ADMIN, Permission.getDefaultMaskForRole(Role.ADMIN)));
        users.add(new User("user", "user", Role.USER, Permission.getDefaultMaskForRole(Role.USER)));
        users.add(new User("manager", "manager", Role.MANAGER, Permission.getDefaultMaskForRole(Role.MANAGER)));
    }

    // logging in process
    public void login(Scanner scanner) {
        System.out.println("You are not logged in. Please log in first.");
        String login = InputUtils.readLine(scanner, "Login: ", false, true);
        String password = InputUtils.readLine(scanner, "Password: ", false, true);

        boolean isSuccess = loginSuccess(login, password);
        if (isSuccess) {
            System.out.println("Login successful! Hello " + login);
        } else {
            System.out.println("Login failed. Please try again.");
        }
    }

    // method for login
    public boolean loginSuccess(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    public void addUserFromStorage(User user) {
        users.add(user);
    }


    public void logout() {
        currentUser = null;
    }

    //====WORK WITH USERS===
    //add user
    public void registerNewUser(String username, String password, Role selectedRole) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username already exists. Please choose another one.");
                return;
            }
        }
        User newUser = new User(username, password, selectedRole, Permission.getDefaultMaskForRole(selectedRole));
        users.add(newUser);
        System.out.println("User registered successfully!");
    }

    //delete user
    public void deleteUser(String username) {
        boolean removed = users.removeIf(user -> user.getUsername().equals(username) && user.getRole() != Role.ADMIN);
        if (removed) {
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("User not found or is ADMIN!");
        }
    }

    //edit user
    public void editUser(String username, Role selectedRole) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (selectedRole == user.getRole()) {
                    System.out.println("User's role is already " + selectedRole.toString());
                    return;
                }
                user.setRole(selectedRole);
                System.out.println("User's role updated successfully!");
                return;
            }
        }
        System.out.println("User not found!");
    }
}

