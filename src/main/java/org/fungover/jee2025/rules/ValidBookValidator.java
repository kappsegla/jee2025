package org.fungover.jee2025.rules;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fungover.jee2025.dto.CreateBook;

public class ValidBookValidator implements ConstraintValidator<ValidBook, CreateBook> {
    @Override
    public boolean isValid(CreateBook createBook, ConstraintValidatorContext constraintValidatorContext) {
        if (Character.isUpperCase(createBook.title().charAt(0)) &&
            createBook.pageCount() != createBook.title().length()) {
            return true;
        }
        constraintValidatorContext
                .buildConstraintViolationWithTemplate("Length and title doesn't match")
                .addPropertyNode("title").addConstraintViolation();
        return false;
    }
}
