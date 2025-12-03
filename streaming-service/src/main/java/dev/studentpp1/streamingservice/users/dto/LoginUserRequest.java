package dev.studentpp1.streamingservice.users.dto;

import dev.studentpp1.streamingservice.users.utils.AppRegex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoginUserRequest(
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
        String password

) {
}
