import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserService {
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;

    public UserService() { //test users
        users.add(new User("admin", "admin", Role.ADMIN));
        users.add(new User("user", "user", Role.USER));
        users.add(new User("manager", "manager", Role.MANAGER));
    }

    // logging in process
    public static void login(Scanner scanner) {
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
    public static boolean loginSuccess(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public static List<User> getAllUsers() {
        return users;
    }

    //return current user to main
    public User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }

    //====WORK WITH USERS===
    //add user
    public static void registerNewUser(String username, String password, Role selectedRole) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username already exists. Please choose another one.");
                return;
            }
        }
        User newUser = new User(username, password, selectedRole);
        users.add(newUser);
        System.out.println("User registered successfully!");
    }

    //delete user
    public static void deleteUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getRole() != Role.ADMIN) {
                users.remove(user);
                System.out.println("User deleted successfully!");
                return;
            }
        }
        System.out.println("User not found!");
    }

    //edit user
    public static void editUser(String username, Role selectedRole) {
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
