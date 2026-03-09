import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        University university = new University();
        UniversityService universityService = new UniversityService(university);
        StudentService studentService = new StudentService(university);
        TeacherService teacherService = new TeacherService(university);
        FacultyService facultyService = new FacultyService(university);
        DepartmentService departmentService = new DepartmentService(university);
        SpecialityService specialityService = new SpecialityService(university);
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();

        // Creating few students
        studentService.addStudent("Zbyshek", "Tymekowskych", LocalDate.of(2025, 9, 1), 101);
        studentService.addStudent("Irzek", "Zlotych", LocalDate.of(2024, 9, 1), 101);
        studentService.addStudent("Irzek", "Tymekowskych", LocalDate.of(2023, 9, 1), 15);

        while (true) {
            //authorization logic
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                UserService.login(scanner);
                continue;
            }

            if (currentUser.getRole()==Role.USER) {
                UserRights.showUserRights(universityService, studentService, teacherService, facultyService, departmentService, specialityService, scanner);
            } else if (currentUser.getRole()==Role.MANAGER) {
                ManagerRights.showManagerRights(universityService, studentService, teacherService, facultyService, departmentService, specialityService, scanner);
            } else if (currentUser.getRole()==Role.ADMIN) {
                System.out.println("Not finished yet. Please choose another role.");
                UserService.logout();
            }
        }
    }





}