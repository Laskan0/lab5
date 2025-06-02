package ru.bmstu.dto;//DTO - data transfer object
//Объект не содержит бизнес-логики и не зависит от фреймворка.
// Нужен только для передачи данных между слоями предложения
//Бины и spring не нужны

public class TokenChangeRequest {
    private String lastName;
    private String firstName;
    private int delta;

    public TokenChangeRequest(String lastName, String firstName, int delta) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.delta = delta;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getDelta() {
        return delta;
    }
}