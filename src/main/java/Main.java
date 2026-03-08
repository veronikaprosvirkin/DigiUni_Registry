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

            System.out.println("\n--- DigiUni (Manager access) ---");
            System.out.println("1. Work with Faculties"); // finished
            System.out.println("2. Work with Departments"); //finished
            System.out.println("3. Work with Specialities"); //finished
            System.out.println("4. Work with Students"); //logic written, not finished realization
            System.out.println("5. Work with Teachers");
            System.out.println("6. Search");
            System.out.println("0. Exit");
            System.out.print("> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> { // Work with faculties
                    System.out.println("1. Add Faculty");
                    System.out.println("2. Manage Existing Faculty");
                    System.out.println("0. Back");
                    int action = InputUtils.readInt(scanner, "> ", 0, 2);

                    if (action == 1) {
                        ModFacultyUtils.facultyAddFaculty(scanner, facultyService);
                    } else if (action == 2) { //manage existing faculties
                        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
                        if (selectedFaculty == null) break;
                        System.out.println("1. Edit Faculty");
                        System.out.println("2. Delete Faculty");
                        System.out.println("0. Back");
                        int workWithFaculty = InputUtils.readInt(scanner, "> ", 0, 2);
                        if (workWithFaculty == 1) { //edit faculty name
                            ModFacultyUtils.facultyManageExistingFacultyRename(scanner, facultyService, selectedFaculty);
                        } else if (workWithFaculty == 2) { //delete faculty
                            ModFacultyUtils.facultyManageExistingFacultyDelete(scanner, facultyService, selectedFaculty);
                        }
                    }
                }


                case "2" -> {// Work with departments
                    System.out.println("1. Add Department");
                    System.out.println("2. Manage existing Department");
                    System.out.println("0. Back");
                    int action = InputUtils.readInt(scanner, "> ", 0, 2);

                    if (action == 1) { // add a new department
                        ModDepartmentUtils.departmentAddDepartment(scanner, departmentService, facultyService);
                    } else if (action == 2) { // manage existing department
                        // Select Faculty and Department
                        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
                        if (selectedFaculty == null) break;
                        Department selectedDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Department");
                        if (selectedDept == null) break;

                        //Work with a selected department
                        System.out.println("\nDepartment: " + selectedDept.getName());
                        System.out.println("1. Edit name of the Department");
                        System.out.println("2. Delete Department");
                        System.out.println("3. Show all Teachers in the Department");
                        System.out.println("0. Back");
                        int workWithDepartment = InputUtils.readInt(scanner, "> ", 0, 3);

                        if (workWithDepartment == 1) { // edit department name
                            ModDepartmentUtils.departmentRenameDepartment(scanner, departmentService, selectedDept, selectedFaculty);
                        } else if (workWithDepartment == 2) { //delete department
                            ModDepartmentUtils.departmentDeleteDepartment(scanner, departmentService, selectedDept, selectedFaculty);
                        } else if (workWithDepartment == 3) { //show all teachers in the department
                            ModDepartmentUtils.departmentShowTeachers(teacherService, selectedDept, scanner);
                        }
                    }
                }
                case "3" -> {   //? Edit speciality
                    System.out.println("1. Add Speciality");
                    System.out.println("2. Manage existing Speciality");
                    System.out.println("0. Back");
                    int action = InputUtils.readInt(scanner, "> ", 0, 2);

                    if (action == 1) {
                        ModSpecialityUtils.specialityAddSpeciality(scanner, specialityService, facultyService);
                    } else if (action == 2) {
                        // Select Faculty and Speciality
                        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
                        if (selectedFaculty == null) break;
                        Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
                        if (selectedSpeciality == null) break;

                        System.out.println("1. Rename Speciality");
                        System.out.println("2. Delete Speciality");
                        System.out.println("0. Back");

                        int workWithSpeciality = InputUtils.readInt(scanner, "> ", 0, 2);
                        if (workWithSpeciality == 1) {
                            ModSpecialityUtils.specialityRenameSpeciality(scanner, specialityService, selectedSpeciality, selectedFaculty);
                        } else if (workWithSpeciality == 2) {
                            ModSpecialityUtils.specialityDeleteSpeciality(scanner, specialityService, selectedSpeciality, selectedFaculty);
                        }
                    }

                }
                case "4" -> {// Work with students
                    System.out.println("1. Add Student");
                    System.out.println("2. Delete Student");
                    System.out.println("3. Edit information about student");
                    System.out.println("4. Show all");
                    System.out.println("0. Back");

                    int workWithStudent = InputUtils.readInt(scanner, "> ", 0, 4);

                    if (workWithStudent == 1) { //add student
                        ModStudentUtils.studentAddStudent(scanner, facultyService, studentService);
                    } else if (workWithStudent == 2) { //delete student
                        int deleteStudent = ModEntitiesUtils.chooseDeleting(scanner);
                        if (deleteStudent == 1) {
                            String fullName = InputUtils.readLine(scanner, "Full name of student: ", false, false);
                            fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
                            List<Student> result = studentService.findStudentsByFullName(fullName);
                            ModEntitiesUtils.deleteEntity(scanner, result, "Student", (student -> studentService.deleteStudent(student, student.getSpeciality())));
                        } else if (deleteStudent == 2) {
                            ModStudentUtils.studentDeleteById(scanner, universityService);
                        }

                    } else if (workWithStudent == 3) { //edit student
                        int editStudent = ModEntitiesUtils.chooseEditing(scanner);
                        if (editStudent == 1) {
                            ModStudentUtils.studentEditByName(scanner, studentService);
                        } else if (editStudent == 2) {
                            ModStudentUtils.studentEditById(scanner, universityService);
                        }


                    } else if (workWithStudent == 4) {
                        List<Student> students = studentService.getAllStudents();//show all students
                        ModEntitiesUtils.showAllEntity(scanner, students, "Students List");
                    }

                }
                case "5" -> {
                    System.out.println("1. Add Teacher");
                    System.out.println("2. Delete Teacher");
                    System.out.println("3. Edit information about teacher");
                    System.out.println("4. Show all");
                    System.out.println("0. Back");
                    int workWithTeacher = InputUtils.readInt(scanner, "> ", 0, 4);

                    if (workWithTeacher == 1) { //add teacher
                        ModTeacherUtils.teacherAddTeacher(scanner, facultyService, teacherService);
                    } else if (workWithTeacher == 2) { //delete teacher
                        int deleteTeacher= ModEntitiesUtils.chooseDeleting(scanner);
                        if (deleteTeacher == 1) {
                            System.out.print("Delete teacher by full name ");
                            String fullName = InputUtils.readLine(scanner, "Full name of teacher: ", false, false);
                            fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
                            List<Teacher> result = teacherService.findTeachersByFullName(fullName);

                            ModEntitiesUtils.deleteEntity(scanner, result, "Teacher", (teacher -> teacherService.deleteTeacher(teacher, teacher.getDepartment()) ));
                        } else if (deleteTeacher == 2) {
                            ModTeacherUtils.teacherDeleteById(scanner, universityService);
                        }

                    } else if (workWithTeacher == 3) { //edit teacher
                        int editTeacher = ModEntitiesUtils.chooseEditing(scanner);
                        if (editTeacher == 1) {
                            ModTeacherUtils.teacherEditByName(scanner, universityService);
                        } else if (editTeacher == 2) {
                            ModTeacherUtils.teacherEditById(scanner, universityService);
                        }
                    } else if (workWithTeacher == 4) {//show all
                        List<Teacher> teachers = teacherService.getAllTeachers();
                        ModEntitiesUtils.showAllEntity(scanner, teachers, "Teachers List");
                    }
                }

                case "6" -> {   //search
                    System.out.println("1. Find Student: ");
                    System.out.println("2. Find Teacher: ");
                    int searchType = InputUtils.readInt(scanner, "> ", 1, 2);
                    if (searchType == 1) { //Find Student
                        System.out.println("1. Search by full name: ");
                        System.out.println("2. Search by group number: ");
                        System.out.println("3. Search by course: ");
                        System.out.println("4. Search by speciality: ");
                        System.out.println("0. Back: ");
                        int searchBy = InputUtils.readInt(scanner, "> ", 0, 4);

                        if (searchBy == 1) { //by full name
                            searchStudentByName(scanner, studentService);
                        } else if (searchBy == 2) { //by group number
                            System.out.println("1. Find in specific speciality");
                            System.out.println("2. Find in all university");
                            int type = InputUtils.readInt(scanner, "> ", 1, 2);

                            if (type == 1) {    // Search in specific speciality
                                searchStudentByGroupSpecific(scanner, facultyService, studentService);
                            } else {      // Search in all university
                                searchStudentByGroupEverywhere(scanner, studentService);
                            }
                        } else if (searchBy == 3) { //by course
                            searchStudentByCourse(scanner, studentService);
                        } else if (searchBy == 4) { // by speciality
                            searchStudentBySpeciality(scanner, studentService,facultyService);
                        }

                    } else if (searchType == 2) { //Find Teacher
                        System.out.println("1. Search by full name: ");
                        System.out.println("2. Search by department: ");
                        System.out.println("3. Search by position: ");
                        System.out.println("0. Back: ");

                        int searchBy = InputUtils.readInt(scanner, "> ", 0, 3);
                        if (searchBy == 1) {
                            searchTeacherByName(scanner, universityService);
                        } else if (searchBy == 2) {
                            searchTeacherByDepartment(scanner, universityService);
                        } else if (searchBy == 3) {
                            searchTeacherByPosition(scanner, universityService);
                        }
                    }

                }
                case "0" -> {
                    return;
                }     //? Stop the program
                default -> System.out.println("Invalid.");  //? Incorrect input
            }
        }
    }










    //! ======= SEARCH ===== //

    /**
     * Search Student by full name
     */
    private static void searchStudentByName(Scanner scanner, StudentService studentService) {
        String name = InputUtils.readLine(scanner, "Enter full name: ", false, false);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        List<Student> result = studentService.findStudentsByFullName(name);
        if (result.isEmpty()) {
            System.out.println("No students found with this name.");
        } else {
            System.out.println(" --- Students found by name part: " + name + " ---");
            result.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by group in specific Speciality
     */
    private static void searchStudentByGroupSpecific(Scanner scanner, FacultyService facultyService, StudentService studentService) {
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (selectedFaculty == null) return;

        Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
        if (selectedSpeciality == null) return;

        int groupNumber = InputUtils.readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

        List<Student> results = studentService.findStudentsInSpecialityByGroup(selectedSpeciality, groupNumber);

        if (results.isEmpty()) {
            System.out.println("No students found in group " + groupNumber + " within " + selectedSpeciality.getName());
        } else {
            System.out.println(" --- Students in group " + groupNumber + " on " + selectedSpeciality.getName() + " ---");
            results.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by group in the whole Univercity
     */
    private static void searchStudentByGroupEverywhere(Scanner scanner, StudentService studentService) {
        int groupNumber = InputUtils.readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

        List<Student> results = studentService.findStudentsByGroup(groupNumber);

        if (results.isEmpty()) {
            System.out.println("No students found in group " + groupNumber + " in the whole university.");
        } else {
            System.out.println(" --- Students in group " + groupNumber + " ---");
            results.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by course
     */
    private static void searchStudentByCourse(Scanner scanner, StudentService studentService) {
        int course = InputUtils.readInt(scanner, "Enter course number: ", 1, 6);
        List<Student> results = studentService.findStudentsByCourse(course);
        if (results.isEmpty()) {
            System.out.println("No students found in course " + course + ".");
        } else {
            System.out.println(" --- Students in course " + course + " ---");
            results.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by speciality
     */
    private static void searchStudentBySpeciality(Scanner scanner, StudentService studentService, FacultyService facultyService) {
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (selectedFaculty == null) return;

        Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
        if (selectedSpeciality == null) return;

        List <Student> result = studentService.findStudentsBySpeciality(selectedSpeciality);
        if (result.isEmpty()) {
            System.out.println("No students found in " + selectedSpeciality.getName() + ".");
        } else {
            System.out.println(" --- Students in " + selectedSpeciality.getName() + " ---");
            result.forEach(System.out::println);
        }
        InputUtils.pause(scanner);

    }

    /**
     * Search Teacher by full name
     */
    private static void searchTeacherByName(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED
    }

    /**
     * Search Teacher by department
     */
    private static void searchTeacherByDepartment(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED
    }

    /**
     * Search Teacher by position
     */
    private static void searchTeacherByPosition(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED
    }


    // * ===== METHODS HELPERS ===== * //



}