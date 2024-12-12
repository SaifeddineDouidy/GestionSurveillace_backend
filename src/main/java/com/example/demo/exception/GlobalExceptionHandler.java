package com.example.demo.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(Model model) {
        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", "The page you are looking for does not exist.");
        return "error/404"; // View name for the 404 error page
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Model model, Exception ex) {
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMessage", "An unexpected error occurred.");
        return "error/500"; // View name for the generic error page
    }
}
