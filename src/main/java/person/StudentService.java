package person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.StudentRepository;
import university.University;
import faculty.Faculty;
import speciality.Speciality;
import speciality.Group;
import utils.IdGenerator;
import utils.validation.EntityValidator;

public class StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final University university;
    private final StudentRepository studentRepository;

    public StudentService(University university) {
        this.university = university;
        this.studentRepository = new StudentRepository(university);
    }

    public void addStudent(String name, String surname,String patronymic, LocalDate enrollmentDate, int groupNumber, StudyForm studyForm) {
        if (!university.getFaculties().isEmpty() &&
                !university.getFaculties().get(0).getSpeciality().isEmpty()) {

            Faculty defaultFaculty = university.getFaculties().get(0);
            Speciality defaultSpec = defaultFaculty.getSpeciality().get(0);
            Student newStudent;
            try {
                newStudent = new Student(IdGenerator.generateStudentId(university,enrollmentDate.getYear()), name, surname, patronymic, enrollmentDate, groupNumber,
                        defaultFaculty,
                        defaultSpec, studyForm);
                EntityValidator.validate(newStudent);
            } catch (IllegalArgumentException e) {
                log.error("Student validation failed: {}", e.getMessage());
                System.out.println(e.getMessage());
                return;
            }

            studentRepository.save(newStudent);
            log.info("Student {} added to group {}", newStudent.getId(), groupNumber);
            System.out.println("Student added to group " + groupNumber);

        } else {
            log.error("Failed to add student: no default faculty/speciality available");
            System.out.println("Error: No department found to add student!");
        }
    }

    public void addStudentToSpeciality(Student student, Speciality speciality, int groupNumber) {
        Objects.requireNonNull(student, "Student cannot be null");
        Objects.requireNonNull(speciality, "Speciality cannot be null");

        student.setSpeciality(speciality);
        student.setGroup(groupNumber);
        Faculty ownerFaculty = findFacultyBySpeciality(speciality);
        if (ownerFaculty != null) {
            student.setFaculty(ownerFaculty);
        }
        studentRepository.save(student);
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
        int oldGroupNumber = student.getGroup();
        student.setGroup(newGroupNumber);
        studentRepository.save(student);

        log.info("Student {} moved from group {} to {}", student.getId(), oldGroupNumber, newGroupNumber);
        System.out.println("Student moved from group " + oldGroupNumber +
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

        Faculty oldFaculty = student.getFaculty();
        Speciality oldSpeciality = student.getSpeciality();
        int oldGroupNumber = student.getGroup();

        student.setFaculty(newFaculty);
        student.setSpeciality(newSpeciality);
        student.setGroup(newGroupNumber);
        studentRepository.save(student);

        String from = (oldFaculty != null && oldSpeciality != null)
                ? oldFaculty.getName() + " / " + oldSpeciality.getName() + " / group " + oldGroupNumber
                : "unknown location";
        log.info("Student {} transferred from {} to {} / {} / group {}", student.getId(), from, newFaculty.getName(), newSpeciality.getName(), newGroupNumber);
        System.out.println("Student " + student.getFullName() + " transferred from " + from +
                " to " + newFaculty.getName() + " / " + newSpeciality.getName() + " / group " + newGroupNumber);
    }

    // delete student
    public void deleteStudent(Student student, Speciality speciality) {
        Objects.requireNonNull(student, "Student cannot be null");
        @SuppressWarnings("unused") Speciality ignored = speciality;

        boolean removed = studentRepository.findById(student.getId()) != null;
        if (removed) {
            studentRepository.delete(student.getId());
        }

        if (removed) {
            log.info("Student {} deleted", student.getId());
            System.out.println("Student " + student.getFullName() + " deleted successfully.");
        } else {
            log.warn("Failed to delete student {}: not found", student.getId());
            System.out.println("Error: Student not found.");
        }
    }


    //** ===== SEARCH ===== **/
    // search all students
    public List<Student> getAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
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
        Student student = studentRepository.findById(id);
        if (student != null) {
            result.add(student);
        }
        if (result.isEmpty()){
            log.info("No student found by id {}", id);
            System.out.println("No student found by id " + id);
        }
        return result;
    }

    private Faculty findFacultyBySpeciality(Speciality speciality) {
        for (Faculty faculty : university.getFaculties()) {
            if (faculty.getSpeciality().contains(speciality)) {
                return faculty;
            }
        }
        return null;
    }
}
