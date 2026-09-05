package com.example.demo.course;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/courses")
public class CourseController {

    private final CourseService courseService;


    @Autowired
    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }


    @GetMapping
    public List<CourseResponse> getCourses(){
        return courseService.getCourses();
    }


    @PostMapping
    public CourseResponse registerNewCourse(
            @RequestBody @Valid CourseRequest request){

        return courseService.addNewCourse(request);
    }


    // Delete one course
    @DeleteMapping("/{courseID}")
    public void deleteCourse(@PathVariable Long courseID){
        courseService.deleteCourse(courseID);
    }

    // Delete all courses
    @DeleteMapping
    public void deleteAllCourses() {
        courseService.deleteAllCourses();
    }


    @PutMapping("/{courseID}")
    public CourseResponse updateCourse(
            @PathVariable Long courseID,
            @RequestBody @Valid CourseRequest request){

        return courseService.updateCourse(courseID, request);
    }



    // PATCH
    @PatchMapping("/{courseID}")
    public CourseResponse patchCourse(
            @PathVariable Long courseID,
            @RequestBody CoursePatchRequest request){

        return courseService.patchCourse(courseID, request);
    }
}
