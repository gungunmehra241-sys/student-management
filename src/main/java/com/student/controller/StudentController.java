package com.student.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.model.Student;
import com.student.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // Add a student
    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        service.addStudent(student);
        return ResponseEntity.ok("Student added successfully");
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    // Search student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {

        Student student = service.getStudentById(id);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(student);
    }

    // Search by email
    @GetMapping("/email/{email}")
    public List<Student> getByEmail(@PathVariable String email) {
        return service.getByEmail(email);
    }

    // Search by course
    @GetMapping("/course/{course}")
    public List<Student> getByCourse(@PathVariable String course) {
        return service.getByCourse(course);
    }

    // Update student scores and skills
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {

        service.updateStudent(id, student);
        return ResponseEntity.ok("Student updated successfully");
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    // Get merit list
    @GetMapping("/merit-list")
    public List<Student> getMeritList() {
        return service.getMeritList();
    }

    // Students needing programming improvement
    @GetMapping("/programming-improvement")
    public List<Student> getProgrammingImprovementStudents() {
        return service.getProgrammingImprovementStudents();
    }

    // Handle validation errors
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleError(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

