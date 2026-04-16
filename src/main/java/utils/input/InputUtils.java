package utils.input;

import person.Person;
import person.StudentService;
import person.TeacherService;

import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.function.Predicate;

public class InputUtils {
    public InputUtils() {};


    // method for pause before going to the next menu
    public static void pause(Scanner scanner){
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
    /**
     * method that reads email prefix, checks if it is valid and unique, and generates email if user wants
     *@param scanner that reads a line
     *@param domain domain "@digiuni.ukma.edu"
     * @param autoGenerator function that generates email prefix, should be passed as lambda or method reference
     * @param isTakenChecker function that checks if email is already taken, should be passed as lambda or method reference
     * @return validated email
     */
    public static String readAndValidateEmail(Scanner scanner, String domain,
                                              Supplier<String> autoGenerator,
                                              Predicate<String> isTakenChecker) {
        System.out.println("Email domain will always be: " + domain);
        String prefix = readLine(scanner, "Enter email prefix/username (press Enter to auto-generate): ", true, true);

        String finalEmail;

        if (prefix.isEmpty()) {
            String generatedEmail = autoGenerator.get();

            if (!isTakenChecker.test(generatedEmail)) {
                System.out.println("Email generated: " + generatedEmail);
                finalEmail = generatedEmail;
            } else {
                System.out.println("Generated email " + generatedEmail + " is already in system");
                finalEmail = forceUniquePrefix(scanner, domain, isTakenChecker);
            }
        } else {
            String emailToCheck = cleanPrefix(prefix) + domain;
            if (isTakenChecker.test(emailToCheck)) {
                System.out.println("This email (" + emailToCheck + ") is already taken");
                finalEmail = forceUniquePrefix(scanner, domain, isTakenChecker);
            } else {
                System.out.println("Email set to: " + emailToCheck);
                finalEmail = emailToCheck;
            }
        }

        return removeSpaces(finalEmail, false, true, true, true);
    }


    private static String forceUniquePrefix(Scanner scanner, String domain, Predicate<String> isTakenChecker) {
        while (true) {
            String newPrefix = readLine(scanner, "Enter a UNIQUE email prefix (only letters, numbers, and dots allowed): ", false, true);
            String fullNewEmail = cleanPrefix(newPrefix) + domain;

            if (!fullNewEmail.replace(domain, "").isEmpty() && !isTakenChecker.test(fullNewEmail)) {
                System.out.println("Email set to: " + fullNewEmail);
                return fullNewEmail;
            } else {
                System.out.println("Error. This email is taken or prefix is empty. Try again.");
            }
        }
    }

    private static String cleanPrefix(String prefix) {
        if (prefix.contains("@")) {
            prefix = prefix.split("@")[0];
        }
        return prefix.toLowerCase().replaceAll("[^a-z0-9.]", "");
    }
}