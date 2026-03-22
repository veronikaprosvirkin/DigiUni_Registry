public class IdGenerator {
    private static int studentCounter = 1;
    private static int teacherCounter = 1;

    public static String generateStudentId(int year) {
       return String.format("st%d%04d", year, studentCounter++);
    }

    public static String generateTeacherId(){
        return String.format("t%04d", teacherCounter++);
    }
}
