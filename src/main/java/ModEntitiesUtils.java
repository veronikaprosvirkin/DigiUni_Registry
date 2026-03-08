import java.util.Scanner;

public class ModEntitiesUtils {
    // * ===== METHODS FOR CHOOSING ENTITIES ===== * //
    public static int chooseEditing(Scanner scanner) {
        System.out.print("1. Edit by full name");
        System.out.println("2. Edit by ID");
        System.out.print("0. Back: ");
        return InputUtils.readInt(scanner, "> ", 0, 2);
    }

    // * ===== METHODS FOR DELETING ENTITIES ===== * //
    public static int chooseDeleting(Scanner scanner) {
        System.out.println("1. Delete by full name");
        System.out.println("2. Delete by ID");
        System.out.println("0. Cancel");
        return InputUtils.readInt(scanner, "> ", 0, 2);
    }
}
