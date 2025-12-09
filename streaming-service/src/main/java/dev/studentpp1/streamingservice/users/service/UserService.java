package dev.studentpp1.streamingservice.users.service;

import java.security.Principal;
import java.util.Objects;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.studentpp1.streamingservice.auth.persistence.AuthenticatedUser;
import dev.studentpp1.streamingservice.auth.persistence.Role;
import dev.studentpp1.streamingservice.users.dto.RegisterUserRequest;
import dev.studentpp1.streamingservice.users.dto.UpdateUserRequest;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import dev.studentpp1.streamingservice.users.exception.UserAlreadyExistsException;
import dev.studentpp1.streamingservice.users.mapper.UpdateUserMapper;
import dev.studentpp1.streamingservice.users.mapper.UserDtoMapper;
import dev.studentpp1.streamingservice.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final UpdateUserMapper updateUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AppUser createUser(RegisterUserRequest request) {
        if (userRepository.findByEmailAndDeletedFalse(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }
        AppUser user = userDtoMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        user.setDeleted(false);
        return userRepository.save(user);
    }

    @Transactional
    public AppUser updateUser(UpdateUserRequest request) {
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert principal != null;
        AppUser appUser = ((AuthenticatedUser) principal).getAppUser();
        updateUserMapper.updateFromDto(request, appUser);
        return userRepository.save(appUser); // find by id -> update
    }

    public AppUser getInfo(Principal principal) {
        return findByEmail(principal.getName());
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmailAndDeletedFalse(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found")
        );
    }

    public AppUser findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User with id " + userId + " not found")
        );
    }

    public void deleteUserById(Long id) {
        AppUser appUser = findById(id);
        appUser.setDeleted(true);
        userRepository.save(appUser);
    }
}