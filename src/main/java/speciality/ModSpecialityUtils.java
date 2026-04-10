package speciality;

import java.util.Scanner;

import user.UserService;
import utils.input.InputUtils;
import utils.ModEntitiesUtils;
import utils.EntityNotFoundException;
import faculty.Faculty;
import speciality.Speciality;
import speciality.SpecialityService;
import faculty.FacultyService;

public class ModSpecialityUtils {
    //! ======= WORK WITH SPECIALITY ===== //
    //show menu for speciality
    public static void showSpecialityMenu(Scanner scanner, SpecialityService specialityService, FacultyService facultyService,
                                          UserService userService) {
        System.out.println("1. Add Speciality");
        System.out.println("2. Manage existing Speciality");
        System.out.println("0. Back");
        int action = InputUtils.readInt(scanner, "> ", 0, 2);

        if (action == 1) {
            ModSpecialityUtils.specialityAddSpeciality(scanner, specialityService, facultyService, userService);
        } else if (action == 2) {
            // Select Faculty and Speciality
            java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
            if (optFaculty.isEmpty()) {
                System.out.println("Faculty wasn't chosen or found");
                return;
            }
            Faculty selectedFaculty = optFaculty.get();

            java.util.Optional<Speciality> optSpec = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
            if (optSpec.isEmpty()) {
                System.out.println("Speciality wasn't chosen or found");
                return;
            }
            Speciality selectedSpeciality = optSpec.get();


            System.out.println("1. Rename Speciality");
            System.out.println("2. Delete Speciality");
            System.out.println("0. Back");

            int workWithSpeciality = InputUtils.readInt(scanner, "> ", 0, 2);
            if (workWithSpeciality == 1) {
                ModSpecialityUtils.specialityRenameSpeciality(scanner, specialityService, selectedSpeciality, selectedFaculty, userService);
            } else if (workWithSpeciality == 2) {
                ModSpecialityUtils.specialityDeleteSpeciality(scanner, specialityService, selectedSpeciality, selectedFaculty, userService);
            }
        }
    }
    /**
     * Add new Speciality
     */
    static void specialityAddSpeciality(Scanner scanner, SpecialityService specialityService, FacultyService facultyService,
                                        UserService userService) {
        System.out.println("Choose faculty where speciality will be added:");
        java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (optFaculty.isEmpty()) {
            System.out.println("Faculty wasn't selected ot found");
            return;
        }
        Faculty selectedFaculty = optFaculty.get();
        String name = InputUtils.readLine(scanner, "Enter new Speciality name: ", false, false);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        specialityService.addNewSpeciality(name, selectedFaculty, userService);
        InputUtils.pause(scanner);
    }

    /**
     * Rename the Speciality
     */
    static void specialityRenameSpeciality(Scanner scanner, SpecialityService specialityService, Speciality selectedSpeciality,
                                           Faculty selectedFaculty, UserService userService) {
        String editName = InputUtils.readLine(scanner, "Write new name for " + selectedSpeciality.getName() + ": ", false, false);
        editName = InputUtils.removeSpaces(editName, false, true, true, true);
        specialityService.editSpecialityName(selectedSpeciality, editName, selectedFaculty, userService);

        InputUtils.pause(scanner);
    }

    /**
     * Delete the Speciality
     */
    static void specialityDeleteSpeciality(Scanner scanner, SpecialityService specialityService, Speciality selectedSpeciality,
                                           Faculty selectedFaculty, UserService userService) {
        System.out.print("Are you sure you want ot delete " + selectedSpeciality.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            specialityService.deleteSpeciality(selectedSpeciality, selectedFaculty, userService);
            System.out.println("Speciality deleted successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
        InputUtils.pause(scanner);
    }
}
