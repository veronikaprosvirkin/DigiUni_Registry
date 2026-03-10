import java.util.List;
import java.util.Scanner;

public class ModDepartmentUtils {
    //! ======= WORK WITH DEPARTMENT ===== //

    //show menu for department
    static void showDepartmentMenu(Scanner scanner, DepartmentService departmentService, FacultyService facultyService, TeacherService teacherService) {
        System.out.println("1. Add Department");
        System.out.println("2. Manage existing Department");
        System.out.println("0. Back");
        int action = InputUtils.readInt(scanner, "> ", 0, 2);

        if (action == 1) { // add a new department
            ModDepartmentUtils.departmentAddDepartment(scanner, departmentService, facultyService);
        } else if (action == 2) { // manage existing department
            // Select Faculty and Department
            Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculty")
                    .orElseThrow(() -> new EntityNotFoundException("Faculty wasn't chosen or found"));

            Department selectedDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Department")
                    .orElseThrow(()-> new EntityNotFoundException("Department wasn't chosen or found"));


            //Work with a selected department
            System.out.println("\nDepartment: " + selectedDept.getName());
            System.out.println("1. Edit name of the Department");
            System.out.println("2. Delete Department");
            System.out.println("3. Show all Teachers in the Department");
            System.out.println("0. Back");
            int workWithDepartment = InputUtils.readInt(scanner, "> ", 0, 3);

            if (workWithDepartment == 1) { // edit department name
                ModDepartmentUtils.departmentRenameDepartment(scanner, departmentService, selectedDept, selectedFaculty);
            } else if (workWithDepartment == 2) { //delete department
                ModDepartmentUtils.departmentDeleteDepartment(scanner, departmentService, selectedDept, selectedFaculty);
            } else if (workWithDepartment == 3) { //show all teachers in the department
                ModDepartmentUtils.departmentShowTeachers(teacherService, selectedDept, scanner);
            }
        }
    }
    /**
     * Add new Department
     */
    static void departmentAddDepartment(Scanner scanner, DepartmentService departmentService, FacultyService facultyService) {
        System.out.println("Choose faculty where department will be added:");
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties")
                .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't selected or found"));
        String name = InputUtils.readLine(scanner, "Enter new Department name: ", false, false);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        departmentService.addNewDepartment(name, selectedFaculty);

        InputUtils.pause(scanner);
    }

    /**
     * Rename the Department
     */
    static void departmentRenameDepartment(Scanner scanner, DepartmentService departmentService, Department selectedDept, Faculty selectedFaculty) {
        String editName = InputUtils.readLine(scanner, "Write new name for " + selectedDept.getName() + ": ", false, false);
        editName = InputUtils.removeSpaces(editName, false, true, true, true);
        departmentService.editDepartmentName(selectedDept, editName, selectedFaculty);

        InputUtils.pause(scanner);
    }

    /**
     * Delete the Department
     */
    static void departmentDeleteDepartment(Scanner scanner, DepartmentService departmentService, Department selectedDept, Faculty selectedFaculty) {
        System.out.print("Are you sure you want or delete " + selectedDept.getName() + "? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            departmentService.deleteDepartment(selectedDept, selectedFaculty);
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
}
