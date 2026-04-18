package person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import university.University;
import user.UserService;
import ui.StudentCardWindow;
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
    @SuppressWarnings("java:S107")
    public static void showStudentMenu(Scanner scanner, StudentService studentService, FacultyService facultyService,
                                       UserService userService, boolean showId, University university, TeacherService teacherService) {
        System.out.println("1. Add Student");
        System.out.println("2. Delete Student");
        System.out.println("3. Edit information about student");
        System.out.println("4. Show all");
        System.out.println("0. Back");

        int workWithStudent = InputUtils.readInt(scanner, "> ", 0, 4);

        if (workWithStudent == 1) { //add student
            ModStudentUtils.studentAddStudent(scanner, facultyService, studentService, university, userService, teacherService);
        } else if (workWithStudent == 2) { //delete student
            int deleteStudent = ModEntitiesUtils.chooseDeleting(scanner);
            if (deleteStudent == 1) {
                String fullName = InputUtils.readLine(scanner, "Full name of student: ", false, false);
                fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
                List<Student> result = studentService.findStudentsByFullName(fullName);
                deleteStudentWithPreview(scanner, result, studentService);
                FileStorageUtils.saveAll(university, userService);
            } else if (deleteStudent == 2) {
                ModStudentUtils.studentDeleteById(scanner, studentService, university, userService);
            }

        } else if (workWithStudent == 3) { //edit student
            int editStudent = ModEntitiesUtils.chooseEditing(scanner);
            if (editStudent == 1) {
                ModStudentUtils.studentEditByName(scanner, studentService, university, userService, teacherService);
            } else if (editStudent == 2) {
                ModStudentUtils.studentEditById(scanner, studentService, university, userService, teacherService);
            }


        } else if (workWithStudent == 4) {
            List<Student> students = studentService.getAllStudents();//show all students
            if (students.size()>1){
                System.out.println("Multiple students found. Please select sorting method: ");
                List<Student> sortedStudents = SortUtils.sortStudents(students, scanner);
                ModEntitiesUtils.showAllEntity(scanner, sortedStudents, "Students List", showId);
                return;
            }
            ModEntitiesUtils.showAllEntity(scanner, students, "Students List", showId);
        }
    }

    //search menu for student
    public static void searchStudentMenu(Scanner scanner, StudentService studentService, FacultyService facultyService, @SuppressWarnings("unused") UniversityService universityService, boolean showId) {
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
                List<Student> sortedStudents = SortUtils.sortStudents(students, scanner);
                ModEntitiesUtils.showAllEntity(scanner, sortedStudents, "Students List", showId);
                return;
            }
            ModEntitiesUtils.showAllEntity(scanner, students, "Students List", showId);
        }
    }
    /**
     * Add new Student
     */
    static void studentAddStudent(Scanner scanner, FacultyService facultyService, StudentService studentService, University university,
                                  UserService userService, TeacherService teacherService) {
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

        // Create a draft student for live preview in the card window during input.
        Student draftStudent = new Student(
                "PENDING",
                "",
                "",
                "",
                LocalDate.of(LocalDate.now().getYear(), 9, 1),
                1,
                selectedFaculty,
                selectedSpeciality,
                null
        );

        StudentCardWindow.open(draftStudent);
        try {
            // Student's info
            String name = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Name: ", false, false), true, false, false, false);
            draftStudent.setName(name);
            StudentCardWindow.refresh(draftStudent);

            String surname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Surname: ", false, false), true, false, false, false);
            draftStudent.setSurname(surname);
            StudentCardWindow.refresh(draftStudent);

            String patronymic = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Patronymic: ", false, false), true, false, false, false);
            draftStudent.setPatronymic(patronymic);
            StudentCardWindow.refresh(draftStudent);

            int enrollmentYear = InputUtils.readInt(scanner, "Enter the year of enrollment: ", 1990, 2026);
            LocalDate enrollmentDate = LocalDate.of(enrollmentYear, 9, 1);
            draftStudent.setEnrollmentDate(enrollmentDate);
            StudentCardWindow.refresh(draftStudent);

            int groupNumber = InputUtils.readInt(scanner, "Enter Group: ", 1, Integer.MAX_VALUE);
            draftStudent.setGroup(groupNumber);
            StudentCardWindow.refresh(draftStudent);

            int studyForm = InputUtils.readInt(scanner, "Enter study form (1 - BUDGET, 2 - CONTRACT): ", 1, 2);
            StudyForm newStudyForm = (studyForm == 1) ? StudyForm.BUDGET : StudyForm.CONTRACT;
            draftStudent.setStudyForm(newStudyForm);
            StudentCardWindow.refresh(draftStudent);

            String domain = "@digiuni.ukma.edu";
            String finalEmail = InputUtils.readAndValidateEmail(
                    scanner,
                    domain,
                    () -> ModEntitiesUtils.generateFullEmail(name, surname, domain),
                    email -> ModEntitiesUtils.isEmailGloballyTaken(email, studentService, teacherService)
            );
            draftStudent.setEmail(finalEmail);
            StudentCardWindow.refresh(draftStudent);

            String phone = InputUtils.readLine(scanner, "Enter phone number (optional, press Enter to skip): ", true, true);
            phone = InputUtils.removeSpaces(phone, false, true, true, true);
            draftStudent.setPhone(phone.isEmpty() ? null : phone);
            StudentCardWindow.refresh(draftStudent);

            String dobStr = InputUtils.readLine(scanner, "Enter date of birth (YYYY-MM-DD, optional, press Enter to skip): ", true, true);
            dobStr = InputUtils.removeSpaces(dobStr, false, true, true, true);
            if (!dobStr.isEmpty()) {
                try {
                    draftStudent.setDateOfBirth(LocalDate.parse(dobStr));
                } catch (Exception e) {
                    System.out.println("Invalid date format. Skipping date of birth.");
                }
            }
            StudentCardWindow.refresh(draftStudent);

            // Save
            String newId = IdGenerator.generateStudentId(enrollmentDate.getYear());
            draftStudent.setId(newId);
            StudentCardWindow.refresh(draftStudent);

            System.out.println("Detected gender: " + draftStudent.getGender());

            studentService.addStudentToSpeciality(draftStudent, selectedSpeciality, groupNumber);
            FileStorageUtils.saveAll(university, userService);

            System.out.println("Student " + draftStudent.getFullName() + " added to group " + groupNumber +
                    " in " + selectedSpeciality.getName());
        } finally {
            StudentCardWindow.close();
        }
    }


    /**
     * Delete the Student by ID
     */
    static void studentDeleteById(Scanner scanner, StudentService studentService, University university, UserService userService) {
        String id = InputUtils.readLine(scanner, "Enter ID of student: ", false, true);

        List<Student> result = studentService.findStudentById(id);
        deleteStudentWithPreview(scanner, result, studentService);
        FileStorageUtils.saveAll(university, userService);
    }

    private static void deleteStudentWithPreview(Scanner scanner, List<Student> students, StudentService studentService) {
        if (students.isEmpty()) {
            return;
        }

        Student studentToDelete;
        if (students.size() > 1) {
            System.out.println("Multiple students found. Please select one: ");
            for (int i = 0; i < students.size(); i++) {
                System.out.println((i + 1) + ". " + students.get(i).getDisplayInfo());
            }
            System.out.println("0. Cancel");

            int index = InputUtils.readInt(scanner, "> ", 0, students.size());
            if (index == 0) {
                System.out.println("Operation cancelled.");
                InputUtils.pause(scanner);
                return;
            }
            studentToDelete = students.get(index - 1);
        } else {
            studentToDelete = students.get(0);
        }

        StudentCardWindow.open(studentToDelete);
        StudentCardWindow.refresh(studentToDelete);
        try {
            String confirmation = InputUtils.readLine(scanner,
                    "Are you sure you want to delete: " + studentToDelete.getName() + "? (y/n): ",
                    false,
                    true);

            if (confirmation.toLowerCase().startsWith("y")) {
                studentService.deleteStudent(studentToDelete, studentToDelete.getSpeciality());
                StudentCardWindow.showArchived();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                System.out.println("Operation cancelled.");
            }
        } finally {
            StudentCardWindow.close();
        }

        InputUtils.pause(scanner);
    }

    /**
     * Edit the Student by name
     */
    static void studentEditByName(Scanner scanner, StudentService studentService, University university,
                                  UserService userService, TeacherService teacherService) {
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
            editStudentDetails(scanner, studentToProcess, studentService, university, userService, teacherService);
            FileStorageUtils.saveAll(university, userService);
        }
    }

    /**
     * Edit the Student by ID
     */
    static void studentEditById(Scanner scanner, StudentService studentService, University university,
                                UserService userService, TeacherService teacherService) {
        String id = InputUtils.readLine(scanner, "Enter ID of student: ", false, true);
        List<Student> result = studentService.findStudentById(id);
        if (result.isEmpty()){
            System.out.println("No student found by id " + id);
        } else {
            Student studentToProcess = result.get(0);
            editStudentDetails(scanner, studentToProcess, studentService, university, userService, teacherService);
            FileStorageUtils.saveAll(university, userService);
        }
    }

    public static void editStudentDetails(Scanner scanner, Student studentToProcess, StudentService studentService,
                                          University university, UserService userService, TeacherService teacherService) {
        StudentCardWindow.open(studentToProcess);
        try {
            while (true) {
                System.out.println("\nEditing student: " + studentToProcess.getFullName());
                System.out.println("1. Change Surname");
                System.out.println("2. Change Name");
                System.out.println("3. Change Course");
                System.out.println("4. Change Faculty/Speciality");
                System.out.println("5. Change Group");
                System.out.println("6. Change Study Form");
                System.out.println("7. Change Status");
                System.out.println("8. Change Email");
                System.out.println("9. Change Phone Number");
                System.out.println("10. Change Date of Birth");
                System.out.println("11. Change Gender");
                System.out.println("0. Finish editing");

                int fieldChoice = InputUtils.readInt(scanner, "> ", 0, 11);
                if (fieldChoice == 0) {
                    FileStorageUtils.saveAll(university, userService);
                    break;
                }

                switch (fieldChoice) {
                case 1 -> {
                    String newSurname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new surname: ", false, true), true, false, false, false);
                    studentToProcess.setSurname(newSurname);
                    StudentCardWindow.refresh(studentToProcess);
                    System.out.println("Surname updated!");
                }
                case 2 -> {
                    String newName = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new name: ", false, true), true, false, false, false);
                    studentToProcess.setName(newName);
                    StudentCardWindow.refresh(studentToProcess);
                    System.out.println("Name updated!");
                }
                case 3 -> {
                    int newCourse = InputUtils.readInt(scanner, "Enter new course (1-6): ", 1, 6);
                    int currentYear = LocalDate.now().getYear();
                    int newEnrollmentYear = currentYear - newCourse + 1;
                    studentToProcess.setEnrollmentDate(LocalDate.of(newEnrollmentYear, 9, 1));
                    StudentCardWindow.refresh(studentToProcess);
                    System.out.println("Course updated!");
                }
                case 4 -> {
                    java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, university.getFaculties(), "Faculties");
                    if (optFaculty.isEmpty()) {
                        System.out.println("Faculty wasn't selected.");
                        continue;
                    }

                    Faculty selectedFaculty = optFaculty.get();
                    java.util.Optional<Speciality> optSpeciality = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getSpeciality(), "Specialities");
                    if (optSpeciality.isEmpty()) {
                        System.out.println("Speciality wasn't selected.");
                        continue;
                    }

                    int newGroup = InputUtils.readInt(scanner, "Enter target group number: ", 1, Integer.MAX_VALUE);
                    studentService.moveStudentToSpeciality(studentToProcess, selectedFaculty, optSpeciality.get(), newGroup);
                    StudentCardWindow.refresh(studentToProcess);
                }
                case 5 -> {
                    int newGroup = InputUtils.readInt(scanner, "Enter new group number: ", 1, Integer.MAX_VALUE);
                    studentService.moveStudentToGroup(studentToProcess, newGroup);
                    StudentCardWindow.refresh(studentToProcess);
                }
                case 6 -> {
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
                        StudentCardWindow.refresh(studentToProcess);
                        System.out.println("Study form updated!");
                    }

                }
                case 7 -> {
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
                        StudentCardWindow.refresh(studentToProcess);
                        System.out.println("Status updated!");
                    }

                }
                case 8 -> {
                    System.out.println("Current email: " + studentToProcess.getEmail());
                    String domain = "@digiuni.ukma.edu";

                    String newEmail = InputUtils.readAndValidateEmail(
                            scanner,
                            domain,
                            () -> ModEntitiesUtils.generateFullEmail(studentToProcess.getOnlyName(), studentToProcess.getSurname(), domain),

                            email -> !email.equalsIgnoreCase(studentToProcess.getEmail()) &&
                                    ModEntitiesUtils.isEmailGloballyTaken(email, studentService, teacherService)
                    );
                    studentToProcess.setEmail(newEmail);
                    FileStorageUtils.updateStudentRecord(studentToProcess);
                    StudentCardWindow.refresh(studentToProcess);
                    System.out.println("Email successfully updated to: " + studentToProcess.getEmail());
                }
                case 9 -> {
                    String newPhone = InputUtils.readLine(scanner, "Enter new phone number: ", false, true);
                    newPhone = InputUtils.removeSpaces(newPhone, false, true, true, true);
                    studentToProcess.setPhone(newPhone);
                    StudentCardWindow.refresh(studentToProcess);
                    System.out.println("Phone number updated!");
                }
                case 10 -> {
                    String newDob = InputUtils.readLine(scanner, "Enter new date of birth (YYYY-MM-DD, empty to clear): ", true, true);
                    newDob = InputUtils.removeSpaces(newDob, false, true, true, true);
                    if (newDob.isEmpty()) {
                        studentToProcess.setDateOfBirth(null);
                        StudentCardWindow.refresh(studentToProcess);
                        System.out.println("Date of birth cleared!");
                    } else {
                        try {
                            studentToProcess.setDateOfBirth(LocalDate.parse(newDob));
                            StudentCardWindow.refresh(studentToProcess);
                            System.out.println("Date of birth updated!");
                        } catch (Exception e) {
                            System.out.println("Invalid date format.");
                        }
                    }
                }
                case 11 -> {
                    Gender newGender = chooseGender(scanner);
                    studentToProcess.changeGender(newGender);
                    StudentCardWindow.refresh(studentToProcess);
                    System.out.println("Gender updated to: " + studentToProcess.getGender());
                }
                }
            }
        } finally {
            StudentCardWindow.close();
        }

    }

    private static Gender chooseGender(Scanner scanner) {
        System.out.println("Select gender:");
        Gender[] genders = Gender.values();
        for (int i = 0; i < genders.length; i++) {
            System.out.println((i + 1) + ". " + genders[i].getDisplayName());
        }
        int choice = InputUtils.readInt(scanner, "> ", 1, genders.length);
        return genders[choice - 1];
    }
}
