package person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import university.University;
import utils.*;
import utils.input.InputUtils;
import utils.sort.SortUtils;
import faculty.FacultyService;
import university.UniversityService;
import speciality.Speciality;
import faculty.Faculty;

public class ModStudentUtils {
    //! ======= WORK WITH STUDENTS ===== //

    //show menu for student
    public static void showStudentMenu(Scanner scanner, StudentService studentService, FacultyService facultyService, UniversityService universityService, boolean showId, University university) {
        System.out.println("1. Add Student");
        System.out.println("2. Delete Student");
        System.out.println("3. Edit information about student");
        System.out.println("4. Show all");
        System.out.println("0. Back");

        int workWithStudent = InputUtils.readInt(scanner, "> ", 0, 4);

        if (workWithStudent == 1) { //add student
            ModStudentUtils.studentAddStudent(scanner, facultyService, studentService, university);
        } else if (workWithStudent == 2) { //delete student
            int deleteStudent = ModEntitiesUtils.chooseDeleting(scanner);
            if (deleteStudent == 1) {
                String fullName = InputUtils.readLine(scanner, "Full name of student: ", false, false);
                fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
                List<Student> result = studentService.findStudentsByFullName(fullName);
                ModEntitiesUtils.deleteEntity(scanner, result, "Student", (student -> studentService.deleteStudent(student, student.getSpeciality())));
                FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
            } else if (deleteStudent == 2) {
                ModStudentUtils.studentDeleteById(scanner, studentService, university);
            }

        } else if (workWithStudent == 3) { //edit student
            int editStudent = ModEntitiesUtils.chooseEditing(scanner);
            if (editStudent == 1) {
                ModStudentUtils.studentEditByName(scanner, studentService, university);
                FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
            } else if (editStudent == 2) {
                ModStudentUtils.studentEditById(scanner, studentService, university);
                FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
            }


        } else if (workWithStudent == 4) {
            List<Student> students = studentService.getAllStudents();//show all students
            if (students.size()>1){
                System.out.println("Multiple students found. Please select sorting method: ");
                students = SortUtils.sortStudents(students, scanner);
            }
            ModEntitiesUtils.showAllEntity(scanner, students, "Students List", showId);
        }
    }

