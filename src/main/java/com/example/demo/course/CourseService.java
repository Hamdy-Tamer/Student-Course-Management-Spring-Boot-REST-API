package com.example.demo.course;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseResponse> getCourses(){
        return courseRepository.findAll()
                .stream()
                .map(course -> new CourseResponse(
                        course.getCourseID(),
                        course.getCourse_name(),
                        course.getCourse_code()
                ))
                .toList();
    }

    public CourseResponse addNewCourse(CourseRequest request){
        Optional<Course> courseOptional = courseRepository.findCourseByCourseCode(request.course_code());

        if(courseOptional.isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Course code is taken"
            );
        }

        Course course = new Course(
                request.course_name(),
                request.course_code()
        );

        Course savedCourse = courseRepository.save(course);

        return new CourseResponse(
                savedCourse.getCourseID(),
                savedCourse.getCourse_name(),
                savedCourse.getCourse_code()
        );
    }

    // Delete one course
    public void deleteCourse(Long courseID){
        boolean exists =
                courseRepository.existsById(courseID);
        if(!exists){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Course with id " + courseID + " doesn't exist"
            );
        }
        courseRepository.deleteById(courseID);
    }

    // Delete all courses
    public void deleteAllCourses() {
        courseRepository.deleteAll();
    }

    // PUT
    @Transactional
    public CourseResponse updateCourse(Long courseID, CourseRequest request){

        Course course =
                courseRepository.findById(courseID)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Course with id " + courseID + " doesn't exist"
                                ));


        if(!Objects.equals(course.getCourse_name(), request.course_name())){
            course.setCourse_name(
                    request.course_name()
            );
        }

        if(!Objects.equals(course.getCourse_code(), request.course_code())){

            Optional<Course> courseOptional = courseRepository.findCourseByCourseCode(request.course_code());

            if(courseOptional.isPresent()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Course code is taken"
                );
            }

            course.setCourse_code(request.course_code());
        }

        return new CourseResponse(
                course.getCourseID(),
                course.getCourse_name(),
                course.getCourse_code()
        );
    }

    // PATCH
    @Transactional
    public CourseResponse patchCourse(Long courseID, CoursePatchRequest request){
        Course course =
                courseRepository.findById(courseID)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Course with id " + courseID + " doesn't exist"
                                ));


        if(request.course_name() != null){
            course.setCourse_name(request.course_name());
        }

        if(request.course_code() != null && !Objects.equals(course.getCourse_code(), request.course_code())){
            Optional<Course> courseOptional = courseRepository.findCourseByCourseCode(request.course_code());


            if(courseOptional.isPresent()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Course code is taken"
                );
            }
            course.setCourse_code(request.course_code());
        }

        return new CourseResponse(
                course.getCourseID(),
                course.getCourse_name(),
                course.getCourse_code()
        );
    }
}
