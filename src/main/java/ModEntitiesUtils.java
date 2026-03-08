import java.util.List;
import java.util.Scanner;

public class ModEntitiesUtils {
    // * ===== METHODS FOR CHOOSING ENTITIES ===== * //
    public static int chooseEditing(Scanner scanner) {
        System.out.println("1. Edit by full name");
        System.out.println("2. Edit by ID");
        System.out.println("0. Back: ");
        return InputUtils.readInt(scanner, "> ", 0, 2);
    }

    // * ===== METHODS FOR DELETING ENTITIES ===== * //
    public static int chooseDeleting(Scanner scanner) {
        System.out.println("1. Delete by full name");
        System.out.println("2. Delete by ID");
        System.out.println("0. Cancel");
        return InputUtils.readInt(scanner, "> ", 0, 2);
    }

    /**
     * ? Speciality selection
     *
     * @param scanner provided
     * @param faculty provided
     * @return Speciality
     */
    static Speciality selectSpeciality(Scanner scanner, Faculty faculty) {
        List<Speciality> specialities = faculty.getSpeciality();
        if (specialities.isEmpty()) {
            System.out.println("No specialities in this faculty!");
            return null;
        }
        System.out.println("--- Choose Speciality ---");
        for (int i = 0; i < specialities.size(); i++) {
            System.out.println((i + 1) + ". " + specialities.get(i).getName());
        }
        System.out.println("0. Cancel");
        int index = InputUtils.readInt(scanner, "> ", 0, specialities.size());
        if (index == 0) {
            return null;
        }
        return specialities.get(index - 1);
    }


    static <T extends NamedEntity> T selectEntity(Scanner scanner, List<T> entities, String entityName) {
        if (entities.isEmpty()) {
            System.out.println("No " + entityName + " available!");
            return null;
        }
        System.out.println("--- Choose " + entityName + " ---");
        for (int i = 0; i < entities.size(); i++) {
            System.out.println((i + 1) + ". " + entities.get(i).getName());
        }
        System.out.println("0. Cancel");

        int index = InputUtils.readInt(scanner, "> ", 0, entities.size());
        return (index == 0) ? null : entities.get(index - 1);

    }

    static <T extends NamedEntity> void showAllEntity(Scanner scanner, List<T> entities, String entityName) {
        if (entities.isEmpty()) {
            System.out.println("No entities found!");
            return;
        }else {
            for (int i = 0; i < entities.size(); i++) {
                System.out.println((i + 1) + ". " + entities.get(i));
            }
        }
        InputUtils.pause(scanner);
    }

    static <T extends NamedEntity> void deleteEntity(Scanner scanner, List<T> entities, String entityName, java.util.function.Consumer<T> deleteAction) {

        if (entities.isEmpty()) {
            return;
        }

        T entityToProcess;
        if (entities.size() > 1) {
            System.out.println("Multiple " + entityName + " found. Please select one: ");
            for (int i = 0; i < entities.size(); i++) {
                System.out.println((i + 1) + ". " + entities.get(i).getDisplayInfo());
            }
            System.out.println("0. Cancel");

            int index = InputUtils.readInt(scanner, "> ", 0, entities.size());

            if (index == 0) {
                System.out.println("Operation cancelled.");
                return;
            }
            entityToProcess = entities.get(index - 1);
        } else {
            entityToProcess = entities.get(0);
        }

        System.out.println("Are you sure you want to delete: " + entityToProcess.getName() + "? (y/n): ");

        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            deleteAction.accept(entityToProcess);
        } else {
            System.out.println("Operation cancelled.");
        }
        InputUtils.pause(scanner);
    }
}
