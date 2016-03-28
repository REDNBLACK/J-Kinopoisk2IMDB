package org.f0w.k2i.gui;

import com.google.common.eventbus.Subscribe;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.f0w.k2i.core.Client;
import org.f0w.k2i.core.command.MovieCommand;
import org.f0w.k2i.core.event.ImportListInitializedEvent;
import org.f0w.k2i.core.event.ImportListProgressAdvancedEvent;
import org.f0w.k2i.core.exchange.finder.MovieFinder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.f0w.k2i.core.command.MovieCommand.Type.*;
import static org.f0w.k2i.core.exchange.finder.MovieFinder.Type.*;

public class Controller {
    private Stage stage;
    private final FileChooser fileChooser = new FileChooser();
    private File kpFile;
    private final Map<String, Object> configMap = new HashMap<>();

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

    @FXML
    private javafx.scene.control.ProgressBar progressBar;

    void init(Stage stage) {
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
            if (newValue.value.equals(ADD_TO_WATCHLIST) || newValue.value.equals(COMBINED)) {
                listId.setEditable(true);
                listId.setDisable(false);
            } else {
                listId.getStyleClass().remove(ERROR_CLASS);
                listId.clear();
                listId.setDisable(true);
                listId.setEditable(false);
            }

            configMap.put("mode", newValue.value.toString());
        });
        modeChoiceBox.getSelectionModel().selectFirst();

        queryFormatChoiceBox.setItems(FXCollections.observableArrayList(
                new Choice<>(XML, "XML"),
                new Choice<>(JSON, "JSON"),
                new Choice<>(HTML, "HTML"),
                new Choice<>(MIXED, "Смешанный")
        ));
        queryFormatChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            configMap.put("query_format", newValue.value.toString());
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

            configMap.put("list", listId.getText());
        });

        authId.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (authId.getText().length() < 10) {
                    authId.getStyleClass().add(ERROR_CLASS);
                } else {
                    authId.getStyleClass().remove(ERROR_CLASS);
                }
            }

            configMap.put("auth", authId.getText());
        });

        progressBar.setMaxWidth(Double.MAX_VALUE);
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
        try {
            Client client = new Client(kpFile, ConfigFactory.parseMap(configMap));
            client.registerListener(new ProgressListener());

            ExecutorService service = Executors.newSingleThreadExecutor();
            service.submit(client::run);
            service.shutdown();
        } catch (IllegalArgumentException|NullPointerException|ConfigException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Произошла ошибка");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    private class ProgressListener {
        final AtomicInteger max = new AtomicInteger(0);
        final AtomicInteger current = new AtomicInteger(0);

        @Subscribe
        public void handleProgressSetUpEvent(ImportListInitializedEvent event) {
            max.set(event.listSize);
        }

        @Subscribe
        public void handleProgressAdvanceEvent(ImportListProgressAdvancedEvent event) {
            int maximum = max.get();
            int cur = current.incrementAndGet();

            progressBar.setProgress((cur * 100 / maximum) * 0.01);
        }
    }

    private class Choice<K, V> {
        private final K value;
        private final V label;

        public Choice(K value, V label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String toString() {
            return String.valueOf(label);
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
            return label != null && label.equals(choice.label) || value != null && value.equals(choice.value);
        }

        @Override
        public int hashCode() {
            int result = value != null ? value.hashCode() : 0;
            result = 31 * result + (label != null ? label.hashCode() : 0);
            return result;
        }
    }
}