package org.f0w.k2i.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.f0w.k2i.core.command.MovieCommand;
import org.f0w.k2i.core.exchange.finder.MovieFinder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.f0w.k2i.core.command.MovieCommand.Type.*;
import static org.f0w.k2i.core.exchange.finder.MovieFinder.Type.*;

public class Controller {
    private Stage stage;
    private final FileChooser fileChooser = new FileChooser();
    private File kpFile;
    private final Map<String, Object> config = new HashMap<>();

    private static final String ERROR_CLASS = "error";

    @FXML
    private Text titleText;

    @FXML
    private ChoiceBox<Choice<MovieCommand.Type, String>> modeChoiceBox;

    @FXML
    private ChoiceBox<Choice<MovieFinder.Type, String>> queryFormatChoiceBox;

    @FXML
    private TextField authId;

    @FXML
    private TextField listId;

    @FXML
    private Button selectFileBtn;

    public void init(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        modeChoiceBox.setItems(FXCollections.observableArrayList(
                new Choice<>(SET_RATING, "Выставить рейтинг"),
                new Choice<>(ADD_TO_WATCHLIST, "Добавить в список"),
                new Choice<>(COMBINED, "Добавить в список и выставить рейтинг")
        ));
        modeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.id.equals(ADD_TO_WATCHLIST) || newValue.id.equals(COMBINED)) {
                listId.setEditable(true);
                listId.setDisable(false);
            } else {
                listId.getStyleClass().remove(ERROR_CLASS);
                listId.clear();
                listId.setDisable(true);
                listId.setEditable(false);
            }

            config.put("mode", newValue.id.toString());
        });
        modeChoiceBox.getSelectionModel().selectFirst();

        queryFormatChoiceBox.setItems(FXCollections.observableArrayList(
                new Choice<>(XML, "XML"),
                new Choice<>(JSON, "JSON"),
                new Choice<>(HTML, "HTML"),
                new Choice<>(MIXED, "Смешанный")
        ));
        queryFormatChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            config.put("query_format", newValue.id.toString());
        });
        queryFormatChoiceBox.getSelectionModel().selectFirst();

        listId.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!listId.getText().startsWith("ls") || listId.getText().length() < 3) {
                    listId.getStyleClass().add(ERROR_CLASS);
                } else {
                    listId.getStyleClass().remove(ERROR_CLASS);
                }
            }

            config.put("list", listId.getText());
        });

        authId.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (authId.getText().length() < 10) {
                    authId.getStyleClass().add(ERROR_CLASS);
                } else {
                    authId.getStyleClass().remove(ERROR_CLASS);
                }
            }

            config.put("auth", authId.getText());
        });
    }

    @FXML
    protected void handleFileChoseAction(ActionEvent event) {
        fileChooser.setTitle("Выберите список кинопоиска:");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLS", "*.xls"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectFileBtn.setText("Выбрать другой файл...");
            titleText.setText(file.getPath());
            kpFile = file;
        }
    }

    @FXML
    protected void handleStartAction(ActionEvent event) {
        System.out.println(config);
    }

    private class Choice<K, V> {
        private final K id;
        private final V displayString;

        public Choice(K id, V displayString) {
            this.id = id;
            this.displayString = displayString;
        }

        @Override
        public String toString() {
            return String.valueOf(displayString);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Choice choice = (Choice) o;
            return displayString != null && displayString.equals(choice.displayString) || id != null && id.equals(choice.id);
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (displayString != null ? displayString.hashCode() : 0);
            return result;
        }
    }
}