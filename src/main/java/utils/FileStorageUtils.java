package utils;

import faculty.Faculty;
import faculty.FacultyService;
import person.*;
import speciality.Group;
import speciality.Speciality;
import department.Department;
import speciality.SpecialityService;
import university.University;
import user.Permission;
import user.Role;
import user.User;
import user.UserService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class FileStorageUtils {

    private static final Path FACULTIES_FILE = Path.of("data", "faculties.csv");
    private static final Path SPECIALITIES_FILE = Path.of("data", "specialities.csv");
    private static final Path DEPARTMENTS_FILE = Path.of("data", "departments.csv");
    private static final Path TEACHERS_FILE = Path.of("data", "teachers.csv");
    private static final Path STUDENTS_FILE = Path.of("data", "students.csv");
    private static final Path USERS_FILE = Path.of("data", "users.csv");
    private static final String DELIMITER = ";";
    private static final String STUDENTS_HEADER = "id;name;surname;patronymic;course;enrollmentDate;group;faculty;speciality;studyForm;status;email;phone;dateOfBirth;age";
    private static final String TEACHERS_HEADER = "id;name;surname;patronymic;position;academicDegree;academicTitle;employmentDate;workload;email;phone;department;dateOfBirth;age";
    private static final String FACULTIES_HEADER = "id;name;shortName;contacts;deanId;deanName;deanSurname;deanPatronymic;deanPosition;deanAcademicDegree;deanAcademicTitle;deanEmploymentDate;deanWorkload;deanEmail;deanPhone";
    private static final String SPECIALITIES_HEADER = "id;name;facultyId";
    private static final String DEPARTMENTS_HEADER = "id;name;facultyId;location;headId;headName;headSurname;headPatronymic;headPosition;headAcademicDegree;headAcademicTitle;headEmploymentDate;headWorkload;headEmail;headPhone";
    private static final String USERS_HEADER = "username;password;role";

    private static final ReentrantLock saveLock = new ReentrantLock();

    // Save all structure
    public static void saveAll(University university, UserService userService, university.UniversityService universityService) {
        try {
            Files.createDirectories(FACULTIES_FILE.getParent());
        } catch (IOException e) {
            System.err.println("Failed to create data directory");
        }

        List<Faculty> faculties = university.getFaculties();
        List<Student> students = gatherAllStudents(university);
        List<User> users = userService.getAllUsers();

        saveFaculties(faculties);
        saveSpecialities(faculties);
        saveDepartments(faculties);
        saveStudents(students);
        saveTeachers(faculties);
        saveUsers(users);
    }

    // True when at least one persisted faculty row exists.
    public static boolean hasSavedStructure() {
        if (!Files.exists(FACULTIES_FILE)) {
            return false;
        }

        try (BufferedReader r = Files.newBufferedReader(FACULTIES_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (!line.isBlank()) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }

        return false;
    }

    // Load all structure
    public static void loadAll(University university, FacultyService facultyService, SpecialityService specialityService, UserService userService) {
        try {
            university.getFaculties().clear();
            if (Files.exists(FACULTIES_FILE)) loadFaculties(university);
            if (Files.exists(SPECIALITIES_FILE)) loadSpecialities(university);
            if (Files.exists(DEPARTMENTS_FILE)) loadDepartments(university);
            if (Files.exists(TEACHERS_FILE)) loadTeachers(university);
            if (Files.exists(STUDENTS_FILE)) loadStudents(university);
            if (Files.exists(USERS_FILE)) loadUsers(userService);
        } catch (IOException e) {
            System.err.println("Load error");
        }
    }

    // Save faculties
    private static void saveFaculties(List<Faculty> faculties) {
        new Thread(() -> {
            saveLock.lock();
            try (BufferedWriter w = Files.newBufferedWriter(FACULTIES_FILE, StandardCharsets.UTF_8)) {
                w.write(FACULTIES_HEADER);
                w.newLine();
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
            } catch (IOException e) {
                System.err.println("Save faculties error: " + e.getMessage());
            } finally {
                saveLock.unlock();
            }
        }).start();
    }

    // Load faculties
    private static void loadFaculties(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(FACULTIES_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length > 0 && "id".equalsIgnoreCase(parts[0])) continue;
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
    private static void saveSpecialities(List<Faculty> faculties) {
        new Thread(() -> {
            saveLock.lock();
            try (BufferedWriter w = Files.newBufferedWriter(SPECIALITIES_FILE, StandardCharsets.UTF_8)) {
                w.write(SPECIALITIES_HEADER);
                w.newLine();
                for (Faculty f : faculties) {
                    for (Speciality s : f.getSpeciality()) {
                        w.write(String.join(DELIMITER, value(s.getId()), value(s.getNameOfSpeciality()), value(f.getId())));
                        w.newLine();
                    }
                }
            } catch (IOException e) {
                System.err.println("Save specialities error: " + e.getMessage());
            } finally {
                saveLock.unlock();
            }
        }).start();
    }

    // Load specialities and link to faculty
    private static void loadSpecialities(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(SPECIALITIES_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length > 0 && "id".equalsIgnoreCase(parts[0])) continue;
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
    private static void saveDepartments(List<Faculty> faculties) {
        new Thread(() -> {
            saveLock.lock();
            try (BufferedWriter w = Files.newBufferedWriter(DEPARTMENTS_FILE, StandardCharsets.UTF_8)) {
                w.write(DEPARTMENTS_HEADER);
                w.newLine();
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
            } catch (IOException e) {
                System.err.println("Save departments error: " + e.getMessage());
            } finally {
                saveLock.unlock();
            }
        }).start();
    }

    // Load departments and link to faculty
    private static void loadDepartments(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(DEPARTMENTS_FILE, StandardCharsets.UTF_8)) {
            String line;

            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length > 0 && "id".equalsIgnoreCase(parts[0])) continue;
                if (parts.length >= 4) {
                    String id = parts[0];
                    String name = parts[1];
                    String facultyId = parts[2];
                    Department d = new Department(id, name);
                    d.setLocation(parts[3].isBlank() ? null : parts[3]);
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
    //safe students
    private static void saveStudents(List<Student> students) {
        new Thread(() -> {
            saveLock.lock();
            try (BufferedWriter w = Files.newBufferedWriter(STUDENTS_FILE, StandardCharsets.UTF_8)) {
                w.write(STUDENTS_HEADER);
                w.newLine();
                for (Student s : students){
                    w.write(String.join(DELIMITER,
                            value(s.getId()),
                            value(s.getOnlyName()),
                            value(s.getSurname()),
                            value(s.getPatronymic()),
                            value(s.getCourseDisplay()),

                            value(s.getEnrollmentDate() != null ? s.getEnrollmentDate().toString() : ""),
                            value(s.getGroup() != 0 ? String.valueOf(s.getGroup()) : ""),
                            value(s.getFaculty() != null ? s.getFaculty().getId() : ""),
                            value(s.getSpeciality() != null ? s.getSpeciality().getId() : ""),

                            value(s.getStudyForm() != null ? s.getStudyForm().toString() : ""),
                            value(s.getStatus() != null ? s.getStatus().toString() : ""),
                            value(s.getEmail()),
                            value(s.getPhone()),
                            value(s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : ""),
                            value(s.getAge() != null ? String.valueOf(s.getAge()) : "")
                    ));
                    w.newLine();
                }
            } catch (IOException e) {
                System.err.println("Save students error: " + e.getMessage());
            } finally {
                saveLock.unlock();
            }
        }).start();
    }
    //load students
    private static void loadStudents(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(STUDENTS_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split(DELIMITER, -1);
                if (parts.length > 0 && "id".equalsIgnoreCase(parts[0])) continue;

                if (parts.length >= 11) {
                    try {
                        String id = parts[0];
                        String name = parts[1];
                        String surname = parts[2];
                        String patronymic = parts[3];

                        LocalDate enrollmentDate = null;
                        if (!parts[5].isEmpty() && !parts[5].equals("null")) {
                            enrollmentDate = LocalDate.parse(parts[5]);
                        }

                        int group = 1;
                        if (!parts[6].isEmpty() && !parts[6].equals("null")) {
                            group = Integer.parseInt(parts[6]);
                        }

                        String facultyId = parts[7];
                        String specialityId = parts[8];
                        String studyFormStr = parts[9];
                        String statusStr = parts[10];
                        String email = parts.length > 11 ? parts[11] : "";
                        String phone = parts.length > 12 ? parts[12] : "";
                        String dobStr = parts.length > 13 ? parts[13] : "";

                        StudyForm form = null;
                        if (!studyFormStr.isEmpty() && !studyFormStr.equals("null")) {
                            form = StudyForm.valueOf(studyFormStr);
                        }

                        StudentStatus status = null;
                        if (!statusStr.isEmpty() && !statusStr.equals("null")) {
                            status = StudentStatus.valueOf(statusStr);
                        }

                        Faculty faculty = null;
                        if (!facultyId.isEmpty() && !facultyId.equals("null")) {
                            for (Faculty f : u.getFaculties()) {
                                if (f.getId().equals(facultyId)) {
                                    faculty = f;
                                    break;
                                }
                            }
                        }

                        Speciality speciality = null;
                        if (!specialityId.isEmpty() && !specialityId.equals("null")) {
                            for (Faculty f : u.getFaculties()) {
                                for (Speciality s : f.getSpeciality()) {
                                    if (s.getId().equals(specialityId)) {
                                        speciality = s;
                                        break;
                                    }
                                }
                                if (speciality != null) break;
                            }
                        }

                        Student student = new Student(id, name, surname, patronymic, enrollmentDate, group, faculty, speciality, form);
                        student.setEmail(blankToNull(email));
                        student.setPhone(blankToNull(phone));
                        if (!dobStr.isEmpty() && !dobStr.equals("null")) {
                            student.setDateOfBirth(LocalDate.parse(dobStr));
                        }
                        IdGenerator.updateStudentCounter(student.getId());

                        if (status != null) {
                            student.setStatus(status);
                        }

                        if (speciality != null) {
                            final int finalGroupNum = group;
                            Group targetGroup = speciality.getGroups().stream()
                                    .filter(g -> g.getGroupNumber() == finalGroupNum)
                                    .findFirst()
                                    .orElse(null);

                            if (targetGroup != null) {
                                targetGroup.getStudents().add(student);
                            } else {
                                targetGroup = new Group(finalGroupNum);
                                speciality.getGroups().add(targetGroup);
                                targetGroup.getStudents().add(student);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Skipped broken student line: " + line);
                    }
                }
            }
        }
    }

    public static void updateStudentRecord(Student student) {
        if (student == null || student.getId() == null || student.getId().isBlank()) {
            return;
        }

        saveLock.lock();
        try {
            Files.createDirectories(STUDENTS_FILE.getParent());

            List<String> rows = new ArrayList<>();
            boolean replaced = false;
            if (Files.exists(STUDENTS_FILE)) {
                List<String> existing = Files.readAllLines(STUDENTS_FILE, StandardCharsets.UTF_8);
                for (String row : existing) {
                    if (row == null || row.isBlank()) {
                        continue;
                    }
                    String[] parts = row.split(DELIMITER, -1);
                    if (parts.length > 0 && "id".equalsIgnoreCase(parts[0])) {
                        continue;
                    }
                    if (parts.length > 0 && student.getId().equals(parts[0])) {
                        rows.add(toStudentCsvRow(student));
                        replaced = true;
                    } else {
                        rows.add(row);
                    }
                }
            }

            if (!replaced) {
                rows.add(toStudentCsvRow(student));
            }

            try (BufferedWriter writer = Files.newBufferedWriter(STUDENTS_FILE, StandardCharsets.UTF_8)) {
                writer.write(STUDENTS_HEADER);
                writer.newLine();
                for (String row : rows) {
                    writer.write(row);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Save student record error: " + e.getMessage());
        } finally {
            saveLock.unlock();
        }
    }
    // Save teachers
    private static void saveTeachers(List<Faculty> faculties) {
        new Thread(() -> {
            saveLock.lock();
            try (BufferedWriter w = Files.newBufferedWriter(TEACHERS_FILE, StandardCharsets.UTF_8)) {
                w.write(TEACHERS_HEADER);
                w.newLine();
                Set<String> savedTeacherIds = new HashSet<>();
                for (Faculty f : faculties) {
                    if (f.getDean() != null) {
                        writeTeacherRowIfNeeded(w, f.getDean(), "DEAN:" + f.getId(), savedTeacherIds);
                    }
                    for (Department d : f.getDepartments()) {
                        if (d.getTeachers() != null) {
                            for (Teacher t : d.getTeachers()) {
                                writeTeacherRowIfNeeded(w, t, d.getId(), savedTeacherIds);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Save teachers error: " + e.getMessage());
            } finally {
                saveLock.unlock();
            }
        }).start();
    }
    // save users
    private static void saveUsers(List<User> users){
         new Thread(() -> {
             saveLock.lock();
             try (BufferedWriter w = Files.newBufferedWriter(USERS_FILE, StandardCharsets.UTF_8)){
                 w.write(USERS_HEADER);
                 w.newLine();
                  for (User u : users) {
                      String line = u.getUsername() + DELIMITER +
                              u.getPassword() + DELIMITER +
                              u.getRole();
                      w.write(line);
                     w.newLine();
                 }

             } catch (IOException e) {
                 throw new RuntimeException(e);
             }finally {
                 saveLock.unlock();
             }

         }).start();
    }
    //load users
    public static void loadUsers(UserService userService) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(USERS_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length > 0 && "username".equalsIgnoreCase(parts[0])) continue;
                if (parts.length >= 3) {
                    String username = parts[0];
                    String password = parts[1];
                     try {
                         Role role = Role.valueOf(parts[2].trim());
                         int userMask = Permission.getDefaultMaskForRole(role);
                         userService.addUserFromStorage(new User(username, password, role, userMask));

                    } catch (IllegalArgumentException e) {
                        System.err.println("Error with loading user " + username + ": unrecognized role '" + parts[2] + "'");
                    }
                }
            }
        }
    }

    private static void writeTeacherRowIfNeeded(BufferedWriter w, Teacher t, String ownerId, Set<String> savedTeacherIds) throws IOException {
        if (t == null || t.getId() == null || t.getId().isBlank()) {
            return;
        }
        if (!savedTeacherIds.add(t.getId())) {
            return;
        }
        writeTeacherRow(w, t, ownerId);
    }

    private static void writeTeacherRow(BufferedWriter w, Teacher t, String ownerId) throws IOException {
        w.write(String.join(DELIMITER,
                value(t.getId()),
                value(t.getOnlyName()),
                value(t.getSurname()),
                value(t.getPatronymic()),
                value(t.getPosition()),
                value(t.getAcademicDegree()),
                value(t.getAcademicTitle()),
                value(t.getEmploymentDate() != null ? t.getEmploymentDate().toString() : ""),
                value(String.valueOf(t.getWorkload())),
                value(t.getEmail()),
                value(t.getPhone()),
                value(ownerId),
                value(t.getDateOfBirth() != null ? t.getDateOfBirth().toString() : ""),
                value(t.getAge() != null ? String.valueOf(t.getAge()) : "")
        ));
        w.newLine();
    }

    // Load teachers
    private static void loadTeachers(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(TEACHERS_FILE, StandardCharsets.UTF_8)) {
            String line;
            Map<String, Teacher> teachersById = new HashMap<>();
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(DELIMITER, -1);
                if (parts.length > 0 && "id".equalsIgnoreCase(parts[0])) continue;

                Teacher teacher = restoreTeacher(parts, 0);

                if (teacher != null && parts.length >= 12) {
                    Teacher canonicalTeacher = teachersById.computeIfAbsent(teacher.getId(), key -> teacher);
                    String ownerId = parts[11];
                    boolean found = false;

                    if (ownerId != null && ownerId.startsWith("DEAN:")) {
                        String facultyId = ownerId.substring("DEAN:".length());
                        for (Faculty f : u.getFaculties()) {
                            if (f.getId().equals(facultyId)) {
                                f.setDean(canonicalTeacher);
                                found = true;
                                break;
                            }
                        }
                    }

                    if (!found) {
                        for (Faculty f : u.getFaculties()) {
                            for (Department d : f.getDepartments()) {
                                if (d.getId().equals(ownerId)) {
                                    canonicalTeacher.setDepartment(d);
                                    d.getTeachers().add(canonicalTeacher);
                                    found = true;
                                    break;
                                }
                            }
                            if (found) break;
                        }
                    }
                    IdGenerator.updateTeacherCounter(canonicalTeacher.getId());
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

        String dob = blankToNull(part(parts, startIndex + 12));
        if (dob != null && !dob.equals("null")) {
            try {
                teacher.setDateOfBirth(LocalDate.parse(dob));
            } catch (Exception ignored) {
            }
        }

        return teacher;
    }

    private static String part(String[] parts, int index) {
        if (index < 0 || index >= parts.length) return null;
        return parts[index];
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private static String toStudentCsvRow(Student s) {
        return String.join(DELIMITER,
                value(s.getId()),
                value(s.getOnlyName()),
                value(s.getSurname()),
                value(s.getPatronymic()),
                value(s.getCourseDisplay()),
                value(s.getEnrollmentDate() != null ? s.getEnrollmentDate().toString() : ""),
                value(s.getGroup() != 0 ? String.valueOf(s.getGroup()) : ""),
                value(s.getFaculty() != null ? s.getFaculty().getId() : ""),
                value(s.getSpeciality() != null ? s.getSpeciality().getId() : ""),
                value(s.getStudyForm() != null ? s.getStudyForm().toString() : ""),
                value(s.getStatus() != null ? s.getStatus().toString() : ""),
                value(s.getEmail()),
                value(s.getPhone()),
                value(s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : ""),
                value(s.getAge() != null ? String.valueOf(s.getAge()) : "")
        );
    }

    private static List<Student> gatherAllStudents(University university) {
        List<Student> allStudents = new java.util.ArrayList<>();
        if (university.getFaculties() != null) {
            for (Faculty faculty : university.getFaculties()) {
                if (faculty.getSpeciality() != null) {
                    for (Speciality speciality : faculty.getSpeciality()) {
                        if (speciality.getGroups() != null) {
                            for (Group group : speciality.getGroups()) {
                                if (group.getStudents() != null) {
                                    allStudents.addAll(group.getStudents());
                                }
                            }
                        }
                    }
                }
            }
        }
        return allStudents;
    }
}
