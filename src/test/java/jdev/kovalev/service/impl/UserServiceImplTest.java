package jdev.kovalev.service.impl;

import jdev.kovalev.dto.request.UserRequestDto;
import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.dto.response.UserResponseDto;
import jdev.kovalev.entity.Subscription;
import jdev.kovalev.entity.User;
import jdev.kovalev.exception.SubscriptionNotFoundException;
import jdev.kovalev.exception.UserNotFoundException;
import jdev.kovalev.mapper.SubscriptionMapper;
import jdev.kovalev.mapper.UserMapper;
import jdev.kovalev.repository.SubscriptionRepository;
import jdev.kovalev.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private String userId;
    private String subscriptionId;
    private User user;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private Subscription subscription;
    private SubscriptionResponseDto subscriptionResponseDto;

    @BeforeEach
    void setUp() {
        userId = "5068c1fc-84cb-4d85-b5ab-1945d37bda3c";
        subscriptionId = "dc59d861-6bd5-4452-9b24-161daadf7f6c";

        user = User.builder()
                .userId(UUID.fromString(userId))
                .firstName("Siarhei")
                .lastName("Kavaleu")
                .subscriptions(new HashSet<>())
                .build();

        userRequestDto = UserRequestDto.builder()
                .firstName("Siarhei")
                .lastName("Kavaleu")
                .build();
        userResponseDto = UserResponseDto.builder()
                .userId(UUID.fromString(userId))
                .firstName("Siarhei")
                .lastName("Kavaleu")
                .build();

        Set<User> users = new HashSet<>();
        users.add(user);
        subscription = Subscription.builder()
                .subscriptionId(UUID.fromString(subscriptionId))
                .digitalServiceName("Netflix")
                .users(users)
                .build();
        subscriptionResponseDto = SubscriptionResponseDto.builder()
                .subscriptionId(UUID.fromString(subscriptionId))
                .digitalServiceName("Netflix")
                .build();
    }

    @Nested
    class FindById {
        @Test
        void findById_whenPresentInDb() {
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(user));
            when(userMapper.toUserResponseDto(any(User.class)))
                    .thenReturn(userResponseDto);

            UserResponseDto actual = userService.findById(userId);

            assertThat(actual)
                    .isNotNull()
                    .isEqualTo(userResponseDto);
        }

        @Test
        void findById_whenNotPresentInDb() {
            when(userRepository.findById(UUID.fromString(userId)))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findById(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(String.format("Пользователь с id: %s не найден", userId));
        }
    }

    @Test
    void create() {
        when(userMapper.toUser(userRequestDto))
                .thenReturn(user);
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(userMapper.toUserResponseDto(any(User.class)))
                .thenReturn(userResponseDto);

        UserResponseDto actual = userService.create(userRequestDto);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(userResponseDto);
    }

    @Nested
    class Update {
        @Test
        void update_whenPresentInDb() {
            when(userRepository.findById(UUID.fromString(userId)))
                    .thenReturn(Optional.of(user));
            doNothing().when(userMapper)
                    .updateUser(any(User.class), any(UserRequestDto.class));
            when(userRepository.save(any(User.class)))
                    .thenReturn(user);
            when(userMapper.toUserResponseDto(any(User.class)))
                    .thenReturn(userResponseDto);

            UserResponseDto actual = userService.update(userId, userRequestDto);

            assertThat(actual)
                    .isNotNull()
                    .isEqualTo(userResponseDto);

            verify(userMapper).updateUser(user, userRequestDto);
        }

        @Test
        void update_whenNotPresentInDb() {
            when(userRepository.findById(UUID.fromString(userId)))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findById(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(String.format("Пользователь с id: %s не найден", userId));
        }
    }

    @Test
    void delete() {
        doNothing()
                .when(userRepository).deleteById(any(UUID.class));

        String actual = userService.delete(userId);

        assertThat(actual)
                .isEqualTo(String.format("Пользователь с id: %s успешно удалён", userId));

        verify(userRepository)
                .deleteById(any(UUID.class));
    }

    @Nested
    class AddSubscription {
        @Test
        void addSubscription_whenUserPresentInDb() {
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(user));
            when(subscriptionRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(subscription));
            user.addSubscription(subscription);

            String actual = userService.addSubscription(userId, subscriptionId);

            assertThat(actual)
                    .isEqualTo(String.format("Пользователю %s %s успешно добавлена подписка \"%s\"",
                                             user.getFirstName(), user.getLastName(), subscription.getDigitalServiceName()));
        }

        @Test
        void addSubscription_whenUserNotPresentInDb() {
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.addSubscription(userId, subscriptionId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(String.format("Пользователь с id: %s не найден", userId));
        }

        @Test
        void addSubscription_whenSubscriptionNotPresentInDb() {
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(user));
            when(subscriptionRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.addSubscription(userId, subscriptionId))
                    .isInstanceOf(SubscriptionNotFoundException.class)
                    .hasMessageContaining(String.format("Подписка с id: %s не найдена", subscriptionId));
        }
    }

    @Test
    void getUserSubscriptions() {
        user.addSubscription(subscription);

        when(userRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(user));
        when(subscriptionMapper.toDto(any(Subscription.class))).thenAnswer(invocation -> {
            Subscription subscription = invocation.getArgument(0);
            return SubscriptionResponseDto.builder()
                    .subscriptionId(subscription.getSubscriptionId())
                    .digitalServiceName(subscription.getDigitalServiceName())
                    .build();
        });

        List<SubscriptionResponseDto> actual = userService.getUserSubscriptions(userId);

        assertThat(actual)
                .isNotNull()
                .hasSameElementsAs(List.of(subscriptionResponseDto));
    }

    @Test
    void removeSubscriptionFromUser() {
        when(userRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(user));
        when(subscriptionRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(subscription));

        String actual = userService.removeSubscriptionFromUser(userId, subscriptionId);

        assertThat(actual)
                .isEqualTo(String.format("Пользователь %s %s успешно удалил подписку \"%s\"",
                                         user.getFirstName(), user.getLastName(), subscription.getDigitalServiceName()));
    }
}