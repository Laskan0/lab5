package ru.bmstu.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.bmstu.context.CurrentUserContext;

@Aspect
@Component
public class AuthorizationAspect {

    private final CurrentUserContext currentUserContext;

    public AuthorizationAspect(CurrentUserContext currentUserContext) {
        this.currentUserContext = currentUserContext;
    }

    // Разрешаем только преподавателям вызывать эти методы
    @Before("execution(* ru.bmstu.service.impl.StudentServiceImpl.addStudent(..)) || " +
            "execution(* ru.bmstu.service.impl.StudentServiceImpl.removeStudent(..)) || " +
            "execution(* ru.bmstu.service.impl.StudentServiceImpl.changeTokens(..))")
    public void checkTeacherAccess() {
        String role = currentUserContext.getCurrentUserRole();
        if (!"teacher".equalsIgnoreCase(role)) {
            throw new SecurityException("Access denied: Only teachers can perform this action.");
        }
    }
}