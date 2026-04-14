package university;

import utils.ModEntitiesUtils;
import utils.input.InputUtils;
import java.util.Scanner;

public class ModUniversityUtils {

    public static void showUniversityProfile(University university, Scanner scanner) {
        System.out.println("\n--- UNIVERSITY PROFILE ---");

        ModEntitiesUtils.printDetailedInfo(university.getInfo());

        System.out.println("Total Faculties: " + university.getFaculties().size());
        InputUtils.pause(scanner);
    }

    public static void editUniversityMenu(University university, Scanner scanner) {
        while (true) {
            UniversityInfo current = university.getInfo();
            System.out.println("\n--- Edit University Settings ---");
            System.out.println("Current Name: " + current.fullName());
            System.out.println("1. Edit Full Name");
            System.out.println("2. Edit Short Name");
            System.out.println("3. Edit City");
            System.out.println("4. Edit Address");
            System.out.println("0. Back");

            int choice = InputUtils.readInt(scanner, "> ", 0, 4);
            if (choice == 0) break;

            String newFullName = current.fullName();
            String newShortName = current.shortName();
            String newCity = current.city();
            String newAddress = current.address();

            switch (choice) {
                case 1 -> {
                    while (true) {
                        System.out.print("Enter new full name: ");
                        newFullName = scanner.nextLine().trim();
                        // Перевіряємо: тільки літери, пробіли, дефіси, апострофи, довжина від 3 символів
                        if (newFullName.matches("^[a-zA-Zа-яА-ЯіІїЇєЄґҐ\\s\\-']{3,}$")) {
                            break; // Виходимо з циклу, якщо все ок
                        }
                        System.out.println("Invalid input! Name must contain only letters and be at least 3 characters long.");
                    }
                }
                case 2 -> {
                    newShortName = InputUtils.readLine(scanner, "Enter new short name: ", false, false);
                }
                case 3 -> {
                    while (true) {
                        System.out.print("Enter new city: ");
                        newCity = scanner.nextLine().trim();

                        if (newCity.matches("^[a-zA-Zа-яА-ЯіІїЇєЄґҐ]+(?:[\\s\\-][a-zA-Zа-яА-ЯіІїЇєЄґҐ']+)?$")) {
                            break;
                        }
                        System.out.println("Invalid input! City must contain only letters, and at most one space or hyphen between words.");
                    }
                }
                case 4 -> {
                    while (true) {
                        System.out.print("Enter new address: ");
                        newAddress = scanner.nextLine().trim();
                        if (!newAddress.isEmpty() && newAddress.length() >= 5) {
                            break;
                        }
                        System.out.println("Invalid input! Address cannot be empty or too short.");
                    }
                }
            }

            university.setInfo(new UniversityInfo(newFullName, newShortName, newCity, newAddress));
            System.out.println("Successfully updated!");
        }
    }
}