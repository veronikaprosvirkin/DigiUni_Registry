import java.util.Scanner;

import javafx.application.Platform;
import repository.TeacherRepository;
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
        Platform.startup(() -> {});
        Platform.setImplicitExit(false);

        University university = new University();
        UserService userService = UserService.getInstance();

        FacultyService facultyService = new FacultyService(university);
        SpecialityService specialityService = new SpecialityService(university);
        TeacherRepository teacherRepository = new TeacherRepository(university);

        boolean hasSavedStructure = FileStorageUtils.hasSavedStructure();

        if (hasSavedStructure)
            FileStorageUtils.loadAll(university, facultyService, specialityService, userService);

        UniversityService universityService = new UniversityService(university, teacherRepository);

        if (!hasSavedStructure)
            FileStorageUtils.saveAll(university, userService, universityService);

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
                user.MainMenu.showMenu(scanner, currentUser);

            } catch (utils.EntityNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}