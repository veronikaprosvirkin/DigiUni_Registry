package person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import university.University;
import faculty.Faculty;
import speciality.Speciality;
import speciality.Group;
import utils.FileStorageUtils;
import utils.IdGenerator;
import utils.validation.EntityValidator;

public class StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private University university;

    public StudentService(University university) {
        this.university = university;
    }

    public void addStudent(String name, String surname,String patronymic, LocalDate enrollmentDate, int groupNumber, StudyForm studyForm) {
        if (!university.getFaculties().isEmpty() &&
                !university.getFaculties().get(0).getSpeciality().isEmpty()) {

            Faculty defaultFaculty = university.getFaculties().get(0);
            Speciality defaultSpec = defaultFaculty.getSpeciality().get(0);
            Group targetGroup = null;

            for (Group g : defaultSpec.getGroups()) {
                if (g.getGroupNumber() == groupNumber) {
                    targetGroup = g;
                    break;
                }
            }

            if (targetGroup == null) {
                targetGroup = new Group(groupNumber);
                defaultSpec.getGroups().add(targetGroup);
            }
            Student newStudent;
            try {
                newStudent = new Student(IdGenerator.generateStudentId(enrollmentDate.getYear()), name, surname, patronymic, enrollmentDate, groupNumber,
                        defaultFaculty,
                        defaultSpec, studyForm);
                EntityValidator.validate(newStudent);
            } catch (IllegalArgumentException e) {
                log.error("Student validation failed: {}", e.getMessage());
                System.out.println(e.getMessage());
                return;
            }

            targetGroup.getStudents().add(newStudent);
            FileStorageUtils.saveAll(university, user.UserService.getInstance());
            log.info("Student {} added to group {}", newStudent.getId(), groupNumber);
            System.out.println("Student added to group " + groupNumber);

        } else {
            log.error("Failed to add student: no default faculty/speciality available");
            System.out.println("Error: No department found to add student!");
        }

    }

    public void addStudentToSpeciality(Student student, Speciality speciality, int groupNumber) {
        Group targetGroup = null;
        for (Group g : speciality.getGroups()) {
            if (g.getGroupNumber() == groupNumber) {
                targetGroup = g;
                break;
            }
        }

        if (targetGroup == null) {
            targetGroup = new Group(groupNumber);
            speciality.getGroups().add(targetGroup);
        }

        targetGroup.getStudents().add(student);
        FileStorageUtils.updateStudentRecord(student);
        FileStorageUtils.saveAll(university, user.UserService.getInstance());
        log.info("Student {} assigned to speciality {} group {}", student.getId(), speciality.getId(), groupNumber);
    }

    //method for moving student to another group
    public void moveStudentToGroup(Student student, int newGroupNumber) {
        if (student.getGroup() == newGroupNumber) {
            log.warn("Student {} already in group {}", student.getId(), newGroupNumber);
            System.out.println("Student is already in group " + newGroupNumber);
            return;
        }

        Speciality studentSpec = student.getSpeciality();

        if (studentSpec == null) {
            log.error("Cannot move student {}: speciality is null", student.getId());
            System.out.println("Error: Could not find speciality for student.");
            return;
        }
        Group oldGroupObj = null;
        for (Group g : studentSpec.getGroups()) {
            if (g.getGroupNumber() == student.getGroup()) {
                g.getStudents().remove(student);
                oldGroupObj = g;
                break;
            }
        }
        addStudentToSpeciality(student, studentSpec, newGroupNumber);
        student.setGroup(newGroupNumber);
        FileStorageUtils.saveAll(university, user.UserService.getInstance());

        log.info("Student {} moved from group {} to {}", student.getId(), (oldGroupObj != null ? oldGroupObj.getGroupNumber() : -1), newGroupNumber);
        System.out.println("Student moved from group " + (oldGroupObj != null ? oldGroupObj.getGroupNumber() : "?") +
                " to " + newGroupNumber);
    }

    public void moveStudentToSpeciality(Student student, Faculty newFaculty, Speciality newSpeciality, int newGroupNumber) {
        if (student == null || newFaculty == null || newSpeciality == null) {
            log.error("Invalid transfer request: student/faculty/speciality is null");
            System.out.println("Error: Student, faculty and speciality must be provided.");
            return;
        }
        if (newGroupNumber <= 0) {
            log.error("Invalid transfer request for student {}: group {} is not positive", student.getId(), newGroupNumber);
            System.out.println("Error: Group number must be greater than 0.");
            return;
        }
        if (!newFaculty.getSpeciality().contains(newSpeciality)) {
            log.error("Invalid transfer request for student {}: speciality {} not in faculty {}", student.getId(), newSpeciality.getId(), newFaculty.getId());
            System.out.println("Error: Selected speciality does not belong to selected faculty.");
            return;
        }

        Faculty oldFaculty = null;
        Speciality oldSpeciality = null;
        Group oldGroup = null;

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality speciality : faculty.getSpeciality()) {
                for (Group group : speciality.getGroups()) {
                    boolean found = group.getStudents().stream().anyMatch(s -> s.getId().equals(student.getId()));
                    if (found) {
                        oldFaculty = faculty;
                        oldSpeciality = speciality;
                        oldGroup = group;
                        group.getStudents().removeIf(s -> s.getId().equals(student.getId()));
                        break;
                    }
                }
                if (oldGroup != null) {
                    break;
                }
            }
            if (oldGroup != null) {
                break;
            }
        }

        Group targetGroup = null;
        for (Group group : newSpeciality.getGroups()) {
            if (group.getGroupNumber() == newGroupNumber) {
                targetGroup = group;
                break;
            }
        }
        if (targetGroup == null) {
            targetGroup = new Group(newGroupNumber);
            newSpeciality.getGroups().add(targetGroup);
        }

        targetGroup.getStudents().add(student);
        student.setFaculty(newFaculty);
        student.setSpeciality(newSpeciality);
        student.setGroup(newGroupNumber);

        FileStorageUtils.saveAll(university, user.UserService.getInstance());

        String from = (oldFaculty != null)
                ? oldFaculty.getName() + " / " + oldSpeciality.getName() + " / group " + oldGroup.getGroupNumber()
                : "unknown location";
        log.info("Student {} transferred from {} to {} / {} / group {}", student.getId(), from, newFaculty.getName(), newSpeciality.getName(), newGroupNumber);
        System.out.println("Student " + student.getFullName() + " transferred from " + from +
                " to " + newFaculty.getName() + " / " + newSpeciality.getName() + " / group " + newGroupNumber);
    }

    // delete student
    public void deleteStudent(Student student, Speciality speciality) {
        boolean removed = false;
        for (Group group : speciality.getGroups()) {
            if (group.getStudents().remove(student)) {
                removed = true;
                break;
            }
        }

        if (removed) {
            FileStorageUtils.saveAll(university, user.UserService.getInstance());
            log.info("Student {} deleted from speciality {}", student.getId(), speciality.getId());
            System.out.println("Student " + student.getFullName() + " deleted successfully.");
        } else {
            log.warn("Failed to delete student {} from speciality {}: not found", student.getId(), speciality.getId());
            System.out.println("Error: Student not found in any group of " + speciality.getName());
        }
    }


    //** ===== SEARCH ===== **/
    // search all students
    public List<Student> getAllStudents() {
        List<Student> allStudents = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                for (Group gro : spec.getGroups()) {
                    allStudents.addAll(gro.getStudents());
                }
            }
        }
        if (allStudents.isEmpty()) {
            log.info("No students found during getAllStudents");
            System.out.println("No students found!");
        }
        return allStudents;
    }

    // Search by name
    public List<Student> findStudentsByFullName(String namePart) {
        List<Student> result = new ArrayList<>();
        // Split by spaces
        String[] searchParts = namePart.toLowerCase().split("\\s+");

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                for (Group gro : spec.getGroups()) {
                    for (Student s : gro.getStudents()) {
                        String fullName = s.getFullName().toLowerCase();
                        boolean matchesAll = true;

                        // Check all parts
                        for (String part : searchParts) {
                            if (!fullName.contains(part)) {
                                matchesAll = false;
                                break;
                            }
                        }

                        // Add if all parts match
                        if (matchesAll) {
                            result.add(s);
                        }
                    }
                }
            }
        }
        if (result.isEmpty()) {
            log.info("No student found by full name query: {}", namePart);
            System.out.println("No student found by full name " + namePart);
        }

        return result;
    }

    // Search by surname
    public List<Student> findStudentsBySurname(String surname) {
        List<Student> result = new ArrayList<>();
        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                for (Group group : spec.getGroups()) {
                    for (Student s : group.getStudents()) {
                        if (s.getSurname().equalsIgnoreCase(surname)) {
                            result.add(s);
                        }
                    }
                }
            }
        }
        return result;
    }

    // Search by group
    public List<Student> findStudentsByGroup(int groupNumber) {
        List<Student> result = new ArrayList<>();
        for (Faculty f : university.getFaculties()) {
            for (Speciality s : f.getSpeciality()) {
                result.addAll(findStudentsInSpecialityByGroup(s, groupNumber));
            }
        }
        return result;
    }

    public List<Student> findStudentsInSpecialityByGroup(Speciality spec, int groupNumber) {
        List<Student> result = new ArrayList<>();
        for (Group g : spec.getGroups()) {
            if (g.getGroupNumber() == groupNumber) {
                result.addAll(g.getStudents());
            }
        }
        return result;
    }

    // Search by Course
    public List<Student> findStudentsByCourse(int course) {
        List<Student> result = new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                for (Group group : spec.getGroups()) {
                    for (Student s : group.getStudents()) {
                        if (s.getCourse() == course) {
                            result.add(s);}
                    }
                }
            }
        }
        if (result.isEmpty()) {
            log.info("No student found on course {}", course);
            System.out.println("No student found on course " + course);
        }
        return result;
    }

    public List<Student> findStudentsBySpeciality(Speciality selectedSpeciality) {
        List <Student> result =new ArrayList<>();

        for (Faculty faculty : university.getFaculties()) {
            for (Speciality spec : faculty.getSpeciality()) {
                if (spec.equals(selectedSpeciality)) {
                    for (Group group : spec.getGroups()) {
                        result.addAll(group.getStudents());
                    }
                }
            }
        }
        return result;
    }

    // Find students by ID
    public List<Student> findStudentById(String id) {
        List<Student> result = new ArrayList<>();
        for(Student s: getAllStudents()){
            if (s.getId().equalsIgnoreCase(id)){
                result.add(s);
                break;
            }
        }
        if (result.isEmpty()){
            log.info("No student found by id {}", id);
            System.out.println("No student found by id " + id);
        }
        return result;
    }
}
