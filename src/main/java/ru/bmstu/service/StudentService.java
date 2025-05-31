package ru.bmstu.service;

import ru.bmstu.dto.TokenChangeRequest;
import ru.bmstu.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentByFullName(String lastName, String firstName);
    void addStudent(Student student);
    void changeTokens(TokenChangeRequest request);
    void removeStudent(Student student);
}