package utils;

import department.Department;
import faculty.Faculty;
import org.junit.jupiter.api.Test;
import person.Teacher;
import speciality.Speciality;
import university.University;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdGeneratorTest {

    @Test
    void generateFacultyId_returnsFirstIdForEmptyUniversity() {
        University university = new University();

        assertEquals("f001", IdGenerator.generateFacultyId(university));
    }

    @Test
    void generateFacultyDepartmentAndSpecialityIds_useMaxExistingValue() {
        University university = new University();

        Faculty faculty = new Faculty("f007", "Engineering", "ENG", "contacts", null);
        faculty.getDepartments().add(new Department("d009", "CS"));
        faculty.getSpeciality().add(new Speciality("sp011", "Software Engineering"));
        university.getFaculties().add(faculty);

        assertEquals("f008", IdGenerator.generateFacultyId(university));
        assertEquals("d010", IdGenerator.generateDepartmentId(university));
        assertEquals("sp012", IdGenerator.generateSpecialityId(university));
    }

    @Test
    void generateTeacherId_usesTeachersAcrossAllDepartments() {
        University university = new University();
        Faculty faculty = new Faculty("f001", "Engineering", "ENG", "contacts", null);

        Department d1 = new Department("d001", "CS");
        Department d2 = new Department("d002", "Math");
        d1.getTeachers().add(new Teacher("t0002", "John", "Doe", "", "Professor", d1));
        d2.getTeachers().add(new Teacher("t0010", "Jane", "Smith", "", "Professor", d2));

        faculty.getDepartments().add(d1);
        faculty.getDepartments().add(d2);
        university.getFaculties().add(faculty);

        assertEquals("t0011", IdGenerator.generateTeacherId(university));
    }

    @Test
    void generateStudentId_returnsFirstValueWhenNoStudentsExist() {
        University university = new University();

        assertEquals("st20260001", IdGenerator.generateStudentId(university, 2026));
    }
}
