package jdev.kovalev.exception;

public class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Пользователь с id: %s не найден";

    public UserNotFoundException(String userId) {
        super(String.format(DEFAULT_MESSAGE, userId));
    }
}
