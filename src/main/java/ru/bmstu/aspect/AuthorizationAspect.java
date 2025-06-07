package ru.bmstu.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.bmstu.context.CurrentUserContext;
import ru.bmstu.exception.AccessDeniedException;

@Aspect
@Component
public class AuthorizationAspect {

    private final CurrentUserContext currentUserContext;

    public AuthorizationAspect(CurrentUserContext currentUserContext) {
        this.currentUserContext = currentUserContext;
    }

    // Перехватываем методы интерфейса StudentService
    @Pointcut("execution(* ru.bmstu.service.StudentService.addStudent(..)) || " +
            "execution(* ru.bmstu.service.StudentService.removeStudent(..)) || " +
            "execution(* ru.bmstu.service.StudentService.changeTokens(..))")
    public void restrictedOperations() {}

    @Before("restrictedOperations()")
    public void checkTeacherAccess() {
        if (!"teacher".equalsIgnoreCase(currentUserContext.getCurrentUserRole())) {
            throw new AccessDeniedException("Only teahcer can do this things");
        }
    }
}