package io.febr.api.controller.exception;

public class CourseNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Course not found";
    }
}
