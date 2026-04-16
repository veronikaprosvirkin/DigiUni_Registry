package utils;

import person.Gender;
import person.GenderInferenceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility to migrate existing CSV files by adding inferred gender to all student and teacher records.
 */
public class GenderMigrationUtils {
    
    private static final Path STUDENTS_FILE = Path.of("data", "students.csv");
    private static final Path TEACHERS_FILE = Path.of("data", "teachers.csv");
    private static final String DELIMITER = ";";
    
    private static final String NEW_STUDENTS_HEADER = "id;name;surname;patronymic;gender;course;enrollmentDate;group;faculty;speciality;studyForm;status;email;phone;dateOfBirth;age";
    private static final String NEW_TEACHERS_HEADER = "id;name;surname;patronymic;gender;position;academicDegree;academicTitle;employmentDate;workload;email;phone;department;dateOfBirth;age";
    
    /**
     * Migrate students.csv by adding inferred gender to all records.
     */
    public static void migrateStudentsGender() throws IOException {
        if (!Files.exists(STUDENTS_FILE)) {
            System.out.println("Students file not found: " + STUDENTS_FILE);
            return;
        }
        
        List<String> lines = Files.readAllLines(STUDENTS_FILE, StandardCharsets.UTF_8);
        List<String> migratedLines = new ArrayList<>();
        
        // Add new header
        migratedLines.add(NEW_STUDENTS_HEADER);
        
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            
            String[] parts = line.split(DELIMITER, -1);
            if (parts.length < 4) {
                // Invalid line, skip
                continue;
            }
            
            // Extract name and patronymic
            String name = parts[1];
            String patronymic = parts[3];
            
            // Infer gender
            Gender gender = GenderInferenceUtils.infer(name, patronymic);
            String genderStr = gender != null ? gender.toString() : "";
            
            // Rebuild line with gender inserted at position 4 (after patronymic)
            StringBuilder migratedLine = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                if (j > 0) migratedLine.append(DELIMITER);
                migratedLine.append(parts[j]);
            }
            migratedLine.append(DELIMITER).append(genderStr);
            for (int j = 4; j < parts.length; j++) {
                migratedLine.append(DELIMITER).append(parts[j]);
            }
            
            migratedLines.add(migratedLine.toString());
        }
        
        // Write back
        Files.write(STUDENTS_FILE, migratedLines, StandardCharsets.UTF_8);
        System.out.println("✓ Students CSV migrated with gender inference. Records: " + (migratedLines.size() - 1));
    }
    
    /**
     * Migrate teachers.csv by adding inferred gender to all records.
     */
    public static void migrateTeachersGender() throws IOException {
        if (!Files.exists(TEACHERS_FILE)) {
            System.out.println("Teachers file not found: " + TEACHERS_FILE);
            return;
        }
        
        List<String> lines = Files.readAllLines(TEACHERS_FILE, StandardCharsets.UTF_8);
        List<String> migratedLines = new ArrayList<>();
        
        // Add new header
        migratedLines.add(NEW_TEACHERS_HEADER);
        
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            
            String[] parts = line.split(DELIMITER, -1);
            if (parts.length < 4) {
                // Invalid line, skip
                continue;
            }
            
            // Extract name and patronymic
            String name = parts[1];
            String patronymic = parts[3];
            
            // Infer gender
            Gender gender = GenderInferenceUtils.infer(name, patronymic);
            String genderStr = gender != null ? gender.toString() : "";
            
            // Rebuild line with gender inserted at position 4 (after patronymic)
            StringBuilder migratedLine = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                if (j > 0) migratedLine.append(DELIMITER);
                migratedLine.append(parts[j]);
            }
            migratedLine.append(DELIMITER).append(genderStr);
            for (int j = 4; j < parts.length; j++) {
                migratedLine.append(DELIMITER).append(parts[j]);
            }
            
            migratedLines.add(migratedLine.toString());
        }
        
        // Write back
        Files.write(TEACHERS_FILE, migratedLines, StandardCharsets.UTF_8);
        System.out.println("✓ Teachers CSV migrated with gender inference. Records: " + (migratedLines.size() - 1));
    }
    
    /**
     * Run both migrations.
     */
    public static void migrateAll() throws IOException {
        System.out.println("Starting gender migration...");
        migrateStudentsGender();
        migrateTeachersGender();
        System.out.println("✓ Gender migration complete!");
    }
    
    public static void main(String[] args) {
        try {
            migrateAll();
        } catch (IOException e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

