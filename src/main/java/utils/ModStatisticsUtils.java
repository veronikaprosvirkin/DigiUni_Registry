package utils;

import department.Department;
import department.DepartmentService;
import faculty.Faculty;
import faculty.FacultyService;
import person.StudentService;
import person.TeacherService;
import speciality.SpecialityService;
import university.University;
import utils.input.InputUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.ToIntFunction;

import static utils.input.InputUtils.pause;

public class ModStatisticsUtils {
    public static void showStatisticsMenu(Scanner scanner, University university, StudentService studentService,
                                          TeacherService teacherService, SpecialityService specialityService) {
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
                case 1 -> showFacultiesStatistics(university, scanner, studentService, teacherService, specialityService);
                case 2 -> showDepartmentsStatistics(university, scanner, teacherService, studentService);
                case 3 -> showSpecialitiesStatistics(university, scanner);
                case 4 -> showTeachersStatistics(university, scanner);
                case 5 -> showStudentsStatistics(university, scanner);
                case 6 -> showUniversityStatistics(university, scanner);
                case 0 -> {
                    return; // Exit the statistics menu
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
             pause(scanner);

        }
    }
    // departments statistics
    private static void showDepartmentsStatistics(University university, Scanner scanner, TeacherService teacherService, StudentService studentService) {
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
                }
                case 2 -> {
                    System.out.println("Average teachers per department: " +
                            averageCount(allDepartments, d -> countTeachers(d,teacherService)));
                }
                case 3 -> {
                    allDepartments.stream().max(Comparator.comparingInt(d -> countTeachers((Department) d, teacherService)))
                            .ifPresent(d -> {System.out.println("Most popular: " + d.getName() + " with "
                                    + countTeachers(d, teacherService));});
                    allDepartments.stream().min(Comparator.comparingInt(d -> countTeachers(d, teacherService)))
                            .ifPresent(d -> {System.out.println("Least popular: " + d.getName() + " with "
                                    + countTeachers(d, teacherService));});
                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    // specialities statistics
    private static void showSpecialitiesStatistics(University university, Scanner scanner) {
        System.out.println("=== Specialities' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Total number of specialities");
            System.out.println("2. Average number of students per speciality"); // Додано!
            System.out.println("3. Most and least popular specialities (by student enrollment)");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 3); // Змінено ліміт на 3

            switch (choice) {
                case 1 -> {
                    // TODO: count all specialities
                }
                case 2 -> {
                    // TODO: average logic for students per speciality
                }
                case 3 -> {
                    // TODO: max/min logic for students
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
    private static void showFacultiesStatistics(University university, Scanner scanner, StudentService studentService, TeacherService teacherService, SpecialityService specialityService) {
        System.out.println("=== Faculties' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Number of faculties");
            System.out.println("2. Average metrics per faculty");
            System.out.println("3. Student population rankings");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 3);

            System.out.println("DEBUG: Faculties count = " + university.getFaculties().size());
            if (!university.getFaculties().isEmpty()) {
                System.out.println("DEBUG: Specs in first faculty = " + university.getFaculties().get(0).getSpecialities().size());
            }

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
                            f -> countStudents(f, studentService)));

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
                    university.getFaculties().stream().max(Comparator.comparingInt(f-> countStudents(f,studentService)))
                            .ifPresent(f -> {System.out.println("Most popular: " + f.getName() + " with " +
                                    countStudents(f,studentService) + " students");});
                    university.getFaculties().stream().min(Comparator.comparingInt(f-> countStudents(f,studentService)))
                            .ifPresent(f -> {System.out.println("Least popular: " + f.getName() + " with " +
                                    countStudents(f,studentService) + " students");});
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
    private static int countStudents(Faculty f, StudentService studentService){
        return studentService.getAllStudents()
                .stream().filter(s-> s.getFaculty().equals(f))
                .toList().size();
    }
    private static int countTeachers(Department d, TeacherService teacherService){
        return teacherService.getTeachersByDepartment(d).size();
    }
}
