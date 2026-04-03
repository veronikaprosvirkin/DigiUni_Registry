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
import utils.EntityNotFoundException;
import person.ModStudentUtils;
import person.ModTeacherUtils;
import faculty.Faculty;
import department.Department;
import speciality.Speciality;

public class UserRights {
    public static void showUserRights(UniversityService universityService, StudentService studentService, TeacherService teacherService, FacultyService facultyService, DepartmentService departmentService, SpecialityService specialityService, Scanner scanner) {
        System.out.println("\n--- DigiUni (User access) ---");
        System.out.println("1. Show Faculties"); // finished
        System.out.println("2. Show Departments"); //finished
        System.out.println("3. Show Specialities"); //finished
        System.out.println("4. Search Students and Teachers");
        System.out.println("0. Log out");
        System.out.print("> ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> {    //? Show faculties
                ModEntitiesUtils.showAllEntity(scanner, facultyService.getFaculties(), "Faculty", false);
            }
            case "2" -> {   //? Show departments
                Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty")
                        .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't chosen or found"));

                ModEntitiesUtils.showAllEntity(scanner, selectedFaculty.getDepartments(), "Department", false);

            }
            case "3" -> {   //? Show specialities
                Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty")
                        .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't chosen or found"));
                ModEntitiesUtils.showAllEntity(scanner, selectedFaculty.getSpeciality(), "Speciality", false);

            }

            case "4" -> {   //? search
                System.out.println("1. Find Student");
                System.out.println("2. Find Teacher");
                int searchType = InputUtils.readInt(scanner, "> ", 1, 2);
                if (searchType == 1) { //? Find Student
                    ModStudentUtils.searchStudentMenu(scanner,studentService,facultyService,universityService, false);
                } else if (searchType == 2) { //? Find Teacher
                    ModTeacherUtils.searchTeacherMenu(scanner,teacherService,facultyService,universityService, false);
                }
            }
            case "0" -> {
                UserService.logout();
            }     //? Log out
            default -> System.out.println("Invalid.");  //? Incorrect input
        }
    }
}
