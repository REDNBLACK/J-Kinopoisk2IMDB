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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.f0w.k2i.core.Client;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.comparator.title.*;
import org.f0w.k2i.core.comparator.year.DeviationYearComparator;
import org.f0w.k2i.core.comparator.year.EqualsYearComparator;
import org.f0w.k2i.core.event.ImportFinishedEvent;
import org.f0w.k2i.core.event.ImportStartedEvent;
import org.f0w.k2i.core.event.ImportProgressAdvancedEvent;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.handler.MovieHandler;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.handler.MovieHandler.Type.*;
import static org.f0w.k2i.core.exchange.finder.MovieFinder.Type.*;

public class Controller {
    private Stage stage;
    private final FileChooser fileChooser = new FileChooser();
    private File kpFile;

    private final File configFile = new File(
            System.getProperty("user.home") + File.separator + "K2IDB" + File.separator + "config.json"
    );

    private final Config config = ConfigFactory.parseFile(configFile)
            .withFallback(ConfigFactory.defaultApplication());

    private final Map<String, Object> configMap = config.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().unwrapped()));

    @FXML
    private Text titleText;

    @FXML
    private ChoiceBox<Choice<MovieHandler.Type, String>> modeChoiceBox;

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
    void initialize() {
        modeChoiceBox.setMaxWidth(300);
        modeChoiceBox.setItems(FXCollections.observableArrayList(
                new Choice<>(MovieHandler.Type.SET_RATING, "Выставить рейтинг"),
                new Choice<>(ADD_TO_WATCHLIST, "Добавить в список"),
                new Choice<>(COMBINED, "Добавить в список и выставить рейтинг")
        ));
        modeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.value.equals(ADD_TO_WATCHLIST) || newValue.value.equals(COMBINED)) {
                listId.setEditable(true);
                listId.setDisable(false);
            } else {
                listId.clear();
                listId.setDisable(true);
                listId.setEditable(false);
            }

            configMap.put("mode", newValue.value.toString());
        });
        modeChoiceBox.getSelectionModel().select(new Choice<>(MovieHandler.Type.valueOf(config.getString("mode"))));

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
        queryFormatChoiceBox.getSelectionModel().select(
            new Choice<>(MovieFinder.Type.valueOf(config.getString("query_format")))
        );

        listId.setMaxWidth(300);
        listId.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            configMap.put("list", listId.getText());
        });
        listId.setText(config.getString("list"));

        authId.setMaxWidth(300);
        authId.focusedProperty().addListener((observable, oldValue, newValue) -> {
            configMap.put("auth", authId.getText());
        });
        authId.setText(config.getString("auth"));

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
        comparatorsBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<Choice<Class<? extends MovieComparator>, String>>) c -> {
            List<String> comparators = c.getList().stream()
                    .map(choice -> choice.value.getName())
                    .collect(Collectors.toList());

            configMap.put("comparators", comparators);
        });
        config.getStringList("comparators").forEach(c -> comparatorsBox.getCheckModel().check(
                new Choice<>(ReflectionUtils.stringToClass(c, MovieComparator.class))
        ));
    }

    void destroy() {
        try {
            Files.write(
                    configFile.toPath(),
                    ConfigFactory.parseMap(configMap)
                            .withFallback(config)
                            .root()
                            .render(ConfigRenderOptions.concise())
                            .getBytes()
            );
        } catch (IOException ignore) {
            // Do nothing
        }
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
            Client client = new Client(kpFile, config);
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
                                    MovieHandler.Error::getMovie,
                                    Collectors.mapping(MovieHandler.Error::getMessage, Collectors.toList())
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

        Choice(K value) {
            this.value = value;
            this.label = null;
        }

        Choice(K value, V label) {
            this.value = value;
            this.label = label;
        }

        @Override
        public String toString() {
            return String.valueOf(label);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Choice<?, ?> choice = (Choice<?, ?>) o;
            return Objects.equals(value, choice.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}