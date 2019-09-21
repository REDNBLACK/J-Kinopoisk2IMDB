package org.f0w.k2i.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 621;

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setStage(stage);

        stage.setTitle("Kinopoisk2IMDB");
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.setResizable(false);

        stage.setOnCloseRequest(e -> {
            if (controller.destroy()) {
                Platform.exit();
            }

            e.consume();
        });

        stage.show();
    }
}
