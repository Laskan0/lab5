package ru.bmstu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bmstu.dto.TokenChangeRequest;
import ru.bmstu.model.Student;
import ru.bmstu.repository.StudentRepository;
import ru.bmstu.service.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentByFullName(String lastName, String firstName) {
        return studentRepository.findByFullName(lastName, firstName);
    }

    @Override
    public void addStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void changeTokens(TokenChangeRequest request) {
        Student student = studentRepository.findByFullName(request.getLastName(), request.getFirstName());
        if (student != null) {
            student.setTokenCount(student.getTokenCount() + request.getDelta());
            studentRepository.save(student);
        }
    }

    @Override
    public void removeStudent(Student student) {
        studentRepository.delete(student);
    }
}