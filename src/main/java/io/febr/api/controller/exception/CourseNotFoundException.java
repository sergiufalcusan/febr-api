package io.febr.api.controller.exception;

public class CourseNotFoundException extends Exception {
    private final Long id;

    private CourseNotFoundException(Long id) {
        this.id = id;
    }

    public static CourseNotFoundException createWith(Long id) {
        return new CourseNotFoundException(id);
    }

    @Override
    public String getMessage() {
        return "Course '" + id + "' not found";
    }
}
