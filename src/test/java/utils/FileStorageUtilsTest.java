package utils;

import department.Department;
import faculty.Faculty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import person.StudyForm;
import person.Teacher;
import speciality.Speciality;
import university.University;

import java.lang.reflect.Field;
import java.io.IOException;
import java.time.LocalDate;
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
    private static final Path TEACHERS_FILE = Path.of("data", "teachers.csv");
    private static final Path STUDENTS_FILE = Path.of("data", "students.csv");


    private byte[] facultiesBackup;
    private byte[] specialitiesBackup;
    private byte[] departmentsBackup;
    private byte[] studentsBackup;

    @BeforeEach
    void setUp() throws Exception {
        Files.createDirectories(Path.of("data"));
        facultiesBackup = backup(FACULTIES_FILE);
        specialitiesBackup = backup(SPECIALITIES_FILE);
        departmentsBackup = backup(DEPARTMENTS_FILE);
        studentsBackup = backup(STUDENTS_FILE);

        // Also backup teachers file explicitly and then delete all files for a clean test state
        byte[] teachersBackupBytes = backup(TEACHERS_FILE);

        Files.deleteIfExists(FACULTIES_FILE);
        Files.deleteIfExists(SPECIALITIES_FILE);
        Files.deleteIfExists(DEPARTMENTS_FILE);
        Files.deleteIfExists(STUDENTS_FILE);
        Files.deleteIfExists(TEACHERS_FILE);

        resetCounters();
    }

    @AfterEach
    void tearDown() throws Exception { Thread.sleep(200);
        restore(FACULTIES_FILE, facultiesBackup);
        restore(SPECIALITIES_FILE, specialitiesBackup);
        restore(DEPARTMENTS_FILE, departmentsBackup);
        restore(STUDENTS_FILE, studentsBackup);
        resetCounters();
    }

    @Test
    void saveAllPersistsFacultiesSpecialitiesAndDepartments() throws Exception {
        University university = new University();
        Teacher dean = new Teacher("t1111", "Anna", "Wójcik", "Kazimierzówna", "Professor", null);
        dean.setAcademicDegree("PhD");
        dean.setAcademicTitle("Docent");
        dean.setEmploymentDate(LocalDate.of(2020, 1, 10));
        dean.setWorkload(1.0);
        dean.setEmail("dean@uni.test");
        dean.setPhone("+380111111111");

        Teacher head = new Teacher("t2222", "Bartosz", "Nowak", "Stefanowicz", "Head", null);
        head.setAcademicDegree("MSc");
        head.setAcademicTitle("Senior Lecturer");
        head.setEmploymentDate(LocalDate.of(2021, 3, 12));
        head.setWorkload(0.75);
        head.setEmail("head@uni.test");
        head.setPhone("+380222222222");

        Faculty faculty = new Faculty("f010", "Faculty A", "FA", "123", dean);
        faculty.getSpeciality().add(new Speciality("sp120", "Cybersecurity"));
        Department department = new Department("d042", "AI Department");
        department.setLocation("Building B");
        department.setHead(head);
        faculty.getDepartments().add(department);
        university.getFaculties().add(faculty);

        FileStorageUtils.saveAll(university); Thread.sleep(200);

        assertTrue(Files.exists(FACULTIES_FILE));
        assertTrue(Files.exists(SPECIALITIES_FILE));
        assertTrue(Files.exists(DEPARTMENTS_FILE));


        String facultiesCsv = Files.readString(FACULTIES_FILE, StandardCharsets.UTF_8);
        String specialitiesCsv = Files.readString(SPECIALITIES_FILE, StandardCharsets.UTF_8);
        String departmentsCsv = Files.readString(DEPARTMENTS_FILE, StandardCharsets.UTF_8);

        assertTrue(facultiesCsv.contains("f010;Faculty A;FA;123"));
        assertTrue(facultiesCsv.contains(";t1111;Anna;Wójcik;Kazimierzówna;Professor;PhD;Docent;2020-01-10;1.0;dean@uni.test;+380111111111"));
        assertTrue(specialitiesCsv.contains("sp120;Cybersecurity;f010"));
        assertTrue(departmentsCsv.contains("d042;AI Department;f010;Building B;t2222;Bartosz;Nowak;Stefanowicz;Head;MSc;Senior Lecturer;2021-03-12;0.75;head@uni.test;+380222222222"));
    }

    @Test
    void testCsvContentVerification() throws Exception {
        University university = new University();
        Faculty faculty = new Faculty("fCSV", "CSV Faculty", "CF", "000", null);
        university.getFaculties().add(faculty);

        FileStorageUtils.saveAll(university); Thread.sleep(200);

        String content = Files.readString(FACULTIES_FILE, StandardCharsets.UTF_8);
        String[] lines = content.split("\n");
        boolean found = false;
        for (String line : lines) {
            if (line.startsWith("fCSV;CSV Faculty;CF;000")) {
                found = true;
                String[] parts = line.split(";");
                assertEquals("fCSV", parts[0]);
                assertEquals("CSV Faculty", parts[1]);
                assertEquals("CF", parts[2]);
                assertEquals("000", parts[3]);
            }
        }
        assertTrue(found, "The saved CSV should contain the faculty data in correct format");
    }

    @Test
    void saveAllWithEmptyUniversityCreatesEmptyFiles() throws Exception {
        University university = new University();

        FileStorageUtils.saveAll(university); Thread.sleep(200);

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

        FileStorageUtils.saveAll(university); Thread.sleep(200);

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
                f002;Faculty B;FB;111;t0100;Irena;Kowalska;Piotrówna;Professor;PhD;Docent;2018-02-03;1.0;dean.b@uni.test;+380000000001
                f010;Faculty A;FA;222;;;;;;;;;;;
                """);

        write(SPECIALITIES_FILE, """
                sp001;Math;f002
                sp120;Cybersecurity;f010
                sp999;Orphan;f999
                """);

        write(DEPARTMENTS_FILE, """
                d005;Economics Department;f002;Block A;t0200;Narcyz;Wiśniewski;Quirynowicz;Head;MSc;Senior Lecturer;2019-05-06;0.75;head.b@uni.test;+380000000002
                d042;AI Department;f010;;;;;;;;;;;;
                d999;Orphan Department;f999
                """);

        University university = new University();
        university.getFaculties().add(new Faculty("f777", "Old Faculty", "OF", "old", null));

        FileStorageUtils.loadAll(university, new faculty.FacultyService(university), new speciality.SpecialityService(university));

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
        assertEquals("Block A", f002.getDepartments().get(0).getLocation());
        assertEquals("t0200", f002.getDepartments().get(0).getHead().getId());

        assertEquals(1, f010.getDepartments().size());
        assertEquals("d042", f010.getDepartments().get(0).getId());
        assertEquals(null, f010.getDepartments().get(0).getHead());

        assertEquals("t0100", f002.getDean().getId());
        assertEquals("Irena", f002.getDean().getOnlyName());
        assertEquals(null, f010.getDean());

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
        FileStorageUtils.loadAll(university, new faculty.FacultyService(university), new speciality.SpecialityService(university));

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
        FileStorageUtils.loadAll(university, new faculty.FacultyService(university), new speciality.SpecialityService(university));

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

        assertDoesNotThrow(() -> FileStorageUtils.loadAll(university, new faculty.FacultyService(university), new speciality.SpecialityService(university)));
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

        assertDoesNotThrow(() -> FileStorageUtils.loadAll(university, new faculty.FacultyService(university), new speciality.SpecialityService(university)));
    }
    @Test
    void saveAllPersistsStudentsInGroups() throws Exception {
        University university = new University();
        Faculty faculty = new Faculty("f999", "IT Faculty", "IT", "123", null);
        Speciality speciality = new Speciality("sp999", "Software Engineering");

        speciality.Group group = new speciality.Group(1);

        person.Student student = new person.Student("st001", "Ignacy", "Zieliński", "Ignacowicz", LocalDate.of(2023, 9, 1), 1, faculty, speciality, StudyForm.CONTRACT);
        student.setEmail("ignacy@uni.pl");
        student.setPhone("123456789");

        if (group.getStudents() == null) group.setStudents(new java.util.ArrayList<>());
        group.getStudents().add(student);

        if (speciality.getGroups() == null) speciality.setGroups(new java.util.ArrayList<>());
        speciality.getGroups().add(group);

        faculty.getSpeciality().add(speciality);
        university.getFaculties().add(faculty);

        FileStorageUtils.saveAll(university); Thread.sleep(200);

        assertTrue(Files.exists(STUDENTS_FILE));
        String studentsCsv = Files.readString(STUDENTS_FILE, StandardCharsets.UTF_8);

        // Verify header
        assertTrue(studentsCsv.startsWith("id;name;surname;patronymic;course;enrollmentDate;group;faculty;speciality;studyForm;status;email;phone"));

        // Verify student row
        assertTrue(studentsCsv.contains("st001;Ignacy;Zieliński;Ignacowicz"));
        assertTrue(studentsCsv.contains("ignacy@uni.pl;123456789"));
    }

    @Test
    void testHasSavedStructureReturnsCorrectBoolean() throws Exception {
        Files.deleteIfExists(FACULTIES_FILE);
        assertFalse(FileStorageUtils.hasSavedStructure(), "No file -> false");

        write(FACULTIES_FILE, "");
        assertFalse(FileStorageUtils.hasSavedStructure(), "Empty file -> false");

        write(FACULTIES_FILE, "\n   \n");
        assertFalse(FileStorageUtils.hasSavedStructure(), "Blank lines only -> false");

        write(FACULTIES_FILE, "f001;Faculty A;FA;111\n");
        assertTrue(FileStorageUtils.hasSavedStructure(), "Valid content -> true");
    }

    @Test
    void saveAllAndLoadAllPreservesStudentsWithAllFields() throws Exception {
        University university = new University();
        Faculty faculty = new Faculty("f1", "Faculty 1", "F1", "123", null);
        Speciality speciality = new Speciality("sp1", "Spec 1");

        speciality.Group group = new speciality.Group(101);
        person.Student student = new person.Student("st123", "Jacek", "Dąbrowski", "Dominikowicz", LocalDate.of(2022, 9, 1), 101, faculty, speciality, StudyForm.BUDGET);
        student.setStatus(person.StudentStatus.ACTIVE);
        student.setEmail("john@uni.edu");
        student.setPhone("+380998887766");

        if (group.getStudents() == null) group.setStudents(new java.util.ArrayList<>());
        group.getStudents().add(student);
        if (speciality.getGroups() == null) speciality.setGroups(new java.util.ArrayList<>());
        speciality.getGroups().add(group);
        faculty.getSpeciality().add(speciality);
        university.getFaculties().add(faculty);

        FileStorageUtils.saveAll(university); Thread.sleep(200);

        // Load into new instance
        University loadedUni = new University();
        loadedUni.getFaculties().add(new Faculty("f1", "Faculty 1", "F1", "123", null));
        loadedUni.getFaculties().get(0).getSpeciality().add(new Speciality("sp1", "Spec 1"));

        FileStorageUtils.loadAll(loadedUni, new faculty.FacultyService(loadedUni), new speciality.SpecialityService(loadedUni));

        person.Student loadedStudent = loadedUni.getFaculties().get(0).getSpeciality().get(0).getGroups().get(0).getStudents().get(0);

        assertEquals("st123", loadedStudent.getId());
        assertEquals("Jacek", loadedStudent.getOnlyName());
        assertEquals("Dąbrowski", loadedStudent.getSurname());
        assertEquals("Dominikowicz", loadedStudent.getPatronymic());
        assertEquals(LocalDate.of(2022, 9, 1), loadedStudent.getEnrollmentDate());
        assertEquals(101, loadedStudent.getGroup());
        assertEquals("f1", loadedStudent.getFaculty().getId());
        assertEquals("sp1", loadedStudent.getSpeciality().getId());
        assertEquals(StudyForm.BUDGET, loadedStudent.getStudyForm());
        assertEquals(person.StudentStatus.ACTIVE, loadedStudent.getStatus());
        assertEquals("john@uni.edu", loadedStudent.getEmail());
        assertEquals("+380998887766", loadedStudent.getPhone());
    }

    @Test
    void saveAllAndLoadAllPreservesTeachersWithAllFields() throws Exception {
        University university = new University();
        Teacher dean = new Teacher("t001", "Amadeusz", "Dudek", "Dariuszowicz", "Dean", null);
        dean.setAcademicDegree("Doctor");
        dean.setAcademicTitle("Professor");
        dean.setEmploymentDate(LocalDate.of(2010, 1, 1));
        dean.setWorkload(1.0);
        dean.setEmail("dean1@uni.edu");
        dean.setPhone("111111");

        Faculty faculty = new Faculty("f2", "Faculty 2", "F2", "222", dean);
        Department dept = new Department("d1", "Dept 1");
        faculty.getDepartments().add(dept);

        Teacher prof = new Teacher("t002", "Aleksander", "Szymański", "Stanisławowicz", "Professor", dept);
        prof.setAcademicDegree("Candidate");
        prof.setAcademicTitle("Docent");
        prof.setEmploymentDate(LocalDate.of(2015, 2, 2));
        prof.setWorkload(1.5);
        prof.setEmail("prof@uni.edu");
        prof.setPhone("222222");
        dept.getTeachers().add(prof);

        university.getFaculties().add(faculty);

        FileStorageUtils.saveAll(university); Thread.sleep(200);

        assertTrue(Files.exists(TEACHERS_FILE));
        String teachersCsv = Files.readString(TEACHERS_FILE, StandardCharsets.UTF_8);

        // Verify header
        assertTrue(teachersCsv.startsWith("id;name;surname;patronymic;position;academicDegree;academicTitle;employmentDate;workload;email;phone;department"));

        // Verify dean row (with DEAN:f2 marker)
        assertTrue(teachersCsv.contains("t001;Amadeusz;Dudek;Dariuszowicz;Dean;Doctor;Professor;2010-01-01;1.0;dean1@uni.edu;111111;DEAN:f2"));

        // Verify professor row
        assertTrue(teachersCsv.contains("t002;Aleksander;Szymański;Stanisławowicz;Professor;Candidate;Docent;2015-02-02;1.5;prof@uni.edu;222222;d1"));

        University loadedUni = new University();
        loadedUni.getFaculties().add(new Faculty("f2", "Faculty 2", "F2", "222", null));
        loadedUni.getFaculties().get(0).getDepartments().add(new Department("d1", "Dept 1"));

        FileStorageUtils.loadAll(loadedUni, new faculty.FacultyService(loadedUni), new speciality.SpecialityService(loadedUni));

        Faculty loadedFac = loadedUni.getFaculties().get(0);
        Teacher loadedDean = loadedFac.getDean();
        assertEquals("t001", loadedDean.getId());
        assertEquals("Amadeusz", loadedDean.getOnlyName());
        assertEquals("Doctor", loadedDean.getAcademicDegree());
        assertEquals("Professor", loadedDean.getAcademicTitle());
        assertEquals(LocalDate.of(2010, 1, 1), loadedDean.getEmploymentDate());
        assertEquals(1.0, loadedDean.getWorkload());
        assertEquals("dean1@uni.edu", loadedDean.getEmail());
        assertEquals("111111", loadedDean.getPhone());
        assertEquals("Dean", loadedDean.getPosition());

        Teacher loadedProf = loadedFac.getDepartments().get(0).getTeachers().get(0);
        assertEquals("t002", loadedProf.getId());
        assertEquals("Aleksander", loadedProf.getOnlyName());
        assertEquals("Candidate", loadedProf.getAcademicDegree());
        assertEquals("Docent", loadedProf.getAcademicTitle());
        assertEquals(LocalDate.of(2015, 2, 2), loadedProf.getEmploymentDate());
        assertEquals(1.5, loadedProf.getWorkload());
        assertEquals("prof@uni.edu", loadedProf.getEmail());
        assertEquals("222222", loadedProf.getPhone());
        assertEquals("Professor", loadedProf.getPosition());
    }

    @Test
    void saveAllDoesNotDuplicateTeacherWhenSamePersonIsDeanAndDepartmentTeacher() throws Exception {
        University university = new University();
        Department department = new Department("d10", "CS Department");
        Teacher sameTeacher = new Teacher("t7777", "Ivan", "Petrenko", "Oleh", "Professor", department);

        Faculty faculty = new Faculty("f10", "Faculty 10", "F10", "contacts", sameTeacher);
        faculty.getDepartments().add(department);
        department.getTeachers().add(sameTeacher);
        university.getFaculties().add(faculty);

        FileStorageUtils.saveAll(university); Thread.sleep(200);

        String teachersCsv = Files.readString(TEACHERS_FILE, StandardCharsets.UTF_8);
        long count = teachersCsv.lines()
                .filter(line -> line.startsWith("t7777;"))
                .count();

        assertEquals(1, count);
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
