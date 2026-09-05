package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Get all students
    @GetMapping
    public List<StudentResponse> getStudents(){
        return studentService.getStudents()
                .stream()
                .map(StudentResponse::fromStudent)
                .toList();
    }

    // Add new student
    @PostMapping
    public ResponseEntity<StudentResponse> registerNewStudent(
            @RequestBody @Valid StudentRequest request){

        Student createdStudent = studentService.addNewStudent(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(StudentResponse.fromStudent(createdStudent));
    }


    // Delete one student
    @DeleteMapping(path = "/{studentID}")
    public void deleteStudent(@PathVariable Long studentID){
        studentService.deleteStudent(studentID);
    }

    // Delete all students
    @DeleteMapping
    public void deleteAllStudents() {
        studentService.deleteAllStudents();
    }

    // PUT
    @PutMapping(path = "/{studentID}")
    public void updateStudent(@PathVariable Long studentID, @RequestBody @Valid StudentRequest request) {
        studentService.updateStudent(studentID, request);
    }

    // PATCH
    @PatchMapping(path = "/{studentID}")
    public void patchStudent(@PathVariable Long studentID, @RequestBody @Valid StudentRequest request) {
        studentService.patchStudent(studentID, request);
    }

}
