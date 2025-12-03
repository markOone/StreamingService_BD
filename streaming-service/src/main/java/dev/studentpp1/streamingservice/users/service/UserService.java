package dev.studentpp1.streamingservice.users.service;

import dev.studentpp1.streamingservice.users.dto.LoginUserRequest;
import dev.studentpp1.streamingservice.users.dto.RegisterUserRequest;
import dev.studentpp1.streamingservice.users.dto.UpdateUserRequest;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import dev.studentpp1.streamingservice.users.exception.UserAlreadyExistsException;
import dev.studentpp1.streamingservice.users.exception.UserNotFoundException;
import dev.studentpp1.streamingservice.users.mapper.UpdateUserMapper;
import dev.studentpp1.streamingservice.users.mapper.UserDtoMapper;
import dev.studentpp1.streamingservice.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final UpdateUserMapper updateUserMapper;

    public AppUser createUser(RegisterUserRequest request) {
        // TODO: validate password (after implementing auth module)
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
        }
        AppUser user = userDtoMapper.toUser(request);
        return userRepository.save(user);
    }

    public AppUser loginUser(LoginUserRequest request) {
        // TODO: validate password (after implementing auth module)
        return userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User with email " + request.email() + " not found"));
    }

    public AppUser updateUser(UpdateUserRequest request) {
        AppUser appUser = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User with email " + request.email() + " not found"));
        updateUserMapper.updateFromDto(request, appUser);
        // TODO: set password (after implementing auth module)
        return userRepository.save(appUser);
    }

    public AppUser getInfo(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("user with id " + id + " not found"));
    }
}