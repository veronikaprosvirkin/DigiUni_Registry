package repository;

import department.Department;
import faculty.Faculty;
import person.Teacher;
import university.University;
import user.UserService;
import utils.FileStorageUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TeacherRepository implements Repository<Teacher, String> {
    private final University university;

    public TeacherRepository(University university) {
        this.university = university;
    }

    @Override
    public Teacher findById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        for (Teacher teacher : findAll()) {
            if (id.equalsIgnoreCase(teacher.getId())) {
                return teacher;
            }
        }

        return null;
    }

    @Override
    public List<Teacher> findAll() {
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

        return new ArrayList<>(teachersById.values());
    }

    @Override
    public void save(Teacher entity) {
        if (entity == null || entity.getId() == null || entity.getId().isBlank()) {
            return;
        }

        removeByIdWithoutPersist(entity.getId());

        if (entity.getDepartment() != null) {
            entity.setFaculty(entity.getDepartment().getFaculty());
            entity.getDepartment().getTeachers().add(entity);
        }

        FileStorageUtils.saveAll(university, UserService.getInstance());
    }

    @Override
    public void delete(String id) {
        if (id == null || id.isBlank()) {
            return;
        }

        if (removeByIdWithoutPersist(id)) {
            FileStorageUtils.saveAll(university, UserService.getInstance());
        }
    }

    private boolean removeByIdWithoutPersist(String id) {
        boolean removed = false;

        for (Faculty faculty : university.getFaculties()) {
            if (sameTeacher(faculty.getDean(), id)) {
                faculty.setDean(null);
                removed = true;
            }

            for (Department department : faculty.getDepartments()) {
                if (sameTeacher(department.getHead(), id)) {
                    department.setHead(null);
                    removed = true;
                }

                if (department.getTeachers().removeIf(t -> sameTeacher(t, id))) {
                    removed = true;
                }
            }
        }

        return removed;
    }

    private boolean sameTeacher(Teacher teacher, String id) {
        return teacher != null && teacher.getId() != null && teacher.getId().equalsIgnoreCase(id);
    }
}

