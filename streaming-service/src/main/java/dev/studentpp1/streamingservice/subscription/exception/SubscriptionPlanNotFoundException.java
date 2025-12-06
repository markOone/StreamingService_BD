package dev.studentpp1.streamingservice.subscription.exception;

public class SubscriptionPlanNotFoundException extends RuntimeException {
    public SubscriptionPlanNotFoundException(String message) {
        super(message);
    }
}
