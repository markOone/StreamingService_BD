package dev.studentpp1.streamingservice.users.dto;

import java.time.LocalDate;

public record UpdateUserRequest(
        String name,
        String surname,
        LocalDate birthday
) {
}
