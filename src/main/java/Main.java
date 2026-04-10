import java.util.Scanner;

import university.University;
import university.UniversityService;
import person.StudentService;
import person.TeacherService;
import faculty.FacultyService;
import department.DepartmentService;
import speciality.SpecialityService;
import user.UserService;
import user.User;
import utils.FileStorageUtils;


public class Main {
    public static void main(String[] args) {
        University university = new University();
        UserService userService = new UserService();

        FacultyService facultyService = new FacultyService(university);
        SpecialityService specialityService = new SpecialityService(university);

        boolean hasSavedStructure = FileStorageUtils.hasSavedStructure();

        if (hasSavedStructure) {
            // Load persisted state first so startup does not overwrite student/teacher CSVs.
            FileStorageUtils.loadAll(university, facultyService, specialityService);
        }

        UniversityService universityService = new UniversityService(university);

        if (!hasSavedStructure) {
            FileStorageUtils.saveAll(university, userService, universityService);
        }

        StudentService studentService = new StudentService(university);
        TeacherService teacherService = new TeacherService(university);
        DepartmentService departmentService = new DepartmentService(university);
        Scanner scanner = new Scanner(System.in);


        while (true) {
            try {
                //authorization logic
                User currentUser = userService.getCurrentUser();
                if (currentUser == null) {
                    userService.login(scanner);
                    continue;
                }
                user.MainMenu.showMenu(
                        universityService,
                        studentService,
                        teacherService,
                        facultyService,
                        departmentService,
                        specialityService,
                        userService,
                        scanner,
                        currentUser,
                        university);

            } catch (utils.EntityNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}