package com.example.demo.student;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Objects;
import java.util.Optional;



@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // GET all
    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    // Create new student
    public Student addNewStudent(StudentRequest request){
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(request.email());

        if(studentOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is taken");
        }

        Student student = new Student(
                request.name(),
                request.email(),
                request.dob()
        );

        return studentRepository.save(student);
    }

    // Delete one student
    public void deleteStudent(Long studentID){
        boolean exists = studentRepository.existsById(studentID);

        if(!exists){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id " + studentID + " doesn't exist");
        }
        studentRepository.deleteById(studentID);

    }

    // Delete all students
    public void deleteAllStudents() {
        studentRepository.deleteAll();
    }

    // PUT
    @Transactional
    public void updateStudent(Long studentID, StudentRequest request){

        Student student = studentRepository.findById(studentID)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        if(request.name() != null){
            student.setName(request.name());
        }

        if(request.email() != null && !Objects.equals(student.getEmail(), request.email())){
            Optional<Student> emailExists = studentRepository.findStudentByEmail(request.email());

            if(emailExists.isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is taken");
            }

            student.setEmail(request.email());
        }

        if(request.dob() != null){
            student.setDob(request.dob());
        }
    }

    // PATCH
    @Transactional
    public void patchStudent(Long studentID, StudentRequest request){
        updateStudent(studentID, request);
    }
}
