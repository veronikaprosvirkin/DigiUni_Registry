package utils;

import java.util.List;
import java.util.Scanner;

import javafx.application.Platform;
import ui.StudentCardWindow;
import ui.TeacherCardWindow;
import utils.input.InputUtils;
import utils.sort.SortUtils;
import utils.namedEntity.NamedEntity;
import person.StudentService;
import person.Student;
import person.TeacherService;
import person.Teacher;
import faculty.FacultyService;
import faculty.Faculty;
import department.Department;
import speciality.Speciality;
import user.Role;
import user.User;
import user.UserService;
import utils.EntityNotFoundException;
import utils.ModEntitiesUtils;

public class SearchUtils {
    //! ======= SEARCH ===== //

    /**
     * Search Student by full name
     */
    public static void searchStudentByName(Scanner scanner, StudentService studentService) {
        String name = InputUtils.readLine(scanner, "Enter full name: ", false, false);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        List<Student> result = studentService.findStudentsByFullName(name);
        if (result.isEmpty()) {
            System.out.println("No students found with this name.");
        } else {
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select sorting method: ");
                result = SortUtils.sortStudents(result, scanner);
            }
            System.out.println(" --- Students found by name part: " + name + " ---");
            printStudentsWithIndexes(result);
            promptToShowStudentCard(scanner, result);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by group in specific Speciality
     */
    public static void searchStudentByGroupSpecific(Scanner scanner, FacultyService facultyService, StudentService studentService) {
        java.util.Optional<Faculty> optFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (optFaculty.isEmpty()) {
            System.out.println("Faculty wasn't selected ot found");
            return;
        }
        Faculty selectedFaculty = optFaculty.get();


        java.util.Optional<Speciality> optSpec = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
        if (optSpec.isEmpty()) {
            System.out.println("Speciality wasn't selected ot found");
            return;
        }
        Speciality selectedSpeciality = optSpec.get();


        int groupNumber = InputUtils.readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

        List<Student> result = studentService.findStudentsInSpecialityByGroup(selectedSpeciality, groupNumber);

        if (result.isEmpty()) {
            System.out.println("No students found in group " + groupNumber + " within " + selectedSpeciality.getName());
        } else {
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select sorting method: ");
                result = SortUtils.sortStudents(result, scanner);
            }
            System.out.println(" --- Students in group " + groupNumber + " on " + selectedSpeciality.getName() + " ---");
            printStudentsWithIndexes(result);
            promptToShowStudentCard(scanner, result);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by group in the whole Univercity
     */
    public static void searchStudentByGroupEverywhere(Scanner scanner, StudentService studentService) {
        int groupNumber = InputUtils.readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

        List<Student> result = studentService.findStudentsByGroup(groupNumber);

        if (result.isEmpty()) {
            System.out.println("No students found in group " + groupNumber + " in the whole university.");
        } else {
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select sorting method: ");
                result = SortUtils.sortStudents(result, scanner);
            }
            System.out.println(" --- Students in group " + groupNumber + " ---");
            printStudentsWithIndexes(result);
            promptToShowStudentCard(scanner, result);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by course
     */
    public static void searchStudentByCourse(Scanner scanner, StudentService studentService) {
        int course = InputUtils.readInt(scanner, "Enter course number: ", 1, 6);
        List<Student> result = studentService.findStudentsByCourse(course);
        if (result.isEmpty()) {
            System.out.println("No students found in course " + course + ".");
        } else {
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select sorting method: ");
                result = SortUtils.sortStudents(result, scanner);
            }
            System.out.println(" --- Students in course " + course + " ---");
            printStudentsWithIndexes(result);
            promptToShowStudentCard(scanner, result);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by speciality
     */
    public static void searchStudentBySpeciality(Scanner scanner, StudentService studentService, FacultyService facultyService) {
        java.util.Optional<Faculty> optFaculty2 = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (optFaculty2.isEmpty()) {
            System.out.println("Faculty wasn't selected ot found");
            return;
        }
        Faculty selectedFaculty2 = optFaculty2.get();


        java.util.Optional<Speciality> optSpec2 = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty2);
        if (optSpec2.isEmpty()) {
            System.out.println("Speciality wasn't selected ot found");
            return;
        }
        Speciality selectedSpeciality2 = optSpec2.get();

        List <Student> result = studentService.findStudentsBySpeciality(selectedSpeciality2);
        if (result.isEmpty()) {
            System.out.println("No students found in " + selectedSpeciality2.getName() + ".");
        } else {
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select sorting method: ");
                result = SortUtils.sortStudents(result, scanner);
            }
            System.out.println(" --- Students in " + selectedSpeciality2.getName() + " ---");
            printStudentsWithIndexes(result);
            promptToShowStudentCard(scanner, result);
        }
        InputUtils.pause(scanner);

    }

    /**
     * Search Teacher by full name
     */
    // Find teachers by name
    public static void searchTeacherByName(Scanner scanner, TeacherService teacherService) {
        String name = InputUtils.readLine(scanner, "Enter full name: ", false, false);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        List<Teacher> result = teacherService.findTeachersByFullName(name);
        if (result.isEmpty()) {
            System.out.println("No teachers found with this name.");
        } else {
            if (result.size() > 1) {
                System.out.println("Multiple teachers found. Please select sorting method: ");
                result = SortUtils.sortTeachers(result, scanner);
            }
            System.out.println(" --- Teachers found by name part: " + name + " ---");
            printTeachersWithIndexes(result);
            promptToShowTeacherCard(scanner, result);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Teacher by department
     */
    public static void searchTeacherByDepartment(Scanner scanner, FacultyService facultyService, TeacherService teacherService) {
        // Select faculty
        java.util.Optional<Faculty> optFaculty3 = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (optFaculty3.isEmpty()) {
            System.out.println("Faculty not found");
            return;
        }
        Faculty selectedFaculty3 = optFaculty3.get();

        // Select department
        java.util.Optional<Department> optDept = ModEntitiesUtils.selectEntity(scanner, selectedFaculty3.getDepartments(), "Departments");
        if (optDept.isEmpty()) {
            System.out.println("Department not found");
            return;
        }
        Department selectedDepartment = optDept.get();

        List<Teacher> result = teacherService.getTeachersByDepartment(selectedDepartment);

        if (result.isEmpty()) {
            System.out.println("No teachers found.");
        } else {
            if (result.size() > 1) {
                System.out.println("Multiple teachers found. Please select sorting method: ");
                result = SortUtils.sortTeachers(result, scanner);
            }
            System.out.println(" --- Teachers in " + selectedDepartment.getName() + " ---");
            printTeachersWithIndexes(result);
            promptToShowTeacherCard(scanner, result);
        }
        InputUtils.pause(scanner);
    }


    /**
     * Search Teacher by position
     */
    public static void searchTeacherByPosition(Scanner scanner, TeacherService teacherService) {
        String rawPosition = InputUtils.readLine(scanner, "Enter position: ", false, false);
        String position = InputUtils.removeSpaces(rawPosition, false, true, true, true);

        // Filter teachers
        List<Teacher> result = teacherService.getAllTeachers().stream()
                .filter(t -> t.getPosition() != null && t.getPosition().toString().toLowerCase().contains(position.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());

        if (result.isEmpty()) {
            System.out.println("No teachers found.");
        } else {
            // Sort
            if (result.size() > 1) {
                System.out.println("Multiple teachers found. Please select sorting method: ");
                result = SortUtils.sortTeachers(result, scanner);
            }
            System.out.println(" --- Teachers by position: " + position + " ---");
            printTeachersWithIndexes(result);
            promptToShowTeacherCard(scanner, result);
        }
        InputUtils.pause(scanner);
    }

    private static void printStudentsWithIndexes(List<Student> students) {
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
    }

    private static void printTeachersWithIndexes(List<Teacher> teachers) {
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i));
        }
    }

    private static void promptToShowStudentCard(Scanner scanner, List<Student> students) {
        if (students == null || students.isEmpty()) {
            return;
        }

        Student selectedStudent = null;

        if (students.size() == 1) {
            selectedStudent = students.get(0);
            System.out.print("\nDo you want to open the graphical Student Card? (y/n): ");
            if (!scanner.hasNextLine())
                return;
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("y") && !answer.equals("yes")) {
                return;
            }
        } else {
            System.out.print("\nEnter the number of the student from the list above to view their card (or press Enter to skip): ");
            if (!scanner.hasNextLine())
                return;
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return;
            }

            try {
                int index = Integer.parseInt(input) - 1;

                if (index >= 0 && index < students.size()) {
                    selectedStudent = students.get(index);
                } else {
                    System.out.println("Invalid number. Skipping...");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Skipping...");
                return;
            }
        }

        if (selectedStudent != null) {
            final Student studentToShow = selectedStudent;
            final boolean showId = canCurrentUserWrite();
            Platform.runLater(() -> {
                try {
                    StudentCardWindow window = new StudentCardWindow();
                    window.open(studentToShow, showId);
                } catch (Exception e) {
                    System.out.println("Error opening Student Card: " + e.getMessage());
                }
            });
        }
    }

    private static void promptToShowTeacherCard(Scanner scanner, List<Teacher> teachers) {
        if (teachers == null || teachers.isEmpty()) {
            return;
        }

        Teacher selectedTeacher = null;

        if (teachers.size() == 1) {
            selectedTeacher = teachers.get(0);
            System.out.print("\nDo you want to open the graphical Teacher Card? (y/n): ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("y") && !answer.equals("yes")) {
                return;
            }
        } else {
            System.out.print("\nEnter the number of the teacher from the list above to view their card (or press Enter to skip): ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return;
            }

            try {
                int index = Integer.parseInt(input) - 1;

                if (index >= 0 && index < teachers.size()) {
                    selectedTeacher = teachers.get(index);
                } else {
                    System.out.println("Invalid number. Skipping...");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Skipping...");
                return;
            }
        }

        if (selectedTeacher != null) {
            final Teacher teacherToShow = selectedTeacher;
            final boolean showId = canCurrentUserWrite();
            Platform.runLater(() -> {
                try {
                    TeacherCardWindow.open(teacherToShow, showId);
                } catch (Exception e) {
                    System.out.println("Error opening Teacher Card: " + e.getMessage());
                }
            });
        }
    }

    private static boolean canCurrentUserWrite() {
        User currentUser = UserService.getInstance().getCurrentUser();
        return currentUser != null
                && (currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.ADMIN);
    }
}
