package person;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

import university.University;
import user.UserService;
import utils.*;
import utils.input.InputUtils;
import utils.sort.SortUtils;
import faculty.Faculty;
import department.Department;
import faculty.FacultyService;
import university.UniversityService;

public class ModTeacherUtils {
    //! ======= WORK WITH TEACHERS ===== //

    //show menu for teacher
    @SuppressWarnings("java:S107")
    public static void showTeacherMenu(Scanner scanner, TeacherService teacherService, FacultyService facultyService,
                                       UserService userService, boolean showId, University university, StudentService studentService) {
        System.out.println("1. Add Teacher");
        System.out.println("2. Delete Teacher");
        System.out.println("3. Edit information about teacher");
        System.out.println("4. Show all");
        System.out.println("0. Back");
        int workWithTeacher = InputUtils.readInt(scanner, "> ", 0, 4);

        if (workWithTeacher == 1) { //add teacher
            ModTeacherUtils.teacherAddTeacher(scanner, facultyService, teacherService, university, userService, studentService);
        } else if (workWithTeacher == 2) { //delete teacher
            int deleteTeacher= ModEntitiesUtils.chooseDeleting(scanner);
            if (deleteTeacher == 1) {
                System.out.print("Delete teacher by full name ");
                String fullName = InputUtils.readLine(scanner, "Full name of teacher: ", false, false);
                fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
                List<Teacher> result = teacherService.findTeachersByFullName(fullName);

                ModEntitiesUtils.deleteEntity(scanner, result, "Teacher", teacherService::deleteTeacher);
                FileStorageUtils.saveAll(university, userService);
            } else if (deleteTeacher == 2) {
                ModTeacherUtils.teacherDeleteById(scanner, teacherService, university, userService);
            }

        } else if (workWithTeacher == 3) { //edit teacher
            int editTeacher = ModEntitiesUtils.chooseEditing(scanner);
            if (editTeacher == 1) {
                ModTeacherUtils.teacherEditByName(scanner, teacherService, university, userService);
            } else if (editTeacher == 2) {
                ModTeacherUtils.teacherEditById(scanner, teacherService, university, userService);
            }
        } else if (workWithTeacher == 4) {//show all
            List<Teacher> teachers = teacherService.getAllTeachers();
            if (teachers.size()>1){
                System.out.println("Multiple teachers found. Please select sorting method: ");
                List<Teacher> sortedTeachers = SortUtils.sortTeachers(teachers, scanner);
                ModEntitiesUtils.showAllEntity(scanner, sortedTeachers, "Teachers List", showId);
                return;
            }
            ModEntitiesUtils.showAllEntity(scanner, teachers, "Teachers List", showId);
        }
    }

