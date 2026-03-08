import java.util.List;
import java.util.Scanner;

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
                ModEntitiesUtils.showAllEntity(scanner, facultyService.getFaculties(), "Faculty");
            }


            case "2" -> {   //? Show departments
                Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
                if (selectedFaculty == null) break;
                ModEntitiesUtils.showAllEntity(scanner, selectedFaculty.getDepartments(), "Department");

            }
            case "3" -> {   //? Show specialities
                Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
                if (selectedFaculty == null) break;
                ModEntitiesUtils.showAllEntity(scanner, selectedFaculty.getSpeciality(), "Speciality" );

            }

            case "4" -> {   //? search
                System.out.println("1. Find Student: ");
                System.out.println("2. Find Teacher: ");
                int searchType = InputUtils.readInt(scanner, "> ", 1, 2);
                if (searchType == 1) { //? Find Student
                    System.out.println("1. Search by full name");
                    System.out.println("2. Search by group number");
                    System.out.println("3. Search by course");
                    System.out.println("4. Search by speciality");
                    System.out.println("5. Show all students");
                    System.out.println("0. Back: ");
                    int searchBy = InputUtils.readInt(scanner, "> ", 0, 5);

                    if (searchBy == 1) { //by full name
                        SearchUtils.searchStudentByName(scanner, studentService);
                    } else if (searchBy == 2) { //by group number
                        System.out.println("1. Find in specific speciality");
                        System.out.println("2. Find in all university");
                        int type = InputUtils.readInt(scanner, "> ", 1, 2);

                        if (type == 1) {    // Search in specific speciality
                            SearchUtils.searchStudentByGroupSpecific(scanner, facultyService, studentService);
                        } else {      // Search in all university
                            SearchUtils.searchStudentByGroupEverywhere(scanner, studentService);
                        }
                    } else if (searchBy == 3) { //by course
                        SearchUtils.searchStudentByCourse(scanner, studentService);
                    } else if (searchBy == 4) { // by speciality
                        SearchUtils.searchStudentBySpeciality(scanner, studentService,facultyService);
                    } else if (searchBy == 5) {
                        List<Student> students = studentService.getAllStudents();
                        ModEntitiesUtils.showAllEntity(scanner, students, "Students List");
                    }

                } else if (searchType == 2) { //? Find Teacher
                    System.out.println("1. Search by full name");
                    System.out.println("2. Search by department");
                    System.out.println("3. Search by position");
                    System.out.println("4. Show all teachers");
                    System.out.println("0. Back: ");

                    int searchBy = InputUtils.readInt(scanner, "> ", 0, 4);
                    if (searchBy == 1) {
                        SearchUtils.searchTeacherByName(scanner, teacherService);
                    } else if (searchBy == 2) {
                        SearchUtils.searchTeacherByDepartment(scanner, universityService);
                    } else if (searchBy == 3) {
                        SearchUtils.searchTeacherByPosition(scanner, universityService);
                    } else if (searchBy == 4) {
                        List<Teacher> teachers = teacherService.getAllTeachers();
                        ModEntitiesUtils.showAllEntity(scanner, teachers, "Teachers List");
                    }
                }

            }
            case "0" -> {
                UserService.logout();
            }     //? Log out
            default -> System.out.println("Invalid.");  //? Incorrect input
        }
    }
}
