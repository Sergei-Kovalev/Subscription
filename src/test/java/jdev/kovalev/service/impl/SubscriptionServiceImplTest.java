package jdev.kovalev.service.impl;

import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.entity.Subscription;
import jdev.kovalev.entity.User;
import jdev.kovalev.mapper.SubscriptionMapper;
import jdev.kovalev.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository repository;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionServiceImpl service;

    @Test
    void getTop() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        List<Subscription> subscriptions = List.of(
                Subscription.builder()
                        .subscriptionId(uuid1)
                        .digitalServiceName("Netflix")
                        .users(Set.of(User.builder().build()))
                        .build(),
                Subscription.builder()
                        .subscriptionId(uuid2)
                        .digitalServiceName("YouTube")
                        .users(Set.of(User.builder().build()))
                        .build());

        List<SubscriptionResponseDto> expected = List.of(
                SubscriptionResponseDto.builder()
                        .subscriptionId(uuid1)
                        .digitalServiceName("Netflix")
                        .build(),
                SubscriptionResponseDto.builder()
                        .subscriptionId(uuid2)
                        .digitalServiceName("YouTube")
                        .build());

        when(repository.findTop())
                .thenReturn(subscriptions);
        when(subscriptionMapper.toDto(any(Subscription.class))).thenAnswer(invocation -> {
            Subscription subscription = invocation.getArgument(0);
            return SubscriptionResponseDto.builder()
                    .subscriptionId(subscription.getSubscriptionId())
                    .digitalServiceName(subscription.getDigitalServiceName())
                    .build();
        });

        List<SubscriptionResponseDto> actual = service.getTop();

        assertThat(actual)
                .isNotEmpty()
                .hasSameElementsAs(expected)
                .hasSameElementsAs(expected);
    }
}