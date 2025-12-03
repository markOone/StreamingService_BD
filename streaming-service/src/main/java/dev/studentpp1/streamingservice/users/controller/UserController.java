package dev.studentpp1.streamingservice.users.controller;

import dev.studentpp1.streamingservice.users.dto.UpdateUserRequest;
import dev.studentpp1.streamingservice.users.dto.UserDto;
import dev.studentpp1.streamingservice.users.mapper.UserDtoMapper;
import dev.studentpp1.streamingservice.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: register/login will be in auth module
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @PostMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserRequest request) {
        UserDto userDto = userDtoMapper.toUserDto(userService.updateUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    // TODO: change to Principal (after implementing auth module)
    @GetMapping
    public ResponseEntity<UserDto> getInfo(@RequestParam("id") Long id) {
        UserDto userDto = userDtoMapper.toUserDto(userService.getInfo(id));
        return ResponseEntity.ok(userDto);
    }
}
