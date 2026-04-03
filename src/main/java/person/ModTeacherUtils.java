package person;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import utils.input.InputUtils;
import utils.ModEntitiesUtils;
import utils.IdGenerator;
import utils.SearchUtils;
import utils.sort.SortUtils;
import faculty.Faculty;
import department.Department;
import utils.EntityNotFoundException;
import faculty.FacultyService;
import university.UniversityService;

public class ModTeacherUtils {
    //! ======= WORK WITH TEACHERS ===== //

    //show menu for teacher
    public static void showTeacherMenu(Scanner scanner, TeacherService teacherService, FacultyService facultyService, UniversityService universityService, boolean showId) {
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
    public static void searchTeacherMenu(Scanner scanner, TeacherService teacherService, FacultyService facultyService, UniversityService universityService, boolean showId) {
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
        String patronymic = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Patronymic: ", false, false), true, false, false, false);
        String position = InputUtils.readLine(scanner, "Position: ", false, true);
        position = InputUtils.removeSpaces(position, false, true, true, true);
        
        String email = InputUtils.readLine(scanner, "Enter email (optional, press Enter to skip): ", true, true);
        email = InputUtils.removeSpaces(email, false, true, true, true);
        String phone = InputUtils.readLine(scanner, "Enter phone number (optional, press Enter to skip): ", true, true);
        phone = InputUtils.removeSpaces(phone, false, true, true, true);
        String academicDegree = InputUtils.readLine(scanner, "Enter academic degree (optional, press Enter to skip): ", true, true);
        academicDegree = InputUtils.removeSpaces(academicDegree, false, true, true, true);
        String academicTitle = InputUtils.readLine(scanner, "Enter academic title (optional, press Enter to skip): ", true, true);
        academicTitle = InputUtils.removeSpaces(academicTitle, false, true, true, true);
        String empDateStr = InputUtils.readLine(scanner, "Enter employment date (YYYY-MM-DD, optional, press Enter to skip): ", true, true);
        empDateStr = InputUtils.removeSpaces(empDateStr, false, true, true, true);
        String workloadStr = InputUtils.readLine(scanner, "Enter workload (e.g. 1.0, optional, press Enter to skip): ", true, true);
        workloadStr = InputUtils.removeSpaces(workloadStr, false, true, true, true);

        // Save
        Teacher newTeacher = new Teacher(IdGenerator.generateTeacherId(), name, surname, patronymic, position, selectedDept);
        if (!email.isEmpty()) newTeacher.setEmail(email);
        if (!phone.isEmpty()) newTeacher.setPhone(phone);
        if (!academicDegree.isEmpty()) newTeacher.setAcademicDegree(academicDegree);
        if (!academicTitle.isEmpty()) newTeacher.setAcademicTitle(academicTitle);
        if (!empDateStr.isEmpty()) {
            try {
                newTeacher.setEmploymentDate(LocalDate.parse(empDateStr));
            } catch (Exception e) {
                System.out.println("Invalid date format. Skipping employment date.");
            }
        } else {
            newTeacher.setEmploymentDate(LocalDate.now());
        }
        if (!workloadStr.isEmpty()) {
            try {
                newTeacher.setWorkload(Double.parseDouble(workloadStr));
            } catch (Exception e) {
                System.out.println("Invalid workload format. Skipping workload.");
            }
        }
        
        teacherService.addTeacher(newTeacher);
        
        System.out.println("Teacher " + name + " " + surname +
                " successfully added to department: " + selectedDept.getName());

        InputUtils.pause(scanner);
    }



    /**
     * Delete the Teacher by ID
     */
    static void teacherDeleteById(Scanner scanner, TeacherService teacherService) {
        String id = InputUtils.readLine(scanner, "Enter ID of teacher: ", false, true);
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
        String id = InputUtils.readLine(scanner, "Enter ID of teacher: ", false, true);
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
        System.out.println("4. Change Email");
        System.out.println("5. Change Phone Number");
        System.out.println("6. Change Academic Degree");
        System.out.println("7. Change Academic Title");
        System.out.println("8. Change Employment Date");
        System.out.println("9. Change Workload");
        System.out.println("0. Finish editing");

        int fieldChoice = InputUtils.readInt(scanner, "> ", 0, 9);
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
            case 4 -> {
                String newEmail = InputUtils.readLine(scanner, "Enter new email: ", false, true);
                newEmail = InputUtils.removeSpaces(newEmail, false, true, true, true);
                teacherToProcess.setEmail(newEmail);
                System.out.println("Email updated!");
            }
            case 5 -> {
                String newPhone = InputUtils.readLine(scanner, "Enter new phone number: ", false, true);
                newPhone = InputUtils.removeSpaces(newPhone, false, true, true, true);
                teacherToProcess.setPhone(newPhone);
                System.out.println("Phone number updated!");
            }
            case 6 -> {
                String newDegree = InputUtils.readLine(scanner, "Enter new academic degree: ", false, true);
                newDegree = InputUtils.removeSpaces(newDegree, false, true, true, true);
                teacherToProcess.setAcademicDegree(newDegree);
                System.out.println("Academic degree updated!");
            }
            case 7 -> {
                String newTitle = InputUtils.readLine(scanner, "Enter new academic title: ", false, true);
                newTitle = InputUtils.removeSpaces(newTitle, false, true, true, true);
                teacherToProcess.setAcademicTitle(newTitle);
                System.out.println("Academic title updated!");
            }
            case 8 -> {
                String newDate = InputUtils.readLine(scanner, "Enter new employment date (YYYY-MM-DD): ", false, true);
                newDate = InputUtils.removeSpaces(newDate, false, true, true, true);
                try {
                    teacherToProcess.setEmploymentDate(LocalDate.parse(newDate));
                    System.out.println("Employment date updated!");
                } catch (Exception e) {
                    System.out.println("Invalid date format.");
                }
            }
            case 9 -> {
                String newWorkload = InputUtils.readLine(scanner, "Enter new workload (e.g. 1.0): ", false, true);
                newWorkload = InputUtils.removeSpaces(newWorkload, false, true, true, true);
                try {
                    teacherToProcess.setWorkload(Double.parseDouble(newWorkload));
                    System.out.println("Workload updated!");
                } catch (Exception e) {
                    System.out.println("Invalid workload format.");
                }
            }
        }
        }
    }
}
