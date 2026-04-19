package ui;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import person.Gender;
import person.Student;
import person.StudyForm;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.animation.FadeTransition;


public class StudentCardController {
    private static final DateTimeFormatter DOB_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String MALE_PHOTO_RESOURCE = "/ui/images/photos/students/student_male_photo.png";
    private static final String FEMALE_PHOTO_RESOURCE = "/ui/images/photos/students/student_female_photo.png";
    private static final String OTHER_PHOTO_RESOURCE = "/ui/images/photos/students/student_other_photo.png";
    private static final Image MALE_PHOTO = loadImage(MALE_PHOTO_RESOURCE);
    private static final Image FEMALE_PHOTO = loadImage(FEMALE_PHOTO_RESOURCE);
    private static final Image OTHER_PHOTO = loadImage(OTHER_PHOTO_RESOURCE);

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Rectangle bgPattern;
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
    private StackPane studentPhoto;
    @FXML
    private ImageView statusStampView;
    @FXML
    private StackPane archivedOverlay;
    @FXML
    private HBox facultyChip;
    @FXML
    private HBox specialityChip;
    @FXML
    private HBox positionChip;



    @FXML
    public void initialize() {
        applySmoothHover(facultyChip);
        applySmoothHover(specialityChip);
        applySmoothHover(positionChip);
        applySmoothHover(studentPhoto);
        setupRootHoverEffect();
    }

    private boolean isIdVisible = true;

    // Updates all FXML labels with data from Student object
    public void updateCard(Student student, boolean showId) {
        this.isIdVisible = showId;
        updateCard(student);
    }

    public void updateCard(Student student) {
        if (student == null) {
            return;
        }

        updateStudentPhoto(student.getGender());
        nameLabel.setText(normalized(student.getOnlyName(), "N/A"));
        surnameLabel.setText(normalized(student.getSurname(), "N/A"));
        patronymicLabel.setText(normalized(student.getPatronymic(), "Not set"));

        idLabel.setVisible(isIdVisible); 
        if (isIdVisible) {
            String rawId = normalized(student.getId(), "N/A");
            idLabel.setText("ID: " + rawId);
        }

        facultyLabel.setText(student.getFaculty() != null ? normalized(student.getFaculty().getName(), "N/A") : "N/A");
        specialityLabel.setText(student.getSpeciality() != null ? normalized(student.getSpeciality().getName(), "N/A") : "N/A");

        String courseStr = (student.getEnrollmentDate() == null) ? "N/A" : String.valueOf(student.getCourse());
        String groupStr = (student.getGroup() <= 0) ? "N/A" : String.valueOf(student.getGroup());
        courseGroupLabel.setText("Course " + courseStr + " | Group " + groupStr);

        phoneLabel.setText(normalized(student.getPhone(), "Not set"));
        emailLabel.setText(normalized(student.getEmail(), "Not set"));
        updateStatusStamp(student);

        if (archivedOverlay != null) {
            archivedOverlay.setVisible(false);
        }
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
        if (statusStampView == null) {
            return;
        }

        StudyForm studyForm = student.getStudyForm();
        if (studyForm == null) {
            statusStampView.setImage(null);
            statusStampView.setVisible(false);
            return;
        }

        String imageName = switch (studyForm) {
            case BUDGET -> "stamp_budget.png";
            case CONTRACT -> "stamp_contract.png";
            default -> null;
        };

        if (imageName == null) {
            statusStampView.setImage(null);
            statusStampView.setVisible(false);
            return;
        }

        try {
            String path = "images/" + imageName;
            statusStampView.setImage(new Image(getClass().getResourceAsStream(path)));
            statusStampView.setVisible(true);
        } catch (Exception e) {
            statusStampView.setImage(null);
            statusStampView.setVisible(false);
            System.err.println("Could not load stamp: " + e.getMessage());
        }
    }

    // Show archived stamp overlay
    public void showArchived() {
        archivedOverlay.setVisible(true);
        archivedOverlay.toFront();
    }


    private void applySmoothHover(Node node) {
        if (node == null) return;

        ScaleTransition st = new ScaleTransition(Duration.millis(200), node);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.35));
        shadow.setRadius(15);
        shadow.setOffsetY(5);
        shadow.setSpread(0.08);
        node.setEffect(shadow);

        Timeline shadowIn = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(shadow.offsetYProperty(), 9),
                        new KeyValue(shadow.radiusProperty(), 24),
                        new KeyValue(shadow.colorProperty(), Color.rgb(0, 0, 0, 0.5))
                )
        );

        // Shadow hover OUT timeline
        Timeline shadowOut = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(shadow.offsetYProperty(), 5),
                        new KeyValue(shadow.radiusProperty(), 15),
                        new KeyValue(shadow.colorProperty(), Color.rgb(0, 0, 0, 0.35))
                )
        );

        node.setOnMouseEntered(e -> {
            st.stop();
            shadowOut.stop();

            st.setToX(1.03);
            st.setToY(1.03);

            st.playFromStart();
            shadowIn.playFromStart();
        });

        node.setOnMouseExited(e -> {
            st.stop();
            shadowIn.stop();

            st.setToX(1.0);
            st.setToY(1.0);

            st.playFromStart();
            shadowOut.playFromStart();
        });
    }

    private void setupRootHoverEffect() {
        FadeTransition fade = new FadeTransition(Duration.millis(300), bgPattern);

        rootPane.setOnMouseEntered(e -> {
            fade.stop();
            fade.setToValue(0.25);
            fade.play();
        });

        rootPane.setOnMouseExited(e -> {
            fade.stop();
            fade.setToValue(0.15);
            fade.play();
        });
    }
}