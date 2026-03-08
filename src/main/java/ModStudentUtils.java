import java.util.List;
import java.util.Scanner;

public class ModStudentUtils {
    //! ======= WORK WITH STUDENTS ===== //

    /**
     * Add new Student
     */
    static void studentAddStudent(Scanner scanner, FacultyService facultyService, StudentService studentService) {
        System.out.println("--- Add Student ---");
        Faculty selectedFaculty = ModEntitiesUtils.selectEntity(scanner, facultyService.getFaculties(), "Faculties");
        if (selectedFaculty == null) return;

        // Select speciality
        Speciality selectedSpeciality = ModEntitiesUtils.selectSpeciality(scanner, selectedFaculty);
        if (selectedSpeciality == null) return;


        // Student's info
        String name = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Name: ", false, false), true, false, false, false);
        String surname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Surname: ", false, false), true, false, false, false);
        int course = InputUtils.readInt(scanner, "Enter Course (1-6): ", 1, 6);
        int groupNumber = InputUtils.readInt(scanner, "Enter Group: ", 1, Integer.MAX_VALUE);


        // Save
        Student s = new Student(name, surname, course, groupNumber,
                selectedFaculty.getName(),
                selectedSpeciality);
        studentService.addStudentToSpeciality(s, selectedSpeciality, groupNumber);

        System.out.println("Student " + s.getFullName() + " added to group " + groupNumber +
                " in " + selectedSpeciality.getName());
    }

    /**
     * Delete the Student by name
     */
    private static void studentDeleteByName(Scanner scanner, StudentService studentService) {
        String fullName = InputUtils.readLine(scanner, "Full name of student: ", false, false);
        fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
        List<Student> result = studentService.findStudentsByFullName(fullName);

        if (result.isEmpty()) {
            System.out.println("No students found with this name.");
        } else {
            Student studentToProcess;
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select one: ");
                for (int i = 0; i < result.size(); i++) {
                    System.out.println((i + 1) + ". " + result.get(i).getFullName() +
                            " (Group: " + result.get(i).getGroup() + ", Course: " + result.get(i).getCourse() + ")");
                }
                System.out.println("0. Cancel");
                int index = InputUtils.readInt(scanner, "> ", 0, result.size());
                if (index == 0) {
                    return;
                }

                studentToProcess = result.get(index - 1);
            } else {
                studentToProcess = result.get(0);
            }
            System.out.print("Are you sure you want ot delete " + studentToProcess.getFullName() + "? (y/n): ");
            if (scanner.nextLine().toLowerCase().startsWith("y")) {
                studentService.deleteStudent(studentToProcess, studentToProcess.getSpeciality());
            } else {
                System.out.println("Operation cancelled.");
            }
        }
        InputUtils.pause(scanner);
    }

    /**
     * Delete the Student by ID
     */
    static void studentDeleteById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }

    /**
     * Edit the Student by name
     */
    static void studentEditByName(Scanner scanner, StudentService studentService) {
        String fullName = InputUtils.readLine(scanner, "Enter full name part: ", false, false);
        fullName = InputUtils.removeSpaces(fullName, false, true, true, true);
        List<Student> result = studentService.findStudentsByFullName(fullName);

        if (result.isEmpty()) {
            System.out.println("No students found with this name.");
        } else {
            Student studentToProcess;
            if (result.size() > 1) {
                System.out.println("Multiple students found. Please select one: ");
                for (int i = 0; i < result.size(); i++) {
                    System.out.println((i + 1) + ". " + result.get(i).getFullName() +
                            " (Group: " + result.get(i).getGroup() + ", Course: " + result.get(i).getCourse() + ")");
                }
                int index = InputUtils.readInt(scanner, "> ", 1, result.size());

                studentToProcess = result.get(index - 1);

            } else {
                studentToProcess = result.get(0);
            }

            System.out.println("\nEditing student: " + studentToProcess.getFullName());
            System.out.println("1. Change Surname");
            System.out.println("2. Change Name");
            System.out.println("3. Change Course");
            System.out.println("4. Change Group");
            System.out.println("0. Cancel");

            int fieldChoice = InputUtils.readInt(scanner, "> ", 0, 4);

            switch (fieldChoice) {
                case 1 -> {
                    String newSurname = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new surname: ", false, false), true, false, false, false);
                    studentToProcess.setSurname(newSurname);
                    System.out.println("Surname updated!");
                }
                case 2 -> {
                    String newName = InputUtils.removeSpaces(InputUtils.readLine(scanner, "Enter new name: ", false, false), true, false, false, false);
                    studentToProcess.setName(newName);
                    System.out.println("Name updated!");
                }
                case 3 -> {
                    int newCourse = InputUtils.readInt(scanner, "Enter new course (1-6): ", 1, 6);
                    studentToProcess.setCourse(newCourse);
                    System.out.println("Course updated!");
                }
                case 4 -> {
                    int newGroup = InputUtils.readInt(scanner, "Enter new group number: ", 1, Integer.MAX_VALUE);
                    studentService.moveStudentToGroup(studentToProcess, newGroup);
                }
            }
        }
    }

    /**
     * Edit the Student by ID
     */
    static void studentEditById(Scanner scanner, UniversityService universityService) {
        // NOT FINISHED METHOD
    }
}
