package service;

import service.Request;
import service.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import person.Student;
import person.StudentStatus;
import person.StudyForm;
import person.Teacher;
import person.TeacherService;
import person.StudentService;
import person.Position;
import faculty.FacultyService;
import faculty.Faculty;
import department.DepartmentService;
import department.Department;
import speciality.SpecialityService;
import speciality.Speciality;
import university.University;
import university.UniversityService;
import user.UserService;
import user.LoginCredentials;
import user.Role;
import repository.TeacherRepository;
import utils.IdGenerator;
import utils.FileStorageUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    private static final Logger log = LoggerFactory.getLogger(ServerMain.class);
    private static final int PORT = 8080;

    public static void main(String[] args) {
        log.info("Starting DigiUni Server...");
        System.out.println("Starting DigiUni Server...");

        University university = new University();
        UserService userService = UserService.getInstance();
        FacultyService facultyService = new FacultyService(university);
        SpecialityService specialityService = new SpecialityService(university);
        TeacherRepository teacherRepository = new TeacherRepository(university);

        if (FileStorageUtils.hasSavedStructure()) {
            FileStorageUtils.loadAll(university, facultyService, specialityService, userService);
            log.info("Database loaded from files.");
        }

        UniversityService universityService = new UniversityService(university, teacherRepository);
        StudentService studentService = new StudentService(university);
        TeacherService teacherService = new TeacherService(university);
        DepartmentService departmentService = new DepartmentService(university);

        if (!FileStorageUtils.hasSavedStructure()) {
            FileStorageUtils.saveAll(university, userService, universityService);
            log.info("Created new file structure.");
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            log.info("Server is listening on port {}...", PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientIp = clientSocket.getInetAddress().toString();

                new Thread(() -> handleClient(
                        clientSocket,
                        clientIp,
                        userService,
                        teacherService,
                        studentService,
                        facultyService,
                        departmentService,
                        specialityService,
                        university,
                        universityService
                )).start();
            }
        } catch (Exception e) {
            log.error("Server crashed!", e);
        }
    }

    private static void handleClient(Socket clientSocket, String clientIp,
                                     UserService userService,
                                     TeacherService teacherService,
                                     StudentService studentService,
                                     FacultyService facultyService,
                                     DepartmentService departmentService,
                                     SpecialityService specialityService,
                                     University university,
                                     UniversityService universityService) {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            Request request = (Request) in.readObject();
            log.info("Received request '{}' from {}", request.getAction(), clientIp);

            Object responseData = null;
            String responseMessage = "OK";
            boolean isSuccess = true;

            switch (request.getAction()) {
                // --- LOGIN ---
                case "LOGIN" -> {
                    LoginCredentials creds = (LoginCredentials) request.getData();
                    if (userService.loginSuccess(creds.username(), creds.password())) {
                        responseData = userService.getCurrentUser();
                    } else {
                        isSuccess = false;
                        responseMessage = "Invalid credentials";
                    }
                }

                // --- TEACHERS ---
                case "GET_ALL_TEACHERS" -> {
                    responseData = teacherService.getAllTeachers();
                }
                case "SEARCH_TEACHER_BY_NAME" -> {
                    String name = (String) request.getData();
                    responseData = teacherService.findTeachersByFullName(name);
                }
                case "SEARCH_TEACHER_BY_ID" -> {
                    String id = (String) request.getData();
                    responseData = teacherService.findTeacherById(id);
                }
                case "ADD_TEACHER" -> {
                    Teacher clientTeacher = (Teacher) request.getData();

                    Department realDept = null;
                    if (clientTeacher.getDepartment() != null) {
                        realDept = findDepartmentById(university, clientTeacher.getDepartment().getId());
                    }

                    if (realDept == null) {
                        isSuccess = false;
                        responseMessage = "Error: Department not found. Make sure to provide a valid department ID for the teacher.";
                    } else {
                        clientTeacher.setDepartment(realDept);
                        clientTeacher.setFaculty(realDept.getFaculty());

                        clientTeacher.setId(IdGenerator.generateTeacherId(university));

                        teacherService.addTeacher(clientTeacher);
                        FileStorageUtils.saveAll(university, userService, universityService);

                        responseData = clientTeacher;
                        responseMessage = "Teacher successfully added!";
                    }
                }
                case "EDIT_TEACHER" -> {
                    Teacher updatedTeacher = (Teacher) request.getData();
                    List<Teacher> found = teacherService.findTeacherById(updatedTeacher.getId());
                    if (found.isEmpty()) {
                        isSuccess = false;
                        responseMessage = "Teacher not found";
                    } else {
                        Teacher existing = found.get(0);
                        copyTeacherFields(existing, updatedTeacher);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseData = existing;
                        responseMessage = "Teacher successfully updated!";
                    }
                }
                case "DELETE_TEACHER" -> {
                    Teacher teacherToDelete = (Teacher) request.getData();
                    teacherService.deleteTeacher(teacherToDelete);
                    FileStorageUtils.saveAll(university, userService, universityService);
                    responseMessage = "Teacher successfully deleted!";
                }
                case "GET_TEACHERS_BY_DEPARTMENT" -> {
                    String deptId = (String) request.getData();
                    Department dept = findDepartmentById(university, deptId);
                    if (dept == null) {
                        isSuccess = false;
                        responseMessage = "Department not found";
                    } else {
                        responseData = teacherService.getTeachersByDepartment(dept);
                    }
                }

                // --- STUDENTS ---
                case "GET_ALL_STUDENTS" -> responseData = studentService.getAllStudents();
                case "SEARCH_STUDENT_BY_NAME" -> responseData = studentService.findStudentsByFullName((String) request.getData());
                case "SEARCH_STUDENT_BY_ID" -> responseData = studentService.findStudentById((String) request.getData());
                case "SEARCH_STUDENT_BY_GROUP" -> responseData = studentService.findStudentsByGroup((Integer) request.getData());
                case "SEARCH_STUDENT_BY_COURSE" -> responseData = studentService.findStudentsByCourse((Integer) request.getData());
                case "SEARCH_STUDENT_BY_SPECIALITY" -> {
                    String specialityId = (String) request.getData();
                    Speciality speciality = specialityService.findById(specialityId);
                    if (speciality == null) {
                        isSuccess = false;
                        responseMessage = "Speciality not found";
                    } else {
                        responseData = studentService.findStudentsBySpeciality(speciality);
                    }
                }
                case "ADD_STUDENT" -> {
                    Student clientStudent = (Student) request.getData();

                    Faculty realFaculty = facultyService.findById(clientStudent.getFaculty().getId());
                    Speciality realSpec = specialityService.findById(clientStudent.getSpeciality().getId());

                    if (realFaculty == null || realSpec == null) {
                        isSuccess = false;
                        responseMessage = "Error: Faculty or Speciality not found. Make sure to provide valid IDs for both.";
                    } else {
                        clientStudent.setFaculty(realFaculty);
                        clientStudent.setSpeciality(realSpec);

                        clientStudent.setId(IdGenerator.generateStudentId(university, clientStudent.getEnrollmentDate().getYear()));

                        studentService.addStudentToSpeciality(clientStudent, realSpec, clientStudent.getGroup());

                        FileStorageUtils.saveAll(university, userService, universityService);

                        responseData = clientStudent;
                        responseMessage = "Student successfully added!";
                    }
                }
                case "EDIT_STUDENT" -> {
                    Student updatedStudent = (Student) request.getData();
                    List<Student> found = studentService.findStudentById(updatedStudent.getId());
                    if (found.isEmpty()) {
                        isSuccess = false;
                        responseMessage = "Student not found";
                    } else {
                        Student existing = found.get(0);
                        copyStudentFields(existing, updatedStudent);
                        if (existing.getSpeciality() == null) {
                            isSuccess = false;
                            responseMessage = "Student speciality is required";
                        } else {
                            studentService.addStudentToSpeciality(existing, existing.getSpeciality(), existing.getGroup());
                            FileStorageUtils.saveAll(university, userService, universityService);
                            responseData = existing;
                            responseMessage = "Student successfully updated!";
                        }
                    }
                }
                case "DELETE_STUDENT" -> {
                    Student studentToDelete = (Student) request.getData();
                    List<Student> found = studentService.findStudentById(studentToDelete.getId());
                    if (found.isEmpty()) {
                        isSuccess = false;
                        responseMessage = "Student not found";
                    } else {
                        Student existing = found.get(0);
                        studentService.deleteStudent(existing, existing.getSpeciality());
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Student successfully deleted!";
                    }
                }
                case "MOVE_STUDENT_TO_GROUP" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String studentId = (String) data.get("studentId");
                    Integer groupNumber = (Integer) data.get("groupNumber");
                    List<Student> found = studentService.findStudentById(studentId);
                    if (found.isEmpty()) {
                        isSuccess = false;
                        responseMessage = "Student not found";
                    } else {
                        studentService.moveStudentToGroup(found.get(0), groupNumber);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Student moved to group successfully!";
                    }
                }
                case "MOVE_STUDENT_TO_SPECIALITY" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String studentId = (String) data.get("studentId");
                    String facultyId = (String) data.get("facultyId");
                    String specialityId = (String) data.get("specialityId");
                    Integer groupNumber = (Integer) data.get("groupNumber");

                    List<Student> found = studentService.findStudentById(studentId);
                    Faculty faculty = facultyService.findById(facultyId);
                    Speciality speciality = specialityService.findById(specialityId);

                    if (found.isEmpty()) {
                        isSuccess = false;
                        responseMessage = "Student not found";
                    } else if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else if (speciality == null) {
                        isSuccess = false;
                        responseMessage = "Speciality not found";
                    } else {
                        studentService.moveStudentToSpeciality(found.get(0), faculty, speciality, groupNumber);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Student moved to speciality successfully!";
                    }
                }

                // --- FACULTIES ---
                case "GET_ALL_FACULTIES" -> responseData = facultyService.getFaculties();
                case "GET_FACULTY_BY_ID" -> {
                    String id = (String) request.getData();
                    Faculty faculty = facultyService.findById(id);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else {
                        responseData = faculty;
                    }
                }
                case "ADD_FACULTY" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String name = (String) data.get("name");
                    String shortName = (String) data.get("shortName");
                    String contacts = (String) data.get("contacts");
                    Teacher dean = (Teacher) data.get("dean");
                    facultyService.addNewFaculty(name, shortName, contacts, dean, userService);
                    FileStorageUtils.saveAll(university, userService, universityService);
                    responseMessage = "Faculty successfully added!";
                }
                case "DELETE_FACULTY" -> {
                    String id = (String) request.getData();
                    Faculty faculty = facultyService.findById(id);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else {
                        facultyService.deleteFaculty(faculty, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Faculty successfully deleted!";
                    }
                }
                case "EDIT_FACULTY_NAME" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String id = (String) data.get("id");
                    String newName = (String) data.get("newName");
                    Faculty faculty = facultyService.findById(id);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else {
                        facultyService.editFacultyName(faculty, newName, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Faculty name successfully updated!";
                    }
                }
                case "EDIT_FACULTY_CONTACTS" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String facultyId = (String) data.get("facultyId");
                    String newContacts = (String) data.get("newContacts");

                    Faculty faculty = facultyService.findById(facultyId);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else {
                        faculty.setContacts(newContacts);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Contacts updated successfully!";
                    }
                }
                case "EDIT_FACULTY_SHORT_NAME" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String facultyId = (String) data.get("facultyId");
                    String newShortName = (String) data.get("newShortName");

                    Faculty faculty = facultyService.findById(facultyId);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else {
                        faculty.setShortName(newShortName);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Short name updated successfully!";
                    }
                }
                case "ASSIGN_FACULTY_DEAN" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String facultyId = (String) data.get("facultyId");
                    String teacherId = (String) data.get("teacherId");
                    Faculty faculty = facultyService.findById(facultyId);
                    Teacher dean = findTeacherById(teacherService, teacherId);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else if (dean == null) {
                        isSuccess = false;
                        responseMessage = "Teacher not found";
                    } else {
                        facultyService.assignDean(faculty, dean, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Dean assigned successfully!";
                    }
                }

                // --- DEPARTMENTS ---
                case "GET_DEPARTMENTS_BY_FACULTY" -> {
                    String facultyId = (String) request.getData();
                    Faculty faculty = facultyService.findById(facultyId);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else {
                        responseData = departmentService.getDepartments(faculty);
                    }
                }
                case "GET_DEPARTMENT_BY_ID" -> {
                    String deptId = (String) request.getData();
                    Department dept = findDepartmentById(university, deptId);
                    if (dept == null) {
                        isSuccess = false;
                        responseMessage = "Department not found";
                    } else {
                        responseData = dept;
                    }
                }
                case "ADD_DEPARTMENT" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String facultyId = (String) data.get("facultyId");
                    String name = (String) data.get("name");
                    String location = (String) data.get("location");
                    String headId = (String) data.get("headId");
                    Faculty faculty = facultyService.findById(facultyId);
                    Teacher head = headId == null ? null : findTeacherById(teacherService, headId);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else {
                        departmentService.addNewDepartment(name, faculty, head, location, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Department successfully added!";
                    }
                }
                case "DELETE_DEPARTMENT" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String facultyId = (String) data.get("facultyId");
                    String deptId = (String) data.get("departmentId");
                    Faculty faculty = facultyService.findById(facultyId);
                    Department department = findDepartmentById(university, deptId);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else if (department == null) {
                        isSuccess = false;
                        responseMessage = "Department not found";
                    } else {
                        departmentService.deleteDepartment(department, faculty, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Department successfully deleted!";
                    }
                }
                case "EDIT_DEPARTMENT_NAME" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String facultyId = (String) data.get("facultyId");
                    String deptId = (String) data.get("departmentId");
                    String newName = (String) data.get("newName");
                    Faculty faculty = facultyService.findById(facultyId);
                    Department department = findDepartmentById(university, deptId);
                    if (faculty == null || department == null) {
                        isSuccess = false;
                        responseMessage = "Faculty or Department not found";
                    } else {
                        departmentService.editDepartmentName(department, newName, faculty, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Department name successfully updated!";
                    }
                }
                case "EDIT_DEPARTMENT_HEAD" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String deptId = (String) data.get("departmentId");
                    String teacherId = (String) data.get("teacherId");
                    Department department = findDepartmentById(university, deptId);
                    Teacher teacher = findTeacherById(teacherService, teacherId);
                    if (department == null) {
                        isSuccess = false;
                        responseMessage = "Department not found";
                    } else if (teacher == null) {
                        isSuccess = false;
                        responseMessage = "Teacher not found";
                    } else {
                        departmentService.editDepartmentHead(department, teacher, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Department head successfully updated!";
                    }
                }
                case "EDIT_DEPARTMENT_LOCATION" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String deptId = (String) data.get("departmentId");
                    String location = (String) data.get("location");
                    Department department = findDepartmentById(university, deptId);
                    if (department == null) {
                        isSuccess = false;
                        responseMessage = "Department not found";
                    } else {
                        departmentService.editDepartmentLocation(department, location, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Department location successfully updated!";
                    }
                }

                // --- SPECIALITIES ---
                case "GET_SPECIALITIES_BY_FACULTY" -> {
                    String facultyId = (String) request.getData();
                    Faculty faculty = facultyService.findById(facultyId);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else {
                        responseData = faculty.getSpeciality();
                    }
                }
                case "GET_SPECIALITY_BY_ID" -> {
                    String specialityId = (String) request.getData();
                    Speciality speciality = specialityService.findById(specialityId);
                    if (speciality == null) {
                        isSuccess = false;
                        responseMessage = "Speciality not found";
                    } else {
                        responseData = speciality;
                    }
                }
                case "ADD_SPECIALITY" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String facultyId = (String) data.get("facultyId");
                    String name = (String) data.get("name");
                    Faculty faculty = facultyService.findById(facultyId);
                    if (faculty == null) {
                        isSuccess = false;
                        responseMessage = "Faculty not found";
                    } else {
                        specialityService.addNewSpeciality(name, faculty, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Speciality successfully added!";
                    }
                }
                case "DELETE_SPECIALITY" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String facultyId = (String) data.get("facultyId");
                    String specialityId = (String) data.get("specialityId");
                    Faculty faculty = facultyService.findById(facultyId);
                    Speciality speciality = specialityService.findById(specialityId);
                    if (faculty == null || speciality == null) {
                        isSuccess = false;
                        responseMessage = "Faculty or Speciality not found";
                    } else {
                        specialityService.deleteSpeciality(speciality, faculty, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Speciality successfully deleted!";
                    }
                }
                case "EDIT_SPECIALITY_NAME" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String facultyId = (String) data.get("facultyId");
                    String specialityId = (String) data.get("specialityId");
                    String newName = (String) data.get("newName");
                    Faculty faculty = facultyService.findById(facultyId);
                    Speciality speciality = specialityService.findById(specialityId);
                    if (faculty == null || speciality == null) {
                        isSuccess = false;
                        responseMessage = "Faculty or Speciality not found";
                    } else {
                        specialityService.editSpecialityName(speciality, newName, faculty, userService);
                        FileStorageUtils.saveAll(university, userService, universityService);
                        responseMessage = "Speciality name successfully updated!";
                    }
                }

                // --- USERS ---
                case "LOGOUT" -> {
                    userService.logout();
                    responseMessage = "Logged out";
                }
                case "GET_CURRENT_USER" -> responseData = userService.getCurrentUser();
                case "GET_ALL_USERS" -> responseData = userService.getAllUsers();
                case "REGISTER_USER" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String username = (String) data.get("username");
                    String password = (String) data.get("password");
                    Role role = Role.valueOf(((String) data.get("role")).trim().toUpperCase());
                    userService.registerNewUser(username, password, role);
                    FileStorageUtils.saveAll(university, userService, universityService);
                    responseMessage = "User successfully registered!";
                }
                case "DELETE_USER" -> {
                    String username = (String) request.getData();
                    userService.deleteUser(username);
                    FileStorageUtils.saveAll(university, userService, universityService);
                    responseMessage = "User delete request processed";
                }
                case "EDIT_USER_ROLE" -> {
                    Map<String, Object> data = asMap(request.getData());
                    String username = (String) data.get("username");
                    Role role = Role.valueOf(((String) data.get("role")).trim().toUpperCase());
                    userService.editUser(username, role);
                    FileStorageUtils.saveAll(university, userService, universityService);
                    responseMessage = "User role updated";
                }

                // --- STORAGE ---
                case "SAVE_ALL" -> {
                    FileStorageUtils.saveAll(university, userService, universityService);
                    responseMessage = "Data successfully saved";
                }
                case "RELOAD_ALL" -> {
                    FileStorageUtils.loadAll(university, facultyService, specialityService, userService);
                    responseMessage = "Data successfully reloaded";
                }
                case "GET_UNIVERSITY" -> responseData = university;
                case "EDIT_UNIVERSITY" -> {
                    university.UniversityInfo newInfo = (university.UniversityInfo) request.getData();
                    university.setInfo(newInfo);
                    FileStorageUtils.saveAll(university, userService, universityService);
                    responseMessage = "University settings successfully updated!";
                }

                default -> {
                    isSuccess = false;
                    responseMessage = "Unknown action: " + request.getAction();
                }
            }

            out.writeObject(new Response(isSuccess, responseMessage, responseData));
            out.flush();

        } catch (Exception e) {
            log.error("Error processing client {}", clientIp, e);
        } finally {
            try { clientSocket.close(); } catch (Exception ignored) {}
        }
    }

    private static Teacher findTeacherById(TeacherService teacherService, String teacherId) {
        List<Teacher> teachers = teacherService.findTeacherById(teacherId);
        return teachers.isEmpty() ? null : teachers.get(0);
    }

    private static Department findDepartmentById(University university, String departmentId) {
        if (departmentId == null) {
            return null;
        }
        for (Faculty faculty : university.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                if (departmentId.equalsIgnoreCase(department.getId())) {
                    return department;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object data) {
        if (data instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException("Request data must be a map for this action");
    }

    private static void copyTeacherFields(Teacher target, Teacher source) {
        if (source.getName() != null) target.setName(source.getName());
        if (source.getSurname() != null) target.setSurname(source.getSurname());
        if (source.getPatronymic() != null) target.setPatronymic(source.getPatronymic());
        if (source.getEmail() != null) target.setEmail(source.getEmail());
        if (source.getPhone() != null) target.setPhone(source.getPhone());
        if (source.getDateOfBirth() != null) target.setDateOfBirth(source.getDateOfBirth());
        if (source.getAcademicDegree() != null) target.setAcademicDegree(source.getAcademicDegree());
        if (source.getAcademicTitle() != null) target.setAcademicTitle(source.getAcademicTitle());
        if (source.getEmploymentDate() != null) target.setEmploymentDate(source.getEmploymentDate());
        if (source.getPosition() != null) target.setPosition(source.getPosition().toString());
        if (source.getDepartment() != null) {
            target.setDepartment(source.getDepartment());
            target.setFaculty(source.getDepartment().getFaculty());
        }
        if (source.getGender() != null) target.changeGender(source.getGender());
        if (source.getWorkload() > 0) target.setWorkload(source.getWorkload());
    }

    private static void copyStudentFields(Student target, Student source) {
        if (source.getName() != null) target.setName(source.getName());
        if (source.getSurname() != null) target.setSurname(source.getSurname());
        if (source.getPatronymic() != null) target.setPatronymic(source.getPatronymic());
        if (source.getEmail() != null) target.setEmail(source.getEmail());
        if (source.getPhone() != null) target.setPhone(source.getPhone());
        if (source.getDateOfBirth() != null) target.setDateOfBirth(source.getDateOfBirth());
        if (source.getEnrollmentDate() != null) target.setEnrollmentDate(source.getEnrollmentDate());
        if (source.getFaculty() != null) target.setFaculty(source.getFaculty());
        if (source.getSpeciality() != null) target.setSpeciality(source.getSpeciality());
        if (source.getStudyForm() != null) target.setStudyForm(source.getStudyForm());
        if (source.getStatus() != null) target.setStatus(source.getStatus());
        if (source.getGender() != null) target.changeGender(source.getGender());
        if (source.getGroup() > 0) target.setGroup(source.getGroup());
    }
}