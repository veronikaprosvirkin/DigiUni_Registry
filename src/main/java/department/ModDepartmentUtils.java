package department;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

import speciality.Speciality;
import user.User;
import utils.input.InputUtils;
import utils.ModEntitiesUtils;
import faculty.Faculty;
import person.Teacher;

// NETWORK IMPORTS
import service.NetworkClient;
import service.Request;
import service.Response;

public class ModDepartmentUtils {
    //! ======= WORK WITH DEPARTMENT (CLIENT) ===== //

    public static void showDepartmentMenu(Scanner scanner, User currentUser) {
        System.out.println("1. Add Department");
        System.out.println("2. Manage existing Department");
        System.out.println("3. Show detail info of Department");
        System.out.println("0. Back");
        int action = InputUtils.readInt(scanner, "> ", 0, 3);

        if (action == 1) {
            departmentAddDepartment(scanner);
        } else if (action == 2) {
            // NETWORK: Fetch Faculties (ВИКОРИСТОВУЄМО ТВІЙ НОВИЙ КОНСТРУКТОР БЕЗ NULL)
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

            Optional<Department> optDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Department");
            if (optDept.isEmpty()) {
                System.out.println("Department wasn't chosen or found");
                return;
            }
            Department selectedDept = optDept.get();

            System.out.println("\nDepartment: " + selectedDept.getName());
            if (selectedDept.getHead() != null) {
                System.out.println("Head: " + selectedDept.getHead().getDisplayInfo());
            } else {
                System.out.println("Head: Not Assigned");
            }
            if (selectedDept.getLocation() != null && !selectedDept.getLocation().isEmpty()) {
                System.out.println("Location: " + selectedDept.getLocation());
            } else {
                System.out.println("Location: Not Set");
            }

            System.out.println("1. Edit name of the Department");
            System.out.println("2. Delete Department");
            System.out.println("3. Show all Teachers in the Department");
            System.out.println("4. Assign/Change Head of Department");
            System.out.println("5. Set/Change Location");
            System.out.println("0. Back");
            int workWithDepartment = InputUtils.readInt(scanner, "> ", 0, 5);

            if (workWithDepartment == 1) {
                departmentRenameDepartment(scanner, selectedDept, selectedFaculty);
            } else if (workWithDepartment == 2) {
                departmentDeleteDepartment(scanner, selectedDept, selectedFaculty);
            } else if (workWithDepartment == 3) {
                departmentShowTeachers(selectedDept, scanner);
            } else if (workWithDepartment == 4) {
                departmentChangeHead(scanner, selectedDept);
            } else if (workWithDepartment == 5) {
                departmentChangeLocation(scanner, selectedDept);
            }
        } else if (action == 3) {
            showDepartmentDetails(scanner);
        }
    }

    public static void showDepartmentDetails(Scanner scanner) {
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

        Optional<Department> optDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Department");
        if (optDept.isEmpty()) {
            System.out.println("Department wasn't chosen or found");
            return;
        }
        Department selectedDept = optDept.get();
        showDepartmentDetails(selectedDept, selectedFaculty);

        System.out.println("=========================================\n");
        InputUtils.pause(scanner);
    }

    public static void showDepartmentDetails(Department selectedDept, Faculty selectedFaculty) {
        ModEntitiesUtils.printDetailedInfo(selectedDept);

        // NETWORK: Get teachers count in this department
        long teachersCount = 0;
        Response res = NetworkClient.sendRequest(new Request("GET_TEACHERS_BY_DEPARTMENT", selectedDept.getId()));
        if (res.isSuccess() && res.getData() != null) {
            @SuppressWarnings("unchecked")
            List<Teacher> teachers = (List<Teacher>) res.getData();
            teachersCount = teachers.size();
        }
        System.out.println("Active Teachers: " + teachersCount);

        System.out.println(" ---- Associated Specialities: ----");
        List<Speciality> specialities = selectedFaculty.getSpeciality();
        if (specialities == null || specialities.isEmpty()) {
            System.out.println("No specialities associated with this faculty.");
        } else {
            specialities.forEach(s -> System.out.println("  * " + s.getName()));
        }
    }

    /**
     * Add new Department
     */
    static void departmentAddDepartment(Scanner scanner) {
        System.out.println("Choose faculty where department will be added:");

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
        String name = InputUtils.readLine(scanner, "Enter new Department name: ", false, true);
        name = InputUtils.removeSpaces(name, false, true, true, true);

        String headId = null;
        System.out.print("Do you want to assign a Head of Department now? (y/n): ");
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            // NETWORK: Fetch all teachers
            Response teachRes = NetworkClient.sendRequest(new Request("GET_ALL_TEACHERS"));
            if (teachRes.isSuccess() && teachRes.getData() != null) {
                @SuppressWarnings("unchecked")
                List<Teacher> allTeachers = (List<Teacher>) teachRes.getData();
                var optionalHead = ModEntitiesUtils.selectEntity(scanner, allTeachers, "Teachers");
                if (optionalHead.isPresent()) {
                    headId = optionalHead.get().getId();
                }
            }
        }

