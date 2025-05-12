package jdev.kovalev.mapper;

import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.entity.Subscription;
import jdev.kovalev.entity.User;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SubscriptionMapperTest {

    private final SubscriptionMapper mapper = new SubscriptionMapperImpl();

    @Test
    void toDto() {
        UUID uuid = UUID.randomUUID();
        SubscriptionResponseDto expected = SubscriptionResponseDto.builder()
                .subscriptionId(uuid)
                .digitalServiceName("Netflix")
                .build();

        Subscription subscription = Subscription.builder()
                .subscriptionId(uuid)
                .digitalServiceName("Netflix")
                .users(new HashSet<>())
                .users(Set.of(User.builder().build()))
                .build();

        SubscriptionResponseDto actual = mapper.toDto(subscription);

        assertThat(actual).isEqualTo(expected);
    }
}