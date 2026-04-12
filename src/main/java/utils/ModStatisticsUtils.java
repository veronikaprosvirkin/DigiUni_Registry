package utils;

import department.Department;
import department.DepartmentService;
import faculty.Faculty;
import faculty.FacultyService;
import person.Student;
import person.StudentService;
import person.StudyForm;
import person.TeacherService;
import speciality.Speciality;
import speciality.SpecialityService;
import university.University;
import utils.input.InputUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static utils.input.InputUtils.pause;

public class ModStatisticsUtils {
    public static void showStatisticsMenu(Scanner scanner, University university, StudentService studentService,
                                          TeacherService teacherService, SpecialityService specialityService, List<Faculty> faculties) {
        String title = "=====Popularity ranking=====";
        while (true) {
            System.out.println("=== Statistics Menu ===");
            System.out.println("1. Faculties' statistics");
            System.out.println("2. Departments' statistics");
            System.out.println("3. Specialities' statistics");
            System.out.println("4. Teachers' statistics");
            System.out.println("5. Students' statistics");
            System.out.println("6. University-wide statistics");
            System.out.println("0. Back to main menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 6);
            switch (choice) {
                case 1 -> showFacultiesStatistics(university, scanner, studentService, teacherService, faculties, title);
                case 2 -> showDepartmentsStatistics(university, scanner, teacherService, studentService, title);
                case 3 -> showSpecialitiesStatistics(university, scanner, studentService, title);
                case 4 -> showTeachersStatistics(university, scanner);
                case 5 -> showStudentsStatistics(university, scanner);
                case 6 -> showUniversityStatistics(university, scanner);
                case 0 -> {
                    return; // Exit the statistics menu
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }

        }
    }
    // departments statistics
    private static void showDepartmentsStatistics(University university, Scanner scanner, TeacherService teacherService,
                                                  StudentService studentService, String title) {
        List<Department> allDepartments = university.getFaculties().stream()
                .flatMap(f -> f.getDepartments().stream())
                .toList();
        System.out.println("=== Departments' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Total number of departments");
            System.out.println("2. Average teachers per department");
            System.out.println("3. Largest and smallest departments (by teacher count)");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 3);

            switch (choice) {
                case 1 -> {
                    System.out.println("Number of departments: " + allDepartments.size());
                    pause(scanner);
                }
                case 2 -> {
                    System.out.println("Average teachers per department: " +
                            averageCount(allDepartments, d -> countTeachers(d,teacherService)));
                    pause(scanner);
                }
                case 3 -> {
                    printLeaders(allDepartments, d -> countTeachers(d, teacherService),
                            Department::getName, title);
                    printOutsiders(allDepartments, d -> countTeachers(d, teacherService),
                            Department::getName);
                    pause(scanner);
                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    // specialities statistics
    private static void showSpecialitiesStatistics(University university, Scanner scanner, StudentService studentService, String title) {
        List<Speciality> allSpecialities = university.getFaculties().stream()
                .flatMap(f -> f.getSpecialities().stream())
                .toList();
        System.out.println("=== Specialities' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Total number of specialities");
            System.out.println("2. Average number of students per speciality"); // Додано!
            System.out.println("3. Most and least popular specialities (by student enrollment)");
            System.out.println("4. Budget vs Contract distribution");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 4);

            switch (choice) {
                case 1 -> {
                    System.out.println("Number of specialities: " + allSpecialities.size());
                    pause(scanner);
                }
                case 2 -> {
                    System.out.println("Average students per speciality :" +
                            averageCount(allSpecialities, s -> countStudents(s, studentService, Student::getSpeciality)));
                }
                case 3 -> {
                    printLeaders(allSpecialities, s -> countStudents(s, studentService, Student::getSpeciality),
                            Speciality::getNameOfSpeciality, title);
                    printOutsiders(allSpecialities, s -> countStudents(s, studentService, Student::getSpeciality),
                            Speciality::getNameOfSpeciality);
                    pause(scanner);
                }
                case 4 -> {
                    printLeaders(allSpecialities, s -> countStudentsByFinancing(s, studentService, Student::getSpeciality, StudyForm.BUDGET),
                            Speciality::getNameOfSpeciality, "Budget students popularity");
                    printOutsiders(allSpecialities, s -> countStudentsByFinancing(s, studentService, Student::getSpeciality, StudyForm.CONTRACT),
                            Speciality::getNameOfSpeciality);
                    pause(scanner);

                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    //teachers statistics
    private static void showTeachersStatistics(University university, Scanner scanner) {
        System.out.println("=== Teachers' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Total number of teachers in the university");
            System.out.println("2. Number of deans vs regular teachers"); // Якщо декан - це просто поле у факультеті, можна рахувати тут
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 2);

            switch (choice) {
                case 1 -> {
                    // TODO: count all teachers
                }
                case 2 -> {
                    // TODO: filter deans logic
                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    // students statistics
    private static void showStudentsStatistics(University university, Scanner scanner) {
        System.out.println("=== Students' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Total number of students in the university");
            System.out.println("2. Distribution of students by course/year of study"); // Дуже крута статистика, якщо у класі Student є поле 'course'
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 2);

            switch (choice) {
                case 1 -> {
                    // TODO: count all students
                }
                case 2 -> {
                    // TODO: grouping/counting logic
                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    //uni statistics
    private static void showUniversityStatistics(University university, Scanner scanner) {
        System.out.println("=== University-wide Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Grand Total Summary (Faculties, Departments, Specialities, Teachers, Students)");
            System.out.println("2. Overall Student-to-Teacher ratio");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 2);

            switch (choice) {
                case 1 -> {
                    // TODO: call multiple size() methods to print a beautiful summary dashboard
                }
                case 2 -> {
                    // TODO: simple math logic (total students / total teachers)
                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    // faculties statistics
    private static void showFacultiesStatistics(University university, Scanner scanner, StudentService studentService,
                                                TeacherService teacherService, List<Faculty> faculties, String title) {
        System.out.println("=== Faculties' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Number of faculties");
            System.out.println("2. Average metrics per faculty");
            System.out.println("3. Student population rankings");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 3);

            switch (choice) {
                case 1 -> {
                    System.out.println("Number of faculties: " + university.getFaculties().size());
                    pause(scanner);
                }
                case 2 -> {
                    System.out.println("Average departments per faculty: " + averageCount(university.getFaculties(), f -> f.getDepartments().size()));

                    System.out.println("Average specialities per faculty: " + averageCount(university.getFaculties(),
                            f -> f.getSpecialities().size()));

                    System.out.println("Average students per faculty: " + averageCount(university.getFaculties(),
                            f -> countStudents(f, studentService, Student::getFaculty)));

                    System.out.println("Average teachers per faculty: " + averageCount(university.getFaculties(),
                            f -> f.getDepartments().stream().mapToInt(d -> teacherService.getTeachersByDepartment(d)
                                    .size()).sum()));
                    pause(scanner);
                }
                case 3 ->{
                    if (university.getFaculties().isEmpty()) {
                        System.out.println("No faculties found to calculate rankings.");
                        return;
                    }

                    printLeaders(faculties, f-> countStudents(f, studentService, Student::getFaculty),
                            Faculty::getNameOfFaculty, title );
                    printOutsiders(faculties, f-> countStudents(f, studentService, Student::getFaculty), Faculty::getNameOfFaculty);
                    pause(scanner);
                }
                case 0 -> {
                    return;
                }
            }
        }

    }
    private static <T> double averageCount(List<T> items, ToIntFunction<T> numberExtractor){
        return items.stream().mapToInt(numberExtractor).average().orElse(0.0);
    }

    private static <T> int countStudents(T item, StudentService studentService, Function<Student, T> studentExtractor){
        return studentService.getAllStudents()
                .stream().filter(s-> studentExtractor.apply(s).equals(item))
                .toList().size();
    }
    private static <T> int countStudentsByFinancing (T item, StudentService studentService, Function<Student, T> studentExtractor, StudyForm studyForm){
        return studentService.getAllStudents()
                .stream().filter(s-> studentExtractor.apply(s).equals(item)&& s.getStudyForm() == studyForm)
                .toList().size();
    }

    private static int countTeachers(Department d, TeacherService teacherService){
        return teacherService.getTeachersByDepartment(d).size();
    }

    private static <T> void printLeaders (List<T> items, ToIntFunction<T> valueExtractor, Function<T, String> nameExtractor, String title){
        if(items == null || items.isEmpty()){
            System.out.println("No data available for " + title);
            return;
        }
        int maxNumber = items.stream().mapToInt(valueExtractor).max().orElse(0);
        System.out.println(title);
        System.out.println("Max count: " + maxNumber);
        items.stream()
                .filter(t -> maxNumber == valueExtractor.applyAsInt(t))
                .forEach(t -> System.out.println(" - " + nameExtractor.apply(t)));
    }

    private static <T> void printOutsiders(List <T> items, ToIntFunction<T> value, Function<T, String> name){
        if (items == null || items.isEmpty()){
            System.out.println("No data available");
            return;
        }
        int minNumber = items.stream().mapToInt(value).min().orElse(0);
        System.out.println("Min count: "+ minNumber);
        items.stream().filter(t -> minNumber == value.applyAsInt(t)).
                forEach(t -> System.out.println(" - " + name.apply(t)));
    }
}
