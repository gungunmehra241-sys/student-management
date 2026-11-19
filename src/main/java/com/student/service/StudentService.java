
package com.student.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.student.model.Student;
import com.student.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // Validate student details
    private void validateStudent(Student s) {

        if (s == null) {
            throw new IllegalArgumentException("Student data required");
        }

        if (s.getStudentId() == null || s.getStudentId() <= 0) {
            throw new IllegalArgumentException("Invalid student ID");
        }

        if (s.getStudentName() == null || s.getStudentName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (s.getEmail() == null || s.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (s.getCourseName() == null || s.getCourseName().isBlank()) {
            throw new IllegalArgumentException("Course is required");
        }

        if (s.getProgrammingScore() < 0 ||
            s.getProgrammingScore() > 100) {
            throw new IllegalArgumentException("Invalid programming score");
        }

        if (s.getMathematicsScore() < 0 ||
            s.getMathematicsScore() > 100) {
            throw new IllegalArgumentException("Invalid mathematics score");
        }
    }

    // Add student
    public void addStudent(Student s) {

        validateStudent(s);

        if (repository.getStudentById(s.getStudentId()) != null) {
            throw new IllegalArgumentException("Student ID already exists");
        }

        if (!repository.getByEmail(s.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("Email already exists");
        }

        repository.addStudent(s);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return repository.getAllStudents();
    }

    // Search by ID
    public Student getStudentById(Long id) {
        return repository.getStudentById(id);
    }

    // Search by email
    public List<Student> getByEmail(String email) {
        return repository.getByEmail(email);
    }

    // Search by course
    public List<Student> getByCourse(String course) {
        return repository.getByCourse(course);
    }

    // Update student
    public void updateStudent(Long id, Student s) {

        validateStudent(s);

        if (repository.getStudentById(id) == null) {
            throw new IllegalArgumentException("Student not found");
        }

        if (!s.getStudentId().equals(id)) {
            throw new IllegalArgumentException("Student ID cannot change");
        }

        repository.updateStudent(id, s);
    }

    // Delete student
    public void deleteStudent(Long id) {

        if (repository.getStudentById(id) == null) {
            throw new IllegalArgumentException("Student not found");
        }

        repository.deleteStudent(id);
    }

    // Merit list
    public List<Student> getMeritList() {
        return repository.getMeritList();
    }

    // Programming improvement list
    public List<Student> getProgrammingImprovementStudents() {
        return repository.getProgrammingImprovementStudents();
    }
}
