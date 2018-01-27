package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Класс отвечает за хранение данных из базы данных
 */
public class Data {

    // id полученное из базы данных
    private SimpleIntegerProperty id;

    // name из базы
    private SimpleStringProperty name;

    // Конструктор
    public Data(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    // дефолтный конструктор
    public Data() {
    }

    // далее гетерры и сеттеры для полей
    public int getId() {
        return this.id.get();
    }

    public String getName() {
        return this.name.get();
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }
}
