package utils;

public class IdGenerator {
    private static int studentCounter = 1;
    private static int teacherCounter = 1;
    private static int facultyCounter = 1;
    private static int departmentCounter = 1;
    private static int specialityCounter = 1;



    public static String generateStudentId(int year) {
       return String.format("st%d%04d", year, studentCounter++);
    }

    public static String generateTeacherId(){
        return String.format("t%04d", teacherCounter++);
    }
    public static String generateFacultyId(){
        return String.format("f%03d", facultyCounter++);
    }
    public static String generateDepartmentId(){
        return String.format("d%03d", departmentCounter++);
    }
    public static String generateSpecialityId(){
        return String.format("sp%03d", specialityCounter++);
    }

}
