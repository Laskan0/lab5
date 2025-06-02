package ru.bmstu.context;

import org.springframework.stereotype.Component;

@Component
public class CurrentUserContext {
    private String currentUserRole;

    public void setCurrentUserRole(String role) {
        this.currentUserRole = role;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }
}