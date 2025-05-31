package ru.bmstu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bmstu.dto.TokenChangeRequest;
import ru.bmstu.model.Student;
import ru.bmstu.service.StudentService;


import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class ApplicationRunner {

    private final StudentService studentService;

    @Autowired
    public ApplicationRunner(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostConstruct
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your first name: ");
            String userFirstName = reader.readLine();

            System.out.print("Enter your last name: ");
            String userLastName = reader.readLine();

            System.out.print("Enter role (student/teacher): ");
            String role = reader.readLine().trim().toLowerCase();

            if ("student".equals(role)) {
                handleStudent(userFirstName, userLastName, reader);
            } else if ("teacher".equals(role)) {
                handleTeacher(reader);
            } else {
                System.out.println("Unknown role.");
            }

        } catch (Exception e) {
            System.err.println("Error during execution: " + e.getMessage());
        }
    }

    private void handleStudent(String firstName, String lastName, BufferedReader reader) throws Exception {
        System.out.println("\n[Student Mode]");
        Student student = studentService.getStudentByFullName(firstName, lastName);
        if (student != null) {
            System.out.println("Your info: " + student);
        } else {
            System.out.println("Student not found.");
        }
    }

    private void handleTeacher(BufferedReader reader) throws Exception {
        while (true) {
            System.out.println("\n[Teacher Mode]");
            System.out.println("Choose an action:");
            System.out.println("1 - View all students");
            System.out.println("2 - Add student");
            System.out.println("3 - Change tokens");
            System.out.println("4 - Remove student");
            System.out.println("0 - Exit");
            System.out.print("Your choice: ");

            String choice = reader.readLine();

            switch (choice) {
                case "1":
                    List<Student> students = studentService.getAllStudents();
                    students.forEach(System.out::println);
                    break;
                case "2":
                    addStudent(reader);
                    break;
                case "3":
                    changeTokens(reader);
                    break;
                case "4":
                    removeStudent(reader);
                    break;
                case "0":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addStudent(BufferedReader reader) throws Exception {
        System.out.print("Enter student's first name: ");
        String firstName = reader.readLine();
        System.out.print("Enter student's last name: ");
        String lastName = reader.readLine();
        System.out.print("Enter token count: ");
        int tokenCount = Integer.parseInt(reader.readLine());

        Student student = new Student(lastName, firstName, tokenCount);
        studentService.addStudent(student);
        System.out.println("Student added: " + student);
    }

    private void changeTokens(BufferedReader reader) throws Exception {
        System.out.print("Enter student's first name: ");
        String firstName = reader.readLine();
        System.out.print("Enter student's last name: ");
        String lastName = reader.readLine();
        System.out.print("Enter delta (positive or negative): ");
        int delta = Integer.parseInt(reader.readLine());

        TokenChangeRequest request = new TokenChangeRequest(lastName, firstName, delta);
        studentService.changeTokens(request);
        System.out.println("Tokens changed: " + request);
    }

    private void removeStudent(BufferedReader reader) throws Exception {
        System.out.print("Enter student's first name to remove: ");
        String firstName = reader.readLine();
        System.out.print("Enter student's last name to remove: ");
        String lastName = reader.readLine();

        Student student = studentService.getStudentByFullName(lastName, firstName);
        if (student != null) {
            studentService.removeStudent(student);
            System.out.println("Student removed: " + student);
        } else {
            System.out.println("Student not found.");
        }
    }
}