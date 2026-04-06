package utils;

public class IdGenerator {
    private static int studentCounter = 1;
    private static int teacherCounter = 1;
    private static int facultyCounter = 1;
    private static int departmentCounter = 1;
    private static int specialityCounter = 1;

    public static void updateStudentCounter(String id) {
        try {
            int num = Integer.parseInt(id.substring(6));
            if (num >= studentCounter) studentCounter = num + 1;
        } catch (Exception e) {}
    }

    public static void updateTeacherCounter(String id) {
        try {
            int num = Integer.parseInt(id.substring(1));
            if (num >= teacherCounter) teacherCounter = num + 1;
        } catch (Exception e) {}
    }

    public static void updateFacultyCounter(String id) {
        try {
            int num = Integer.parseInt(id.substring(1));
            if (num >= facultyCounter) facultyCounter = num + 1;
        } catch (Exception e) {}
    }

    public static void updateDepartmentCounter(String id) {
        try {
            int num = Integer.parseInt(id.substring(1));
            if (num >= departmentCounter) departmentCounter = num + 1;
        } catch (Exception e) {}
    }

    public static void updateSpecialityCounter(String id) {
        try {
            int num = Integer.parseInt(id.substring(2));
            if (num >= specialityCounter) specialityCounter = num + 1;
        } catch (Exception e) {}
    }

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
