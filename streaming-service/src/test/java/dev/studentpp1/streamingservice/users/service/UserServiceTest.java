package dev.studentpp1.streamingservice.users.service;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.studentpp1.streamingservice.AbstractPostgresContainerTest;
import dev.studentpp1.streamingservice.auth.persistence.AuthenticatedUser;
import dev.studentpp1.streamingservice.users.dto.RegisterUserRequest;
import dev.studentpp1.streamingservice.users.dto.UpdateUserRequest;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import dev.studentpp1.streamingservice.users.exception.UserAlreadyExistsException;
import dev.studentpp1.streamingservice.users.repository.UserRepository;

@SpringBootTest
class UserServiceTest extends AbstractPostgresContainerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RegisterUserRequest validRegisterRequest;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        userRepository.deleteAll();
        validRegisterRequest = new RegisterUserRequest(
                "ActiveName",
                "ActiveSurname",
                "active.user@example.com",
                "StartP@ss123",
                LocalDate.of(1995, 10, 20)
        );
    }

    @Test
    void testCreateUser_Success() {
        AppUser createdUser = userService.createUser(validRegisterRequest);
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo(validRegisterRequest.email());
        assertThat(createdUser.isDeleted()).isFalse();
        assertThat(passwordEncoder.matches(validRegisterRequest.password(), createdUser.getPassword())).isTrue();
    }

    @Test
    void testCreateUser_ActiveUserAlreadyExists_ThrowsException() {
        AppUser appUser = userService.createUser(validRegisterRequest);
        RegisterUserRequest duplicateRequest = new RegisterUserRequest(
                appUser.getName(),
                appUser.getSurname(),
                appUser.getEmail(),
                "DifferentPassword123!",
                LocalDate.now()
        );
        assertThrows(UserAlreadyExistsException.class, () ->
                userService.createUser(duplicateRequest)
        );
    }

    @Test
    void testDeleteUserById_Success() {
        AppUser userToDelete = userService.createUser(validRegisterRequest);
        Long userId = userToDelete.getId();
        userService.deleteUserById(userId);
        AppUser dbRecord = userRepository.findByIdIncludingDeleted(userId).orElseThrow();
        assertThat(dbRecord.isDeleted()).isTrue();
        assertThrows(UsernameNotFoundException.class, () ->
                userService.findById(userId)
        );
        assertThrows(UsernameNotFoundException.class, () ->
                userService.findByEmail(validRegisterRequest.email())
        );
    }

    @Test
    void testDeleteUserById_NotFound_ThrowsException() {
        assertThrows(UsernameNotFoundException.class, () ->
                userService.deleteUserById(999L)
        );
    }

    @Test
    void testFindById_ActiveUserFound() {
        AppUser savedUser = userService.createUser(validRegisterRequest);
        AppUser foundUser = userService.findById(savedUser.getId());
        assertThat(foundUser.getName()).isEqualTo(validRegisterRequest.name());
    }

    @Test
    void testFindByEmail_ActiveUserFound(){
        userService.createUser(validRegisterRequest);
        AppUser foundUser = userService.findByEmail(validRegisterRequest.email());
        assertThat(foundUser.getEmail()).isEqualTo(validRegisterRequest.email());
    }

    @Test
    void testFindById_NotFound() {
        assertThrows(UsernameNotFoundException.class, () ->
                userService.findById(888L)
        );
    }

    @Test
    void testUpdateUser_Success() {
        AppUser initialUser = userService.createUser(validRegisterRequest);
        String originalEmail = initialUser.getEmail();
        String originalPasswordHash = initialUser.getPassword();
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(initialUser);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser, null, authenticatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UpdateUserRequest updateRequest = new UpdateUserRequest(
                "UpdatedName",
                "UpdatedSurname",
                LocalDate.of(1985, 1, 1)
        );
        AppUser updatedUser = userService.updateUser(updateRequest);
        assertThat(updatedUser.getId()).isEqualTo(initialUser.getId());
        assertThat(updatedUser.getName()).isEqualTo(updateRequest.name());
        assertThat(updatedUser.getSurname()).isEqualTo(updateRequest.surname());
        assertThat(updatedUser.getBirthday()).isEqualTo(updateRequest.birthday());
        assertThat(updatedUser.getEmail()).isEqualTo(originalEmail);
        assertThat(updatedUser.getPassword()).isEqualTo(originalPasswordHash);
    }

    @Test
    void testUpdateUser_NoPrincipal_ThrowsException() {
        UpdateUserRequest updateRequest = new UpdateUserRequest(
                "TestName", "TestSurname", LocalDate.now()
        );
        assertThrows(NullPointerException.class, () ->
                userService.updateUser(updateRequest)
        );
    }
}