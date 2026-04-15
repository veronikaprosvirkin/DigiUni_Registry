package ui;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import person.Student;

public final class StudentCardWindow {
    private static final AtomicBoolean FX_INITIALIZED = new AtomicBoolean(false);
    private static final Object WINDOW_LOCK = new Object();

    private static Stage stage;
    private static StudentCardController controller;

    private StudentCardWindow() {
    }

    public static void open(Student student) {
        if (student == null) {
            return;
        }
        ensureJavaFxStarted();

        Platform.runLater(() -> {
            synchronized (WINDOW_LOCK) {
                if (stage == null) {
                    createWindow();
                }
                if (controller != null) {
                    controller.updateCard(student);
                }
                stage.show();
                stage.toFront();
            }
        });
    }

    public static void refresh(Student student) {
        if (student == null || !FX_INITIALIZED.get()) {
            return;
        }

        Platform.runLater(() -> {
            synchronized (WINDOW_LOCK) {
                if (controller != null) {
                    controller.updateCard(student);
                }
            }
        });
    }

    public static void close() {
        if (!FX_INITIALIZED.get()) {
            return;
        }

        Platform.runLater(() -> {
            synchronized (WINDOW_LOCK) {
                if (stage != null) {
                    stage.hide();
                }
            }
        });
    }

    private static void ensureJavaFxStarted() {
        if (FX_INITIALIZED.get()) {
            return;
        }

        synchronized (WINDOW_LOCK) {
            if (FX_INITIALIZED.get()) {
                return;
            }

            CountDownLatch startupLatch = new CountDownLatch(1);
            try {
                Platform.startup(startupLatch::countDown);
            } catch (IllegalStateException alreadyStarted) {
                FX_INITIALIZED.set(true);
                return;
            }
            try {
                startupLatch.await();
                FX_INITIALIZED.set(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("JavaFX startup interrupted", e);
            }
        }
    }

    private static void createWindow() {
        try {
            URL fxmlUrl = Objects.requireNonNull(StudentCardWindow.class.getResource("/ui/student-card.fxml"),
                    "Missing FXML resource: /ui/student-card.fxml");
            URL cssUrl = Objects.requireNonNull(StudentCardWindow.class.getResource("/ui/student-card.css"),
                    "Missing CSS resource: /ui/student-card.css");

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            controller = loader.getController();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            stage = new Stage();
            stage.setTitle("Student Card");
            stage.setScene(scene);
            stage.setMinWidth(460);
            stage.setMinHeight(360);
            stage.setOnCloseRequest(event -> stage.hide());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create student card window", e);
        }
    }
}


