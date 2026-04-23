package speciality;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

import user.User;
import utils.input.InputUtils;
import utils.ModEntitiesUtils;
import faculty.Faculty;
import person.Student;
import person.StudyForm;

// NETWORK IMPORTS
import service.NetworkClient;
import service.Request;
import service.Response;

public class ModSpecialityUtils {
    //! ======= WORK WITH SPECIALITY (CLIENT) ===== //

    // Notice: All Services removed!
    public static void showSpecialityMenu(Scanner scanner, User currentUser) {
        System.out.println("1. Add Speciality");
        System.out.println("2. Manage existing Speciality");
        System.out.println("3. Show details of Speciality");
        System.out.println("0. Back");

        int action = InputUtils.readInt(scanner, "> ", 0, 3);

        if (action == 1) {
            specialityAddSpeciality(scanner);
        } else if (action == 2) {
            // NETWORK: Fetch Faculties
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

            Optional<Speciality> optSpec = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
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
                specialityRenameSpeciality(scanner, selectedSpeciality, selectedFaculty);
            } else if (workWithSpeciality == 2) {
                specialityDeleteSpeciality(scanner, selectedSpeciality, selectedFaculty);
            }
        } else if (action == 3) {
            showSpecialityDetails(scanner);
        }
    }

    private static void showSpecialityDetails(Scanner scanner) {
        // NETWORK: Fetch Faculties
        Response res = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
        if (!res.isSuccess() || res.getData() == null) return;
        @SuppressWarnings("unchecked")
        List<Faculty> faculties = (List<Faculty>) res.getData();

        Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, faculties, "Faculty");
        if (optFaculty.isEmpty()) {
            System.out.println("Faculty wasn't chosen or found");
            return;
        }
        Faculty selectedFaculty = optFaculty.get();

        Optional<Speciality> optSpec = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
        if (optSpec.isEmpty()) {
            System.out.println("Speciality wasn't chosen or found");
            return;
        }
        Speciality selectedSpeciality = optSpec.get();
        showSpecialityDetails(selectedSpeciality);
    }

    public static void showSpecialityDetails(Speciality selectedSpeciality) {
        System.out.println("--- Speciality Details ---");
        System.out.println("Name: " + selectedSpeciality.getName());
        System.out.println("Groups:");

        if (selectedSpeciality.getGroups() == null || selectedSpeciality.getGroups().isEmpty()) {
            System.out.println("No groups in this speciality.");
        } else {
            selectedSpeciality.getGroups().forEach(group ->
                    System.out.println("- " + group.getGroupNumber())
            );
        }
        System.out.println("\n--- Current Enrollment (1st Year) ---");

        // NETWORK: Fetch all students to calculate statistics
        long firstYearBudget = 0;
        long firstYearContract = 0;

        Response res = NetworkClient.sendRequest(new Request("GET_ALL_STUDENTS"));
        if (res.isSuccess() && res.getData() != null) {
            @SuppressWarnings("unchecked")
            List<Student> allStudents = (List<Student>) res.getData();

            firstYearBudget = allStudents.stream()
                    .filter(s -> s.getSpeciality() != null && s.getSpeciality().getId().equals(selectedSpeciality.getId()))
                    .filter(s -> s.getCourse() == 1)
                    .filter(s -> s.getStudyForm() == StudyForm.BUDGET)
                    .count();

            firstYearContract = allStudents.stream()
                    .filter(s -> s.getSpeciality() != null && s.getSpeciality().getId().equals(selectedSpeciality.getId()))
                    .filter(s -> s.getCourse() == 1)
                    .filter(s -> s.getStudyForm() == StudyForm.CONTRACT)
                    .count();
        }

        System.out.println(" - Budget places taken:   " + firstYearBudget);
        System.out.println(" - Contract places taken: " + firstYearContract);

        System.out.println("=========================================\n");
    }

    /**
     * Add new Speciality
     */
    static void specialityAddSpeciality(Scanner scanner) {
        System.out.println("Choose faculty where speciality will be added:");

        // NETWORK: Fetch Faculties
        Response res = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
        if (!res.isSuccess() || res.getData() == null) return;
        @SuppressWarnings("unchecked")
        List<Faculty> faculties = (List<Faculty>) res.getData();

        Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, faculties, "Faculties");
        if (optFaculty.isEmpty()) {
            System.out.println("Faculty wasn't selected or found");
            return;
        }
        Faculty selectedFaculty = optFaculty.get();
        String name = InputUtils.readLine(scanner, "Enter new Speciality name: ", false, true);
        name = InputUtils.removeSpaces(name, false, true, true, true);

        // NETWORK CALL
        Map<String, Object> data = new HashMap<>();
        data.put("facultyId", selectedFaculty.getId());
        data.put("name", name);

        Response addRes = NetworkClient.sendRequest(new Request("ADD_SPECIALITY", data));
        System.out.println(addRes.getMessage());

        InputUtils.pause(scanner);
    }

    /**
     * Rename the Speciality
     */
    static void specialityRenameSpeciality(Scanner scanner, Speciality selectedSpeciality, Faculty selectedFaculty) {
        String editName = InputUtils.readLine(scanner, "Write new name for " + selectedSpeciality.getName() + ": ", false, true);
        editName = InputUtils.removeSpaces(editName, false, true, true, true);

        // NETWORK CALL
        Map<String, Object> data = new HashMap<>();
        data.put("facultyId", selectedFaculty.getId());
        data.put("specialityId", selectedSpeciality.getId());
        data.put("newName", editName);

        Response res = NetworkClient.sendRequest(new Request("EDIT_SPECIALITY_NAME", data));
        System.out.println(res.getMessage());

        InputUtils.pause(scanner);
    }

    /**
     * Delete the Speciality
     */
    static void specialityDeleteSpeciality(Scanner scanner, Speciality selectedSpeciality, Faculty selectedFaculty) {
        System.out.print("Are you sure you want to delete " + selectedSpeciality.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {

            // NETWORK CALL
            Map<String, Object> data = new HashMap<>();
            data.put("facultyId", selectedFaculty.getId());
            data.put("specialityId", selectedSpeciality.getId());

            Response res = NetworkClient.sendRequest(new Request("DELETE_SPECIALITY", data));
            System.out.println(res.getMessage());
        } else {
            System.out.println("Operation cancelled.");
        }
        InputUtils.pause(scanner);
    }
}