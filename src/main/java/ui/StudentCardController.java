package ui;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import person.Gender;
import person.Student;

public class StudentCardController {
    private static final DateTimeFormatter DOB_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String MALE_PHOTO_RESOURCE = "/ui/images/student_male_photo.png";
    private static final String FEMALE_PHOTO_RESOURCE = "/ui/images/student_female_photo.png";
    private static final String OTHER_PHOTO_RESOURCE = "/ui/images/student_other_photo.png";
    private static final Image MALE_PHOTO = loadImage(MALE_PHOTO_RESOURCE);
    private static final Image FEMALE_PHOTO = loadImage(FEMALE_PHOTO_RESOURCE);
    private static final Image OTHER_PHOTO = loadImage(OTHER_PHOTO_RESOURCE);

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
    private Label courseGroupLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Rectangle studentPhotoRect;
    @FXML
    private ImageView statusStampView;


    // Updates all FXML labels with data from Student object
    public void updateCard(Student student) {
        if (student == null) {
            return;
        }

        updateStudentPhoto(student.getGender());
        nameLabel.setText(normalized(student.getOnlyName(), "N/A"));
        surnameLabel.setText(normalized(student.getSurname(), "N/A"));
        patronymicLabel.setText(normalized(student.getPatronymic(), "Not set"));

        // Adds "ID: " prefix to match the design
        String rawId = normalized(student.getId(), "N/A");
        idLabel.setText("ID: " + rawId);

        facultyLabel.setText(student.getFaculty() != null ? normalized(student.getFaculty().getName(), "N/A") : "N/A");
        specialityLabel.setText(student.getSpeciality() != null ? normalized(student.getSpeciality().getName(), "N/A") : "N/A");
        courseGroupLabel.setText("Course " + student.getCourse() + " | Group " + student.getGroup());;
        phoneLabel.setText(normalized(student.getPhone(), "Not set"));
        emailLabel.setText(normalized(student.getEmail(), "Not set"));
        updateStatusStamp(student);
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

    private void updateStudentPhoto(Gender gender) {
        if (studentPhotoRect == null) {
            return;
        }

        Image photo = resolvePhotoByGender(gender);
        if (photo != null) {
            studentPhotoRect.setFill(new ImagePattern(photo));
        }
    }

    private static Image resolvePhotoByGender(Gender gender) {
        if (gender == Gender.FEMALE) {
            return FEMALE_PHOTO != null ? FEMALE_PHOTO : MALE_PHOTO;
        }
        if (gender == Gender.MALE) {
            return MALE_PHOTO;
        }

        return OTHER_PHOTO != null ? OTHER_PHOTO : MALE_PHOTO;
    }

    private static Image loadImage(String resourcePath) {
        URL imageUrl = StudentCardController.class.getResource(resourcePath);
        return imageUrl == null ? null : new Image(imageUrl.toExternalForm());
    }

    // Sets stamp image based on study form
    private void updateStatusStamp(Student student) {
        if (student.getStudyForm() == null) {
            statusStampView.setImage(null);
            return;
        }

        String imageName;
        // Use your enum values here (e.g., BUDGET/CONTRACT)
        if (student.getStudyForm().toString().equalsIgnoreCase("BUDGET")) {
            imageName = "stamp_budget.png";
        } else {
            imageName = "stamp_contract.png";
        }

        try {
            String path = "images/" + imageName;
            statusStampView.setImage(new Image(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            // Log error if image missing
            System.err.println("Could not load stamp: " + e.getMessage());
        }
    }
}