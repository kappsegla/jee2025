package org.fungover.jee2025.exceptions;

public class NotFound extends RuntimeException {
    public NotFound() {
        super();
    }
    public NotFound(String message) {
        super(message);
    }
}
