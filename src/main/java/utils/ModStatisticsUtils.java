package utils;

import department.Department;
import faculty.Faculty;
import person.*;
import speciality.Speciality;
import utils.dto.FacultyStats;
import utils.input.InputUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static utils.input.InputUtils.pause;

// NETWORK IMPORTS
import service.NetworkClient;
import service.Request;
import service.Response;

public class ModStatisticsUtils {

    public static void showStatisticsMenu(Scanner scanner) {
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
                case 1 -> showFacultiesStatistics(scanner, title);
                case 2 -> showDepartmentsStatistics(scanner, title);
                case 3 -> showSpecialitiesStatistics(scanner, title);
                case 4 -> showTeachersStatistics(scanner);
                case 5 -> showStudentsStatistics(scanner);
                case 6 -> showUniversityStatistics(scanner);
                case 0 -> { return; }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    // departments statistics
    private static void showDepartmentsStatistics(Scanner scanner, String title) {
        List<Faculty> faculties = fetchFaculties();
        List<Teacher> allTeachers = fetchTeachers();

        List<Department> allDepartments = faculties.stream()
                .flatMap(f -> f.getDepartments().stream())
                .toList();

        System.out.println("=== Departments' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Total number of departments");
            System.out.println("2. Average teachers per department");
            System.out.println("3. Largest and smallest departments (by teacher count)");
            System.out.println("4. Distribution of teachers by gender in departments");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 4);

            switch (choice) {
                case 1 -> {
                    System.out.println("Number of departments: " + allDepartments.size());
                    pause(scanner);
                }
                case 2 -> {
                    System.out.println("Average teachers per department: " +
                            averageCount(allDepartments, d -> countTeachers(d, allTeachers)));
                    pause(scanner);
                }
                case 3 -> {
                    printLeaders(allDepartments, d -> countTeachers(d, allTeachers), Department::getName, title);
                    printOutsiders(allDepartments, d -> countTeachers(d, allTeachers), Department::getName);
                    pause(scanner);
                }
                case 4 -> {
                    System.out.println("\n=== Gender Distribution of teachers by Department ===");
                    for (Department d : allDepartments) {
                        List<Teacher> teachersInDepartment = allTeachers.stream()
                                .filter(t -> t.getDepartment() != null && t.getDepartment().getId().equals(d.getId()))
                                .toList();
                        printGenderStatistics(teachersInDepartment, "Teachers in " + d.getName());
                    }
                    pause(scanner);
                }
                case 0 -> { return; }
            }
        }
    }

    // specialities statistics
    private static void showSpecialitiesStatistics(Scanner scanner, String title) {
        List<Faculty> faculties = fetchFaculties();
        List<Student> allStudents = fetchStudents();

        List<Speciality> allSpecialities = faculties.stream()
                .flatMap(f -> f.getSpecialities().stream()) // Note: If your IDE says getSpecialities() is wrong, change it to getSpeciality()
                .toList();

        System.out.println("=== Specialities' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Total number of specialities");
            System.out.println("2. Average number of students per speciality");
            System.out.println("3. Most and least popular specialities (by student enrollment)");
            System.out.println("4. Budget vs Contract distribution");
            System.out.println("5. Distribution of students by gender in specialities");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 5);

            switch (choice) {
                case 1 -> {
                    System.out.println("Number of specialities: " + allSpecialities.size());
                    pause(scanner);
                }
                case 2 -> {
                    System.out.println("Average students per speciality :" +
                            averageCount(allSpecialities, s -> countStudents(s, allStudents, Student::getSpeciality)));
                    pause(scanner);
                }
                case 3 -> {
                    printLeaders(allSpecialities, s -> countStudents(s, allStudents, Student::getSpeciality),
                            Speciality::getNameOfSpeciality, title);
                    printOutsiders(allSpecialities, s -> countStudents(s, allStudents, Student::getSpeciality),
                            Speciality::getNameOfSpeciality);
                    pause(scanner);
                }
                case 4 -> {
                    printLeaders(allSpecialities, s -> countStudentsByFinancing(s, allStudents, Student::getSpeciality, StudyForm.BUDGET),
                            Speciality::getNameOfSpeciality, "Budget students popularity");
                    printOutsiders(allSpecialities, s -> countStudentsByFinancing(s, allStudents, Student::getSpeciality, StudyForm.CONTRACT),
                            Speciality::getNameOfSpeciality);
                    pause(scanner);
                }
                case 5 -> {
                    System.out.println("\n=== Gender Distribution by Speciality ===");
                    for (Speciality s : allSpecialities) {
                        List<Student> studentsInSpeciality = allStudents.stream()
                                .filter(st -> st.getSpeciality() != null && st.getSpeciality().getId().equals(s.getId()))
                                .toList();
                        printGenderStatistics(studentsInSpeciality, s.getNameOfSpeciality());
                    }
                    pause(scanner);
                }
                case 0 -> { return; }
            }
        }
    }

    //teachers statistics
    private static void showTeachersStatistics(Scanner scanner) {
        List<Faculty> faculties = fetchFaculties();
        List<Teacher> allTeachers = fetchTeachers();

        System.out.println("=== Teachers' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Total number of teachers in the university");
            System.out.println("2. Distribution of teachers by gender");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 2);

            switch (choice) {
                case 1 -> {
                    int totalTeachers = faculties.stream().flatMap(f -> f.getDepartments().stream())
                            .mapToInt(d -> countTeachers(d, allTeachers))
                            .sum();
                    System.out.println("Total number of teachers: " + totalTeachers);
                    pause(scanner);
                }
                case 2 ->{
                    printGenderStatistics(allTeachers, "All Teachers");
                    pause(scanner);
                }
                case 0 -> { return; }
            }
        }
    }

    // students statistics
    private static void showStudentsStatistics(Scanner scanner) {
        List<Student> allStudents = fetchStudents();

        System.out.println("=== Students' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Total number of students in the university");
            System.out.println("2. Distribution of students by course/year of study");
            System.out.println("3. Distribution of students by study form (budget vs contract)");
            System.out.println("4. Distribution of students by gender");
            System.out.println("5. Number of unique last names (Diversity Check)");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 4);

            switch (choice) {
                case 1 -> {
                    System.out.println("Total number of students: " + allStudents.size());
                    pause(scanner);
                }
                case 2 -> {
                    System.out.println("=== Distribution by Course ===");
                    allStudents.stream()
                            .collect(Collectors.groupingBy(Student::getCourse, TreeMap::new, Collectors.counting()))
                            .forEach((course, count) -> {
                                int numberOfStudents = count.intValue();
                                String bar = "█".repeat(numberOfStudents/15);
                                System.out.printf("Course %d: %-4d students | %s%n", course, numberOfStudents, bar);
                            });
                    pause(scanner);
                }
                case 3 -> {
                    System.out.println("=== Distribution by Study Form ===");
                    long budgetCount = allStudents.stream().filter(s -> s.getStudyForm() == StudyForm.BUDGET).count();
                    long contractCount = allStudents.stream().filter(s -> s.getStudyForm() == StudyForm.CONTRACT).count();
                    long total = budgetCount + contractCount;

                    if (total == 0) {
                        System.out.println("No students found in the system.");
                    } else {
                        double budgetPercent = (budgetCount * 100.0) / total;
                        double contractPercent = (contractCount * 100.0) / total;
                        System.out.println("Total Students: " + total);
                        System.out.printf("Budget: %d (%.1f%%)%n", budgetCount, budgetPercent);
                        System.out.printf("Contract: %d (%.1f%%)%n", contractCount, contractPercent);
                    }
                    pause(scanner);
                }
                case 4 -> {
                    printGenderStatistics(allStudents, "All Students");
                    pause(scanner);
                }
                case 5 -> {
                    System.out.println("=== Student Body Diversity ===");
                    Set<String> uniqueLastNames = allStudents.stream()
                            .map(Student::getSurname)
                            .collect(Collectors.toSet());

                    int totalStudents = allStudents.size();
                    int uniqueCount = uniqueLastNames.size();

                    System.out.println("Total students: " + totalStudents);
                    System.out.println("Unique last names: " + uniqueCount);

                    if (totalStudents > 0) {
                        double diversityRatio = (uniqueCount * 100.0) / totalStudents;
                        System.out.printf("Diversity ratio: %.1f%%%n", diversityRatio);
                    }
                    pause(scanner);
                }
                case 0 -> { return; }
            }
        }
    }

    // uni statistics
    private static void showUniversityStatistics(Scanner scanner) {
        List<Faculty> faculties = fetchFaculties();
        List<Student> allStudents = fetchStudents();
        List<Teacher> allTeachers = fetchTeachers();

        System.out.println("=== University-wide Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Grand Total Summary (Faculties, Departments, Specialities, Teachers, Students)");
            System.out.println("2. Overall Student-to-Teacher ratio");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 2);

            switch (choice) {
                case 1 -> {
                    System.out.println("Total Faculties: " + faculties.size());
                    System.out.println("------------------------------------");
                    System.out.println("Total departments: " + faculties.stream().mapToInt(f -> f.getDepartments().size()).sum());
                    System.out.println("------------------------------------");
                    System.out.println("Total specialities: " + faculties.stream().mapToInt(f -> f.getSpecialities().size()).sum());
                    System.out.println("------------------------------------");
                    System.out.println("Total teachers: " + allTeachers.size());
                    System.out.println("------------------------------------");
                    long budgetCount = allStudents.stream().filter(s -> s.getStudyForm() == StudyForm.BUDGET).count();
                    long contractCount = allStudents.stream().filter(s -> s.getStudyForm() == StudyForm.CONTRACT).count();
                    System.out.println("Total students: " + allStudents.size() + " (Budget: " + budgetCount + ", Contract: " + contractCount + ")");
                    pause(scanner);
                }
                case 2 -> {
                    int studentCount = allStudents.size();
                    int teacherCount = allTeachers.size();
                    System.out.println("Overall Student-to-Teacher Ratio: " + (teacherCount == 0 ? "N/A" : String.format("%.2f", (double) studentCount / teacherCount)));
                    pause(scanner);
                }
                case 0 -> { return; }
            }
        }
    }

    // faculties statistics
    private static void showFacultiesStatistics(Scanner scanner, String title) {
        List<Faculty> faculties = fetchFaculties();
        List<Student> allStudents = fetchStudents();
        List<Teacher> allTeachers = fetchTeachers();

        System.out.println("=== Faculties' Statistics ===");
        while (true) {
            System.out.println("Show statistics for:");
            System.out.println("1. Number of faculties");
            System.out.println("2. Average metrics per faculty");
            System.out.println("3. Student population rankings");
            System.out.println("4. Gender distribution of students by faculty");
            System.out.println("0. Back to statistics menu");

            int choice = InputUtils.readInt(scanner, "> ", 0, 4);

            switch (choice) {
                case 1 -> {
                    System.out.println("Number of faculties: " + faculties.size());
                    pause(scanner);
                }
                case 2 -> {
                    List<FacultyStats> facultyStats = generateFacultyStats(faculties, allStudents, allTeachers);

                    System.out.println("Average departments per faculty: " + averageCount(faculties, f -> f.getDepartments().size()));
                    System.out.println("Average specialities per faculty: " + averageCount(faculties, f -> f.getSpecialities().size()));
                    System.out.println("Average students per faculty: " + averageCount(facultyStats, f -> Math.toIntExact(f.studentCount())));
                    System.out.println("Average teachers per faculty: " + averageCount(facultyStats, f -> Math.toIntExact(f.teacherCount())));
                    pause(scanner);
                }
                case 3 ->{
                    List<FacultyStats> facultyStats = generateFacultyStats(faculties, allStudents, allTeachers);
                    if (facultyStats.isEmpty()) {
                        System.out.println("No faculties found to calculate rankings.");
                        return;
                    }

                    printLeaders(facultyStats, f -> Math.toIntExact(f.studentCount()), FacultyStats::facultyName, title);
                    printOutsiders(facultyStats, f -> Math.toIntExact(f.studentCount()), FacultyStats::facultyName);

                    printLeaders(faculties, f -> countStudentsByFinancing(f, allStudents, Student::getFaculty, StudyForm.CONTRACT),
                            Faculty::getNameOfFaculty, "Most popular faculties among contract students");
                    pause(scanner);
                }
                case 4 ->{
                    for (Faculty f : faculties) {
                        List<Student> studentsInFaculty = allStudents.stream()
                                .filter(s -> s.getFaculty() != null && s.getFaculty().getId().equals(f.getId()))
                                .toList();
                        printGenderStatistics(studentsInFaculty, "Students in " + f.getNameOfFaculty());
                    }
                    pause(scanner);
                }
                case 0 -> { return; }
            }
        }
    }

    // --- REFACTORED UTILITY METHODS (using Lists instead of Services) ---

    public static List<FacultyStats> generateFacultyStats(List<Faculty> faculties, List<Student> allStudents, List<Teacher> allTeachers) {
        return faculties.stream()
                .map(faculty -> new FacultyStats(
                        faculty.getNameOfFaculty(),
                        countStudents(faculty, allStudents, Student::getFaculty),
                        faculty.getDepartments().stream()
                                .mapToLong(department -> countTeachers(department, allTeachers))
                                .sum()
                ))
                .toList();
    }

    private static <T> double averageCount(List<T> items, ToIntFunction<T> numberExtractor){
        return items.stream().mapToInt(numberExtractor).average().orElse(0.0);
    }

    private static <T> int countStudents(T item, List<Student> allStudents, Function<Student, T> studentExtractor){
        return (int) allStudents.stream()
                .filter(s -> s != null && studentExtractor.apply(s) != null && studentExtractor.apply(s).equals(item))
                .count();
    }

    private static <T> int countStudentsByFinancing (T item, List<Student> allStudents, Function<Student, T> studentExtractor, StudyForm studyForm){
        return (int) allStudents.stream()
                .filter(s -> s != null && studentExtractor.apply(s) != null && studentExtractor.apply(s).equals(item) && s.getStudyForm() == studyForm)
                .count();
    }

    private static int countTeachers(Department d, List<Teacher> allTeachers){
        return (int) allTeachers.stream()
                .filter(t -> t.getDepartment() != null && t.getDepartment().getId().equals(d.getId()))
                .count();
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

    public static <T extends Person> void printGenderStatistics(List<T> people, String groupName) {
        System.out.println("\n=== Gender Statistics: " + groupName + " ===");

        if (people == null || people.isEmpty()) {
            System.out.println("No data available.");
            System.out.println("=========================================\n");
            return;
        }

        Map<Gender, Long> stats = people.stream()
                .filter(p -> p.getGender() != null)
                .collect(Collectors.groupingBy(Person::getGender, Collectors.counting()));

        long totalWithGender = stats.values().stream().mapToLong(Long::longValue).sum();
        long notSpecified = people.size() - totalWithGender;

        for (Gender g : Gender.values()) {
            long count = stats.getOrDefault(g, 0L);
            double percentage = (count * 100.0) / people.size();
            System.out.printf("%-20s : %d (%.1f%%)\n", g.getDisplayName(), count, percentage);
        }

        if (notSpecified > 0) {
            double percentage = (notSpecified * 100.0) / people.size();
            System.out.printf("%-20s : %d (%.1f%%)\n", "Not Specified", notSpecified, percentage);
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total Persons        : " + people.size());
        System.out.println("=========================================\n");
    }

    // --- HELPER METHODS FOR NETWORK FETCHING ---
    @SuppressWarnings("unchecked")
    private static List<Faculty> fetchFaculties() {
        Response res = NetworkClient.sendRequest(new Request("GET_ALL_FACULTIES"));
        return res.isSuccess() && res.getData() != null ? (List<Faculty>) res.getData() : new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private static List<Student> fetchStudents() {
        Response res = NetworkClient.sendRequest(new Request("GET_ALL_STUDENTS"));
        return res.isSuccess() && res.getData() != null ? (List<Student>) res.getData() : new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private static List<Teacher> fetchTeachers() {
        Response res = NetworkClient.sendRequest(new Request("GET_ALL_TEACHERS"));
        return res.isSuccess() && res.getData() != null ? (List<Teacher>) res.getData() : new ArrayList<>();
    }
}