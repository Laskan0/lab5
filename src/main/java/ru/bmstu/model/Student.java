package ru.bmstu.model;
//модельная сущность(domain model) - объект предаставляющий студента. Хранит данные о студенте.
public class Student {
    private String lastName;
    private String firstName;
    private int tokenCount;

    public Student(String lastName, String firstName, int tokenCount) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.tokenCount = tokenCount;
    }

    // Геттеры
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getTokenCount() {
        return tokenCount;
    }

    // Сеттер
    public void setTokenCount(int tokenCount) {
        this.tokenCount = tokenCount;
    }

    // Для удобного вывода в консоль
    @Override
    public String toString() {
        return "Имя: " + firstName + " " + lastName + "\n" + "Жетоны: " + tokenCount;
    }
}