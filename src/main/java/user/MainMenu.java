package user;

import java.util.List;
import java.util.Scanner;

import department.Department;
import university.University;
import university.UniversityService;
import person.StudentService;
import person.TeacherService;
import faculty.FacultyService;
import department.DepartmentService;
import speciality.SpecialityService;
import utils.FileStorageUtils;
import utils.ModStatisticsUtils;
import utils.input.InputUtils;
import utils.ModEntitiesUtils;
import person.ModStudentUtils;
import person.ModTeacherUtils;
import faculty.ModFacultyUtils;
import speciality.ModSpecialityUtils;
import department.ModDepartmentUtils;
import faculty.Faculty;

public class MainMenu {

    public static void showMenu(UniversityService universityService, StudentService studentService,
                                TeacherService teacherService, FacultyService facultyService,
                                DepartmentService departmentService, SpecialityService specialityService,
                                UserService userService, Scanner scanner, User currentUser, University university,
                                List<Faculty> faculties) {

        boolean canWrite = currentUser.hasPermission(Permission.WRITE);
        boolean isAdmin = currentUser.hasPermission(Permission.ADMIN);

        System.out.println("\n--- DigiUni (Main Menu) ---");
        System.out.println("Role: " + currentUser.getRole() + " | Username: " + currentUser.getUsername());

        System.out.println("1. " + (canWrite ? "Work with Faculties" : "Show Faculties"));
        System.out.println("2. " + (canWrite ? "Work with Departments" : "Show Departments"));
        System.out.println("3. " + (canWrite ? "Work with Specialities" : "Show Specialities"));

        if (canWrite) {
            System.out.println("4. Work with Students");
            System.out.println("5. Work with Teachers");
            System.out.println("6. Search");
            System.out.println("7. University Statistics");
        } else {
            System.out.println("4. Search Students and Teachers");
            System.out.println("5. University Statistics");
        }

        if (isAdmin) {
            System.out.println("8. Work with Users");
        }

        System.out.println("0. Log out");
        System.out.print("> ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> {
                if (canWrite) {
                    ModFacultyUtils.showFacultiesMenu(scanner, facultyService, teacherService, currentUser, userService);
                } else {
                    ModEntitiesUtils.showAllEntity(scanner, facultyService.getFaculties(), "Faculty", false);
                }
            }
            case "2" -> {
                if (canWrite) {
                    ModDepartmentUtils.showDepartmentMenu(scanner, departmentService, facultyService, teacherService, userService);
                } else {
                    java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
                    if (optFaculty.isEmpty()) {
                        System.out.println("Faculty wasn't chosen or found");
                        break;
                    }
                    Faculty selectedFaculty = optFaculty.get();

                    ModEntitiesUtils.showAllEntity(scanner, selectedFaculty.getDepartments(), "Department", false);

                    int options = InputUtils.readInt(scanner, "Press the number to see department details or 0 to return: ", 0, selectedFaculty.getDepartments().size());

                    if (options == 0) {
                        break;
                    } else {
                        Department selectedDept = selectedFaculty.getDepartments().get(options - 1);
                        ModDepartmentUtils.showDepartmentDetails(selectedDept, selectedFaculty, teacherService);
                        InputUtils.pause(scanner);
                    }
                }
            }
            case "3" -> {
                if (canWrite) {
                    ModSpecialityUtils.showSpecialityMenu(scanner, specialityService, facultyService, userService);
                } else {
                    java.util.Optional<Faculty> optFaculty2 = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
                    if (optFaculty2.isEmpty()) {
                        System.out.println("Faculty wasn't chosen or found");
                        return;
                    }
                    ModEntitiesUtils.showAllEntity(scanner, optFaculty2.get().getSpeciality(), "Speciality", false);
                }
            }
            case "4" -> {
                if (canWrite) {
                    ModStudentUtils.showStudentMenu(scanner, studentService, facultyService, userService, true, university);
                } else {
                    System.out.println("1. Find Student");
                    System.out.println("2. Find Teacher");
                    int searchType = InputUtils.readInt(scanner, "> ", 1, 2);
                    if (searchType == 1) {
                        ModStudentUtils.searchStudentMenu(scanner, studentService, facultyService, universityService, false);
                    } else if (searchType == 2) {
                        ModTeacherUtils.searchTeacherMenu(scanner, teacherService, facultyService, universityService, false);
                    }
                }
            }
            case "5" -> {
                if (canWrite) {
                    ModTeacherUtils.showTeacherMenu(scanner, teacherService, facultyService, userService, true, university);
                } else {
                    ModStatisticsUtils.showStatisticsMenu(scanner, university, studentService, teacherService, specialityService, faculties);
                }
            }
            case "6" -> {
                if (canWrite) {
                    System.out.println("1. Find Student");
                    System.out.println("2. Find Teacher");
                    int searchType = InputUtils.readInt(scanner, "> ", 1, 2);
                    if (searchType == 1) {
                        ModStudentUtils.searchStudentMenu(scanner, studentService, facultyService, universityService, true);
                    } else if (searchType == 2) {
                        ModTeacherUtils.searchTeacherMenu(scanner, teacherService, facultyService, universityService, true);
                    }
                } else {
                    System.out.println("Invalid choice.");
                }
            }
            case "7" ->{
                if (canWrite) {
                    ModStatisticsUtils.showStatisticsMenu(scanner, university, studentService, teacherService, specialityService, faculties);
                } else {
                    System.out.println("Invalid choice.");
                }

            }
            case "8" -> {
                if (isAdmin) {
                    ModUserUtils.showUserMenu(scanner, userService, university, universityService);
                } else {
                    System.out.println("Invalid choice.");
                }
            }
            case "0" -> {
                FileStorageUtils.saveAll(university, userService);
                userService.logout();
            }
            default -> System.out.println("Invalid.");
        }
    }
}
