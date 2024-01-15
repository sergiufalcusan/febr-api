package io.febr.api.controller.exception;

import java.util.List;

public class ApiException {
    private List<String> errors;

    public ApiException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
