import java.util.Scanner;

public class ModFacultyUtils {
    //! ======= WORK WITH FACULTY ===== //

    /**
     * Add new Faculty
     */
    static void facultyAddFaculty(Scanner scanner, FacultyService facultyService) {
        String name = InputUtils.readLine(scanner, "Enter new Faculty name: ", false, false);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        facultyService.addNewFaculty(name);
        Main.pause(scanner);
    }

    /**
     * Rename Faculty
     */
    static void facultyManageExistingFacultyRename(Scanner scanner, FacultyService facultyService, Faculty selectedFacultyToRename) {
        String newName = InputUtils.readLine(scanner, "Enter new Faculty name: ", false, false);
        newName = InputUtils.removeSpaces(newName, false, true, true, true);
        facultyService.editFacultyName(selectedFacultyToRename, newName);
        Main.pause(scanner);
    }

    /**
     * Delete Faculty
     */
    static void facultyManageExistingFacultyDelete(Scanner scanner, FacultyService facultyService, Faculty selectedFacultyToDelete) {
        System.out.print("Are you sure you want to delete " + selectedFacultyToDelete.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            facultyService.deleteFaculty(selectedFacultyToDelete);
            System.out.println("Faculty deleted successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
        Main.pause(scanner);
    }
}
