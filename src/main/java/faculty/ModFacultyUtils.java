package faculty;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import user.User;
import utils.input.InputUtils;
import utils.ModEntitiesUtils;
import person.Teacher;

// NETWORK IMPORTS
import service.NetworkClient;
import service.Request;
import service.Response;
import person.Student;

public class ModFacultyUtils {
    //! ======= WORK WITH FACULTY (CLIENT) ===== //

    public static void showFacultiesMenu(Scanner scanner, User currentUser) {
        System.out.println("1. Add Faculty");
        System.out.println("2. Manage Existing Faculty");
        System.out.println("3. Show details of Faculty");
        System.out.println("0. Back");
        int action = InputUtils.readInt(scanner, "> ", 0, 3);

        if (action == 1) {
            facultyAddFaculty(scanner);
        } else if (action == 2) {
            Response res = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
            if (!res.isSuccess() || res.getData() == null) {
                System.out.println("Failed to load faculties.");
                return;
            }
            @SuppressWarnings("unchecked")
            List<Faculty> faculties = (List<Faculty>) res.getData();

            Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, faculties, "Faculty");
            if (optFaculty.isEmpty()) {
                System.out.println("Faculty wasn't chosen or found");
                return;
            }
            Faculty selectedFaculty = optFaculty.get();

            System.out.println("1. Edit Faculty Name");
            System.out.println("2. Delete Faculty");
            System.out.println("3. Edit contacts");
            System.out.println("4. Assign Dean");
            System.out.println("5. Edit Short Name");
            System.out.println("0. Back");
            int workWithFaculty = InputUtils.readInt(scanner, "> ", 0, 5);

            if (workWithFaculty == 1) {
                facultyManageExistingFacultyRename(scanner, selectedFaculty);
            } else if (workWithFaculty == 2) {
                facultyManageExistingFacultyDelete(scanner, selectedFaculty);
            } else if (workWithFaculty == 3) {
                facultyManageExistingFacultyEditContacts(scanner, selectedFaculty);
            } else if (workWithFaculty == 4) {
                Teacher dean = selectTeacherFlow(scanner);
                if (dean != null) {

                    java.util.Map<String, Object> data = new java.util.HashMap<>();
                    data.put("facultyId", selectedFaculty.getId());
                    data.put("teacherId", dean.getId());
                    Response assignRes = NetworkClient.sendRequest(new Request("ASSIGN_FACULTY_DEAN", data));
                    System.out.println(assignRes.getMessage());
                }
            } else if (workWithFaculty == 5) {
                facultyManageExistingFacultyRenameShort(scanner, selectedFaculty);
            }
        } else if (action == 3) {
            showFacultiesDetails(scanner);
        }
    }

    public static void showFacultiesDetails(Scanner scanner) {
        Response res = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
        if (!res.isSuccess() || res.getData() == null) {
            System.out.println("Failed to load faculties.");
            return;
        }
        @SuppressWarnings("unchecked")
        List<Faculty> faculties = (List<Faculty>) res.getData();

        Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, faculties, "Faculty");
        if (optFaculty.isEmpty()) {
            System.out.println("Faculty wasn't chosen or found");
            return;
        }
        showFacultiesDetails(optFaculty.get(), scanner);
    }

    public static void showFacultiesDetails(Faculty selectedFaculty, Scanner scanner) {
        ModEntitiesUtils.printDetailedInfo(selectedFaculty);
        System.out.println("--- Faculty Structure & Size ---");

        int deptCount = selectedFaculty.getDepartments().size();
        System.out.println(" - Total Departments: " + deptCount);

        int specCount = selectedFaculty.getSpeciality().size();
        System.out.println(" - Total Specialities: " + specCount);

        // NETWORK: Fetch all students to count them
        long studentCount = 0;
        Response res = NetworkClient.sendRequest(new Request("GET_ALL_STUDENTS"));
        if (res.isSuccess() && res.getData() != null) {
            @SuppressWarnings("unchecked")
            List<Student> allStudents = (List<Student>) res.getData();
            studentCount = allStudents.stream()
                    .filter(s -> s.getFaculty() != null && s.getFaculty().getId().equals(selectedFaculty.getId()))
                    .count();
        }
        System.out.println(" - Total Students: " + studentCount);

        System.out.println("=========================================\n");
        InputUtils.pause(scanner);
    }

    private static Teacher selectTeacherFlow(Scanner scanner) {
        System.out.println("How would you like to find the teacher?");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("0. Cancel");

        int searchChoice = InputUtils.readInt(scanner, "> ", 0, 2);
        Teacher selectedTeacher = null;

        if (searchChoice == 1) {
            String teacherId = InputUtils.readLine(scanner, "Enter Teacher ID: ", false, true);
            Response res = NetworkClient.sendRequest(new Request("SEARCH_TEACHER_BY_ID", teacherId));
            if (res.isSuccess() && res.getData() != null) {
                @SuppressWarnings("unchecked")
                List<Teacher> foundById = (List<Teacher>) res.getData();
                if (!foundById.isEmpty()) selectedTeacher = foundById.get(0);
            }
        } else if (searchChoice == 2) {
            String teacherName = InputUtils.readLine(scanner, "Enter Teacher Name: ", false, false);
            Response res = NetworkClient.sendRequest(new Request("SEARCH_TEACHER_BY_NAME", teacherName));
            if (res.isSuccess() && res.getData() != null) {
                @SuppressWarnings("unchecked")
                List<Teacher> foundByName = (List<Teacher>) res.getData();
                if (!foundByName.isEmpty()) {
                    if (foundByName.size() == 1) {
                        selectedTeacher = foundByName.get(0);
                    } else {
                        for (int i = 0; i < foundByName.size(); i++) {
                            System.out.println((i + 1) + ". [" + foundByName.get(i).getId() + "] " + foundByName.get(i).getFullName());
                        }
                        int pick = InputUtils.readInt(scanner, "Your choice: ", 0, foundByName.size());
                        if (pick > 0) selectedTeacher = foundByName.get(pick - 1);
                    }
                }
            }
        }
        return selectedTeacher;
    }

    private static void facultyManageExistingFacultyEditContacts(Scanner scanner, Faculty selectedFaculty) {
        System.out.println("Current contacts: " + selectedFaculty.getContacts());
        String newContacts = InputUtils.readLine(scanner, "Enter new contact information", false, true);

        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("facultyId", selectedFaculty.getId());
        data.put("newContacts", newContacts);

        Response res = NetworkClient.sendRequest(new Request("EDIT_FACULTY_CONTACTS", data));
        System.out.println(res.getMessage());
    }

    private static String generateShortName(String name) {
        if (name == null || name.trim().isEmpty()) return "F";
        String[] words = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            String lower = word.toLowerCase();
            if (lower.equals("of") || lower.equals("and") || lower.equals("the") || lower.equals("for")) {
                continue;
            }
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        String res = sb.toString();
        if (res.isEmpty() || res.charAt(0) != 'F') {
            res = "F" + res;
        }
        return res;
    }

    static void facultyAddFaculty(Scanner scanner) {
        String name = InputUtils.readLine(scanner, "Enter new Faculty name: ", false, true);
        name = InputUtils.removeSpaces(name, false, true, true, true);

        String generatedShort = generateShortName(name);
        String shortMsg = "Enter new Faculty short name (leave blank to use suggested: " + generatedShort + "): ";
        String shortName = InputUtils.readLine(scanner, shortMsg, true, false);
        shortName = InputUtils.removeSpaces(shortName, false, true, true, true);
        if (shortName.isEmpty()) {
            shortName = generatedShort;
        }

        String contact = InputUtils.readLine(scanner, "Enter contact information: ", false, true);

        System.out.println("Assign a Dean:");
        Teacher dean = selectTeacherFlow(scanner);

        if (dean != null) {
            java.util.Map<String, Object> data = new java.util.HashMap<>();
            data.put("name", name);
            data.put("shortName", shortName);
            data.put("contacts", contact);
            data.put("dean", dean);

            Response res = NetworkClient.sendRequest(new Request("ADD_FACULTY", data));
            System.out.println(res.getMessage());
        } else {
            System.out.println("Error: Faculty cannot be created without a Dean!");
        }
        InputUtils.pause(scanner);
    }

    static void facultyManageExistingFacultyRename(Scanner scanner, Faculty selectedFacultyToRename) {
        String newName = InputUtils.readLine(scanner, "Enter new Faculty name: ", false, true);
        newName = InputUtils.removeSpaces(newName, false, true, true, true);

        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("id", selectedFacultyToRename.getId());
        data.put("newName", newName);

        Response res = NetworkClient.sendRequest(new Request("EDIT_FACULTY_NAME", data));
        System.out.println(res.getMessage());
        InputUtils.pause(scanner);
    }

    static void facultyManageExistingFacultyRenameShort(Scanner scanner, Faculty selectedFacultyToRename) {
        System.out.println("Current short name: " + selectedFacultyToRename.getShortName());
        String newName = InputUtils.readLine(scanner, "Enter new Faculty short name: ", false, true);
        newName = InputUtils.removeSpaces(newName, false, true, true, true);

        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("facultyId", selectedFacultyToRename.getId());
        data.put("newShortName", newName);

        Response res = NetworkClient.sendRequest(new Request("EDIT_FACULTY_SHORT_NAME", data));
        System.out.println(res.getMessage());
        InputUtils.pause(scanner);
    }

    static void facultyManageExistingFacultyDelete(Scanner scanner, Faculty selectedFacultyToDelete) {
        System.out.print("Are you sure you want to delete " + selectedFacultyToDelete.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            Response res = NetworkClient.sendRequest(new Request("DELETE_FACULTY", selectedFacultyToDelete.getId()));
            System.out.println(res.getMessage());
        } else {
            System.out.println("Operation cancelled.");
        }
        InputUtils.pause(scanner);
    }
}