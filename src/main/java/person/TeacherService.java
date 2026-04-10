package person;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import repository.TeacherRepository;
import university.University;
import department.Department;
import utils.IdGenerator;

public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(University university) {
        this.teacherRepository = new TeacherRepository(university);
    }
    // Adding a teacher
    public void addTeacher(String name, String surname, String patronymic, String position, Department selectedDept) {
        Objects.requireNonNull(selectedDept, "Department cannot be null");
        Teacher newTeacher = new Teacher(IdGenerator.generateTeacherId(), name, surname, patronymic, position, selectedDept);
        teacherRepository.save(newTeacher);
    }

    public void addTeacher(Teacher teacher) {
        Objects.requireNonNull(teacher, "Teacher cannot be null");
        teacherRepository.save(teacher);
    }

    //** ===== SEARCH ===== **/
    // find all teachers
    public List<Teacher> getAllTeachers() {
        List<Teacher> allTeachers = teacherRepository.findAll();
        if (allTeachers.isEmpty()) {
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
            System.out.println("Teacher " + teacher.getFullName() + " deleted.");
        } else {
            System.out.println("Error: Teacher not found.");
        }
    }

    public void deleteTeacher(Teacher teacher, @SuppressWarnings("unused") Department department) {
        deleteTeacher(teacher);
    }
}
