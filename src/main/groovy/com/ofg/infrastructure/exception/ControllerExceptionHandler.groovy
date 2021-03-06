package com.ofg.infrastructure.exception

import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletResponse

@Slf4j
@ControllerAdvice
@TypeChecked
class ControllerExceptionHandler {

    private static final String INTERNAL_ERROR = "internal error"

    @ExceptionHandler(BadParametersException.class)
    @ResponseBody
    public List<BadParameterError> handleBadParametersExceptions(BadParametersException exception, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        return getListOfBindErrors(exception)
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, String> handleAnyOtherExceptions(Exception exception, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
        log.error("Unexpected exception: ", exception)
        return [error:INTERNAL_ERROR, message:exception.getMessage()]
    }

    private List<BadParameterError> getListOfBindErrors(BadParametersException exception) {
        List<BadParameterError> bindErrorList = []
        List<ObjectError> errorList = exception.getErrors()
        for (ObjectError error : errorList) {
            bindErrorList.add(getBindError(error))
        }
        return bindErrorList
    }

    private BadParameterError getBindError(ObjectError error) {
        new BadParameterError(error.getProperties().get("field"), error.getDefaultMessage())
    }
}