    //search menu for student
    public static void searchStudentMenu(Scanner scanner, StudentService studentService, FacultyService facultyService, UniversityService universityService, boolean showId) {
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
            ModEntitiesUtils.showAllEntity(scanner, students, "Students List", showId);
        }
    }
    /**
     * Add new Student
     */
    static void studentAddStudent(Scanner scanner, FacultyService facultyService, StudentService studentService, University university) {
        System.out.println("--- Add Student ---");
        java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (optFaculty.isEmpty()) {
            System.out.println("Faculty wasn't selected ot found");
            return;
        }
        Faculty selectedFaculty = optFaculty.get();

        // Select speciality
        java.util.Optional<Speciality> optSpec = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
        if (optSpec.isEmpty()) {
            System.out.println("Speciality wasn't selected ot found");
            return;
        }
        Speciality selectedSpeciality = optSpec.get();



        // Student's info
        String name = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Name: ", false, false), true, false, false, false);
        String surname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Surname: ", false, false), true, false, false, false);
        String patronymic = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Patronymic: ", false, false), true, false, false, false);
        int enrollmentYear = InputUtils.readInt(scanner, "Enter the year of enrollment: ", 1990, 2026);
        LocalDate enrollmentDate = LocalDate.of(enrollmentYear, 9, 1);
        int groupNumber = InputUtils.readInt(scanner, "Enter Group: ", 1, Integer.MAX_VALUE);
        int studyForm = InputUtils.readInt(scanner, "Enter study form (1 - BUDGET, 2 - CONTRACT): ", 1, 2);
        StudyForm newStudyForm;
        if (studyForm == 1) {
            newStudyForm = StudyForm.BUDGET;
        }
        else{
            newStudyForm = StudyForm.CONTRACT;
        }
        String domain = "@digiuni.ukma.edu";
        String finalEmail = "";

        String prefix = InputUtils.readLine(scanner, "Enter email without domen (press Enter to generate): ", true, true);

        if (prefix.isEmpty()) {
            String generatedEmail = ModStudentUtils.generateStudentEmail(name, surname);

            if (!isEmailTaken(generatedEmail, studentService)) {
                System.out.println("Email generated: " + generatedEmail);
                finalEmail = generatedEmail;
            } else {
                System.out.println("Generated email " + generatedEmail + " is already in system");
                while (true) {
                    String newPrefix = InputUtils.readLine(scanner, "Enter UNIQUE prefix for student: ", false, true);

                    if (newPrefix.contains("@")) {
                        newPrefix = newPrefix.split("@")[0];
                    }
                    newPrefix = newPrefix.toLowerCase().replaceAll("[^a-z0-9.]", "");

                    String newEmail = newPrefix + domain;

                    if (!newPrefix.isEmpty() && !isEmailTaken(newEmail, studentService)) {
                        finalEmail = newEmail;
                        break;
                    } else {
                        System.out.println("Error. This email is taken or empty. Try again.");
                    }
                }
            }
        } else {
            if (prefix.contains("@")) {
                prefix = prefix.split("@")[0];
            }
            prefix = prefix.toLowerCase().replaceAll("[^a-z0-9.]", "");

            String emailToCheck = prefix + domain;

            if (isEmailTaken(emailToCheck, studentService)) {
                System.out.println("This email is already taken, try again");
                while (true) {
                    String newPrefix = InputUtils.readLine(scanner, "Enter a UNIQUE email prefix: ", false, true);

                    if (newPrefix.contains("@")) {
                        newPrefix = newPrefix.split("@")[0];
                    }
                    newPrefix = newPrefix.toLowerCase().replaceAll("[^a-z0-9.]", "");

                    String fullNewEmail = newPrefix + domain;

                    if (!newPrefix.isEmpty() && !isEmailTaken(fullNewEmail, studentService)) {
                        finalEmail = fullNewEmail;
                        break;
                    } else {
                        System.out.println("Error. This email is also taken or empty. Write again.");
                    }
                }
            } else {
                finalEmail = emailToCheck;
            }
        }

        finalEmail = InputUtils.removeSpaces(finalEmail, false, true, true, true);
        String phone = InputUtils.readLine(scanner, "Enter phone number (optional, press Enter to skip): ", true, true);
        phone = InputUtils.removeSpaces(phone, false, true, true, true);

        String dobStr = InputUtils.readLine(scanner, "Enter date of birth (YYYY-MM-DD, optional, press Enter to skip): ", true, true);
        dobStr = InputUtils.removeSpaces(dobStr, false, true, true, true);
        LocalDate dateOfBirth = null;
        if (!dobStr.isEmpty()) {
            try {
                dateOfBirth = LocalDate.parse(dobStr);
            } catch (Exception e) {
                System.out.println("Invalid date format. Skipping date of birth.");
            }
        }

        // Save
        String newId = IdGenerator.generateStudentId(enrollmentDate.getYear());
        Student s = new Student(newId,name, surname, patronymic, enrollmentDate, groupNumber,
                selectedFaculty,
                selectedSpeciality,newStudyForm, dateOfBirth);

        s.setEmail(finalEmail);
        if (!phone.isEmpty()) s.setPhone(phone);

        studentService.addStudentToSpeciality(s, selectedSpeciality, groupNumber);
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));

        System.out.println("Student " + s.getFullName() + " added to group " + groupNumber +
                " in " + selectedSpeciality.getName());
    }

    //work with email
    private static boolean isEmailTaken(String email, StudentService studentService){
        if (email.isEmpty() || email ==null){
            return false;
        }
        for (Student s : studentService.getAllStudents()) {
            if (s.getEmail() != null && email.equals(s.getEmail())) {
                return true;
            }
        }
        return false;
    }

    private static String generateStudentEmail(String name, String surname){
        if(name == null || surname == null){
            return "";
        }
        String nameL = String.valueOf(name.toLowerCase().charAt(0));
        String domen = "@digiuni.ukma.edu";
        return nameL + "."+surname.toLowerCase().replace(" ", "")+ domen;
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
    static void studentDeleteById(Scanner scanner, StudentService studentService, University university) {
        String id = InputUtils.readLine(scanner, "Enter ID of student: ", false, true);

        List<Student> result = studentService.findStudentById(id);
        ModEntitiesUtils.deleteEntity(scanner, result, "Student", (student -> studentService.deleteStudent(student, student.getSpeciality())));
        FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
    }

    /**
     * Edit the Student by name
     */
    static void studentEditByName(Scanner scanner, StudentService studentService, University university) {
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
            editStudentDetails(scanner, studentToProcess, studentService, university);
            FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        }
    }

    /**
     * Edit the Student by ID
     */
    static void studentEditById(Scanner scanner, StudentService studentService, University university) {
        String id = InputUtils.readLine(scanner, "Enter ID of student: ", false, false);
        List<Student> result = studentService.findStudentById(id);
        if (result.isEmpty()){
            System.out.println("No student found by id " + id);
        } else {
            Student studentToProcess = result.get(0);
            editStudentDetails(scanner, studentToProcess, studentService, university);
            FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
        }
    }

    public static void editStudentDetails(Scanner scanner, Student studentToProcess, StudentService studentService, University university) {
        while (true) {
            System.out.println("\nEditing student: " + studentToProcess.getFullName());
            System.out.println("1. Change Surname");
            System.out.println("2. Change Name");
            System.out.println("3. Change Course");
            System.out.println("4. Change Group");
            System.out.println("5. Change Study Form");
            System.out.println("6. Change Status");
            System.out.println("7. Change Email");
            System.out.println("8. Change Phone Number");
            System.out.println("9. Change Date of Birth");
            System.out.println("0. Finish editing");

            int fieldChoice = InputUtils.readInt(scanner, "> ", 0, 9);
            if (fieldChoice == 0) {
                FileStorageUtils.saveAll(university, new user.UserService(), new university.UniversityService(university));
                break;
            }

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
                case 5 -> {
                    int formChoice = InputUtils.readInt(scanner, "Enter new study form (1 - BUDGET, 2 - CONTRACT): ", 1, 2);
                    StudyForm newStudyForm;
                    if (formChoice == 1) {
                        newStudyForm = StudyForm.BUDGET;
                    }else{
                        newStudyForm = StudyForm.CONTRACT;
                    }
                    if(studentToProcess.getStudyForm() == newStudyForm){
                        System.out.println("Error: Student is already on this study form!");
                    } else {
                        studentToProcess.setStudyForm(newStudyForm);
                        System.out.println("Study form updated!");
                    }

                }
                case 6 -> {
                    int statusChoice = InputUtils.readInt(scanner, "Enter new status (1 - ACTIVE, 2 - ACADEMIC LEAVE, 3-EXPELLED, 4-GRADUATED: ", 1, 4);
                    StudentStatus newStatus = null;
                    if (statusChoice == 1) {
                        newStatus = StudentStatus.ACTIVE;
                    } else if (statusChoice == 2) {
                        newStatus = StudentStatus.ACADEMIC_LEAVE;
                    } else if (statusChoice == 3) {
                        newStatus = StudentStatus.EXPELLED;
                    } else if (statusChoice == 4) {
                        newStatus = StudentStatus.GRADUATED;
                    }
                    if(studentToProcess.getStatus() == newStatus){
                        System.out.println("Error: Student is already has this status!");
                    } else {
                        studentToProcess.setStatus(newStatus);
                        System.out.println("Status updated!");
                    }

                }
                case 7 -> {
                    String newEmail = InputUtils.readLine(scanner, "Enter new email: ", false, false);
                    newEmail = InputUtils.removeSpaces(newEmail, false, true, true, true);
                    studentToProcess.setEmail(newEmail);
                    System.out.println("Email updated!");
                }
                case 8 -> {
                    String newPhone = InputUtils.readLine(scanner, "Enter new phone number: ", false, false);
                    newPhone = InputUtils.removeSpaces(newPhone, false, true, true, true);
                    studentToProcess.setPhone(newPhone);
                    System.out.println("Phone number updated!");
                }
                case 9 -> {
                    String newDob = InputUtils.readLine(scanner, "Enter new date of birth (YYYY-MM-DD, empty to clear): ", true, true);
                    newDob = InputUtils.removeSpaces(newDob, false, true, true, true);
                    if (newDob.isEmpty()) {
                        studentToProcess.setDateOfBirth(null);
                        System.out.println("Date of birth cleared!");
                    } else {
                        try {
                            studentToProcess.setDateOfBirth(LocalDate.parse(newDob));
                            System.out.println("Date of birth updated!");
                        } catch (Exception e) {
                            System.out.println("Invalid date format.");
                        }
                    }
                }
                }
        }

    }
}
