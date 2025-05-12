package jdev.kovalev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.exception.handler.ControllersExceptionHandler;
import jdev.kovalev.service.SubscriptionService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private List<SubscriptionResponseDto> subscriptions;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(subscriptionController)
                .setControllerAdvice(new ControllersExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        subscriptions = List.of(
                SubscriptionResponseDto.builder()
                        .subscriptionId(UUID.randomUUID())
                        .digitalServiceName("Netflix")
                        .build(),
                SubscriptionResponseDto.builder()
                        .subscriptionId(UUID.randomUUID())
                        .digitalServiceName("Youtube")
                        .build());
    }

    @Test
    @SneakyThrows
    void getTop() {
        when(subscriptionService.getTop())
                .thenReturn(subscriptions);

        mockMvc.perform(get("/subscriptions/top"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(subscriptions.size()))
                .andExpect(jsonPath("$[0].digitalServiceName").value("Netflix"))
                .andExpect(jsonPath("$[1].digitalServiceName").value("Youtube"))
                .andDo(print());

        verify(subscriptionService).getTop();
    }
}