package com.kraluk.totoscheduler.web.rest.errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * View Model for transferring error message with a list of field errors.
 */
public class ErrorVm implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final String description;

    private List<FieldErrorVm> fieldErrors;

    public ErrorVm(String message) {
        this(message, null);
    }

    public ErrorVm(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public ErrorVm(String message, String description, List<FieldErrorVm> fieldErrors) {
        this.message = message;
        this.description = description;
        this.fieldErrors = fieldErrors;
    }

    public void add(String objectName, String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorVm(objectName, field, message));
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public List<FieldErrorVm> getFieldErrors() {
        return fieldErrors;
    }
}
