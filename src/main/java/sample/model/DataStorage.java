package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Классс отвечает за соединение с базой данных MySQL
 */
public class DataStorage {

    // Поле соединения к базе данные
    private Connection connection;

    /**
     * Конструкор в котором происходит подключение к базе данных
     */
    public DataStorage() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cdek_schema", "root", "root");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Класс получает все данные из базы и сохраняет из в лист
     * @return лист данных
     */
    public ObservableList<Data> values() {
        // экземляр листа для обектов Data
        ObservableList<Data> storage = FXCollections.observableArrayList();
        try (PreparedStatement st = connection.prepareStatement("SELECT * FROM cdek")) {
            // получаем сет
            ResultSet set = st.executeQuery();
            // перебираем сет и добавляем данные в лист
            while (set.next()) {
                storage.add(new Data(set.getInt(1), set.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storage;
    }

    /**
     * Метод добавляет данные в таблицу
     * @param name строка которую нужно добавить
     * @return возвращает true если строка добавлена  и false если нет
     */
    public boolean addName(String name) {
        boolean isAdd = false;
        try (PreparedStatement st = connection.prepareStatement("INSERT INTO cdek (name) values (?)")) {
            st.setString(1, name);
            st.executeUpdate();
            // добавление прошло удачно
            isAdd = true;
        } catch (SQLException e) {
            // неудачно
            e.printStackTrace();
        }
        return isAdd;
    }

    /**
     * Ищет данные в базе и возвращает лист найденных данных.
     * @param name строка содержащаю данные которые нужно найти
     * @return вовращает лист Data.  Лист содержит 1 или 0 элементов если поиск происходил по id,
     * так как совпадений id  в базе нет (столбец отмечен как auto increment). Лист содержит количество совпадений
     * если поиск проиводился по имени.
     */
    public ObservableList<Data> find(String name) {
        ObservableList<Data> storage = FXCollections.observableArrayList();
        // если строка является числом  - поиск по id
        if (isInt(name)) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * from cdek WHERE id=(?)")) {
                statement.setInt(1, Integer.parseInt(name));
                ResultSet  set = statement.executeQuery();
                while (set.next()) {
                    storage.add(new Data(set.getInt(1), set.getString(2)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // если строка является строкой поиск по имени.
        else {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM cdek WHERE name=(?)")) {
                statement.setString(1, name);
                ResultSet  set = statement.executeQuery();
                while (set.next()) {
                    storage.add(new Data(set.getInt(1), set.getString(2)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return storage;
    }

    /**
     * Проверяет есляется или строка числом
     * @param name строка которую нужно проверить
     * @return возвращает true если строка  содержит только числа,  false - если строка не число.
     */
    private boolean isInt(String name) {
        try {
            // преобразовываем строку в число
            Integer.parseInt(name);
        } catch (NumberFormatException e) {
            // если не приобразовывается
            return false;
        }
        // преобразовывается
        return true;
    }
}
