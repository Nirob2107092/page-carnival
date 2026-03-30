package com.pc.pc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("statusCode", 404);
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("backLink", "/");
        model.addAttribute("backText", "Go Home");
        return "error";
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleInsufficientStock(InsufficientStockException ex, Model model) {
        model.addAttribute("statusCode", 409);
        model.addAttribute("errorTitle", "Insufficient Stock");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("backLink", "/buyer/cart");
        model.addAttribute("backText", "Back to Cart");
        return "error";
    }

    @ExceptionHandler(EmptyCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmptyCart(EmptyCartException ex, Model model) {
        model.addAttribute("statusCode", 400);
        model.addAttribute("errorTitle", "Empty Cart");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("backLink", "/buyer/catalog");
        model.addAttribute("backText", "Browse Books");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("statusCode", 500);
        model.addAttribute("errorTitle", "Something Went Wrong");
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        model.addAttribute("backLink", "/");
        model.addAttribute("backText", "Go Home");
        return "error";
    }
}
