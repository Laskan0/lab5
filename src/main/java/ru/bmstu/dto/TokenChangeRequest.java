package ru.bmstu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenChangeRequest {
    private String lastName;
    private String firstName;
    private int delta; // изменение количества жетонов (+ или -)
}