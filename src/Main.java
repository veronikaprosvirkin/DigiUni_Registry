import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UniversityService service = new UniversityService();
        Scanner scanner = new Scanner(System.in);

        // Creating few students
        service.addStudent("Zbyshek Tymekowskych", 1, 101);
        service.addStudent("Irzek Zlotych", 1, 101);

        while (true) {
            System.out.println("\n--- DigiUni (Hierarchical) ---");
            System.out.println("1. Add Student");
            System.out.println("2. Find by Name");
            System.out.println("3. Find by Group");
            System.out.println("4. Find by Course");
            System.out.println("5. Show All Students");
            System.out.println("6. Add Teacher ");
            System.out.println("0. Exit");
            System.out.print("> ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {   //? Creating a student
                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    // Ask again if blank
                    while (name.isBlank()) {
                        System.out.print("Name cannot be empty. Enter Name: ");
                        name = scanner.nextLine();
                    }

                    int course = readInt(scanner, "Enter Course: ");
                    int group = readInt(scanner, "Enter Group: ");
                }
                case "2" -> {   //? Search by name
                    System.out.println("1. Find Student\n2. Find Teacher");
                    String type = scanner.nextLine();

                    System.out.print("Enter name part: ");
                    String q = scanner.nextLine();

                    if (type.equals("1")) {             // Search among students
                        service.findStudentsByName(q).forEach(System.out::println);
                    }
                    else if (type.equals("2")) {      // Search among teachers
                        service.findTeachersByName(q).forEach(System.out::println);
                    }
                }
                case "3" -> {   //? Search by group
                    System.out.print("Group: ");
                    int g = readInt(scanner, "Enter Course: ");
                    service.findStudentsByGroup(g).forEach(System.out::println);
                }
                case "4" -> {   //? Search by course
                    System.out.print("Course: ");
                    int c = readInt(scanner, "Enter Course: ");
                    service.findStudentsByCourse(c).forEach(System.out::println);
                }
                case "5" -> service.getAllStudents().forEach(System.out::println);   //? Print all students
                case "6" -> {   //? Create a teacher
                    System.out.print("Teacher Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Position: ");
                    String pos = scanner.nextLine();
                    service.addTeacher(name, pos);
                    System.out.println("Teacher added!");
                }
                case "0" -> { return; }     //? Stop the program
                default -> System.out.println("Invalid.");  //? Incorrect input
            }
        }
    }




    // Helper method to loop until user enters a valid number
    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }
}