import java.util.Scanner;

public class ModSpecialityUtils {
    //! ======= WORK WITH SPECIALITY ===== //
    //show menu for speciality
    static void showSpecialityMenu(Scanner scanner, SpecialityService specialityService, FacultyService facultyService) {
        System.out.println("1. Add Speciality");
        System.out.println("2. Manage existing Speciality");
        System.out.println("0. Back");
        int action = InputUtils.readInt(scanner, "> ", 0, 2);

        if (action == 1) {
            ModSpecialityUtils.specialityAddSpeciality(scanner, specialityService, facultyService);
        } else if (action == 2) {
            // Select Faculty and Speciality
            Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty")
                    .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't chosen or found"));

            Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty)
                    .orElseThrow(()-> new EntityNotFoundException("Speciality wasn't chosen or found"));


            System.out.println("1. Rename Speciality");
            System.out.println("2. Delete Speciality");
            System.out.println("0. Back");

            int workWithSpeciality = InputUtils.readInt(scanner, "> ", 0, 2);
            if (workWithSpeciality == 1) {
                ModSpecialityUtils.specialityRenameSpeciality(scanner, specialityService, selectedSpeciality, selectedFaculty);
            } else if (workWithSpeciality == 2) {
                ModSpecialityUtils.specialityDeleteSpeciality(scanner, specialityService, selectedSpeciality, selectedFaculty);
            }
        }
    }
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
