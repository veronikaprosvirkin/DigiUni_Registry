import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class SortUtils {

    // Sort students
    public static List<Student> sortStudents(List<Student> students, Scanner scanner) {
        System.out.println("1. Sort by full name");
        System.out.println("2. Sort by enrollment year");
        System.out.println("3. Sort by course");
        System.out.println("4. Sort by group");

        int choice = InputUtils.readInt(scanner, "> ", 1, 4);

        switch (choice) {
            case 1 -> students.sort(Comparator.comparing(Student::getFullName));
            case 2 -> students.sort(Comparator.comparingInt(Student::getEnrollmentDate));
            case 3 -> students.sort(Comparator.comparingInt(Student::getCourse));
            case 4 -> students.sort(Comparator.comparingInt(Student::getGroup));
        }
        return students;
    }
}