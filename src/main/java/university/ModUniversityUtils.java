package university;

import utils.ModEntitiesUtils;
import utils.input.InputUtils;
import java.util.Scanner;

// NETWORK IMPORTS
import service.NetworkClient;
import service.Request;
import service.Response;

public class ModUniversityUtils {

    public static void showUniversityProfile(Scanner scanner) {
        System.out.println("\n--- UNIVERSITY PROFILE ---");

        // NETWORK: Fetch University from server
        Response res = NetworkClient.sendRequest(new Request("GET_UNIVERSITY"));
        if (!res.isSuccess() || res.getData() == null) {
            System.out.println("Failed to load university profile.");
            return;
        }

        University university = (University) res.getData();

        ModEntitiesUtils.printDetailedInfo(university.getInfo());
        System.out.println("Total Faculties: " + university.getFaculties().size());

        InputUtils.pause(scanner);
    }

    public static void editUniversityMenu(Scanner scanner) {
        while (true) {
            // NETWORK: Fetch current University info
            Response res = NetworkClient.sendRequest(new Request("GET_UNIVERSITY"));
            if (!res.isSuccess() || res.getData() == null) {
                System.out.println("Failed to load university profile.");
                return;
            }

            University university = (University) res.getData();
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

            // NETWORK: Send updated info to server
            UniversityInfo newInfo = new UniversityInfo(newFullName, newShortName, newCity, newAddress);
            Response updateRes = NetworkClient.sendRequest(new Request("EDIT_UNIVERSITY", newInfo));
            System.out.println(updateRes.getMessage());
        }
    }
}