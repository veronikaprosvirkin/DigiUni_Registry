package person;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import utils.*;
import utils.input.InputUtils;
import utils.sort.SortUtils;
import faculty.Faculty;
import speciality.Speciality;
import ui.StudentCardWindow;

// NETWORK IMPORTS (Based on your folder structure)
import service.NetworkClient;
import service.Request;
import service.Response;

public class ModStudentUtils {
    //! ======= WORK WITH STUDENTS (CLIENT) ===== //

    // Notice: All Services and University parameters are removed!
    public static void showStudentMenu(Scanner scanner, boolean showId) {
        System.out.println("1. Add Student");
        System.out.println("2. Delete Student");
        System.out.println("3. Edit information about student");
        System.out.println("4. Show all");
        System.out.println("0. Back");

        int workWithStudent = InputUtils.readInt(scanner, "> ", 0, 4);

        if (workWithStudent == 1) {
            studentAddStudent(scanner, showId);
        } else if (workWithStudent == 2) {
            int deleteStudent = ModEntitiesUtils.chooseDeleting(scanner);
            if (deleteStudent == 1) {
                String fullName = InputUtils.readLine(scanner, "Full name of student: ", false, false);
                fullName = InputUtils.removeSpaces(fullName, false, true, true, true);

                // NETWORK CALL
                Response res = NetworkClient.sendRequest(new Request("SEARCH_STUDENT_BY_NAME", fullName));
                if (res.isSuccess() && res.getData() != null) {
                    @SuppressWarnings("unchecked")
                    List<Student> result = (List<Student>) res.getData();
                    deleteStudentWithPreview(scanner, result, showId);
                }
            } else if (deleteStudent == 2) {
                studentDeleteById(scanner, showId);
            }
        } else if (workWithStudent == 3) {
            int editStudent = ModEntitiesUtils.chooseEditing(scanner);
            if (editStudent == 1) {
                studentEditByName(scanner, showId);
            } else if (editStudent == 2) {
                studentEditById(scanner, showId);
            }
        } else if (workWithStudent == 4) {
            // NETWORK CALL
            Response res = NetworkClient.sendRequest(new Request("GET_ALL_STUDENTS"));
            if (res.isSuccess() && res.getData() != null) {
                @SuppressWarnings("unchecked")
                List<Student> students = (List<Student>) res.getData();
                if (students.size() > 1) {
                    System.out.println("Multiple students found. Please select sorting method: ");
                    List<Student> sortedStudents = SortUtils.sortStudents(students, scanner);
                    ModEntitiesUtils.showAllEntity(scanner, sortedStudents, "Students List", showId);
                    return;
                }
                ModEntitiesUtils.showAllEntity(scanner, students, "Students List", showId);
            }
        }
    }

    public static void searchStudentMenu(Scanner scanner, boolean showId) {
        System.out.println("1. Search by full name");
        System.out.println("2. Search by group number");
        System.out.println("3. Search by course");
        System.out.println("4. Search by speciality");
        System.out.println("5. Show all");
        System.out.println("0. Back");

        int searchBy = InputUtils.readInt(scanner, "> ", 0, 5);
        List<Student> found = null;

        if (searchBy == 1) {
            String name = InputUtils.readLine(scanner, "Enter full name: ", false, false);
            name = InputUtils.removeSpaces(name, false, true, true, true);
            Response res = NetworkClient.sendRequest(new Request("SEARCH_STUDENT_BY_NAME", name));
            if (res.isSuccess()) found = (List<Student>) res.getData();

        } else if (searchBy == 2) {
            int group = InputUtils.readInt(scanner, "Enter group number: ", 1, 100);
            Response res = NetworkClient.sendRequest(new Request("SEARCH_STUDENT_BY_GROUP", group));
            if (res.isSuccess()) found = (List<Student>) res.getData();

        } else if (searchBy == 3) {
            int course = InputUtils.readInt(scanner, "Enter course (1-6): ", 1, 6);
            Response res = NetworkClient.sendRequest(new Request("SEARCH_STUDENT_BY_COURSE", course));
            if (res.isSuccess()) found = (List<Student>) res.getData();

        } else if (searchBy == 4) {
            String specId = InputUtils.readLine(scanner, "Enter Speciality ID: ", false, true);
            Response res = NetworkClient.sendRequest(new Request("SEARCH_STUDENT_BY_SPECIALITY", specId));
            if (res.isSuccess()) found = (List<Student>) res.getData();

        } else if (searchBy == 5) {
            Response res = NetworkClient.sendRequest(new Request("GET_ALL_STUDENTS"));
            if (res.isSuccess()) found = (List<Student>) res.getData();
        }

        if (found != null && !found.isEmpty()) {
            if (found.size() > 1) {
                System.out.println("Multiple students found. Please select sorting method:");
                found = SortUtils.sortStudents(found, scanner);
            }
            ModEntitiesUtils.showAllEntity(scanner, found, "Search Results", showId);

            System.out.println("\nWould you like to view a detailed graphical card?");
            int choice = InputUtils.readInt(scanner, "Enter the number (1-" + found.size() + ") or 0 to skip: ", 0, found.size());
            if (choice != 0) {
                StudentCardWindow.open(found.get(choice - 1), showId);
            }
        } else if (searchBy != 0) {
            System.out.println("No students found.");
        }
    }

