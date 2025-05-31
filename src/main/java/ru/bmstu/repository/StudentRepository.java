package ru.bmstu.repository;

import ru.bmstu.model.Student;

import java.util.List;

public interface StudentRepository {
    List<Student> findAll();
    Student findByFullName(String lastName, String firstName);
    void save(Student student);
    boolean delete(Student student);
}