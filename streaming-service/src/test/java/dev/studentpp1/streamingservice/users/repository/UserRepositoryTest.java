package dev.studentpp1.streamingservice.users.repository;

import dev.studentpp1.streamingservice.AbstractPostgresContainerTest;
import dev.studentpp1.streamingservice.auth.persistence.Role;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest extends AbstractPostgresContainerTest {

    @Autowired
    private UserRepository userRepository;

    private AppUser deletedUser;
    private AppUser activeUser1;
    private AppUser activeUser2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        deletedUser = userRepository.save(AppUser.builder()
                .name("John")
                .surname("Doe")
                .email("user1@example.com")
                .password("password1")
                .birthday(LocalDate.of(1990, 1, 1))
                .role(Role.ROLE_USER)
                .deleted(true)
                .build());

        activeUser1 = userRepository.save(AppUser.builder()
                .name("Jane")
                .surname("Smith")
                .email("user2@example.com")
                .password("password2")
                .birthday(LocalDate.of(1995, 2, 2))
                .role(Role.ROLE_USER)
                .deleted(false)
                .build());

        activeUser2 = userRepository.save(AppUser.builder()
                .name("Alice")
                .surname("Brown")
                .email("user3@example.com")
                .password("password3")
                .birthday(LocalDate.of(1998, 3, 3))
                .role(Role.ROLE_USER)
                .deleted(false)
                .build());
    }

    @Test
    void findByEmailAndDeletedFalse_shouldReturnNotDeletedUser() {
        Optional<AppUser> result = userRepository.findByEmailAndDeletedFalse("user2@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(activeUser1.getId());
        assertThat(result.get().isDeleted()).isFalse();
        assertThat(result.get().getEmail()).isEqualTo("user2@example.com");
    }

    @Test
    void findByEmailAndDeletedFalse_shouldReturnEmpty_whenUserDeleted() {
        Optional<AppUser> result = userRepository.findByEmailAndDeletedFalse("user1@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void findByEmailAndDeletedFalse_shouldReturnEmpty_whenEmailNotFound() {
        Optional<AppUser> result = userRepository.findByEmailAndDeletedFalse("missing@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void findById_shouldReturnUser_whenNotDeleted() {
        Optional<AppUser> result = userRepository.findById(activeUser2.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(activeUser2.getId());
        assertThat(result.get().isDeleted()).isFalse();
    }

    @Test
    void findById_shouldReturnEmpty_whenDeleted() {
        Optional<AppUser> result = userRepository.findById(deletedUser.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        Optional<AppUser> result = userRepository.findById(999_999L);

        assertThat(result).isEmpty();
    }

    @Test
    void findByIdIncludingDeleted_shouldReturnUser_whenNotDeleted() {
        Optional<AppUser> result = userRepository.findByIdIncludingDeleted(activeUser1.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(activeUser1.getId());
        assertThat(result.get().isDeleted()).isFalse();
    }

    @Test
    void findByIdIncludingDeleted_shouldReturnUser_whenDeleted() {
        Optional<AppUser> result = userRepository.findByIdIncludingDeleted(deletedUser.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(deletedUser.getId());
        assertThat(result.get().isDeleted()).isTrue();
    }

    @Test
    void findByIdIncludingDeleted_shouldReturnEmpty_whenNotFound() {
        Optional<AppUser> result = userRepository.findByIdIncludingDeleted(999_999L);

        assertThat(result).isEmpty();
    }
}
