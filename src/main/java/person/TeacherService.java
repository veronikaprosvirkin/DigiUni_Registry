package person;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import university.University;
import department.Department;
import faculty.Faculty;
import utils.IdGenerator;

public class TeacherService {
    private University university;

    public TeacherService(University university) {
        this.university = university;
    }
    // Adding a teacher
    public void addTeacher(String name, String surname, String patronymic, String position, Department selectedDept) {
        Objects.requireNonNull(selectedDept, "Department cannot be null");
        if (selectedDept != null) {
            Teacher newTeacher = new Teacher(IdGenerator.generateTeacherId(), name, surname, patronymic, position, selectedDept);
            selectedDept.getTeachers().add(newTeacher);
        }
    }

    public void addTeacher(Teacher teacher) {
        Objects.requireNonNull(teacher, "Teacher cannot be null");
        if (teacher != null && teacher.getDepartment() != null) {
            teacher.getDepartment().getTeachers().add(teacher);
        }
    }

    //** ===== SEARCH ===== **/
    // find all teachers
    public List<Teacher> getAllTeachers() {
        List<Teacher> allTeachers = new ArrayList<>(collectUniqueTeachers().values());
        if (allTeachers.isEmpty()) {
            System.out.println("No teachers found!");
        }
        return allTeachers;
    }

    // Find teachers by name
    public List<Teacher> findTeachersByFullName(String namePart) {
        List<Teacher> result = new ArrayList<>();

        for (Teacher teacher : collectUniqueTeachers().values()) {
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

        for (Teacher t : getAllTeachers()){
            if (t.getId().equalsIgnoreCase(id)){
                result.add(t);
                break;
            }
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

        boolean removed = false;

        for (Faculty faculty : university.getFaculties()) {
            if (sameTeacher(faculty.getDean(), teacher)) {
                faculty.setDean(null);
                removed = true;
            }

            for (Department department : faculty.getDepartments()) {
                if (sameTeacher(department.getHead(), teacher)) {
                    department.setHead(null);
                    removed = true;
                }

                if (department.getTeachers().removeIf(t -> sameTeacher(t, teacher))) {
                    removed = true;
                }
            }
        }

        if (removed) {
            teacher.setDepartment(null);
            System.out.println("Teacher " + teacher.getFullName() + " deleted.");
        } else {
            System.out.println("Error: Teacher not found.");
        }
    }

    public void deleteTeacher(Teacher teacher, Department department) {
        deleteTeacher(teacher);
    }

    private boolean sameTeacher(Teacher first, Teacher second) {
        return first != null && second != null && first.getId().equalsIgnoreCase(second.getId());
    }

    private Map<String, Teacher> collectUniqueTeachers() {
        Map<String, Teacher> teachersById = new LinkedHashMap<>();

        for (Faculty faculty : university.getFaculties()) {
            Teacher dean = faculty.getDean();
            if (dean != null && dean.getId() != null && !dean.getId().isBlank()) {
                teachersById.putIfAbsent(dean.getId().toLowerCase(), dean);
            }

            for (Department department : faculty.getDepartments()) {
                for (Teacher teacher : department.getTeachers()) {
                    if (teacher != null && teacher.getId() != null && !teacher.getId().isBlank()) {
                        teachersById.putIfAbsent(teacher.getId().toLowerCase(), teacher);
                    }
                }
            }
        }

        return teachersById;
    }
}
