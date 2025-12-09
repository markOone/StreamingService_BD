package dev.studentpp1.streamingservice.users.controller;

import dev.studentpp1.streamingservice.users.dto.UpdateUserRequest;
import dev.studentpp1.streamingservice.users.dto.UserDto;
import dev.studentpp1.streamingservice.users.mapper.UserDtoMapper;
import dev.studentpp1.streamingservice.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @PostMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UpdateUserRequest request) {
        UserDto userDto = userDtoMapper.toUserDto(userService.updateUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> getInfo(Principal principal) {
        UserDto userDto = userDtoMapper.toUserDto(userService.getInfo(principal));
        return ResponseEntity.ok(userDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
