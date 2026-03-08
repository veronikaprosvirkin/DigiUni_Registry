import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private University university;

    public StudentService(University university) {
        this.university = university;
    }

    public void addStudent(String name, String surname, int course, int groupNumber) {
        if (groupNumber <= 0)
            throw new IllegalArgumentException("Group number must be greater than 0.");
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


            Student newStudent = new Student(name, surname, course, groupNumber,
                    defaultFaculty.getName(),
                    defaultSpec);

            targetGroup.getStudents().add(newStudent);
            System.out.println("Student added to group " + groupNumber);

        } else {
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
    }

    //method for moving student to another group
    public void moveStudentToGroup(Student student, int newGroupNumber) {
        if (student.getGroup() == newGroupNumber) {
            System.out.println("Student is already in group " + newGroupNumber);
            return;
        }

        Speciality studentSpec = null;

        for (Faculty f : university.getFaculties()) {
            if (f.getName().equals(student.getFaculty())) {
                for (Speciality s : f.getSpeciality()) {
                    if (s.getName().equals(student.getSpeciality().getName())) {
                        studentSpec = s;
                        break;
                    }
                }
            }
        }

        if (studentSpec == null) {
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

        System.out.println("Student moved from group " + (oldGroupObj != null ? oldGroupObj.getGroupNumber() : "?") +
                " to " + newGroupNumber);
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
            System.out.println("Student " + student.getFullName() + " deleted successfully.");
        } else {
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
}
