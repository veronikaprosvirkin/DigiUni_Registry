package faculty;

import java.util.List;
import java.util.Scanner;

import user.Permission;
import user.User;
import user.UserService;
import utils.input.InputUtils;
import faculty.Faculty;
import faculty.FacultyService;
import person.TeacherService;
import utils.ModEntitiesUtils;
import utils.EntityNotFoundException;
import person.Teacher;

public class ModFacultyUtils {
    //! ======= WORK WITH FACULTY ===== //

    public static void showFacultiesMenu(Scanner scanner, FacultyService facultyService, TeacherService teacherService,
                                         User currentUser, UserService userService) {
        System.out.println("1. Add Faculty");
        System.out.println("2. Manage Existing Faculty");
        System.out.println("0. Back");
        int action = InputUtils.readInt(scanner, "> ", 0, 2);

        if (action == 1) {
            ModFacultyUtils.facultyAddFaculty(scanner, facultyService, teacherService, userService);
        } else if (action == 2) { //manage existing faculties
            java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
            if (optFaculty.isEmpty()) {
                System.out.println("Faculty wasn't chosen or found");
                return;
            }
            Faculty selectedFaculty = optFaculty.get();

            System.out.println("1. Edit Faculty");
            System.out.println("2. Delete Faculty");
            System.out.println("3. Edit contacts");
            System.out.println("4. Assign Dean");
            System.out.println("5. Edit Short Name");
            System.out.println("0. Back");
            int workWithFaculty = InputUtils.readInt(scanner, "> ", 0, 5);
            if (workWithFaculty == 1) { //edit faculty name
                ModFacultyUtils.facultyManageExistingFacultyRename(scanner, facultyService, selectedFaculty, userService);
            } else if (workWithFaculty == 2) { //delete faculty
                ModFacultyUtils.facultyManageExistingFacultyDelete(scanner, facultyService, selectedFaculty, userService);
            } else if (workWithFaculty == 3) { //edit contacts
                ModFacultyUtils.facultyManageExistingFacultyEditContacts(scanner, facultyService, selectedFaculty);
            } else if (workWithFaculty == 4) {
                Teacher dean = selectTeacherFlow(scanner, teacherService);
                if (dean != null) {
                    facultyService.assignDean(selectedFaculty, dean, userService);
                    System.out.println("Success! " + dean.getFullName() + " is now the Dean.");
                }
            } else if (workWithFaculty == 5) {
                ModFacultyUtils.facultyManageExistingFacultyRenameShort(scanner, facultyService, selectedFaculty);
            }
        }
    }

    private static Teacher selectTeacherFlow(Scanner scanner, TeacherService teacherService) {
        System.out.println("How would you like to find the teacher?");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("0. Cancel");

        int searchChoice = InputUtils.readInt(scanner, "> ", 0, 2);
        Teacher selectedTeacher = null;

        if (searchChoice == 1) {
            String teacherId = InputUtils.readLine(scanner, "Enter Teacher ID: ", false, true);
            List<Teacher> foundById = teacherService.findTeacherById(teacherId);
            if (foundById != null && !foundById.isEmpty()) {
                selectedTeacher = foundById.get(0);
            }
        } else if (searchChoice == 2) {
            String teacherName = InputUtils.readLine(scanner, "Enter Teacher Name: ", false, false);
            List<Teacher> foundByName = teacherService.findTeachersByFullName(teacherName);

            if (foundByName != null && !foundByName.isEmpty()) {
                if (foundByName.size() == 1) {
                    selectedTeacher = foundByName.get(0);
                } else {
                    // Твій блок вибору з декількох (залишаємо як було)
                    for (int i = 0; i < foundByName.size(); i++) {
                        System.out.println((i + 1) + ". [" + foundByName.get(i).getId() + "] " + foundByName.get(i).getFullName());
                    }
                    int pick = InputUtils.readInt(scanner, "Your choice: ", 0, foundByName.size());
                    if (pick > 0) selectedTeacher = foundByName.get(pick - 1);
                }
            }
        }
        return selectedTeacher; // Повертаємо об'єкт
    }

    private static void facultyManageExistingFacultyEditContacts(Scanner scanner, FacultyService facultyService, Faculty selectedFaculty) {
        System.out.println("Current contacts: " + selectedFaculty.getContacts());

        String newContacts = InputUtils.readLine(scanner, "Enter new contact information", false, true);
        selectedFaculty.setContacts(newContacts);
        System.out.println("Contacts for " + selectedFaculty.getName() + " updated successfully!");
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

    /**
     * Add new Faculty
     */
    static void facultyAddFaculty(Scanner scanner, FacultyService facultyService, TeacherService teacherService, UserService userService) {
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
        Teacher dean = selectTeacherFlow(scanner, teacherService);

        if (dean != null) {
            facultyService.addNewFaculty(name, shortName, contact, dean, userService);
        } else {
            System.out.println("Error: Faculty cannot be created without a Dean!");
        }
        InputUtils.pause(scanner);
    }

    /**
     * Rename Faculty
     */
    static void facultyManageExistingFacultyRename(Scanner scanner, FacultyService facultyService, Faculty selectedFacultyToRename,
                                                   UserService userService) {
        String newName = InputUtils.readLine(scanner, "Enter new Faculty name: ", false, true);
        newName = InputUtils.removeSpaces(newName, false, true, true, true);
        facultyService.editFacultyName(selectedFacultyToRename, newName, userService);
        InputUtils.pause(scanner);
    }

    /**
     * Rename Faculty Short Name
     */
    static void facultyManageExistingFacultyRenameShort(Scanner scanner, FacultyService facultyService, Faculty selectedFacultyToRename) {
        System.out.println("Current short name: " + selectedFacultyToRename.getShortName());
        String newName = InputUtils.readLine(scanner, "Enter new Faculty short name: ", false, true);
        newName = InputUtils.removeSpaces(newName, false, true, true, true);
        selectedFacultyToRename.setShortName(newName);
        System.out.println("Short name updated successfully!");
        InputUtils.pause(scanner);
    }

    /**
     * Delete Faculty
     */
    static void facultyManageExistingFacultyDelete(Scanner scanner, FacultyService facultyService, Faculty selectedFacultyToDelete,
                                                   UserService userService) {
        System.out.print("Are you sure you want to delete " + selectedFacultyToDelete.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            facultyService.deleteFaculty(selectedFacultyToDelete, userService);
            System.out.println("Faculty deleted successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
        InputUtils.pause(scanner);
    }
}
