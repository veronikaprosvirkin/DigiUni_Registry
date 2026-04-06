package utils;

import faculty.Faculty;
import speciality.Speciality;
import department.Department;
import university.University;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileStorageUtils {

    private static final Path FACULTIES_FILE = Path.of("data", "faculties.csv");
    private static final Path SPECIALITIES_FILE = Path.of("data", "specialities.csv");
    private static final Path DEPARTMENTS_FILE = Path.of("data", "departments.csv");

    // Save all structure
    public static void saveAll(University university) {
        try {
            Files.createDirectories(FACULTIES_FILE.getParent());
            saveFaculties(university.getFaculties());
            saveSpecialities(university.getFaculties());
            saveDepartments(university.getFaculties());
        } catch (IOException e) {
            System.err.println("Save error");
        }
    }

    // Load all structure
    public static void loadAll(University university) {
        try {
            university.getFaculties().clear();
            if (Files.exists(FACULTIES_FILE)) loadFaculties(university);
            if (Files.exists(SPECIALITIES_FILE)) loadSpecialities(university);
            if (Files.exists(DEPARTMENTS_FILE)) loadDepartments(university);
        } catch (IOException e) {
            System.err.println("Load error");
        }
    }

    // Save faculties
    private static void saveFaculties(List<Faculty> faculties) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(FACULTIES_FILE, StandardCharsets.UTF_8)) {
            for (Faculty f : faculties) {
                // Ignore dean for simplicity in this example
                w.write(f.getId() + ";" + f.getNameOfFaculty() + ";" + f.getShortName() + ";" + f.getContacts());
                w.newLine();
            }
        }
    }

    // Load faculties
    private static void loadFaculties(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(FACULTIES_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    String id = parts[0];
                    String name = parts[1];
                    String shortName = parts[2];
                    String contact = parts[3];
                    u.getFaculties().add(new Faculty(id, name, shortName, contact, null));
                    IdGenerator.updateFacultyCounter(id);
                }
            }
        }
    }

    // Save specialities
    private static void saveSpecialities(List<Faculty> faculties) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(SPECIALITIES_FILE, StandardCharsets.UTF_8)) {
            for (Faculty f : faculties) {
                for (Speciality s : f.getSpeciality()) {
                    w.write(s.getId() + ";" + s.getNameOfSpeciality() + ";" + f.getId());
                    w.newLine();
                }
            }
        }
    }

    // Load specialities and link to faculty
    private static void loadSpecialities(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(SPECIALITIES_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    String id = parts[0];
                    String name = parts[1];
                    String facultyId = parts[2];
                    Speciality s = new Speciality(id, name);
                    IdGenerator.updateSpecialityCounter(id);

                    // Find parent faculty and add
                    u.getFaculties().stream()
                            .filter(f -> f.getId().equals(facultyId))
                            .findFirst()
                            .ifPresent(f -> f.getSpeciality().add(s));
                }
            }
        }
    }

    // Save departments
    private static void saveDepartments(List<Faculty> faculties) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(DEPARTMENTS_FILE, StandardCharsets.UTF_8)) {
            for (Faculty f : faculties) {
                for (Department d : f.getDepartments()) {
                    w.write(d.getId() + ";" + d.getNameOfDepartment() + ";" + f.getId());
                    w.newLine();
                }
            }
        }
    }

    // Load departments and link to faculty
    private static void loadDepartments(University u) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(DEPARTMENTS_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    String id = parts[0];
                    String name = parts[1];
                    String facultyId = parts[2];
                    Department d = new Department(id, name);
                    IdGenerator.updateDepartmentCounter(id);

                    // Find parent faculty and add
                    u.getFaculties().stream()
                            .filter(f -> f.getId().equals(facultyId))
                            .findFirst()
                            .ifPresent(f -> f.getDepartments().add(d));
                }
            }
        }
    }
}