package ru.bmstu.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.bmstu.dto.TokenChangeRequest;
import ru.bmstu.model.Student; // Импорт класса Student
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class AuditAspect {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Логируем все изменения токенов
    @Before("execution(* ru.bmstu.service.impl.StudentServiceImpl.changeTokens(..)) && args(request)")
    public void logTokenChange(JoinPoint joinPoint, TokenChangeRequest request) {
        String methodName = joinPoint.getSignature().getName();
        System.out.printf("[%s] [INFO] Преподаватель изменил количество жетонов: %s%n",
                LocalDateTime.now().format(formatter), request);
    }

    // Логируем добавление студента
    @Before("execution(* ru.bmstu.service.impl.StudentServiceImpl.addStudent(..)) && args(student)")
    public void logAddStudent(JoinPoint joinPoint, Student student) {
        System.out.printf("[%s] [INFO] Преподаватель добавил студента: %s%n",
                LocalDateTime.now().format(formatter), student);
    }

    // Логируем удаление студента
    @Before("execution(* ru.bmstu.service.impl.StudentServiceImpl.removeStudent(..)) && args(student)")
    public void logRemoveStudent(JoinPoint joinPoint, Student student) {
        System.out.printf("[%s] [INFO] Преподаватель удалил студента: %s%n",
                LocalDateTime.now().format(formatter), student);

    }
    @Before("execution(* ru.bmstu.service.impl.StudentServiceImpl.getAllStudents(..))")
    public void logViewAllStudents(JoinPoint joinPoint) {
        System.out.printf("[%s] [INFO] User viewed all students%n",
                LocalDateTime.now().format(formatter));
    }
}