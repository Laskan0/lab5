package ru.bmstu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bmstu.context.CurrentUserContext;
import ru.bmstu.dto.TokenChangeRequest;
import ru.bmstu.model.Student;
import ru.bmstu.service.StudentService;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class ApplicationRunner {

    private final StudentService studentService;
    private final CurrentUserContext currentUserContext;

    @Autowired // говорит что зависимости StudentService и CurrentUserContext должны быть внедрены в конструктор
    //Spring умничка и сам найдет нужные реализации сервисов и контекста и передаст в класс
    public ApplicationRunner(StudentService studentService, CurrentUserContext currentUserContext) {
        this.studentService = studentService;
        this.currentUserContext = currentUserContext;
    }

    @PostConstruct // Хотим чтобы приложение сразу начало работать после старта spring контекста
    public void run() {
        System.out.println("[DEBUG] InMemoryStudentRepository initialization started");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your first name: ");
            String userFirstName = reader.readLine();

            System.out.print("Enter your last name: ");
            String userLastName = reader.readLine();

            System.out.print("Enter role (student/teacher): ");
            String role = reader.readLine().trim().toLowerCase();

            // Сохраняем роль в контексте
            currentUserContext.setCurrentUserRole(role);

            if ("student".equals(role)) {
                handleStudent();
            } else if ("teacher".equals(role)) {
                handleTeacher(reader);
            } else {
                System.out.println("Unknown role.");
            }

        } catch (Exception e) {
            System.err.println("Error during execution: " + e.getMessage());
        }
    }

    private void handleStudent() {
        System.out.println("\n[Student Mode]");
        System.out.println("You can only view the list of students.\n");
        studentService.getAllStudents().forEach(System.out::println);
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
                    studentService.getAllStudents().forEach(System.out::println);
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
        while (true) {
            try {
                System.out.print("Enter student's first name: ");
                String firstName = reader.readLine();

                System.out.print("Enter student's last name: ");
                String lastName = reader.readLine();

                System.out.print("Enter token count: ");
                int tokenCount = Integer.parseInt(reader.readLine());

                Student student = new Student(lastName, firstName, tokenCount);
                studentService.addStudent(student);
                System.out.println("Student added: " + student);
                break; // Выходим из цикла при успешном вводе
            } catch (NumberFormatException e) {
                System.err.println("\nWrong input: Please enter a valid number for token count.");
                continue; // Повторяем ввод
            } catch (Exception e) {
                System.err.println("\nError during execution: " + e.getMessage());
                break; // Выходим из цикла при других ошибках
            }
        }
    }

    private void changeTokens(BufferedReader reader) throws Exception {
        while (true) {
            try {
                System.out.print("Enter student's first name: ");
                String firstName = reader.readLine();

                System.out.print("Enter student's last name: ");
                String lastName = reader.readLine();

                System.out.print("Enter delta (positive or negative): ");
                int delta = Integer.parseInt(reader.readLine());

                studentService.changeTokens(new TokenChangeRequest(lastName, firstName, delta));
                System.out.println("Tokens changed.");
                break; // Выходим из цикла при успешном вводе
            } catch (NumberFormatException e) {

                System.err.println("\nWrong input: Please enter a valid number for delta.");
                continue; // Повторяем ввод
            } catch (Exception e) {
                System.err.println("\nError during execution: " + e.getMessage());
                break; // Выходим из цикла при других ошибках
            }
        }
    }

    private void removeStudent(BufferedReader reader) throws Exception {
        System.out.print("Enter student's first name to remove: ");
        String firstName = reader.readLine();
        System.out.print("Enter student's last name to remove: ");
        String lastName = reader.readLine();

        Student student = studentService.getStudentByFullName(firstName, lastName);
        if (student != null) {
            studentService.removeStudent(student);
            System.out.println("Student removed: " + student);
        } else {
            System.out.println("Student not found.");
        }
    }
}