package dev.studentpp1.streamingservice.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.studentpp1.streamingservice.users.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailAndDeletedFalse(String email);

    @Query("SELECT u FROM AppUser u WHERE u.id = :id AND u.deleted = FALSE")
    Optional<AppUser> findById(@Param("id") Long id);

    @Query("SELECT u FROM AppUser u WHERE u.id = :id")
    Optional<AppUser> findByIdIncludingDeleted(@Param("id") Long id);
}
