package person;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.Optional;

import utils.*;
import utils.input.InputUtils;
import utils.sort.SortUtils;
import faculty.Faculty;
import department.Department;
import ui.TeacherCardWindow;

// Network imports
import service.NetworkClient;
import service.Request;
import service.Response;

public class ModTeacherUtils {
    //! ======= WORK WITH TEACHERS (CLIENT) ===== //

    @SuppressWarnings("java:S107")
    public static void showTeacherMenu(Scanner scanner, boolean showId) {
        System.out.println("1. Add Teacher");
        System.out.println("2. Delete Teacher");
        System.out.println("3. Edit information about teacher");
        System.out.println("4. Show all");
        System.out.println("0. Back");
        int workWithTeacher = InputUtils.readInt(scanner, "> ", 0, 4);

        if (workWithTeacher == 1) {
            teacherAddTeacher(scanner);
        } else if (workWithTeacher == 2) {
            int deleteTeacher = ModEntitiesUtils.chooseDeleting(scanner);
            if (deleteTeacher == 1) {
                System.out.print("Delete teacher by full name ");
                String fullName = InputUtils.readLine(scanner, "Full name of teacher: ", false, false);
                fullName = InputUtils.removeSpaces(fullName, false, true, true, true);

                // NETWORK: Search Teacher
                Response res = NetworkClient.sendRequest(new Request("SEARCH_TEACHER_BY_NAME", fullName));
                if (res.isSuccess()) {
                    @SuppressWarnings("unchecked")
                    List<Teacher> result = (List<Teacher>) res.getData();
                    deleteTeacherWithPreview(scanner, result, showId);
                }
            } else if (deleteTeacher == 2) {
                teacherDeleteById(scanner, showId);
            }

        } else if (workWithTeacher == 3) {
            int editTeacher = ModEntitiesUtils.chooseEditing(scanner);
            if (editTeacher == 1) {
                teacherEditByName(scanner, showId);
            } else if (editTeacher == 2) {
                teacherEditById(scanner, showId);
            }
        } else if (workWithTeacher == 4) {
            // NETWORK: Get all Teachers
            Response res = NetworkClient.sendRequest(new Request("GET_ALL_TEACHERS"));
            if (res.isSuccess()) {
                @SuppressWarnings("unchecked")
                List<Teacher> teachers = (List<Teacher>) res.getData();
                if (teachers.size() > 1) {
                    System.out.println("Multiple teachers found. Please select sorting method: ");
                    List<Teacher> sortedTeachers = SortUtils.sortTeachers(teachers, scanner);
                    ModEntitiesUtils.showAllEntity(scanner, sortedTeachers, "Teachers List", showId);
                    return;
                }
                ModEntitiesUtils.showAllEntity(scanner, teachers, "Teachers List", showId);
            }
        }
    }

    public static void searchTeacherMenu(Scanner scanner, boolean showId) {
        System.out.println("1. Search by full name");
        System.out.println("2. Search by ID");
        System.out.println("3. Search by department");
        System.out.println("4. Show all");
        System.out.println("0. Back");

        int searchBy = InputUtils.readInt(scanner, "> ", 0, 4);
        List<Teacher> found = null;

        if (searchBy == 1) {
            String name = InputUtils.readLine(scanner, "Enter full name: ", false, false);
            name = InputUtils.removeSpaces(name, false, true, true, true);
            Response res = NetworkClient.sendRequest(new Request("SEARCH_TEACHER_BY_NAME", name));
            if (res.isSuccess()) found = (List<Teacher>) res.getData();
        } else if (searchBy == 2) {
            String id = InputUtils.readLine(scanner, "Enter ID: ", false, true);
            Response res = NetworkClient.sendRequest(new Request("SEARCH_TEACHER_BY_ID", id));
            if (res.isSuccess()) found = (List<Teacher>) res.getData();
        } else if (searchBy == 3) {
            String deptId = InputUtils.readLine(scanner, "Enter Department ID: ", false, false);
            Response res = NetworkClient.sendRequest(new Request("GET_TEACHERS_BY_DEPARTMENT", deptId));
            if (res.isSuccess()) found = (List<Teacher>) res.getData();
        } else if (searchBy == 4) {
            Response res = NetworkClient.sendRequest(new Request("GET_ALL_TEACHERS"));
            if (res.isSuccess()) found = (List<Teacher>) res.getData();
        }

        if (found != null && !found.isEmpty()) {
            if (found.size() > 1) {
                System.out.println("Multiple teachers found. Please select sorting method:");
                found = SortUtils.sortTeachers(found, scanner);
            }
            ModEntitiesUtils.showAllEntity(scanner, found, "Search Results", showId);

            System.out.println("\nWould you like to view a detailed graphical card?");
            int choice = InputUtils.readInt(scanner, "Enter the number (1-" + found.size() + ") or 0 to skip: ", 0, found.size());
            if (choice != 0) {
                TeacherCardWindow.open(found.get(choice - 1), showId);
            }
        } else if (searchBy != 0) {
            System.out.println("No teachers found.");
        }
    }

