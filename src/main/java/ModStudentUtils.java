import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ModStudentUtils {
    //! ======= WORK WITH STUDENTS ===== //

    //show menu for student
    static void showStudentMenu(Scanner scanner, StudentService studentService, FacultyService facultyService, UniversityService universityService) {
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
            if (students.size()>1){
                System.out.println("Multiple students found. Please select sorting method: ");
                students = SortUtils.sortStudents(students, scanner);
            }
            ModEntitiesUtils.showAllEntity(scanner, students, "Students List");
        }
    }

    //search menu for student
    static void searchStudentMenu(Scanner scanner, StudentService studentService, FacultyService facultyService, UniversityService universityService) {
        System.out.println("1. Search by full name");
        System.out.println("2. Search by group number");
        System.out.println("3. Search by course");
        System.out.println("4. Search by speciality");
        System.out.println("5. Show all");
        System.out.println("0. Back");
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
        } else  if (searchBy == 5) {
            List<Student> students = studentService.getAllStudents(); // show all students
            if (students.size()>1){
                System.out.println("Multiple students found. Please select sorting method: ");
                students = SortUtils.sortStudents(students, scanner);
            }
            ModEntitiesUtils.showAllEntity(scanner, students, "Students List");
        }
    }
    /**
     * Add new Student
     */
    static void studentAddStudent(Scanner scanner, FacultyService facultyService, StudentService studentService) {
        System.out.println("--- Add Student ---");
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties")
                .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't selected ot found"));


        // Select speciality
        Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty)
                .orElseThrow(()-> new EntityNotFoundException("Speciality wasn't selected ot found"));



        // Student's info
        String name = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Name: ", false, false), true, false, false, false);
        String surname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Surname: ", false, false), true, false, false, false);
        int enrollmentYear = InputUtils.readInt(scanner, "Enter the year of enrollment: ", 1990, 2026);
        LocalDate enrollmentDate = LocalDate.of(enrollmentYear, 9, 1);
        int groupNumber = InputUtils.readInt(scanner, "Enter Group: ", 1, Integer.MAX_VALUE);


        // Save
        Student s = new Student(name, surname, enrollmentDate, groupNumber,
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
    static void studentDeleteById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }

    /**
     * Edit the Student by name
     */
    static void studentEditByName(Scanner scanner, StudentService studentService) {
        String fullName = InputUtils.readLine(scanner, "Enter full name part: ", false, false);
        fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
        List<Student> result = studentService.findStudentsByFullName(fullName);

        if (result.isEmpty()) {
            System.out.println("No students found with this name.");
        } else {
            Student studentToProcess;
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select one: ");
                // Sort alphabetically
                result.sort(Comparator.comparing(Student::getFullName));
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
                    int currentYear = LocalDate.now().getYear();
                    int newEnrollmentYear = currentYear - newCourse + 1;
                    studentToProcess.setEnrollmentDate(LocalDate.of(newEnrollmentYear, 9, 1));
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
    static void studentEditById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }
}
