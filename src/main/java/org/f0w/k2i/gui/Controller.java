package org.f0w.k2i.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    private Stage stage;
    private final FileChooser fileChooser = new FileChooser();

    @FXML
    private Text actiontarget;

    @FXML
    private ChoiceBox modeChoiceBox;

    @FXML
    private ChoiceBox queryFormatChoiceBox;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }

    @FXML
    protected void handleFileChoseAction(ActionEvent event) {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            actiontarget.setText(file.getAbsolutePath());
        }
    }
}