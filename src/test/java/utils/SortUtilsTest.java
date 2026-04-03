package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import person.Student;
import person.Teacher;
import person.StudyForm;
import department.Department;
import faculty.Faculty;
import speciality.Speciality;
import utils.sort.SortUtils;

public class SortUtilsTest {
    private List<Student> students;
    private List<Teacher> teachers;
    private Department dept1;
    private Department dept2;

    // Init data
    @BeforeEach
    void setUp() {
        students = new ArrayList<>();
        Teacher testDean = new Teacher(IdGenerator.generateTeacherId(), "Ivan", "Ivanov", "Ivanovych", "Dean", null);
        Faculty f = new Faculty("fc01", "Faculty of Informatics", "FI", "contacts1", testDean);
        Speciality s = new Speciality("sp01","SE");

        students.add(new Student("st001", "Jan", "Kowalski", "sm", LocalDate.of(2025, 9, 1), 102, f, s,StudyForm.BUDGET));
        students.add(new Student("st002", "Anna", "Nowak", "sm", LocalDate.of(2024, 9, 1), 101, f, s, StudyForm.CONTRACT));
        students.add(new Student("st003", "Anna", "Kaczmarek","sm", LocalDate.of(2024, 9, 1), 101, f, s, StudyForm.BUDGET));

        teachers = new ArrayList<>();
        dept1 = new Department("dp01","CS");
        dept2 = new Department("dp02","Math");

        teachers.add(new Teacher("th1000","Marek", "Wiśniewski", "sm","Prof", dept2));
        teachers.add(new Teacher("th1001","Ewa", "Wójcik", "sm", "Docent", dept1));
        teachers.add(new Teacher("th1002","Adam", "Dąbrowski","sm", "Docent", dept1));
    }

    // Test student sort by name
    @Test
    void testSortStudentsByName() {
        Scanner scanner = new Scanner("1\n");
        SortUtils.sortStudents(students, scanner);
        assertEquals("Kaczmarek Anna", students.get(0).getFullName());
        assertEquals("Kowalski Jan", students.get(1).getFullName());
        assertEquals("Nowak Anna", students.get(2).getFullName());
    }

    // Test student sort by year
    @Test
    void testSortStudentsByYear() {
        Scanner scanner = new Scanner("2\n");
        SortUtils.sortStudents(students, scanner);
        assertEquals("Kaczmarek Anna", students.get(0).getFullName());
        assertEquals("Nowak Anna", students.get(1).getFullName());
        assertEquals("Kowalski Jan", students.get(2).getFullName());
    }

    // Test teacher sort by position
    @Test
    void testSortTeachersByPosition() {
        Scanner scanner = new Scanner("2\n");
        SortUtils.sortTeachers(teachers, scanner);
        assertEquals("Dąbrowski Adam", teachers.get(0).getFullName());
        assertEquals("Wójcik Ewa", teachers.get(1).getFullName());
        assertEquals("Wiśniewski Marek", teachers.get(2).getFullName());
    }

    // Test teacher sort by department
    @Test
    void testSortTeachersByDepartment() {
        Scanner scanner = new Scanner("3\n");
        SortUtils.sortTeachers(teachers, scanner);
        assertEquals("Dąbrowski Adam", teachers.get(0).getFullName());
        assertEquals("Wójcik Ewa", teachers.get(1).getFullName());
        assertEquals("Wiśniewski Marek", teachers.get(2).getFullName());
    }
}