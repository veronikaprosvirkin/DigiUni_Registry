package department;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import speciality.Speciality;
import user.UserService;
import utils.input.InputUtils;
import department.Department;
import department.DepartmentService;
import faculty.FacultyService;
import person.TeacherService;
import person.Teacher;
import utils.ModEntitiesUtils;
import utils.EntityNotFoundException;
import faculty.Faculty;

public class ModDepartmentUtils {
    //! ======= WORK WITH DEPARTMENT ===== //

    //show menu for department
    public static void showDepartmentMenu(Scanner scanner, DepartmentService departmentService, FacultyService facultyService, TeacherService teacherService, UserService userService) {
        System.out.println("1. Add Department");
        System.out.println("2. Manage existing Department");
        System.out.println("3. Show detail info of Department");
        System.out.println("0. Back");
        int action = InputUtils.readInt(scanner, "> ", 0, 3);

        if (action == 1) { // add a new department
            ModDepartmentUtils.departmentAddDepartment(scanner, departmentService, facultyService, teacherService, userService);
        } else if (action == 2) { // manage existing department
            // Select Faculty and Department
            java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
            if (optFaculty.isEmpty()) {
                System.out.println("Faculty wasn't chosen or found");
                return;
            }
            Faculty selectedFaculty = optFaculty.get();

            java.util.Optional<Department> optDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Department");
            if (optDept.isEmpty()) {
                System.out.println("Department wasn't chosen or found");
                return;
            }
            Department selectedDept = optDept.get();


            //Work with a selected department
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

            if (workWithDepartment == 1) { // edit department name
                ModDepartmentUtils.departmentRenameDepartment(scanner, departmentService, selectedDept, selectedFaculty, userService);
            } else if (workWithDepartment == 2) { //delete department
                ModDepartmentUtils.departmentDeleteDepartment(scanner, departmentService, selectedDept, selectedFaculty, userService);
            } else if (workWithDepartment == 3) { //show all teachers in the department
                ModDepartmentUtils.departmentShowTeachers(teacherService, selectedDept, scanner);
            } else if (workWithDepartment == 4) { // change head
                ModDepartmentUtils.departmentChangeHead(scanner, departmentService, teacherService, selectedDept, userService);
            } else if (workWithDepartment == 5) { // change location
                ModDepartmentUtils.departmentChangeLocation(scanner, departmentService, selectedDept,userService );
            }
        } else if (action == 3) { // show detail info of department
            showDepartmentDetails(scanner, facultyService, teacherService);
        }
    }

    public static void showDepartmentDetails(Scanner scanner, FacultyService facultyService, TeacherService teacherService) {
        Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty");
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
        showDepartmentDetails(selectedDept, selectedFaculty, teacherService);

        System.out.println("=========================================\n");
        InputUtils.pause(scanner);
    }

    public static void showDepartmentDetails(Department selectedDept, Faculty selectedFaculty, TeacherService teacherService) {
        ModEntitiesUtils.printDetailedInfo(selectedDept);
        long teachersCount = teacherService.getTeachersByDepartment(selectedDept).size();
        System.out.println("Active Teachers: " + teachersCount);

        System.out.println(" ---- Associated Specialities: ----");
        if (selectedFaculty.getSpecialities() == null || selectedFaculty.getSpecialities().isEmpty()) {
            System.out.println("No specialities associated with this faculty.");
        } else {
            selectedFaculty.getSpecialities().forEach(s ->
                    System.out.println("  * " + s.getNameOfSpeciality())
            );
        }
    }

    /**
     * Add new Department
     */
    static void departmentAddDepartment(Scanner scanner, DepartmentService departmentService, FacultyService facultyService,
                                        TeacherService teacherService, UserService userService) {
        System.out.println("Choose faculty where department will be added:");
        java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (optFaculty.isEmpty()) {
            System.out.println("Faculty wasn't selected or found");
            return;
        }
        Faculty selectedFaculty = optFaculty.get();
        String name = InputUtils.readLine(scanner, "Enter new Department name: ", false, true);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        
        Teacher head = null;
        System.out.print("Do you want to assign a Head of Department now? (y/n): ");
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
             var optionalHead = ModEntitiesUtils.selectEntity(scanner, teacherService.getAllTeachers(), "Teachers");
             if (optionalHead.isPresent()) {
                 head = optionalHead.get();
             }
        }

        System.out.print("Do you want to set a Location now? (y/n): ");
        String location = null;
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
             location = InputUtils.readLine(scanner, "Enter location: ", false, true);
        }

        departmentService.addNewDepartment(name, selectedFaculty, head, location, userService );

        InputUtils.pause(scanner);
    }

    /**
     * Rename the Department
     */
    static void departmentRenameDepartment(Scanner scanner, DepartmentService departmentService, Department selectedDept,
                                           Faculty selectedFaculty, UserService userService) {
        String editName = InputUtils.readLine(scanner, "Write new name for " + selectedDept.getName() + ": ", false, true);
        editName = InputUtils.removeSpaces(editName, false, true, true, true);
        departmentService.editDepartmentName(selectedDept, editName, selectedFaculty, userService);

        InputUtils.pause(scanner);
    }

    /**
     * Delete the Department
     */
    static void departmentDeleteDepartment(Scanner scanner, DepartmentService departmentService, Department selectedDept,
                                           Faculty selectedFaculty, UserService userService) {
        System.out.print("Are you sure you want or delete " + selectedDept.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            departmentService.deleteDepartment(selectedDept, selectedFaculty, userService);
            System.out.println("Department deleted successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }

    }

    /**
     * Show all teachers in the Department
     */
    static void departmentShowTeachers(TeacherService teacherService, Department selectedDept, Scanner scanner) {
        List<Teacher> teachers = teacherService.getTeachersByDepartment(selectedDept);
        if (teachers.isEmpty()) {
            System.out.println("There are no teachers assigned to " + selectedDept.getName() + " yet.");
        } else {
            System.out.println("\n--- Teachers in " + selectedDept.getName() + " ---");
            teachers.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }

    static void departmentChangeHead(Scanner scanner, DepartmentService departmentService, TeacherService teacherService,
                                     Department selectedDept, UserService userService) {
        System.out.println("Current head: " + (selectedDept.getHead() != null ? selectedDept.getHead().getDisplayInfo() : "None"));
        var optionalHead = ModEntitiesUtils.selectEntity(scanner, teacherService.getAllTeachers(), "Teachers");
        if (optionalHead.isPresent()) {
            departmentService.editDepartmentHead(selectedDept, optionalHead.get(), userService);
        } else {
            System.out.println("No head assigned.");
        }
        InputUtils.pause(scanner);
    }

    static void departmentChangeLocation(Scanner scanner, DepartmentService departmentService, Department selectedDept,
                                         UserService userService) {
        System.out.println("Current location: " + (selectedDept.getLocation() != null ? selectedDept.getLocation() : "None"));
        String location = InputUtils.readLine(scanner, "Enter new location (or leave empty to clear): ", true, true);
        departmentService.editDepartmentLocation(selectedDept, location, userService);
        InputUtils.pause(scanner);
    }
}
