import java.util.Scanner;

public class InputUtils {
    private InputUtils() {};

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

                if (line.chars().anyMatch(c -> !Character.isLetter(c))) {
                    System.out.println("Error: Special symbols are not allowed!");
                    continue;
                }
            }

            return line;
        }
    }

    /**
     * ? Method that clears all spaces
     * @param line before cleaning
     * @return cleared line
     */
    public static String removeSpaces(String line) {
        String result = "";
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isSpaceChar(line.charAt(i))) {
                result += line.charAt(i);
            }
        }
        return result;
    }
}