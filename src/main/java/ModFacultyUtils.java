import java.util.Scanner;

public class ModFacultyUtils {
    //! ======= WORK WITH FACULTY ===== //

    static void showFacultiesMenu(Scanner scanner, FacultyService facultyService){
        System.out.println("1. Add Faculty");
        System.out.println("2. Manage Existing Faculty");
        System.out.println("0. Back");
        int action = InputUtils.readInt(scanner, "> ", 0, 2);

        if (action == 1) {
            ModFacultyUtils.facultyAddFaculty(scanner, facultyService);
        } else if (action == 2) { //manage existing faculties
            Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty")
                    .orElseThrow(() -> new EntityNotFoundException("Faculty wasn't chosen or found"));

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


    /**
     * Add new Faculty
     */
    static void facultyAddFaculty(Scanner scanner, FacultyService facultyService) {
        String name = InputUtils.readLine(scanner, "Enter new Faculty name: ", false, false);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        facultyService.addNewFaculty(name);
        InputUtils.pause(scanner);
    }

    /**
     * Rename Faculty
     */
    static void facultyManageExistingFacultyRename(Scanner scanner, FacultyService facultyService, Faculty selectedFacultyToRename) {
        String newName = InputUtils.readLine(scanner, "Enter new Faculty name: ", false, false);
        newName = InputUtils.removeSpaces(newName, false, true, true, true);
        facultyService.editFacultyName(selectedFacultyToRename, newName);
        InputUtils.pause(scanner);
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
        InputUtils.pause(scanner);
    }
}
