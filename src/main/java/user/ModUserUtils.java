package user;

import java.util.List;
import java.util.Scanner;
import utils.input.InputUtils;
import user.Role;
import user.UserService;
import user.User;
import university.University;
import university.UniversityService;
import utils.FileStorageUtils;

public class ModUserUtils {

    // show menu for user
    public static void showUserMenu(Scanner scanner, UserService userService, University university, UniversityService universityService) {
        System.out.println("1. Add User");
        System.out.println("2. Delete User");
        System.out.println("3. Edit User's role");
        System.out.println("4. Show all");
        System.out.println("0. Back");
        int workWithUser = InputUtils.readInt(scanner, "> ", 0, 4);
        if (workWithUser == 1) {
            addUser(scanner, userService, university, universityService);
        }
        else if (workWithUser == 2){
            deleteUser(scanner, userService, university, universityService);
        }
        else if (workWithUser == 3){
            editUserRole(scanner, userService, university, universityService);
        }
        else if (workWithUser == 4){
            showAllUsers(scanner, userService);
        }
    }

    //show menu for roles
    private static Role chooseRole(Scanner scanner) {
        System.out.println("Choose role: ");
        System.out.println("1. Admin");
        System.out.println("2. Manager");
        System.out.println("3. User");
        int roleChoice = InputUtils.readInt(scanner, "> ", 1, 3);
        return switch (roleChoice){
            case 1 -> Role.ADMIN;
            case 2 -> Role.MANAGER;
            case 3 -> Role.USER;
            default -> Role.USER;
        };
    }

    //=====WORK WITH USERS=====
    //add user
    private static void addUser(Scanner scanner, UserService userService, University university, UniversityService universityService) {
        String username = InputUtils.readLine(scanner, "Enter username: ", true, true);
        String password = InputUtils.readLine(scanner, "Enter password: ", true, true);
        Role selectedRole = chooseRole(scanner);
        userService.registerNewUser(username, password, selectedRole);
        FileStorageUtils.saveAll(university, userService, universityService);
    }
    //delete user
    private static void deleteUser(Scanner scanner, UserService userService, University university, UniversityService universityService) {
        String username = InputUtils.readLine(scanner, "Enter username of user you want to delete: ", true, true);
        userService.deleteUser(username);
        FileStorageUtils.saveAll(university, userService, universityService);
    }

    //edit user role
    private static void editUserRole(Scanner scanner, UserService userService, University university, UniversityService universityService) {
        String username = InputUtils.readLine(scanner, "Enter username of user you want to edit: ", true, true);
        Role selectedRole = ModUserUtils.chooseRole(scanner);
        userService.editUser(username, selectedRole);
        FileStorageUtils.saveAll(university, userService, universityService);
    }
    //show all users
    private static void showAllUsers(Scanner scanner, UserService userService) {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found!");
        } else {
            System.out.println("--- Users List ---");
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                System.out.println((i + 1) + ". Username: " + u.getUsername() + " | Role: " + u.getRole());
            }
        }
        InputUtils.pause(scanner);
    }




}
