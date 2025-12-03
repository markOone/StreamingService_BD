package dev.studentpp1.streamingservice.users.utils;

public final class AppRegex {
    private AppRegex() {}
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
}
