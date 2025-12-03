package dev.studentpp1.streamingservice.users.dto;

import dev.studentpp1.streamingservice.users.utils.AppRegex;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterUserRequest(
        @NotBlank
        @NotNull
        String name,
        @NotBlank
        @NotNull
        String surname,
        @NotBlank
        @NotNull
        @Email
        String email,
        @NotBlank
        @NotNull
        @Pattern(
                regexp = AppRegex.PASSWORD,
                message = "Password must contain at least 8 characters, one uppercase, one lowercase, one number and one special character"
        )
        String password,
        @Past(message = "Birthday must be in the past")
        LocalDate birthday
) {
}
