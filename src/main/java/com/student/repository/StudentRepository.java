
package com.student.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.student.model.Student;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbc;

    public StudentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Convert database row into Student object
    private Student mapStudent(java.sql.ResultSet rs,
                               int rowNum) throws java.sql.SQLException {

        Student s = new Student();

        s.setStudentId(rs.getLong("student_id"));
        s.setStudentName(rs.getString("student_name"));
        s.setEmail(rs.getString("email"));
        s.setCourseName(rs.getString("course_name"));
        s.setProgrammingScore(rs.getDouble("programming_score"));
        s.setMathematicsScore(rs.getDouble("mathematics_score"));

        return s;
    }

    // Add student
    public void addStudent(Student s) {

        String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?)";

        jdbc.update(sql,
                s.getStudentId(),
                s.getStudentName(),
                s.getEmail(),
                s.getCourseName(),
                s.getProgrammingScore(),
                s.getMathematicsScore());

        if (s.getTechnicalSkills() != null) {
            for (String skill : s.getTechnicalSkills()) {
                jdbc.update(
                    "INSERT INTO student_skills(student_id, skill) VALUES (?, ?)",
                    s.getStudentId(), skill);
            }
        }
    }

    // Get all students
    public List<Student> getAllStudents() {

        List<Student> students = jdbc.query(
                "SELECT * FROM students", this::mapStudent);

        for (Student s : students) {
            s.setTechnicalSkills(getSkills(s.getStudentId()));
        }

        return students;
    }

    // Get skills of a student
    public List<String> getSkills(Long id) {

        return jdbc.query(
            "SELECT skill FROM student_skills WHERE student_id = ?",
            (rs, rowNum) -> rs.getString("skill"),
            id);
    }

    // Search by ID
    public Student getStudentById(Long id) {

        List<Student> result = jdbc.query(
            "SELECT * FROM students WHERE student_id = ?",
            this::mapStudent, id);

        if (result.isEmpty()) {
            return null;
        }

        Student s = result.get(0);
        s.setTechnicalSkills(getSkills(id));

        return s;
    }

    // Search by email
    public List<Student> getByEmail(String email) {

        List<Student> result = jdbc.query(
            "SELECT * FROM students WHERE email = ?",
            this::mapStudent, email);

        for (Student s : result) {
            s.setTechnicalSkills(getSkills(s.getStudentId()));
        }

        return result;
    }

    // Search by course
    public List<Student> getByCourse(String course) {

        List<Student> result = jdbc.query(
            "SELECT * FROM students WHERE course_name = ?",
            this::mapStudent, course);

        for (Student s : result) {
            s.setTechnicalSkills(getSkills(s.getStudentId()));
        }

        return result;
    }

    // Update scores and skills
    public void updateStudent(Long id, Student s) {

        jdbc.update(
            "UPDATE students SET programming_score = ?, mathematics_score = ? WHERE student_id = ?",
            s.getProgrammingScore(),
            s.getMathematicsScore(),
            id);

        jdbc.update(
            "DELETE FROM student_skills WHERE student_id = ?", id);

        if (s.getTechnicalSkills() != null) {
            for (String skill : s.getTechnicalSkills()) {
                jdbc.update(
                    "INSERT INTO student_skills(student_id, skill) VALUES (?, ?)",
                    id, skill);
            }
        }
    }

    // Delete student
    public void deleteStudent(Long id) {

        jdbc.update(
            "DELETE FROM students WHERE student_id = ?", id);
    }

    // Merit list: highest average first
    public List<Student> getMeritList() {

        List<Student> students = getAllStudents();

        students.sort((a, b) ->
            Double.compare(b.getAverage(), a.getAverage()));

        return students;
    }

    // Students who need programming improvement
    // but have mathematics score >= 75
    public List<Student> getProgrammingImprovementStudents() {

        List<Student> result = jdbc.query(
            "SELECT * FROM students WHERE programming_score < mathematics_score AND mathematics_score >= 75",
            this::mapStudent);

        for (Student s : result) {
            s.setTechnicalSkills(getSkills(s.getStudentId()));
        }

        return result;
    }
}
