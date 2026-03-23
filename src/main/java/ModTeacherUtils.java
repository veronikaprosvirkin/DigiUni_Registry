import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ModTeacherUtils {
    //! ======= WORK WITH TEACHERS ===== //

    //show menu for teacher
    static void showTeacherMenu(Scanner scanner, TeacherService teacherService, FacultyService facultyService, UniversityService universityService, boolean showId) {
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
                ModTeacherUtils.teacherDeleteById(scanner, teacherService);
            }

        } else if (workWithTeacher == 3) { //edit teacher
            int editTeacher = ModEntitiesUtils.chooseEditing(scanner);
            if (editTeacher == 1) {
                ModTeacherUtils.teacherEditByName(scanner, teacherService);
            } else if (editTeacher == 2) {
                ModTeacherUtils.teacherEditById(scanner, teacherService);
            }
        } else if (workWithTeacher == 4) {//show all
            List<Teacher> teachers = teacherService.getAllTeachers();
            if (teachers.size()>1){
                System.out.println("Multiple teachers found. Please select sorting method: ");
                teachers = SortUtils.sortTeachers(teachers, scanner);
            }
            ModEntitiesUtils.showAllEntity(scanner, teachers, "Teachers List", showId);
        }
    }

    //search menu for teacher
    static void searchTeacherMenu(Scanner scanner, TeacherService teacherService, FacultyService facultyService, UniversityService universityService, boolean showId) {
        System.out.println("1. Search by full name");
        System.out.println("2. Search by department");
        System.out.println("3. Search by position");
        System.out.println("4. Show all");
        System.out.println("0. Back");

        int searchBy = InputUtils.readInt(scanner, "> ", 0, 4);
        if (searchBy == 1) {
            SearchUtils.searchTeacherByName(scanner, teacherService);
        } else if (searchBy == 2) {
            SearchUtils.searchTeacherByDepartment(scanner, facultyService, teacherService);
        } else if (searchBy == 3) {
            SearchUtils.searchTeacherByPosition(scanner, teacherService);
        } else  if (searchBy == 4) {
            List<Teacher> teachers = teacherService.getAllTeachers();
            if (teachers.size()>1){
                System.out.println("Multiple teachers found. Please select sorting method: ");
                teachers = SortUtils.sortTeachers(teachers, scanner);
            }
            ModEntitiesUtils.showAllEntity(scanner, teachers, "Teachers List", showId);
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
    static void teacherDeleteById(Scanner scanner, TeacherService teacherService) {
        String id = InputUtils.readLine(scanner, "Enter ID of teacher: ", false, false);
        List<Teacher> result = teacherService.findTeacherById(id);
        ModEntitiesUtils.deleteEntity(scanner, result, "Teacher", (teacher -> teacherService.deleteTeacher(teacher, teacher.getDepartment())));
    }

    /**
     * Edit the Teacher by name
     */
    static void teacherEditByName(Scanner scanner, TeacherService teacherService) {
        String fullName = InputUtils.readLine(scanner, "Enter full name part: ", false, false);
        fullName = InputUtils.removeSpaces(fullName, false, true, true, true);

        // Find teachers
        List<Teacher> result = teacherService.findTeachersByFullName(fullName);

        if (result.isEmpty()) {
            System.out.println("No teachers found with this name.");
        } else {
            Teacher teacherToProcess;
            // Select if multiple
            if (result.size() > 1) {
                System.out.println("Multiple teachers found. Please select one: ");
                // Sort alphabetically
                result.sort(Comparator.comparing(Teacher::getFullName));
                for (int i = 0; i < result.size(); i++) {
                    System.out.println((i + 1) + ". " + result.get(i).getFullName() +
                            " (" + result.get(i).getPosition() + ")");
                }
                int index = InputUtils.readInt(scanner, "> ", 1, result.size());
                teacherToProcess = result.get(index - 1);
            } else {
                teacherToProcess = result.get(0);
            }
            editTeacherDetails(scanner, teacherToProcess);
        }
    }

    /**
     * Edit the Teacher by ID
     */
    static void teacherEditById(Scanner scanner, TeacherService teacherService) {
        String id = InputUtils.readLine(scanner, "Enter ID of teacher: ", false, false);
        List<Teacher> result = teacherService.findTeacherById(id);
        if (result.isEmpty()){
            System.out.println("No teacher found by id " + id);
        } else {
            Teacher teacherToProcess = result.get(0);
            editTeacherDetails(scanner, teacherToProcess);

        }
    }

    private static void editTeacherDetails(Scanner scanner, Teacher teacherToProcess){
        while(true){
        System.out.println("\nEditing teacher: " + teacherToProcess.getFullName());
        System.out.println("1. Change Surname");
        System.out.println("2. Change Name");
        System.out.println("3. Change Position");
        System.out.println("0. Cancel");

        int fieldChoice = InputUtils.readInt(scanner, "> ", 0, 3);
        if (fieldChoice == 0) { break;}

        switch (fieldChoice) {
            case 1 -> {
                //? Update surname
                String newSurname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new surname: ", false, false), true, false, false, false);
                teacherToProcess.setSurname(newSurname);
                System.out.println("Surname updated!");
            }
            case 2 -> {
                //? Update name
                String newName = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new name: ", false, false), true, false, false, false);
                teacherToProcess.setName(newName);
                System.out.println("Name updated!");
            }
            case 3 -> {
                //? Update position
                String newPosition = InputUtils.readLine(scanner, "Enter new position: ", false, true);
                newPosition = InputUtils.removeSpaces(newPosition, false, true, true, true);
                teacherToProcess.setPosition(newPosition);
                System.out.println("Position updated!");
            }
        }
        }
    }
}
