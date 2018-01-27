package sample;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.Data;
import sample.model.DataStorage;


/**
 * Класс контроллер
 */
public class SampleController {

    /**
     * Экземпляр класса хранилища который получает доступ к базе данных
     */
    DataStorage storage = new DataStorage();

    /**
     * Поле ввода добавления данных в базу
     */
    @FXML
    private TextField inField;

    /**
     * Кнопка отвечающая за добаление строки в базу данных
     */
    @FXML
    private Button addBt;

    /**
     * Поле поиска в баззе данных
     */
    @FXML
    private TextField searchField;

    /**
     * Кнопка показывающая все сожержимое базы данных
     */
    @FXML
    private Button searchBt;

    /**
     * Таблица в которой отображаются полученные данные
     */
    @FXML
    private TableView<Data> mainTable;

    /**
     * Столбец отвечающий за вывод id
     */
    @FXML
    private TableColumn<Data, Integer> idColumn;

    /**
     * Столбец отвечающий за вывод name
     */
    @FXML
    private TableColumn<Data, String> nameColumn;

    /**
     * метод добавляющий введенное значение в базу данных
     */
    @FXML
    void addName() {
        // Получает строку из поля ввода
        String name = inField.getText();
        if (name.equals("")) {
            // добавления не происходит если поле пустое, просто выводит результат
            showResult("Поле имени пустое. Повторите ввод");
        } else if (storage.addName(name)) {
            // если строка успешно добавлена выводит результат
            showResult("Пользователь " + name + " успешно добавлен");
        }
        // иначе если добавить не удалось
        else {

            showResult("Пользователь не был добавлен");
        }
        // очищает поле ввода
        inField.setText("");
    }

    /**
     * Выводит список всей базы данных
     */
    @FXML
    void findAll() {
        // Очищает полу ввода
        inField.setText("");
        // поле поиска
        searchField.setText("");
        // получаем список всех данных из базы
        ObservableList<Data> list = storage.values();
        // устанавливаем какой столбец за какие данные отвечает
        // id
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        // name
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        // устанавливаем данные в таблицу
        mainTable.setItems(list);
    }

    /**
     * Метод находит пользователя по id  или name
     */
    @FXML
    void find() {
        // Отсекает от строки лишние пробелы
        String name = searchField.getText().trim();
        // заполняем лист данныеми из базы, если удалось найти соответсвующие запросу
        ObservableList<Data> list = storage.find(name);
        // Если лист пуст
        if (list.isEmpty()) {
            showResult("Пользователя нет в базе данных");
        }
        // Иначе вновь устанавливаем данные для столбцов, заполняем таблицу
        else {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            mainTable.setItems(list);
        }
        // очищаем поле поиска.
        searchField.setText("");
    }

    /**
     * Метод отображает результат в специальном модальном окне
     * @param message сообщение котрое нужно отобразить
     */
    private void showResult(String message) {
        // Новая сцена
        Stage stage = new Stage();
        // Делаем ее модальной
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Сообщение");
        // Размеры
        stage.setMinWidth(250);
        stage.setMinHeight(150);
        stage.setResizable(false);

        Label label = new Label(message);
        Button button = new Button("OK");
        // Действие при нажатии на кнопку
        button.setOnAction(event -> stage.close());


        VBox layout = new VBox(20);
        // Добавляем лэйбл и кнопку
        layout.getChildren().addAll(label, button);
        layout.setAlignment(Pos.CENTER);

        // устанавливаем на главную сцена
        stage.setScene(new Scene(layout));
        // Отображаем
        stage.show();
    }
}

