package ui;

import java.io.InputStream;

import faculty.Faculty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import person.Gender;
import person.Position;
import person.Teacher;

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
    private boolean isIdVisible = true;

    public void updateCard(Teacher teacher) {
        if (teacher == null) {
            return;
        }

        surnameLabel.setText(teacher.getSurname() != null ? teacher.getSurname() : "N/A");
        nameLabel.setText(teacher.getName() != null ? teacher.getOnlyName() : "N/A");
        patronymicLabel.setText(teacher.getPatronymic() != null ? teacher.getPatronymic() : "");
        idLabel.setText("ID: " + (teacher.getId() != null ? teacher.getId() : "N/A"));


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

        sb.append(" (").append(teacher.getWorkload()).append(")");

        return !sb.isEmpty() ? sb.toString() : "Position not specified";
    }
    private void setTeacherPhoto(Teacher teacher) {
        String id = teacher.getId();
        Position pos = teacher.getPosition();
        String photoPath = null;

        if (id != null) {
            String personalPath = "/ui/images/photos/" + id + ".png";
            if (getClass().getResource(personalPath) != null) {
                photoPath = personalPath;
            }
        }

        if (photoPath == null) {
            if (pos == Position.DEAN || pos == Position.HEAD_OF_DEPARTMENT) {
                if (teacher.getGender() == Gender.FEMALE) {
                    photoPath = "/ui/images/head_female.png";
                } else if (teacher.getGender() == Gender.MALE) {
                    photoPath = "/ui/images/head_male.png";
                }
            }
        }

        if (photoPath == null) {
            if (teacher.getGender() == Gender.FEMALE) {
                photoPath = "/ui/images/teacher_female_photo.png";
            } else if (teacher.getGender() == Gender.MALE) {
                photoPath = "/ui/images/teacher_male_photo.png";
            } else {
                photoPath = "/ui/images/teacher_other_photo.png";
            }
        }

        try {
            InputStream stream = getClass().getResourceAsStream(photoPath);
            if (stream == null) {
                throw new IllegalArgumentException("Photo not found: " + photoPath);
            }
            Image image = new Image(stream);
            teacherPhotoRect.setFill(new ImagePattern(image));
        } catch (Exception e) {
            InputStream fallbackStream = getClass().getResourceAsStream("/ui/images/teacher_other_photo.png");
            if (fallbackStream != null) {
                teacherPhotoRect.setFill(new ImagePattern(new Image(fallbackStream)));
            }
            System.err.println("Error with loading photo: " + photoPath);
        }
    }


    public void showArchived() {
        if (archivedOverlay != null) {
            archivedOverlay.setVisible(true);
        }
    }
}