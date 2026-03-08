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
        studentService.addStudent("Zbyshek", "Tymekowskych", 1, 101);
        studentService.addStudent("Irzek", "Zlotych", 1, 101);
        studentService.addStudent("Irzek", "Tymekowskych", 2, 15);

        while (true) {
            //authorization logic
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                System.out.println("You are not logged in. Please log in first.");
                String login = InputUtils.readLine(scanner, "Login: ", false, true);
                String password = InputUtils.readLine(scanner, "Password: ", false, true);

                boolean isSuccess = userService.login(login, password);
                if (isSuccess) {
                    System.out.println("Login successful! Hello "+ login);
                }
                else {
                    System.out.println("Login failed. Please try again.");
                }
                continue;
            }

            if (currentUser.getRole()==Role.USER) {
                UserRights.showUserRights(universityService, studentService, teacherService, facultyService, departmentService, specialityService, scanner);
            } else if (currentUser.getRole()==Role.MANAGER) {
                ManagerRights.showManagerRights(universityService, studentService, teacherService, facultyService, departmentService, specialityService, scanner);
            }
        }
    }





}