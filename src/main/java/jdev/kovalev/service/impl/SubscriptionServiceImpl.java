package jdev.kovalev.service.impl;

import jdev.kovalev.mapper.SubscriptionMapper;
import jdev.kovalev.dto.response.SubscriptionResponseDto;
import jdev.kovalev.repository.SubscriptionRepository;
import jdev.kovalev.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    final SubscriptionMapper subscriptionMapper;

    @Override
    public List<SubscriptionResponseDto> getTop() {
        return subscriptionRepository.findTop().stream()
                .map(subscriptionMapper::toDto)
                .toList();
    }
}
