package ru.bmstu;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bmstu.aspect.AuditAspect;
import ru.bmstu.service.StudentService;

public class Application {
    public static void main(String[] args) {
        // Создаем Spring контекст
        var context = new AnnotationConfigApplicationContext("ru.bmstu");

        // Получаем сервис из контекста
        StudentService studentService = context.getBean(StudentService.class);

        // Получаем ApplicationRunner и запускаем приложение
        ApplicationRunner runner = context.getBean(ApplicationRunner.class);
        runner.run();
    }
}