package org.fungover.jee2025.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.fungover.jee2025.rules.ValidBook;

@ValidBook(message = "Not the default message")
public record CreateBook(
        @NotBlank @NotNull String title,
        @Positive(message = "must be positive") int pageCount) {}