        System.out.print("Do you want to set a Location now? (y/n): ");
        String location = null;
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            location = InputUtils.readLine(scanner, "Enter location: ", false, true);
        }

        // NETWORK CALL
        Map<String, Object> data = new HashMap<>();
        data.put("facultyId", selectedFaculty.getId());
        data.put("name", name);
        data.put("headId", headId);
        data.put("location", location);

        Response addRes = NetworkClient.sendRequest(new Request("ADD_DEPARTMENT", data));
        System.out.println(addRes.getMessage());

        InputUtils.pause(scanner);
    }

    /**
     * Rename the Department
     */
    static void departmentRenameDepartment(Scanner scanner, Department selectedDept, Faculty selectedFaculty) {
        String editName = InputUtils.readLine(scanner, "Write new name for " + selectedDept.getName() + ": ", false, true);
        editName = InputUtils.removeSpaces(editName, false, true, true, true);

        Map<String, Object> data = new HashMap<>();
        data.put("facultyId", selectedFaculty.getId());
        data.put("departmentId", selectedDept.getId());
        data.put("newName", editName);

        Response res = NetworkClient.sendRequest(new Request("EDIT_DEPARTMENT_NAME", data));
        System.out.println(res.getMessage());

        InputUtils.pause(scanner);
    }

    /**
     * Delete the Department
     */
    static void departmentDeleteDepartment(Scanner scanner, Department selectedDept, Faculty selectedFaculty) {
        System.out.print("Are you sure you want or delete " + selectedDept.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {

            Map<String, Object> data = new HashMap<>();
            data.put("facultyId", selectedFaculty.getId());
            data.put("departmentId", selectedDept.getId());

            Response res = NetworkClient.sendRequest(new Request("DELETE_DEPARTMENT", data));
            System.out.println(res.getMessage());
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    /**
     * Show all teachers in the Department
     */
    static void departmentShowTeachers(Department selectedDept, Scanner scanner) {
        Response res = NetworkClient.sendRequest(new Request("GET_TEACHERS_BY_DEPARTMENT", selectedDept.getId()));
        if (res.isSuccess() && res.getData() != null) {
            @SuppressWarnings("unchecked")
            List<Teacher> teachers = (List<Teacher>) res.getData();
            if (teachers.isEmpty()) {
                System.out.println("There are no teachers assigned to " + selectedDept.getName() + " yet.");
            } else {
                System.out.println("\n--- Teachers in " + selectedDept.getName() + " ---");
                teachers.forEach(t -> System.out.println(t.getDisplayInfo()));
            }
        } else {
            System.out.println("Could not load teachers for this department.");
        }
        InputUtils.pause(scanner);
    }

    static void departmentChangeHead(Scanner scanner, Department selectedDept) {
        System.out.println("Current head: " + (selectedDept.getHead() != null ? selectedDept.getHead().getDisplayInfo() : "None"));

        Response teachRes = NetworkClient.sendRequest(new Request("GET_ALL_TEACHERS"));
        if (teachRes.isSuccess() && teachRes.getData() != null) {
            @SuppressWarnings("unchecked")
            List<Teacher> allTeachers = (List<Teacher>) teachRes.getData();
            var optionalHead = ModEntitiesUtils.selectEntity(scanner, allTeachers, "Teachers");
            if (optionalHead.isPresent()) {
                Map<String, Object> data = new HashMap<>();
                data.put("departmentId", selectedDept.getId());
                data.put("teacherId", optionalHead.get().getId());

                Response res = NetworkClient.sendRequest(new Request("EDIT_DEPARTMENT_HEAD", data));
                System.out.println(res.getMessage());
            } else {
                System.out.println("No head assigned.");
            }
        }
        InputUtils.pause(scanner);
    }

    static void departmentChangeLocation(Scanner scanner, Department selectedDept) {
        System.out.println("Current location: " + (selectedDept.getLocation() != null ? selectedDept.getLocation() : "None"));
        String location = InputUtils.readLine(scanner, "Enter new location (or leave empty to clear): ", true, true);

        Map<String, Object> data = new HashMap<>();
        data.put("departmentId", selectedDept.getId());
        data.put("location", location);

        Response res = NetworkClient.sendRequest(new Request("EDIT_DEPARTMENT_LOCATION", data));
        System.out.println(res.getMessage());

        InputUtils.pause(scanner);
    }
}