package person;

import java.util.ArrayList;
import java.util.List;
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
        List<Teacher> allTeachers = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Department dept : faculty.getDepartments()) {
                allTeachers.addAll(dept.getTeachers());
            }
        }
        if (allTeachers.isEmpty()) {
            System.out.println("No teachers found!");
        }
        return allTeachers;
    }

    // Find teachers by name
    public List<Teacher> findTeachersByFullName(String namePart) {
        List<Teacher> result = new ArrayList<>();

        for (Faculty f : university.getFaculties()) {
            for (Department d : f.getDepartments()) {
                for (Teacher t : d.getTeachers()) {
                    if (t.getFullName().toLowerCase().contains(namePart.toLowerCase())) {
                        result.add(t);
                    }
                }
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

    public void deleteTeacher(Teacher teacher, Department department) {
        Objects.requireNonNull(teacher, "Teacher cannot be null");
        Objects.requireNonNull(department, "Department cannot be null");

        boolean removed = department.getTeachers().remove(teacher);

        if (removed) {
            System.out.println("Teacher " + teacher.getFullName() + " removed from " + department.getName());
        } else {
            System.out.println("Error: Teacher not found in this department.");
        }
    }
}
