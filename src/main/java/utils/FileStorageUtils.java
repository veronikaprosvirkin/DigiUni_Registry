package utils;

import faculty.Faculty;
import speciality.Speciality;
import department.Department;
import person.Teacher;
import university.University;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileStorageUtils {

    private static final Path FACULTIES_FILE = Path.of("data", "faculties.csv");
    private static final Path SPECIALITIES_FILE = Path.of("data", "specialities.csv");
    private static final Path DEPARTMENTS_FILE = Path.of("data", "departments.csv");
    private static final String DELIMITER = ";";

    // Save all structure
    public static void saveAll(University university) {
        try {
            Files.createDirectories(FACULTIES_FILE.getParent());
            saveFaculties(university.getFaculties());
            saveSpecialities(university.getFaculties());
            saveDepartments(university.getFaculties());
        } catch (IOException e) {
            System.err.println("Save error");
        }
    }

    // Load all structure
    public static void loadAll(University university) {
        try {
            university.getFaculties().clear();
            if (Files.exists(FACULTIES_FILE)) loadFaculties(university);
            if (Files.exists(SPECIALITIES_FILE)) loadSpecialities(university);
            if (Files.exists(DEPARTMENTS_FILE)) loadDepartments(university);
        } catch (IOException e) {
            System.err.println("Load error");
        }
    }

    // Save faculties
    private static void saveFaculties(List<Faculty> faculties) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(FACULTIES_FILE, StandardCharsets.UTF_8)) {
            for (Faculty f : faculties) {
                Teacher dean = f.getDean();
                w.write(String.join(DELIMITER,
                        value(f.getId()),
                        value(f.getNameOfFaculty()),
                        value(f.getShortName()),
                        value(f.getContacts()),
                        value(dean == null ? null : dean.getId()),
                        value(dean == null ? null : dean.getOnlyName()),
                        value(dean == null ? null : dean.getSurname()),
                        value(dean == null ? null : dean.getPatronymic()),
                        value(dean == null ? null : dean.getPosition()),
                        value(dean == null ? null : dean.getAcademicDegree()),
                        value(dean == null ? null : dean.getAcademicTitle()),
                        value(dean == null || dean.getEmploymentDate() == null ? null : dean.getEmploymentDate().toString()),
                        value(dean == null ? null : String.valueOf(dean.getWorkload())),
                        value(dean == null ? null : dean.getEmail()),
                        value(dean == null ? null : dean.getPhone())
                ));
                w.newLine();
            }
        }
    }

    // Load faculties
    private static void loadFaculties(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(FACULTIES_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length >= 4) {
                    String id = parts[0];
                    String name = parts[1];
                    String shortName = parts[2];
                    String contact = parts[3];
                    Teacher dean = restoreTeacher(parts, 4);
                    u.getFaculties().add(new Faculty(id, name, shortName, contact, dean));
                    IdGenerator.updateFacultyCounter(id);
                }
            }
        }
    }

    // Save specialities
    private static void saveSpecialities(List<Faculty> faculties) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(SPECIALITIES_FILE, StandardCharsets.UTF_8)) {
            for (Faculty f : faculties) {
                for (Speciality s : f.getSpeciality()) {
                    w.write(String.join(DELIMITER, value(s.getId()), value(s.getNameOfSpeciality()), value(f.getId())));
                    w.newLine();
                }
            }
        }
    }

    // Load specialities and link to faculty
    private static void loadSpecialities(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(SPECIALITIES_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length >= 3) {
                    String id = parts[0];
                    String name = parts[1];
                    String facultyId = parts[2];
                    Speciality s = new Speciality(id, name);

                    // Find parent faculty and add
                    u.getFaculties().stream()
                            .filter(f -> f.getId().equals(facultyId))
                            .findFirst()
                            .ifPresent(f -> {
                                f.getSpeciality().add(s);
                                IdGenerator.updateSpecialityCounter(id);
                            });
                }
            }
        }
    }

    // Save departments
    private static void saveDepartments(List<Faculty> faculties) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(DEPARTMENTS_FILE, StandardCharsets.UTF_8)) {
            for (Faculty f : faculties) {
                for (Department d : f.getDepartments()) {
                    Teacher head = d.getHead();
                    w.write(String.join(DELIMITER,
                            value(d.getId()),
                            value(d.getNameOfDepartment()),
                            value(f.getId()),
                            value(d.getLocation()),
                            value(head == null ? null : head.getId()),
                            value(head == null ? null : head.getOnlyName()),
                            value(head == null ? null : head.getSurname()),
                            value(head == null ? null : head.getPatronymic()),
                            value(head == null ? null : head.getPosition()),
                            value(head == null ? null : head.getAcademicDegree()),
                            value(head == null ? null : head.getAcademicTitle()),
                            value(head == null || head.getEmploymentDate() == null ? null : head.getEmploymentDate().toString()),
                            value(head == null ? null : String.valueOf(head.getWorkload())),
                            value(head == null ? null : head.getEmail()),
                            value(head == null ? null : head.getPhone())
                    ));
                    w.newLine();
                }
            }
        }
    }

    // Load departments and link to faculty
    private static void loadDepartments(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(DEPARTMENTS_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length >= 3) {
                    String id = parts[0];
                    String name = parts[1];
                    String facultyId = parts[2];
                    Department d = new Department(id, name);
                    if (parts.length > 3) {
                        d.setLocation(parts[3].isBlank() ? null : parts[3]);
                    }
                    d.setHead(restoreTeacher(parts, 4));

                    // Find parent faculty and add
                    u.getFaculties().stream()
                            .filter(f -> f.getId().equals(facultyId))
                            .findFirst()
                            .ifPresent(f -> {
                                f.getDepartments().add(d);
                                IdGenerator.updateDepartmentCounter(id);
                            });
                }
            }
        }
    }

    private static String value(String raw) {
        return raw == null ? "" : raw;
    }

    private static Teacher restoreTeacher(String[] parts, int startIndex) {
        if (parts.length <= startIndex) return null;
        String id = part(parts, startIndex);
        if (id == null || id.isBlank()) return null;

        Teacher teacher = new Teacher(
                id,
                value(part(parts, startIndex + 1)),
                value(part(parts, startIndex + 2)),
                value(part(parts, startIndex + 3)),
                blankToNull(part(parts, startIndex + 4)),
                null
        );
        teacher.setAcademicDegree(blankToNull(part(parts, startIndex + 5)));
        teacher.setAcademicTitle(blankToNull(part(parts, startIndex + 6)));

        String employmentDate = blankToNull(part(parts, startIndex + 7));
        if (employmentDate != null) {
            try {
                teacher.setEmploymentDate(LocalDate.parse(employmentDate));
            } catch (Exception ignored) {
            }
        }

        String workload = blankToNull(part(parts, startIndex + 8));
        if (workload != null) {
            try {
                teacher.setWorkload(Double.parseDouble(workload));
            } catch (NumberFormatException ignored) {
            }
        }

        teacher.setEmail(blankToNull(part(parts, startIndex + 9)));
        teacher.setPhone(blankToNull(part(parts, startIndex + 10)));
        return teacher;
    }

    private static String part(String[] parts, int index) {
        if (index < 0 || index >= parts.length) return null;
        return parts[index];
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}