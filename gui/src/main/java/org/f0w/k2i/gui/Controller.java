package org.f0w.k2i.gui;

import com.google.common.eventbus.Subscribe;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.CheckComboBox;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.event.ImportFinishedEvent;
import org.f0w.k2i.core.event.ImportProgressAdvancedEvent;
import org.f0w.k2i.core.event.ImportStartedEvent;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.comparator.MovieComparator.Type.*;
import static org.f0w.k2i.core.exchange.finder.MovieFinder.Type.*;
import static org.f0w.k2i.core.handler.MovieHandler.Type.*;

public class Controller {
    private Stage stage;
    private ClientExecutor clientExecutor = new ClientExecutor();

    private final Path configPath = Paths.get(System.getProperty("user.home"), "K2IDB", "config.json");

    private final Config config = ConfigFactory.parseFile(configPath.toFile())
            .withFallback(ConfigFactory.defaultApplication());

    private final Map<String, Object> configMap = config.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().unwrapped()));

    @FXML
    private ComboBox<Choice<MovieHandler.Type, String>> modeComboBox;

    @FXML
    private ComboBox<Choice<MovieFinder.Type, String>> queryFormatComboBox;

    @FXML
    private Label selectedFile;

    @FXML
    private TextField authId;

    @FXML
    private TextField listId;

    @FXML
    private Button selectFileBtn;

    @FXML
    private CheckBox cleanRunCheckbox;

    @FXML
    private Button runBtn;

    @FXML
    private Label progressStatus;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private CheckComboBox<Choice<MovieComparator.Type, String>> comparatorsBox;

    @FXML
    private TextField userAgentField;

    @FXML
    private TextField yearDeviationField;

    @FXML
    private TextField timeoutField;

    @FXML
    private TextField logLevelField;

    @FXML
    void initialize() {
        // Основные
        modeComboBox.setItems(FXCollections.observableList(Arrays.asList(
                new Choice<>(COMBINED, "Добавить в список и выставить рейтинг"),
                new Choice<>(SET_RATING, "Выставить рейтинг"),
                new Choice<>(ADD_TO_WATCHLIST, "Добавить в список")
        )));
        modeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getValue().equals(ADD_TO_WATCHLIST) || newValue.getValue().equals(COMBINED)) {
                listId.setEditable(true);
                listId.setDisable(false);
            } else {
                listId.clear();
                listId.setDisable(true);
                listId.setEditable(false);
            }

            configMap.put("mode", newValue.getValue().toString());
        });
        modeComboBox.getSelectionModel().select(new Choice<>(MovieHandler.Type.valueOf(config.getString("mode"))));

        authId.focusedProperty().addListener(o -> configMap.put("auth", authId.getText()));
        authId.setText(config.getString("auth"));

        listId.focusedProperty().addListener(o -> configMap.put("list", listId.getText()));
        listId.setText(config.getString("list"));


        // Дополнительные
        queryFormatComboBox.setItems(FXCollections.observableList(Arrays.asList(
                new Choice<>(XML, "XML"),
                new Choice<>(JSON, "JSON"),
                new Choice<>(HTML, "HTML"),
                new Choice<>(MIXED, "Смешанный")
        )));
        queryFormatComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            configMap.put("query_format", newValue.getValue().toString());
        });
        queryFormatComboBox.getSelectionModel().select(
                new Choice<>(MovieFinder.Type.valueOf(config.getString("query_format")))
        );

        comparatorsBox.getItems().addAll(FXCollections.observableList(Arrays.asList(
                new Choice<>(YEAR_DEVIATION, "Год с отклонением"),
                new Choice<>(YEAR_EQUALS, "Год с полным совпадением"),
                new Choice<>(TITLE_SMART, "Интеллектуальное сравнение названий"),
                new Choice<>(TITLE_EQUALS, "Полное совпадение названий"),
                new Choice<>(TITLE_CONTAINS, "Одно название содержит другое"),
                new Choice<>(TITLE_STARTS, "Одно название начинается с другого"),
                new Choice<>(TITLE_ENDS, "Одно название оканчивается другим")
        )));
        comparatorsBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<Choice<MovieComparator.Type, String>>) c -> {
            List<String> comparators = c.getList().stream()
                    .map(choice -> choice.getValue().toString())
                    .collect(Collectors.toList());

            configMap.put("comparators", comparators);
        });
        config.getStringList("comparators").forEach(c -> comparatorsBox.getCheckModel().check(
                new Choice<>(MovieComparator.Type.valueOf(c))
        ));

        cleanRunCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            clientExecutor.setCleanRun(newValue);
        });


        // Для экспертов
        userAgentField.focusedProperty().addListener(o -> configMap.put("user_agent", userAgentField.getText()));
        userAgentField.setText(config.getString("user_agent"));

        yearDeviationField.focusedProperty()
                .addListener(o -> configMap.put("year_deviation", yearDeviationField.getText()));
        yearDeviationField.setText(config.getString("year_deviation"));

        timeoutField.focusedProperty().addListener(o -> configMap.put("timeout", timeoutField.getText()));
        timeoutField.setText(config.getString("timeout"));

        logLevelField.focusedProperty().addListener(o -> configMap.put("log_level", logLevelField.getText()));
        logLevelField.setText(config.getString("log_level"));
    }

    boolean destroy() {
        if (clientExecutor.isRunning()) {
            if (!confirmStop()) {
                return false;
            }

            clientExecutor.terminate();
        }

        byte[] configuration = ConfigFactory.parseMap(configMap)
                .withFallback(config)
                .root()
                .render(ConfigRenderOptions.concise())
                .getBytes();

        try {
            Path parentDir = configPath.getParent();
            if (!Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            Files.write(configPath, configuration);
        } catch (IOException ignore) {
            // Do nothing
        }

        return true;
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void handleFileChoseAction(ActionEvent event) {
        FileChooser choser = new FileChooser();
        choser.setTitle("Выберите список кинопоиска:");
        choser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLS", "*.xls"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        File file = choser.showOpenDialog(stage);

        if (file != null) {
            selectFileBtn.setText("Выбрать другой файл...");
            selectedFile.setText(file.getPath());
            clientExecutor.setFilePath(file.toPath());
            runBtn.setDisable(false);
        }
    }

    @FXML
    protected void handleStartAction(ActionEvent event) {
        try {
            clientExecutor.setListeners(Arrays.asList(new ProgressBarUpdater(), new RunButtonUpdater()));
            clientExecutor.setConfig(configMap);
            clientExecutor.run();
        } catch (IllegalArgumentException | NullPointerException | ConfigException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Произошла ошибка");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    private void handleStopAction(ActionEvent event) {
        if (clientExecutor.isRunning() && confirmStop()) {
            clientExecutor.terminate();
        }
    }

    private boolean confirmStop() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Потдверждение");
        alert.setHeaderText("Подтверждение остановки обработки фильмов");
        alert.setContentText("Вы уверены что хотите остановить обработку фильмов?");

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private class ProgressBarUpdater {
        private final AtomicInteger max = new AtomicInteger(0);
        private final AtomicInteger current = new AtomicInteger(0);
        private final AtomicInteger failed = new AtomicInteger(0);
        private final AtomicInteger successful = new AtomicInteger(0);

        @Subscribe
        public void handleStart(ImportStartedEvent event) {
            max.set(event.listSize);

            Platform.runLater(() -> {
                progressStatus.setText("0/" + max.get());
            });
        }

        @Subscribe
        public void handleAdvance(ImportProgressAdvancedEvent event) {
            int maximum = max.get();
            int cur = current.incrementAndGet();
            if (event.successful) {
                successful.incrementAndGet();
            } else {
                failed.incrementAndGet();
            }

            progressBar.setProgress((cur * 100 / maximum) * 0.01);
            Platform.runLater(() -> progressStatus.setText(cur + "/" + maximum));
        }

        @Subscribe
        public void handleEnd(ImportFinishedEvent event) {
            Platform.runLater(() -> showResultDialog(event.errors, max.get(), successful.get(), failed.get()));
        }

        private void showResultDialog(List<MovieHandler.Error> errors, int maximum, int successful, int failed) {
            Alert alert = new Alert(Alert.AlertType.NONE);

            if (errors.isEmpty()) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Обработка успешно завершена");
                alert.setHeaderText("Обработка фильмов была успешно завершена.");

                if (successful == maximum) {
                    alert.setContentText("Были обработаны все " + maximum + " фильмов, без ошибок");
                } else {
                    alert.setContentText("Были обработаны " + successful + " из " + maximum + " фильмов, без ошибок");
                }
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setTitle("Обработка завершена c ошибками");
                alert.setHeaderText("Обработка фильмов была завершена с ошибками.");
                alert.setContentText("Были обработаны " + successful + " из " + maximum + " фильмов, без ошибок");

                // Create expandable Exception.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);

                errors.stream()
                        .collect(Collectors.groupingBy(
                                MovieHandler.Error::getMovie,
                                Collectors.mapping(MovieHandler.Error::getMessage, Collectors.toList())
                        ))
                        .entrySet()
                        .forEach(e -> {
                            Movie movie = e.getKey();

                            pw.println(movie.getTitle() + "(" + movie.getYear() + "):");

                            e.getValue().forEach(pw::println);

                            pw.println();
                            pw.println();
                        });

                String exceptionText = sw.toString();

                Label label = new Label("Произошли ошибки с " + failed + " фильмами:");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                alert.getDialogPane().setExpandableContent(expContent);
            }

            alert.showAndWait();
        }
    }

    private class RunButtonUpdater {
        @Subscribe
        public void handleStart(ImportStartedEvent event) {
            Platform.runLater(() -> {
                runBtn.setText("Остановить");
                runBtn.getStyleClass().remove("primary");
                runBtn.getStyleClass().add("danger");
                runBtn.setOnAction(Controller.this::handleStopAction);
            });
        }

        @Subscribe
        public void handleEnd(ImportFinishedEvent event) {
            Platform.runLater(() -> {
                runBtn.setText("Запустить");
                runBtn.getStyleClass().remove("danger");
                runBtn.getStyleClass().add("primary");
                runBtn.setOnAction(Controller.this::handleStartAction);
            });
        }
    }
}