    /**
     * Add new Teacher
     */
    @SuppressWarnings("unchecked")
    static void teacherAddTeacher(Scanner scanner) {
        System.out.println("--- Add Teacher ---");

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

        Optional<Department> optDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Departments in " + selectedFaculty.getName());
        if (optDept.isEmpty()) {
            System.out.println("Department wasn't selected or found");
            return;
        }
        Department selectedDept = optDept.get();

        System.out.println("Available positions:");
        Position[] positions = Position.values();
        for (int i = 0; i < positions.length; i++) {
            System.out.println((i + 1) + ". " + positions[i].getDisplayName());
        }
        int posChoice = InputUtils.readInt(scanner, "> ", 1, positions.length);
        Position position = positions[posChoice - 1];

        String name = InputUtils.readLine(scanner, "Enter teacher's name: ", false, false);
        String surname = InputUtils.readLine(scanner, "Enter teacher's surname: ", false, false);
        String patronymic = InputUtils.readLine(scanner, "Enter teacher's patronymic (optional): ", true, false);

        // Fetching all emails temporarily for validation
        List<Teacher> allT = (List<Teacher>) NetworkClient.sendRequest(new Request("GET_ALL_TEACHERS")).getData();
        List<Student> allS = (List<Student>) NetworkClient.sendRequest(new Request("GET_ALL_STUDENTS")).getData();

        String domain = "@ukma.edu.ua";
        String finalEmail = InputUtils.readAndValidateEmail(
                scanner, domain,
                () -> ModEntitiesUtils.generateFullEmail(name, surname, domain),
                email -> ModEntitiesUtils.isEmailGloballyTaken(email, allS, allT) // Assuming you overload this or pass lists
        );

        String phone = InputUtils.readLine(scanner, "Enter phone number (optional): ", true, true);
        String academicDegree = InputUtils.readLine(scanner, "Enter academic degree (optional): ", true, true);
        String academicTitle = InputUtils.readLine(scanner, "Enter academic title (optional): ", true, true);
        String empDateStr = InputUtils.readLine(scanner, "Enter employment date (YYYY-MM-DD, optional): ", true, true);
        String workloadStr = InputUtils.readLine(scanner, "Enter workload (e.g. 1.0, optional): ", true, true);
        String dobStr = InputUtils.readLine(scanner, "Enter date of birth (YYYY-MM-DD, optional): ", true, true);

        LocalDate dateOfBirth = null;
        if (!dobStr.isEmpty()) {
            try { dateOfBirth = LocalDate.parse(dobStr); } catch (Exception ignored) {}
        }

        Teacher newTeacher = new Teacher("", name, surname, patronymic, position, selectedDept, dateOfBirth);
        if (!finalEmail.isEmpty()) newTeacher.setEmail(finalEmail);
        if (!phone.isEmpty()) newTeacher.setPhone(phone);
        if (!academicDegree.isEmpty()) newTeacher.setAcademicDegree(academicDegree);
        if (!academicTitle.isEmpty()) newTeacher.setAcademicTitle(academicTitle);
        if (!empDateStr.isEmpty()) {
            try { newTeacher.setEmploymentDate(LocalDate.parse(empDateStr)); } catch (Exception ignored) {}
        } else {
            newTeacher.setEmploymentDate(LocalDate.now());
        }
        if (!workloadStr.isEmpty()) {
            try { newTeacher.setWorkload(Double.parseDouble(workloadStr)); } catch (Exception ignored) {}
        }

        // NETWORK: Send the complete object to the server to save
        Response addRes = NetworkClient.sendRequest(new Request("ADD_TEACHER", newTeacher));
        System.out.println(addRes.getMessage());

        InputUtils.pause(scanner);
    }

