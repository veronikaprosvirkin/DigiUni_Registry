package user;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import department.Department;
import speciality.Speciality;
import utils.input.InputUtils;
import utils.ModEntitiesUtils;
import person.ModStudentUtils;
import person.ModTeacherUtils;
import faculty.ModFacultyUtils;
import speciality.ModSpecialityUtils;
import department.ModDepartmentUtils;
import university.ModUniversityUtils;
import utils.ModStatisticsUtils;
import faculty.Faculty;


import service.NetworkClient;
import service.Request;
import service.Response;

public class MainMenu {

    public static void showMenu(Scanner scanner, User currentUser) {

        boolean canWrite = currentUser.hasPermission(Permission.WRITE);
        boolean isAdmin = currentUser.hasPermission(Permission.ADMIN);

        System.out.println("\n--- DigiUni (Main Menu) ---");
        System.out.println("Role: " + currentUser.getRole() + " | Username: " + currentUser.getUsername());

        System.out.println("1. " + (isAdmin ? "Work with University" : "Show University Profile"));
        System.out.println("2. " + (canWrite ? "Work with Faculties" : "Show Faculties"));
        System.out.println("3. " + (canWrite ? "Work with Departments" : "Show Departments"));
        System.out.println("4. " + (canWrite ? "Work with Specialities" : "Show Specialities"));

        if (canWrite) {
            System.out.println("5. Work with Students");
            System.out.println("6. Work with Teachers");
            System.out.println("7. Search");
            System.out.println("8. University Statistics");
        } else {
            System.out.println("5. Search Students and Teachers");
            System.out.println("6. University Statistics");
        }

        if (isAdmin) {
            System.out.println("9. Work with Users");
        }

        System.out.println("0. Log out");
        System.out.print("> ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> {
                if (isAdmin) {
                    System.out.println("\n--- Work with University ---");
                    System.out.println("1. Show University Profile");
                    System.out.println("2. Edit University Settings");
                    System.out.println("0. Back");

                    int uniChoice = InputUtils.readInt(scanner, "> ", 0, 2);
                    if (uniChoice == 1) {
                        ModUniversityUtils.showUniversityProfile(scanner);
                    } else if (uniChoice == 2) {
                        ModUniversityUtils.editUniversityMenu(scanner);
                    }
                } else {
                    ModUniversityUtils.showUniversityProfile(scanner);
                }
            }
            case "2" -> {
                if (canWrite) {
                    ModFacultyUtils.showFacultiesMenu(scanner, currentUser);
                } else {

                    Response res = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
                    if (res.isSuccess() && res.getData() != null) {
                        @SuppressWarnings("unchecked")
                        List<Faculty> faculties = (List<Faculty>) res.getData();

                        ModEntitiesUtils.showAllEntity(scanner, faculties, "Faculty", false);

                        if (!faculties.isEmpty()) {
                            int options = InputUtils.readInt(scanner, "Press the number to see faculty details or 0 to return: ", 0, faculties.size());
                            if (options != 0) {
                                Faculty selectedFaculty = faculties.get(options - 1);
                                ModFacultyUtils.showFacultiesDetails(selectedFaculty, scanner);
                            }
                        }
                    } else {
                        System.out.println("Failed to load faculties from server.");
                    }
                }
            }
            case "3" -> {
                if (canWrite) {
                    ModDepartmentUtils.showDepartmentMenu(scanner, currentUser);
                } else {
                    // NETWORK CALL: Fetch faculties first to select one
                    Response res = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
                    if (res.isSuccess() && res.getData() != null) {
                        @SuppressWarnings("unchecked")
                        List<Faculty> faculties = (List<Faculty>) res.getData();

                        Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, faculties, "Faculty");
                        if (optFaculty.isEmpty()) {
                            System.out.println("Faculty wasn't chosen or found");
                            break;
                        }
                        Faculty selectedFaculty = optFaculty.get();

                        ModEntitiesUtils.showAllEntity(scanner, selectedFaculty.getDepartments(), "Department", false);

                        int options = InputUtils.readInt(scanner, "Press the number to see department details or 0 to return: ", 0, selectedFaculty.getDepartments().size());

                        if (options != 0) {
                            Department selectedDept = selectedFaculty.getDepartments().get(options - 1);
                            ModDepartmentUtils.showDepartmentDetails(selectedDept, selectedFaculty);
                            InputUtils.pause(scanner);
                        }
                    }
                }
            }
            case "4" -> {
                if (canWrite) {
                    ModSpecialityUtils.showSpecialityMenu(scanner, currentUser);
                } else {
                    Response res = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
                    if (res.isSuccess() && res.getData() != null) {
                        @SuppressWarnings("unchecked")
                        List<Faculty> faculties = (List<Faculty>) res.getData();

                        Optional<Faculty> optFaculty2 = ModEntitiesUtils.selectEntity(scanner, faculties, "Faculty");
                        if (optFaculty2.isEmpty()) {
                            System.out.println("Faculty wasn't chosen or found");
                            break;
                        }
                        Faculty selectedFaculty = optFaculty2.get();
                        ModEntitiesUtils.showAllEntity(scanner, selectedFaculty.getSpeciality(), "Speciality", false);

                        int options = InputUtils.readInt(scanner, "Press the number to see speciality details or 0 to return: ", 0, selectedFaculty.getSpeciality().size());
                        if (options != 0) {
                            Speciality selectedSpec = selectedFaculty.getSpeciality().get(options - 1);
                            ModSpecialityUtils.showSpecialityDetails(selectedSpec);
                            InputUtils.pause(scanner);
                        }
                    }
                }
            }
            case "5" -> {
                if (canWrite) {
                    ModStudentUtils.showStudentMenu(scanner, true);
                } else {
                    System.out.println("1. Find Student");
                    System.out.println("2. Find Teacher");
                    int searchType = InputUtils.readInt(scanner, "> ", 1, 2);
                    if (searchType == 1) {
                        ModStudentUtils.searchStudentMenu(scanner, false);
                    } else if (searchType == 2) {
                        ModTeacherUtils.searchTeacherMenu(scanner, false);
                    }
                }
            }
            case "6" -> {
                if (canWrite) {
                    ModTeacherUtils.showTeacherMenu(scanner, true);
                } else {
                    ModStatisticsUtils.showStatisticsMenu(scanner);
                }
            }
            case "7" -> {
                if (canWrite) {
                    System.out.println("1. Find Student");
                    System.out.println("2. Find Teacher");
                    int searchType = InputUtils.readInt(scanner, "> ", 1, 2);
                    if (searchType == 1) {
                        ModStudentUtils.searchStudentMenu(scanner, true);
                    } else if (searchType == 2) {
                        ModTeacherUtils.searchTeacherMenu(scanner, true);
                    }
                } else {
                    System.out.println("Invalid choice.");
                }
            }
            case "8" -> {
                if (canWrite) {
                    ModStatisticsUtils.showStatisticsMenu(scanner);
                } else {
                    System.out.println("Invalid choice.");
                }
            }
            case "9" -> {
                if (isAdmin) {
                    ModUserUtils.showUserMenu(scanner);
                } else {
                    System.out.println("Invalid choice.");
                }
            }
            case "0" -> {
                System.out.println("Logging out...");
                NetworkClient.sendRequest(new Request("LOGOUT"));
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    }
}