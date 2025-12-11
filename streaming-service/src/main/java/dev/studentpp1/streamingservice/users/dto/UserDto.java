package dev.studentpp1.streamingservice.users.dto;

import dev.studentpp1.streamingservice.auth.persistence.Role;

import java.time.LocalDate;

public record UserDto(
        String name,
        String surname,
        String email,
        LocalDate birthday,
        Role role
) {
}
