import java.util.Scanner;

public class ModTeacherUtils {
    //! ======= WORK WITH TEACHERS ===== //

    /**
     * Add new Teacher
     */
    static void teacherAddTeacher(Scanner scanner, FacultyService facultyService, TeacherService teacherService) {
        System.out.println("--- Add Teacher ---");
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties")
                .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't selected ot found"));

        // Select Department
        Department selectedDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Departments in " + selectedFaculty.getName())
                .orElseThrow(()-> new EntityNotFoundException("Department wasn't selected ot found"));

        // Teachers's info
        String name = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Name: ", false, false), true, false, false, false);
        String surname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Surname: ", false, false), true, false, false, false);
        String position = InputUtils.readLine(scanner, "Position: ", false, true);
        position = InputUtils.removeSpaces(position, false, true, true, true);


        // Save
        Teacher t = new Teacher(name, surname, position, selectedDept);
        teacherService.addTeacher(name, surname, position, selectedDept);
        System.out.println("Teacher " + name + " " + surname +
                " successfully added to department: " + selectedDept.getName());

        InputUtils.pause(scanner);
    }

    /**
     * Delete the Teacher by name
     */


    /**
     * Delete the Teacher by ID
     */
    static void teacherDeleteById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }

    /**
     * Edit the Teacher by name
     */
    static void teacherEditByName(Scanner scanner, UniversityService universityService) {
        String fullName = InputUtils.readLine(scanner, "Enter full name part: ", false, false);
        fullName = InputUtils.removeSpaces(fullName,false, true, true, true);
        //to be continued
    }

    /**
     * Edit the Teacher by ID
     */
    static void teacherEditById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }
}
