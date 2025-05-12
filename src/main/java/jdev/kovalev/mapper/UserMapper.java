package jdev.kovalev.mapper;

import jdev.kovalev.dto.request.UserRequestDto;
import jdev.kovalev.dto.response.UserResponseDto;
import jdev.kovalev.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    User toUser(UserRequestDto userRequestDto);

    void updateUser(@MappingTarget User user, UserRequestDto userRequestDto);
}
