package dev.studentpp1.streamingservice.users.mapper;

import dev.studentpp1.streamingservice.users.dto.RegisterUserRequest;
import dev.studentpp1.streamingservice.users.dto.UserDto;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserDtoMapper {
    UserDto toUserDto(AppUser user);
    AppUser toUser(RegisterUserRequest request);
}
