package ru.bmstu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bmstu.context.CurrentUserContext;
import ru.bmstu.dto.TokenChangeRequest;
import ru.bmstu.exception.AccessDeniedException;
import ru.bmstu.model.Student;
import ru.bmstu.service.StudentService;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class ApplicationRunner {

    private final StudentService studentService;
    private final CurrentUserContext currentUserContext;

    @Autowired
    public ApplicationRunner(StudentService studentService, CurrentUserContext currentUserContext) {
        this.studentService = studentService;
        this.currentUserContext = currentUserContext;
    }



    public void run() {
        System.out.println("[DEBUG] App is running");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your firstname : ");
            String userFirstName = reader.readLine();

            System.out.print("Enter your surname: ");
            String userLastName = reader.readLine();

            System.out.print("Enter your role (student/teacher): ");
            String role = reader.readLine().trim().toLowerCase();

            currentUserContext.setCurrentUserRole(role);


            // Общее меню для всех ролей
            while (true) {
                System.out.println("\n[Menu]");
                System.out.println("1 - Look for list of students");
                System.out.println("2 - Add student");
                System.out.println("3 - Change tokens");
                System.out.println("4 - Delete student");
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
                        System.out.println("Wrong choice.");
                }
            }

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private void addStudent(BufferedReader reader) throws Exception {
        try {
            System.out.print("Enter student`s firstname : ");
            String firstName = reader.readLine();

            System.out.print("Enter student`s last name: ");
            String lastName = reader.readLine();

            System.out.print("Enter number of tokens: ");
            int tokenCount = Integer.parseInt(reader.readLine());

            Student student = new Student(lastName, firstName, tokenCount);
            studentService.addStudent(student);
            System.out.println("Student added: " + student);
        } catch (NumberFormatException e) {
            System.err.println("Error: Enter correct number of tokens.");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeTokens(BufferedReader reader) throws Exception {
        try {
            System.out.print("Enter student`s firstname: ");
            String firstName = reader.readLine();

            System.out.print("Enter student`s last name: ");
            String lastName = reader.readLine();

            System.out.print("Enter delta (positive or negative number): ");
            int delta = Integer.parseInt(reader.readLine());

            studentService.changeTokens(new TokenChangeRequest(lastName, firstName, delta));
            System.out.println("Tokens changed .");
        } catch (NumberFormatException e) {
            System.err.println("Error: Enter correct number for delta.");
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeStudent(BufferedReader reader) throws Exception {
        try {
            System.out.print("Enter student`s firstname for deletion: ");
            String firstName = reader.readLine();

            System.out.print("Enter student`s lastname for deletion: ");
            String lastName = reader.readLine();

            Student student = studentService.getStudentByFullName(firstName, lastName);
            if (student != null) {
                studentService.removeStudent(student);
                System.out.println("Student deleted : " + student);
            } else {
                System.out.println("Student not found.");
            }
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
        }
    }
}