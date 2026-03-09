import java.util.Scanner;

public class ModSpecialityUtils {
    //! ======= WORK WITH SPECIALITY ===== //

    /**
     * Add new Speciality
     */
    static void specialityAddSpeciality(Scanner scanner, SpecialityService specialityService, FacultyService facultyService) {
        System.out.println("Choose faculty where speciality will be added:");
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties")
                .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't selected ot found"));
        String name = InputUtils.readLine(scanner, "Enter new Speciality name: ", false, false);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        specialityService.addNewSpeciality(name, selectedFaculty);
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
