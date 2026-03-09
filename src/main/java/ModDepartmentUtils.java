import java.util.List;
import java.util.Scanner;

public class ModDepartmentUtils {
    //! ======= WORK WITH DEPARTMENT ===== //

    /**
     * Add new Department
     */
    static void departmentAddDepartment(Scanner scanner, DepartmentService departmentService, FacultyService facultyService) {
        System.out.println("Choose faculty where department will be added:");
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (selectedFaculty != null) {
            String name = InputUtils.readLine(scanner, "Enter new Department name: ", false, false);
            name = InputUtils.removeSpaces(name, false, true, true, true);
            departmentService.addNewDepartment(name, selectedFaculty);
        } else {
            System.out.println("No faculties found. Please add a new one first.");
        }
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
        System.out.print("Are you sure you want ot delete " + selectedDept.getName() + "? (y/n): ");
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
