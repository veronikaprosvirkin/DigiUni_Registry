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
            System.out.println("Login successful! Hello "+ login);
        }
        else {
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
    //return current user to main
    public User getCurrentUser(){
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}
