package org.f0w.k2i.gui;

import com.google.common.eventbus.Subscribe;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.f0w.k2i.core.Client;
import org.f0w.k2i.core.command.MovieCommand;
import org.f0w.k2i.core.command.MovieError;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.comparator.title.*;
import org.f0w.k2i.core.comparator.year.DeviationYearComparator;
import org.f0w.k2i.core.comparator.year.EqualsYearComparator;
import org.f0w.k2i.core.event.ImportFinishedEvent;
import org.f0w.k2i.core.event.ImportStartedEvent;
import org.f0w.k2i.core.event.ImportProgressAdvancedEvent;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private Button startBtn;

    @FXML
    private javafx.scene.control.ProgressBar progressBar;

    @FXML
    private CheckComboBox<Choice<Class<? extends MovieComparator>, String>> comparatorsBox;

    void init(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        modeChoiceBox.setMaxWidth(300);
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

        queryFormatChoiceBox.setMaxWidth(300);
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

        listId.setMaxWidth(300);
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

        authId.setMaxWidth(300);
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

        comparatorsBox.setMaxWidth(300);
        comparatorsBox.getItems().addAll(FXCollections.observableArrayList(
                new Choice<>(DeviationYearComparator.class, "Год с отклонением"),
                new Choice<>(EqualsYearComparator.class, "Год с полным совпадением"),
                new Choice<>(SmartTitleComparator.class, "Интеллектуальное сравнение названий"),
                new Choice<>(EqualsTitleComparator.class, "Полное совпадение названий"),
                new Choice<>(ContainsTitleComparator.class, "Одно название содержит другое"),
                new Choice<>(StartsWithTitleComparator.class, "Одно название начинается с другого"),
                new Choice<>(EndsWithTitleComparator.class, "Одно название оканчивается другим")
        ));
        comparatorsBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<Choice<Class<? extends MovieComparator>, String>>() {
            @Override
            public void onChanged(Change<? extends Choice<Class<? extends MovieComparator>, String>> c) {
                List<String> comparators = c.getList().stream()
                        .map(choice -> choice.value.getName())
                        .collect(Collectors.toList());

                configMap.put("comparators", comparators);
            }
        });
        comparatorsBox.getCheckModel().check(0);
        comparatorsBox.getCheckModel().check(2);
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
            progressBar.setProgress(0.0);
            startBtn.setText("Запустить");
        }
    }

    @FXML
    protected void handleStartAction(ActionEvent event) {
        progressBar.setProgress(0.0);

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
        private final AtomicInteger max = new AtomicInteger(0);
        private final AtomicInteger current = new AtomicInteger(0);
        private final AtomicInteger successfulCount = new AtomicInteger(0);

        @Subscribe
        public void handleStart(ImportStartedEvent event) {
            max.set(event.listSize);

            Platform.runLater(() -> {
                startBtn.setText("В процессе...");
                startBtn.setDisable(true);
            });
        }

        @Subscribe
        public void handleAdvance(ImportProgressAdvancedEvent event) {
            int maximum = max.get();
            int cur = current.incrementAndGet();
            if (event.successful) {
                successfulCount.incrementAndGet();
            }

            progressBar.setProgress((cur * 100 / maximum) * 0.01);
        }

        @Subscribe
        public void handleEnd(ImportFinishedEvent event) {
            Platform.runLater(() -> {
                startBtn.setText("Запустить заново");
                startBtn.setDisable(false);

                Alert alert = new Alert(Alert.AlertType.NONE);

                if (event.errors.isEmpty()) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Обработка успешно завершена");
                    alert.setHeaderText("Обработка фильмов была успешно завершена.");
                    alert.setContentText("Были обработаны все " + max.get() + " фильмов, без ошибок");
                } else {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setTitle("Обработка завершена c ошибками");
                    alert.setHeaderText("Обработка фильмов была завершена с ошибками.");

                    alert.setContentText(
                            "Было обработаны " + successfulCount.get() + " из " + max.get() + " фильмов, без ошибок"
                    );

                    // Create expandable Exception.
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);

                    Map<Movie, List<String>> errors = event.errors.stream()
                            .collect(Collectors.groupingBy(
                                    MovieError::getMovie,
                                    Collectors.mapping(MovieError::getError, Collectors.toList())
                            ));

                    errors.entrySet().forEach(e -> {
                        Movie movie = e.getKey();

                        pw.println(movie.getTitle() + "(" + movie.getYear() + "):");

                        e.getValue().forEach(pw::println);

                        pw.println();
                        pw.println();
                    });

                    String exceptionText = sw.toString();

                    Label label = new Label("Произошли ошибки с " + (max.get() - successfulCount.get()) + " фильмами:");

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
            });
        }
    }

    private class Choice<K, V> {
        final K value;
        final V label;

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