package repository;

import faculty.Faculty;
import person.Student;
import speciality.Group;
import speciality.Speciality;
import university.University;
import user.UserService;
import utils.FileStorageUtils;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements Repository<Student, String> {
    private final University university;

    public StudentRepository(University university) {
        this.university = university;
    }

    @Override
    public Student findById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality speciality : faculty.getSpeciality()) {
                for (Group group : speciality.getGroups()) {
                    for (Student student : group.getStudents()) {
                        if (id.equalsIgnoreCase(student.getId())) {
                            return student;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<Student> findAll() {
        List<Student> allStudents = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality speciality : faculty.getSpeciality()) {
                for (Group group : speciality.getGroups()) {
                    allStudents.addAll(group.getStudents());
                }
            }
        }

        return allStudents;
    }

    @Override
    public void save(Student entity) {
        if (entity == null || entity.getId() == null || entity.getId().isBlank()) {
            return;
        }

        removeByIdWithoutPersist(entity.getId());

        Speciality targetSpeciality = entity.getSpeciality();
        if (targetSpeciality == null) {
            throw new IllegalArgumentException("Student speciality must not be null.");
        }

        Group targetGroup = findOrCreateGroup(targetSpeciality, entity.getGroup());
        targetGroup.getStudents().add(entity);

        FileStorageUtils.updateStudentRecord(entity);
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

    private Group findOrCreateGroup(Speciality speciality, int groupNumber) {
        for (Group group : speciality.getGroups()) {
            if (group.getGroupNumber() == groupNumber) {
                return group;
            }
        }

        Group newGroup = new Group(groupNumber);
        speciality.getGroups().add(newGroup);
        return newGroup;
    }

    private boolean removeByIdWithoutPersist(String id) {
        for (Faculty faculty : university.getFaculties()) {
            for (Speciality speciality : faculty.getSpeciality()) {
                for (Group group : speciality.getGroups()) {
                    if (group.getStudents().removeIf(student -> id.equalsIgnoreCase(student.getId()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
