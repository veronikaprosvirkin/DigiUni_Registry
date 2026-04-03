import java.time.LocalDate;
import java.util.List;
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
import user.Role;
import user.UserRights;
import user.ManagerRights;
import user.AdminRights;
import person.StudyForm;
import department.Department;



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
        studentService.addStudent("Zbyshek", "Tymekowskych", "sm", LocalDate.of(2025, 9, 1), 101, StudyForm.BUDGET);
        studentService.addStudent("Irzek", "Zlotych", "sm", LocalDate.of(2024, 9, 1), 101, StudyForm.BUDGET);
        studentService.addStudent("Irzek", "Tymekowskych", "sm", LocalDate.of(2023, 9, 1), 15,StudyForm.CONTRACT);

        Department defaultDept = university.getFaculties().get(0).getDepartments().get(0);


        while (true) {
            try {
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
                } else if (currentUser.getRole() == Role.ADMIN) {
                    AdminRights.showAdminRights(universityService, studentService, teacherService, facultyService, departmentService, specialityService, scanner);
                }
            } catch (utils.EntityNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}