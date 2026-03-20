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
    static void searchStudentByGroupEverywhere(Scanner scanner, StudentService studentService) {
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
    static void searchStudentByCourse(Scanner scanner, StudentService studentService) {
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
    static void searchStudentBySpeciality(Scanner scanner, StudentService studentService, FacultyService facultyService) {
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties")
                .orElseThrow(()-> new EntityNotFoundException("Faculty wasn't selected ot found"));


        Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty)
                .orElseThrow(()-> new EntityNotFoundException("Speciality wasn't selected ot found"));

        List <Student> result = studentService.findStudentsBySpeciality(selectedSpeciality);
        if (result.isEmpty()) {
            System.out.println("No students found in " + selectedSpeciality.getName() + ".");
        } else {
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select sorting method: ");
                result = SortUtils.sortStudents(result, scanner);
            }
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
    static void searchTeacherByDepartment(Scanner scanner, FacultyService facultyService, TeacherService teacherService) {
        // Select faculty
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties")
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found"));

        // Select department
        Department selectedDepartment = ModEntitiesUtils.selectEntity(scanner, selectedFaculty.getDepartments(), "Departments")
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));

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
    static void searchTeacherByPosition(Scanner scanner, TeacherService teacherService) {
        String rawPosition = InputUtils.readLine(scanner, "Enter position: ", false, false);
        String position = InputUtils.removeSpaces(rawPosition, false, true, true, true);

        // Filter teachers
        List<Teacher> result = teacherService.getAllTeachers().stream()
                .filter(t -> t.getPosition().toLowerCase().contains(position.toLowerCase()))
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