    //search menu for teacher
    public static void searchTeacherMenu(Scanner scanner, TeacherService teacherService, FacultyService facultyService, @SuppressWarnings("unused") UniversityService universityService, boolean showId) {
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
                List<Teacher> sortedTeachers = SortUtils.sortTeachers(teachers, scanner);
                ModEntitiesUtils.showAllEntity(scanner, sortedTeachers, "Teachers List", showId);
                return;
            }
            ModEntitiesUtils.showAllEntity(scanner, teachers, "Teachers List", showId);
        }
    }
    /**
     * Add new Teacher
     */
    static void teacherAddTeacher(Scanner scanner, FacultyService facultyService, TeacherService teacherService,
                                  University university, UserService userService, StudentService studentService) {
        System.out.println("--- Add Teacher ---");
        java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (optFaculty.isEmpty()) {
            throw new EntityNotFoundException("Faculty wasn't selected or found");
        }
        Faculty selectedFaculty = optFaculty.get();

        // Select Department
        java.util.Optional<Department> optDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Departments in " + selectedFaculty.getName());
        if (optDept.isEmpty()) {
            System.out.println("Department wasn't selected or found");
            return;
        }
        Department selectedDept = optDept.get();

        // Teachers's info
        String name = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Name: ", false, false), true, false, false, false);
        String surname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Surname: ", false, false), true, false, false, false);
        String patronymic = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Patronymic: ", false, false), true, false, false, false);
        
        // Position selection using enum
        System.out.println("Available positions:");
        Position[] positions = Position.values();
        for (int i = 0; i < positions.length; i++) {
            System.out.println((i + 1) + ". " + positions[i].getDisplayName());
        }
        int posChoice = InputUtils.readInt(scanner, "> ", 1, positions.length);
        Position position = positions[posChoice - 1];

        String domain = "@digiuni.ukma.edu";
        String finalEmail = InputUtils.readAndValidateEmail(
                scanner,
                domain,
                () -> ModEntitiesUtils.generateFullEmail(name, surname, domain),
                email -> ModEntitiesUtils.isEmailGloballyTaken(email, studentService, teacherService)
        );

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

        String dobStr = InputUtils.readLine(scanner, "Enter date of birth (YYYY-MM-DD, optional, press Enter to skip): ", true, true);
        dobStr = InputUtils.removeSpaces(dobStr, false, true, true, true);
        LocalDate dateOfBirth = null;
        if (!dobStr.isEmpty()) {
            try {
                dateOfBirth = LocalDate.parse(dobStr);
            } catch (Exception e) {
                System.out.println("Invalid date format. Skipping date of birth.");
            }
        }

        // Save
        Teacher newTeacher = new Teacher(IdGenerator.generateTeacherId(), name, surname, patronymic, position, selectedDept, dateOfBirth);
        System.out.println("Detected gender: " + newTeacher.getGender());
        if (!finalEmail.isEmpty()) newTeacher.setEmail(finalEmail);
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
        FileStorageUtils.saveAll(university, userService);
        
        System.out.println("Teacher " + name + " " + surname +
                " successfully added to department: " + selectedDept.getName());

        InputUtils.pause(scanner);
    }



    /**
     * Delete the Teacher by ID
     */
    static void teacherDeleteById(Scanner scanner, TeacherService teacherService, University university, UserService userService) {
        String id = InputUtils.readLine(scanner, "Enter ID of teacher: ", false, true);
        List<Teacher> result = teacherService.findTeacherById(id);
        ModEntitiesUtils.deleteEntity(scanner, result, "Teacher", teacherService::deleteTeacher);
        FileStorageUtils.saveAll(university, userService);
    }

    /**
     * Edit the Teacher by name
     */
    static void teacherEditByName(Scanner scanner, TeacherService teacherService, University university, UserService userService) {
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
            editTeacherDetails(scanner, teacherToProcess, university, userService);
        }
    }

    /**
     * Edit the Teacher by ID
     */
    static void teacherEditById(Scanner scanner, TeacherService teacherService, University university, UserService userService) {
        String id = InputUtils.readLine(scanner, "Enter ID of teacher: ", false, true);
        List<Teacher> result = teacherService.findTeacherById(id);
        if (result.isEmpty()){
            System.out.println("No teacher found by id " + id);
        } else {
            Teacher teacherToProcess = result.get(0);
            editTeacherDetails(scanner, teacherToProcess, university, userService);

        }
    }

    private static void editTeacherDetails(Scanner scanner, Teacher teacherToProcess, University university, UserService userService) {
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
        System.out.println("10. Change Date of Birth");
        System.out.println("11. Change Gender");
        System.out.println("0. Finish editing");

        int fieldChoice = InputUtils.readInt(scanner, "> ", 0, 11);
        if (fieldChoice == 0) {
            FileStorageUtils.saveAll(university, userService);
            break;
        }

        switch (fieldChoice) {
            case 1 -> {
                //? Update surname
                String newSurname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new surname: ", false, true), true, false, false, false);
                teacherToProcess.setSurname(newSurname);
                System.out.println("Surname updated!");
            }
            case 2 -> {
                //? Update name
                String newName = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new name: ", false, true), true, false, false, false);
                teacherToProcess.setName(newName);
                System.out.println("Name updated!");
            }
            case 3 -> {
                //? Update position
                System.out.println("Available positions:");
                Position[] positions = Position.values();
                for (int i = 0; i < positions.length; i++) {
                    System.out.println((i + 1) + ". " + positions[i].getDisplayName());
                }
                int posChoice = InputUtils.readInt(scanner, "> ", 1, positions.length);
                Position newPosition = positions[posChoice - 1];
                teacherToProcess.setPosition(newPosition.toString());
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
            case 10 -> {
                String newDob = InputUtils.readLine(scanner, "Enter new date of birth (YYYY-MM-DD, empty to clear): ", true, true);
                newDob = InputUtils.removeSpaces(newDob, false, true, true, true);
                if (newDob.isEmpty()) {
                    teacherToProcess.setDateOfBirth(null);
                    System.out.println("Date of birth cleared!");
                } else {
                    try {
                        teacherToProcess.setDateOfBirth(LocalDate.parse(newDob));
                        System.out.println("Date of birth updated!");
                    } catch (Exception e) {
                        System.out.println("Invalid date format.");
                    }
                }
            }
            case 11 -> {
                Gender newGender = chooseGender(scanner);
                teacherToProcess.changeGender(newGender);
                System.out.println("Gender updated to: " + teacherToProcess.getGender());
            }
        }
        }
    }

    private static Gender chooseGender(Scanner scanner) {
        System.out.println("Select gender:");
        Gender[] genders = Gender.values();
        for (int i = 0; i < genders.length; i++) {
            System.out.println((i + 1) + ". " + genders[i].getDisplayName());
        }
        int choice = InputUtils.readInt(scanner, "> ", 1, genders.length);
        return genders[choice - 1];
    }
}
