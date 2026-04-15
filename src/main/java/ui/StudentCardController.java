package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import person.Student;

public class StudentCardController {
    private static final DateTimeFormatter DOB_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    private Label nameValue;
    @FXML
    private Label surnameValue;
    @FXML
    private Label patronymicValue;
    @FXML
    private Label idValue;
    @FXML
    private Label facultyValue;
    @FXML
    private Label specialityValue;
    @FXML
    private Label dobValue;
    @FXML
    private Label phoneValue;
    @FXML
    private Label emailValue;

    public void updateCard(Student student) {
        if (student == null) {
            return;
        }

        nameValue.setText(normalized(student.getOnlyName(), "N/A"));
        surnameValue.setText(normalized(student.getSurname(), "N/A"));
        patronymicValue.setText(normalized(student.getPatronymic(), "Not set"));
        idValue.setText(normalized(student.getId(), "N/A"));
        facultyValue.setText(student.getFaculty() != null ? normalized(student.getFaculty().getName(), "N/A") : "N/A");
        specialityValue.setText(student.getSpeciality() != null ? normalized(student.getSpeciality().getName(), "N/A") : "N/A");
        dobValue.setText(formatDate(student.getDateOfBirth()));
        phoneValue.setText(normalized(student.getPhone(), "Not set"));
        emailValue.setText(normalized(student.getEmail(), "Not set"));
    }

    private static String formatDate(LocalDate date) {
        return date == null ? "Not set" : DOB_FORMATTER.format(date);
    }

    private static String normalized(String value) {
        return normalized(value, "");
    }

    private static String normalized(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }
}


