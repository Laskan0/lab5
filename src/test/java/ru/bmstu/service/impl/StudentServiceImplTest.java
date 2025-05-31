package ru.bmstu.service.impl;

import org.junit.Before;
import org.junit.Test;
import ru.bmstu.dto.TokenChangeRequest;
import ru.bmstu.model.Student;
import ru.bmstu.repository.InMemoryStudentRepository;
import ru.bmstu.repository.StudentRepository;
import ru.bmstu.service.StudentService;

import java.util.List;

import static org.junit.Assert.*;

public class StudentServiceImplTest {

    private StudentService studentService;
    private StudentRepository mockRepository;

    @Before
    public void setUp() {
        // Создаем мок-репозиторий
        mockRepository = new InMemoryStudentRepository();
        studentService = new StudentServiceImpl(mockRepository);
    }

    @Test
    public void testGetAllStudents_ShouldReturnListOfStudents() {
        List<Student> students = studentService.getAllStudents();
        assertNotNull(students);
        assertFalse(students.isEmpty());
    }

    @Test
    public void testGetStudentByFullName_ShouldReturnValidStudent() {
        Student student = studentService.getStudentByFullName("Ivanov", "Ivan");
        assertNotNull(student);
        assertEquals("Ivanov", student.getLastName());
        assertEquals("Ivan", student.getFirstName());
    }

    @Test
    public void testChangeTokens_ShouldIncreaseTokenCount() {
        TokenChangeRequest request = new TokenChangeRequest("Ivanov", "Ivan", 5);
        studentService.changeTokens(request);

        Student updated = studentService.getStudentByFullName("Ivanov", "Ivan");
        assertNotNull(updated);
        assertEquals(15, updated.getTokenCount());
    }

    @Test
    public void testAddStudent_ShouldAddNewStudent() {
        Student newStudent = new Student("Smith", "John", 3);
        studentService.addStudent(newStudent);

        Student added = studentService.getStudentByFullName("Smith", "John");
        assertNotNull(added);
        assertEquals(3, added.getTokenCount());
    }

    @Test
    public void testRemoveStudent_ShouldDeleteStudent() {
        Student student = studentService.getStudentByFullName("Sidorov", "Sidr");
        assertTrue(studentService.getStudentByFullName("Sidorov", "Sidr") != null);

        studentService.removeStudent(student);
        assertNull(studentService.getStudentByFullName("Sidorov", "Sidr"));
    }
}