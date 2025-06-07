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
    private static final String CSV_FILE_PATH = "data/students.csv";

    @PostConstruct
    public void init() {
        loadStudentsFromCSV();
    }

    // Чтение из файла в classpath (src/main/resources)
    private void loadStudentsFromCSV() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(CSV_FILE_PATH)),
                        StandardCharsets.UTF_8
                )
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    students.add(new Student(parts[0], parts[1], Integer.parseInt(parts[2])));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("SCV loading failed", e);
        }
    }

    private void saveStudentsToCSV() {
        File file = new File(
                Objects.requireNonNull(getClass().getClassLoader().getResource(CSV_FILE_PATH)).getFile()
        );

        // Создаем файл, если его нет
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // Создаем директории, если нужно
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Cannot create file " + CSV_FILE_PATH, e);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Student student : students) {
                writer.write(student.getFirstName() + "," + student.getLastName() + "," + student.getTokenCount());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("CSV saving error", e);
        }
    }

    @Override
    public List<Student> findAll() {
        return List.copyOf(students);
    }

    @Override
    public Student findByFullName(String lastName, String firstName) {
        return students.stream()
                .filter(s -> s.getLastName().equalsIgnoreCase(lastName) &&
                        s.getFirstName().equalsIgnoreCase(firstName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Student student) {
        students.removeIf(s ->
                s.getLastName().equalsIgnoreCase(student.getLastName()) &&
                        s.getFirstName().equalsIgnoreCase(student.getFirstName())
        );
        students.add(student);
        saveStudentsToCSV();
    }

    @Override
    public boolean delete(Student student) {
        boolean deleted = students.removeIf(s ->
                s.getLastName().equalsIgnoreCase(student.getLastName()) &&
                        s.getFirstName().equalsIgnoreCase(student.getFirstName())
        );
        if (deleted) {
            saveStudentsToCSV();
        }
        return deleted;
    }
}