package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import person.Student;

public class StudentCardController {
    private static final DateTimeFormatter DOB_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label patronymicLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label facultyLabel;
    @FXML
    private Label specialityLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;

    // Updates all FXML labels with data from Student object
    public void updateCard(Student student) {
        if (student == null) {
            return;
        }

        nameLabel.setText(normalized(student.getOnlyName(), "N/A"));
        surnameLabel.setText(normalized(student.getSurname(), "N/A"));
        patronymicLabel.setText(normalized(student.getPatronymic(), "Not set"));

        // Adds "ID: " prefix to match the design
        String rawId = normalized(student.getId(), "N/A");
        idLabel.setText("ID: " + rawId);

        facultyLabel.setText(student.getFaculty() != null ? normalized(student.getFaculty().getName(), "N/A") : "N/A");
        specialityLabel.setText(student.getSpeciality() != null ? normalized(student.getSpeciality().getName(), "N/A") : "N/A");
        dobLabel.setText(formatDate(student.getDateOfBirth()));
        phoneLabel.setText(normalized(student.getPhone(), "Not set"));
        emailLabel.setText(normalized(student.getEmail(), "Not set"));
    }

    // Formats LocalDate to string or returns default text
    private static String formatDate(LocalDate date) {
        return date == null ? "Not set" : DOB_FORMATTER.format(date);
    }

    // Normalizes string with empty fallback
    private static String normalized(String value) {
        return normalized(value, "");
    }

    // Trims string or returns fallback if null/empty
    private static String normalized(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }
}