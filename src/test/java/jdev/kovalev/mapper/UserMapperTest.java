package jdev.kovalev.mapper;

import jdev.kovalev.dto.request.UserRequestDto;
import jdev.kovalev.dto.response.UserResponseDto;
import jdev.kovalev.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = new UserMapperImpl();

    private String userId;
    private User user;
    private UserResponseDto userResponseDto;
    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
        user = User.builder()
                .userId(UUID.fromString(userId))
                .firstName("Siarhei")
                .lastName("Kovalev")
                .subscriptions(new HashSet<>())
                .build();
        userResponseDto = UserResponseDto.builder()
                .userId(UUID.fromString(userId))
                .firstName("Siarhei")
                .lastName("Kovalev")
                .build();
        userRequestDto = UserRequestDto.builder()
                .firstName("Siarhei")
                .lastName("Kovalev")
                .build();
    }

    @Test
    void toUserResponseDto() {
        UserResponseDto actual = mapper.toUserResponseDto(user);

        assertThat(actual).isEqualTo(userResponseDto);
    }

    @Test
    void toUser() {
        User actual = mapper.toUser(userRequestDto);
        actual.setUserId(UUID.fromString(userId));

        assertThat(actual).isEqualTo(user);
    }

    @Test
    void updateUser() {
        User actual = mapper.toUser(userRequestDto);
        actual.setUserId(UUID.fromString(userId));

        assertThat(actual).isEqualTo(user);
    }
}