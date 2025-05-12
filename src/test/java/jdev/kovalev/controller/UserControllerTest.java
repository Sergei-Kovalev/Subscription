package jdev.kovalev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdev.kovalev.dto.request.UserRequestDto;
import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.dto.response.UserResponseDto;
import jdev.kovalev.exception.SubscriptionNotFoundException;
import jdev.kovalev.exception.UserNotFoundException;
import jdev.kovalev.exception.handler.ControllersExceptionHandler;
import jdev.kovalev.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final static String USER_NOT_FOUND_MESSAGE = "Пользователь с id: %s не найден";
    private static final String SUBSCRIPTION_NOT_FOUND_MESSAGE = "Подписка с id: %s не найдена";
    private final static String SUCCESSFULLY_DELETED = "Пользователь с id: %s успешно удалён";
    private final static String SUBSCRIPTION_ADDED = "Пользователю %s %s успешно добавлена подписка \"%s\"";
    private final static String SUBSCRIPTION_REMOVED = "Пользователь %s %s успешно удалил подписку \"%s\"";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private List<SubscriptionResponseDto> subscriptions;

    private String userId;
    private String subscriptionId;

    private UserResponseDto userResponseDto;
    private UserRequestDto userRequestDto;
    private SubscriptionResponseDto subscriptionResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new ControllersExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        userId = "c93cb706-7fcc-4ce3-a88a-c9c8e92b6c20";
        subscriptionId = "8b6b0719-32b3-4db5-ad73-e707c19c09b9";

        userResponseDto = UserResponseDto.builder()
                .userId(UUID.fromString(userId))
                .firstName("Siarhei")
                .lastName("Kavaleu")
                .build();
        userRequestDto = UserRequestDto.builder()
                .firstName("Siarhei")
                .lastName("Kavaleu")
                .build();
        subscriptionResponseDto = SubscriptionResponseDto.builder()
                .subscriptionId(UUID.fromString(subscriptionId))
                .digitalServiceName("Netflix")
                .build();

        subscriptions = List.of(
                subscriptionResponseDto,
                SubscriptionResponseDto.builder()
                        .subscriptionId(UUID.randomUUID())
                        .digitalServiceName("Youtube")
                        .build());
    }

    @Nested
    class FindById {
        @Test
        @SneakyThrows
        void findById_whenUserFound() {
            when(userService.findById(userId))
                    .thenReturn(userResponseDto);

            mockMvc.perform(get("/users/{id}", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.firstName").value("Siarhei"))
                    .andExpect(jsonPath("$.lastName").value("Kavaleu"))
                    .andDo(print());

            verify(userService).findById(userId);
        }

        @Test
        @SneakyThrows
        void findById_whenUserNotFound() {
            when(userService.findById(userId))
                    .thenThrow(new UserNotFoundException(userId));

            mockMvc.perform(get("/users/{id}", userId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, userId)))
                    .andDo(print());

            verify(userService).findById(userId);
        }
    }

    @Test
    @SneakyThrows
    void create() {
        when(userService.create(any(UserRequestDto.class)))
                .thenReturn(userResponseDto);

        mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Siarhei"))
                .andExpect(jsonPath("$.lastName").value("Kavaleu"))
                .andDo(print());
        verify(userService).create(any(UserRequestDto.class));
    }

    @Nested
    class Update {
        @Test
        @SneakyThrows
        void update_whenUserFound() {
            when(userService.update(userId, userRequestDto))
                    .thenReturn(userResponseDto);

            mockMvc.perform(put("/users/{id}", userId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(userRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.firstName").value("Siarhei"))
                    .andExpect(jsonPath("$.lastName").value("Kavaleu"))
                    .andDo(print());

            verify(userService).update(userId, userRequestDto);
        }

        @Test
        @SneakyThrows
        void update_whenUserNotFound() {
            when(userService.update(userId, userRequestDto))
                    .thenThrow(new UserNotFoundException(userId));

            mockMvc.perform(put("/users/{id}", userId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(userRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, userId)))
                    .andDo(print());

            verify(userService).update(userId, userRequestDto);
        }
    }

    @Test
    @SneakyThrows
    void delete_user() {
        when(userService.delete(userId))
                .thenReturn(String.format(SUCCESSFULLY_DELETED, userId));

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format(SUCCESSFULLY_DELETED, userId)))
                .andDo(print());

        verify(userService).delete(userId);
    }

    @Nested
    class AddSubscription {
        @Test
        @SneakyThrows
        void addSubscription_whenAllOk() {
            when(userService.addSubscription(userId, subscriptionId))
                    .thenReturn(String.format(SUBSCRIPTION_ADDED, userRequestDto.getFirstName(),
                                              userRequestDto.getLastName(), subscriptionResponseDto.getDigitalServiceName()));

            mockMvc.perform(post("/users/{id}/subscriptions", userId)
                                    .param("subId", subscriptionId))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.format(SUBSCRIPTION_ADDED, userRequestDto.getFirstName(),
                                                              userRequestDto.getLastName(), subscriptionResponseDto.getDigitalServiceName())))
                    .andDo(print());

            verify(userService).addSubscription(userId, subscriptionId);
        }

        @Test
        @SneakyThrows
        void addSubscription_whenUserNotFound() {
            when(userService.addSubscription(userId, subscriptionId))
                    .thenThrow(new UserNotFoundException(userId));

            mockMvc.perform(post("/users/{id}/subscriptions", userId)
                                    .param("subId", subscriptionId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, userId)))
                    .andDo(print());

            verify(userService).addSubscription(userId, subscriptionId);
        }

        @Test
        @SneakyThrows
        void addSubscription_whenSubscriptionNotFound() {
            when(userService.addSubscription(userId, subscriptionId))
                    .thenThrow(new SubscriptionNotFoundException(subscriptionId));

            mockMvc.perform(post("/users/{id}/subscriptions", userId)
                                    .param("subId", subscriptionId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value(String.format(SUBSCRIPTION_NOT_FOUND_MESSAGE, subscriptionId)))
                    .andDo(print());

            verify(userService).addSubscription(userId, subscriptionId);
        }
    }

    @Nested
    class GetUserSubscriptions {
        @Test
        @SneakyThrows
        void getUserSubscriptions_whenUserFound() {
            when(userService.getUserSubscriptions(userId))
                    .thenReturn(subscriptions);

            mockMvc.perform(get("/users/{id}/subscriptions", userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].digitalServiceName").value("Netflix"))
                    .andExpect(jsonPath("$[1].digitalServiceName").value("Youtube"))
                    .andDo(print());

            verify(userService).getUserSubscriptions(userId);
        }

        @Test
        @SneakyThrows
        void getUserSubscriptions_whenUserNotFound() {
            when(userService.getUserSubscriptions(userId))
                    .thenThrow(new UserNotFoundException(userId));

            mockMvc.perform(get("/users/{id}/subscriptions", userId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, userId)))
                    .andDo(print());

            verify(userService).getUserSubscriptions(userId);
        }
    }

    @Nested
    class removeSubscription {
        @Test
        @SneakyThrows
        void removeSubscriptionFromUser_whenAllOk() {
            when(userService.removeSubscriptionFromUser(userId, subscriptionId))
                    .thenReturn(String.format(SUBSCRIPTION_REMOVED, userRequestDto.getFirstName(),
                                              userRequestDto.getLastName(), subscriptionResponseDto.getDigitalServiceName()));

            mockMvc.perform(delete("/users/{id}/subscriptions/{sub_id)}", userId, subscriptionId))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.format(SUBSCRIPTION_REMOVED,
                                                              userRequestDto.getFirstName(),
                                                              userRequestDto.getLastName(),
                                                              subscriptionResponseDto.getDigitalServiceName())))
                    .andDo(print());

            verify(userService).removeSubscriptionFromUser(userId, subscriptionId);
        }

        @Test
        @SneakyThrows
        void removeSubscriptionFromUser_whenUserNotFound() {
            when(userService.removeSubscriptionFromUser(userId, subscriptionId))
                    .thenThrow(new UserNotFoundException(userId))
            ;
            mockMvc.perform(delete("/users/{id}/subscriptions/{sub_id)}", userId, subscriptionId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value(String.format(USER_NOT_FOUND_MESSAGE, userId)))
                    .andDo(print());

            verify(userService).removeSubscriptionFromUser(userId, subscriptionId);
        }

        @Test
        @SneakyThrows
        void removeSubscriptionFromUser_whenSubscriptionNotFound() {
            when(userService.removeSubscriptionFromUser(userId, subscriptionId))
                    .thenThrow(new SubscriptionNotFoundException(subscriptionId));

            mockMvc.perform(delete("/users/{id}/subscriptions/{sub_id)}", userId, subscriptionId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value(String.format(SUBSCRIPTION_NOT_FOUND_MESSAGE, subscriptionId)))
                    .andDo(print());

            verify(userService).removeSubscriptionFromUser(userId, subscriptionId);
        }
    }
}