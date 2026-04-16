package utils;

import java.util.List;
import java.util.Scanner;

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
            result.forEach(System.out::println);
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
            result.forEach(System.out::println);
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
            result.forEach(System.out::println);
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
            result.forEach(System.out::println);
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
            result.forEach(System.out::println);
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
            result.forEach(System.out::println);
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
            result.forEach(System.out::println);
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
            result.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }
}