    /**
     * Add new Student with Live Preview
     */
    @SuppressWarnings("unchecked")
    static void studentAddStudent(Scanner scanner, boolean showId) {
        System.out.println("--- Add Student ---");

        // NETWORK: Fetch Faculties
        Response facRes = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
        if (!facRes.isSuccess() || facRes.getData() == null) {
            System.out.println("Failed to load faculties from server.");
            return;
        }
        List<Faculty> faculties = (List<Faculty>) facRes.getData();

        Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, faculties, "Faculties");
        if (optFaculty.isEmpty()) {
            System.out.println("Faculty wasn't selected or found");
            return;
        }
        Faculty selectedFaculty = optFaculty.get();

        Optional<Speciality> optSpec = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
        if (optSpec.isEmpty()) {
            System.out.println("Speciality wasn't selected or found");
            return;
        }
        Speciality selectedSpeciality = optSpec.get();

        // Create a draft student for live preview
        Student draftStudent = new Student("PENDING", "", "", "", LocalDate.of(LocalDate.now().getYear(), 9, 1), 1, selectedFaculty, selectedSpeciality, null);

        StudentCardWindow.open(draftStudent, showId);
        try {
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
            draftStudent.setStudyForm((studyForm == 1) ? StudyForm.BUDGET : StudyForm.CONTRACT);
            StudentCardWindow.refresh(draftStudent);

            // Fetch emails for validation
            List<Student> allS = (List<Student>) NetworkClient.sendRequest(new Request("GET_ALL_STUDENTS")).getData();
            List<Teacher> allT = (List<Teacher>) NetworkClient.sendRequest(new Request("GET_ALL_TEACHERS")).getData();

            String domain = "@student.ukma.edu.ua";
            String finalEmail = InputUtils.readAndValidateEmail(
                    scanner, domain,
                    () -> ModEntitiesUtils.generateFullEmail(name, surname, domain),
                    email -> ModEntitiesUtils.isEmailGloballyTaken(email, allS, allT)
            );
            draftStudent.setEmail(finalEmail);
            StudentCardWindow.refresh(draftStudent);

            String phone = InputUtils.readLine(scanner, "Enter phone number (optional): ", true, true);
            draftStudent.setPhone(phone.isEmpty() ? null : InputUtils.removeSpaces(phone, false, true, true, true));
            StudentCardWindow.refresh(draftStudent);

            String dobStr = InputUtils.readLine(scanner, "Enter date of birth (YYYY-MM-DD, optional): ", true, true);
            if (!dobStr.isEmpty()) {
                try { draftStudent.setDateOfBirth(LocalDate.parse(InputUtils.removeSpaces(dobStr, false, true, true, true))); }
                catch (Exception e) { System.out.println("Invalid date format. Skipping."); }
            }

            StudentCardWindow.refresh(draftStudent);

            System.out.println("Detected gender: " + draftStudent.getGender());

            // NETWORK: Send draft to Server to save
            Response addRes = NetworkClient.sendRequest(new Request("ADD_STUDENT", draftStudent));
            System.out.println(addRes.getMessage());

        } finally {
            StudentCardWindow.close();
        }
    }

    static void studentDeleteById(Scanner scanner, boolean showId) {
        String id = InputUtils.readLine(scanner, "Enter ID of student: ", false, true);
        Response res = NetworkClient.sendRequest(new Request("SEARCH_STUDENT_BY_ID", id));
        if (res.isSuccess() && res.getData() != null) {
            @SuppressWarnings("unchecked")
            List<Student> result = (List<Student>) res.getData();
            deleteStudentWithPreview(scanner, result, showId);
        }
    }

    private static void deleteStudentWithPreview(Scanner scanner, List<Student> students, boolean showId) {
        if (students == null || students.isEmpty()) {
            System.out.println("No students found.");
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
            if (index == 0) return;
            studentToDelete = students.get(index - 1);
        } else {
            studentToDelete = students.get(0);
        }

        StudentCardWindow.open(studentToDelete, showId);
        StudentCardWindow.refresh(studentToDelete);
        try {
            String confirmation = InputUtils.readLine(scanner, "Are you sure you want to delete: " + studentToDelete.getName() + "? (y/n): ", false, true);
            if (confirmation.toLowerCase().startsWith("y")) {
                // NETWORK CALL
                Response res = NetworkClient.sendRequest(new Request("DELETE_STUDENT", studentToDelete));
                System.out.println(res.getMessage());

                StudentCardWindow.showArchived();
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            } else {
                System.out.println("Operation cancelled.");
            }
        } finally {
            StudentCardWindow.close();
        }
    }

    static void studentEditByName(Scanner scanner, boolean showId) {
        String fullName = InputUtils.readLine(scanner, "Enter full name part: ", false, false);
        fullName = InputUtils.removeSpaces(fullName, false, true, true, true);

        Response res = NetworkClient.sendRequest(new Request("SEARCH_STUDENT_BY_NAME", fullName));
        if (res.isSuccess()) {
            @SuppressWarnings("unchecked")
            List<Student> result = (List<Student>) res.getData();
            processStudentEditingSelection(scanner, result, showId);
        }
    }

    static void studentEditById(Scanner scanner, boolean showId) {
        String id = InputUtils.readLine(scanner, "Enter ID of student: ", false, true);

        Response res = NetworkClient.sendRequest(new Request("SEARCH_STUDENT_BY_ID", id));
        if (res.isSuccess()) {
            @SuppressWarnings("unchecked")
            List<Student> result = (List<Student>) res.getData();
            processStudentEditingSelection(scanner, result, showId);
        }
    }

    private static void processStudentEditingSelection(Scanner scanner, List<Student> result, boolean showId) {
        if (result == null || result.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        Student studentToProcess;
        if (result.size() > 1) {
            System.out.println("Multiple students found. Please select one: ");
            result.sort(Comparator.comparing(Student::getFullName));
            for (int i = 0; i < result.size(); i++) {
                System.out.println((i + 1) + ". " + result.get(i).getFullName() + " (Group: " + result.get(i).getGroup() + ")");
            }
            int index = InputUtils.readInt(scanner, "> ", 1, result.size());
            studentToProcess = result.get(index - 1);
        } else {
            studentToProcess = result.get(0);
        }
        editStudentDetails(scanner, studentToProcess, showId);
    }

    @SuppressWarnings("unchecked")
    public static void editStudentDetails(Scanner scanner, Student studentToProcess, boolean showId) {
        StudentCardWindow.open(studentToProcess, showId);
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
                System.out.println("0. Finish editing and Save");

                int fieldChoice = InputUtils.readInt(scanner, "> ", 0, 11);
                if (fieldChoice == 0) {
                    // NETWORK: Send modified object back to server
                    Response res = NetworkClient.sendRequest(new Request("EDIT_STUDENT", studentToProcess));
                    System.out.println(res.getMessage());
                    break;
                }

                switch (fieldChoice) {
                    case 1 -> studentToProcess.setSurname(InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new surname: ", false, true), true, false, false, false));
                    case 2 -> studentToProcess.setName(InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new name: ", false, true), true, false, false, false));
                    case 3 -> {
                        int newCourse = InputUtils.readInt(scanner, "Enter new course (1-6): ", 1, 6);
                        studentToProcess.setEnrollmentDate(LocalDate.of(LocalDate.now().getYear() - newCourse + 1, 9, 1));
                    }
                    case 4 -> {
                        // NETWORK: Fetch Faculties
                        Response facRes = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
                        if (facRes.isSuccess() && facRes.getData() != null) {
                            List<Faculty> faculties = (List<Faculty>) facRes.getData();
                            Optional<Faculty> optFac = ModEntitiesUtils.selectEntity(scanner, faculties, "Faculties");
                            if (optFac.isPresent()) {
                                Optional<Speciality> optSpec = ModEntitiesUtils.selectEntity(scanner, optFac.get().getSpeciality(), "Specialities");
                                if (optSpec.isPresent()) {
                                    studentToProcess.setFaculty(optFac.get());
                                    studentToProcess.setSpeciality(optSpec.get());
                                    studentToProcess.setGroup(InputUtils.readInt(scanner, "Enter target group number: ", 1, Integer.MAX_VALUE));
                                }
                            }
                        }
                    }
                    case 5 -> studentToProcess.setGroup(InputUtils.readInt(scanner, "Enter new group number: ", 1, Integer.MAX_VALUE));
                    case 6 -> {
                        System.out.println("1. BUDGET\n2. CONTRACT");
                        studentToProcess.setStudyForm((InputUtils.readInt(scanner, "> ", 1, 2) == 1) ? StudyForm.BUDGET : StudyForm.CONTRACT);
                    }
                    case 7 -> {
                        StudentStatus[] statuses = StudentStatus.values();
                        for (int i = 0; i < statuses.length; i++) {
                            System.out.println((i + 1) + ". " + statuses[i].name());
                        }
                        studentToProcess.setStatus(statuses[InputUtils.readInt(scanner, "> ", 1, statuses.length) - 1]);
                    }
                    case 8 -> {
                        List<Student> allS = (List<Student>) NetworkClient.sendRequest(new Request("GET_ALL_STUDENTS")).getData();
                        List<Teacher> allT = (List<Teacher>) NetworkClient.sendRequest(new Request("GET_ALL_TEACHERS")).getData();
                        String domain = "@student.ukma.edu.ua";
                        String newEmail = InputUtils.readAndValidateEmail(scanner, domain,
                                () -> ModEntitiesUtils.generateFullEmail(studentToProcess.getOnlyName(), studentToProcess.getSurname(), domain),
                                email -> !email.equalsIgnoreCase(studentToProcess.getEmail()) && ModEntitiesUtils.isEmailGloballyTaken(email, allS, allT)
                        );
                        studentToProcess.setEmail(newEmail);
                    }
                    case 9 -> studentToProcess.setPhone(InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new phone: ", false, true), false, true, true, true));
                    case 10 -> {
                        String dob = InputUtils.readLine(scanner, "Enter new DOB (YYYY-MM-DD, empty to clear): ", true, true);
                        if (dob.isEmpty()) studentToProcess.setDateOfBirth(null);
                        else try { studentToProcess.setDateOfBirth(LocalDate.parse(dob)); } catch (Exception ignored) {}
                    }
                    case 11 -> studentToProcess.changeGender(chooseGender(scanner));
                }
                StudentCardWindow.refresh(studentToProcess);
                System.out.println("Updated locally! Press 0 to Save.");
            }
        } finally {
            StudentCardWindow.close();
        }
    }

    private static Gender chooseGender(Scanner scanner) {
        System.out.println("Select gender:");
        Gender[] genders = Gender.values();
        for (int i = 0; i < genders.length; i++) System.out.println((i + 1) + ". " + genders[i].getDisplayName());
        return genders[InputUtils.readInt(scanner, "> ", 1, genders.length) - 1];
    }
}