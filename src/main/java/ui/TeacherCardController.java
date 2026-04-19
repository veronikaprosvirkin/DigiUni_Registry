package ui;

import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import person.Gender;
import person.Position;
import person.Teacher;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class TeacherCardController {
    @FXML
    private Label surnameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label patronymicLabel;
    @FXML
    private Label idLabel;

    @FXML
    private Label facultyLabel;
    @FXML
    private Label departmentLabel;
    @FXML
    private Label positionLabel;

    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;

    @FXML
    private Rectangle teacherPhotoRect;

    @FXML
    private StackPane archivedOverlay;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Rectangle bgPattern;
    @FXML
    private StackPane teacherPhoto;
    @FXML
    private HBox facultyChip;
    @FXML
    private HBox departmentChip;
    @FXML
    private HBox positionChip;
    @FXML
    private VBox contactsLabel;

    @FXML
    public void initialize() {
        applySmoothHover(facultyChip);
        applySmoothHover(departmentChip);
        applySmoothHover(positionChip);
        applySmoothHover(teacherPhoto);
        applySmoothHover(contactsLabel);

        setupRootHoverEffect();
    }
    private boolean isIdVisible = true;

    public void updateCard(Teacher teacher, boolean showId) {
        this.isIdVisible = showId;
        updateCard(teacher);
    }

    public void updateCard(Teacher teacher) {
        if (teacher == null) {
            return;
        }

        surnameLabel.setText(teacher.getSurname() != null ? teacher.getSurname() : "N/A");
        nameLabel.setText(teacher.getName() != null ? teacher.getOnlyName() : "N/A");
        patronymicLabel.setText(teacher.getPatronymic() != null ? teacher.getPatronymic() : "");

        idLabel.setVisible(isIdVisible);
        if (isIdVisible) {
            idLabel.setText("ID: " + (teacher.getId() != null ? teacher.getId() : "N/A"));
        }

        phoneLabel.setText(teacher.getPhone() != null ? teacher.getPhone() : "No phone");
        emailLabel.setText(teacher.getEmail() != null ? teacher.getEmail() : "No email");

        if (teacher.getDepartment() != null) {
            departmentLabel.setText(teacher.getDepartment().getName());
            if (teacher.getDepartment().getFaculty() != null) {
                facultyLabel.setText(teacher.getDepartment().getFaculty().getName());
            }
        } else {
            departmentLabel.setText("N/A");
            facultyLabel.setText("N/A");
        }

        String positionText = formatPositionString(teacher);
        positionLabel.setText(positionText);

        setTeacherPhoto(teacher);

        if (archivedOverlay != null) {
            archivedOverlay.setVisible(false);
        }
    }


    private String formatPositionString(Teacher teacher) {
        StringBuilder sb = new StringBuilder();

        if (teacher.getPosition() != null) {
            sb.append(teacher.getPosition());
        }

        if (teacher.getAcademicDegree() != null && !teacher.getAcademicDegree().isEmpty()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(teacher.getAcademicDegree());
        }

        return !sb.isEmpty() ? sb.toString() : "Position not specified";
    }

    private void setTeacherPhoto(Teacher teacher) {
        String id = teacher.getId() != null ? teacher.getId().toString() : null;
        Position pos = teacher.getPosition();
        String photoPath = null;

        if (id != null) {
            String personalPath = "/ui/images/photos/teachers/" + id + ".png";
            if (getClass().getResource(personalPath) != null) {
                photoPath = personalPath;
            }
        }

        if (photoPath == null) {
            if (pos == Position.DEAN || pos == Position.HEAD_OF_DEPARTMENT) {
                if (teacher.getGender() == Gender.FEMALE) {
                    photoPath = "/ui/images/photos/teachers/head_female.png";
                } else if (teacher.getGender() == Gender.MALE) {
                    photoPath = "/ui/images/photos/teachers/head_male.png";
                }
            }
        }

        if (photoPath == null) {
            if (teacher.getGender() == Gender.FEMALE) {
                photoPath = "/ui/images/photos/teachers/teacher_female_photo.png";
            } else if (teacher.getGender() == Gender.MALE) {
                photoPath = "/ui/images/photos/teachers/teacher_male_photo.png";
            } else {
                photoPath = "/ui/images/photos/teachers/teacher_other_photo.png";
            }
        }

        try {
            InputStream stream = getClass().getResourceAsStream(photoPath);
            if (stream == null) {
                throw new IllegalArgumentException("Photo not found: " + photoPath);
            }
            Image image = new Image(stream);
            applyCoverImage(teacherPhotoRect, image);
        } catch (Exception e) {
            InputStream fallbackStream = getClass().getResourceAsStream("/ui/images/photos/teachers/teacher_other_photo.png");
            if (fallbackStream != null) {
                applyCoverImage(teacherPhotoRect, new Image(fallbackStream));
            }
            System.err.println("Error with loading photo: " + photoPath);
        }
    }

    private static void applyCoverImage(Rectangle photoRect, Image image) {
        if (photoRect == null || image == null) {
            return;
        }

        double rectWidth = photoRect.getWidth();
        double rectHeight = photoRect.getHeight();
        if (rectWidth <= 0 || rectHeight <= 0 || image.getWidth() <= 0 || image.getHeight() <= 0) {
            photoRect.setFill(new ImagePattern(image));
            return;
        }

        // Scale to cover the full frame, then center by offsetting overflow.
        double scale = Math.max(rectWidth / image.getWidth(), rectHeight / image.getHeight());
        double scaledWidth = image.getWidth() * scale;
        double scaledHeight = image.getHeight() * scale;
        double x = (rectWidth - scaledWidth) / 2.0;
        double y = (rectHeight - scaledHeight) / 2.0;

        photoRect.setFill(new ImagePattern(image, x, y, scaledWidth, scaledHeight, false));
    }


    public void showArchived() {
        if (archivedOverlay != null) {
            archivedOverlay.setVisible(true);
        }
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
                        new KeyValue(shadow.colorProperty(), Color.rgb(0, 0, 0, 0.3))
                )
        );

        Timeline shadowOut = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(shadow.offsetYProperty(), 5),
                        new KeyValue(shadow.radiusProperty(), 15),
                        new KeyValue(shadow.colorProperty(), Color.rgb(0, 0, 0, 0.4))
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
        if (bgPattern == null || rootPane == null) return;

        FadeTransition fade = new FadeTransition(Duration.millis(300), bgPattern);

        rootPane.setOnMouseEntered(e -> {
            fade.stop();
            fade.setToValue(0.2);
            fade.play();
        });

        rootPane.setOnMouseExited(e -> {
            fade.stop();
            fade.setToValue(0.1);
            fade.play();
        });
    }

}