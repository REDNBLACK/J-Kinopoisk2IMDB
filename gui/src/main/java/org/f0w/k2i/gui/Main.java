package org.f0w.k2i.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.init(stage);

        stage.setTitle("Kinopoisk2IMDB GUI");
        stage.setScene(new Scene(root, 500, 300));
        stage.show();
    }
}
