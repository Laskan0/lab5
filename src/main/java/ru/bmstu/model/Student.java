package ru.bmstu.model;

import lombok.Data;

@Data
public class Student {
    private String lastName;
    private String firstName;
    private int tokenCount;

    // Конструктор с тремя параметрами
    public Student(String lastName, String firstName, int tokenCount) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.tokenCount = tokenCount;
    }
}