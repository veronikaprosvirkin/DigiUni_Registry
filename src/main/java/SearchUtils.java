import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchUtils {
    //! ======= SEARCH ===== //

    /**
     * Search Student by full name
     */
    static void searchStudentByName(Scanner scanner, StudentService studentService) {
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
    static void searchStudentByGroupSpecific(Scanner scanner, FacultyService facultyService, StudentService studentService) {
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties")
                .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't selected ot found"));


        Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty)
                .orElseThrow(()-> new EntityNotFoundException("Speciality wasn't selected ot found"));


        int groupNumber = InputUtils.readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

        List<Student> results = studentService.findStudentsInSpecialityByGroup(selectedSpeciality, groupNumber);

        if (results.isEmpty()) {
            System.out.println("No students found in group " + groupNumber + " within " + selectedSpeciality.getName());
        } else {
            System.out.println(" --- Students in group " + groupNumber + " on " + selectedSpeciality.getName() + " ---");
            results.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by group in the whole Univercity
     */
    static void searchStudentByGroupEverywhere(Scanner scanner, StudentService studentService) {
        int groupNumber = InputUtils.readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

        List<Student> results = studentService.findStudentsByGroup(groupNumber);

        if (results.isEmpty()) {
            System.out.println("No students found in group " + groupNumber + " in the whole university.");
        } else {
            System.out.println(" --- Students in group " + groupNumber + " ---");
            results.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by course
     */
    static void searchStudentByCourse(Scanner scanner, StudentService studentService) {
        int course = InputUtils.readInt(scanner, "Enter course number: ", 1, 6);
        List<Student> results = studentService.findStudentsByCourse(course);
        if (results.isEmpty()) {
            System.out.println("No students found in course " + course + ".");
        } else {
            System.out.println(" --- Students in course " + course + " ---");
            results.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Student by speciality
     */
    static void searchStudentBySpeciality(Scanner scanner, StudentService studentService, FacultyService facultyService) {
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties")
                .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't selected ot found"));


        Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty)
                .orElseThrow(()-> new EntityNotFoundException("Speciality wasn't selected ot found"));

        List <Student> result = studentService.findStudentsBySpeciality(selectedSpeciality);
        if (result.isEmpty()) {
            System.out.println("No students found in " + selectedSpeciality.getName() + ".");
        } else {
            System.out.println(" --- Students in " + selectedSpeciality.getName() + " ---");
            result.forEach(System.out::println);
        }
        InputUtils.pause(scanner);

    }

    /**
     * Search Teacher by full name
     */
    // Find teachers by name
    static void searchTeacherByName(Scanner scanner, TeacherService teacherService) {
        String name = InputUtils.readLine(scanner, "Enter full name: ", false, false);
        name = InputUtils.removeSpaces(name, false, true, true, true);
        List<Teacher> result = teacherService.findTeachersByFullName(name);
        if (result.isEmpty()) {
            System.out.println("No teachers found with this name.");
        } else {
            System.out.println(" --- Teachers found by name part: " + name + " ---");
            result.forEach(System.out::println);
        }
        InputUtils.pause(scanner);
    }

    /**
     * Search Teacher by department
     */
    static void searchTeacherByDepartment(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED
    }

    /**
     * Search Teacher by position
     */
    static void searchTeacherByPosition(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED
    }
}
