package person;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.TeacherRepository;
import university.University;
import department.Department;
import utils.IdGenerator;
import utils.validation.EntityValidator;

public class TeacherService {
    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);
    private final TeacherRepository teacherRepository;

    public TeacherService(University university) {
        this.teacherRepository = new TeacherRepository(university);
    }
    // Adding a teacher
    public void addTeacher(String name, String surname, String patronymic, Position position, Department selectedDept) {
        Objects.requireNonNull(selectedDept, "Department cannot be null");
        Teacher newTeacher = new Teacher(IdGenerator.generateTeacherId(), name, surname, patronymic, position, selectedDept);
        try {
            EntityValidator.validate(newTeacher);
        } catch (IllegalArgumentException e) {
            log.error("Teacher validation failed: {}", e.getMessage());
            System.out.println(e.getMessage());
            return;
        }
        teacherRepository.save(newTeacher);
        log.info("Teacher {} created in department {}", newTeacher.getId(), selectedDept.getId());
    }

    public void addTeacher(Teacher teacher) {
        Objects.requireNonNull(teacher, "Teacher cannot be null");
        try {
            EntityValidator.validate(teacher);
        } catch (IllegalArgumentException e) {
            log.error("Teacher validation failed: {}", e.getMessage());
            System.out.println(e.getMessage());
            return;
        }
        teacherRepository.save(teacher);
        log.info("Teacher {} created", teacher.getId());
    }

    //** ===== SEARCH ===== **/
    // find all teachers
    public List<Teacher> getAllTeachers() {
        List<Teacher> allTeachers = teacherRepository.findAll();
        if (allTeachers.isEmpty()) {
            log.info("No teachers found during getAllTeachers");
            System.out.println("No teachers found!");
        }
        return allTeachers;
    }

    // Find teachers by name
    public List<Teacher> findTeachersByFullName(String namePart) {
        List<Teacher> result = new ArrayList<>();

        for (Teacher teacher : teacherRepository.findAll()) {
            if (teacher.getFullName().toLowerCase().contains(namePart.toLowerCase())) {
                result.add(teacher);
            }
        }
        if (result.isEmpty()) {
            log.info("No teacher found by name query: {}", namePart);
            System.out.println("No teacher found by name " + namePart);
        }
        return result;
    }

    // Find teachers by ID
    public List<Teacher> findTeacherById(String id) {
        List<Teacher> result = new ArrayList<>();

        Teacher teacher = teacherRepository.findById(id);
        if (teacher != null) {
            result.add(teacher);
        }
        if (result.isEmpty()){
            log.info("No teacher found by id {}", id);
            System.out.println("No teacher found by id " + id);
        }
        return result;
    }

    public List<Teacher> getTeachersByDepartment(Department department) {
        Objects.requireNonNull(department, "Department cannot be null");
        return department.getTeachers();
    }

    public void deleteTeacher(Teacher teacher) {
        Objects.requireNonNull(teacher, "Teacher cannot be null");

        boolean removed = teacherRepository.findById(teacher.getId()) != null;
        if (removed) {
            teacherRepository.delete(teacher.getId());
        }

        if (removed) {
            teacher.setDepartment(null);
            log.info("Teacher {} deleted", teacher.getId());
            System.out.println("Teacher " + teacher.getFullName() + " deleted.");
        } else {
            log.warn("Failed to delete teacher {}: not found", teacher.getId());
            System.out.println("Error: Teacher not found.");
        }
    }

    public void deleteTeacher(Teacher teacher, @SuppressWarnings("unused") Department department) {
        deleteTeacher(teacher);
    }
}
