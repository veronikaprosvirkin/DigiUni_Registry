package user;

import java.util.List;
import java.util.Scanner;
import university.UniversityService;
import person.StudentService;
import person.TeacherService;
import faculty.FacultyService;
import department.DepartmentService;
import speciality.SpecialityService;
import utils.input.InputUtils;
import utils.ModEntitiesUtils;
import person.ModStudentUtils;
import person.ModTeacherUtils;
import faculty.ModFacultyUtils;
import speciality.ModSpecialityUtils;
import department.ModDepartmentUtils;
import utils.sort.SortUtils;

public class ManagerRights {
    public static void showManagerRights(UniversityService universityService, StudentService studentService, TeacherService teacherService, FacultyService facultyService, DepartmentService departmentService, SpecialityService specialityService, Scanner scanner) {
        System.out.println("\n--- DigiUni (Manager access) ---");
        System.out.println("1. Work with Faculties"); // finished
        System.out.println("2. Work with Departments"); //finished
        System.out.println("3. Work with Specialities"); //finished
        System.out.println("4. Work with Students"); //logic written, not finished realization
        System.out.println("5. Work with Teachers");
        System.out.println("6. Search");
        System.out.println("0. Log out");
        System.out.print("> ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> { //? Work with faculties
               ModFacultyUtils.showFacultiesMenu(scanner, facultyService, teacherService);
            }
            case "2" -> {   //? Work with departments
                ModDepartmentUtils.showDepartmentMenu(scanner,departmentService,facultyService, teacherService);
            }
            case "3" -> {   //? Edit speciality
                ModSpecialityUtils.showSpecialityMenu(scanner, specialityService, facultyService);
            }
            case "4" -> {   //? Work with students
                ModStudentUtils.showStudentMenu(scanner,studentService,facultyService,universityService, true);
            }
            case "5" -> {   //? Work with teachers
                ModTeacherUtils.showTeacherMenu(scanner,teacherService,facultyService,universityService, true);
            }
            case "6" -> {   //? search
                System.out.println("1. Find Student");
                System.out.println("2. Find Teacher");
                int searchType = InputUtils.readInt(scanner, "> ", 1, 2);
                if (searchType == 1) { //? Find Student
                    ModStudentUtils.searchStudentMenu(scanner,studentService,facultyService,universityService, true);
                } else if (searchType == 2) { //? Find Teacher
                    ModTeacherUtils.searchTeacherMenu(scanner,teacherService,facultyService,universityService, true);
                }
            }
            case "0" -> {
                UserService.logout();
            }     //? Log out
            default -> System.out.println("Invalid.");  //? Incorrect input
        }
    }
}
