import java.util.List;
import java.util.Scanner;

public class ModTeacherUtils {
    //! ======= WORK WITH TEACHERS ===== //

    //show menu for teacher
    static void showTeacherMenu(Scanner scanner, TeacherService teacherService, FacultyService facultyService, UniversityService universityService) {
        System.out.println("1. Add Teacher");
        System.out.println("2. Delete Teacher");
        System.out.println("3. Edit information about teacher");
        System.out.println("4. Show all");
        System.out.println("0. Back");
        int workWithTeacher = InputUtils.readInt(scanner, "> ", 0, 4);

        if (workWithTeacher == 1) { //add teacher
            ModTeacherUtils.teacherAddTeacher(scanner, facultyService, teacherService);
        } else if (workWithTeacher == 2) { //delete teacher
            int deleteTeacher= ModEntitiesUtils.chooseDeleting(scanner);
            if (deleteTeacher == 1) {
                System.out.print("Delete teacher by full name ");
                String fullName = InputUtils.readLine(scanner, "Full name of teacher: ", false, false);
                fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
                List<Teacher> result = teacherService.findTeachersByFullName(fullName);

                ModEntitiesUtils.deleteEntity(scanner, result, "Teacher", (teacher -> teacherService.deleteTeacher(teacher, teacher.getDepartment()) ));
            } else if (deleteTeacher == 2) {
                ModTeacherUtils.teacherDeleteById(scanner, universityService);
            }

        } else if (workWithTeacher == 3) { //edit teacher
            int editTeacher = ModEntitiesUtils.chooseEditing(scanner);
            if (editTeacher == 1) {
                ModTeacherUtils.teacherEditByName(scanner, universityService);
            } else if (editTeacher == 2) {
                ModTeacherUtils.teacherEditById(scanner, universityService);
            }
        } else if (workWithTeacher == 4) {//show all
            List<Teacher> teachers = teacherService.getAllTeachers();
            ModEntitiesUtils.showAllEntity(scanner, teachers, "Teachers List");
        }
    }

    //search menu for teacher
    static void searchTeacherMenu(Scanner scanner, TeacherService teacherService, FacultyService facultyService, UniversityService universityService) {
        System.out.println("1. Search by full name");
        System.out.println("2. Search by department");
        System.out.println("3. Search by position");
        System.out.println("0. Back");

        int searchBy = InputUtils.readInt(scanner, "> ", 0, 3);
        if (searchBy == 1) {
            SearchUtils.searchTeacherByName(scanner, teacherService);
        } else if (searchBy == 2) {
            SearchUtils.searchTeacherByDepartment(scanner, universityService);
        } else if (searchBy == 3) {
            SearchUtils.searchTeacherByPosition(scanner, universityService);
        }
    }
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
