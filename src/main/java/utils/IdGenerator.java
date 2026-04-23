package utils;

import university.University;
import person.Student;
import person.Teacher;
import department.Department;
import faculty.Faculty;
import speciality.Speciality;

public class IdGenerator {

    
    public static String generateTeacherId(University university) {
        int maxId = university.getFaculties().stream()
                .flatMap(f -> f.getDepartments().stream())
                .flatMap(d -> d.getTeachers().stream())
                .map(Teacher::getId)
                .mapToInt(id -> extractNumericPart(id, "t"))
                .max()
                .orElse(0); // Якщо вчителів взагалі немає, почнемо з 0

        return String.format("t%04d", maxId + 1);
    }
    
    public static String generateStudentId(University university, int year) {
        int maxId = university.getFaculties().stream()
                .flatMap(f -> f.getSpeciality().stream())
                .flatMap(s -> s.getGroups().stream())
                .flatMap(g -> g.getStudents().stream())
                .map(Student::getId)
                // Студентські ID довші (st20260001), тому беремо останні 4 цифри
                .mapToInt(id -> {
                    try {
                        return Integer.parseInt(id.substring(6));
                    } catch (Exception e) { return 0; }
                })
                .max()
                .orElse(0);

        return String.format("st%d%04d", year, maxId + 1);
    }
    
    public static String generateFacultyId(University university) {
        int maxId = university.getFaculties().stream()
                .map(Faculty::getId)
                .mapToInt(id -> extractNumericPart(id, "f"))
                .max()
                .orElse(0);
        return String.format("f%03d", maxId + 1);
    }

    public static String generateDepartmentId(University university) {
        int maxId = university.getFaculties().stream()
                .flatMap(f -> f.getDepartments().stream())
                .map(Department::getId)
                .mapToInt(id -> extractNumericPart(id, "d"))
                .max()
                .orElse(0);
        return String.format("d%03d", maxId + 1);
    }

    public static String generateSpecialityId(University university) {
        int maxId = university.getFaculties().stream()
                .flatMap(f -> f.getSpeciality().stream())
                .map(Speciality::getId)
                .mapToInt(id -> extractNumericPart(id, "sp"))
                .max()
                .orElse(0);
        return String.format("sp%03d", maxId + 1);
    }
    
    private static int extractNumericPart(String id, String prefix) {
        if (id == null || !id.startsWith(prefix)) return 0;
        try {
            return Integer.parseInt(id.replace(prefix, ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}