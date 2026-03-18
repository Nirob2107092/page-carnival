package com.pc.pc.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class BookDto {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 200)
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value = 0)
    private Integer stock;

    @NotBlank(message = "Category is required")
    private String category;

    // getters & setters
}