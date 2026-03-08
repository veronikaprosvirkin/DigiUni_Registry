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
                        specialityAddSpeciality(scanner, specialityService, facultyService);
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
                            specialityRenameSpeciality(scanner, specialityService, selectedSpeciality, selectedFaculty);
                        } else if (workWithSpeciality == 2) {
                            specialityDeleteSpeciality(scanner, specialityService, selectedSpeciality, selectedFaculty);
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
                        studentAddStudent(scanner, facultyService, studentService);
                    } else if (workWithStudent == 2) { //delete student
                        int deleteStudent = ModEntitiesUtils.chooseDeleting(scanner);
                        if (deleteStudent == 1) {
                            String fullName = InputUtils.readLine(scanner, "Full name of student: ", false, false);
                            fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
                            List<Student> result = studentService.findStudentsByFullName(fullName);
                            ModEntitiesUtils.deleteEntity(scanner, result, "Student", (student -> studentService.deleteStudent(student, student.getSpeciality())));
                        } else if (deleteStudent == 2) {
                            studentDeleteById(scanner, universityService);
                        }

                    } else if (workWithStudent == 3) { //edit student
                        int editStudent = ModEntitiesUtils.chooseEditing(scanner);
                        if (editStudent == 1) {
                            studentEditByName(scanner, studentService);
                        } else if (editStudent == 2) {
                            studentEditById(scanner, universityService);
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
                        teacherAddTeacher(scanner, facultyService, teacherService);
                    } else if (workWithTeacher == 2) { //delete teacher
                        int deleteTeacher= ModEntitiesUtils.chooseDeleting(scanner);
                        if (deleteTeacher == 1) {
                            System.out.print("Delete teacher by full name ");
                            String fullName = InputUtils.readLine(scanner, "Full name of teacher: ", false, false);
                            fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
                            List<Teacher> result = teacherService.findTeachersByFullName(fullName);

                            ModEntitiesUtils.deleteEntity(scanner, result, "Teacher", (teacher -> teacherService.deleteTeacher(teacher, teacher.getDepartment()) ));
                        } else if (deleteTeacher == 2) {
                            teacherDeleteById(scanner, universityService);
                        }

                    } else if (workWithTeacher == 3) { //edit teacher
                        int editTeacher = ModEntitiesUtils.chooseEditing(scanner);
                        if (editTeacher == 1) {
                            teacherEditByName(scanner, universityService);
                        } else if (editTeacher == 2) {
                            teacherEditById(scanner, universityService);
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



    //! ======= WORK WITH SPECIALITY ===== //

    /**
     * Add new Speciality
     */
    private static void specialityAddSpeciality(Scanner scanner, SpecialityService specialityService, FacultyService facultyService) {
        System.out.println("Choose faculty where speciality will be added:");
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (selectedFaculty != null) {
            String name = InputUtils.readLine(scanner, "Enter new Speciality name: ", false, false);
            name = InputUtils.removeSpaces(name, false, true, true, true);
            specialityService.addNewSpeciality(name, selectedFaculty);
        } else {
            System.out.println("No faculties found. Please add a new one first.");
        }
        InputUtils.pause(scanner);
    }

    /**
     * Rename the Speciality
     */
    private static void specialityRenameSpeciality(Scanner scanner, SpecialityService specialityService, Speciality selectedSpeciality, Faculty selectedFaculty) {
        String editName = InputUtils.readLine(scanner, "Write new name for " + selectedSpeciality.getName() + ": ", false, false);
        editName = InputUtils.removeSpaces(editName, false, true, true, true);
        specialityService.editSpecialityName(selectedSpeciality, editName, selectedFaculty);

        InputUtils.pause(scanner);
    }

    /**
     * Delete the Speciality
     */
    private static void specialityDeleteSpeciality(Scanner scanner, SpecialityService specialityService, Speciality selectedSpeciality, Faculty selectedFaculty) {
        System.out.print("Are you sure you want ot delete " + selectedSpeciality.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            specialityService.deleteSpeciality(selectedSpeciality, selectedFaculty);
            System.out.println("Speciality deleted successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
        InputUtils.pause(scanner);
    }

    //! ======= WORK WITH STUDENTS ===== //

    /**
     * Add new Student
     */
    private static void studentAddStudent(Scanner scanner, FacultyService facultyService, StudentService studentService) {
        System.out.println("--- Add Student ---");
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (selectedFaculty == null) return;

        // Select speciality
        Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
        if (selectedSpeciality == null) return;


        // Student's info
        String name = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Name: ", false, false), true, false, false, false);
        String surname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Surname: ", false, false), true, false, false, false);
        int course = InputUtils.readInt(scanner, "Enter Course (1-6): ", 1, 6);
        int groupNumber = InputUtils.readInt(scanner, "Enter Group: ", 1, Integer.MAX_VALUE);


        // Save
        Student s = new Student(name, surname, course, groupNumber,
                selectedFaculty.getName(),
                selectedSpeciality);
        studentService.addStudentToSpeciality(s, selectedSpeciality, groupNumber);

        System.out.println("Student " + s.getFullName() + " added to group " + groupNumber +
                " in " + selectedSpeciality.getName());
    }

    /**
     * Delete the Student by name
     */
    private static void studentDeleteByName(Scanner scanner, StudentService studentService) {
        String fullName = InputUtils.readLine(scanner, "Full name of student: ", false, false);
        fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
        List<Student> result = studentService.findStudentsByFullName(fullName);

        if (result.isEmpty()) {
            System.out.println("No students found with this name.");
        } else {
            Student studentToProcess;
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select one: ");
                for (int i = 0; i < result.size(); i++) {
                    System.out.println((i + 1) + ". " + result.get(i).getFullName() +
                            " (Group: " + result.get(i).getGroup() + ", Course: " + result.get(i).getCourse() + ")");
                }
                System.out.println("0. Cancel");
                int index = InputUtils.readInt(scanner, "> ", 0, result.size());
                if (index == 0) {
                    return;
                }

                studentToProcess = result.get(index - 1);
            } else {
                studentToProcess = result.get(0);
            }
            System.out.print("Are you sure you want ot delete " + studentToProcess.getFullName() + "? (y/n): ");
            if (scanner.nextLine().toLowerCase().startsWith("y")) {
                studentService.deleteStudent(studentToProcess, studentToProcess.getSpeciality());
            } else {
                System.out.println("Operation cancelled.");
            }
        }
        InputUtils.pause(scanner);
    }

    /**
     * Delete the Student by ID
     */
    private static void studentDeleteById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }

    /**
     * Edit the Student by name
     */
    private static void studentEditByName(Scanner scanner, StudentService studentService) {
        String fullName = InputUtils.readLine(scanner, "Enter full name part: ", false, false);
        fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
        List<Student> result = studentService.findStudentsByFullName(fullName);

        if (result.isEmpty()) {
            System.out.println("No students found with this name.");
        } else {
            Student studentToProcess;
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select one: ");
                for (int i = 0; i < result.size(); i++) {
                    System.out.println((i + 1) + ". " + result.get(i).getFullName() +
                            " (Group: " + result.get(i).getGroup() + ", Course: " + result.get(i).getCourse() + ")");
                }
                int index = InputUtils.readInt(scanner, "> ", 1, result.size());

                studentToProcess = result.get(index - 1);

            } else {
                studentToProcess = result.get(0);
            }

            System.out.println("\nEditing student: " + studentToProcess.getFullName());
            System.out.println("1. Change Surname");
            System.out.println("2. Change Name");
            System.out.println("3. Change Course");
            System.out.println("4. Change Group");
            System.out.println("0. Cancel");

            int fieldChoice = InputUtils.readInt(scanner, "> ", 0, 4);

            switch (fieldChoice) {
                case 1 -> {
                    String newSurname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new surname: ", false, false), true, false, false, false);
                    studentToProcess.setSurname(newSurname);
                    System.out.println("Surname updated!");
                }
                case 2 -> {
                    String newName = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new name: ", false, false), true, false, false, false);
                    studentToProcess.setName(newName);
                    System.out.println("Name updated!");
                }
                case 3 -> {
                    int newCourse = InputUtils.readInt(scanner, "Enter new course (1-6): ", 1, 6);
                    studentToProcess.setCourse(newCourse);
                    System.out.println("Course updated!");
                }
                case 4 -> {
                    int newGroup = InputUtils.readInt(scanner, "Enter new group number: ", 1, Integer.MAX_VALUE);
                    studentService.moveStudentToGroup(studentToProcess, newGroup);
                }
            }
        }
    }

    /**
     * Edit the Student by ID
     */
    private static void studentEditById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }

    /**
     * Show all students
     */
    /*private static void studentShowAll(UniversityService universityService) {
        List<Student> result = universityService.getAllStudents();
        if (result.isEmpty()) {
            System.out.println("No students found");
        } else {
            System.out.println(" --- Students ---");
            result.forEach(System.out::println);
        }
    }*/

    //! ======= WORK WITH TEACHERS ===== //

    /**
     * Add new Teacher
     */
    private static void teacherAddTeacher(Scanner scanner, FacultyService facultyService, TeacherService teacherService) {
        System.out.println("--- Add Teacher ---");
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (selectedFaculty == null) return;

        // Select Department
        Department selectedDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Departments in " + selectedFaculty.getName());
        if (selectedDept == null) return;


        // Teachers's info
        String name = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Name: ", false, false), true, false, false, false);
        String surname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Surname: ", false, false), true, false, false, false);
        String position = InputUtils.readLine(scanner, "Position: ", false, true);
        position = InputUtils.removeSpaces(position, false, true, true, true);


        // Save
        Teacher t = new Teacher(name, surname, position, selectedDept);
        teacherService.addTeacher(name, surname, position, selectedDept);
        System.out.println("Teacher " + name + " " + surname +
                " successfully added to department: " + selectedDept.getName());

        InputUtils.pause(scanner);
    }

    /**
     * Delete the Teacher by name
     */


    /**
     * Delete the Teacher by ID
     */
    private static void teacherDeleteById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }

    /**
     * Edit the Teacher by name
     */
    private static void teacherEditByName(Scanner scanner, UniversityService universityService) {
        String fullName = InputUtils.readLine(scanner, "Enter full name part: ", false, false);
        fullName = InputUtils.removeSpaces(fullName,false, true, true, true);
        //to be continued
    }

    /**
     * Edit the Teacher by ID
     */
    private static void teacherEditById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }

    /**
     * Show all teachers
     */
    /*private static void teacherShowAll(UniversityService universityService) {
        List<Teacher> result = universityService.getAllTeachers();
        if (result.isEmpty()) {
            System.out.println("No teachers found");
        } else {
            System.out.println(" --- Teachers ---");
            result.forEach(System.out::println);
        }
    }*/

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