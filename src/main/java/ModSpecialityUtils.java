import java.util.Scanner;

public class ModSpecialityUtils {
    //! ======= WORK WITH SPECIALITY ===== //

    /**
     * Add new Speciality
     */
    static void specialityAddSpeciality(Scanner scanner, SpecialityService specialityService, FacultyService facultyService) {
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
    static void specialityRenameSpeciality(Scanner scanner, SpecialityService specialityService, Speciality selectedSpeciality, Faculty selectedFaculty) {
        String editName = InputUtils.readLine(scanner, "Write new name for " + selectedSpeciality.getName() + ": ", false, false);
        editName = InputUtils.removeSpaces(editName, false, true, true, true);
        specialityService.editSpecialityName(selectedSpeciality, editName, selectedFaculty);

        InputUtils.pause(scanner);
    }

    /**
     * Delete the Speciality
     */
    static void specialityDeleteSpeciality(Scanner scanner, SpecialityService specialityService, Speciality selectedSpeciality, Faculty selectedFaculty) {
        System.out.print("Are you sure you want ot delete " + selectedSpeciality.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            specialityService.deleteSpeciality(selectedSpeciality, selectedFaculty);
            System.out.println("Speciality deleted successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
        InputUtils.pause(scanner);
    }
}
