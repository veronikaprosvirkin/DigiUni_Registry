package utils.sort;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import utils.input.InputUtils;
import person.Student;
import person.Teacher;
import department.Department;

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

            case 2 -> students.sort(Comparator.comparing(Student::getEnrollmentDate)
                    .thenComparing(Student::getFullName));
            case 3 -> students.sort(Comparator.comparingInt(Student::getCourse)
                    .thenComparing(Student::getFullName));
            case 4 -> students.sort(Comparator.comparingInt(Student::getGroup)
                    .thenComparing(Student::getFullName));
        }
        return students;
    }

    public static List<Teacher> sortTeachers(List<Teacher> teachers, Scanner scanner) {
        System.out.println("1. Sort by full name");
        System.out.println("2. Sort by position");
        System.out.println("3. Sort by department");

        int choice = InputUtils.readInt(scanner, "> ", 1, 3);

        switch (choice) {
            case 1 -> teachers.sort(Comparator.comparing(Teacher::getFullName));
            case 2 -> teachers.sort(Comparator.comparing(Teacher::getPosition)
                    .thenComparing(Teacher::getFullName));
            case 3 -> teachers.sort(Comparator.comparing((Teacher t) -> t.getDepartment().getName())
                    .thenComparing(Teacher::getFullName));
        }

        return teachers;
    }
}