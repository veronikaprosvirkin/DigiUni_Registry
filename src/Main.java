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
            System.out.println("1. Work with Faculties"); //logic written, not finished realization
            System.out.println("2. Work with Departments"); //logic written, not finished realization
            System.out.println("3. Work with Specialities"); //logic written, not finished realization
            System.out.println("4. Work with Students"); //logic written, not finished realization
            System.out.println("5. Work with Teachers");
            System.out.println("6. Search");
            System.out.println("0. Exit");
            System.out.print("> ");

            /* System.out.println("1. Add Student");

            System.out.println("2. Find by Full Name");

            System.out.println("3. Find by Group");

            System.out.println("4. Find by Course");

            System.out.println("5. Show All Students");

            System.out.println("6. Add Teacher "); */

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    // Work with faculties
                    Faculty selectedFaculty = selectFaculty(scanner, service);
                    if (selectedFaculty == null) break;

                    System.out.println("1. Add Faculty");
                    System.out.println("2. Delete Faculty");
                    System.out.println("3. Edit Faculty");
                    System.out.println("4. Show all");
                    System.out.println("0. Back");
                    int workWithFaculty = readInt(scanner, "Enter number of operation: ", 0, 4);
                    if (workWithFaculty ==1){ //add faculty
                        String name = readLine(scanner,"Enter new Faculty name: ", true);
                        service.addNewFaculty(name);
                    } else if (workWithFaculty == 2) { //delete faculty
                        System.out.println("Choose faculty to delete:");
                        Faculty selectedFacultyToDelete = selectFaculty(scanner, service);
                        System.out.println("Are you sure you want to delete " + selectedFacultyToDelete.getName() + "? (y/n)");
                        if (scanner.nextLine().toLowerCase().startsWith("y")) {
                            service.deleteFaculty(selectedFacultyToDelete);
                            System.out.println("Faculty deleted successfully!");
                        }
                        else {
                            System.out.println("Operation cancelled.");
                        }

                    } else if (workWithFaculty == 3) {
                        //edit faculty
                    }
                    else {
                        break;
                    }
                }


                case "2" -> {// Work with departments
                    System.out.println("1. Add Department");
                    System.out.println("2. Manage Existing Department");
                    int action = readInt(scanner, "Enter number of operation: ", 1, 2);

                    if (action == 1) { // add a new department
                        System.out.println("Choose faculty in which department will be added:");
                        Faculty selectedFaculty = selectFaculty(scanner, service);
                        if (selectedFaculty != null) {
                            String name = readLine(scanner, "Enter new Department name: ", true);
                            service.addNewDepartment(name, selectedFaculty);
                            System.out.println("Department created successfully!");
                        }
                        else {
                            System.out.println("No faculties found. Please add a new one first.");
                        }

                    } else if (action ==2) { // manage existing department

                    System.out.println("Choose faculty:");
                    Faculty selectedFaculty = selectFaculty(scanner, service);
                    if (selectedFaculty == null) break;

                    System.out.println("Choose department:");
                    Department selectedDept = selectDepartment(scanner, selectedFaculty);
                    if (selectedDept == null) break;

                    //Work with a selected department
                    System.out.println("\nDepartment: " + selectedDept.getName());
                    System.out.println("1. Edit name of the Department");
                    System.out.println("2. Delete Department");
                    System.out.println("3. Show all Teachers in the Department");
                    System.out.println("0. Back");

                    int workWithDepartment = readInt(scanner, "Select action: ", 0, 3);

                    if (workWithDepartment ==1){ // edit department name
                        String editName = readLine(scanner,"Write new name for chosen department", true);
                       service.editDepartmentName(selectedDept, editName, selectedFaculty);

                    } else if (workWithDepartment == 2) { //delete department
                        System.out.println("Are you sure you want ot delete " + selectedDept.getName() + "? (y/n)");
                        if (scanner.nextLine().toLowerCase().startsWith("y")) {
                            service.deleteDepartment(selectedDept, selectedFaculty);
                            System.out.println("Faculty deleted successfully!");
                        }
                        else {
                            System.out.println("Operation cancelled.");
                        }

                    } else if (workWithDepartment == 3) {
                        //Show all teachers in the department
                    }
                    else {
                        break;
                    }
                    }
                }
                case "3" -> {   //? Edit speciality
                    Faculty selectedFaculty = selectFaculty(scanner, service);
                    if (selectedFaculty == null) break;

                    // Select speciality
                    Speciality selectedSpeciality = selectSpeciality(scanner, selectedFaculty);
                    if (selectedSpeciality == null) break;

                    System.out.println("1. Add Speciality");
                    System.out.println("2. Edit Speciality");
                    System.out.println("3. Delete Speciality");
                    System.out.println("0. Back");

                    int workWithSpeciality = readInt(scanner, "Enter number of operation: ", 0, 4);
                    if (workWithSpeciality ==1){
                        //add faculty
                    } else if (workWithSpeciality == 2) {
                        //delete faculty
                    } else if (workWithSpeciality == 3) {
                        //edit faculty
                    }
                    else {
                        break;
                    }



                    /*
                    System.out.println("1. Find in specific speciality");
                    System.out.println("2. Find in all university");

                    int type = readInt(scanner, "> ", 1, 2);

                    if (type == 1) {    // Search in specific speciality

                        Faculty selectedFaculty = selectFaculty(scanner, service);
                        if (selectedFaculty == null) break;

                        Speciality selectedSpeciality = selectDepartments(scanner, selectedFaculty);
                        if (selectedSpeciality == null) break;

                        int groupNumber = readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

                        List<Student> results = service.findStudentsInSpecialityByGroup(selectedSpeciality, groupNumber);

                        if (results.isEmpty()) {
                            System.out.println("No students found in group " + groupNumber + " within " + selectedSpeciality.getName());
                        } else {
                            results.forEach(System.out::println);
                        }
                    }
                    else {      // Search in all university
                        int groupNumber = readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

                        List<Student> results = service.findStudentsByGroup(groupNumber);

                        if (results.isEmpty()) {
                            System.out.println("No students found in group " + groupNumber + " in the whole university.");
                        } else {
                            results.forEach(System.out::println);
                        }
                    }*/
                }
                case "4" -> {// Work with students
                    System.out.println("1. Add Student");
                    System.out.println("2. Delete Student");
                    System.out.println("3. Edit information about student");
                    System.out.println("4. Show all");
                    System.out.println("0. Back");

                    int workWithStudent = readInt(scanner, "Enter number of operation: ", 0, 4);

                    if (workWithStudent == 1){ //add student
                        System.out.println("--- Add Student ---");
                        Faculty selectedFaculty = selectFaculty(scanner, service);
                        if (selectedFaculty == null) break;

                        // Select speciality
                        Speciality selectedSpeciality = selectSpeciality(scanner, selectedFaculty);
                        if (selectedSpeciality == null) break;


                        // Student's info
                        String name = readLine(scanner, "Name: ", true);
                        String surname = readLine(scanner, "Surname: ", true);
                        int course = readInt(scanner, "Enter Course (1-6): ", 1, 6);
                        int groupNumber = readInt(scanner, "Enter Group: ", 1, Integer.MAX_VALUE);


                        // Save
                        Student s = new Student(name, surname, course, groupNumber,
                                selectedFaculty.getName(),
                                selectedSpeciality.getName());
                        service.addStudentToSpeciality(s, selectedSpeciality, groupNumber);

                        System.out.println("Student " + name + " added to group " + groupNumber +
                                " in " + selectedSpeciality.getName());
                    }
                    else if (workWithStudent == 2){ //delete student

                        System.out.println("--- Delete Student ---");
                        System.out.print("1. Delete by full name: ");
                        System.out.println("2. Delete by ID: ");
                        System.out.print("0. Back: ");

                        int deleteStudent = readInt(scanner, "Select option: ", 0, 2);
                        if (deleteStudent == 1){

                            String name = readLine(scanner, "Name: ", true);
                            String surname = readLine(scanner, "Surname: ", true);
                            //not finished
                        }
                        else if (deleteStudent == 2){

                            //for id
                        }
                        else {
                            break;
                        }
                    } else if (workWithStudent == 3) { //edit student
                        System.out.print("1. Edit by full name: ");
                        System.out.println("2. Edit by ID: ");
                        System.out.print("0. Back: ");
                        int editStudent = readInt(scanner, "Select option: ", 0, 2);
                        if (editStudent == 1) {
                            // Search among students

                            System.out.print("Enter full name part: ");
                            String q = scanner.nextLine();
                            List<Student> result = service.findStudentsByFullName(q);

                            if (result.isEmpty()) {
                                System.out.println("No students found with this name.");
                            } else {
                                Student studentToProcess;
                                if (result.size() > 1) {
                                    System.out.println("Multiple students found. Please select one:");
                                    for (int i = 0; i < result.size(); i++) {
                                        System.out.println((i + 1) + ". " + result.get(i).getFullName() +
                                                " (Group: " + result.get(i).getGroup() + ")");
                                    }
                                    int index = readInt(scanner, "Enter student number: ", 1, result.size());

                                    Student student = result.get(index - 1);

                                    System.out.println("\nEditing student: " + student.getFullName());
                                    System.out.println("1. Change Surname");
                                    System.out.println("2. Change Course");
                                    System.out.println("3. Change Group");
                                    System.out.println("0. Cancel");

                                    int fieldChoice = readInt(scanner, "Select field to edit: ", 0, 3);

                                    switch (fieldChoice) {
                                        case 1 -> {
                                            String newSurname = readLine(scanner, "Enter new surname: ", true);
                                            student.setSurname(newSurname);
                                            System.out.println("Surname updated!");
                                        }
                                        case 2 -> {
                                            int newCourse = readInt(scanner, "Enter new course (1-6): ", 1, 6);
                                            student.setCourse(newCourse);
                                            System.out.println("Course updated!");
                                        }
                                        case 3 -> {
                                            int newGroup = readInt(scanner, "Enter new group number: ", 1, Integer.MAX_VALUE);
                                            //not finished
                                            service.moveStudentToGroup(student, newGroup);
                                            System.out.println("Group updated and student moved!");
                                        }
                                    }
                                }
                            }



                        }
                        else if (editStudent == 2){
                            //for id
                        }
                        else {
                            break;
                        }

                    }

                }
                case "5" -> {
                    System.out.println("1. Add Teacher");
                    System.out.println("2. Delete Teacher");
                    System.out.println("3. Edit information about teacher");
                    System.out.println("4. Show all");
                    System.out.println("0. Back");

                    int workWithTeacher = readInt(scanner, "Enter number of operation: ", 0, 4);

                    if (workWithTeacher == 1){ //add teacher
                        System.out.println("--- Add Teacher ---");
                        Faculty selectedFaculty = selectFaculty(scanner, service);
                        if (selectedFaculty == null) break;

                        // Select Department
                        Department selectedDept = selectDepartment(scanner, selectedFaculty);
                        if (selectedDept == null) break;


                        // Teachers's info
                        String name = readLine(scanner, "Name: ", true);
                        String surname = readLine(scanner, "Surname: ", true);
                        String position = readLine(scanner, "Position: ", true);



                        // Save
                        Teacher t = new Teacher(name,surname,position);
                        service.addTeacher(name, surname, position);
                        System.out.println("Teacher " + name + " " + surname +
                                " successfully added to department: " + selectedDept.getName());
                    }
                    else if (workWithTeacher == 2){
                        //delete teacher

                    } else if (workWithTeacher == 3) { //edit teacher
                        System.out.print("1. Edit by full name: ");
                        System.out.println("2. Edit by ID: ");
                        System.out.print("0. Back: ");
                        int editTeacher = readInt(scanner, "Select option: ", 0, 2);
                        if (editTeacher == 1) {
                            // Search among teachers
                        }
                        else if (editTeacher == 2){
                            //for id
                        }
                        else {
                            break;
                        }

                    }
                }

                case "6" -> {   //search
                    System.out.println("1. Find Student: ");
                    System.out.println("2. Find Teacher: ");
                    int searchType = readInt(scanner, "Select type of search: ", 1, 2);
                    if (searchType == 1) {

                        System.out.println("1. Search by full name: ");
                        System.out.println("2. Search by group number: ");
                        System.out.println("3. Search by course: ");
                        System.out.println("4. Search by speciality: ");
                        System.out.println("0. Back: ");

                        int searchBy = readInt(scanner, "Select search type: ", 0, 4);

                        if (searchBy == 1) { //by full name
                            String name = readLine(scanner, "Enter full name: ", true);
                            List<Student> result = service.findStudentsByFullName(name);
                            if (result.isEmpty()) {
                                System.out.println("No students found with this name.");
                            } else {
                                result.forEach(System.out::println);
                            }
                        }
                        else if (searchBy == 2) { //by group number

                            System.out.println("1. Find in specific speciality");
                            System.out.println("2. Find in all university");

                            int type = readInt(scanner, "> ", 1, 2);

                            if (type == 1) {    // Search in specific speciality

                                Faculty selectedFaculty = selectFaculty(scanner, service);
                                if (selectedFaculty == null) break;

                                Speciality selectedSpeciality = selectSpeciality(scanner, selectedFaculty);
                                if (selectedSpeciality == null) break;

                                int groupNumber = readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

                                List<Student> results = service.findStudentsInSpecialityByGroup(selectedSpeciality, groupNumber);

                                if (results.isEmpty()) {
                                    System.out.println("No students found in group " + groupNumber + " within " + selectedSpeciality.getName());
                                } else {
                                    results.forEach(System.out::println);
                                }
                            }
                            else {      // Search in all university
                                int groupNumber = readInt(scanner, "Enter Group number: ", 1, Integer.MAX_VALUE);

                                List<Student> results = service.findStudentsByGroup(groupNumber);

                                if (results.isEmpty()) {
                                    System.out.println("No students found in group " + groupNumber + " in the whole university.");
                                } else {
                                    results.forEach(System.out::println);
                                }
                            }
                            } else if (searchBy == 3) { //by course
                                int course = readInt(scanner, "Enter course number: ", 1, 6);
                                List<Student> results = service.findStudentsByCourse(course);
                        } else if (searchBy == 4) { // by speciality
                            //not finished
                        }

                    } else if (searchType == 2) { //search for teacher
                        System.out.println("1. Search by full name: ");
                        System.out.println("2. Search by department: ");
                        System.out.println("3. Search by position: ");
                        System.out.println("0. Back: ");

                        int searchBy = readInt(scanner, "Select search type: ", 0, 3);
                        if (searchBy == 1){
                            //search by full name
                        } else if (searchBy == 2) {

                        }



                    }

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
    private static Speciality selectSpeciality(Scanner scanner, Faculty faculty) {
        List<Speciality> specialities = faculty.getSpeciality();
        if (specialities.isEmpty()) {
            System.out.println("No specialities in this faculty!");
            return null;
        }
        System.out.println("--- Choose Speciality ---");
        for (int i = 0; i < specialities.size(); i++) {
            System.out.println((i + 1) + ". " + specialities.get(i).getName());
        }
        int index = readInt(scanner, "Enter speciality number: ", 1, specialities.size()) - 1;
        return specialities.get(index);
    }

    private static Department selectDepartment(Scanner scanner, Faculty faculty) {

        List<Department> departments = faculty.getDepartments();

        if (departments.isEmpty()) {
            System.out.println("No departments found in faculty: " + faculty.getName());
            return null;
        }

        System.out.println("\n--- Choose Department from " + faculty.getName() + " ---");
        for (int i = 0; i < departments.size(); i++) {
            System.out.println((i + 1) + ". " + departments.get(i).getName());
        }


        int index = readInt(scanner, "Enter department number (0 to cancel): ", 0, departments.size());

        if (index == 0) return null;

        return departments.get(index - 1);
    }
}