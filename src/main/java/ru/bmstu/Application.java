package ru.bmstu;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bmstu.service.StudentService;

public class Application {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext("ru.bmstu");
        StudentService studentService = context.getBean(StudentService.class);
        ApplicationRunner runner = context.getBean(ApplicationRunner.class);
        runner.run();
    }
}