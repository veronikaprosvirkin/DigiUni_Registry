import java.util.Scanner;

public class InputUtils {
    private InputUtils() {};


    // method for pause before going to the next menu
    static void pause(Scanner scanner){
        System.out.println("\nPress Enter to return to the menu...");
        scanner.nextLine();
    }

    /**
     * ? Method that checks if the line is an Integer between min and max values
     * @param scanner that reads a line
     * @param prompt that displays to user
     * @param min value allowed
     * @param max value allowed
     * @return verified int
     */
    public static int readInt(Scanner scanner, String prompt, int min, int max) {
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
     * ? Method that checks if the line is blank or has not only letters
     * @param scanner that reads a line
     * @param prompt that displays to user
     * @param emptyAllowed false to forbid line being empty
     * @param specialSymbolsAllowed false to forbid anything except letters
     * @return verified line
     */
    public static String readLine(Scanner scanner, String prompt, boolean emptyAllowed, boolean specialSymbolsAllowed) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();

            if (!emptyAllowed && line.isBlank()) {
                System.out.println("Error: Field cannot be empty!");
                continue;
            }

            if (!specialSymbolsAllowed) {
                if (line.chars().anyMatch(c -> !Character.isLetter(c) && !Character.isSpaceChar(c))) {
                    System.out.println("Error: Special symbols are not allowed!");
                    continue;
                }
            }

            return line;
        }
    }

    /**
     * Method that clears all spaces
     * @param line before cleaning
     * @param rmAll remove all spaces
     * @param rmStart remove spaces at start
     * @param rmEnd remove spaces at end
     * @param rmMulti remove spaces that repeats
     * @return cleared line
     */
    public static String removeSpaces(String line, boolean rmAll, boolean rmStart, boolean rmEnd, boolean rmMulti) {
        if (line == null || line.isEmpty()) return "";

        if (rmAll) return line.replace(" ", "");

        String result = line;

        if (rmStart) {  result = result.replaceAll("^\\s+", "");    }

        if (rmEnd)   {  result = result.replaceAll("\\s+$", "");    }

        if (rmMulti) {
            StringBuilder sb = new StringBuilder();
            boolean spaceDetected = false;
            for (int i = 0; i < result.length(); i++) {
                char c = result.charAt(i);
                if (!Character.isSpaceChar(c)) {
                    sb.append(c);
                    spaceDetected = false;
                } else if (!spaceDetected) {
                    sb.append(c);
                    spaceDetected = true;
                }
            }
            return sb.toString();
        }

        return result;
    }
}