package dev.studentpp1.streamingservice.users.dto;

import java.time.LocalDate;

public record UserDto(
        String name,
        String surname,
        String email,
        LocalDate birthday
) {
}
