package ru.bmstu.repository;
//Реализация интерфейса StudentRepo. Работает с данными в памяти. Загружает данные из файла и выполняет операции
import org.springframework.stereotype.Repository;
import ru.bmstu.model.Student;

import jakarta.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository // Аннотация spring, которая указывает, что это компонент, который управляет данными
public class InMemoryStudentRepository implements StudentRepository {

    private final List<Student> students = new ArrayList<>();
    private static final String CSV_FILE_PATH = "students.csv";

    @PostConstruct//Аннотация, которая говорит, что метод будет вызван автоматически создания бина
    public void init() {
        loadStudentsFromCSV();
    }

    private void loadStudentsFromCSV() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CSV_FILE_PATH);
             //автоматически закрывает ресурс после завершения работы
             //Получаем InputScream к файлу с данными
             //считываем как ресурс

             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))) {
            //
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    students.add(new Student(parts[0], parts[1], Integer.parseInt(parts[2])));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error loading students from CSV", e);
        }
    }

    private void saveStudentsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {


            for (Student student : students) {
                writer.write(student.getFirstName() + "," + student.getLastName() + "," + student.getTokenCount());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving students to CSV", e);
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
        saveStudentsToCSV(); // Сохраняем изменения в CSV
    }

    @Override
    public boolean delete(Student student) {
        boolean deleted = students.removeIf(s -> s.getLastName().equals(student.getLastName())
                && s.getFirstName().equals(student.getFirstName()));
        if (deleted) {
            saveStudentsToCSV(); // Сохраняем изменения в CSV
        }
        return deleted;
    }
}