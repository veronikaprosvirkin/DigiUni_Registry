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

    public static void updateFacultyCounter(String id) {
        int numericPart = extractNumericPart(id, "f");
        if (numericPart >= facultyCounter) {
            facultyCounter = numericPart + 1;
        }
    }

    public static void updateDepartmentCounter(String id) {
        int numericPart = extractNumericPart(id, "d");
        if (numericPart >= departmentCounter) {
            departmentCounter = numericPart + 1;
        }
    }

    public static void updateSpecialityCounter(String id) {
        int numericPart = extractNumericPart(id, "sp");
        if (numericPart >= specialityCounter) {
            specialityCounter = numericPart + 1;
        }
    }

    private static int extractNumericPart(String id, String prefix) {
        if (id == null || !id.startsWith(prefix)) {
            return -1;
        }

        try {
            return Integer.parseInt(id.substring(prefix.length()));
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }

    public static void updateTeacherCounter(String id) {
        int numericPart = extractNumericPart(id, "t");
        if (numericPart >= teacherCounter) {
            teacherCounter = numericPart + 1;
        }
    }
    public static void updateStudentCounter(String id) {
        if (id != null && id.startsWith("st") && id.length() > 6) {
            try {
                int numericPart = Integer.parseInt(id.substring(6));
                if (numericPart >= studentCounter) {
                    studentCounter = numericPart + 1;
                }
            } catch (NumberFormatException ignored) {

            }
        }
    }
}
