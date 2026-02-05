import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UniversityService service = new UniversityService();
        Scanner scanner = new Scanner(System.in);

        // Creating few students
        service.addStudent("Zbyshek", "Tymekowskych", 1, 101);
        service.addStudent("Irzek", "Zlotych", 1, 101);
        service.addStudent("Irzek", "Tymekowskych", 2, 15);

        while (true) {
            System.out.println("\n--- DigiUni (Hierarchical) ---");
            System.out.println("1. Add Student");
            System.out.println("2. Find by Full Name");
            System.out.println("3. Find by Group");
            System.out.println("4. Find by Course");
            System.out.println("5. Show All Students");
            System.out.println("6. Add Teacher ");
            System.out.println("0. Exit");
            System.out.print("> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    // Select faculty
                    Faculty selectedFaculty = selectFaculty(scanner, service);
                    if (selectedFaculty == null) break;

                    // Select speciality
                    Speciality selectedSpeciality = selectDepartment(scanner, selectedFaculty);
                    if (selectedSpeciality == null) break;

                    // Student's info
                    String name = readLine(scanner, "Name: ", true);
                    String surname = readLine(scanner, "Surname: ", true);
                    int course = readInt(scanner, "Enter Course (1-6): ", 1, 6);
                    int group = readInt(scanner, "Enter Group: ", 1, Integer.MAX_VALUE);

                    // Save
                    Student s = new Student(name, surname, course, group,
                            selectedFaculty.getName(),
                            selectedSpeciality.getName());
                    service.addStudentToDepartment(s, selectedSpeciality);
                    System.out.println("Student added to " + selectedSpeciality.getName());
                }
                case "2" -> {   //? Search by full name
                    System.out.println("1. Find Student");
                    System.out.println("2. Find Teacher");
                    int type = readInt(scanner, "> ", 1, 2);

                    System.out.print("Enter full name part: ");
                    String q = scanner.nextLine();

                    if (type==1) {             // Search among students
                        service.findStudentsByFullName(q).forEach(System.out::println);
                    }
                    else if (type==2) {      // Search among teachers
                        service.findTeachersByFullName(q).forEach(System.out::println);
                    }
                }
                case "3" -> {   //? Search by group
                    System.out.print("Group: ");
                    int g = readInt(scanner, "Enter Course: ", 1, Integer.MAX_VALUE);
                    service.findStudentsByGroup(g).forEach(System.out::println);
                }
                case "4" -> {   //? Search by course
                    System.out.print("Course: ");
                    int c = readInt(scanner, "Enter Course: ", 1, 6);
                    service.findStudentsByCourse(c).forEach(System.out::println);
                }
                case "5" -> service.getAllStudents().forEach(System.out::println);   //? Print all students
                case "6" -> {   //? Create a teacher
                    String name = readLine(scanner, "Teacher name: ", true);
                    String surname = readLine(scanner, "Teacher Surname: ", true);
                    String pos = readLine(scanner, "Position: ", true);
                    service.addTeacher(name,surname, pos);
                    System.out.println("Teacher added!");
                }
                case "0" -> { return; }     //? Stop the program
                default -> System.out.println("Invalid.");  //? Incorrect input
            }
        }
    }




    // * ===== METHODS HELPERS ===== * //

    /**
     * ? Method that checks if the line is an Integer between min and max values
     * @param scanner
     * @param prompt
     * @param min
     * @param max
     * @return int
     */
    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int intInput = Integer.parseInt(input);
                if (intInput >= min && intInput <= max) {
                    return intInput;
                }
                // Describe error
                if (min == Integer.MIN_VALUE && max != Integer.MAX_VALUE) {
                    System.out.println("Error: Number must be less than or equal to " + max + "!");
                } else if (min != Integer.MIN_VALUE && max == Integer.MAX_VALUE) {
                    System.out.println("Error: Number must be more than or equal to " + min + "!");
                } else {
                    System.out.println("Error: Number must be between " + min + " and " + max + "!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

    /**
     * ? Method that checks if the line is not blank
     * @param scanner
     * @param prompt
     * @return
     */
    private static String readLine(Scanner scanner, String prompt, boolean mustBeNotEmpty) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        if (mustBeNotEmpty) {
            while (line.isBlank()) {
                System.out.println("Field cannot be empty!");
                System.out.print(prompt);
                line = scanner.nextLine();
            }
        }
        return line;
    }

    /**
     * ? Faculty selection
     * @param scanner
     * @param service
     * @return
     */
    private static Faculty selectFaculty(Scanner scanner, UniversityService service) {
        List<Faculty> faculties = service.getFaculties();
        if (faculties.isEmpty()) {
            System.out.println("No faculties available!");
            return null;
        }
        System.out.println("--- Choose Faculty ---");
        for (int i = 0; i < faculties.size(); i++) {
            System.out.println((i + 1) + ". " + faculties.get(i).getName());
        }
        int index = readInt(scanner, "Enter Faculty number: ", 1, faculties.size()) - 1;
        return faculties.get(index);
    }

    /**
     * ? Department selection
     * @param scanner
     * @param faculty
     * @return
     */
    private static Speciality selectDepartment(Scanner scanner, Faculty faculty) {
        List<Speciality> specialities = faculty.getSpeciality();
        if (specialities.isEmpty()) {
            System.out.println("No departments in this faculty!");
            return null;
        }
        System.out.println("--- Choose Speciality ---");
        for (int i = 0; i < specialities.size(); i++) {
            System.out.println((i + 1) + ". " + specialities.get(i).getName());
        }
        int index = readInt(scanner, "Enter Department number: ", 1, specialities.size()) - 1;
        return specialities.get(index);
    }
}