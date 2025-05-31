package ru.bmstu.repository;

import org.springframework.stereotype.Repository;
import ru.bmstu.model.Student;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryStudentRepository implements StudentRepository {

    private final List<Student> students = new ArrayList<>();

    // Загружаем CSV как ресурс (из папки resources)
    @PostConstruct
    public void init() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("students.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    students.add(new Student(parts[0], parts[1], Integer.parseInt(parts[2])));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке данных студентов", e);
        }
    }

    @Override
    public List<Student> findAll() {
        return List.copyOf(students);
    }

    @Override
    public Student findByFullName(String lastName, String firstName) {
        return students.stream()
                .filter(s -> s.getLastName().equals(lastName) && s.getFirstName().equals(firstName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Student student) {
        students.removeIf(s -> s.getLastName().equals(student.getLastName())
                && s.getFirstName().equals(student.getFirstName()));
        students.add(student);
    }

    @Override
    public boolean delete(Student student) {
        return students.removeIf(s -> s.getLastName().equals(student.getLastName())
                && s.getFirstName().equals(student.getFirstName()));
    }
}