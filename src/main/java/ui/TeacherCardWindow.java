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
import person.Teacher;

public final class TeacherCardWindow {
    private static final AtomicBoolean FX_INITIALIZED = new AtomicBoolean(false);
    private static final Object WINDOW_LOCK = new Object();

    private static Stage stage;
    private static TeacherCardController controller;

    private TeacherCardWindow() {
    }

    public static void open(Teacher teacher) {
        if (teacher == null) {
            return;
        }
        ensureJavaFxStarted();

        Platform.runLater(() -> {
            synchronized (WINDOW_LOCK) {
                if (stage == null) {
                    createWindow();
                }
                if (controller != null) {
                    controller.updateCard(teacher);
                }
                stage.show();
                stage.toFront();
            }
        });
    }

    public static void refresh(Teacher teacher) {
        if (teacher == null || !FX_INITIALIZED.get()) {
            return;
        }

        Platform.runLater(() -> {
            synchronized (WINDOW_LOCK) {
                if (controller != null) {
                    controller.updateCard(teacher);
                }
            }
        });
    }

    public static void showArchived() {
        if (!FX_INITIALIZED.get()) {
            return;
        }

        Platform.runLater(() -> {
            synchronized (WINDOW_LOCK) {
                if (controller != null) {
                    controller.showArchived();
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
                Platform.startup(() -> {
                    // Prevent FX shutdown
                    Platform.setImplicitExit(false);
                    startupLatch.countDown();
                });
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
            URL fxmlUrl = Objects.requireNonNull(TeacherCardWindow.class.getResource("/ui/teacher-card.fxml"),
                    "Missing FXML resource: /ui/teacher-card.fxml");
            URL cssUrl = Objects.requireNonNull(TeacherCardWindow.class.getResource("/ui/teacher-card.css"),
                    "Missing CSS resource: /ui/teacher-card.css");

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            controller = loader.getController();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(cssUrl.toExternalForm());

            // Create stage
            stage = new Stage();
            stage.setTitle("Teacher Card");
            stage.setScene(scene);

            stage.setResizable(false);
            stage.setAlwaysOnTop(true);

            stage.setOnCloseRequest(event -> stage.hide());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create teacher card window", e);
        }
    }
}
