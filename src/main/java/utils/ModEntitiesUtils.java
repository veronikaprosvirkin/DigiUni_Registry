package utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import utils.annotations.DetailDisplay;
import utils.input.InputUtils;
import person.Student;
import person.Teacher;
import speciality.Speciality;
import faculty.Faculty;
import department.Department;
import utils.namedEntity.NamedEntity;

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
    public static Optional<Speciality> selectSpeciality(Scanner scanner, Faculty faculty) {
        List<Speciality> specialities = faculty.getSpeciality();
        if (specialities.isEmpty()) {
            System.out.println("No specialities in this faculty!");
            return Optional.empty();
        }
        System.out.println("--- Choose Speciality ---");
        for (int i = 0; i < specialities.size(); i++) {
            System.out.println((i + 1) + ". " + specialities.get(i).getName());
        }
        System.out.println("0. Cancel");
        int index = InputUtils.readInt(scanner, "> ", 0, specialities.size());
        if (index == 0) {
            return Optional.empty();
        }
        return Optional.of(specialities.get(index - 1));
    }


    public static <T extends NamedEntity> Optional<T> selectEntity(Scanner scanner, List<T> entities, String entityName) {
        if (entities.isEmpty()) {
            System.out.println("No " + entityName + " available!");
            return Optional.empty();
        }
        System.out.println("--- Choose " + entityName + " ---");
        for (int i = 0; i < entities.size(); i++) {
            System.out.println((i + 1) + ". " + entities.get(i).getName());
        }
        System.out.println("0. Cancel");

        int index = InputUtils.readInt(scanner, "> ", 0, entities.size());
        return (index == 0) ? Optional.empty() : Optional.of(entities.get(index - 1));

    }

    public static <T extends NamedEntity> void showAllEntity(Scanner scanner, List<T> entities, String entityName, boolean showId) {
        if (entities.isEmpty()) {
            System.out.println("No " + entityName + " found!");
            return;
        }else {
            for (int i = 0; i < entities.size(); i++) {
                T entity = entities.get(i);
                String idPrefix = "";
                if (showId) {
                    if (entity instanceof Student){
                        Student student = (Student) entity;
                        idPrefix = "[ID: " + student.getId() + "]  ";
                    } else if (entity instanceof Teacher) {
                        Teacher teacher = (Teacher) entity;
                        idPrefix = "[ID: " + teacher.getId() + "]  ";
                    }
                }
                System.out.println((i + 1) + ". " + idPrefix + entity.getDisplayInfo());
            }
        }
        InputUtils.pause(scanner);
    }

    public static <T extends NamedEntity> void deleteEntity(Scanner scanner, List<T> entities, String entityName, java.util.function.Consumer<T> deleteAction) {

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
    public static void printDetailedInfo(Object entity) {
        if (entity == null) return;

        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();

        System.out.println("\n=========================================");
        System.out.println("   DETAILED INFORMATION (" + clazz.getSimpleName().toUpperCase() + ")");
        System.out.println("=========================================");

        boolean found = false;
        for (Field field : fields) {
            if (field.isAnnotationPresent(DetailDisplay.class)) {
                found = true;
                DetailDisplay annotation = field.getAnnotation(DetailDisplay.class);
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    String displayValue = "N/A";

                    if (value != null) {
                        displayValue = value.toString();
                    }
                    System.out.printf("%-20s : %s%n", annotation.label(), displayValue);

                } catch (IllegalAccessException e) {
                    System.out.println("Error accessing field: " + field.getName());
                }
            }
        }

        if (!found) {
            System.out.println("No annotated fields found for display.");
        }
        System.out.println("=========================================\n");
    }
}