    static void teacherDeleteById(Scanner scanner, boolean showId) {
        String id = InputUtils.readLine(scanner, "Enter ID of teacher: ", false, true);
        Response res = NetworkClient.sendRequest(new Request("SEARCH_TEACHER_BY_ID", id));
        if (res.isSuccess()) {
            @SuppressWarnings("unchecked")
            List<Teacher> result = (List<Teacher>) res.getData();
            deleteTeacherWithPreview(scanner, result, showId);
        }
    }

    private static void deleteTeacherWithPreview(Scanner scanner, List<Teacher> teachers, boolean showId) {
        if (teachers == null || teachers.isEmpty()) {
            System.out.println("No teachers found.");
            return;
        }

        Teacher teacherToDelete;
        if (teachers.size() > 1) {
            System.out.println("Multiple teachers found. Please select one: ");
            for (int i = 0; i < teachers.size(); i++) {
                System.out.println((i + 1) + ". " + teachers.get(i).getDisplayInfo());
            }
            System.out.println("0. Cancel");

            int index = InputUtils.readInt(scanner, "> ", 0, teachers.size());
            if (index == 0) return;
            teacherToDelete = teachers.get(index - 1);
        } else {
            teacherToDelete = teachers.get(0);
        }

        TeacherCardWindow.open(teacherToDelete, showId);
        TeacherCardWindow.refresh(teacherToDelete);
        try {
            String confirmation = InputUtils.readLine(scanner, "Are you sure you want to delete: " + teacherToDelete.getName() + "? (y/n): ", false, true);
            if (confirmation.toLowerCase().startsWith("y")) {
                // NETWORK: Request Deletion
                Response res = NetworkClient.sendRequest(new Request("DELETE_TEACHER", teacherToDelete));
                System.out.println(res.getMessage());

                TeacherCardWindow.showArchived();
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            } else {
                System.out.println("Operation cancelled.");
            }
        } finally {
            TeacherCardWindow.close();
        }
    }

    static void teacherEditByName(Scanner scanner, boolean showId) {
        String fullName = InputUtils.readLine(scanner, "Enter full name part: ", false, false);
        fullName = InputUtils.removeSpaces(fullName, false, true, true, true);

        Response res = NetworkClient.sendRequest(new Request("SEARCH_TEACHER_BY_NAME", fullName));
        if (res.isSuccess()) {
            @SuppressWarnings("unchecked")
            List<Teacher> result = (List<Teacher>) res.getData();
            processEditingSelection(scanner, result, showId);
        }
    }

    static void teacherEditById(Scanner scanner, boolean showId) {
        String id = InputUtils.readLine(scanner, "Enter ID of teacher: ", false, true);
        Response res = NetworkClient.sendRequest(new Request("SEARCH_TEACHER_BY_ID", id));
        if (res.isSuccess()) {
            @SuppressWarnings("unchecked")
            List<Teacher> result = (List<Teacher>) res.getData();
            processEditingSelection(scanner, result, showId);
        }
    }

    private static void processEditingSelection(Scanner scanner, List<Teacher> result, boolean showId) {
        if (result == null || result.isEmpty()) {
            System.out.println("No teachers found.");
            return;
        }
        Teacher teacherToProcess;
        if (result.size() > 1) {
            System.out.println("Multiple teachers found. Please select one: ");
            result.sort(Comparator.comparing(Teacher::getFullName));
            for (int i = 0; i < result.size(); i++) {
                System.out.println((i + 1) + ". " + result.get(i).getFullName() + " (" + result.get(i).getPosition() + ")");
            }
            int index = InputUtils.readInt(scanner, "> ", 1, result.size());
            teacherToProcess = result.get(index - 1);
        } else {
            teacherToProcess = result.get(0);
        }
        editTeacherDetails(scanner, teacherToProcess, showId);
    }

