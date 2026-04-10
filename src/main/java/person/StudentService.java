package person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import university.University;
import faculty.Faculty;
import repository.StudentRepository;
import speciality.Speciality;
import speciality.Group;
import utils.IdGenerator;

public class StudentService {
    private final University university;
    private final StudentRepository studentRepository;

    public StudentService(University university) {
        this.university = university;
        this.studentRepository = new StudentRepository(university);
    }

    public void addStudent(String name, String surname,String patronymic, LocalDate enrollmentDate, int groupNumber, StudyForm studyForm) {
        if (groupNumber <= 0)
            throw new IllegalArgumentException("Group number must be greater than 0.");
        if (!university.getFaculties().isEmpty() &&
                !university.getFaculties().get(0).getSpeciality().isEmpty()) {

            Faculty defaultFaculty = university.getFaculties().get(0);
            Speciality defaultSpec = defaultFaculty.getSpeciality().get(0);
            Student newStudent = new Student(IdGenerator.generateStudentId(enrollmentDate.getYear()),name, surname, patronymic, enrollmentDate, groupNumber,
                    defaultFaculty,
                    defaultSpec, studyForm);

            studentRepository.save(newStudent);
            System.out.println("Student added to group " + groupNumber);

        } else {
            System.out.println("Error: No department found to add student!");
        }

    }

    public void addStudentToSpeciality(Student student, Speciality speciality, int groupNumber) {
        student.setSpeciality(speciality);
        student.setGroup(groupNumber);
        studentRepository.save(student);
    }

    //method for moving student to another group
    public void moveStudentToGroup(Student student, int newGroupNumber) {
        if (student.getGroup() == newGroupNumber) {
            System.out.println("Student is already in group " + newGroupNumber);
            return;
        }

        Speciality studentSpec = student.getSpeciality();

        if (studentSpec == null) {
            System.out.println("Error: Could not find speciality for student.");
            return;
        }
        int oldGroupNumber = student.getGroup();
        for (Group g : studentSpec.getGroups()) {
            g.getStudents().removeIf(s -> s.getId().equals(student.getId()));
        }
        student.setGroup(newGroupNumber);
        studentRepository.save(student);

        System.out.println("Student moved from group " + oldGroupNumber +
                " to " + newGroupNumber);
    }

    public void moveStudentToSpeciality(Student student, Faculty newFaculty, Speciality newSpeciality, int newGroupNumber) {
        if (student == null || newFaculty == null || newSpeciality == null) {
            System.out.println("Error: Student, faculty and speciality must be provided.");
            return;
        }
        if (newGroupNumber <= 0) {
            System.out.println("Error: Group number must be greater than 0.");
            return;
        }
        if (!newFaculty.getSpeciality().contains(newSpeciality)) {
            System.out.println("Error: Selected speciality does not belong to selected faculty.");
            return;
        }

        Student existingStudent = studentRepository.findById(student.getId());
        Faculty oldFaculty = existingStudent != null ? existingStudent.getFaculty() : null;
        Speciality oldSpeciality = existingStudent != null ? existingStudent.getSpeciality() : null;
        int oldGroupNumber = existingStudent != null ? existingStudent.getGroup() : -1;

        studentRepository.delete(student.getId());

        student.setFaculty(newFaculty);
        student.setSpeciality(newSpeciality);
        student.setGroup(newGroupNumber);
        studentRepository.save(student);

        String from = (oldFaculty != null)
                ? oldFaculty.getName() + " / " + oldSpeciality.getName() + " / group " + oldGroupNumber
                : "unknown location";
        System.out.println("Student " + student.getFullName() + " transferred from " + from +
                " to " + newFaculty.getName() + " / " + newSpeciality.getName() + " / group " + newGroupNumber);
    }

    // delete student
    public void deleteStudent(Student student, Speciality speciality) {
        Student existingStudent = studentRepository.findById(student.getId());
        if (existingStudent != null) {
            studentRepository.delete(student.getId());
            System.out.println("Student " + student.getFullName() + " deleted successfully.");
        } else {
            System.out.println("Error: Student not found in any group of " + speciality.getName());
        }
    }


    //** ===== SEARCH ===== **/
    // search all students
    public List<Student> getAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) {
            System.out.println("No students found!");
        }
        return allStudents;
    }

    // Search by name
    public List<Student> findStudentsByFullName(String namePart) {
        List<Student> result = new ArrayList<>();
        // Split by spaces
        String[] searchParts = namePart.toLowerCase().split("\\s+");

        for (Student s : studentRepository.findAll()) {
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
        if (result.isEmpty()) {
            System.out.println("No student found by full name " + namePart);
        }

        return result;
    }

    // Search by surname
    public List<Student> findStudentsBySurname(String surname) {
        List<Student> result = new ArrayList<>();
        for (Student s : studentRepository.findAll()) {
            if (s.getSurname().equalsIgnoreCase(surname)) {
                result.add(s);
            }
        }
        return result;
    }

    // Search by group
    public List<Student> findStudentsByGroup(int groupNumber) {
        List<Student> result = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            if (student.getGroup() == groupNumber) {
                result.add(student);
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

        for (Student s : studentRepository.findAll()) {
            if (s.getCourse() == course) {
                result.add(s);
            }
        }
        if (result.isEmpty()) {
            System.out.println("No student found on course " + course);
        }
        return result;
    }

    public List<Student> findStudentsBySpeciality(Speciality selectedSpeciality) {
        List <Student> result =new ArrayList<>();

        for (Student student : studentRepository.findAll()) {
            if (selectedSpeciality.equals(student.getSpeciality())) {
                result.add(student);
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
            System.out.println("No student found by id " + id);
        }
        return result;
    }
}
