package utils;

import department.Department;
import faculty.Faculty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import speciality.Speciality;
import university.University;

import java.lang.reflect.Field;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileStorageUtilsTest {

    private static final Path FACULTIES_FILE = Path.of("data", "faculties.csv");
    private static final Path SPECIALITIES_FILE = Path.of("data", "specialities.csv");
    private static final Path DEPARTMENTS_FILE = Path.of("data", "departments.csv");

    private byte[] facultiesBackup;
    private byte[] specialitiesBackup;
    private byte[] departmentsBackup;

    @BeforeEach
    void setUp() throws Exception {
        Files.createDirectories(Path.of("data"));
        facultiesBackup = backup(FACULTIES_FILE);
        specialitiesBackup = backup(SPECIALITIES_FILE);
        departmentsBackup = backup(DEPARTMENTS_FILE);
        resetCounters();
    }

    @AfterEach
    void tearDown() throws Exception {
        restore(FACULTIES_FILE, facultiesBackup);
        restore(SPECIALITIES_FILE, specialitiesBackup);
        restore(DEPARTMENTS_FILE, departmentsBackup);
        resetCounters();
    }

    @Test
    void saveAllPersistsFacultiesSpecialitiesAndDepartments() throws Exception {
        University university = new University();
        Faculty faculty = new Faculty("f010", "Faculty A", "FA", "123", null);
        faculty.getSpeciality().add(new Speciality("sp120", "Cybersecurity"));
        faculty.getDepartments().add(new Department("d042", "AI Department"));
        university.getFaculties().add(faculty);

        FileStorageUtils.saveAll(university);

        assertTrue(Files.exists(FACULTIES_FILE));
        assertTrue(Files.exists(SPECIALITIES_FILE));
        assertTrue(Files.exists(DEPARTMENTS_FILE));

        String facultiesCsv = Files.readString(FACULTIES_FILE, StandardCharsets.UTF_8);
        String specialitiesCsv = Files.readString(SPECIALITIES_FILE, StandardCharsets.UTF_8);
        String departmentsCsv = Files.readString(DEPARTMENTS_FILE, StandardCharsets.UTF_8);

        assertTrue(facultiesCsv.contains("f010;Faculty A;FA;123"));
        assertTrue(specialitiesCsv.contains("sp120;Cybersecurity;f010"));
        assertTrue(departmentsCsv.contains("d042;AI Department;f010"));
    }

    @Test
    void saveAllWithEmptyUniversityCreatesEmptyFiles() throws Exception {
        University university = new University();

        FileStorageUtils.saveAll(university);

        assertTrue(Files.exists(FACULTIES_FILE));
        assertTrue(Files.exists(SPECIALITIES_FILE));
        assertTrue(Files.exists(DEPARTMENTS_FILE));
        assertEquals("", Files.readString(FACULTIES_FILE, StandardCharsets.UTF_8));
        assertEquals("", Files.readString(SPECIALITIES_FILE, StandardCharsets.UTF_8));
        assertEquals("", Files.readString(DEPARTMENTS_FILE, StandardCharsets.UTF_8));
    }

    @Test
    void saveAllOverwritesExistingContentInsteadOfAppending() throws Exception {
        write(FACULTIES_FILE, "f999;Old;O;old\n");
        write(SPECIALITIES_FILE, "sp999;OldSpec;f999\n");
        write(DEPARTMENTS_FILE, "d999;OldDept;f999\n");

        University university = new University();
        Faculty faculty = new Faculty("f001", "New Faculty", "NF", "new", null);
        faculty.getSpeciality().add(new Speciality("sp001", "New Spec"));
        faculty.getDepartments().add(new Department("d001", "New Dept"));
        university.getFaculties().add(faculty);

        FileStorageUtils.saveAll(university);

        String facultiesCsv = Files.readString(FACULTIES_FILE, StandardCharsets.UTF_8);
        String specialitiesCsv = Files.readString(SPECIALITIES_FILE, StandardCharsets.UTF_8);
        String departmentsCsv = Files.readString(DEPARTMENTS_FILE, StandardCharsets.UTF_8);

        assertFalse(facultiesCsv.contains("f999;Old;O;old"));
        assertFalse(specialitiesCsv.contains("sp999;OldSpec;f999"));
        assertFalse(departmentsCsv.contains("d999;OldDept;f999"));
        assertTrue(facultiesCsv.contains("f001;New Faculty;NF;new"));
        assertTrue(specialitiesCsv.contains("sp001;New Spec;f001"));
        assertTrue(departmentsCsv.contains("d001;New Dept;f001"));
    }

    @Test
    void loadAllRestoresHierarchyClearsOldStateAndUpdatesCounters() throws Exception {
        write(FACULTIES_FILE, """
                f002;Faculty B;FB;111
                f010;Faculty A;FA;222
                """);

        write(SPECIALITIES_FILE, """
                sp001;Math;f002
                sp120;Cybersecurity;f010
                sp999;Orphan;f999
                """);

        write(DEPARTMENTS_FILE, """
                d005;Economics Department;f002
                d042;AI Department;f010
                d999;Orphan Department;f999
                """);

        University university = new University();
        university.getFaculties().add(new Faculty("f777", "Old Faculty", "OF", "old", null));

        FileStorageUtils.loadAll(university);

        assertEquals(2, university.getFaculties().size());
        assertFalse(university.getFaculties().stream().anyMatch(f -> f.getId().equals("f777")));

        Faculty f002 = university.getFaculties().stream().filter(f -> f.getId().equals("f002")).findFirst().orElseThrow();
        Faculty f010 = university.getFaculties().stream().filter(f -> f.getId().equals("f010")).findFirst().orElseThrow();

        assertEquals(1, f002.getSpeciality().size());
        assertEquals("sp001", f002.getSpeciality().get(0).getId());

        assertEquals(1, f010.getSpeciality().size());
        assertEquals("sp120", f010.getSpeciality().get(0).getId());

        assertEquals(1, f002.getDepartments().size());
        assertEquals("d005", f002.getDepartments().get(0).getId());

        assertEquals(1, f010.getDepartments().size());
        assertEquals("d042", f010.getDepartments().get(0).getId());

        assertEquals("f011", IdGenerator.generateFacultyId());
        assertEquals("sp121", IdGenerator.generateSpecialityId());
        assertEquals("d043", IdGenerator.generateDepartmentId());
    }

    @Test
    void loadAllIgnoresMalformedRowsAndBlankLines() throws Exception {
        write(FACULTIES_FILE, """
                f001;Faculty A;FA;111
                malformed

                f002;Faculty B;FB;222
                """);

        write(SPECIALITIES_FILE, """
                sp001;Spec A;f001
                sp002;MissingFaculty
                ;;;
                sp003;Spec B;f002
                """);

        write(DEPARTMENTS_FILE, """
                d001;Dept A;f001
                d-bad
                d002;Dept B;f002
                """);

        University university = new University();
        FileStorageUtils.loadAll(university);

        assertEquals(2, university.getFaculties().size());
        Faculty f001 = university.getFaculties().stream().filter(f -> f.getId().equals("f001")).findFirst().orElseThrow();
        Faculty f002 = university.getFaculties().stream().filter(f -> f.getId().equals("f002")).findFirst().orElseThrow();

        assertEquals(1, f001.getSpeciality().size());
        assertEquals("sp001", f001.getSpeciality().get(0).getId());
        assertEquals(1, f002.getSpeciality().size());
        assertEquals("sp003", f002.getSpeciality().get(0).getId());

        assertEquals(1, f001.getDepartments().size());
        assertEquals("d001", f001.getDepartments().get(0).getId());
        assertEquals(1, f002.getDepartments().size());
        assertEquals("d002", f002.getDepartments().get(0).getId());
    }

    @Test
    void loadAllHandlesOnlySomeFilesPresent() throws Exception {
        write(FACULTIES_FILE, "f002;Faculty B;FB;111\n");
        Files.deleteIfExists(SPECIALITIES_FILE);
        write(DEPARTMENTS_FILE, "d005;Economics Department;f002\n");

        University university = new University();
        FileStorageUtils.loadAll(university);

        assertEquals(1, university.getFaculties().size());
        Faculty loaded = university.getFaculties().get(0);
        assertEquals("f002", loaded.getId());
        assertTrue(loaded.getSpeciality().isEmpty());
        assertEquals(1, loaded.getDepartments().size());
        assertEquals("d005", loaded.getDepartments().get(0).getId());
    }

    @Test
    void loadAllHandlesMissingFilesWithoutFailing() throws Exception {
        Files.deleteIfExists(FACULTIES_FILE);
        Files.deleteIfExists(SPECIALITIES_FILE);
        Files.deleteIfExists(DEPARTMENTS_FILE);

        University university = new University();

        assertDoesNotThrow(() -> FileStorageUtils.loadAll(university));
        assertTrue(university.getFaculties().isEmpty());
    }

    @Test
    void saveAllHandlesIoFailureWithoutThrowing() throws Exception {
        Files.deleteIfExists(FACULTIES_FILE);
        Files.createDirectory(FACULTIES_FILE);

        University university = new University();
        university.getFaculties().add(new Faculty("f001", "Faculty A", "FA", "111", null));

        assertDoesNotThrow(() -> FileStorageUtils.saveAll(university));
    }

    @Test
    void loadAllHandlesIoFailureWithoutThrowing() throws Exception {
        Files.deleteIfExists(FACULTIES_FILE);
        Files.createDirectory(FACULTIES_FILE);

        University university = new University();

        assertDoesNotThrow(() -> FileStorageUtils.loadAll(university));
    }

    private byte[] backup(Path file) throws Exception {
        if (!Files.exists(file)) {
            return null;
        }
        return Files.readAllBytes(file);
    }

    private void restore(Path file, byte[] backup) throws Exception {
        cleanupDirectoryPath(file);
        if (backup == null) {
            Files.deleteIfExists(file);
            return;
        }
        Files.write(file, backup);
    }

    private void write(Path file, String content) throws Exception {
        cleanupDirectoryPath(file);
        Files.writeString(file, content, StandardCharsets.UTF_8);
    }

    private void cleanupDirectoryPath(Path file) throws IOException {
        if (!Files.isDirectory(file)) {
            return;
        }
        try (var walk = Files.walk(file)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private void resetCounters() throws Exception {
        setCounter("studentCounter", 1);
        setCounter("teacherCounter", 1);
        setCounter("facultyCounter", 1);
        setCounter("departmentCounter", 1);
        setCounter("specialityCounter", 1);
    }

    private void setCounter(String fieldName, int value) throws Exception {
        Field field = IdGenerator.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setInt(null, value);
    }
}