    private static void editTeacherDetails(Scanner scanner, Teacher teacherToProcess, boolean showId) {
        TeacherCardWindow.open(teacherToProcess, showId);
        try {
            while(true) {
                System.out.println("\nEditing teacher: " + teacherToProcess.getFullName());
                System.out.println("1. Change Surname");
                System.out.println("2. Change Name");
                System.out.println("3. Change Position");
                System.out.println("4. Change Email");
                System.out.println("5. Change Phone Number");
                System.out.println("6. Change Academic Degree");
                System.out.println("7. Change Academic Title");
                System.out.println("8. Change Employment Date");
                System.out.println("9. Change Workload");
                System.out.println("10. Change Date of Birth");
                System.out.println("11. Change Gender");
                System.out.println("0. Finish editing and Save");

                int fieldChoice = InputUtils.readInt(scanner, "> ", 0, 11);

                if (fieldChoice == 0) {
                    // NETWORK: Send the modified object back to the server to update
                    Response res = NetworkClient.sendRequest(new Request("EDIT_TEACHER", teacherToProcess));
                    System.out.println(res.getMessage());
                    break;
                }

                switch (fieldChoice) {
                    case 1 -> {
                        String newSurname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new surname: ", false, true), true, false, false, false);
                        teacherToProcess.setSurname(newSurname);
                        System.out.println("Surname updated locally!");
                    }
                    case 2 -> {
                        String newName = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new name: ", false, true), true, false, false, false);
                        teacherToProcess.setName(newName);
                        System.out.println("Name updated locally!");
                    }
                    case 3 -> {
                        System.out.println("Available positions:");
                        Position[] positions = Position.values();
                        for (int i = 0; i < positions.length; i++) {
                            System.out.println((i + 1) + ". " + positions[i].getDisplayName());
                        }
                        int posChoice = InputUtils.readInt(scanner, "> ", 1, positions.length);
                        teacherToProcess.setPosition(positions[posChoice - 1].toString());
                        System.out.println("Position updated locally!");
                    }
                    case 4 -> teacherToProcess.setEmail(InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new email: ", false, true), false, true, true, true));
                    case 5 -> teacherToProcess.setPhone(InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new phone: ", false, true), false, true, true, true));
                    case 6 -> teacherToProcess.setAcademicDegree(InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new academic degree: ", false, true), false, true, true, true));
                    case 7 -> teacherToProcess.setAcademicTitle(InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new academic title: ", false, true), false, true, true, true));
                    case 8 -> {
                        try { teacherToProcess.setEmploymentDate(LocalDate.parse(InputUtils.readLine(scanner, "Enter new date (YYYY-MM-DD): ", false, true))); }
                        catch (Exception e) { System.out.println("Invalid date."); }
                    }
                    case 9 -> {
                        try { teacherToProcess.setWorkload(Double.parseDouble(InputUtils.readLine(scanner, "Enter new workload (e.g. 1.0): ", false, true))); }
                        catch (Exception e) { System.out.println("Invalid workload."); }
                    }
                    case 10 -> {
                        String dob = InputUtils.readLine(scanner, "Enter new DOB (YYYY-MM-DD, empty to clear): ", true, true);
                        if (dob.isEmpty()) teacherToProcess.setDateOfBirth(null);
                        else try { teacherToProcess.setDateOfBirth(LocalDate.parse(dob)); } catch (Exception e) { System.out.println("Invalid date."); }
                    }
                    case 11 -> teacherToProcess.changeGender(chooseGender(scanner));
                }
                TeacherCardWindow.refresh(teacherToProcess);
            }
        } finally {
            TeacherCardWindow.close();
        }
    }

    private static Gender chooseGender(Scanner scanner) {
        System.out.println("Select gender:");
        Gender[] genders = Gender.values();
        for (int i = 0; i < genders.length; i++) System.out.println((i + 1) + ". " + genders[i].getDisplayName());
        return genders[InputUtils.readInt(scanner, "> ", 1, genders.length) - 1];
    }
}