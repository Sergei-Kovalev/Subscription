package jdev.kovalev.exception;

public class SubscriptionNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Подписка с id: %s не найдена";

    public SubscriptionNotFoundException(String userId) {
        super(String.format(DEFAULT_MESSAGE, userId));
    }
}
